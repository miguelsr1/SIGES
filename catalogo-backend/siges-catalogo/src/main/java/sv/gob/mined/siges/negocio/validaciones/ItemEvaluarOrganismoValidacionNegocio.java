/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgItemEvaluarOrganismo;

/**
 *
 * @author Sofis Solutions
 */
public class ItemEvaluarOrganismoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ieo SgItemEvaluarOrganismo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgItemEvaluarOrganismo ieo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ieo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ieo.getIeoCodigo())) {
                ge.addError("ieoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ieo.getIeoCodigo().length() > 45){
                ge.addError("ieoCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ieo.getIeoNombre())) {
                ge.addError("ieoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ieo.getIeoNombre().length() > 255){
                ge.addError("ieoNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
