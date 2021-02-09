/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstudioRealizado; 

public class EstudioRealizadoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgEstudioRealizado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstudioRealizado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (entity.getEreEscolaridad() == null){
                ge.addError("ereEscolaridad", Errores.ERROR_ESCOLARIDAD_VACIO);
            }     
            if (entity.getEreSistemaGestionContenido() == null){
                ge.addError("ereSistemaGestionContenido", Errores.ERROR_SISTEMA_GESTION_CONTENIDO_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }    
    
}
