package com.chexiang.ui.page.dlm;

import java.util.Date;

import com.chexiang.ui.page.dlm.scene.Operator;

//待办事项
public class PotentialCus extends DlmPage {

	@Override
	protected void initMenu() {
		menu("待办事项");
	}

	private void leftMeun(String child) {
		while (!webElement("展开列表", child).isDisPlay()) {
			webElement("一级leftMeun", child).click();
		}
		webElement("二级leftMeun", child).click();
	}

	public void createCard(Operator seeker) {
		initMenu();
		rightNow.setTime(new Date());
		if (Operator.InternetOfficer.equals(seeker)) {
			leftMeun("网络待建卡客户");
		} else if (Operator.ExhibitionReceptionist.equals(seeker)) {
			leftMeun("展厅待建卡客户");
		}
		select("每页显示行数").setValue("20");
		webElement("客户建卡", caseParam.get("潜客名称")).click();
		select("morePopAge").setRandom();
		select("custType").setValue("个人客户");
		webElement("detail").setValue("....................");
		webElement("label").click();
		webElement("客户标签", "家庭收入").select();
		webElement("客户标签", "计划购车时间").select();
		webElement("客户标签", "职业").select();
		webElement("saveBtn").click();
		// 意向
		select("velSeriesId").setRandom("AP14", "请选择");
		select("velModelId").setRandom();
		select("marketNameId").setRandom();
		select("csLevelWant").setRandom();
		select("budget").setRandom();
		// 跟进结果
		select("opporFollowWay").setRandom();
		webElement("followResultBaseVO.lookVel").select();
		webElement("followResult.resultDesc").setValue("...");
		// 下次跟进计划
		webElement("planFollowTimes").setValue(dateFormat.format(rightNow.getTime()));
		select("followWay").setValue("QQ");
		webElement("followUpVO.lookVel").select();
		webElement("save_customer_btn").click();
		webElement("弹窗提示", "alert").Assert("新建客户成功！");
		webElement("okDopAlertButton").click();
	}

	public void failConfirm(Operator sale) {
		initMenu();
		leftMeun("未处理战败审核数");
		webElement("战败销售主管", sale.getName()).click();
		select("每页显示行数").setValue("20");
		webElement("战败潜客", caseParam.get("潜客名称")).select();
		webElement("agreeOppor").click();
		webElement("agreeAuditOpinion").setValue("...");
		webElement("doAgrBtn").click();
		webElement("弹窗提示", "alert").Assert("操作成功!");
		webElement("okDopAlertButton").click();
	}
}