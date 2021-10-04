<%@page import="com.fly.pro2.DTO.HotelDTO"%>
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
<!-- model.속성으로 지정한 것은 $로 쓸 수 있다. -->
<%-- 전체 프린트: ${list}<br> --%>
<%-- 전체 크기: ${list.size()}<br> --%>
<%-- 키워드의 이미지 프린트: ${list2} --%>
<table border=1>
	<tr bgcolor="#0A82FF" align="center" style="color: white; height: 50px">
		<td width="300">이미지</td>
		<td width="200">호텔명</td>
		<td width="150">평점</td>
		<td width="200">주소</td>
		<td width="200">스탠다드 가격</td>
		<td width="200">객실 선택</td>
	</tr>
<%-- items=리스트가 받아올 배열명, var=for문 내에서 사용할 변수 --%>	
<c:forEach items="${list}" var="bag">
	<tr align="center">
		<td width="300"><img src="resources/img/hotel/${bag.hid}.jpg" width="300" height="150"></td>
		<td width="200">${bag.hname}</td>
		<td width="150">
		<%
			//jsp의 내장된 객체 pageContext로 forEach 안의 dto를 추출
			//pageContext는 jstl과 함께 쓰며, forEach의 items는 반드시 리스트 형태일 것
			HotelDTO dto = (HotelDTO)pageContext.getAttribute("bag"); //()안에는 for문에서 var로 지정한 값
			String hid = dto.getHid();
			//out.print(hid);
			char c = hid.charAt(11);
			int c2 = Integer.parseInt(String.valueOf(c));
			// String으로 변환하는 메서드 2개
			// String.valueOf() - 파라미터가 null이면 문자열 "null"을 반환
			// toString() - 대상 값이 null이면 널포인트 에러를 발생시키고 Object에 담긴 값이 String이 아니어도 출력
		%>
			<% for (int i = 0; i < c2; i++) { %>
				<img src="resources/img/hotel/star.jpg" width="20" height="20">
			<% } %>
		</td>
		<td width="200">${bag.hregion}</td>
		<td width="200">${bag.stdprice}</td>
		<td width="200">
<%-- 			<a href="htDetail.jsp?hid=${bag.hid}&himage=${bag.hid}.jpg&checkin=${checkin} --%>
<%-- 			&checkout=${bag.checkout}&guestnum=${bag.guestnum}"> --%>

			<a href="htDetail.jsp?hid=${bag.hid}&himage=${bag.hid}.jpg&checkin=${checkin}&checkout=${checkout}
			&stdnum=${stdnum}&guestnum=${guestnum}">
				<button style="background: #FE5C24; color: white; border:none; padding: 5px">객실 선택</button>
			</a>
		</td>
	</tr>
</c:forEach>
</table>
</body>
</html>