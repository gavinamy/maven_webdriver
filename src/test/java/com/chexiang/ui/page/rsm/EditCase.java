package com.chexiang.ui.page.rsm;

public class EditCase extends RsmPage{
	/**
	 * 
	 */
	public void inquiry() {
		sleep();
		qutiIframe();
		webElement("tab").click();
		iFrame("iFrame").switchIn();
		
		webElement("预约单号").setParam("预约单号");

		webElement("查询").click();
	}
	
	/**
	 * 
	 */
	public void EditDispatchCase()
	{
		inquiry();
		
		webElement("变更").click();
		
		webElement("交车").click();
		
		webElement("保存更改").click();
		
		webElement("验车单保存成功").click();
		
	}
	
}
