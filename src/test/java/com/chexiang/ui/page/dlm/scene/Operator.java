package com.chexiang.ui.page.dlm.scene;

public enum Operator {
	InternetOfficer("网络推广专员", "SQ2103ajxsxstgly", "宋兢勤"), InternetDirector("网销主管", "SQ2103ajxsyy", "杨勇"), Manager(
			"销售经理", "SQ2103ajxsxtgly", "周圆圆"), Administrator("系统管理员", "SQ2103ajxsxtgly", "周圆圆"), ExhibitionAdviser(
					"销售顾问", "SQ2103ajxsgwy", "顾伟彦"), ExhibitionReceptionist("前台", "SQ2103ajxsqtrw", "朱晨晨");

	private final String role, id, name;

	private Operator(String role, String id, String name)
	{
		this.role = role;
		this.id = id;
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
}
