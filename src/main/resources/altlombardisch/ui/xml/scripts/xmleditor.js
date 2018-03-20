/*global ace,InputPanel,jQuery*/
/*jslint browser: true, devel: true*/

var XmlEditor;

jQuery.noConflict();

XmlEditor = (function() {
    "use strict";
    var editors = {}, setupApostropheKey = function(editor, mirror) {
        mirror.keydown(function(event) {
            var character = "’", selectionRange = editor.getSelectionRange();

            if (event.shiftKey && event.which === 163) {
                event.preventDefault();
                editor.session.getDocument().remove(selectionRange);
                editor.insert(character);
                editor.focus();
            }
        });
    }, setupXmlEditor = function(mirror, textarea, editor, container, readOnly) {
        mirror.height(textarea.height());
        mirror.on("resize", function() {
            editor.resize();
        });

        // allow to use tabs for navigation
        editor.commands.removeCommand("indent");
        editor.commands.removeCommand("outdent");

        editor.setReadOnly(readOnly);
        editor.setOptions({
            fontFamily : "Fira Mono"
        });
        editor.setFontSize("13px");
        editor.setTheme("ace/theme/chrome");
        editor.renderer.setShowGutter(true);
        editor.setDragDelay(0);
        editor.setAnimatedScroll(true);
        editor.setBehavioursEnabled(true);
        editor.setDisplayIndentGuides(true);
        editor.setFadeFoldWidgets(true);
        editor.setShowFoldWidgets(true);
        editor.setShowInvisibles(true);
        editor.setShowPrintMargin(false);
        editor.on("focus", function() {
            mirror.addClass("focus");
        });
        editor.on("blur", function() {
            mirror.removeClass("focus");
        });

        if (container.data("autofocus") === "autofocus") {
            editor.focus();
        }

        editor.getSession().setMode("ace/mode/xml");
        editor.getSession().setTabSize(4);
        editor.getSession().setUseSoftTabs(true);
        editor.getSession().setUseWrapMode(true);
        editor.getSession().setWrapLimitRange();
        editor.getSession().setValue(textarea.val());
        editor.getSession().on("change", function() {
            textarea.val(editor.getSession().getValue());
        });
    }, expandMirror = function(mirror, editor, expandButton) {
        var containerWidth = jQuery("main .container").first().width(), inputPanelHeight = jQuery(
                ".inputPanel").outerHeight(), navbarHeight = jQuery(
                ".navbar-fixed-top").height(), paddingBottom = 15, paddingTop = 30, windowHeight = jQuery(
                window).height(), windowWidth = jQuery(window).width();

        jQuery(expandButton).hide().toggleClass("on off");
        mirror.parent().addClass("expanded").css({
            left : ((windowWidth - containerWidth) / 2) + "px",
            top : (navbarHeight + paddingTop) + "px"
        });
        mirror.css({
            height : (windowHeight - inputPanelHeight - navbarHeight
                    - paddingBottom - paddingTop)
                    + "px",
            width : containerWidth + "px"
        });

        editor.resize();
        editor.renderer.scrollCursorIntoView();
        editor.focus();
    }, collapseMirror = function(mirror, editor, expandButton, scrollLeft,
            scrollTop, mirrorHeight) {
        jQuery(expandButton).hide().toggleClass("on off");
        mirror.parent().removeClass("expanded").css({
            left : "",
            top : ""
        });
        mirror.css({
            height : mirrorHeight + "px",
            width : ""
        });
        jQuery(window).scrollLeft(scrollLeft).scrollTop(scrollTop);

        editor.resize();
        editor.renderer.scrollCursorIntoView();
        editor.focus();
    }, animateElements = function(elements, properties, propertyIndex) {
        var property = properties[propertyIndex];

        elements.delay(50).queue(function(next) {
            var key;

            for (key in property) {
                if (property.hasOwnProperty(key)) {
                    jQuery(this).css(key, property[key]);
                }
            }

            next();
        });
    }, animateGrowing = function(elements) {
        var properties = [ {
            "top" : "3px",
            "left" : "4px",
            "height" : "5px",
            "width" : "11px"
        }, {
            "top" : "2px",
            "left" : "3px",
            "height" : "7px",
            "width" : "13px"
        }, {
            "top" : "2px",
            "left" : "2px",
            "height" : "7px",
            "width" : "15px"
        }, {
            "top" : "1px",
            "left" : "1px",
            "height" : "9px",
            "width" : "17px"
        } ], i;

        for (i = 0; i < properties.length; i = i + 1) {
            animateElements(elements, properties, i);
        }
    }, animateShrinking = function(elements) {
        var properties = [ {
            "top" : "1px",
            "left" : "2px",
            "height" : "9px",
            "width" : "15px"
        }, {
            "top" : "2px",
            "left" : "3px",
            "height" : "7px",
            "width" : "13px"
        }, {
            "top" : "2px",
            "left" : "4px",
            "height" : "7px",
            "width" : "11px"
        }, {
            "top" : "3px",
            "left" : "5px",
            "height" : "5px",
            "width" : "9px"
        } ], i;

        for (i = 0; i < properties.length; i = i + 1) {
            animateElements(elements, properties, i);
        }
    }, setupMouseEvents = function(mirror, expandButton) {
        var expandButtonId = expandButton.attr("id");

        mirror
                .on("mouseleave", function() {
                    expandButton.stop().fadeOut(100);
                })
                .on("mouseenter", "div#" + expandButtonId + ".off", function() {
                    InputPanel.setLocked(true);
                    animateGrowing(jQuery(".inner div", expandButton));
                })
                .on("mouseleave", "div#" + expandButtonId + ".off", function() {
                    InputPanel.setLocked(false);
                    animateShrinking(jQuery(".inner div", expandButton));
                })
                .on("mouseenter", "div#" + expandButtonId + ".on", function() {
                    InputPanel.setLocked(true);
                    animateShrinking(jQuery(".inner div", expandButton));
                })
                .on("mouseleave", "div#" + expandButtonId + ".on", function() {
                    InputPanel.setLocked(false);
                    animateGrowing(jQuery(".inner div", expandButton));
                })
                .on(
                        "mousemove",
                        function(event) {
                            var horizontalOffset = jQuery(mirror).offset().left
                                    + jQuery(mirror).outerWidth() - event.pageX, verticalOffset = event.pageY
                                    - jQuery(mirror).offset().top;

                            if (mirror.hasClass("focus")) {
                                if (horizontalOffset < 80
                                        && verticalOffset < 80) {
                                    expandButton.stop().fadeIn(100);
                                } else {
                                    expandButton.stop().fadeOut(100);
                                }
                            }
                        });
    }, addExpandButton = function(mirror, editor) {
        var expandButtonId = mirror.attr("id") + "-expand", expandButton = jQuery(
                "<div></div>").attr("id", expandButtonId), scrollLeft, scrollTop, mirrorHeight = mirror
                .height();

        expandButton.addClass("expandButton off").hide();
        expandButton.append(jQuery("<div></div>").addClass("inner").append(
                jQuery("<div></div>")));
        mirror.prepend(expandButton);

        jQuery(document).on("click", "div#" + expandButtonId + ".off",
                function() {
                    scrollLeft = jQuery(window).scrollLeft();
                    scrollTop = jQuery(window).scrollTop();

                    expandMirror(mirror, editor, expandButton);
                    InputPanel.setFullscreenMode(true);
                }).on(
                "click",
                "div#" + expandButtonId + ".on",
                function() {
                    collapseMirror(mirror, editor, expandButton, scrollLeft,
                            scrollTop, mirrorHeight);
                    InputPanel.setFullscreenMode(false);
                });

        setupMouseEvents(mirror, expandButton);
    }, init = function(mirrorId, textareaId, basePath, readOnly) {
        var mirror = jQuery("#" + mirrorId), textarea = jQuery("#" + textareaId), editor = ace
                .edit(mirrorId), container = mirror.parent();

        editors[mirrorId] = editor;

        ace.config.set("basePath", basePath);
        setupApostropheKey(editor, mirror);
        setupXmlEditor(mirror, textarea, editor, container, readOnly);
        addExpandButton(mirror, editor);
    };

    return {
        init : init,
        editors : editors
    };
}());
