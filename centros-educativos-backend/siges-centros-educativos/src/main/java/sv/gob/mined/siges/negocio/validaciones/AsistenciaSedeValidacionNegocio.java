/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsistenciaSede; 

public class AsistenciaSedeValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgAsistenciaSede
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAsistenciaSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (entity.getAseAnio()==null){
                ge.addError("aseAnio", Errores.ERROR_ANIO_VACIO);
            }
                 
            if (entity.getAseTipoApoyo()==null){
                ge.addError("aseTipoApoyo", Errores.ERROR_TIPO_APOYO_VACIO);
            }
                 
            if (entity.getAseTipoProveedor()==null){
                ge.addError("aseTipoProveedor", Errores.ERROR_TIPO_PROVEEDOR_VACIO);
            }
            
            if (StringUtils.isBlank(entity.getAseNombreProveedor())){
                ge.addError("aseNombreProveedor", Errores.ERROR_NOMBRE_PROVEEDOR_VACIO);
            } else if (entity.getAseNombreProveedor().length() > 255){
                ge.addError("aseNombreProveedor", Errores.ERROR_LARGO_NOMBRE_PROVEEDOR_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}