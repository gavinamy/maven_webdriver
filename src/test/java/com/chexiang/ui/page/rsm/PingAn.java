package com.chexiang.ui.page.rsm;

import java.io.StringWriter;
import java.util.UUID;

import com.chexiang.core.ParameterUtil;

public class PingAn extends RsmPage {

	ParameterUtil paraUtil = new ParameterUtil();
	StringWriter writer = new StringWriter();
	String request;

	/**
	 * 网推模拟器
	 * 
	 * @param: void
	 * @return: void
	 * @Author: 手慢无
	 * @Create: Date: 2015-08-17
	 */
	public void pingAnPut() {
		// paraUtil.setTemplate(new
		// File(this.getClass().getResource("").getPath()));
		// paraUtil.process("pingan.xml", writer);
		// request = writer.toString();

		// 更新数据库，让62004坐席独自在线，以便接受推送
		this.dbExecute("update t_am_users set description ='103200002' where agent_id <> '62004'");
		// 读取xml报文模板，并将id改为uuid
		uiCase.XmlLoad(this.getClass().getResource("pingan.xml").getFile());
		uiCase.XmlEdit(UUID.randomUUID().toString().replaceAll("-", "").substring(15), "Body", "Apply", "SALVATION_ID");
		// 推送并刷新临时表
		uiCase.post(this.pageData.get("request"), uiCase.Xml2String());
		uiCase.post(this.pageData.get("refresh"), "");
	}
}
