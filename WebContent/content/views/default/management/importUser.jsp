<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='../common/header.jsp' />
<div class="row clearfix">
	<jsp:include page='../common/menu.jsp' />
	<div class="col-md-10 column">
		<form action="management/userImport" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td colspan="3"><c:if test="${not empty message}">文件上传成功</c:if></td>
				</tr>
				<tr>
					<td>上传文件:</td>
					<td><input type="file" name="file"></td>
					<td><input type="submit" name="submit" value="上传" class="form-control"></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<jsp:include page='../common/footer.jsp' />