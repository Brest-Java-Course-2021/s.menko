<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
         <meta charset="utf-8">
        <title>Админпанель</title>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
           <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/ico/favicon.ico">
   		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    </head><!--/head-->
    <body >

        <div class="page-wrapper">
            <header id="header"><!--header-->
                <div class="header_top"><!--header_top-->
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="contactinfo">
                                    <h5>
                                        <a href="/admin"><i class="fa fa-edit"></i> Админпанель</a>
                                    </h5>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="social-icons pull-right">
                                    <ul class="nav navbar-nav">
                                        <li><a href="/"><i class="fa fa-sign-out"></i>На сайт</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--/header_top-->