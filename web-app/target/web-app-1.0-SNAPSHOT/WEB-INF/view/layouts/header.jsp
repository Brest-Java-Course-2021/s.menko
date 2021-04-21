<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Главная</title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/ico/favicon.ico">
    </head><!--/head-->

    <body>
        <header id="header"><!--header-->

            <div class="header-middle">
                <div class="container">
                    <div class="row">

                        <div class="col-sm-12">
                            <div class="shop-menu pull-right">
                                <ul class="nav navbar-nav">                                    
                                    <li><a href="/cart"><i class="fa fa-shopping-cart"></i> Корзина
                                    <span id="cart-count"><c:if test="${countItrems!=null}">(${countItrems})</c:if></span>
                                    </a></li>
                                   <c:if test="${usersId==null}">
                                        <li><a href="/user/login"><i class="fa fa-lock"></i> Вход</a></li>
                                  		<li><a href="/user/register"> Регистрация</a></li>
                                   </c:if>
                                   <c:if test="${usersId!=null}">
                                    <li><a href="/cabinet"><i class="fa fa-user"></i> Аккаунт</a></li>
                                    <li><a href="/user/logout"><i class="fa fa-unlock"></i> Выход</a></li>
                                    <li><a href="/admin"><i class="fa fa-edit"></i> Админпанель</a></li>
                                   
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--/header-middle-->

            <div class="header-bottom"><!--header-bottom-->
                <div class="container">
                    <div class="row">
                        <div class="col-sm-12">
                 
                            <div class="mainmenu pull-left">
                                <ul class="nav navbar-nav collapse navbar-collapse">
                                    <li><a href="/">Главная</a></li>
                                    <li class="dropdown"><a href="#">Магазин<i class="fa fa-angle-down"></i></a>
                                        <ul role="menu" class="sub-menu">
                                            <li><a href="/catalog">Каталог товаров</a></li>
                                            <li><a href="/cart">Корзина</a></li> 
                                        </ul>
                                    </li> 
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--/header-bottom-->
            
        </header><!--/header-->