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
                        <div class="features_items"><!--features_items-->
                            <h2 class="title text-center">Последние товары</h2>
                            
                             <c:forEach var="tempProduct" items="${products}">
                            <div class="col-sm-4">
                                <div class="product-image-wrapper">
                                    <div class="single-products">
                                        <div class="productinfo text-center">
                                            <img src="${productService.getImage(tempProduct.id)}" alt="" />
                                            <h2>${tempProduct.price}</h2>
                                            <p><a href="/product/${tempProduct.id}">${tempProduct.name}</a></p>
                                            <a href="#" data-id="${tempProduct.id}" class="btn btn-default add-to-cart"><i class="fa fa-shopping-cart"></i>В корзину</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                      		</c:forEach>

                        </div><!--features_items-->

                    </div>
                </div>
            </div>
        </section>

         <jsp:include page="../layouts/footer.jsp" />