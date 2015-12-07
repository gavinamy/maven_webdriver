package com.chexiang.ui.page.dlm.scene;

public enum FollowOppor {
	velLook("看车"), testRide("展厅试乘试驾"), doorTestRide("上门试乘试驾"), depositOrder("定金订单"), deliveryVel("交车"), failApply(
			"战败申请");

	private final String name;

	private FollowOppor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
