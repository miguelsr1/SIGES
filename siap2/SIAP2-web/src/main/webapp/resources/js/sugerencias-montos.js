var sugerencias_fuentes = [];


function reinicializarSugerencias(my_sugerencias){
    console.log(my_sugerencias);
    console.log("REINICIALIZAR SUGERENCIAS");
    sugerenicas_fuetnes= my_sugerencias;
    console.log(sugerencias_fuentes);
    inicializarSugerencias();
}


function extractor(query) {
    var result = /([^ ]+)$/.exec(query);
    if (result && result[1])
        return result[1].trim();
    return '';
}

function inicializarSugerencias(){        
    //se crea el objeto
    var autocomplete = $('.typeahead').typeahead({
        source: sugerencias_fuentes,
        updater: function(item) {
            return this.$element.val().replace(/[^ ]*$/,'')+item+' ';
        },
        matcher: function (item) {
          var tquery = extractor(this.query);
          if(!tquery) return false;
          return ~item.toLowerCase().indexOf(tquery.toLowerCase());
        },
        highlighter: function (item) {
          var query = extractor(this.query).replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
          return item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>';
          });
        }
    });
    //se actualiza las fuentes para las existentes
    if ( autocomplete.data('typeahead')){
        autocomplete.data('typeahead').source = sugerencias_fuentes;        
    }
    

}

$(document).ready(function() { 
    $('#anadirParipassu').on('shown.bs.modal', function () {
        try {
            inicializarSugerencias();
        } catch (e) {
            console.log(e);
        }
    });
  $('#anadirParipassuConTramos').on('shown.bs.modal', function () {
        try {
            inicializarSugerencias();
        } catch (e) {
            console.log(e);
        }
    });

});
   
   
   
function cambioTipoMonto(data) {
    var status = data.status; // Can be "begin", "complete" or "success".
    var source = data.source; // The parent HTML DOM element.

    switch (status) {
        case "begin": // Before the ajax request is sent.
            // ...
            break;

        case "complete": // After the ajax response is arrived.
           //...
            break;

        case "success": // After update of HTML DOM based on ajax response.
            inicializarSugerencias();
            break;
    }
}