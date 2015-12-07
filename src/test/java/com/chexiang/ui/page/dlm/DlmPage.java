package com.chexiang.ui.page.dlm;

import com.chexiang.ui.page.BasePage;

public abstract class DlmPage extends BasePage {

	@Override
	public void close() {
		webElement("userName").mouseMoveOn();
		webElement("功能菜单", "退出").click();
	}

	@Override
	public void menu(String father) {
		webElement("一级菜单", father).click();
	}

	@Override
	public void menu(String father, String child) {
		webElement("一级菜单", father).mouseMoveOn();
		webElement("二级菜单", father, child).click();
	}

	protected abstract void initMenu();

	public void initDate() {
		caseParam.put("QQ", paraUtil.RandCharAndNum(10, 0));
		caseParam.put("发票编号", paraUtil.RandCharAndNum(8, 0));
		caseParam.put("手机号码", 1 + caseParam.get("QQ"));
		caseParam.put("潜客名称", paraUtil.RandCharAndNum(5, 9));
		caseParam.put("身份证", caseParam.get("QQ") + caseParam.get("发票编号"));
	}

	/**
	 * 
	 * @Override protected Select select(String key) { return null; }
	 * 
	 *           protected class divSelect extends BaseElement { public
	 *           divSelect(String key) { super(key); } public void select(String
	 *           option) { element.click();
	 * 
	 *           } }
	 */
	protected void failMsg() {
		select("ccreason1").setRandom();
		select("ccreason11").setRandom();
		select("ccreason2").setRandom();
		select("ccreason22").setRandom();
		select("ccbuy1").setRandom();
		webElement("csMark").setValue("剁手毁一生");
		select("ccbuy2").setRandom();
		webElement("submitBtn").click();

		webElement("弹窗提示", "confirm").Assert("您真的要战败这个意向？");
		webElement("okDopConfirmButton").click();
		webElement("弹窗提示", "alert").Assert("保存成功!");
		webElement("okDopAlertButton").click();
	}
}
