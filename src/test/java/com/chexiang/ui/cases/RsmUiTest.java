package com.chexiang.ui.cases;


import org.testng.annotations.Test;

import com.chexiang.ui.page.rsm.CaseInfo;
import com.chexiang.ui.page.rsm.EditCase;
import com.chexiang.ui.page.rsm.EndCase;
import com.chexiang.ui.page.rsm.HomePage;
import com.chexiang.ui.page.rsm.MyDesk;
import com.chexiang.ui.page.rsm.PingAn;
import com.chexiang.ui.page.rsm.ReserveCheck;
import com.chexiang.ui.page.rsm.VerificationDispatch;
import com.chexiang.ui.page.rsm.YCaseInfo;
import com.chexiang.ui.page.rsm.scene.BusinessType;
import com.chexiang.ui.page.rsm.scene.CarTeam;
import com.chexiang.ui.page.rsm.scene.OrderType;
import com.chexiang.ui.page.rsm.scene.ResultType;
import com.chexiang.ui.page.rsm.scene.VerificationType;




/**
 * @author liuzhixiang
 *
 */
public class RsmUiTest extends UiCase {

	protected PingAn pingan;
	protected HomePage home;
	protected MyDesk myDesk;
	protected CaseInfo caseInfo;
	protected YCaseInfo yCaseInfo;
	public ReserveCheck reserveCheck;
	protected VerificationDispatch verificationDispatch;
	protected EditCase editCase;
	protected EndCase endCase;
	

	@Override
	protected void end() {
		home.closeDb();
	}

	// 通过testng框架输入数据驱动
	// @Test(dataProvider = "uiProvider", invocationCount = 1, threadPoolSize =
	// 1)
	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendOwnerSuccess() {
		// 自建工单，抢修，自有车队，成功
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Own, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendOwnerFail() {
		// 自建工单，抢修，自有车队，失败
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Own, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendOwnerCancel() {
		// 自建工单，抢修，自有车队，取消
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Own, ResultType.Cancel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowOwnerSuccess() {
		// 自建工单，拖车，自有车队，成功
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Own, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowOwnerFail() {
		// 自建工单，拖车，自有车队，失败
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Own, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowOwnerCancel() {
		// 自建工单，拖车，自有车队，取消
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Own, ResultType.Cancel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendParkingSuccess() {
		// 自建工单，抢修，驻车点，成功
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendParkingFail() {
		// 自建工单，抢修，驻车点，失败
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createMendParkingCancel() {
		// 自建工单，抢修，驻车点，取消
		this.rsmRegression(OrderType.Own, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Cancel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowParkingSuccess() {
		// 自建工单，拖车，驻车点，成功
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Parking, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowParkingFail() {
		// 自建工单，拖车，驻车点，失败
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Parking, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createTowParkingCancel() {
		// 自建工单，拖车，驻车点，取消
		this.rsmRegression(OrderType.Own, BusinessType.Tow, CarTeam.Parking, ResultType.Cancel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createAccidentParkingSuccess() {
		this.rsmRegression(OrderType.Own, BusinessType.Accident, CarTeam.Parking, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createAccidentParkingFail() {
		this.rsmRegression(OrderType.Own, BusinessType.Accident, CarTeam.Parking, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createAccidentParkingCancel() {
		this.rsmRegression(OrderType.Own, BusinessType.Accident, CarTeam.Parking, ResultType.Cancel);
		//Reporter.log("M3 WAS CALLED");
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentParkingSuccess() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentParkingFail() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentParkingCancel() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Parking, ResultType.Cancel);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentOwnSuccess() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Own, ResultType.Success);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentOwnFail() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Own, ResultType.Fail);
	}

	@Test(invocationCount = 1, threadPoolSize = 1)
	public void createInternetPushAccidentOwnCancel() {
		this.rsmRegression(OrderType.InternetPush, BusinessType.UrgentMend, CarTeam.Own, ResultType.Cancel);
	}
	
    
	/**
	 * 验车成功
	 */
	@Test(invocationCount = 1, threadPoolSize = 1)
	public void VerificationSuccess() {
		this.VerificationCheck(VerificationType.Success);
	}
	
    /**
     * 验车失败
     */
    @Test(invocationCount = 1, threadPoolSize = 1)
	public void VerificationFail() {
		this.VerificationCheck(VerificationType.Fail);
	}
    
    /**
     * 取消验车
     */
    @Test(invocationCount = 1, threadPoolSize = 1)
	public void VerificationCancel() {
		this.VerificationCheck(VerificationType.Cancel);
	}
	
	private void VerificationCheck(VerificationType verificationType)
	{
		home = new HomePage();				
		// 登陆
		home.logIn("60001");
		reserveCheck = new ReserveCheck();		
		reserveCheck.menu("验车管理", "预约确认");
		//	接单
		reserveCheck.AcceptCase();
		// 关闭tab
		reserveCheck.close();
		
		// 验车派单
		verificationDispatch = new VerificationDispatch();		
		verificationDispatch.menu("验车派单");		
		verificationDispatch.DispatchCase(verificationType);		
		verificationDispatch.close();
		
		if(verificationType.GetDes()!="取消验车")
		{
			// 验车单变更
			editCase = new EditCase();		
			editCase.menu("验车单变更");		
			editCase.EditDispatchCase();		
			editCase.close();
			
			// 交车结案
			endCase =new EndCase();		
			endCase.menu("交车结案");		
			endCase.EndCarCase(verificationType);
			endCase.close();	
		}
	}	
	

	private void rsmRegression(OrderType orderType, BusinessType businessType, CarTeam carTeam, ResultType result) {
		// 该方法提供了救援相关的业务流程
		home = new HomePage();
		myDesk = new MyDesk();
		caseInfo = new CaseInfo();
		yCaseInfo = new YCaseInfo();
		pingan = new PingAn();

		if (OrderType.InternetPush == orderType) {
			// 网推工单数据准备
			pingan.pingAnPut();
		}
		// 登陆
		home.logIn("62004");
		// 授权管理，进入我的桌面
		myDesk.menu("救援管理", "我的桌面");

		// 新建工单
		myDesk.createSerial(orderType, businessType);
		// 关闭我的桌面tab页
		myDesk.close();
		// 新建工单页面输入信息
		caseInfo.create(orderType, businessType);
		// 成功之后关闭新建工单tab页
		caseInfo.close();

		// 案件信息tab页
		yCaseInfo.menu("案件信息");
		// 调度
		yCaseInfo.dispatch(carTeam);
		// 编辑调度信息，并完成
		yCaseInfo.editOrder(orderType, result);
		// 关闭案件信息tab页
		yCaseInfo.close();
	}
	
	
}
