/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ProyectoFinanciamientoSistemaIntegradoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pfs SgProyectoFinanciamientoSistemaIntegrado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProyectoFinanciamientoSistemaIntegrado pfs) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pfs==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(pfs.getPfsCodigo())) {
                ge.addError("pfsCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pfs.getPfsCodigo().length() > 45){
                ge.addError("pfsCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(pfs.getPfsNombre())) {
                ge.addError("pfsNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pfs.getPfsNombre().length() > 255){
                ge.addError("pfsNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgProyectoFinanciamientoSistemaIntegrado
     * @param c2 SgProyectoFinanciamientoSistemaIntegrado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgProyectoFinanciamientoSistemaIntegrado c1, SgProyectoFinanciamientoSistemaIntegrado c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getPfsCodigo(), c2.getPfsCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getPfsNombre(), c2.getPfsNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getPfsHabilitado(), c2.getPfsHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
