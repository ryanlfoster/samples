<%@include file="/WEB-INF/views/includes/init.jsp" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:template>
	<jsp:body>
	<div class="padding-md content">
		<h1 class="page-title">Initiatives</h1>
		<div class="accordion">
			<c:forEach var="product" items="${products}">
				<h3><a href="#">${product.name} <strong >^</strong></a></h3>
				<div class=" section grid">
					<ul class="column width-20">
						<li><a data-href="product/${product.id}/description">Description</a><span class="arrow"></span></li>
						<%-- <li><a data-href="product/${product.id}/roadmap">Roadmap</a><span class="arrow"></span></li> --%>
						<li><a data-href="product/${product.id}/spend">YTD Spend</a><span class="arrow"></span></li>
						<li><a data-href="product/${product.id}/team">Team</a><span class="arrow"></span></li>
						<%-- <li><a data-href="product/${product.id}/screenshots">Screenshots</a><span class="arrow"></span></li> --%>
					</ul>
					<div class="column width-75 right-content">
						<p>
							Mauris mauris ante, blandit et, ultrices a, suscipit eget, quam. Integer
							ut neque. Vivamus nisi metus, molestie vel, gravida in, condimentum sit
							amet, nunc. Nam a nibh. Donec suscipit eros. Nam mi. Proin viverra leo ut
							odio. Curabitur malesuada. Vestibulum a velit eu ante scelerisque vulputate.
						</p>
					</div>
				</div>
			</c:forEach>
			<h3><a href="#">Initiative's Teams <strong >^</strong></a></h3>
			<div class="section grid">
				<div class="column width-100">
					<c:forEach var="product" items="${products}">
						<div class="section grid" data-teamsize="${product.id}">
						<div class="column width-20">
								<div class="bubble">${product.name}</div>
							</div>
							<div class="column width-75">
								<div class="bubble-chart"></div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="grid padding-sm">
			<div class="column width-50 efforts">
				<h2>Other Efforts</h2>
				<hr/>
				<ul>
					<c:forEach var="op" items="${otherProducts}">
						<li>${op.name}</li>
					</c:forEach>
				</ul>
			</div>
			<div class="column width-50 efforts">
				<h2>Future Efforts</h2>
				<hr/>
				<ul>
					<c:forEach var="fp" items="${futureProducts}">
						<li>${fp.name}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<script>
		$('[data-teamsize]').each(function(i, e){
			var productId = $(e).attr('data-teamsize');
			$.get('product/' + productId + '/teamsize', function(data){
				$('.bubble-chart', e).append(data);
			});
		});
	</script>
	</jsp:body>
</tags:template>