<%--shows menuLists of specify restaurant--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="messages.app"/>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<script type="text/javascript" src="resources/js/datatableUtil.js" defer></script>
<script type="text/javascript" src="resources/js/menuListsUtil.js" defer></script>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <div class="jumbotron">
        <div class="shadow">
            <h3>${restaurant.name}, ${restaurant.address}</h3>
            <p style="color: darkgreen"><fmt:message key="menuLists.list"/></p>
            <table class="table" >
                <tr>
                    <td>
                        <a class="btn btn-primary" type="button" onclick="add()">
                            <span class="glyphicon glyphicon-plus-sign"></span>
                            <fmt:message key="menuLists.add"/>
                        </a>
                    </td>
                    <td>
                        <input name="filter" type="radio" checked="checked" id="GET_ALL" onclick="updateTable('ALL')"/>
                        <label  for="GET_ALL"><fmt:message key="status.ALL"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_ENABLED" onclick="updateTable('TRUE')"/>
                        <label  for="GET_ENABLED"><fmt:message key="common.enabled"/></label>
                    </td>
                    <td>
                        <input name="filter" type="radio" id="GET_DISABLED" onclick="updateTable('FALSE')"/>
                        <label  for="GET_DISABLED"><fmt:message key="common.disabled"/></label>
                    </td>
                </tr>
            </table>
            <div class="view-box">
                <table class="table table-hover table-bordered " id="datatable">
                    <thead>
                    <tr>
                        <th></th>
                        <th><fmt:message key="menuLists.description"/></th>
                        <th><fmt:message key="common.dateTime"/></th>
                        <th><fmt:message key="menuLists.content"/></th>
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
                        <label for="description" class="control-label col-xs-3"><fmt:message
                                key="menuLists.description"/></label>

                        <div class="col-xs-9">
                            <input class="form-control" id="description" name="description"
                                   placeholder="<fmt:message key="menuLists.description"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dateTime" class="control-label col-xs-3"><fmt:message
                                key="common.dateTime"/></label>

                        <div class="col-xs-9">
                            <input class="form-control" id="dateTime" name="dateTime"
                                   placeholder="<fmt:message key="common.dateTime"/>">
                        </div>
                    </div>

                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">
                                <label for="enabled" class="text-right"><fmt:message key="users.enabled"/></label>
                            </h3>
                        </div>
                        <div class="panel-body">
                            <input type="checkbox" id="enabled" name="enabled" value="true" checked >
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



