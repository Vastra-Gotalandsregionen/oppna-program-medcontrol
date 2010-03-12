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
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<fmt:setBundle basename="se.vgregion.portal.medcontrol.MedControl" />

<portlet:defineObjects />
  
<portlet:actionURL var="savePreferences" />
  
<portlet:actionURL var="formAction" />

<div style="margin: 6px"><b><fmt:message key="preferences.title"/></b><br>  
  <form:form modelAttribute="medControlFormBacker" htmlEscape="false" method="post" action="${formAction}">
    <form:errors path="listItemLimitation" cssStyle="color:red" />
    <table>
      <tr>
        <td><fmt:message key="max.in.list" />:</td>
         <td><form:input path="listItemLimitation" size="3" maxlength="2" /></td>      
      </tr>
    </table>
    <input value=<fmt:message key="save" /> type="submit">
  </form:form>
</div>
