package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.rsm.scene.BusinessType;
import com.chexiang.ui.page.rsm.scene.OrderType;

public class MyDesk extends RsmPage {

	public void createSerial(OrderType orderType, BusinessType businessType) {
		this.qutiIframe();
		webElement("tab").click();
		iFrame("iframe").switchIn();
		webElement(orderType.GetDes() + businessType.GetDes()).click();
	}
}