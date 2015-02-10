$(document).ready(function() {
    var updateTemp = function() {
        jQuery.ajax({
            url: "/mostRecentTemp",
            success: function(data) {
                jQuery("#content").text(data.temp);
            },
            dataType: "json"
        });
    };

    setInterval(updateTemp, 1000);
});