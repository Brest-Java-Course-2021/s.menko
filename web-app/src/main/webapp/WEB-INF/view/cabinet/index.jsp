<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<section>
    <div class="container">
        <div class="row">

            <h1>Кабинет пользователя</h1>
            
            <h3>Привет, ${user.name}</h3>
            <ul>
                <li><a href="/cabinet/edit">Редактировать данные</a></li>
            </ul>
            
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer.jsp" />