package com.chexiang.ui.page.rsm.scene;

public enum OrderType {
	Own("自建"), InternetPush("网推");

	private String des;

	private OrderType(String string)

	{
		des = string;
	}

	public String GetDes() {
		return des.toString();
	}
}
