<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FEP Calendar - ${name}</title>
<link rel="stylesheet" type="text/css" href="/css/bokwire.css" />
<meta charset="EUC-KR" http-equiv="refresh" content="600">
<script>
function saveItem(key, value) {
	var filterKey = document.getElementById("filterKey").value;
	if ( filterKey != "" ) {
		alert("필터 해제 필요!");
		return;
	}
	if ( window.event.keyCode == 9 ) {
		document.frm.key.value = key;	
		document.frm.value.value = value;
		document.frm.startDay.value = document.getElementById("startDay").value;
		document.frm.submit();
	}
}
function eventFilter(name, check) {
	document.href="/manager/calender/${name}"
}
function openSearch(name, year) {
	var key = document.getElementById("searchkey").value;
	window.open('/manager/calfind?name=' + name + '&year=' + year + '&searchkey=' + key,'FEP CALENDER SEARCH','width=700, height=850');
}
function holidayCheck() {
	var table = document.getElementById("mainTable");
	var tds = table.getElementsByTagName("td");
	for (i=0; i<tds.length; i++) {
	    if ( tds[i].innerText.indexOf("*holiday*") > 0 ) {
	        tds[i].style.backgroundColor="#FFDEE9";
	    }
	    if ( tds[i].innerText.indexOf("*vacation*") > 0 ) {
	        tds[i].style.backgroundColor="#F6FFDD";
	    }
	}
	var sendMsg = "${sendMsg}";
	if ( sendMsg != "" ) {
		alert(sendMsg);
	}
}
</script>
</head>
<body onload="holidayCheck();">
<div style="float: right; padding-right: 15px; padding-top:12px;">
	<span style="color: #FFF;">
		<b style="cursor: pointer;" onclick="location.href='/logout';">${sessionUserName }님 안녕하세요</b> | 
		Make Money
	</span>
	<img src="/images/kbank_logo.gif" height="30px" style="vertical-align: middle;" />
</div>
<h1 style="font-family: se-nanumsquare;">
	<c:choose>
		<c:when test="${name eq 'mkt'}" >
		마케팅/배포 일정공유 캘린더  - ${yearInt }년 ${monthInt }월 ${dayInt }일
		</c:when>
		<c:otherwise>
		FEP 오픈캘린더(${name})  - ${yearInt }년 ${monthInt }월
		</c:otherwise>
	</c:choose>
</h1>
<ul><li>
<a href="/manager/calendar/${name}?year=${yearInt }&month=${monthInt-1 }&key=&value=&filterKey=${filterKey}">이전달</a> |
<a href="/manager/calendar/${name}?year=${yearInt }&month=${monthInt+1 }&key=&value=&filterKey=${filterKey}">다음달</a> | 
시작일자 : <input type="text" id="startDay" value="${startDay}" style="width: 30px;" autocomplete="off"/>
<input type="button" value="SUBMIT" onclick="location.href='/manager/calendar/${name}?year=${yearInt }&month=${monthInt}&startDay='+document.getElementById('startDay').value;" />
| 달력검색 : <input type="text" id="searchkey"  style="width: 150px;" autocomplete="off"/> <input type="button" value="SEARCH" onclick="openSearch('${name}', '${yearInt}');" />
| 필터 : <input type="text" id="filterKey" style="width: 150px;" autocomplete="off" value="${filterKey}"/> 
<input type="button" value="필터" onclick="location.href='/manager/calendar/${name}?year=${yearInt}&month=${monthInt}&filterKey='+document.getElementById('filterKey').value;" />
</li></ul>
<table style="width: 100%; table-layout:fixed;" border=1 id="mainTable">
<tr>
	<th style="width: 10%;">일</th>
	<th style="width: 16%;">월</th>
	<th style="width: 16%;">화</th>
	<th style="width: 16%;">수</th>
	<th style="width: 16%;">목</th>
	<th style="width: 16%;">금</th>
	<th style="width: 10%;">토</th>
</tr>
<c:set var="isContinue" value="true"/>
<c:set var="tdColor" value="#FFFFFF" /><!-- E1F6FA -->
<c:forEach var="row" items="${dayTable}" varStatus="row_status">
<c:if test="${isContinue eq 'true'}">
	<tr>
		<c:forEach var="col" items="${row}" varStatus="cal_status">
		<c:choose>
			<c:when test="${col > 0}">
				<c:set var="tdColor" value="#FFFFFF" />
				<c:if test="${cal_status.first}"><c:set var="tdColor" value="#E1F6FA" /></c:if>
				<c:if test="${cal_status.last}"><c:set var="tdColor" value="#E1F6FA" /></c:if>
				<td valign="top" style="font-size: 10pt; line-height: 140%; background-color: ${tdColor};">
					<b>${col}</b>
					<c:if test="${col == dayInt}"><font color="blue"> Today</font></c:if>
					<br/>
					<c:if test="${col >= startDay}">
						<div contenteditable='true' onkeydown="saveItem('CAL.${yearInt}.${monthInt}.${col}', this.innerHTML);"  >
							<c:set var="tempKey">CAL.${yearInt}.${monthInt}.${col}</c:set>
							${contents[tempKey]}
						</div>
					</c:if>
				</td>
			</c:when>
			<c:otherwise>
				<td style="background-color: #F3F3F2">
					&nbsp;
				</td>
				<c:if test="${row_status.index > 1}">
					<c:set var="isContinue" value="false"/>
				</c:if>
			</c:otherwise>
		</c:choose>
		</c:forEach>
	</tr>
</c:if>
</c:forEach>
</table>
<form name="frm" action="/manager/calendar/${name}" method="POST">
	<input type="hidden" name="key" value="" />
	<input type="hidden" name="value" value="" />
	<input type="hidden" name="year" value="${yearInt}" />
	<input type="hidden" name="month" value="${monthInt}" />
	<input type="hidden" name="startDay" value="${startDay }"/>
	</form>
</body>
</html>