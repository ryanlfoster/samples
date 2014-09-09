<%@include file="/WEB-INF/views/includes/init.jsp"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<script>
	var spendingArray = [];
	spendingArray.push({
		x: Date.january().first(),
		y: 0
	});
</script>
<c:forEach var="fin" items="${finance}">
	<script>
		var spending = {
				x: new Date('${fin.recordDate}'),
				y: ${fin.spent}
		};
		spendingArray.unshift(spending);
	</script> 
</c:forEach>
<div id="container${productId}" style="min-width: 400px; height: 300px; margin: 0 auto"></div>
<script>
spendingArray = _.sortBy(spendingArray, 'x');
var chart = new Highcharts.Chart({
    chart: {
        renderTo: 'container${productId}',
        type: 'spline',
        marginRight: 130,
        marginBottom: 25
    },
    title: {
        text: 'Year-To-Date Spending',
        x: -20 //center
    },plotOptions: {
        spline: {
			lineWidth: 2,
          	color: '#6D8C25',
            marker: {
                enabled: false,
                states: {
                    hover: {
                        enabled: false,
                    }
                }
            }
        },
      line: {
            lineWidth: 2,
			color: '#ADCC65',
			dashStyle: 'dash',
            marker: {
                enabled: false,
                states: {
                    hover: {
                        enabled: false,
                    }
                }
            }
        }
    },xAxis: {
    	type: 'datetime'
    },
    yAxis: {
        title: {
            text: 'Amount Spent'
        },
        plotLines: [{
            value: 0,
            width: 1,
            color: '#808080'
        }]
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'top',
        x: -10,
        y: 100,
        borderWidth: 0
    },
    series: [{
        name: 'YTD',
        data: spendingArray
    }, {
        name: 'Budget',
        type: 'line',
      	data: [{x: Date.january(), y: 0},
             {x: Date.december(), y: ${budget.budget}}]
    }]
});
</script>