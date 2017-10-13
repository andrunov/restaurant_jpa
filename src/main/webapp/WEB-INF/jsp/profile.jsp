<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="rerTags" tagdir="/WEB-INF/tags" %>
<fmt:setBundle basename="messages.app"/>


<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="container">
    <div class="jumbotron">
        <div class="shadow">
            <h3>
                <c:choose>
                    <c:when test="${register == true}"><fmt:message key="app.register"/></c:when>
                    <c:otherwise><fmt:message key="app.profile"/></c:otherwise>
                </c:choose>
            </h3>
            <div class="view-box">
                <form class="form-horizontal" method="post" action="${register ? 'register' : 'profile'}">


                    <div class="form-group ${nameErrorMessage != null ? 'error' : '' }">
                        <label class="control-label col-sm-2"><fmt:message key="users.name"/></label>
                             <div class="col-sm-3">
                                     <input type="text" class="form-control" id="name" name="name" value="${user.name}" placeholder="<fmt:message key="users.name"/>">
                             </div>
                        <div class="col-sm-7">
                            <span class="help-inline">${nameErrorMessage}</span>
                        </div>
                    </div>

                    <div class="form-group ${emailErrorMessage != null ? 'error' : '' }">
                        <label class="control-label col-sm-2"><fmt:message key="users.email"/></label>
                             <div class="col-sm-3">
                                  <input type="email" class="form-control" id="email" name="email" value="${user.email}" placeholder="<fmt:message key="users.email"/>">
                             </div>
                        <div class="col-sm-7">
                            <span class="help-inline">${emailErrorMessage}</span>
                        </div>
                    </div>

                    <div class="form-group ${passwordErrorMessage != null ? 'error' : '' }">
                        <label class="control-label col-sm-2"><fmt:message key="users.password"/></label>
                                    <div class="col-sm-3">
                                       <input type="password" class="form-control" id="password" name="password" value="${user.password}" placeholder="<fmt:message key="users.password"/>">
                                    </div>
                        <div class="col-sm-7">
                            <c:choose>
                                <c:when test="${passwordErrorMessage != null}"><span class="help-inline"><fmt:message key="error.password"/></span></c:when>
                            </c:choose>
                            <%--<span class="help-inline"><fmt:message key="error.password"/></span>--%>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-offset-2 col-xs-10">
                            <button class="btn btn-success" type="button" onclick="history.back()">
                                <span class="glyphicon glyphicon-remove"></span>
                                <fmt:message key="common.cancel"/>
                            </button>
                            <button type="submit" class="btn btn-primary">
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

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>