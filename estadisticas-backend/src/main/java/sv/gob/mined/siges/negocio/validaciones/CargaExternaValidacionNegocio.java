/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCargaExterna;

/**
 *
 * @author Sofis Solutions
 */
public class CargaExternaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ese SgEstadisticaEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCargaExterna ese) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ese == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (ese.getCarNombre() == null) {
                ge.addError("carNombre", Errores.ERROR_NOMBRE_EXTRACCION_VACIO);
            }

            if (ese.getCarAnio() == null) {
                ge.addError("carAnio", Errores.ERROR_ANIO_VACIO);
            }
                        
            if (ese.getCarIndicador() == null){
                ge.addError("carIndicador", Errores.ERROR_CATEGORIA_O_INDICADOR_VACIO);
            }
           
            if (ese.getCarDescripcion()!= null && ese.getCarDescripcion().length() > 2000) {
                ge.addError("carDescripcion", Errores.ERROR_LARGO_DESCRIPCION_2000);
            }
            
            if (StringUtils.isBlank(ese.getCarExcelTmpPath())){
                ge.addError("carArchivo", Errores.ERROR_ARCHIVO_VACIO);
            }
            
            if (ese.getCarTipoNumerico() == null){
                ge.addError("carTipoNumerico", Errores.ERROR_TIPO_NUMERICO_VACIO);
            }

           

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
