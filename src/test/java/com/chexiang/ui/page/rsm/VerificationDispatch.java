package com.chexiang.ui.page.rsm;

import com.chexiang.ui.page.rsm.scene.VerificationType;

/**
 * @author liuzhixiang
 *
 */
public class VerificationDispatch extends RsmPage{
	
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
	 * 验车派单
	 */
	public void DispatchCase(VerificationType verificationType)
	{
		inquiry();
		
		webElement("派单").click();
		
		select("检验所").setValue("第二检测站(汶水东路937号)");
		
		select("验车员").setValue("孙翔");
		
		if(verificationType.GetDes()=="取消验车")
		{
			webElement("取消验车").click();
			
			select("取消原因").setValue("材料不全");
			
			webElement("确定").click();
		}
		else
		{
			webElement("调度完成").click();
			
			webElement("派单成功确认").click();
		}
		
	}
}
