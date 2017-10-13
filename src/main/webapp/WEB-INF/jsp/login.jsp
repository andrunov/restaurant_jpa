<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<fmt:setBundle basename="messages.app"/>


<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<div class="container">

<div class="navbar navbar-default">
        <div class="container-fluid">
        <div class="navbar-header navbar-brand"><fmt:message key="app.title"/></div>
        <div class="navbar-collapse collapse">

            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form class="navbar-form" role="form" action="spring_security_check" method="post">
                        <div class="form-group">
                            <input type="text" placeholder="Email" class="form-control" name="username">
                        </div>
                        <div class="form-group">
                            <input type="password" placeholder="Password" class="form-control" name="password">
                        </div>
                        <button type="submit" class="btn btn-success">
                            <span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>
                        </button>
                    </form>
                </li>
                <jsp:include page="fragments/lang.jsp"/>
            </ul>
        </div>
    </div>
</div>

    <div class="jumbotron">
        <div class="container">
            <c:if test="${param.error}">
                <div class="error">
                        ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
                </div>
            </c:if>
            <c:if test="${param.message=='registersuccessfull'}">
                <div class="message">
                    ${param.username}, <fmt:message key="app.registered"/>
                </div>
            </c:if>
            <br/>
            <p>
                <a class="btn btn-lg btn-success" href="register"><fmt:message key="app.register"/> &raquo;</a>
                <button type="submit" class="btn btn-lg btn-primary" onclick="setCredentials('vgordeev.78@gmail.com', '111222777')">
                    <fmt:message key="app.login"/> <fmt:message key="roles.ROLE_USER_FULL"/>
                </button>
                <button type="submit" class="btn btn-lg btn-success" onclick="setCredentials('r_eliseev78yandex.ru', '222333555')">
                    <fmt:message key="app.login"/> <fmt:message key="roles.ROLE_ADMIN_FULL"/>
                </button>
            </p>
        </div>
        <div class="container">
            <div class="lead">
                <fmt:message key="app.describe"/>
            </div>
            <div class="lead">
                <jsp:include page="fragments/stackOfTechnologies.jsp"/>

            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
<script type="text/javascript">
    <c:if test="${not empty param.username}">
    setCredentials("${param.username}", "");
    </c:if>
    function setCredentials(username, password) {
        $('input[name="username"]').val(username)
        $('input[name="password"]').val(password)
    }
</script>