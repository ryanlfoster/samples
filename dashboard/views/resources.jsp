<%@include file="/WEB-INF/views/includes/init.jsp" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:template>
	<jsp:body>
	<div class="padding-md content">
		<h1 class="page-title">Resources</h1>
		<c:forEach var="product" items="${products}">
			<div class="team">
				<h3>${product.name}</h3>
				<c:forEach var="teamMember" items="${product.teamMembers}">
					<div class="resource">
						<div class="image">
							<img src="https://my.ldschurch.org/User%20Photos/Profile%20Pictures/LDS_${teamMember.ldsAccount}_LThumb.jpg" style="display:block"/>
						</div>
						<p><b>${teamMember.name}</b></p>
						<p><i>${teamMember.title}</i></p>
					</div>
				</c:forEach>
				<hr/>
			</div>
		</c:forEach>
	</div>
	</jsp:body>
</tags:template>