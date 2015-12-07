package com.chexiang.ui.page.rsm.scene;

public enum CarTeam {

	Own("自有车队"), Parking("驻车点位置");

	private String des;

	private CarTeam(String string)

	{
		des = string;
	}

	public String GetDes() {
		return des.toString();
	}
}
