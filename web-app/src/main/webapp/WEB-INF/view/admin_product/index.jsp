<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />

<section>
   
    <div class="container" >
        <div class="row">
            <br/>

            <div class="breadcrumbs">
                <ol class="breadcrumb">
                    <li><a href="/admin">Админпанель</a></li>
                    <li class="active">Управление товарами</li>
                </ol>
            </div>

            <a href="/admin/product/create" class="btn btn-default back"><i class="fa fa-plus"></i> Добавить товар</a>
            
            <h4>Список товаров</h4>

            <br/>

            <table class="table-bordered table-striped table">
                <tr>
                    <th>ID товара</th>
                    <th>Артикул</th>
                    <th>Название товара</th>
                    <th>Цена</th>
                    <th></th>
                    <th></th>
                </tr>
               <c:forEach var="tempproduct" items="${allProducts}">
               
               		<c:url var="updateLink" value="/admin/product/update/">
						<c:param name="productId" value="${tempproduct.id}" />
					</c:url>					

					<!-- construct an "delete" link with customer id -->
					<c:url var="deleteLink" value="/admin/product/delete/">
						<c:param name="productId" value="${tempproduct.id}" />
					</c:url>	
                    <tr>
                        <td>${tempproduct.id}</td>
                        <td>${tempproduct.code}</td>
                        <td>${tempproduct.name}</td>
                        <td>${tempproduct.price}</td>  
                        <td><a href="${updateLink}" title="Редактировать"><i class="fa fa-pencil-square-o"></i></a></td>
                        <td><a href="${deleteLink}" onclick="if (!(confirm('Are you sure you want to delete this product?'))) return false" title="Удалить"><i class="fa fa-times"></i></a></td>
                    </tr>
                </c:forEach>
            </table>

        </div>
    </div>
</section>

<jsp:include page="../layouts/footer_admin.jsp" />