 <%@ page language="java" contentType="text/html;charset=UTF-8"%>
 <footer id="footer"><!--Footer-->
            <div class="footer-bottom">
                <div class="container">
                    <div class="row">
                        <p class="pull-left">Copyright © 2021</p>
                        <p class="pull-right">Курс Java Epam </p>
                    </div>
                </div>
            </div>
        </footer><!--/Footer-->



        <script src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>

        <script>
    	$(document).ready(function(){
        $(".add-to-cart").click(function () {
            var id = $(this).attr("data-id");
            $.post("/cart/add/"+id, {}, function (data) {
                $("#cart-count").html(data);
            });
            return false;
        });
    });
</script>
        
    </body>
</html>