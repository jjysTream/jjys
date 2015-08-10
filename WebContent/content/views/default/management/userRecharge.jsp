<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; %>
<base href="<%=basePath%>">
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<meta name="description" content=""/>
<meta name="author" content=""/>
<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="content/static/img/apple-touch-icon-144-precomposed.png"></link>
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="content/static/img/apple-touch-icon-114-precomposed.png"></link>
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="content/static/img/apple-touch-icon-72-precomposed.png"></link>
<link rel="apple-touch-icon-precomposed"
	href="content/static/img/apple-touch-icon-57-precomposed.png"></link>
<link rel="shortcut icon" href="content/static/img/favicon.png"></link>
  <script src="http://code.jquery.com/jquery-1.9.1.min.js"></script> 

  <!-- include libraries BS3 -->
  <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.0.1/css/bootstrap.min.css" />
  <script type="text/javascript" src="http://netdna.bootstrapcdn.com/bootstrap/3.0.1/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />

  <!-- include summernote -->
  <link rel="stylesheet" href="content/static/css/bootstrap/summernote.css"/>
  <script type="text/javascript" src="content/static/js/lib/summernote.js"></script>
  <script type="text/javascript" src="content/static/js/lib/summernote-zh-CN.js"></script>
</head>

<body>
<div class="row clearfix">
	<div class="col-md-10 column">
		<div class="list-group">
			<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">
					${recharge.membersUser.userName }
				</h3>
			</div>
			<div class="list-group-item">
				<p class="list-group-item-text">
				<table class="table table-striped">
					<tr class="">
						<th>创建机构</th>
						<th>续费机构</th>
						<th>起始日期</th>
						<th>截至日期</th>
						<th>操作</th> 
					</tr>
					<c:forEach var="recharge" items="${list }" varStatus="loop">
						<c:choose>
							<c:when test="${loop.index%2==0 }">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="success">
							</c:otherwise>
						</c:choose>
						<td>
						${recharge.cateDepartment.departmentName }
						</td> 
						<td>
						${recharge.renewDepartment.departmentName }
						</td>
						<td>${recharge.createDate }</td>
						<td>${recharge.endDate }</td>
						<td>
							<c:if test="${loop.index == 0 }">
							<a onclick="" href="management/xufei?id=${recharge.membersUser.membersUserID }"><span class="badge">续费</span></a>
							<a onclick="" href="management/deletexufei?id=${recharge.recordID }"><span class="badge">删除</span></a>
							</c:if>
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
</body>
</html>