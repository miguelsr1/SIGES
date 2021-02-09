/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIdiomaRealizado;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class IdiomaRealizadoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgIdiomaRealizado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgIdiomaRealizado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getIreIdioma() == null){
                ge.addError("ireIdioma", Errores.ERROR_IDIOMA_VACIO);
            }
            if (entity.getIreNivelIdioma() == null){
                ge.addError("ireNivelIdioma", Errores.ERROR_NIVEL_IDIOMA_VACIO);
            }
            if (entity.getIreEstudiosRealizados() == null){
                ge.addError("ireEstudiosRealizados", Errores.ERROR_ESTUDIOS_REALIZADOS_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
     /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgIdiomaRealizado
     * @param c2 SgIdiomaRealizado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgIdiomaRealizado c1, SgIdiomaRealizado c2) {
        
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getIreIdioma(), c2.getIreIdioma());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getIreNivelIdioma(), c2.getIreNivelIdioma());            
                respuesta = respuesta && StringUtils.equals(c1.getIreObservaciones(), c2.getIreObservaciones());  
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getIreValidado(), c2.getIreValidado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    

}
