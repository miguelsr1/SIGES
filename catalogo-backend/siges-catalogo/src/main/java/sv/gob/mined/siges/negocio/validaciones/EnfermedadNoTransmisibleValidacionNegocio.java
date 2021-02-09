/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEnfermedadNoTransmisible; 
import sv.gob.mined.siges.utils.BooleanUtils;


/**
 *
 * @author Sofis Solutions
 */  
public class EnfermedadNoTransmisibleValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ent SgEnfermedadNoTransmisible
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEnfermedadNoTransmisible ent) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ent==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ent.getEntCodigo())) {
                ge.addError("entCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ent.getEntCodigo().length() > 45){
                ge.addError("entCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ent.getEntNombre())) {
                ge.addError("entNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ent.getEntNombre().length() > 255){
                ge.addError("entNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEnfermedadNoTransmisible
     * @param c2 SgEnfermedadNoTransmisible
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEnfermedadNoTransmisible c1, SgEnfermedadNoTransmisible c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEntCodigo(), c2.getEntCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEntNombre(), c2.getEntNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEntHabilitado(), c2.getEntHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
