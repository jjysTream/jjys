<%@page import="com.yc.entity.user.Personnel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='../common/header.jsp' />
<script type="text/javascript">
	// Popup window code
	function popupWindow(url) {
		popupWindow = window
				.open(
						url,
						'popUpWindow',
						'height=600,width=600,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
	}
</script>
<div class="row clearfix">
	<jsp:include page='../common/menu.jsp' />
	<div class="col-md-10 column">
	<c:set var="person"
	value='<%=(Personnel) request.getSession().getAttribute(
						"loginPersonnle")%>'></c:set>
	<form class="form-horizontal"
					action="management/searchUser" method="POST">
					<div class="form-group">
						<div class="col-sm-2">
							<input type="text" name="seachName"  class="form-control" placeholder="身份证">
						</div>
						<div class="col-sm-2">
							<button type="submit" class="btn btn-default ">查询</button>
						</div>
					</div>
				</form>
		<div class="list-group">
			<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					<a href="management/regist">账户列表<span class="badge navbar-right">添加&nbsp;+</span></a>
				</h3>
			</div>
			<div class="list-group-item">
				<p class="list-group-item-text">
				<table class="table table-striped">
					<tr class="">
						<th>身份证</th>
						<th>用户名</th>
						<th>性别</th>
						<th>电话</th>
						<th>充值记录</th>
						<th></th> 
					</tr>
					<c:forEach var="cdc" items="${list }" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index%2==0 }">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="success">
							</c:otherwise>
						</c:choose>
						<td>${cdc.loginName }</td>
						<td>${cdc.userName }</td>
						<td>
							<c:if test="${cdc.sex == 'Female' }">女</c:if>
							<c:if test="${cdc.sex == 'Male' }">男</c:if>
						</td> 
						<td>
						<c:set value="false" var="isok"></c:set>
						<c:if test="${person.department.departmentID == 1 || person.department.level == 4 || person.isView == 'true'}">
							${cdc.phone }
							<c:set value="true" var="isok"></c:set>
						</c:if>
						<c:if test="${!isok}">
							权限不够
						</c:if>
						</td> 
						<td><a onclick="popupWindow('management/rechargeForUser?id=${cdc.membersUserID }');" href="javascript:void(0);"><span class="badge">交易记录</span></a></td>
						<td>
							<a onclick="" href="management/updateUser?id=${cdc.membersUserID }"><span class="badge">修改</span></a>
							<a onclick="" href="management/deleteUser?id=${cdc.membersUserID }"><span class="badge">删除</span></a></td>
						</tr>
					</c:forEach>
				</table>
				</p>
			</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page='../common/footer.jsp' />