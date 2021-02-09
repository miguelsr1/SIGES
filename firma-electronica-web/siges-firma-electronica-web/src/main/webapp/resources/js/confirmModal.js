var confirmo = false;
var ultimoBotonId;

/**
 * para suar con eliminarGeneralModal.xhtml
 * @param {type} btnId
 * @param {type} tiulo
 * @param {type} mensaje
 * @returns {Boolean}
 */
function existeConfirmacion(btnId) {
    ultimoBotonId = btnId;
    if (!confirmo) {
        $('#confirmModalCallBack .ocultar-al-abrir').hide();
        $('#confirmModalCallBack').modal('show');
        return false;
    } else {
        confirmo = false;
        return true;
    }
}

/**
 * para suar con eliminarGeneralModal.xhtml
 * @param {type} btnId
 * @param {type} tiulo
 * @param {type} mensaje
 * @returns {Boolean}
 */
function existeConfirmacionEliminar(btnId) {
    ultimoBotonId = btnId;
    if (!confirmo) {
        $('#confirmModalCancelar .ocultar-al-abrir').hide();
        $('#confirmModalCancelar').modal('show');
        return false;
    } else {
        confirmo = false;
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

function existeConfirmacionConParametros(btnId, titulo, mensaje) {
    ultimoBotonId = btnId;
    if (!confirmo) {
        $('#confirmModalCallBack .ocultar-al-abrir').hide();
        $('#confirmModalCallBackTitle').html(titulo);
        $('#confirmModalCallBackBody').html(mensaje);
        $('#confirmModalCallBack').modal('show');
        return false;
    } else {
        confirmo = false;
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

function confirmacionCancelar(btnId, mensaje, idFunc) {
    ultimoBotonId = btnId;
    if (!confirmo) {
        $('#confirmModalCancelar .ocultar-al-abrir').hide();
        $('#confirmModalCancelarBody').html(mensaje);
        $('#confirmModalCancelar').modal('show');
        $('#idFunc').hide();
        $('#idFunc').val(idFunc);
        return false;
    } else {
        confirmo = false;
        return true;
    }
}

function confirmarCancel() {
    console.log("Llego hasta confirmar");
    confirmo = true;
    var motivo = document.getElementById('#motivo').value;
    alert(motivo);
    document.getElementById('#mensaje');
    document.getElementById(ultimoBotonId).click();
    console.log("fin confirmar");
}

function confirmar() {
    console.log("Llego hasta confirmar");
    confirmo = true;
    document.getElementById(ultimoBotonId).click();
    console.log("fin confirmar");
}

