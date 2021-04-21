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
            <div class="col-sm-3">
                <div class="left-sidebar">
                    <h2>Каталог</h2>
                     <div class="panel-group category-products">
                            
                                <c:forEach var="tempCategory" items="${categories}">
                                
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title"><a href="/catalog/category/${tempCategory.id}">${tempCategory.name}</a></h4>
                                    </div>
                                </div>
                               </c:forEach>
                            
                     </div>
                </div>
            </div>

            <div class="col-sm-9 padding-right">
                <div class="features_items">
                    <h2 class="title text-center">Корзина</h2>

 					<c:if test="${!errorExist}"> 
                        <font color="green">Заказ оформлен! Мы вам перезвоним!</font>
					</c:if>
                   <c:if test="${errorExist}"> 

                        <p>Выбрано товаров: ${countItrems}, на сумму: ${totalPrice}, руб.</p><br/>

                        <div class="col-sm-4">
                          
                            <p>Для оформления заказа заполните форму. Наш менеджер свяжется с Вами.</p>

                            <div class="login-form">
                                <form:form  accept-charset="UTF-8" action="/cart/checkout/process" modelAttribute="order">
	 					
	 									<form:errors path="userName"  cssClass="error"/>
	 				    				<form:errors path="userPhone" cssClass="error"/>
	 				    				<form:errors path="userComment" cssClass="error"/>
	 									
	 					
	 					
	 					<h2>Оформление заказа:</h2>
		
						Ваше имя : <form:input path="userName" />
						<br>
						Номер телефона: <form:input path="userPhone" />
						<br>
						Комментарий к заказу: <form:input path="userComment" />
						
						<!--  Дата:<form:input type="datetime-local" path="dateTime"/> -->  
						<br>
		    			<input type="submit" value="Submit" />

					</form:form>  
                        </div>

                    </c:if>

                </div>

            </div>
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer.jsp" />