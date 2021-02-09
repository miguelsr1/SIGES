/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReqFondoCed;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 * Validación de las reglas de negocio de los requerimientos de fondos (detalle
 * de los centros educativos)
 *
 * @author Sofis Solutions
 */
public class ReqFondoCedValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rfc SgReqFondoCed
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReqFondoCed rfc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rfc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
//            if (rfc.getRfcSolTransferenciaFk()==null) {
//                ge.addError("rfcSolTransferenciaFk", Errores.ERROR_SOL_TRANS_REQUERIMIENTO);
//            }
//
//            if (rfc.getRfcTransferenciaCedFk()==null) {
//                ge.addError("rfcTransferenciaCedFk", Errores.ERROR_TRANS_CED_REQUERIMIENTO);
//            }
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
     * @param c1 SgReqFondoCed
     * @param c2 SgReqFondoCed
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgReqFondoCed c1, SgReqFondoCed c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = BooleanUtils.sonIguales(c1.getRfcHabilitado(), c2.getRfcHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getRfcHabilitado(), c2.getRfcHabilitado());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
