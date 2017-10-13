<%--shows orders of specify user--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="messages.app"/>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<script type="text/javascript" src="resources/js/datatableUtil.js" defer></script>
<script type="text/javascript" src="resources/js/ordersUtil.js" defer></script>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<%--orders datatable--%>
<div class="container">
    <div class="jumbotron">
        <div class="shadow">
            <h3>${currentUser.name}, ${currentUser.email}</h3>
            <p style="color: darkgreen"><fmt:message key="orders.ofUser"/>, <fmt:message key="orders.sum"/> - <span style="color: darkred">${currentUser.totalOrdersAmount} <fmt:message key="common.rub"/></span></p>
            <table class="table" >
                <tr>
                    <td>
                        <a class="btn btn-primary" type="button" onclick="addOrder()">
                            <span class="glyphicon glyphicon-plus-sign"></span>
                            <fmt:message key="orders.add"/>
                        </a>
                    </td>
                    <td>
                        <input name="filter" type="radio" checked="checked" id="GET_ALL" onclick="updateTable('ALL')"/>
                        <label  for="GET_ALL"><fmt:message key="status.ALL"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_ACCEPTED" onclick="updateTable('ACCEPTED')"/>
                        <label  for="GET_ACCEPTED"><fmt:message key="status.ACCEPTED"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_PREPARING" onclick="updateTable('PREPARING')"/>
                        <label  for="GET_PREPARING"><fmt:message key="status.PREPARING"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_READY" onclick="updateTable('READY')"/>
                        <label  for="GET_READY"><fmt:message key="status.READY"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_FINISHED" onclick="updateTable('FINISHED')"/>
                        <label  for="GET_FINISHED"><fmt:message key="status.FINISHED"/></label>
                    </td>
                    <td class="col-sm-1" align="right">
                        <label for="dateTimeFilter"><fmt:message key="common.dateFilter"/></label>
                    </td>
                    <td class="col-sm-2">
                            <input class="form-control" type="text" id="dateTimeFilter"
                                   placeholder="<fmt:message key="common.dateFilter"/>">
                    </td>
                </tr>
            </table>
            <div class="view-box">
                <table class="table table-hover table-bordered " id="ordersDT">
                    <thead>
                    <tr>
                        <th></th>
                        <th><fmt:message key="common.dateTime"/></th>
                        <th><fmt:message key="restaurants.nameAndAddress"/></th>
                        <th><fmt:message key="orders.total"/></th>
                        <th><fmt:message key="orders.content"/></th>
                        <th><fmt:message key="common.update"/></th>
                        <th><fmt:message key="common.delete"/></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>

<%-- restaurant modal window--%>
<div class="modal fade" id="selectRestaurant">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"> <fmt:message key="restaurants.select"/></h2>
            </div>
            <div class="modal-body">
                <table class="table table-hover table-bordered " id="restaurantDT" style="width: 100%">
                    <thead>
                    <tr>
                        <th><fmt:message key="restaurants.name"/></th>
                        <th><fmt:message key="restaurants.address"/></th>
                        <th><fmt:message key="common.select"/></th>
                    </tr>
                    </thead>
                </table>
                <div class="span7 text-center">
                    <a class="btn btn-success" type="button" onclick="$('#selectRestaurant').modal('hide')">
                        <span class="glyphicon glyphicon-remove"></span>
                        <fmt:message key="common.cancel"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%--menuList modal window--%>
<div class="modal fade" id="selectMenuList">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><fmt:message key="menuLists.select"/></h2>
                <h3 class="modal-title" id="modalTitleRestaurant"></h3>
            </div>
            <div class="modal-body">
                <table class="table" style="width: 100%" >
                    <thead>
                    <tr>
                        <td></td>
                        <td>
                            <input name="filter_for_status" type="radio" checked="checked" id="GET_ALL_MENU" onclick="updateMenuListTable('ALL')"/>
                            <label  for="GET_ALL_MENU"><fmt:message key="status.ALL"/></label>
                        </td>
                        <td>
                            <input name="filter_for_status" type="radio" id="GET_ENABLED" onclick="updateMenuListTable('TRUE')"/>
                            <label  for="GET_ENABLED"><fmt:message key="common.enabled"/></label>
                        </td>
                        <td>
                            <input name="filter_for_status" type="radio" id="GET_DISABLED" onclick="updateMenuListTable('FALSE')"/>
                            <label  for="GET_DISABLED"><fmt:message key="common.disabled"/></label>
                        </td>
                        <td></td>
                    </tr>
                    </thead>
                </table>
                <table class="table table-hover table-bordered " id="menuListDT" style="width: 100%;" >
                    <thead>
                    <tr>
                        <th></th>
                        <th><fmt:message key="menuLists.description"/></th>
                        <th><fmt:message key="common.dateTime"/></th>
                        <th><fmt:message key="common.select"/></th>
                    </tr>
                    </thead>
                </table>
                <div class="span7 text-center">
                    <a class="btn btn-success" type="button" onclick="$('#selectMenuList').modal('hide')">
                        <span class="glyphicon glyphicon-remove"></span>
                        <fmt:message key="common.cancel"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%--dishes modal window--%>
<div class="modal fade" id="selectDishes">
    <div class="container">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"><fmt:message key="dishes.select"/></h2>
                <h3 class="modal-title" id="modalTitleRestaurant2"></h3>
                <h3 class="modal-title" id="modalTitleMenuList"></h3>
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
                                <a class="btn btn-primary" type="button" onclick="complete()">
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

<%--edit order modal window--%>
<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="span7 text-center">
                    <h2 class="modal-title" id="modalTitle"></h2>
                </div>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" method="post" id="detailsForm">
                    <input type="text" hidden="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><fmt:message  key="common.dateTime"/></label>

                        <div class="col-xs-9">
                            <input class="form-control" id="dateTime" name="dateTime"
                                   placeholder="<fmt:message key="common.dateTime"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <p>
                            <label  class="control-label col-xs-3"><fmt:message key="order.status"/></label>
                        </p>

                        <div class="col-xs-9">
                            <p>
                                <input name="status" type="radio" id="ACCEPTED" value="ACCEPTED">
                                <label  for="ACCEPTED"><fmt:message key="status.ACCEPTED"/></label>
                            </p>
                            <p>
                                <input name="status" type="radio" id="PREPARING" value="PREPARING">
                                <label  for="PREPARING"><fmt:message key="status.PREPARING"/></label>
                            </p>
                            <p>
                                <input name="status" type="radio" id="READY" value="READY">
                                <label  for="READY"><fmt:message key="status.READY"/></label>
                            </p>
                            <p>
                                <input name="status" type="radio" id="FINISHED" value="FINISHED">
                                <label  for="FINISHED"><fmt:message key="status.FINISHED"/></label>
                            </p>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="span7 text-center">
                            <button class="btn btn-success" type="button" onclick="$('#editRow').modal('hide')">
                                <span class="glyphicon glyphicon-remove"></span>
                                <fmt:message key="common.cancel"/>
                            </button>
                            <button type="button" onclick="saveWithFilter()" class="btn btn-primary">
                                <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                                <fmt:message key="common.complete"/>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>



