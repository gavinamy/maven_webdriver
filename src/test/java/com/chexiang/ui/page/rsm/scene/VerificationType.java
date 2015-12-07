/**
 * Copyright 2015 liuzhixiang
 *
 *	 Created on 2015年9月29日 下午1:39:36
 *
 */
package com.chexiang.ui.page.rsm.scene;

/**
 * @author liuzhixiang
 *
 */
public enum VerificationType {
	Success("验车成功"), Fail("验车失败"),Cancel("取消验车");

	private String des;

	private VerificationType(String string)

	{
		des = string;
	}

	public String GetDes() {
		return des.toString();
	}
}