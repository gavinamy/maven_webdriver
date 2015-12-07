package com.chexiang.ui.page.dlm;

import com.chexiang.ui.page.dlm.scene.Operator;

public class Exhibition extends DlmPage {

	@Override
	protected void initMenu() {
		menu("展厅接待");
	}

	public void receive(Operator sale) {
		initMenu();
		webElement("新建客户").click();
		sleep();
		webElement("customerNames").setParam("潜客名称");
		select("servantPosts").setValue(sale.getRole());
		select("servantNames").setValue(sale.getName());
		webElement("arriveTimes").setValue("10:00");
		webElement("leavieTimes").setValue("11:00");
		select("arriveTypes").setValue("展厅接待");
		select("sta").setValue("有效");
		webElement("arrivePeopleCounts").setValue("3");
		webElement("customerTels").setParam("手机号码");
		select("arriveCounts").setRandom();
		webElement("remarks").setValue("...");
		webElement("searchExhibitionBtn").click();
		webElement("弹窗提示", "confirm").Assert("确认提交吗？");
		webElement("okDopConfirmButton").click();
		webElement("弹窗提示", "alert").Assert("展厅流量添加成功!");
		webElement("okDopAlertButton").click();
	}
}
