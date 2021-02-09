$(document).ready(function () {
    totalizarTabla();
});


function totalizarTabla() {
    var totals = [];

    var $dataRows = $("#sum_table tr:not('.totalColumn, .titlerow')");

    $dataRows.each(function () {
        $(this).find('.rowSumarData').each(function (i) {
            if (!totals[i]) {
                totals[i] = numeral(0);
            }
            if ($(this).has("input").length) {
                var numero = numeral($(this).children("input").val());
                totals[i] = numero.add(totals[i]);
            } else {
                var numero = numeral(numeral().unformat($(this).children("span").html()));
                totals[i] = numero.add(totals[i]);
            }

        });
    });
    $("#sum_table td.totalCol").each(function (i) {
        if (totals[i]) {
            $(this).html(totals[i].format('0,0.00'));
        } else {
            $(this).html(totals[i]);
        }

    });

}
