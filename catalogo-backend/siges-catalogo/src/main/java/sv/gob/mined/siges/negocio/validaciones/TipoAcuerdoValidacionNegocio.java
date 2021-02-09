/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAcuerdo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoAcuerdoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tao SgTipoAcuerdo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoAcuerdo tao) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tao == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tao.getTaoCodigo())) {
                ge.addError("taoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tao.getTaoCodigo().length() > 45) {
                ge.addError("taoCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tao.getTaoNombre())) {
                ge.addError("taoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tao.getTaoNombre().length() > 255) {
                ge.addError("taoNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgTipoAcuerdo
     * @param c2 SgTipoAcuerdo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoAcuerdo c1, SgTipoAcuerdo c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringUtils.equals(c1.getTaoCodigo(), c2.getTaoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTaoNombre(), c2.getTaoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoHabilitado(), c2.getTaoHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteAmpliacionServicios(), c2.getTaoTramiteAmpliacionServicios());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteDisminucionServicios(), c2.getTaoTramiteDisminucionServicios());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteCambioDomicilioSede(), c2.getTaoTramiteCambioDomicilioSede());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteCreacionSede(), c2.getTaoTramiteCreacionSede());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteNominacionSede(), c2.getTaoTramiteNominacionSede());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTaoTramiteRevocatoriaSede(), c2.getTaoTramiteRevocatoriaSede());

            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
