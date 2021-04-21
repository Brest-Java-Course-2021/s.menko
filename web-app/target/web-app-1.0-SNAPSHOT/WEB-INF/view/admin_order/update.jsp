<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<section>
    <div class="container">
        <div class="row">

            <br/>

            <div class="breadcrumbs">
                <ol class="breadcrumb">
                    <li><a href="/admin">Админпанель</a></li>
                    <li><a href="/admin/order">Управление заказами</a></li>
                    <li class="active">Редактировать заказ</li>
                </ol>
            </div>


            <h4>Редактировать заказ</h4>

            <br/>

            <div class="col-lg-4">
                <div class="login-form">
                    <form:form action="/admin/order/save" modelAttribute="order" method="POST">
		
					<form:hidden path="id"/>		
					<form:hidden path="userId"/>
					<form:errors path="userName"  cssClass="error"/>
					<form:errors path="userPhone"  cssClass="error"/>

                        <p>Имя клиента</p>
                       <form:input path="userName" />

                        <p>Телефон клиента</p>
                        <form:input path="userPhone"/>
                        
                         <p>Комментарий клиента</p>
                        <form:input path="userComment"/>
 
					<form:input type="datetime-local" path="dateTime" />
                       <p>Статус</p>
                        <form:select path="status">
                            <form:option  value="1" label="Новый заказ"/>
                            <form:option  value="2" label="В обработке"/>
                            <form:option  value="3" label="Добавляется"/>
                            <form:option  value="4" label="Закрыт"/>
                        </form:select>

                        <br/><br/>

                        <input type="submit" name="submit" class="btn btn-default" value="Сохранить">

                        <br/><br/>

                   </form:form>
                </div>
            </div>

        </div>
    </div>
</section>

<jsp:include page="../layouts/footer_admin.jsp" />