package com.chexiang.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.google.common.collect.ImmutableMap;

/**
 * @ClassName: BaseCase
 * @Description: 所有Case的基类
 * @author 手慢无
 * @date 2015-09-24
 */
public abstract class BaseCase {
	// 项目文件夹
	protected static final File FILEROOT = (new File(Class.class.getClass().getResource("/").getPath()).getParentFile()
			.getParentFile());
	// 全局环境参数，例如url等，由于其全局特性，必须设置成static
	public ImmutableMap<String, String> GlobalConst;
	// 原始数据变量，从数据驱动读取该些数据，该类型数据一经读取，在运行时不可改变
	// public HashMap caseData;
	// case组件之间的变量，通常该数据来自于运行时，例如业务流水号等
	public HashMap<String, String> caseParam = new HashMap<String, String>();

	protected Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	private Document xDoc;
	private Element xRoot, xNode;

	/**
	 * @Title: tearDown
	 * @Description: 销毁方法
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 * 
	 *       该方法提供AfterSuite统一入口供testNG调用，具体实现委托给子类完成
	 */
	@AfterSuite
	protected void tearDown() {
		end();
	}

	/**
	 * @Title: end
	 * @Description: 销毁方法
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 * 
	 *       抽象方法，由子类实现，被tearDown调用
	 */
	protected abstract void end();

	/**
	 * @Title: setUp
	 * @Description: 初始化方法
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 * 
	 *       该方法提供BeforeSuite统一入口供testNG调用，具体实现委托给子类完成
	 */
	@BeforeSuite
	protected void setUp() {
		// 全局常量
		GlobalConst = ImmutableMap.copyOf((Map) readConst());
		start();
	}

	/**
	 * @Title: start
	 * @Description: 初始化方法
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 * 
	 *       抽象方法，由子类实现，被setUp调用
	 */
	protected abstract void start();

	/**
	 * @Title: csv2Map
	 * @Description:读取csv数据驱动
	 * @param csvFileName
	 * @return Iterator<CSVRecord>
	 * @date 2015-08-06
	 */
	public Iterator<CSVRecord> csv2Map(String csvFileName) {
		CSVParser parser = null;
		try {
			parser = new CSVParser(new FileReader(csvFileName), CSVFormat.EXCEL.withHeader());
		} catch (IOException e) {
			logger.error("csv文件" + csvFileName + "读取错误！");
			e.printStackTrace();
		}
		return parser.iterator();
	}

	/**
	 * @Title: readConst
	 * @Description:读取每个case的常量文件
	 * @param void
	 * @return void
	 * @date 2015-08-04
	 * 
	 *       每个case都代表一个项目的用例集 每个项目都有一些统一的配置参数，例如url和数据库连接字符串
	 *       这些常量放在const.properties中
	 */

	private Properties readConst() {
		Properties properties = new Properties();
		try {
			// 加载各项目下的资源文件，个项目的常量资源文件都应该在case目录下
			properties.load(new FileInputStream(getClass().getResource("const.properties").getFile()));
		} catch (IOException e) {
			logger.error("未在路径" + getClass().getResource("") + "下找到资源文件" + "const.properties");
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * @Title: XmlLoad
	 * @Description:dom方式解析xml文件
	 * @param fileUri
	 * @return void
	 * @date 2015-08-27
	 */
	public void XmlLoad(String fileUri) {

		SAXBuilder builder = new SAXBuilder();
		try {
			xDoc = builder.build(fileUri);
			xRoot = xDoc.getRootElement();
		} catch (JDOMException | IOException e) {
			logger.error("文件" + fileUri + "解析失败");
			e.printStackTrace();
		}
	}

	/**
	 * @Title: XmlEdit
	 * @Description:编辑xmldom
	 * @param value,xpath:用","分割为数组，而非"/"连接的字符串
	 * @return void
	 * @date 2015-08-27
	 */
	public void XmlEdit(String value, String... xpath) {
		xNode = xRoot;
		for (String path : xpath) {
			xNode = xNode.getChild(path);
		}
		xNode.setText(value);
	}

	/**
	 * @Title: Xml2String
	 * @Description:xmldom转换为字符串
	 * @param void
	 * @return String
	 * @date 2015-08-27
	 */
	public String Xml2String() {
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			new XMLOutputter(format).output(xDoc, bo);
		} catch (IOException e) {
			logger.error("xml反序列化错误");
			e.printStackTrace();
		}
		return bo.toString();
	}

	/**
	 * @Title: post
	 * @Description:向服务器发送报文，并获取返回值
	 * @param uri,para
	 * @return String:response
	 * @date 2015-07-27
	 */
	public String post(String uri, String request) {
		logger.trace("开始发送post请求：url：" + uri + "\n" + "request：" + request);
		String response = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(uri);
			StringEntity entity = null;

			try {
				entity = new StringEntity(request);
				entity.setContentType("text/xml");
				entity.setContentEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			httppost.setEntity(entity);

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			try {
				response = httpclient.execute(httppost, responseHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response;
		}
	}
}
