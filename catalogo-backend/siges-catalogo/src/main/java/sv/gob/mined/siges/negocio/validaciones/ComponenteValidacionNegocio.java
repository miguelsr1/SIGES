/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgComponente;

/**
 *
 * @author Sofis Solutions
 */
public class ComponenteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cpn SgComponente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgComponente cpn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cpn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cpn.getCpnNombre())) {
                ge.addError("cpnNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cpn.getCpnNombre().length() > 100){
                ge.addError("cpnNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            
            if(!StringUtils.isBlank(cpn.getCpnDescripcion()) && cpn.getCpnDescripcion().length()>255){
                ge.addError("cpnDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
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
     * @param c1 SgComponente
     * @param c2 SgComponente
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgComponente c1, SgComponente c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCpnNombre(), c2.getCpnNombre());
                respuesta = respuesta && StringUtils.equals(c1.getCpnDescripcion(), c2.getCpnDescripcion());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
