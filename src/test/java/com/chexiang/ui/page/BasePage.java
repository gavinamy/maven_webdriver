package com.chexiang.ui.page;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.chexiang.core.ParameterUtil;
import com.chexiang.ui.cases.UiCase;

/**
 * @ClassName: BasePage
 * @Description: 页面基类，提供页面操作的一系列方法
 * @author 手慢无
 * @date 2015-08-03
 */
public abstract class BasePage {
	// 上传图片目录
	protected static final String IMG_FILE = Class.class.getClass().getResource("/img/testImg.jpg").getPath()
			.replaceFirst("/", "").replaceAll("/", "\\\\");
	// 实例化类的父类，构造函数初始化
	private Class superClass;
	// 数据库连接池，采用单例模式，每个系统一个连接，构造函数初始化
	// 登记式单例模式
	private static final HashMap<Class, Connection> dbConnect = new HashMap<Class, Connection>();
	private ResultSet rs;// 无需初始化
	// 由于一个case有可能调用到多个系统，每个系统的公共对象库有可能会有冲突，
	// 因此，每个系统的公共对象库采用登记式单例模式彼此分开
	private static final HashMap<Class, Properties> pubRepositories = new HashMap<Class, Properties>();;
	// 对象库，构造函数初始化
	private Properties elementRepository;
	// 公共对象数据
	// private static HashMap<Class, CSVRecord> pubPageData = new
	// HashMap<Package, CSVRecord>();
	// 页面数据
	protected CSVRecord pageData;
	// 本case的引用，方便方法体中直接调用case参数
	public static UiCase uiCase;

	protected ParameterUtil paraUtil;

	// driver:浏览器对象;actions:特殊操作方法集
	protected static WebDriver driver;
	protected static Actions actions;

	private Random random;
	// case组件之间的变量，通常该数据来自于运行时，例如业务流水号等
	// 由于该变量必须贯穿多个page，因此必须由case管理
	// 该变量指向uiCase的变量，在构造方法中，该变量直接被赋值为uiCase相应参数的引用
	protected HashMap<String, String> caseParam;
	// 原始数据变量，从数据驱动读取该些数据，该类型数据一经读取，在运行时不可改变
	// protected ImmutableMap<String, String> caseData;

	protected SimpleDateFormat dateFormat;
	protected Calendar rightNow;

	protected Logger logger;

	public BasePage() {
		superClass = this.getClass().getSuperclass();

		elementRepository = new Properties();
		paraUtil = new ParameterUtil();
		caseParam = uiCase.caseParam;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		random = new Random();
		rightNow = Calendar.getInstance();
		logger = LogManager.getLogger(this.getClass().getSimpleName());
		// 暂时无需数据驱动
		// caseData = uiCase.caseData;
		this.loadElementRepository();
	}

	/**
	 * @Title: openBrowser
	 * @Description: 打开浏览器
	 * @param browser
	 * @return void
	 * @date 2015-07-29
	 * 
	 *       设置浏览器类型，并最大化 。 （该静态方法在BeforeMethod中调用，先于构造方法执行）
	 */

