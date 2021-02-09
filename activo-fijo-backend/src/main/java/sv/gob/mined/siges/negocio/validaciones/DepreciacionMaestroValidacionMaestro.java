/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;

public class DepreciacionMaestroValidacionMaestro {
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfDepreciacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfDepreciacionMaestro entity, Integer anioLimiteMenorAdmitido, Boolean anioEspecifico) throws BusinessException {
        boolean respuesta = true;
        
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(anioEspecifico != null) {
                if(anioEspecifico) {
                    if (entity.getDmaAnioProceso()== null) {
                        ge.addError("dmaAnioProceso", Errores.ERROR_ANIO_VACIO);
                    } else {
                        if(entity.getDmaAnioProceso().compareTo(anioLimiteMenorAdmitido) == -1) {
                            ge.addError("dmaAnioProceso", Errores.ERROR_ANIO_MENOR_LIMITE_MENOR_A_1960);
                        } else {
                            BigDecimal anioActual = new BigDecimal(LocalDate.now().getYear());
                            BigDecimal anioProceso = new BigDecimal(entity.getDmaAnioProceso());
                            if(anioProceso.compareTo(anioActual) == 0 && entity.getDmaMesProceso()!= null) {
                                BigDecimal mesActual = new BigDecimal(LocalDate.now().getMonthValue());
                                BigDecimal mesProceso = new BigDecimal(entity.getDmaMesProceso());
                                if(mesProceso.compareTo(mesActual) == 1) {
                                    ge.addError("mes", Errores.ERROR_MES_MAYOR_ACTUAL);
                                }
                            } else if(anioProceso.compareTo(anioActual) == 1) {
                                ge.addError("anio", Errores.ERROR_ANIO_MAYOR_ACTUAL);
                            }
                        }  
                    }
                    
                    if(entity.getDmaEstado() == null) {
                        ge.addError("dmaEstado", Errores.ERROR_ESTADO_VACIO);
                    }
                } else {
                    if (entity.getDmaFuenteFinanciamientoFk()== null) {
                        ge.addError("dmaFuenteFinanciamientoFk", Errores.ERROR_FUENTE_FINANCIAMIENTO_ORIGEN_VACIO);
                    }
                }
            }
            
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
}
