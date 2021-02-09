/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgImplementadora;

/**
 *
 * @author Sofis Solutions
 */
public class ImplementadoraValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param imp SgImplementadoras
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgImplementadora imp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (imp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(imp.getImpCodigo())) {
                ge.addError("impCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (imp.getImpCodigo().length() > 45){
                ge.addError("impCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(imp.getImpNombre())) {
                ge.addError("impNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (imp.getImpNombre().length() > 255){
                ge.addError("impNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
