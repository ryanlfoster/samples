<%@include file="/WEB-INF/views/includes/init.jsp" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div id="container" style="min-width: 400px; height: 300px; margin: 0 auto"></div>
<script>
var chart = new Highcharts.Chart({
    chart: {
        renderTo: 'container',
        type: 'areaspline'
    },
    title: {
        text: 'Monthly Average Temperature'
    },
    subtitle: {
        text: 'Source: WorldClimate.com'
    },
    xAxis: {
        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    },
    yAxis: {
        title: {
            text: 'Temperature (°C)'
        }
    },

    plotOptions: {
        areaspline:{enableMousTracking:false,marker:{enabled:false}},
        line: {
            dataLabels: {
                enabled: true,
                formatter: function() {
                    return '<b>monkey' + this.series.name + '</b><br/>' + this.x + ': ' + this.y + '°C';
                },
                useHTML: true
            },
            marker: {
                enabled: false
            },
            lineWidth:0,
            enableMouseTracking: false
        }
    },
    series: [{
        type: 'line',
        name: 'Tokyo',
        data: [30, 30, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]},
    {
        name: 'London',
        data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]}]
});
</script>