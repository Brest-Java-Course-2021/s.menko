<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />

<c:if test="${error == null}">
<section>
    <div class="container">
        <div class="row">
            
            <br/>
            
            <h4>Добрый день, администратор!</h4>
            
            <br/>
            
            <p>Вам доступны такие возможности:</p>
            
            <br/>
            
            <ul>
                <li><a href="/admin/product">Управление товарами</a></li>
                <li><a href="/admin/category">Управление категориями</a></li>
                <li><a href="/admin/order">Управление заказами</a></li>
            </ul>
            
        </div>
    </div>
</section>
</c:if> 
<c:if test="${error != null}">
	<p align="center">${error} </p>
</c:if> 
<jsp:include page="../layouts/footer_admin.jsp" />