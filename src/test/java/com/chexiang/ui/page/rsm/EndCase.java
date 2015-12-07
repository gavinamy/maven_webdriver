package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.rsm.scene.VerificationType;

/**
 * @author liuzhixiang
 *
 */
public class EndCase extends RsmPage{
	/**
	 * 根据预约单号查询
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
	 * 交车结案
	 */
	public void EndCarCase(VerificationType verificationType)
	{
		inquiry();
		
		webElement("结案").click();
		
		if(verificationType.GetDes()=="验车成功")
		{
			webElement("验车成功").click();
		}else if(verificationType.GetDes()=="验车失败")
		{
			webElement("验车失败").click();
		}
		
		webElement("关闭验车").click();
		
		webElement("结案成功").click();
	}
}
