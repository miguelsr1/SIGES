/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoOrganismoAdministrativo;

/**
 *
 * @author Sofis Solutions
 */
public class TipoOrganismoAdministrativoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param toa SgTipoOrganismoAdministrativo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoOrganismoAdministrativo toa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (toa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(toa.getToaCodigo())) {
                ge.addError("toaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (toa.getToaCodigo().length() > 45){
                ge.addError("toaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(toa.getToaNombre())) {
                ge.addError("toaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (toa.getToaNombre().length() > 255){
                ge.addError("toaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(toa.getToaPlazo() != null && toa.getToaPlazo() > 10){
                ge.addError("toaPlazo", Errores.ERROR_PLAZO_10ANIOS);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
