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
					<c:if test="${save == true}"> <font color="green">Data edited!</font> </c:if>
					<c:if test="${save == false}">
                        <form:form  accept-charset="UTF-8" action="/cabinet/edit/save" modelAttribute="user">
	 					
	 					<form:errors path="name" cssClass="error" /><br>
	 					<form:errors path="password" cssClass="error" /><br>
	 					<form:hidden path="id"/>
	 					<form:hidden path="email"/>
	 					<form:hidden path="role"/>
	 					<h2>Редактирование данных</h2>
		
						New Name : <form:input path="name" />
						<br>
						New Password: <form:input type="password" path="password" />
						<br>
						
						
						<input type="submit" value="Сохранить" />
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