package com.chexiang.ui.cases;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.csv.CSVRecord;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.chexiang.core.BaseCase;
import com.chexiang.ui.page.BasePage;

/**
 * @ClassName: UiCase
 * @Description: ui测试的基类，为ui测试case提供一些公共的方法
 * @author 手慢无
 * @date 2015-09-24
 */
public abstract class UiCase extends BaseCase {

	/**
	 * 基类beforesuit方法
	 * 
	 * ui测试实现：打开浏览器
	 */
	@Override
	protected void start() {
		BasePage.uiCase = this;
	}

	/**
	 * aftermethed
	 * 
	 * 关闭浏览器
	 */
	@AfterMethod
	protected void methodClose() {
		BasePage.destroy();
	}

	// 数据驱动，从csv中读数据驱动
	/**
	 * @Title: uiProvider
	 * @Description: 数据驱动加载器
	 * @param methed:调用此数据驱动的方法名称,context
	 * @return Iterator
	 * @date 2015-08-27
	 */
	@DataProvider(name = "uiProvider")
	public Iterator<CSVRecord> uiProvider(Method methed, ITestContext context) {
		// 资源文件的格式为：测试类名.测试方法名.csv
		return csv2Map(this.getClass().getResource(this.getClass().getSimpleName() + ".csv").getFile());
	}
}
