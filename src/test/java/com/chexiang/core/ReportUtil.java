package com.chexiang.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ReportUtil {

	private HashMap<String, Object> testSuit;
	private HashMap<String, Object> testCase;
	private HashMap<String, Object> checkPoint;
	private ArrayList<HashMap> caseList;
	private ArrayList<HashMap> checkList;

	private ParameterUtil parameter;
	private Configuration cfg;
	private Writer writer;

	private boolean checkPass, casePass;

	private File reportFile, templateFile;
	private org.apache.logging.log4j.Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	/**
	 * @ClassName: ReportUtil
	 * @Description: 对字符串或外部文本进行处理，参数绑定等
	 * @author 手慢无
	 * @date 2015-09-24
	 */
	private ReportUtil() {
		testSuit = new HashMap<String, Object>();
		caseList = new ArrayList<HashMap>();
		parameter = new ParameterUtil();
		cfg = new Configuration();
		this.cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setSharedVariable("random", new RandomMethod());
	}

	// 饿汉式
	private static final ReportUtil report = new ReportUtil();

	/**
	 * @Title:getInstance
	 * @Description: 初始化单例模式
	 */
	public static ReportUtil getInstance() {
		return report;
	}

	/**
	 * @param file
	 * 
	 *            设置reportFile:report目标文件夹
	 */
	public void setReportFile(File file) {
		reportFile = file;
	}

	/**
	 * @param file
	 * 
	 *            设置templateFile:report模板文件夹
	 * 
	 *            加载模板路径和变量 在接口测试中，大部分情况是，模板几乎都放在一个总目录中，插值变量对象也是共用一个
	 */
	public void setTemplateFile(File file) {
		// 模板文件路径为：core文件夹
		templateFile = file;
		try {
			// 通过Freemarker的Configuration读取相应的ftl
			// 设定去哪里读取相应的ftl模板文件
			cfg.setDirectoryForTemplateLoading(templateFile);
		} catch (IOException e) {
			logger.warn("找不到文件夹：" + templateFile.getPath());
			e.printStackTrace();
		}
	}

	public void setTemplateFile() {
		setTemplateFile(FileUtils.getFile(getClass().getResource("").getFile()));
	}

	/**
	 * @param name
	 * 
	 *            testsuit初始化：1.命名;2.创建caseList
	 */
	public void setSuitName(String name) {
		testSuit.put("name", name);
		testSuit.put("caseList", caseList);
	}

	/**
	 * @param className,methodName,indexName
	 * 
	 *            testsuit初始化：1.命名;2.创建checkList;3.将自己加入当前suit的caseList
	 */
	public void setCaseName(String className, String methodName, String indexName) {
		if (null != testCase) {
			// 分支：如果不是第一个case，则先生成前一个case的report
			createReportParticulars();
		}
		casePass = true;
		testCase = new HashMap<String, Object>();
		checkList = new ArrayList<HashMap>();
		testCase.put("checkList", checkList);
		testCase.put("class", className);
		testCase.put("method", methodName);
		testCase.put("index", indexName);
		testCase.put("link", className + "." + methodName + "." + indexName + ".htm");
		caseList.add(testCase);
	}

	/**
	 * @param name
	 * 
	 *            testsuit初始化：1.命名;2.将自己加入当前suit的checkList
	 */
	public void setCheckName(String name) {
		checkPoint = new HashMap<String, Object>();
		checkPoint.put("name", name);
		checkList.add(checkPoint);
	}

	/**
	 * @Title: verify
	 * @Description: 检查点验证
	 * @param act：实际值,exp：预期值
	 * @return void
	 * @date 2015-08-27
	 */
	public void verify(String act, String exp) {
		checkPoint.put("act", act);
		checkPoint.put("exp", exp);
		checkPass = act.equals(exp);
		casePass &= checkPass;
		checkPoint.put("pass", checkPass);
	}

	public void fail(String msg) {
		checkPass = false;
		casePass = false;
		checkPoint.put("pass", checkPass);
		checkPoint.put("remark", msg);
	}

	/**
	 * @Title: createReportIndex
	 * @Description: 生成测试报告index
	 * @param void
	 * @return void
	 * @date 2015-09-10
	 */
	public void createReportIndex() {

		// 每次setCaseName会触发上一case的report操作，因此，最后一次无法自动触发，
		// 须由indexReport触发
		createReportParticulars();

		// 开始生成index文件
		try {
			writer = new FileWriter(FileUtils.getFile(reportFile, "index.htm"));
			cfg.getTemplate("indexReportTemplate.ftl").process(testSuit, writer);
			writer.flush();
		} catch (IOException | TemplateException e) {
			logger.error("读取模板：indexReportTemplate.ftl发生错误！");
			e.printStackTrace();
		}

	}

	/**
	 * @Title: createReportParticulars
	 * @Description: 生成详细case测试报告
	 * @param void
	 * @return void
	 * @date 2015-09-10
	 */
	private void createReportParticulars() {
		testCase.put("pass", casePass);
		File particularFile = FileUtils.getFile(reportFile, "particulars");
		try {
			// 定位writer输出路径
			writer = new FileWriter(FileUtils.getFile(particularFile, (String) testCase.get("link")));
			cfg.getTemplate("caseReportTemplate.ftl").process(testCase, writer);
			writer.flush();
		} catch (IOException | TemplateException e) {
			logger.error("读取模板：caseReportTemplate.ftl发生错误！");
			e.printStackTrace();
		}
	}

	/**
	 * @ClassName: RandomMethod
	 * @Description: 为freeMarker提供随机字符串的方法模型
	 * @author 手慢无
	 * @date 2015-09-24
	 */
	class RandomMethod implements TemplateMethodModel {
		@Override
		public String exec(List args) throws TemplateModelException {
			return parameter.RandCharAndNum(Integer.parseInt(args.get(0).toString()),
					Integer.parseInt(args.get(1).toString()));
		}
	}
	//
	// private class CheckPoint {
	// public CheckPoint(String pName) {
	// name = pName;
	// }
	//
	// public String name;
	// public String exp, actual;
	//
	// public boolean isPass() {
	// return exp.equals(actual);
	// }
	//
	// }
	//
	// private class TestCase {
	// public TestCase(String cName) {
	// name = cName;
	// }
	//
	// public String link;
	// public String name;
	// public ArrayList<CheckPoint> checkList = new ArrayList<CheckPoint>();
	// }
	//
	// private class TestSuit {
	// public String name;
	// public ArrayList<TestCase> caseList = new ArrayList<TestCase>();
	// }
}
