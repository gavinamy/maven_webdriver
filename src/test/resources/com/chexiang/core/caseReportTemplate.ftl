<html>
<head>
<title>${class+"."+method}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
@import "../hovertable.css";
</style>
</head>
<body bgcolor="#FFF8DC">
	<div align="center">
		<h1>${class+"."+method}</h1>
		<table class="hovertable">
			<tr>
				<th>检查点名称</th>
				<th>预期值</th>
				<th>实际值</th>
				<th>是否通过</th>
				<th>截图</th>
				<th>日志</th>
				<th>备注</th>
			</tr>
			<#list checkList as checkPoint >  
			<tr>
				<td>${checkPoint.name!""}</td>
				<td>${checkPoint.exp!""}</td>
				<td>${checkPoint.act!""}</td>
				<td><font color =${checkPoint.pass?string("blue","red")}>${checkPoint.pass?string("PASS","FAIL")}</font></td>
				<td></td>
				<td></td>
				<td>${checkPoint.remark!""}</td>
			</tr>
			</#list>
		</table>
	</div>
</body>
</html>