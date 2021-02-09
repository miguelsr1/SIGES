$('.confirm-modal').on('show.bs.modal', function (e) {
    //console.log("entro en el nuevo modal");    
    var valor = $(e.relatedTarget).data('id');
    $(this).find('.modal-body input').val(valor);    
    //console.log("seteo el valor " + valor);
    $(this).find('.modal-body .ocultar-al-abrir').hide();   
});

