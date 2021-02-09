var confirmo=false;
var ultimoBotonId;

/**
 * para suar con eliminarGeneralModal.xhtml
 * @param {type} btnId
 * @param {type} tiulo
 * @param {type} mensaje
 * @returns {Boolean}
 */
function existeConfirmacion(btnId){
    ultimoBotonId=btnId;
    if (!confirmo){
        $('#confirmModalCallBack .ocultar-al-abrir').hide();  
        //si no existe una confirmacion se habre el modal de confirmacion
        $('#confirmModalCallBack').modal('show');

        return false;
        //$('#confirmModalCallBack').find('.modal-body .ocultar-al-abrir').hide();         
    }else{
        //en caso de existir una confirmacion se continua con el flujo normal
        confirmo= false;
        return true;
    }
}


/**
 * se llama del confirmarConParametros.xhtml
 * @param {type} btnId
 * @param {type} tiulo
 * @param {type} mensaje
 * @returns {Boolean}
 */

function existeConfirmacionConParametros(btnId, titulo, mensaje){
    console.log(" 1 ");
    
    ultimoBotonId=btnId;
    
    console.log(" 1 ");
    if (!confirmo){
        $('#confirmModalCallBackConParametros .ocultar-al-abrir').hide();  
        //si no existe una confirmacion se habre el modal de confirmacion
        //se inicializa el contenido del modal
        $('#modal-confirmarConParametros-titulo-custom').html(titulo);
        $('#modal-confirmarConParametros-contenido-custom').html(mensaje);
        
        //se abre el modal
        $('#confirmModalCallBackConParametros').modal('show');

        return false;
        //$('#confirmModalCallBack').find('.modal-body .ocultar-al-abrir').hide();         
    }else{
        //en caso de existir una confirmacion se continua con el flujo normal
        confirmo= false;
        return true;
    }
}






function confirmar(){
    confirmo = true;
    document.getElementById(ultimoBotonId).click();
}




