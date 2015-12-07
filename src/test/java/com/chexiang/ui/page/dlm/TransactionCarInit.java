package com.chexiang.ui.page.dlm;

public class TransactionCarInit extends DlmPage {

	@Override
	protected void initMenu() {
		menu("客户管理", "实销上报");
	}

	public void refund() {
		initMenu();
		webElement("invoiceNumber").setParam("发票编号");
		webElement("searchTransactionCar").click();
		webElement("实销上报复选框", caseParam.get("发票编号")).select();
		webElement("退车按钮").click();
		webElement("弹窗提示", "confirm").Assert("确认进行退车吗？");
		webElement("okDopConfirmButton").click();
		webElement("弹窗提示", "alert").Assert("退车成功");
		webElement("okDopAlertButton").click();
	}
}
