<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
	<style>
		.error {color:red}
	</style>
</head>
<section>
    <div class="container">
        <div class="row">

            <div class="col-sm-4 col-sm-offset-4 padding-right">
                

                    <div class="signup-form"><!--sign up form-->
					<c:if test="${register == true}"> <font color="green">You are registered successfully!!!</font> </c:if>
					<c:if test="${register == false}">
                        <form:form  accept-charset="UTF-8" action="/user/processForm" modelAttribute="user">
	 					
	 					<form:errors path="name"  cssClass="error"/>
	 				    <form:errors path="password" cssClass="error"/>
	 				    <form:errors path="email" cssClass="error"/>
	 					<c:if test="${!notEmailExist}"><font color="red">Такой email уже существует! </font></c:if>
	 					
	 					
	 					<h2>Регистрация на сайте</h2>
		
						Name : <form:input path="name" />
						<br>
						Email: <form:input path="email" />
						<br>
						Password: <form:input type="password" path="password" />
						<br>
		    			<input type="submit" value="Submit" />

					</form:form>  
					</c:if>                                               
                    </div><!--/sign up form-->
                
                <br/>
                <br/>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer.jsp" />