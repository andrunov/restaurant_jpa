<%--header of any page, includs navigation bar--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<fmt:setBundle basename="messages.app"/>
<!-- Static navbar -->
<div class="container">

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
            <sec:authorize access="isAuthenticated()">
                <li><a class="btn btn-primary" href="javascript:javascript:history.go(-1)">&nbsp;<span class="glyphicon glyphicon-arrow-left"></span>&nbsp;</a></li>
                <li><a class="btn btn-primary" href="javascript:javascript:history.go(+1)">&nbsp;<span class="glyphicon glyphicon-arrow-right"></span>&nbsp;</a></li>
                <li><a class="btn btn-primary" href="/">&nbsp;<span class="glyphicon glyphicon-home"></span>&nbsp;</a></li>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <li><a class="btn btn-primary" href="users"><fmt:message key="users.title"/></a></li>
                        <li><a class="btn btn-primary" href="restaurants"><fmt:message key="restaurants.title"/></a></li>
                    </sec:authorize>
                </sec:authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right" vertical-align: center>
                <ul class="nav navbar-nav">
                    <li><a class="btn btn-success" role="button" href="profile"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;${user.name}-<fmt:message key="app.profile"/></a></li>
                    <li><a class="btn btn-warning" role="button" onclick="$('#aboutProject').modal()"><span class="glyphicon glyphicon-star"></span>&nbsp;&nbsp;<fmt:message key="app.about"/></a></li>
                    <li><a class="btn btn-danger" href="logout"><fmt:message key="app.logout"/>&nbsp;&nbsp;<span class="glyphicon glyphicon-log-out"></span></a></li>
                    <jsp:include page="lang.jsp"/>
                </ul>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</nav>
</div>

<%-- about project modal window--%>
<div class="modal fade" id="aboutProject">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title"> <fmt:message key="app.about"/></h2>
            </div>
            <div class="modal-body">
                <div class="span7 text-area">
                    <h4>
                        <fmt:message key="app.describe"/>
                        <p></p>
                        <jsp:include page="stackOfTechnologies.jsp"/>
                    </h4>
                </div>
                <div class="span7 text-center">
                    <a class="btn btn-success" type="button" onclick="$('#aboutProject').modal('hide')">
                        <span class="glyphicon glyphicon-ok"></span>
                        <fmt:message key="common.ok"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>