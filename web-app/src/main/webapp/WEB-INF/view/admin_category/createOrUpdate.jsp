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
                    <li><a href="/admin/category">Управление категориями</a></li>
                    <li class="active">Редактировать категорию</li>
                </ol>
            </div>


            <h4>Редактировать категорию</h4>

            <br/>

            <div class="col-lg-4">
                <div class="login-form">
                    <form:form action="/admin/category/save" modelAttribute="category" method="POST">
		
					<form:hidden path="id"/>
					<form:errors path="name"  cssClass="error"/>

                        <p>Название категории</p>
                       <form:input path="name" />

                        <p>Порядковый номер</p>
                        <form:input path="sortOrder"/>

                       <p>Статус</p>
                        <form:select path="status">
                            <form:option  value="1" label="Отображается"/>
                            <form:option  value="0" label="Скрыт"/>
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