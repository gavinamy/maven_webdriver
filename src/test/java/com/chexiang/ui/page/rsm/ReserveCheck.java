package com.chexiang.ui.page.rsm;

/**
 * @author liuzhixiang
 *
 */
public class ReserveCheck extends RsmPage{	
	/**
	 * 验车单确认
	 */
	public void AcceptCase()
	{
		
		this.qutiIframe();
		webElement("tab").click();
		iFrame("iFrame").switchIn();
		
		webElement("接单").click();
		String s = webElement("预约单号").getElement().getAttribute("value").toString();
		caseParam.put("预约单号", s);		
		
		select("validateCarType").setValue("验车");
		
		webElement("预估费用").setValue("200");
		
		select("接车区县").setValue("普陀区");
		
		webElement("接车地址").setValue("曹杨");
		
		select("交车区县").setValue("静安区");
		
		webElement("交车地址").setValue("武宁南路");
		
		webElement("预约完成").click();
		
		webElement("预约成功确认").click();
	}
	
}
