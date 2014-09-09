function drawDonut(container, key) {
	var textDiv = $("<div>").addClass("donut-text");

	//Custom Arc Attribute, position x&y, value portion of total, total value, Radius
	var archtype = Raphael(container, 46, 46);
	archtype.customAttributes.arc = function (xloc, yloc, value, total, R) {
	    var alpha = 360 / total * value,
	        a = (90 - alpha) * Math.PI / 180,
	        x = xloc + R * Math.cos(a),
	        y = yloc - R * Math.sin(a),
	        path;
	    if (total == value) {
	        path = [
	            ["M", xloc, yloc - R],
	            ["A", R, R, 0, 1, 1, xloc - 0.01, yloc - R]
	        ];
	    } else {
	        path = [
	            ["M", xloc, yloc - R],
	            ["A", R, R, 0, +(alpha > 180), 1, x, y]
	        ];
	    }
	    return {
	        path: path
	    };
	};

	$.getJSON('scripture/progress/'+key+'/0', function(data) {
		var submittedPercent = parseInt((data.submitted / data.total) * 100);
		var approvedPercent = parseInt((data.approved / data.total) * 100);

		var text = archtype.text(23, 23, approvedPercent + "%");
	    text.attr({'font-size': 12, 'font-family': 'helvetica'});
	    text.attr("fill", "#f1f1f1");
		
		drawArc(archtype, "000000", 100, 0);
		drawArc(archtype, "979797", 99, 0);
		
		drawArc(archtype, "000000", submittedPercent, 1000);
		drawArc(archtype, "f7be0d", submittedPercent - 1, 1000);

		drawArc(archtype, "000000", approvedPercent, 1200);
		drawArc(archtype, "6ebe75", approvedPercent - 1, 1200);
	});
	

} 

function drawArc(archtype, color, percentage, time) {
	
	var my_arc = archtype.path().attr({
	    "stroke": "#"+color,
	    "stroke-width": 6,
	    arc: [23, 23, 0, 100, 20]
	});

	my_arc.animate({
	    arc: [23, 23, percentage, 100, 20]
	}, time, "");
}
