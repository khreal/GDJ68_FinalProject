<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>



</head>
<body id="page-top">
	<div id="wrapper">


		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">
			<div class="card">
				<div class="card-body">

					<div class="wrapper-toolbar">

						<div style="float:left">공지사항 리스트</div> 
						<div style="text-align: right;">
							<form class="form-inline" action="/notice/search" method="get">

								<!-- 검색 설정 -->
								<select class="btn btn-gradient-light" id="top-search-select">
									<option selected="selected" value="noticeTitle">제목</option>
									<option value="noticeContents">내용</option>
									<option value="name">작성자</option>
								</select> 
								
								<input style="display: inline-block;" id="top-search-bar"
									class="form-control" type="search" placeholder="입력 후 [Enter]"
									aria-label="Search">
								<button id="top-search-btn" class="btn btn-info" type="submit">검색</button>

							</form>
						</div>
					</div>

					<ul class="nav-tabs">
						<li onclick="location.href='./list'" class="active"><a class="link-tab">전체</a></li>
						<li data-cd="B011"><a class="link-tab">공지</a></li>
						<li data-cd="B012"><a class="link-tab">문의</a></li>
						<li data-cd="B013"><a class="link-tab">익명</a></li>
					</ul>	


					<div
						style="text-align: right; padding-top: 20px; padding-right: 10px">
						<div id="grid-top-date"></div>
					</div>
					<div id="content">


						<div class="container-fluid">


				  
				  
				
				    <table class="table-bordered mt-2" id="approval-table">
				        <thead>
				           <tr>
				             <th>선택</th>
				             <th>작성일자</th>
				             <th>제목</th>
				             <th>작성자</th>
				             <th>조회수</th>
				           </tr>
				        </thead>
				        <tbody>
				        <c:forEach items="${list}" var="vo" varStatus="i">
				           <tr>
				             <td><input type="checkbox"></td>
				             <td class="approval-start-date">${vo.regDate}</td>
				             <td><a href="/notice/detail?noticeNo=${vo.noticeNo}">${vo.noticeTitle}</a></td>
				             <td>${vo.humanResourceVO.name}</td>
				             <td>${vo.noticeHit}</td>
				           </tr>
				         </c:forEach>
				        </tbody>
				      
				    </table>
				  </div>
				  
				  
				  
				    <!-- pagination -->
				  <div style="text-align: center; margin: 20px 20px">
				  <nav aria-label="Page navigation example" style="display: inline-block;">
  <ul class="pagination">
    <li class="page-item ${pager.pre?'':'disabled'}">
      <a class="page-link" href="/notice/list?page=${startNum-1}" aria-label="Previous">
        <i class="mdi mdi-arrow-left-drop-circle"></i>
      </a>
    </li>
    
    <c:forEach begin="${pager.startNum}" end="${pager.lastNum}" var="i">
    <li class="page-item"><a class="page-link" href="/notice/list?page=${i}">${i}</a></li>
    </c:forEach>
    
    <li class="page-item ${pager.next?'':'disabled'}">
      <a class="page-link" href="/notice/list?page=${lastNum+1}" aria-label="Next">
        <i class="mdi mdi-arrow-right-drop-circle"></i>
      </a>
    </li>
  </ul>
</nav>

  <!-- Button List  -->
				 <div style="float:left;">
				  <button class="btn btn-info" onclick="location.href='/notice/update'">수정</button>
				  <button class="btn btn-info" id="notice-delete-btn">삭제</button>
				 </div>
				 
				  <div style="float: right;">
				  <button class="btn btn-info" onclick="location.href='/notice/add'">글 작성</button>
				 
				 
				</div>

  </div>
				  </div>
				  
    
      
  
      
     
<!-- modal end -->			
	

				</div>
              </div>
			</div>
</div>
		

	
			


	
	<script src="/js/commons/list-date.js"></script>
	<script src="/js/commons/ul-tabs.js"></script>
    
    <!-- 리스트 ul tabs 이동 -->
	<script src="/js/approval/list-move.js"></script>
	<!-- 기안일자 변환 -->
	<script src="/js/approval/approval-date.js"></script>
	
	
	


</body>
</html>