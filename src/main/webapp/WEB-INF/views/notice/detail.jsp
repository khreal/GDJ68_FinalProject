<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<div class="card">
<div class="card-body">
  <table class="table table-bordered" id="dataTable" width="100%"
									cellspacing="0">
			
			<tbody>
				<tr>
					<th scope="row" class="point">제목</th>
					<td colspan="3" class="subject">${vo.noticeTitle}</td>
				</tr>
				<tr>
					<th scope="row">작성자</th>
					<td>${vo.humanResourceVO.name}</td>
					<th scope="row">작성일</th>
					<td>${vo.regDate}</td>
				</tr>
				<tr>
					<th scope="row">조회수</th>
					<td colspan="3">${vo.noticeHit}
						
					</td>
				</tr>
				<tr>
					
					<td colspan="4">
					 <c:forEach items="${vo.attachmentVOs}" var="f">
					    <img alt="" src="../files/${f.fileName}">
					    <a href="./fileDown?fileNo=${f.fileNo}">${f.originalName}</a>
					</c:forEach> 
					${vo.noticeContents}</td>
				</tr>
			</tbody>
			</table>   
			<a href="./update?noticeNo=${vo.noticeNo}" class="btn btn-info">수정</a>
			</div>
			</div> 
			
</body>
</html>