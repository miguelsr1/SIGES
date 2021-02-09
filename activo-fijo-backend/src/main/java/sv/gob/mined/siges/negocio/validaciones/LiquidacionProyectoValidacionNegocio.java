/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfLiquidacionProyecto;

public class LiquidacionProyectoValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfLiquidacionProyecto
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfLiquidacionProyecto entity, Integer anioLimiteMenorAdmitido) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DESCAGO_VACIO);
        } else {
            if (entity.getLprFuenteFinanciamientoOrigenFk() == null) {
                ge.addError("lprFuenteFinanciamientoOrigenFk", Errores.ERROR_FUENTE_FINANCIAMIENTO_ORIGEN_VACIO);
            }
            
            if(entity.getLprProyectoFk() == null) {
                ge.addError("lprProyectoFk", Errores.ERROR_PROYECTO_VACIO);
            }
            
            if (entity.getLprFuenteFinanciamientoDestinoFk() == null) {
                ge.addError("lprFuenteFinanciamientoDestinoFk", Errores.ERROR_FUENTE_FINANCIAMIENTO_DESTINO_VACIO);
            }
            
            if(entity.getLprFechaLiquidacion() == null) {
                ge.addError("lprFechaLiquidacion", Errores.ERROR_FECHA_LIQUIDACION_VACIO);
            } else {
                //La fecha de adquisiciòn no puede ser mayor a la fecha actual y tampoco puede ser menor que el año de limite menor
                if(entity.getLprFechaLiquidacion().isAfter(LocalDate.now())) {
                    ge.addError("lprFechaLiquidacion", Errores.ERROR_FECHA_LIQUIDACION_MAYOR_FECHA_ACTUAL);
                } else if(entity.getLprFechaLiquidacion().getYear() < anioLimiteMenorAdmitido) {
                    ge.addError("lprFechaLiquidacion", Errores.ERROR_FECHA_LIQUIDACION_MENOR_FECHA_LIMITE_MENOR);
                }
            }
             
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
}
