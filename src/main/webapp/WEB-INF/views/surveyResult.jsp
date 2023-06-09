<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.3.min.js"></script>
<style>
	table, th, td{
      border: 2px solid green;
      border-collapse: collapse;
      padding: 5px 10px;
   }
   th{
   	font-size: 14px;
   }
	td{
		text-align: center;
		font-size: 13px;
	}
</style>
</head>
<body>
	<table>
		<tr>
			<th rowspan="6">답변의 대한 점수는 다음과 같습니다.</th>
		</tr>
		<tr>
			<td>매우 그렇지 않다. 1점</td>
		</tr>
		<tr>
			<td>그렇지 않다. 2점</td>
		</tr>
		<tr>
			<td>보통이다. 3점</td>
		</tr>
		<tr>
			<td>그렇다. 4점</td>
		</tr>
		<tr>
			<td>매우 그렇다. 5점</td>
		</tr>
		<tr>
			<th >사용자 ${result.user_id_value} 님의 점수</th>
			<th>${result.total_score_value} 점</th>
		</tr>
		<tr>
			<th>사용자 ${result.user_id_value} 님의 비건 단계</th>
			<th>${result.step}</th>
		</tr>
		<tr>
			<th colspan="2">
				<button onclick="location.href='main.go'">메인</button>
    			<button onclick="location.href='surveyReset.do?retry=true'">다시하기</button>
			</th>
		</tr>
	</table>
    <!-- 01~10점 플렉시테리언 -->
    <!-- 11~20점 폴로 페스코 베지테리언 -->
    <!-- 21~30점 페스코 베지테리언 -->
    <!-- 31~40점 락토 오보 베지테리언 -->
    <!-- 41~50점 비건 -->
</body>
<script></script>
</html>
