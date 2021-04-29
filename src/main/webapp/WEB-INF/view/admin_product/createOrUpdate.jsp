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
                    <li><a href="/admin/product">Управление товарами</a></li>
                    <li class="active">Редактировать товар</li>
                </ol>
            </div>


            <h4>Редактировать товар</h4>

            <br/>

            <div class="col-lg-4">
                <div class="login-form">
                    <form:form action="/admin/product/save" modelAttribute="product" enctype="multipart/form-data" method="POST">
		
					<form:hidden path="id"/>
					
					<form:errors path="name"  cssClass="error"/>
					<form:errors path="image"  cssClass="error"/>
					<form:errors path="brand"  cssClass="error"/>
					<form:errors path="description"  cssClass="error"/>

                        <p>Название товара</p>
                       <form:input path="name" />

                        <p>Артикул</p>
                        <form:input path="code"/>

                        <p>Стоимость, $</p>
                       <form:input path="price"/>

                        <p>Категория</p>
						<form:select path="category_id">
						
						<c:forEach var="tempCategory" items="${categories}">
                        
                          <form:option value="${tempCategory.id}" label="${tempCategory.name}"/>
                                
                         </c:forEach>
						
						</form:select>
                        <br/><br/>

                        <p>Производитель</p>
                       <form:input path="brand"/>
						<img src="${productService.getImage(product.id)}" width="200" alt="" />
                        
                        <p>Изображение товара</p>
                        <form:input type="file" path="image"/>

                        <p>Детальное описание</p>
                        <form:textarea path="description"/>

                        <br/><br/>

                        <p>Наличие на складе</p>
                         <form:select path="availability">
                            <form:option value="1" label="Да"/>
                            <form:option value="0" label="Нет"/>
                       </form:select>


                        <br/><br/>

                        <p>Новинка</p>
                        <form:select path="is_new">
                            <form:option  value="1" label="Да"/>
                            <form:option  value="0" label="Нет"/>
                         </form:select>

                        <br/><br/>

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