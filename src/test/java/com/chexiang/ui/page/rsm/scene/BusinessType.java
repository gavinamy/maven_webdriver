package com.chexiang.ui.page.rsm.scene;

public enum BusinessType {
	Accident("事故"), Tow("拖车"), Drive("代驾"), UrgentMend("抢修");

	private String des;

	private BusinessType(String string)

	{
		des = string;
	}

	public String GetDes() {
		return des.toString();
	}
}