	public void openBrowser(String browser) {
		String browserPath = "/com/chexiang/ui/";
		switch (browser) {
		case "FF":
			driver = new FirefoxDriver((new ProfilesIni()).getProfile("default"));
			break;
		case "IE":
			System.setProperty("webdriver.ie.driver",
					getClass().getResource(browserPath + "IEDriverServer.exe").getPath());
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
			break;
		case "CH":
			System.setProperty("webdriver.chrome.driver",
					getClass().getResource(browserPath + "chromedriver.exe").getPath());
			driver = new ChromeDriver();
			break;
		default:
			throw new NullPointerException("错误的浏览器类型");
		}
		actions = new Actions(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(pageData.get("uri"));
	}

	/**
	 * @Title: destroy
	 * @Description: 销毁浏览器
	 * @param void
	 * @return void
	 * @date 2015-07-29
	 */
	public static void destroy() {
		driver.switchTo().defaultContent();
		driver.quit();
	}

	protected abstract void close();

	/**
	 * @Title: menu
	 * @Description: 菜单
	 * @param father,child
	 * @return void
	 * @date 2015-07-29
	 */
	protected abstract void menu(String child);

	public abstract void menu(String father, String child);

	/**
	 * @Title: loadElementRepository
	 * @Description: 加载对象库
	 * @param void
	 * @return void
	 * @date 2015-07-29
	 */
	private void loadElementRepository() {
		Properties publicRepository;
		try {
			// 1.装载公共对象库
			// 用基类名进行单例登记
			if (!pubRepositories.containsKey(superClass)) {
				publicRepository = new Properties();
				publicRepository.load(new InputStreamReader(
						new FileInputStream(getClass()
								.getResource(getClass().getSuperclass().getSimpleName() + ".properties").getFile()),
						"UTF-8"));
				pubRepositories.put(superClass, publicRepository);
				publicRepository = null;
			}
			// 2.装载私有对象库
			// 对象库配置文件在class文件同一目录，仅扩展名不同
			elementRepository.load(new InputStreamReader(
					new FileInputStream(getClass().getResource(getClass().getSimpleName() + ".properties").getFile()),
					"UTF-8"));

			// 私有对象库和公共对象库进行key比对，及时警报
			Set pubKeys = ((Properties) pubRepositories.get(superClass).clone()).keySet();
			pubKeys.retainAll(elementRepository.keySet());
			if (0 != pubKeys.size()) {
				logger.warn(getClass().getSimpleName() + "私有对象库与公共对象库" + superClass + "冲突，交集为："
						+ "、" +pubKeys);

				logger.warn(getClass().getSimpleName() + "私有对象库与公共对象库" + superClass + "冲突，交集为：");


			}

		} catch (Exception e) {
			logger.warn(e.getStackTrace()[0].getMethodName() + "!文件未找到");
		}
		// 3.读取数据驱动
		pageData = uiCase.csv2Map(getClass().getResource(getClass().getSimpleName() + ".csv").getFile()).next();
	}

	/**
	 * @Title: sleep
	 * @Description: 进程休眠
	 * @param void
	 * @return void
	 * @date 2015-07-29
	 */
	protected void sleep(int mSecends) {
		try {
			Thread.sleep(mSecends);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void sleep() {
		sleep(500);
	}

	/**
	 * @Title: ElementExist
	 * @Description: 判断对象是否存在
	 * @param void
	 * @return void
	 * @date 2015-07-29
	 * 
	 *       该方法通过全局时间判定，因此耗时较长，不建议频繁使用
	 */
	protected boolean ElementExist(By Locator) {
		try {
			driver.findElement(Locator);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			return false;
		}
	}

	/**
	 * @Title: linkToMysql
	 * @Description: 连接mysql数据库
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 */
	private void linkToMysql() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 每个系统只有一个数据库，以超类作为索引，登记式单例
			if (!dbConnect.containsKey(superClass)) {
				dbConnect.put(superClass,
						DriverManager.getConnection(this.pubRepositories.get(superClass).getProperty("dburi")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: closeDb
	 * @Description: 关闭当前数据库
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 */
	public void closeDb() {
		try {
			dbConnect.get(superClass).close();
		} catch (Exception e) {
			logger.trace("数据库可能未打开，无需关闭，错误日志：" + e.getMessage());
		}
	}

	/**
	 * @Title: dbQuery
	 * @Description: 执行sql查询
	 * @param sql
	 * @return void
	 * @date 2015-08-03
	 */
	public void dbQuery(String sql) {
		try {
			linkToMysql();
			rs = dbConnect.get(superClass).createStatement().executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @Title: dbExecute
	 * @Description: 批量执行增删改
	 * @param sqls
	 * @return void
	 * @date 2015-08-03
	 */
	public void dbExecute(String... sqls) {

		linkToMysql();

		Statement stmt;
		try {
			stmt = dbConnect.get(superClass).createStatement();
			for (String sql : sqls) {
				stmt.addBatch(sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @Title: getSimpleResult
	 * @Description: 获取简单查询结果
	 * @param void
	 * @return String
	 * @date 2015-08-03
	 * 
	 *       返回查询结果的第一个记录的第一个字段（一般用于仅有一个字段的查询）
	 */
	public String getSimpleResult() {
		String queryData = null;

		try {
			rs.first();
			queryData = rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryData;
	}

	/**
	 * @Title: resultSet2Maps
	 * @Description: 将查询结果转换成Map列表
	 * @param void
	 * @return ArrayList<HashMap<String, Object>>
	 * @date 2015-08-03
	 */
	public ArrayList<HashMap<String, Object>> resultSet2Maps() {
		ArrayList<HashMap<String, Object>> queryData = null;
		HashMap<String, Object> row = new HashMap<String, Object>();
		try {
			while (rs.next()) {
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.put(rs.getMetaData().getCatalogName(i), rs.getObject(i));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryData;
	}

	/**
	 * @Title: qutiIframe
	 * @Description: 跳回最外层html
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 */
	protected void qutiIframe() {
		driver.switchTo().defaultContent();
	}

	/**
	 * @Title: DbAssert
	 * @Description: 数据库验证
	 * @param void
	 * @return void
	 * @date 2015-08-03
	 */
	protected void DbAssert(String sql, String expected) {
		dbQuery(sql);
		Assert.assertEquals(getSimpleResult(), expected);
	}

	/**
	 * @param key,parameters
	 * @return BaseElement
	 * 
	 *         通过键和参数获取页面控件对象
	 */
	protected BaseElement webElement(String key) {
		return new BaseElement(key);
	}

	protected BaseElement webElement(String key, String... parameters) {
		return new BaseElement(key, parameters);
	}

	/**
	 * @ClassName: BasePage
	 * @Description: 页面控件基类，提供封装好的控件对象
	 * @author 手慢无
	 * @date 2015-08-03
	 */
	protected class BaseElement {

		protected WebElement element;
		protected String key;

		public BaseElement() {
		}

		/**
		 * @param elementKey
		 * 
		 *            仅通过key查找对象：查找顺序：公共对象库、私有对象库、页面id或name
		 */
		public BaseElement(String elementKey) {
			sleep();
			key = elementKey.trim();
			String xpath;
			if (pubRepositories.get(superClass).containsKey(key)) {
				xpath = pubRepositories.get(superClass).getProperty(key);
			} else if (elementRepository.containsKey(key)) {
				xpath = elementRepository.getProperty(key);
			} else {
				xpath = ".//*[@id='" + key + "' or @name='" + key + "']";
			}
			element = driver.findElement(By.xpath(xpath));
			// if (!element.isEnabled()) {
			// logger.error("控件：" + key + "被置灰，无法操作");
			// }
		}

		/**
		 * @param elementKey
		 * 
		 *            仅通过key和parameters查找对象
		 */
		public BaseElement(String elementKey, String... parameters) {

			sleep();
			key = elementKey.trim();
			// 先从公共对象库查找，随后查找私有对象库
			String xpath = pubRepositories.get(superClass).containsKey(key)
					? pubRepositories.get(superClass).getProperty(key) : elementRepository.getProperty(key);
			// 循环替换插值
			for (String parameter : parameters) {
				xpath = xpath.replaceFirst("\\$\\{((?!\\$).)*\\}", parameter);
			}
			element = driver.findElement(By.xpath(xpath));
			// if (!element.isEnabled()) {
			// logger.error("控件：" + key + "被置灰，无法操作");
			// }
		}

		public WebElement getElement() {
			return element;
		}

		public void click() {
			getMethodLog();
			this.element.click();
		}

		public void setValue(String value) {
			getMethodLog(value);
			this.element.clear();
			this.element.sendKeys(value);
		}

		public void setDriverData() {
			setValue(pageData.get(key));
		}

		public void setDriverData(String driverKey) {
			setValue(pageData.get(driverKey));
		}

		public void setParam(String paraKey) {
			setValue(caseParam.get(paraKey));
		}

		public void select() {
			while (!element.isSelected()) {
				element.click();
			}
		}

		public void putParam(String paraKey) {
			getMethodLog(paraKey + ":" + element.getText());
			caseParam.put(paraKey, element.getText());
		}

		public void putParam(String paraKey, String attribute) {
			getMethodLog(paraKey + ":" + element.getAttribute(attribute));
			caseParam.put(paraKey, element.getAttribute(attribute));
		}

		public void mouseMoveOn() {
			actions.moveToElement(element).perform();
			actions.moveByOffset(0, 1);
		}

		public void focuse() {
			actions.contextClick(element).perform();
		}

		public boolean isShowText(String expected) {
			getMethodLog(expected);
			sleep();
			return element.getText().contains(expected);
		}

		public boolean isDisPlay() {
			return element.getAttribute("style").contains("block");
		}

		public void Assert(String attribute, String expected) {
			// Assert.assertEquals("", "");
			getMethodLog(expected);
		}

		public void Assert(String expected) {
			getMethodLog(expected);
			sleep();
			Assert.assertEquals(element.getText().trim(), expected);
		}

		protected void getMethodLog() {
			logger.trace(this.getClass().getSimpleName() + ":" + key + ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName());
		}

		protected void getMethodLog(String value) {
			logger.trace(this.getClass().getSimpleName() + ":" + key + ":"
					+ Thread.currentThread().getStackTrace()[2].getMethodName() + ":" + value);
		}
	}

	//
	// protected Table table(String key) {
	//
	// }
	protected NormalSelect select(String key) {
		sleep();
		return new NormalSelect(key);
	}

	protected class NormalSelect extends BaseElement {
		private Select select;

		public NormalSelect(String key) {
			super(key);
			select = new Select(element);
		}

		@Override
		public void setValue(String value) {
			getMethodLog(value);
			select.selectByVisibleText(value);
		}

		@Override
		public void setParam(String paraKey) {
			setValue(caseParam.get(paraKey));
		}

		@Override
		public void setDriverData() {
			setValue(pageData.get(key));
		}

		public void setIndex(int index) {
			getMethodLog(String.valueOf(index));
			select.selectByIndex(index);
		}

		public void setRandom(String... excludes) {
			int option;
			do {
				option = random.nextInt(select.getOptions().size());
			} while (Arrays.asList(excludes).contains(select.getOptions().get(option).getText()));
			setIndex(option);
		}

		public void setRandom() {
			setRandom("请选择");
		}

		public void setLastOption() {
			setIndex(select.getOptions().size() - 1);
		}
	}

	protected IFrame iFrame(String key) {
		return new IFrame(key);
	}

	protected class IFrame extends BaseElement {
		public IFrame(String key) {
			super(key);
		}

		public void switchIn() {
			driver.switchTo().frame(element);
		}
	}

	// protected JsSelect jsSelect(String key) {
	// return new JsSelect(key);
	// }

	// protected class JsSelect extends BaseElement {
	// public JsSelect(String key) {
	// super(key);
	// }
	//
	// public void select(String option) {
	// element.click();
	// driver.findElement(By.xpath(".//*[contains(@id,'select2-drop')]//div[contains(.,'"
	// + option + "')]"))
	// .click();
	// getMethodLog();
	// }
	// }

	protected AlertWindow alert() {
		sleep();
		return new AlertWindow();
	}

	protected class AlertWindow extends BaseElement {
		private Alert alert;

		public AlertWindow() {
			alert = driver.switchTo().alert();
		}

		public void ok() {
			getMethodLog();
			sleep();
			alert.accept();
			sleep();
		}

		public void cancel() {
			getMethodLog();
			sleep();
			alert.dismiss();
			sleep();
		}

		@Override
		public void click() {
			getMethodLog();
			ok();
		}

		public void click(Boolean flag) {
			getMethodLog();
			if (flag) {
				ok();
			} else {
				cancel();
			}
		}

		@Override
		public void Assert(String expected) {
			getMethodLog(expected);
			Assert.assertEquals(alert.getText(), expected);
		}
	}
}