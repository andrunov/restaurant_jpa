<%--shows content of order. Uses for update exist order
or as finish 4-th step of creation new order--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="messages.app"/>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<script type="text/javascript" src="resources/js/datatableUtil.js" defer></script>
<script type="text/javascript" src="resources/js/orders_dishesUtil.js" defer></script>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <div class="jumbotron">
        <div class="shadow">
            <h3>${currentUser.name}, ${currentUser.email}</h3>
            <p style="color: darkgreen"><fmt:message key="order.from"/>  ${localDate}; <span style="color: #b26301">${restaurant.name}, ${restaurant.address}</span> - <span style="color: darkred"><span id="totalPriceHeader">${totalPrice}</span> <fmt:message key="common.rub"/></span></p>
            <div class="view-box">
                <a class="btn btn-primary" type="button" onclick="openDishList()">
                    <span class="glyphicon glyphicon-plus-sign"></span>
                    <fmt:message key="menuLists.open"/>
                </a>
                <table class="table table-hover table-bordered " id="datatable">
                    <thead>
                    <tr>
                        <th><fmt:message key="dishes.description"/></th>
                        <th><fmt:message key="dishes.price"/></th>
                        <th class="col-xs-1"><span class="glyphicon glyphicon-plus"></span></th>
                        <th class="col-xs-1"><fmt:message key="common.quantity"/></th>
                        <th class="col-xs-1"><span class="glyphicon glyphicon-minus"></span></th>
                        <th><fmt:message key="common.delete"/></th>
                    </tr>
                    </thead>
                </table>
                <table class="table" >
                    <tr>
                        <th class="col-sm-1"></th>
                        <th class="col-sm-1"></th>
                        <th class="col-sm-1" style="text-align: center">
                            <a class="btn btn-success" type="button" onclick="history.back()">
                                <span class="glyphicon glyphicon-remove"></span>
                                <fmt:message key="common.cancel"/>
                            </a>
                        </th>
                        <th class="col-sm-1" style="text-align: center">
                                <a class="btn btn-primary" type="button" onclick="complete()">
                                    <span class="glyphicon glyphicon-ok"></span>
                                    <fmt:message key="common.complete"/>
                                </a>
                        </th>
                        <th class="col-sm-1" style="text-align: right"><fmt:message key="orders.total"/>:</th>
                        <th class="col-sm-1" style="text-align: right"><span id="totalPrice">${totalPrice}</span> <fmt:message key="common.rub"/></th>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>

<%--dishes modal window--%>
<div class="modal fade" id="selectDishes">
    <div class="container">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h2 class="modal-title"><fmt:message key="dishes.select"/></h2>
                    <h3 class="modal-title">${restaurant.name}, ${restaurant.address}</h3>
                    <h3 class="modal-title" id="modalTitleMenuList">Menu list</h3>
                </div>
                <div class="modal-body">
                    <table class="table table-hover table-bordered " id="dishDT" style="width: 100%">
                        <thead>
                        <tr>
                            <th><fmt:message key="dishes.description"/></th>
                            <th><fmt:message key="dishes.price"/></th>
                            <th><fmt:message key="app.select"/></th>
                        </tr>
                        </thead>
                    </table>

                    <table class="table">
                        <tr>
                            <th class="col-sm-2"></th>
                            <th class="col-sm-1">
                                <a class="btn btn-success" type="button" onclick="$('#selectDishes').modal('hide')">
                                    <span class="glyphicon glyphicon-remove"></span>
                                    <fmt:message key="common.cancel"/>
                                </a>
                            </th>
                            <th class="col-sm-1" style="text-align: center">
                                <div class="span7 text-center">
                                    <a class="btn btn-primary" type="button" onclick="refreshDishes()">
                                        <span class="glyphicon glyphicon-ok"></span>
                                        <fmt:message key="common.complete"/>
                                    </a>
                                </div>
                            </th>
                            <th class="col-sm-2"></th>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>



