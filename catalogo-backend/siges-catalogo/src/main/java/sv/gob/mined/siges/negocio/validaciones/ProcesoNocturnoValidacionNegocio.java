/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProcesoNocturno;

/**
 *
 * @author Sofis Solutions
 */
public class ProcesoNocturnoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param prn SgProcesoNocturno
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProcesoNocturno prn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (prn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
         
            if (StringUtils.isBlank(prn.getPrnNombre())) {
                ge.addError("prnNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (prn.getPrnNombre().length() > 255){
                ge.addError("prnNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(prn.getPrnServicio()==null){
                ge.addError("prnNombre", Errores.ERROR_SERVICIO_VACIO);
            }
            if (StringUtils.isBlank(prn.getPrnUrl())) {
                ge.addError("prnNombre", Errores.ERROR_URL_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
