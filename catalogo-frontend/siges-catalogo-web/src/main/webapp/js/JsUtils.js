/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function scrollToDivIfNotVisible(id) {
    var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();

    var elemTop = $("#" + id).offset().top;
    var elemBottom = elemTop + $("#" + id).height();


    if ((elemBottom <= docViewBottom) && (elemTop >= docViewTop)) {
    } else {
        $('html,body').animate({
            scrollTop: $("#" + id).offset().top - 10
        });
    }
}

function scrollToTop() {

    $('html,body').animate({
        scrollTop: 0
    });

}

function centerAndShowAyudaDialog() {
    ayudaDialog.initPosition();
    ayudaDialog.show();
}

PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'ant.',
    nextText: 'sig.',
    currentText: 'actual',
    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun',
        'Jul', 'Ago', 'Set', 'Oct', 'Nov', 'Dic'],
    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
    dayNamesShort: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá'],
    dayNamesMin: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sá'],
    weekHeader: 'Sem.',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText: 'Todos los días'
};


function htmlbodyHeightUpdate() {
    var screenwidth = $(window).width();
    if (screenwidth > 575) {
        var height3 = $(window).height();
        var height1 = $(".ui-menu-list").height() + 311;
        height2 = $(".content-general").height();
        if (height2 + 138 > height3) {
            $("html").height(Math.max(height1, height3, height2 + 138));
            $("body").height(Math.max(height1, height3, height2 + 138));
            $("#sidebar").height(Math.max(height1, height3, height2 + 138));
        } else {
            $("html").height(Math.max(height1, height3, height2));
            $("body").height(Math.max(height1, height3, height2));
            $("#sidebar").height(Math.max(height1, height3, height2));
        }
    } else {
        $("html").removeAttr('style');
        $("body").removeAttr('style');
        $("#sidebar").removeAttr('style');
        $("#sidebar").removeClass('active');
    }
}

$(document).ready(function () {
    htmlbodyHeightUpdate()
    $(window).resize(function () {
        htmlbodyHeightUpdate()
    });
    $(window).scroll(function () {
        height2 = $(".content-general").height();
        htmlbodyHeightUpdate()
    });

    $('.overlay').on('click', function () {
        $('#sidebar').removeClass('active');
        $('.overlay').fadeOut();
    });

    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        $('.overlay').fadeToggle();

        if (576 > $(window).width()) {
            if ($("#sidebar").hasClass("active")) {
                heightMenu = $('.ui-menu-list').height() + 117;
                $("#sidebar").animate({height: heightMenu});
            } else {
                $("#sidebar").removeAttr('style');
            }
        }
    });

    $('#sidebarCollapseDescktop').on('click', function () {
        $('#sidebar').toggleClass('min');
        $('#content').toggleClass('max');
    });
});

