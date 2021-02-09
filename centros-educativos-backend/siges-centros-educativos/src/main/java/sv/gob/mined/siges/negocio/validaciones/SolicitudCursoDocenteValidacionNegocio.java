/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumEstadoCurso;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente; 

public class SolicitudCursoDocenteValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgSolicitudCursoDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSolicitudCursoDocente entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if(entity.getScdCurso() == null){
                ge.addError("scdCurso", Errores.ERROR_CURSO_DOCENTE_VACIO);
            }else if(!entity.getScdCurso().getCdsEstado().equals(EnumEstadoCurso.PUBLICADO)){
                ge.addError("scdCurso", Errores.ERROR_CURSO_DOCENTE_NO_PUBLICADO);
            }
            
            if (entity.getScdPersonal()==null){
                ge.addError("scdPersonal", Errores.ERROR_PERSONAL_VACIO);
            }     
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
