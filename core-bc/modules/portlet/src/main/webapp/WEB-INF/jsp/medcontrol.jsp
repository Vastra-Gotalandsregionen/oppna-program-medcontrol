<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA


--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style type="text/css">
  <%@ include file="/style/style.css"%>
</style>

<script type="text/javascript"><!--

  function showCase(url) {
    window.open(url);
  }

--></script>

<fmt:setBundle basename="se.vgregion.portal.medcontrol.MedControl" />

<div id="listDivId">
  <table width="100%">
    <c:forEach items="${devCaseList}" var="case">
      <tr onclick="showCase('${case.url}');" title="<fmt:message key="case"/>: ${case.caseNumber}, <fmt:message key="registered"/>: <fmt:formatDate pattern="yyyy-MM-dd" value="${case.registeredDate}" />" style="cursor:pointer">
        <td class="caseList"><c:out value="${case.description}"></c:out></td>
      </tr>
    </c:forEach>
  </table>
</div>
