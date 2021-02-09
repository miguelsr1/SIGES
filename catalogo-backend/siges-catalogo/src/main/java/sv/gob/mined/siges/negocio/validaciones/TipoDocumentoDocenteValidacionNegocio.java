/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoDocumentoDocente;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoDocumentoDocenteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tdd SgTipoDocumentoDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoDocumentoDocente tdd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tdd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tdd.getTddCodigo())) {
                ge.addError("tddCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tdd.getTddCodigo().length() > 45){
                ge.addError("tddCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tdd.getTddNombre())) {
                ge.addError("tddNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tdd.getTddNombre().length() > 255){
                ge.addError("tddNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoDocumentoDocente
     * @param c2 SgTipoDocumentoDocente
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoDocumentoDocente c1, SgTipoDocumentoDocente c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTddCodigo(), c2.getTddCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTddNombre(), c2.getTddNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTddHabilitado(), c2.getTddHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
