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
			
                        <form:form  accept-charset="UTF-8" action="/user/processLogin" modelAttribute="user">					
	 					
	 					<form:errors path="email" cssClass="error" /><br>
	 					<form:errors path="password" cssClass="error" /><br>
	 					<font color="red">${error}</font>
	 					
	 					<h2>Вход на сайт</h2>
		
						Email: <form:input path="email" />
						<br>
						Password: <form:input type="password" path="password" />
						<br>
		    			<input type="submit" value="Submit" />

					</form:form>  
					                                              
                    </div><!--/sign up form-->
                
                <br/>
                <br/>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer.jsp" />