/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPlantilla;

/**
 *
 * @author Sofis Solutions
 */
public class PlantillaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pla SgPlantilla
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPlantilla pla) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pla==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(pla.getPlaCodigo())) {
                ge.addError("plaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pla.getPlaCodigo().length() > 45){
                ge.addError("plaCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(pla.getPlaNombre())) {
                ge.addError("plaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pla.getPlaNombre().length() > 255){
                ge.addError("plaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(pla.getPlaArchivo()==null){
                ge.addError("plaArchivo", Errores.ERROR_ARCHIVO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
