/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCursoDocente;

public class CursoDocenteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCursoDocente entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getCdsFechaInicio() == null) {
                ge.addError("cdsFechaInicio", Errores.ERROR_FECHA_INICIO_VACIA);
            }
            if (entity.getCdsFechaFin() == null) {
                ge.addError("cdsFechaFin", Errores.ERROR_FECHA_FIN_VACIA);
            }
            
            if(entity.getCdsFechaInicio()!=null && entity.getCdsFechaFin()!=null){
                if(entity.getCdsFechaInicio().isAfter(entity.getCdsFechaFin())){
                    ge.addError("cdsFechaInicio", Errores.ERROR_FECHA_INICIO_MAYOR_FIN);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
