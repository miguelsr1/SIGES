/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfItemObraExterior;

/**
 *
 * @author Sofis Solutions
 */
public class InfItemObraExteriorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ioe SgInfItemObraExterior
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfItemObraExterior ioe) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ioe==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ioe.getIoeCodigo())) {
                ge.addError("ioeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ioe.getIoeCodigo().length() > 45){
                ge.addError("ioeCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ioe.getIoeNombre())) {
                ge.addError("ioeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ioe.getIoeNombre().length() > 255){
                ge.addError("ioeNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(ioe.getIoeObraExterior()==null){
                ge.addError("ioeNombre", Errores.ERROR_OBRA_EXTERIOR_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

   
}
