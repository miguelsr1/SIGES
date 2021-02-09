/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCierreAnioLectivoSede;

/**
 *
 * @author bruno
 */
public class SgCierreAnioLectivoSedeValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgPlaza
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCierreAnioLectivoSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if(entity.getCalSede() == null){
                ge.addError("calSede", Errores.ERROR_SEDE_VACIO);
            }
                
            if (entity.getCalAnioLectivo()==null){
                ge.addError("calAnioLectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }     
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
