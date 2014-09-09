<%@include file="/WEB-INF/views/includes/init.jsp" %>
<div class="header">
	<div id="navigation">
		<span class="nav-title">
			<a href="/">Content Media Technology Portfolio</a>
		</span>
		<ul class="top-nav">
			<li><a href="${pageContext['request'].contextPath}/">Product</a></li>
			<li><a href="${pageContext['request'].contextPath}/financials">Financials</a></li>
			<!-- <li><a href="${pageContext['request'].contextPath}/resources">ICS Resources</a></li> -->
		</ul>
	</div>

	<div class="toolbar">
		<span class="tool-link float-right">
			<a href="?signmeout">Sign Out</a>
		</span>
	</div>
</div>


