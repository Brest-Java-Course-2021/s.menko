<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header_admin.jsp" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section>
    <div class="container">
        <div class="row">

            <br/>

            <div class="breadcrumbs">
                <ol class="breadcrumb">
                    <li><a href="/admin">Админпанель</a></li>
                    <li><a href="/admin/order">Управление заказами</a></li>
                    <li class="active">Просмотр заказа</li>
                </ol>
            </div>


            <h4>Просмотр заказа №${order.id}</h4>
            <br/>




            <h5>Информация о заказе</h5>
            <table class="table-admin-small table-bordered table-striped table">
                <tr>
                    <td>Номер заказа</td>
                    <td>${order.id}</td>
                </tr>
                <tr>
                    <td>Имя клиента</td>
                    <td>${order.userName}</td>
                </tr>
                <tr>
                    <td>Телефон клиента</td>
                    <td>${order.userPhone}</td>
                </tr>
                <tr>
                    <td>Комментарий клиента</td>
                    <td>${order.userComment}</td>
                </tr>
              <c:if test="${order.userId != null}">
                    <tr>
                        <td>ID клиента</td>
                        <td>${order.userId}</td>
                    </tr>
               </c:if> 
                <tr>
                    <td><b>Статус заказа</b></td>
                    <td>${orderService.getStatusText(order.status)}</td>
                </tr>
                <tr>
                    <td><b>Дата заказа</b></td>
                    <td>${order.dateTime.format(dataFormat)}</td>
                </tr>
            </table>

            <h5>Товары в заказе</h5>

            <table class="table-admin-medium table-bordered table-striped table ">
                <tr>
                    <th>ID товара</th>
                    <th>Артикул товара</th>
                    <th>Название</th>
                    <th>Цена</th>
                    <th>Количество</th>
                </tr>
                 <c:forEach var="tempProduct" items="${products}">
                    <tr>
                        <td>${tempProduct.id}</td>
                        <td>${tempProduct.code}</td>
                        <td>${tempProduct.name}</td>
                        <td>${tempProduct.price}</td>
                        <td>${quantity.get(tempProduct.id)}</td>
                    </tr>
                </c:forEach>
                
                <tr>
                      <td colspan="4">Общая стоимость:</td>
                      <td>${totalPrice}</td>
                  </tr>
            </table>

            <a href="/admin/order/" class="btn btn-default back"><i class="fa fa-arrow-left"></i> Назад</a>
        </div>


</section>

<jsp:include page="../layouts/footer_admin.jsp" />