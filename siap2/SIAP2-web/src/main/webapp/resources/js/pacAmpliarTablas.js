function reajustarGrupos(){
    if ($('#grupos-div.col-sm-6').length) {
        //muestra completo el div grupos
         $( "#grupos-div" ).removeClass( "col-sm-6 ocultar-columnas");
         $( "#grupos-div" ).removeClass( "col-sm-6 ocultar-columnas");
         $( "#grupos-div" ).addClass( "col-sm-12");
         $( "#insumos-div" ).addClass( "hidden" );
         $( "#ampliarGrupo" ).removeClass( "glyphicon-fullscreen");
         $( "#ampliarGrupo" ).addClass( "glyphicon-arrow-left");
         
    }else{
        //oculta div grupos
         $( "#grupos-div" ).removeClass( "col-sm-12");
         $( "#grupos-div" ).addClass( "col-sm-6 ocultar-columnas" );
         $( "#insumos-div" ).removeClass( "hidden" );
         $( "#ampliarGrupo" ).removeClass( "glyphicon-arrow-left");
         $( "#ampliarGrupo" ).addClass( "glyphicon-fullscreen");
    }
}


function reajustarInsumos(){
    if ($('#insumos-div.col-sm-6').length) {
        //muestra completo el div grupos
         $( "#insumos-div" ).removeClass( "col-sm-6 ocultar-columnas");
         $( "#insumos-div" ).removeClass( "col-sm-6 ocultar-columnas");
         $( "#insumos-div" ).addClass( "col-sm-12");
         $( "#grupos-div" ).addClass( "hidden" );
         $( "#ampliarInsumos" ).removeClass( "glyphicon-fullscreen");
         $( "#ampliarInsumos" ).addClass( "glyphicon-arrow-left");
         
    }else{
        //oculta div grupos
         $( "#insumos-div" ).removeClass( "col-sm-12");
         $( "#insumos-div" ).addClass( "col-sm-6 ocultar-columnas" );
         $( "#grupos-div" ).removeClass( "hidden" );
         $( "#ampliarInsumos" ).removeClass( "glyphicon-arrow-left");
         $( "#ampliarInsumos" ).addClass( "glyphicon-fullscreen");
    }
}