<%@page import="com.yc.entity.user.Personnel"%>
<%@ page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构管理员</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<link href="content/static/css/bootstrap/navbar.css" rel="stylesheet">
<link href="content/static/css/bootstrap/bootstrap.min.css"
	rel="stylesheet">
<script src="content/static/js/echart/ie-emulation-modes-warning.js"></script>
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="content/static/img/apple-touch-icon-144-precomposed.png" />
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="content/static/img/apple-touch-icon-114-precomposed.png" />
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="content/static/img/apple-touch-icon-72-precomposed.png" />
<link rel="apple-touch-icon-precomposed"
	href="content/static/img/apple-touch-icon-57-precomposed.png" />
<link rel="shortcut icon" href="content/static/img/favicon.png" />
<script type="text/javascript" src="content/static/js/lib/jquery.min.js"></script>
<script type="text/javascript"
	src="content/static/js/lib/bootstrap.min.js"></script>

<script type="text/javascript"
	src="content/static/js/echart/ie10-viewport-bug-workaround.js"></script>

<link href="content/static/css/datetime/jquery-clockpicker.min.css"
	rel="stylesheet" />
<link href="content/static/css/datetime/jquery.datetimepicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="content/static/js/datetime/bootstrap-clockpicker.min.js"></script>
<script type="text/javascript"
	src="content/static/js/datetime/jquery.datetimepicker.js"></script>
</head>
<body>
	<!-- Static navbar -->
	<jsp:include page='../common/header.jsp' />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page='../common/menu.jsp' />
			<div class="col-md-10 column">
				<form class="form-horizontal"
					action="management/searchPersonnelResult" method="POST">
					<div class="form-group">
						<div class="col-sm-2">
							<select name="department_id" id="department_id"
								class="form-control" onchange="depChange(this);">
								<option value="0">选择机构
									<c:forEach var="department" items="${departmentlist }">
										<option value="${department.departmentID }">${department.departmentName }
									</c:forEach>
							</select>
						</div>
						<div class="col-sm-2">
							<select name="forbidden" class="form-control">
								<option value="info">禁用状况</option>
								<option value="no">已禁用</option>
								<option value="yes">未禁用</option>
							</select>
						</div>
						<div class="col-sm-2">
							<button type="submit" class="btn btn-default ">查询</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="col-md-10 column">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							机构管理员列表 <a href="personnel/addPersonnel?mathed=add"> <span
								class="badge navbar-right" id="add"><font size="3px;">添加&nbsp;&nbsp;+</font></span></a>
						</h3>
					</div>
					<c:set var="person"
	value='<%=(Personnel) request.getSession().getAttribute(
						"loginPersonnle")%>'></c:set>
					<div class="list-group-item">
						<p class="list-group-item-text">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>用户名</th>
									<th>性别</th>
									<th>所属机构</th>
									<th>电话</th>
									<th>邮件</th>
									<th>是否禁用</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="personnel" items="${personnellist }"
									varStatus="pool">
									<c:choose>
										<c:when test="${loop.index%2==0 }">
											<tr>
										</c:when>
										<c:otherwise>
											<tr class="success">
										</c:otherwise>
									</c:choose>
									<td>${personnel.userName}</td>
									<td><c:if test="${personnel.sex == 'Female'}">女</c:if> <c:if
											test="${personnel.sex == 'Male'}">男</c:if></td>
									<td>${personnel.department.departmentName}</td>
									<td>
										${personnel.phone}
									</td>
									<td>${personnel.email}</td>
									<td><c:if test="${personnel.forbidden == false}">正常使用</c:if>
										<c:if test="${personnel.forbidden != false}">已经禁用</c:if></td>
									<td>
										<c:if test="${personnel.personnelID == person.personnelID || person.department.level < personnel.department.level }">
										<button class="btn btn-default"
											onclick="updatePerson('personnel/addPersonnel?id=${personnel.personnelID}&mathed=update');">修改</button>
										</c:if>
										<c:if test="${person.department.departmentID == 1 && personnel.department.departmentID != 1 }">
											<button type="button" class="btn btn-default"
												onclick="forbiddenPersonnelById('${personnel.personnelID}');">
												<c:if test="${personnel.forbidden == false}">禁用</c:if>
												<c:if test="${personnel.forbidden != false}">恢复</c:if>
											</button> 
											<button type="button" class="btn btn-default"
												onclick="forbiddenPersonnelById('${personnel.personnelID}');">
												<c:if test="${personnel.isView == false}">电话可见</c:if>
												<c:if test="${personnel.isView != false}">电话隐藏</c:if>
											</button> 
											
										<a href="personnel/deleteUser?id=${personnel.personnelID }">
										<button class="btn btn-default" >删除</button></a>
										</c:if>
									</td>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		function forbiddenPersonnelById(obj) {
			location.href = "personnel/forbiddenPersonnel?id=" + obj;
		}

		function updatePerson(url){
			location.href = url;
		}
		function addMember(obj) {
			document.form.action = "personnel/addPersonnel?mathed=" + obj;
			document.form.submit();
		}

		function depChange(obj) {
			jQuery.ajax({
				type : 'GET',
				contentType : 'application/json',
				url : 'getShopCategory/getPositions?depID=' + obj.value,
				dataType : 'json',
				success : function(data) {
					if (data.success == 'true') {
						var pos = document.getElementById('position_id');
						var numd = pos.options.length;
						for (i = numd - 1; i >= 0; i--) {
							pos.remove(i);
						}
						var objOption = new Option("选择职位", "0");
						pos.options[pos.options.length] = objOption;
						$.each(data.list, function(i, position) {
							var objOption = new Option(position.positionname,
									position.positionid);
							pos.options[pos.options.length] = objOption;
						});
					}
				}
			});
		}

		function popupwindow(url) {
			var w = 1200;
			var h = 800;
			var title = "员工";
			var left = (screen.width / 2) - (w / 2);
			var top = (screen.height / 2) - (h / 2);
			return window
					.open(
							url,
							title,
							'directories=0,titlebar=0,toolbar=0,location=0,status=0,menubar=0,scrollbars=yes,resizable=yes, width='
									+ w
									+ ', height='
									+ h
									+ ', top='
									+ top
									+ ', left=' + left);
		}
	</script>
</body>
</html>
