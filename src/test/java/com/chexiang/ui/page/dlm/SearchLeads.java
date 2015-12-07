package com.chexiang.ui.page.dlm;

import com.chexiang.ui.page.dlm.scene.Operator;

//潜客线索
public class SearchLeads extends DlmPage {
	@Override
	protected void initMenu() {
		menu("潜客线索", "潜客线索");
	}

	public void createLeads() {
		initMenu();
		webElement("createLeadsBtn").click();
		webElement("_potentialCustomerName").setParam("潜客名称");
		webElement("_mobile").setParam("手机号码");
		webElement("_QQ").setParam("QQ");
		select("_mainContactType").setDriverData();
		select("_firstResourceName").setDriverData();
		select("_secondResourceName").setDriverData();
		webElement("createCardBtn").click();
		webElement("弹窗提示","alert").Assert("保存成功");
		webElement("okDopAlertButton").click();
	}

	public void assign(Operator sale) {
		initMenu();
		webElement("potentialCustomerName").setParam("潜客名称");
		webElement("searchLeadsBtn").click();
		webElement("潜客复选框", caseParam.get("潜客名称")).select();
		webElement("allocate_leads_btn").click();
		webElement("分配销售主管", sale.getName()).select();
		webElement("allocateLeadsBtn").click();
		while (webElement("弹窗提示","alert").isShowText("只有已清洗未分配处理结果为待分配的线索才可以进行分配")) {
			webElement("okDopAlertButton").click();
			webElement("allocateLeadsBtn").click();
			sleep();
		}
		webElement("弹窗提示","alert").Assert("潜客线索分配成功");
		webElement("okDopAlertButton").click();
	}
}