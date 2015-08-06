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
						<th>登录名</th>
						<th>密码</th>
						<th>用户名</th>
						<th>性别</th>
						<th>体质</th>
						<th>Email</th>
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
						<td>${cdc.password }</td>
						<td>${cdc.userName }</td>
						<td>
							<c:if test="${cdc.sex == 'Female' }">女</c:if>
							<c:if test="${cdc.sex == 'Male' }">男</c:if>
						</td> 
						<td>
							<c:if test="${cdc.bodyConstitution == 'FlatAndQuality' }">平和质</c:if>
							<c:if test="${cdc.bodyConstitution == 'QiDeficiency' }">气虚质</c:if>
							<c:if test="${cdc.bodyConstitution == 'YangXuzhi' }">阳虚质</c:if>
							<c:if test="${cdc.bodyConstitution == 'YinDeficiency' }">阴虚质</c:if>
							<c:if test="${cdc.bodyConstitution == 'QiStagnation' }">气郁质</c:if>
							<c:if test="${cdc.bodyConstitution == 'BloodStasis' }">血瘀质</c:if>
							<c:if test="${cdc.bodyConstitution == 'HotAndhumidQuality' }">湿热质</c:if>
							<c:if test="${cdc.bodyConstitution == 'PhlegmDampnessQuality' }">痰湿质</c:if>
							<c:if test="${cdc.bodyConstitution == 'IntrinsicQuality' }">特稟质</c:if>
						</td> 
						<td>${cdc.email }</td>
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