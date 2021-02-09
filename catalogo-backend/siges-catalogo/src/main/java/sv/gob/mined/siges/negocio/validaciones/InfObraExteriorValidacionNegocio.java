/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfObraExterior;

/**
 *
 * @author Sofis Solutions
 */
public class InfObraExteriorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param oex SgInfObraExterior
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfObraExterior oex) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (oex==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(oex.getOexCodigo())) {
                ge.addError("oexCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (oex.getOexCodigo().length() > 45){
                ge.addError("oexCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(oex.getOexNombre())) {
                ge.addError("oexNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (oex.getOexNombre().length() > 255){
                ge.addError("oexNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
