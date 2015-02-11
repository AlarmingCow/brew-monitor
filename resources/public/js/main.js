$(document).ready(function() {
    var updateTemp = function() {
        jQuery.ajax({
            url: "/mostRecentTemps?results=20",
            success: function(data) {
                jQuery("#most-recent-temp").text(data.temps[0].temp);
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

    var svg = d3.select("#graph").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.json("/mostRecentTemps?results=20", function (data) {
        var temps = data.temps;
        temps.forEach(function(d) {
            d.time = parseDate(d.time);
            d.temp = +d.temp;
        });

        x.domain(d3.extent(temps, function(d) { return d.time; }));
        y.domain(d3.extent(temps, function(d) { return d.temp; }));

        svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis);

        svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("Temperature (ºC)");

        svg.append("path")
            .datum(temps)
            .attr("class", "line")
            .attr("d", line);
    });
});