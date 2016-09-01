$('#next').click(function () {
    APP.next(APP.step)
})
$('#back').click(function () {
    APP.next(-APP.step)
})
$('#next_sent').click(function () {
    APP.stepNext();
})
$('#back_sent').click(function () {
    APP.stepBack();
})

$('#send').click(function () {
    var txt = $('#inp_text').val();
    APP.sendText(txt)
})


$("body").keypress(function (event) {
    if (event.which == 43) {
        APP.stepNext();
    } else if (event.which == 45) {
        APP.stepBack();
    }
    console.log(event.which);
});
$('#inp_text').keypress(function (event) {
    if (event.which == 13) {
        var txt = $('#inp_text').val();
        APP.sendText(txt)
    }
});

$('#save_current').click(function () {
    var text = APP.textsArray[APP.sent]._2
    $.ajax({
        type: "POST",
        url: document.location.origin + "/api/sent",
        data: text,
        success: function (data) {
            console.log("saved")
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        },
        dataType: "text",
        contentType: "text/plain"

    });

})

function highlightSent(sent) {
    $('circle').removeClass('node_current')
    $('text').removeClass("text_current");
    $('tr').removeClass("snet_row_high");

    $('.sent_' + sent).addClass("node_current")
    $('.text_sent_' + sent).addClass("text_current");
    $('.link_text_' + sent).addClass("text_current");
    $('tr[data-sent="' + sent + '"]').addClass("snet_row_high");
}