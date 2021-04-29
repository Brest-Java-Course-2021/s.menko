<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../layouts/header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <section>
            <div class="container">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="left-sidebar">
                            <h2>Каталог</h2>
                            <div class="panel-group category-products" id="accordian"><!--category-productsr-->
                                  <c:forEach var="tempCategory" items="${categories}">
                                
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title"><a href="/catalog/category/${tempCategory.id}"
        
                                        class=" <c:if test="${tempCategory.id == tempId}"><c:out value="active"/> </c:if> ">
                                      
                                        
                                        ${tempCategory.name}</a></h4>
                                    </div>
                                </div>
                               </c:forEach>
                            </div><!--/category-products-->

                        </div>
                    </div>

                    <div class="col-sm-9 padding-right">
                        <div class="product-details"><!--product-details-->
                            <div class="row">
                                <div class="col-sm-5">
                                    <div class="view-product">
                                        <img src="${productService.getImage(product.id)}" alt="" />
                                    </div>
                                </div>
                                <div class="col-sm-7">
                                    <div class="product-information"><!--/product-information-->
                                        <img src="${pageContext.request.contextPath}/resources/images/product-details/new.jpg" class="newarrival" alt="" />
                                        <h2> ${product.name}</h2>
                                        <p>Код товара: ${product.code}</p>
                                        <span>
                                            <span>US ${product.price}</span>
                            
                                        </span>
                                        <p><b>Наличие:</b> 
                                        
                                        <c:if test="${product.availability == 1}"><c:out value="на складе"/> </c:if> 
                                         <c:if test="${product.availability == 0}"><c:out value="отсутствует"/> </c:if>
                                        </p>
                                        <c:if test="${product.is_new == 1}"><p><b>Состояние:</b>New</p></c:if> 
                                        <p><b>Производитель:</b>  ${product.brand}</p>
                                    </div><!--/product-information-->
                                </div>
                            </div>
                            <div class="row">                                
                                <div class="col-sm-12">
                                    <h5>Описание товара</h5>
                                    ${product.description}
                                </div>
                            </div>
                        </div><!--/product-details-->

                    </div>
                </div>
            </div>
        </section>
        

        <br/>
        <br/>
        
        <jsp:include page="../layouts/footer.jsp" />