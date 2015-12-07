package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.rsm.scene.BusinessType;
import com.chexiang.ui.page.rsm.scene.OrderType;

public class CaseInfo extends RsmPage {

	public void create(OrderType orderType, BusinessType businessType) {
		caseParam.put("车牌号", "沪ae350b6");
		caseParam.put("手机号", "13818111655");
		caseParam.put("姓名", "手慢无");
		caseParam.put("车架号", "NQN8F2LJPCZD8ENI3YSW0S64KSLF08");
		this.qutiIframe();

		webElement("tab").click();
		iFrame("iFrame").switchIn();

		if (OrderType.InternetPush == orderType) {
			getCaseNumber(orderType);
			webElement("网推接单").click();
		} else {
			edit("contrName").setValue("平保");
			select("服务类型").setIndex(1);
		}
		select("resProvince").setValue("上海市");
		select("resCity").setValue("上海市");
		select("resArea").setValue("静安区");

		webElement("mobilephone").setParam("手机号");
		webElement("custName").click();
		webElement("custName").setParam("姓名");
		select("caseType").setValue("实时案件");

		webElement("telephone").setParam("手机号");
		webElement("linkPhone").setParam("手机号");

		webElement("resAddr").setDriverData();
		webElement("licenseNum").setParam("车牌号");
		webElement("serialNum").setParam("车架号");
		if (BusinessType.Accident == businessType || BusinessType.Tow == businessType) {
			webElement("destination").setValue("武宁南路200号");
		}
		// webElement("备注").setDriverData();

		select("bkType").setValue("发动机故障");

		webElement("casem2").click();
		alert().Assert("请求成功");
		alert().ok();
		webElement("查询按钮").click();
		if (OrderType.InternetPush != orderType) {
			getCaseNumber(orderType);
		}
	}

	private void getCaseNumber(OrderType orderType) {
		webElement(orderType.GetDes() + "案件编号").putParam("案件编号");
	}
}
