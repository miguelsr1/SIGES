/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPerfilesUsuariosIngresadosCe;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class PerfilesUsuariosIngresadosCeValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pui SgPerfilesUsuariosIngresadosCe
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPerfilesUsuariosIngresadosCe pui) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pui==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(pui.getPuiCodigo())) {
                ge.addError("puiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pui.getPuiCodigo().length() > 45){
                ge.addError("puiCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(pui.getPuiNombre())) {
                ge.addError("puiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pui.getPuiNombre().length() > 255){
                ge.addError("puiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgPerfilesUsuariosIngresadosCe
     * @param c2 SgPerfilesUsuariosIngresadosCe
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgPerfilesUsuariosIngresadosCe c1, SgPerfilesUsuariosIngresadosCe c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getPuiCodigo(), c2.getPuiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getPuiNombre(), c2.getPuiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getPuiHabilitado(), c2.getPuiHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
