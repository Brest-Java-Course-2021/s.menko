<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
	<style>
		.error {color:red}
	</style>
</head> 

<section>
    <div class="container">
        <div class="row">

            <br/>

            <div class="breadcrumbs">
                <ol class="breadcrumb">
                    <li><a href="/admin">Админпанель</a></li>
                    <li class="active">Управление заказами</li>
                </ol>
            </div>
            
            <h4>Список заказов</h4>

            <br/>

 <form:form  accept-charset="UTF-8" action="/admin/order" modelAttribute="dataInterval">
 
 						<br>
						С  <form:input type="datetime-local" path="StartData" />
						
						По <form:input type="datetime-local" path="EndData" />
						
 <input type="submit" name="submit" class="btn btn-default" value="Отсортировать">
 <c:if test="${!dataValidation}"> 
	 <font color="red">
 		Начальная дата больше, чем конечная!
 	</font>
 </c:if>
 
 </form:form> 
 <c:if test="${!orders.isEmpty()}"> 

            <table class="table-bordered table-striped table">
                <tr>
                    <th>ID заказа</th>
                    <th>Имя покупателя</th>
                    <th>Телефон покупателя</th>
                    <th>Дата оформления</th>
                    <th>Статус</th>
                    <th></th>
                    <th></th>
                    
                </tr>
               <c:forEach var="temporder" items="${orders}">
               
               		<c:url var="viewLink" value="/admin/order/view/">
						<c:param name="orderId" value="${temporder.id}" />
					</c:url>
					
					<c:url var="updateLink" value="/admin/order/update/">
						<c:param name="orderId" value="${temporder.id}" />
					</c:url>						

					<!-- construct an "delete" link with customer id -->
					<c:url var="deleteLink" value="/admin/order/delete/">
						<c:param name="orderId" value="${temporder.id}" />
					</c:url>	
                    <tr>
                        <td>${temporder.id}</td>
                        <td>${temporder.userName}</td>
                        <td>${temporder.userPhone}</td>
                        <td>${temporder.dateTime.format(dataFormat)}</td> 
                        <td><a href="${viewLink}" title="Смотреть"><i class="fa fa-eye"></i></a></td> 
                        <td><a href="${updateLink}" title="Редактировать"><i class="fa fa-pencil-square-o"></i></a></td>
                        <td><a href="${deleteLink}" onclick="if (!(confirm('Are you sure you want to delete this order?'))) return false" title="Удалить"><i class="fa fa-times"></i></a></td>
                    </tr>
                </c:forEach>
            </table>
</c:if>
<c:if test="${orders.isEmpty()}"> 
<p align="center" font-size:30px"> Нет заказов!</p>
</c:if>
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer_admin.jsp" />