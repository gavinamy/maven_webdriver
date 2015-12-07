package com.chexiang.ui.page.rsm.scene;

public enum ResultType {
	Success("案件成功"), Fail("案件失败"), Cancel("案件取消");

	private String des;

	private ResultType(String string)

	{
		des = string;
	}

	public String GetDes() {
		return des.toString();
	}
}
