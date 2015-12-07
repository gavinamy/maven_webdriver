package com.chexiang.api.cases;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import org.testng.annotations.Test;

import com.chexiang.core.JsonUtil;
import com.chexiang.core.ParameterUtil;
import com.chexiang.core.ReportUtil;
import com.google.gson.JsonObject;

/**
 * @ClassName: VenusApiTest
 * @Description: Venus的接口测试类
 * @author 手慢无
 * @date 2015-09-24
 */
public class VenusApiTest extends ApiCase {
	// 文件夹过滤器
	// private final FileFilter directoryFliter = DirectoryFileFilter.DIRECTORY;
	// 数据驱动文件的根目录
	private final File root = new File(getClass().getResource(getClass().getSimpleName()).getPath());
	private ParameterUtil paraUtil = new ParameterUtil();
	private ReportUtil report = ReportUtil.getInstance();

	private String className, methodName, indexName;

	/**
	 * @Title: execute
	 * @Description: 执行接口测试入口方法
	 * @param void
	 * @return void
	 * @date 2015-09-24
	 */
	@Test
	public void execute() {
		report.setSuitName(getClass().getSimpleName());
		dataStruct();
	}

	/**
	 * @Title: dataStruct
	 * @Description: 循环遍历外部数据存储结构，执行测试方法
	 * @param void
	 * @return void
	 * @date 2015-08-27
	 */
	public void dataStruct() {
		// 过滤器：匹配文件夹
		FileFilter isDir = FileFilterUtils.directoryFileFilter();
		// 过滤器：匹配不以9开头的三位纯数字文件夹
		FileFilter isNormalCase = (FileFilter) FileFilterUtils.and(DirectoryFileFilter.DIRECTORY,
				new RegexFileFilter("^[0-8]\\d{2}$"));
		// 外层文件夹，以Class名命名
		for (File classFile : root.listFiles(isDir)) {
			className = classFile.getName();
			// 第二层，以方法名命名
			for (File methodFile : classFile.listFiles(isDir)) {
				methodName = methodFile.getName();
				// 内层，以编号命名，每一个编号代表一个用例，用例名必须由三位数字组成
				// 如果编号起始数字是9，代表被调用的用例，只能被调用，不能直接执行，所以此类用例也被过滤
				for (File caseIndex : methodFile.listFiles(isNormalCase)) {
					indexName = caseIndex.getName();
					report.setCaseName(className, methodName, indexName);
					// 执行用例
					perform(caseIndex);
				}
			}
		}
	}

	/**
	 * @Title: perform
	 * @Description: 读取case文件，发送request，验证response
	 * @param fCase:case文件夹
	 * @return JsonObject:response对象
	 * @date 2015-08-27
	 * 
	 *       1.读request和response到jsonutil 2.如果有前置，则按照前置号定位前置并执行
	 *       3.执行完毕之后，读取前置response，并过滤request
	 */
	private JsonObject perform(File fCase) {
		JsonUtil currentCase = new JsonUtil();
		String request;
		try {
			// 1.读request和expected到jsonutil；
			currentCase.setRequest(FileUtils.readFileToString(FileUtils.getFile(fCase, "request.js"), "utf-8"));
			currentCase.setExpect(FileUtils.readFileToString(FileUtils.getFile(fCase, "response.js"), "utf-8"));
		} catch (IOException e) {
			logger.error("读文件发生错误");
			e.printStackTrace();
		}
		JsonObject perfix = currentCase.getPerfix(JsonObject.class);
		// 判断是否有前置用例
		if (null != perfix) {
			// 分支：如果有前置
			// 从perfix中提取前置用例的定位
			String perfixPath = perfix.get("port").getAsString() + "/" + perfix.get("method").getAsString() + "/"
					+ perfix.get("caseNo").getAsString();
			// 将request从json形式转化成字符串
			request = currentCase.request2String();
			// 1.递归调用perform，执行前置用例
			// 2.获取前置用例response
			// 3.用前置response的值，替换request中的变量
			request = paraUtil.subsitute(request, perform(FileUtils.getFile(root, perfixPath)));
			// 将处理后的request再转化成json对象
			currentCase.setRequest(request);
		}
		// 发送请求
		currentCase.setActual(
				post(GlobalConst.get(getClass().getSimpleName()) + fCase.getParentFile().getParentFile().getName() + "/"
						+ fCase.getParentFile().getName(), currentCase.request2String()));
		// 验证response
		currentCase.responseAssert();
		// 将response以json形式返回
		return currentCase.getActual(JsonObject.class);
	}

	/**
	 * @Title: end
	 * @Description:生成report
	 * @param void
	 * @return void
	 * @date 2015-08-27
	 * 
	 *       aftersuit调用基类的tearDown方法，基类的tearDown方法调用该方法
	 *       基类中，end方法与start相对应，都是抽象方法，委托给子类自定义实现特定功能
	 */

	@Override
	protected void end() {
		report.createReportIndex();
	}

	/**
	 * @Title: start
	 * @Description:设定report的模板目录和输出目录
	 * @param void
	 * @return void
	 * @date 2015-08-27
	 * 
	 *       beforesuit调用基类的setUp方法，基类的setUp方法调用该方法
	 *       基类中，start方法与end相对应，都是抽象方法，委托给子类自定义实现特定功能
	 */
	@Override
	protected void start() {
		report.setTemplateFile();
		report.setReportFile(FileUtils.getFile(FILEROOT, "test-output", "report"));
	}
}