<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='../common/header.jsp' />
<br>
<br>
<br>
<script type="text/javascript">
	function ff(){
		alert($('#file').val());
	}
</script>
<div class="row clearfix">
	<div class="col-md-10 column">
		<form action="management/export" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td colspan="4"><c:if test="${not empty message}">导出成功</c:if></td>
				</tr>
				<tr>
					<td>选择目标路径:</td>
					<td><input type="file" name="file" id="file">
						<input type="hidden" name="level" value="${level }">
						<input type="hidden" name="paymentDateLeft" value="${paymentDateLeft }">
						<input type="hidden" name="paymentDateRight" value="${paymentDateRight }">
					</td>
					<td><input type="button" onclick="ff();" name="submit" value="导出" class="form-control"></td>
					<td>execl文件</td>
				</tr>
			</table>
		</form>
	</div>
</div>
