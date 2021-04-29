 <%@ page language="java" contentType="text/html;charset=UTF-8"%>   
    <div class="page-buffer"></div>
</div>

<footer id="footer" class="page-footer"><!--Footer-->
    <div class="footer-bottom">
        <div class="container">
            <div class="row">
                <p class="pull-left">Copyright © 2021</p>
                <p class="pull-right">Курс Java Start Epam</p>
            </div>
        </div>
    </div>
</footer><!--/Footer-->



 <script src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>
 <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script> 


$(document).ready(function() {
	if(${MessageProduct!=null})
		alert("Вы не можете удалить данный продукт, потому что он находится в заказе!")
	if(${MessageCategory!=null})
		alert("Вы не можете удалить данную категорию, потому что ее продукты находятся в заказе!")
});
</script>	
</body>
</html>