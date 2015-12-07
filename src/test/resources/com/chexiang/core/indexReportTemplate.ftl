<html>
<head>
<title>${name}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
@import "hovertable.css";
</style>
</head>
<body bgcolor="#FFF8DC">
	<div align="center">
		<h1>${name}</h1><br/>
		<table class="hovertable">
		<tr>
		<th>测试开始时间：</th><th>测试结束时间：</th><th>耗时：</th><th>接口执行总数：</th><th>用例执行总数：</th><th>检查点总数：</th><th>本次测试：</th>
		</tr><tr>
		<td></td><td></td><td></td>
		<td></td><td></td><td></td>
		<td><font color=</font></td>
		</tr>
		</table><br/>
		<h2>测试明细</h2><br/>
		<table class="hovertable">
			<tr>
				<th>执行时间</th><th>接口号</th><th>端口号</th><th>功能描述</th><th>用例号</th><th>用例描述</th>
				<th>是否通过</th><th>明细链接</th><th>备注</th>
			</tr>
			<#list caseList as case >  
				<tr>  
					<td></td><td>${case.class}</td>
					<td>${case.method}</td><td></td><td>${case.index}</td><td></td>
					<td><font color=${case.pass?string("blue","red")}>${case.pass?string("PASS","FAIL")}</font></td>
					<td><a href="particulars/${case.link}">${case.class+"."+case.method+"."+case.index}</a></td>
					<td></td>
				</tr>
			</#list>
		</table>
	</div>
</body>
</html>