<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<a href="${pageContext.request.contextPath}/" class="breadcrumbLink">eDelfoi.fi</a>

<span class="breadCrumbSpacer">&gt;</span>

<a href="${pageContext.request.contextPath}/admin/dashboard.page?lang=${dashboardLang}" class="breadcrumbLink"><fmt:message key="breadcrumb.panelAdmin.dashboard"/></a>

<c:if test="${not empty(param.breadcrumbPageTitle)}">

  <span class="breadCrumbSpacer">&gt;</span>

  <a href="${param.breadcrumbPageUrl}" class="breadcrumbLink">${param.breadcrumbPageTitle}</a>
</c:if>