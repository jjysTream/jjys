<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='../common/header.jsp' />
<br>
<br>
<br>
<div class="row clearfix">
	<div class="col-md-10 column">
		<form action="management/export" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td colspan="2"><c:if test="${not empty name}">文件已经生成，请下载！！！</c:if></td>
				</tr>
				<tr>
					<td>文件:</td>
					<td>
						<a href="${path }">点击下载：${name }</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
