/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.dto.SpMensajeRespuesta;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;

/**
 *
 * @author usuario
 */
public class MensajeRespuestaValidacionNegocio {
        
    public static boolean validar(SpMensajeRespuesta entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_ENVIANDO_DATOS_SIMPLE);
        } else {          
            if (StringUtils.isBlank(entity.getEstado())){
                ge.addError("estado", Errores.ERROR_ENVIANDO_DATOS_SIMPLE);
            } 
            else{
                if(!entity.getEstado().equals("200")){
                    ge.addError("estado", Errores.ERROR_ENVIANDO_DATOS_SIMPLE);
                }
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
