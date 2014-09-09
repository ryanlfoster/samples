<%@include file="/WEB-INF/views/includes/init.jsp"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<script>
	var TeamSize = [];
</script>
<c:forEach var="size" items="${teamSize}">
	<script>
		var team = {
				y: ${size.teamSize},
				x: new Date('${size.timestamp}')
		};
		TeamSize.push(team);
	</script> 
</c:forEach>
<div id="containerTeamSize${productId}" style="min-width: 600px; height: 125px; margin: 0 auto"></div>
<script>
$(function () {
    var chart = new Highcharts.Chart({
            chart: {
                renderTo: 'containerTeamSize${productId}',
                type: 'areaspline',
                height: 200
            },
            xAxis: {
	            type: 'datetime'
        	},
        	yAxis: {
	            title:{style: {display: 'none'}},
	            tickInterval: 1
        	},
            title: {
                style: {display: 'none'}
            },
           	legend: {
           		enabled: false
           	},
            tooltip: {
                formatter: function() {
                	var d = new Date(this.x);
                    return ''+
                    d.toString('MMM yyyy') +': '+ this.y +' units';
                }
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                areaspline: {
                    fillOpacity: 0.5
                }
            },
            series: [{
            	color: '#8A5436',
                data: TeamSize
            }]
        });
    });
</script>