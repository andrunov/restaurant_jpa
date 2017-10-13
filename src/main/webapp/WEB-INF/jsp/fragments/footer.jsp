<%--footer of any page, includes link to application repository in WEB--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="messages.app"/>

<footer class="navbar-default-bottom">
    <div class="container">
        <nav class="navbar navbar-link">
            <table  class="table">
                <tr>
                <td class="col-sm-3" style="text-align: center">
                    <a href="https://restaurantdemoapp.herokuapp.com/"><fmt:message key="app.heroku"/></a>
                </td>
                <td class="col-sm-3" style="text-align: center">
                    <a href="https://github.com/andrunov/restaurant_jdbc"><fmt:message key="app.jdbc"/></a>
                </td>
                <td class="col-sm-3" style="text-align: center">
                    <a href="https://github.com/andrunov/restaurant_jpa"><fmt:message key="app.jpa"/></a>
                </td>
                </tr>
            </table>
        </nav>
    </div>
</footer>

<%--function creates an array of bundle resourses specify of users local--%>
<script type="text/javascript">
    var i18n = {};
    <c:forEach var='key' items='<%=new String[]{"common.select","common.update","common.delete","common.failed",
                                                    "roles.ROLE_ADMIN","roles.ROLE_USER",
                                                    "users.add","users.edit",
                                                    "restaurants.add","restaurants.edit","restaurants.select",
                                                    "menuLists.add","menuLists.edit", "menuLists.select",
                                                    "orders.add","orders.edit",
                                                    "dishes.add","dishes.edit","dishes.select"}%>'>
    i18n['${key}'] = '<fmt:message key="${key}"/>';
    </c:forEach>
</script>