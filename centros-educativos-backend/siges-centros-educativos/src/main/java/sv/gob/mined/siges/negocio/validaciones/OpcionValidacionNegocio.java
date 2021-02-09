/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgOpcion; 

public class OpcionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgNivel
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgOpcion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(entity.getOpcCodigo())){
                ge.addError("opcCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getOpcCodigo().length() > 4){
                ge.addError("opcCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(entity.getOpcNombre())){
                ge.addError("opcNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getOpcNombre().length() > 500){
                ge.addError("opcNombre", Errores.ERROR_LARGO_NOMBRE_500);
            }
            if(entity.getOpcRelacionOpcionProgramaEdu()==null || entity.getOpcRelacionOpcionProgramaEdu().isEmpty()){
                ge.addError("opcProgramaEducativo", Errores.ERROR_SELECCION_PROGRAMA_EDUCATIVO_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}