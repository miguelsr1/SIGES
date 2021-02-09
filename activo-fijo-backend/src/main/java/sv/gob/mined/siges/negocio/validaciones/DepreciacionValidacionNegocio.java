/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import sv.gob.mined.siges.dto.ProcesoDepreciacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion;


public class DepreciacionValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfDepreciacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfDepreciacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DESCAGO_VACIO);
        } else {
            if (entity.getDepAnio()== null) {
                ge.addError("depAnio", Errores.ERROR_ANIO_VACIO);
            }
            
            if (entity.getDepMes()== null) {
                ge.addError("depMes", Errores.ERROR_MES_VACIO);
            }
            
            if (entity.getDepNoDiasDepreciados()== null) {
                ge.addError("depNoDiasDepreciados", Errores.ERROR_NUMERO_DIAS_DEPRECIADOS_VACIO);
            }
            
            if (entity.getDepMontoDepreciacion()== null) {
                ge.addError("depMontoDepreciacion", Errores.ERROR_MONTO_DEPRECIACION_VACIO);
            }
            
            if (entity.getDepBienesDepreciablesFk()== null) {
                ge.addError("depBienesDepreciablesFk", Errores.ERROR_BIEN_VACIO);
            }

             if (entity.getDepFuenteFinanciamientoFk() == null) {
                ge.addError("depFuenteFinanciamientoFk", Errores.ERROR_FUENTE_FINANCIAMIENTO_VACIO);
             } else {
                if(entity.getDepFuenteFinanciamientoFk().getFfiRequiereProyecto() != null && entity.getDepFuenteFinanciamientoFk().getFfiRequiereProyecto()) {
                    if(entity.getDepProyectoFk() == null) {
                        ge.addError("depProyectoFk", Errores.ERROR_PROYECTO_VACIO);
                    }
                }
            }
             
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfDepreciacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarProcesoDepreciacion(ProcesoDepreciacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DESCAGO_VACIO);
        } else {
            if (entity.getAnio()== null) {
                ge.addError("anio", Errores.ERROR_ANIO_VACIO);
            } else {
                BigDecimal anioActual = new BigDecimal(LocalDate.now().getYear());
                BigDecimal anioProceso = new BigDecimal(entity.getAnio());
                if(anioProceso.compareTo(anioActual) == 0 && entity.getMes()!= null) {
                    BigDecimal mesActual = new BigDecimal(LocalDate.now().getMonthValue());
                    BigDecimal mesProceso = new BigDecimal(entity.getMes());
                    if(mesProceso.compareTo(mesActual) == 1) {
                        ge.addError("mes", Errores.ERROR_MES_MAYOR_ACTUAL);
                    }
                } else if(anioProceso.compareTo(anioActual) == 1) {
                    ge.addError("anio", Errores.ERROR_ANIO_MAYOR_ACTUAL);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
}
