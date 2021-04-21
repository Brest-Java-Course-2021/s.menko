<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />

<section>
    <div class="container">
        <div class="row">
${MessageCategory}
            <br/>

            <div class="breadcrumbs">
                <ol class="breadcrumb">
                    <li><a href="/admin">Админпанель</a></li>
                    <li class="active">Управление категориями</li>
                </ol>
            </div>

            <a href="/admin/category/create" class="btn btn-default back"><i class="fa fa-plus"></i> Добавить категорию</a>
            
            <h4>Список категорий</h4>

            <br/>

            <table class="table-bordered table-striped table">
                <tr>
                    <th>ID категории</th>
                    <th>Название категории</th>
                    <th>Порядковый номер</th>
                    <th>Статус</th>
                    <th></th>
                    <th></th>
                </tr>
               <c:forEach var="tempcategory" items="${categories}">
               
               		<c:url var="updateLink" value="/admin/category/update/">
						<c:param name="categoryId" value="${tempcategory.id}" />
					</c:url>					

					<!-- construct an "delete" link with customer id -->
					<c:url var="deleteLink" value="/admin/category/delete/">
						<c:param name="categoryId" value="${tempcategory.id}" />
					</c:url>	
                    <tr>
                        <td>${tempcategory.id}</td>
                        <td>${tempcategory.name}</td>
                        <td>${tempcategory.sortOrder}</td>
                        <td>${tempcategory.status}</td>  
                        <td><a href="${updateLink}" title="Редактировать"><i class="fa fa-pencil-square-o"></i></a></td>
                        <td><a href="${deleteLink}" onclick="if (!(confirm('Are you sure you want to delete this category?'))) return false" title="Удалить"><i class="fa fa-times"></i></a></td>
                    </tr>
                </c:forEach>
            </table>

        </div>
    </div>
</section>

<jsp:include page="../layouts/footer_admin.jsp" />