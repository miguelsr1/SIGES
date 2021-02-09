/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;

/**
 *
 * @author Sofis Solutions
 */
public class ArchivoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param arc SgArchivo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgArchivo arc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (arc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
           
            if (StringUtils.isBlank(arc.getAchNombre())) {
                ge.addError("arcNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (arc.getAchNombre().length() > 255){
                ge.addError("arcNombre",Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgArchivo
     * @param c2 SgArchivo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgArchivo c1, SgArchivo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
              
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
