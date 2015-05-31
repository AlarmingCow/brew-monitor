var cToF = function(temp) {
    return temp*9/5 + 32;
};

$(document).ready(function() {
    var updateTemp = function() {
        jQuery.ajax({
            url: "/mostRecentTemps?results=1",
            success: function(data) {
                jQuery("#most-recent-temp").text(Math.round(cToF(data.temps[0].temp)) + " ºF");
                jQuery("#most-recent-temp-time").text(moment(data.temps[0].time).fromNow());
            },
            dataType: "json"
        });
    };

    setInterval(updateTemp, 1000);

    var margin = {top: 20, right: 20, bottom: 30, left: 50},
        width = 960 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    var parseDate = d3.time.format.iso.parse;

    var x = d3.time.scale()
        .range([0, width]);

    var y = d3.scale.linear()
        .range([height, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom");

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left");

    var line = d3.svg.line()
        .x(function(reading) { return x(reading.time); })
        .y(function(reading) { return y(reading.temp); });

    var graph = d3.select("#graph").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var drawGraph = function () {
        var minTime = new Date().getTime() - 3600000;
        d3.json("/mostRecentTemps?min-date=" + minTime + "&results=" + 5000, function (data) {
            var temps = data.temps;
            temps.forEach(function (d) {
                d.time = parseDate(d.time);
                d.temp = cToF(+d.temp);
            });

            graph.selectAll("*").remove();

            x.domain(d3.extent(temps, function (d) {
                return d.time;
            }));
            y.domain(d3.extent(temps, function (d) {
                return d.temp;
            }));

            graph.append("g")
                .attr("class", "x-axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

            graph.append("g")
                .attr("class", "y-axis")
                .call(yAxis)
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Temperature (ºF)");

            graph.append("path")
                .datum(temps)
                .attr("class", "line")
                .attr("d", line);
        });
    };

    setInterval(function () { drawGraph() }, 1000);
});