<%@include file="/WEB-INF/views/includes/init.jsp"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<h2>Screenshots</h2>

<c:forEach var="shot" items="${screenshots}">
	<div class="screenshot">
		<%-- Base64 all these large images wont give best performance. kinda a hack.
			ill allow somebody with more klout to make a ScreenshotService
			and a Screenshot Image Resolver type-of-endpoint  --%>
		<a href="${pageContext['request'].contextPath}/image/screenshot/${shot.id}" target="_blank">
			<div>${shot.uploaded}</div>
			<img src="${pageContext['request'].contextPath}/image/screenshot/${shot.id}" width="25%" />
		</a>
	</div>
</c:forEach>
