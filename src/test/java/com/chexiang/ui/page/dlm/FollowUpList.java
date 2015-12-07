package com.chexiang.ui.page.dlm;

import java.util.Date;

import com.chexiang.ui.page.dlm.scene.FollowOppor;

//跟进计划
public class FollowUpList extends DlmPage {
	@Override
	protected void initMenu() {
		menu("客户管理", "跟进计划");
	}

	public void followOppor(FollowOppor fol) {
		initMenu();

		rightNow.setTime(new Date());

		webElement("name").setParam("潜客名称");
		webElement("searchFollowUp").click();
		webElement("跟进复选框", caseParam.get("潜客名称")).select();
		webElement("followOppor").click();

		// 跟进计划后客户意向变化情况
		select("velLevel0").setValue("A");
		select("opporFollowWay0").setValue("QQ");
		webElement(fol.name() + "0").select();
		switch (fol) {
		case velLook:
			nextFollowPlan();
			break;
		case testRide:
			select("testRideAddress0").setValue("展厅");
			webElement("idCardNum0").setParam("身份证");
			nextFollowPlan();
			break;
		case doorTestRide:
			webElement("idCardNum10").setParam("身份证");
			nextFollowPlan();
			break;
		case depositOrder:
			webElement("deposit0").setValue("10000");
			select("depositVelSeriesId_0").setRandom();
			select("depositMarketId_0").setRandom();
			select("depositColorName_0").setRandom();
			select("depositInteriorMarket_0").setRandom();
			webElement("payType_00").click();
			webElement("velFace_0").setValue("99999");
			nextFollowPlan();
			break;
		case deliveryVel:
			// 车辆信息
			webElement("弹窗提示","confirm").Assert("是否需要修改客户标签?");
			webElement("destoryDopConfirmButton").click();
			webElement("弹窗提示","confirm").Assert("该客户还未获取二维码信息，请确认是否还继续交车?");
			webElement("okDopConfirmButton").click();
			select("vinCode").setLastOption();
			webElement("inputKeyDown").setValue("");

			webElement("motorInvoiceAmount").setValue("99000");
			webElement("serviceInvoiceAmount").setValue("999");
			webElement("velFare").setValue("99999");
			webElement("invoiceDetail").click();
			webElement("usedKmNum").setValue("10");
			webElement("deliverDate").setValue(dateFormat.format(rightNow.getTime()));
			webElement("invoiceDate").setValue(dateFormat.format(rightNow.getTime()));
			webElement("receiptNum").setParam("发票编号");
			webElement("invoiceImgFile").setValue(IMG_FILE);
			webElement("invoiceType0").click();
			// 车主信息
			select("velOwnerType").setRandom();
			webElement("getVelName").setParam("潜客名称");
			webElement("getVelPhone").setParam("手机号码");
			webElement("custName").setParam("潜客名称");
			webElement("phone").setParam("手机号码");
			select("custType").setValue("个人客户");
			select("profession").setRandom();
			select("familyStructure").setRandom();
			select("annualIncome").setRandom();
			select("industry").setRandom();
			select("educationLevel").setRandom();
			select("paperType").setRandom();
			webElement("paperNum").setParam("身份证");
			webElement("zipCode").setValue("200000");
			select("provinceCode").setRandom();
			select("cityCode").setRandom();
			select("gender").setRandom();
			webElement("address").setValue("天鹅座 开普勒452b");
			// 销售成功分析
			select("primeReason").setRandom();
			select("indirectReason").setRandom();
			webElement("saveDeliverVelBtn").click();
			webElement("弹窗提示","alert").Assert("交车成功!");
			webElement("okDopAlertButton").click();
			break;
		case failApply:
			failMsg();
			break;
		default:
		}
	}

	private void nextFollowPlan() {
		// 下次跟进计划
		webElement("followUpVO.resultDesc").setValue("....................");
		select("opporFollowWay0").setValue("QQ");
		webElement("planFollowTimes").setValue(dateFormat.format(rightNow.getTime()));
		select("nextFollowPlan.followWay").setValue("QQ");
		webElement("nextFollowPlan.lookVel").select();
		webElement("nextFollowPlan.remarkDesc").setValue("...");
		webElement("updateFollowBtn").click();
		webElement("弹窗提示","confirm").Assert("是否需要修改客户标签?");
		webElement("destoryDopConfirmButton").click();
		webElement("弹窗提示","alert").Assert("保存成功!");
		webElement("okDopAlertButton").click();
	}
}