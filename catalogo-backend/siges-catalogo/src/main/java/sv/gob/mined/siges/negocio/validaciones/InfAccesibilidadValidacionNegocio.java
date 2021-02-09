/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfAccesibilidad;

/**
 *
 * @author Sofis Solutions
 */
public class InfAccesibilidadValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param acc SgInfAccesibilidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfAccesibilidad acc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (acc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(acc.getAccCodigo())) {
                ge.addError("accCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (acc.getAccCodigo().length() > 45){
                ge.addError("accCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(acc.getAccNombre())) {
                ge.addError("accNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (acc.getAccNombre().length() > 255){
                ge.addError("accNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
