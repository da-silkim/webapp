$.fn.liveBorder = function(options){
    var settings = $.extend({}, {top: false, top2: false, right: false, right2: false, bottom: true, bottom2: false, left: false, left2: false}, options);
    var border = $("<div>").addClass("live-border");

    if(settings.top)
        border.append($("<div>", {class: "border top"}).append($("<div>", {class: "bar"}))); //하단
    if(settings.top2)
        border.append($("<div>", {class: "border top2"}).append($("<div>", {class: "bar"}))); //하단2
    if(settings.right)
        border.append($("<div>", {class: "border right"}).append($("<div>", {class: "bar"}))); // 우측
    if(settings.right2)
        border.append($("<div>", {class: "border right2"}).append($("<div>", {class: "bar"}))); // 우측2
    if(settings.bottom)
        border.append($("<div>", {class: "border bottom"}).append($("<div>", {class: "bar"}))); // 상단
    if(settings.bottom2)
        border.append($("<div>", {class: "border bottom2"}).append($("<div>", {class: "bar"}))); // 상단2
    if(settings.left)
        border.append($("<div>", {class: "border left"}).append($("<div>", {class: "bar"}))); //좌측
    if(settings.left2)
        border.append($("<div>", {class: "border left2"}).append($("<div>", {class: "bar"}))); //좌측2
        
   // border.insertAfter($(this));
    $(this).html(border);
}