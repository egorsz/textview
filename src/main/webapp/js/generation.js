
var GEN = {};


GEN.generateSentancesTable = function() {
    var html = '<table><tbody class="sent_tbody">'
    for (var t in APP.txt) {
        var sent = APP.txt[t]
        html += '<tr class="sent_row" data-sent="' + t + '"><td>' + sent + '</td></tr>'
    }

    html += '</tbody ></table>'
    $('#sent_cont').html(html)

    $(".sent_row").mouseover(function () {
            var sent = $(this).attr("data-sent")
            $(this).toggleClass("snet_row_high")
            $('.sent_' + sent).toggleClass("node_high")
            $('.text_sent_' + sent).toggleClass("text_high");
            $('.link_text_' + sent).toggleClass("text_high");

        })
        .mouseout(function () {
            var sent = $(this).attr("data-sent")
            $(this).toggleClass("snet_row_high")
            $('.sent_' + sent).toggleClass("node_high");
            $('.text_sent_' + sent).toggleClass("text_high");
            $('.link_text_' + sent).toggleClass("text_high");
        });
}

GEN.generateSettings = function() {
    var html = '<div>'
    
    
}