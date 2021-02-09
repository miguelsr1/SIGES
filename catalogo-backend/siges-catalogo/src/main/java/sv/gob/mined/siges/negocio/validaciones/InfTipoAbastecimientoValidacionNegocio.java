/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfTipoAbastecimiento;

/**
 *
 * @author Sofis Solutions
 */
public class InfTipoAbastecimientoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tab SgInfTipoAbastecimiento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfTipoAbastecimiento tab) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tab==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tab.getTabCodigo())) {
                ge.addError("tabCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tab.getTabCodigo().length() > 45){
                ge.addError("tabCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tab.getTabNombre())) {
                ge.addError("tabNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tab.getTabNombre().length() > 255){
                ge.addError("tabNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

 
}
