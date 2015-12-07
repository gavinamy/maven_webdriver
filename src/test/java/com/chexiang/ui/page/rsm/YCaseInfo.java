package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.rsm.scene.CarTeam;
import com.chexiang.ui.page.rsm.scene.OrderType;
import com.chexiang.ui.page.rsm.scene.ResultType;

public class YCaseInfo extends RsmPage {

	public void inquiry() {
		sleep();
		qutiIframe();
		webElement("tab").click();
		iFrame("iFrame").switchIn();
		// webElement("licenseNum").setParam("车牌号");
		// webElement("carSerialNum").setParam("车架号");
		webElement("serialNum").setParam("案件编号");

		webElement("查询").click();
	}

	public void dispatch(CarTeam carTeam) {
		inquiry();
//		webElement("案件列表").putParam("案件编号");
		this.DbAssert(
				"SELECT CODE_NAME FROM t_fix_code WHERE CODE = (SELECT case_status  FROM t_case WHERE  serial_num= "
						+ this.caseParam.get("案件编号") + ")",
				"案件受理");

		webElement("调度").click();

		switch (carTeam) {
		case Own:
			webElement("调度成功").click();
			webElement("anjiSave").click();
			break;
		case Parking:
			webElement("驻车点位置").click();
			webElement("驻车点位置调度成功").click();
			webElement("驻车点位置确定").click();
			break;
		}

		webElement("okDopConfirmButton").click();
		webElement("调度确定").click();
	}

	public void editOrder(OrderType orderType, ResultType resultType) {
		inquiry();
		webElement("修改").click();
		select("departProvince").setValue("上海市");
		select("departCity").setValue("上海市");
		select("departArea").setValue("静安区");
		webElement("departAddr").setValue("武宁南路287号");
		date("发车时间").click();
		date("到达现场时间").click();
		date("救援结束时间").click();
		webElement(resultType.GetDes()).click();
		switch (resultType) {
		case Cancel:
			// TODO 如果是网推工单：
			if (OrderType.InternetPush == orderType) {
				webElement("deadheadKilometers").setValue("10");
			}
			// this.select("cancelReason").selectByIndex(2);
			webElement("取消原因").click();
			break;
		case Fail:
			webElement("失败原因").click();
			break;
		default:
			break;
		}
		alert().ok();
		webElement("caseclosebtn").click();
		if (ResultType.Cancel == resultType) {
			date("案件取消时间").click();
			webElement("案件关闭确认").click();
		}
		alert().Assert("请求成功");
		alert().ok();
	}
}