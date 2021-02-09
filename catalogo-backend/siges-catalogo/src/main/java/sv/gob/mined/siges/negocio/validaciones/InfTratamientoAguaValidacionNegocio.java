/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfTratamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
public class InfTratamientoAguaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tra SgInfTratamientoAgua
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfTratamientoAgua tra) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tra==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tra.getTraCodigo())) {
                ge.addError("traCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tra.getTraCodigo().length() > 45){
                ge.addError("traCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tra.getTraNombre())) {
                ge.addError("traNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tra.getTraNombre().length() > 255){
                ge.addError("traNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
