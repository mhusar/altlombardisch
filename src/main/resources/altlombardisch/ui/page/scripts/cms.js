jQuery.ajaxSetup({
    cache : false
});

jQuery.fn.isInViewport = function (offset) {
    var elementTop, elementBottom, viewportTop, viewportBottom;

    if (!jQuery(this).length) {
        return;
    }

    // set undefined offset to 0
    if (typeof offset === "undefined") {
        offset = 0;
    }

    elementTop = jQuery(this).offset().top;
    elementBottom = elementTop + jQuery(this).outerHeight();
    viewportTop = jQuery(window).scrollTop();
    viewportBottom = viewportTop + jQuery(window).height();

    return (elementBottom - offset) > viewportTop && (elementTop + offset) < viewportBottom;
};

jQuery.debounce = function (delay, callback) {
    var timeout = null;

    return function () {
        var _arguments = arguments;

        if (timeout) {
            clearTimeout(timeout);
        }

        timeout = setTimeout(function () {
            callback.apply(null, _arguments);
            timeout = null;
        }, delay);
    };
}

function setupFeedbackPanel(id) {
    var listItems;

    if (typeof id !== "undefined") {
        listItems = jQuery(id + " li");
    } else {
        listItems = jQuery(".feedbackPanel li");
    }

    listItems.each(function(index) {
        var listElementClass = jQuery(this).attr("class");

        jQuery(this).removeClass(listElementClass).addClass("alert alert-" + listElementClass);
    });
}

// resize editor when parent tab is selected
jQuery(document).on("shown.bs.tab", "a[data-toggle='tab']", function (event) {
    var tabToggle = jQuery(event.target);

    if (tabToggle.is("[href]")) {
        jQuery(tabToggle.attr("href")).find(".mirror.ace_editor").each(function () {
            ace.edit(jQuery(this).attr("id")).resize();
        });
    }
});
