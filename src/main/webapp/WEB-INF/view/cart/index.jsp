<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../layouts/header.jsp" />

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
                    
                   <c:if test="${cartList}">
                  
                        <p>Вы выбрали такие товары:</p>
                        <table class="table-bordered table-striped table">
                            <tr>
                                <th>Код товара</th>
                                <th>Название</th>
                                <th>Стомость, грн</th>
                                <th>Количество, шт</th>
                                 <th>Удалить</th>
                            </tr>
                             <c:forEach var="tempProduct" items="${productsByIds}">
                                <tr>
                                    <td>${tempProduct.code}</td>
                                    <td>
                                        <a href="/product/${tempProduct.id}">
                                            ${tempProduct.name}
                                        </a>
                                    </td>
                                    <td>${tempProduct.price}</td>
                                    <td>${products.get(tempProduct.id)}</td> 
                                    
                                    <td>
                                        <a class="btn btn-default checkout" href="/cart/delete/${tempProduct.id}">
                                            <i class="fa fa-times"></i>
                                        </a>
                                    </td>                        
                                </tr>
                            </c:forEach>
                                <tr>
                                    <td colspan="3">Общая стоимость:</td>
                                    <td>${totalPrice}</td>
                                    
                                    
                                    
                                </tr>
                        </table>
                         <a class="btn btn-default checkout" href="/cart/checkout"><i class="fa fa-shopping-cart"></i> Оформить заказ</a>
                        </c:if>
                     <c:if test="${!cartList}"> 
                      
                        <p>Корзина пуста</p>
                     </c:if>

                </div>

            </div>
        </div>
    </div>
</section>

<jsp:include page="../layouts/footer.jsp" />