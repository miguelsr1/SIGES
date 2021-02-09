/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgGestionAnioLectivo; 

public class GestionAnioLectivoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgGestionAnioLectivo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getGalFechaDesde() == null){
                ge.addError("galFechaDesde", Errores.ERROR_FECHA_VACIO);
            }
            if (entity.getGalFechaHasta() == null){
                ge.addError("galFechaHasta", Errores.ERROR_FECHA_VACIO);
            }
            if (entity.getGalAnio() == null){
                ge.addError("galAnio", Errores.ERROR_ANIO_VACIO);
            }
            if (entity.getGalFechaDesde().isAfter(entity.getGalFechaHasta())){
                ge.addError("galFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}

