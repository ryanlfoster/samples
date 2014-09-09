<%@include file="/WEB-INF/views/includes/init.jsp" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:template>
	<jsp:body>
	<fmt:setLocale value="en_US"/>
	<div class="padding-md content">
		<h1 class="page-title">Financials</h1>
		<div id="yearToDate">
			<h2>Year-To-Date Spending</h2>
			<div id="graph">
				<img class="total-finance" src="${pageContext['request'].contextPath}/image/finance/${financeImage.id}"/>
			</div>
		</div>
		<div id="projects">
			<c:forEach var="product" items="${products}">
				<c:set var="spending" value="${spendings.get(product.id)}"/>
				<div class="project">
					<h3>${product.name}</h3>
					<div class="bar">
						<div style="width: ${spending.spent/budgets[product.id].budget*100}%;" class="fill"></div>
					</div>
					<span><fmt:formatNumber value="${spending.spent}" type="currency"/> of <fmt:formatNumber value="${budgets[product.id].budget}" type="currency"/></span>
				</div>
			</c:forEach>
		</div>
	</div>
	</jsp:body>
</tags:template>