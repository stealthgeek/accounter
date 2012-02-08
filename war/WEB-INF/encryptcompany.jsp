<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>

<title>Accounter | Subscription </title>
<link type="text/css" href="../../css/ss.css" rel="stylesheet" />
<%
String info =(String) request.getAttribute("info");
%>
<%@ include file="./feedback.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
$('#submitButton').click(function() {
	$("#accounterForm").validate({
		rules: {
			pass1: "required",
			pass2: {
				required: true,
				equalTo: "#pass1"
			}
		},
		messages: {
			pass1: "<i18n:i18n msg='pleaseenteryourpassword'/>",
			pass2: {
				required: "<i18n:i18n msg='pleaseconfirmyourpassword'/>",
				equalTo: "<i18n:i18n msg='pleaseenterthesamepasswordasabove'/>"
			}
		}
	});
});

});	



</script>

</head>


<body align="center">


		<div id="commanContainer" class="encryptContainer">
		
			<tr><td><c:if test="${info!=null}"> 
					<div id="login_success" class="common-box">
					<span>${info}</span>
			</div>
			</c:if>
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
			  <form align="center" id="accounterForm"   action="/main/encryption" style="width:650px" class="accounterform" method="post">
			  <div id="content" >
			  <div  ><i18n:i18n msg='encryptionEnablesyourCompanydata'/></div>
			  <div  ><i18n:i18n msg='encryptionPasswordisImportant'/></div>
			  </div><div>
			 <input type="hidden" name="on0" value="Companies list">Companies list
			<c:if test="${companeyList != null}">
			<select name="companyname">
			<c:forEach var="company" items="${companeyList}">
			<option value="${company}">${company}</option> 
			</c:forEach></select> 
			</c:if>
			 Enter password
			<input type="password" id= "pass1" name="pass1"  >Confirm password
			<input type="password" id= "pass2"  name="pass2"  >
			<input type="submit" id="submitButton" name="submit" class="allviews-common-button" value="submit">
			 <a   href="/main/logout" ><i18n:i18n msg='logout'/></a>
			<div>
			</div>
		</div>
		   </form>
		   <div class="form-bottom-options">
		     
		   </div>
		   </div>
	     <!-- Footer Section-->
		
	 	
			<div id="mainFooter"  >
		    <div>
		       <span><i18n:i18n msg='atTherateCopy'/></span> |
		       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
		       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
		       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
		    </div>
		</div>
			

 
</body>
</html>