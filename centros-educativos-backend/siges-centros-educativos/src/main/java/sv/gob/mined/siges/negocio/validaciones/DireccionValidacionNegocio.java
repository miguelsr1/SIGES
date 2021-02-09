/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion; 


public class DireccionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDireccion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDireccion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(entity.getDirDireccion())){
                ge.addError("dirDireccion", Errores.ERROR_DIRECCION_VACIA);
            } else if (entity.getDirDireccion().length() > 255){
                ge.addError("dirDireccion", Errores.ERROR_LARGO_DIRECCION_255);
            }            
            if (entity.getDirDepartamento() == null){
                ge.addError("dirDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
            } 
            if (entity.getDirMunicipio() == null){
                ge.addError("dirMunicipio", Errores.ERROR_MUNICIPIO_VACIO);
            }
            if (entity.getDirZona()== null){
                ge.addError("dirZona", Errores.ERROR_ZONA_VACIO);
            } 
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
