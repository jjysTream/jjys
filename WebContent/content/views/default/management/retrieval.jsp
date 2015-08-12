<%@page import="com.yc.entity.user.Personnel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='../common/header.jsp' />
<script type="text/javascript"
	src="content/static/js/echart/ie10-viewport-bug-workaround.js"></script>
<link href="content/static/css/datetime/jquery-clockpicker.min.css"
	rel="stylesheet">
<link href="content/static/css/datetime/jquery.datetimepicker.css"
	rel="stylesheet">
<script type="text/javascript"
	src="content/static/js/datetime/bootstrap-clockpicker.min.js"></script>
<script type="text/javascript"
	src="content/static/js/datetime/jquery.datetimepicker.js"></script>
<script type="text/javascript">
	// Popup window code
	function popupWindow(url) {
		popupWindow = window
				.open(
						url,
						'popUpWindow',
						'height=600,width=600,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
	}
	function importUser(){
		window.location.href = 'management/importUser';
	}
	function dateInfoxxx(obj) {
		var date = obj;
		$('#' + date).datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});
	}
	function isSettle(id){
		window.location.href = 'management/isSettle?recordID='+id+'&level='+$('#level').val()+'&paymentDateLeft='+$('#paymentDateLeft').val()+'&paymentDateRight='+$('#paymentDateRight').val();
	}
</script>
<div class="row clearfix">
	<jsp:include page='../common/menu.jsp' />
	<div class="col-md-10 column">
	<c:set var="person"
	value='<%=(Personnel) request.getSession().getAttribute(
						"loginPersonnle")%>'></c:set>
	<form class="form-horizontal"
					action="management/retrieval" method="POST">
					<div class="form-group">
						<div class="col-sm-2">
							<select name="level" id="level" class="form-control" >
								<option value="-1">-----会员等级-----
								<option value="senior" <c:if test="${level == 'senior' }">checked</c:if>>高级
								<option value="common" <c:if test="${level == 'common' }">checked</c:if>>普通
							</select>
						</div>
						<div class="col-sm-2">
							<input type="text" name="paymentDateLeft" id="paymentDateLeft" onclick="dateInfoxxx('paymentDateLeft')" class="form-control" placeholder="起始日期">
						</div>
						<div class="col-sm-2">
							<input type="text" name="paymentDateRight" id="paymentDateRight" onclick="dateInfoxxx('paymentDateRight')" class="form-control" placeholder="截至日期">
						</div>
						<div class="col-sm-2">
							<button type="submit" class="btn btn-default ">查询</button>
						</div>
						<div class="col-sm-2">
							<button type="button" onclick="expartUser();" class="btn ">导出</button>
						</div>
					</div>
				</form>
		<div class="list-group">
			<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					账户列表
				</h3>
			</div>
			<div class="list-group-item">
				<p class="list-group-item-text">
				<table class="table table-striped">
					<tr class="">
						<th>身份证</th>
						<th>用户名</th>
						<th>会员等级</th>
						<th>性别</th>
						<th>电话</th>
						<th>起始日期</th>
						<th>结束日期</th>
						<th>创建机构</th>
						<th>续费机构</th>
						<th>服务中心</th>
						<th>省级机构</th>
						<th>机构总部</th>
						<th>操作</th>
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
						<td><c:if test="${cdc.level == 'common' }"></c:if>
						<c:if test="${cdc.level == 'senior' }">G</c:if>
						</td>
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
						<td>${cdc.cateDate }</td>
						<td>${cdc.endDate }</td>
						<td>${cdc.cateDepartment.departmentName }</td>
						<td>${cdc.renewDepartment.departmentName }</td>
						<td>${cdc.cateDepartment.parentLevel.departmentName }</td>
						<td>${cdc.cateDepartment.parentLevel.parentLevel.departmentName }</td>
						<td>${cdc.cateDepartment.parentLevel.parentLevel.parentLevel.departmentName }</td>
						<td>
						<button onclick="isSettle('cdc.recordID');"  class="btn "><c:if test="${!cdc.isSettle }">未结算</c:if><c:if test="${cdc.isSettle }">已结算</c:if></button>
						</td>
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
