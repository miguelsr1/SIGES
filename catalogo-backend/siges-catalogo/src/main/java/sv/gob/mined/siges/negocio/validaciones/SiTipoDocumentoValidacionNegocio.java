/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSiTipoDocumento;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class SiTipoDocumentoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tid SgSiTipoDocumento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSiTipoDocumento tid) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tid==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tid.getTidCodigo())) {
                ge.addError("tidCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tid.getTidCodigo().length() > 45){
                ge.addError("tidCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tid.getTidNombre())) {
                ge.addError("tidNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tid.getTidNombre().length() > 255){
                ge.addError("tidNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgSiTipoDocumento
     * @param c2 SgSiTipoDocumento
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSiTipoDocumento c1, SgSiTipoDocumento c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTidCodigo(), c2.getTidCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTidNombre(), c2.getTidNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTidHabilitado(), c2.getTidHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
