$(document).ready(function() {
    var updateTemp = function() {
        jQuery.ajax({
            url: "/mostRecentTemps?results=20",
            success: function(data) {
                jQuery("#content").text(data.temps[0]);
            },
            dataType: "json"
        });
    };

    setInterval(updateTemp, 1000);
});