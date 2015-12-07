package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.BasePage;

//这是rsm的页面
public class RsmPage extends BasePage {

	@Override
	public void close() {
		this.qutiIframe();
		webElement("tabClose").click();
	}

	@Override
	public void menu(String child) {
		webElement("二级菜单", child).click();
	}

	@Override
	public void menu(String father, String child) {
		webElement("一级菜单", father).click();
		this.menu(child);
	}

	protected DateSelect date(String key) {
		return new DateSelect(key);
	}

	protected class DateSelect extends BaseElement {
		public DateSelect(String key) {
			super(key);
		}

		@Override
		public void click() {
			getMethodLog();
			element.click();
			webElement("当前时间").click();
		}
	}

	protected EditSelect edit(String key) {
		return new EditSelect(key);
	}

	protected class EditSelect extends BaseElement {
		public EditSelect(String key) {
			super(key);
		}

		@Override
		public void setValue(String value) {
			super.setValue(value);
			element.click();
			webElement("EditSelectOptions", value).click();
		}
	}
}