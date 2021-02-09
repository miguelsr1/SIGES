/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAlcanceCapacitacion; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class AlcanceCapacitacionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param aca SgAlcanceCapacitacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAlcanceCapacitacion aca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (aca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(aca.getAcaCodigo())){
                ge.addError("acaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (aca.getAcaCodigo().length() > 45){
                ge.addError("acaCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(aca.getAcaNombre())){
                ge.addError("acaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (aca.getAcaNombre().length() > 255){
                ge.addError("acaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgAlcanceCapacitacion
     * @param c2 SgAlcanceCapacitacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAlcanceCapacitacion c1, SgAlcanceCapacitacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAcaCodigo(), c2.getAcaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAcaNombre(), c2.getAcaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAcaHabilitado(), c2.getAcaHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}

