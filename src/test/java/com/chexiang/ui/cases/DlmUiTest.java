package com.chexiang.ui.cases;

import org.testng.annotations.Test;

import com.chexiang.ui.page.dlm.SearchLeads;
import com.chexiang.ui.page.dlm.TransactionCarInit;
import com.chexiang.ui.page.dlm.scene.FollowOppor;
import com.chexiang.ui.page.dlm.scene.Operator;
import com.chexiang.ui.page.dlm.Exhibition;
import com.chexiang.ui.page.dlm.FollowUpList;
import com.chexiang.ui.page.dlm.HomePage;
import com.chexiang.ui.page.dlm.PotentialCus;

/**
 * @ClassName: DlmUiTest
 * @Description: dlm系统测试用例
 * @author 手慢无
 * @date 2015-09-24
 */
public class DlmUiTest extends UiCase {
	private HomePage home;
	private SearchLeads leads;
	private PotentialCus potCus;
	private FollowUpList folUp;
	private Exhibition exihbit;
	private TransactionCarInit transCarInit;

	private Operator seeker, salesman, manager;

	/**
	 * 基类aftersuit方法，委托给实现类
	 * 
	 * 关闭数据库
	 */
	@Override
	protected void end() {
		home.closeDb();
	}

	/**
	 * 基类beforesuit方法，委托给实现类
	 */
	@Override
	protected void start() {
		super.start();
		home = new HomePage();
		leads = new SearchLeads();
		potCus = new PotentialCus();
		folUp = new FollowUpList();
		exihbit = new Exhibition();
		transCarInit = new TransactionCarInit();
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0000() {
		// 网推新建线索
		seeker = Operator.InternetOfficer;
		// 网销建卡、跟进
		salesman = Operator.InternetDirector;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 、网销建卡、网销跟进、网销战败
		createLeads();
		// 跟进，战败
		followOppor(FollowOppor.velLook, FollowOppor.failApply);
		// 战败审核
		managerConfirm(FollowOppor.failApply);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0001() {
		// 网推新建线索
		seeker = Operator.InternetOfficer;
		// 网销建卡、跟进
		salesman = Operator.InternetDirector;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 、网销建卡、网销跟进、网销战败
		createLeads();
		// 跟进，交车
		followOppor(FollowOppor.velLook, FollowOppor.deliveryVel);
		// 退车
		managerConfirm(FollowOppor.deliveryVel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0010() {
		// 展厅接待
		seeker = Operator.ExhibitionReceptionist;
		// 展厅建卡、跟进
		salesman = Operator.ExhibitionAdviser;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 网销顾问建卡、网销跟进、网销战败
		createLeads();
		// 跟进，战败
		followOppor(FollowOppor.velLook, FollowOppor.failApply);
		// 战败审核
		managerConfirm(FollowOppor.failApply);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0011() {
		// 展厅接待
		seeker = Operator.ExhibitionReceptionist;
		// 展厅顾问建卡、跟进
		salesman = Operator.ExhibitionAdviser;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 网销建卡、网销跟进、网销战败
		createLeads();
		// 跟进，战败
		followOppor(FollowOppor.velLook, FollowOppor.failApply);
		// 战败审核
		managerConfirm(FollowOppor.failApply);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0012() {
		// 展厅接待
		seeker = Operator.ExhibitionReceptionist;
		// 网销建卡、跟进
		salesman = Operator.InternetDirector;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 网销建卡、网销跟进、网销战败
		createLeads();
		// 跟进，交车
		followOppor(FollowOppor.velLook, FollowOppor.deliveryVel);
		// 退车
		managerConfirm(FollowOppor.deliveryVel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void dlm0013() {
		// 展厅接待
		seeker = Operator.ExhibitionReceptionist;
		// 网销建卡、跟进
		salesman = Operator.InternetDirector;
		manager = Operator.Manager;
		// 数据初始化
		home.initDate();
		// 网销建卡、网销跟进、网销战败
		createLeads();
		// 跟进，交车
		followOppor(FollowOppor.velLook, FollowOppor.deliveryVel);
		// 退车
		managerConfirm(FollowOppor.deliveryVel);
	}

	public void debuggerScript() {
	}

	/**
	 * @Title: createLeads
	 * @Description: 情报人员新建线索
	 * @param void
	 * @return void
	 * @date 2015-08-27
	 */
	private void createLeads() {
		home.logIn(seeker);
		if (Operator.InternetOfficer.equals(seeker)) {
			leads.createLeads();
			leads.assign(salesman);
		} else if (Operator.ExhibitionReceptionist.equals(seeker)) {
			exihbit.receive(salesman);
		}
	}

	/**
	 * @Title: followOppor
	 * @Description: 销售人员建卡与跟踪
	 * @param follows:
	 *            跟进形式：试驾、战败、定金、交车等
	 * @return void
	 * @date 2015-08-27
	 */
	private void followOppor(FollowOppor... follows) {
		home.logOutAndIn(salesman);
		potCus.createCard(seeker);
		// 网销主管跟进
		for (FollowOppor fo : follows) {
			folUp.followOppor(fo);
		}
	}

	/**
	 * @Title: managerConfirm
	 * @Description: 经理审核
	 * @param follow:
	 *            战败或交车
	 * @return void
	 * @date 2015-08-27
	 */
	private void managerConfirm(FollowOppor follow) {
		home.logOutAndIn(Operator.Manager);
		if (FollowOppor.failApply.equals(follow)) {
			potCus.failConfirm(salesman);
		} else if (FollowOppor.deliveryVel.equals(follow)) {
			transCarInit.refund();
		}
	}
}