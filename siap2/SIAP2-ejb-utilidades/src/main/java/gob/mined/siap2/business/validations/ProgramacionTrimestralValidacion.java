/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.exceptions.BusinessException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class ProgramacionTrimestralValidacion {
    
    public boolean validar(ProgramacionTrimestral p) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (p == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if (p.getAnio() == null) {
                ge.addError(ConstantesErrores.ERR_ANIO_FISCAL_NO_SELECCIONADO);
            } 
            
            List<String> primerTrimestre = validarTrimestres(p.getFechaDesdePrimerTrimestre(), p.getFechaHastaPrimerTrimestre(), (p.getAnio() != null ? p.getAnio().getAnio() : null), "PRIMER_TRIMESTRE");
            List<String> segundoTrimestre = validarTrimestres(p.getFechaDesdeSegundoTrimestre(), p.getFechaHastaSegundoTrimestre(), (p.getAnio() != null ? p.getAnio().getAnio() : null), "SEGUNDO_TRIMESTRE");
            List<String> tercerTrimestre = validarTrimestres(p.getFechaDesdeTercerTrimestre(), p.getFechaHastaTercerTrimestre(), (p.getAnio() != null ? p.getAnio().getAnio() : null), "TERCER_TRIMESTRE");
            List<String> cuartoTrimestre = validarTrimestres(p.getFechaDesdeCuartoTrimestre(), p.getFechaHastaCuartoTrimestre(), (p.getAnio() != null ? p.getAnio().getAnio() : null), "CUARTO_TRIMESTRE");
            
            ge.getErrores().addAll(primerTrimestre);
            ge.getErrores().addAll(segundoTrimestre);
            ge.getErrores().addAll(tercerTrimestre);
            ge.getErrores().addAll(cuartoTrimestre);
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    private List<String> validarTrimestres(Date fechaInicial, Date fechaFinal, Integer anio, String modificador) throws BusinessException {
        LocalDateTime dateDesde = null;
        LocalDateTime dateHasta = null;
        List<String> errores = new LinkedList();
        try {
            if(fechaInicial != null || modificador != null) {
                dateDesde = fechaInicial != null ? LocalDateTime.ofInstant(fechaInicial.toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0) : null;
                dateHasta = fechaFinal != null ? LocalDateTime.ofInstant(fechaFinal.toInstant(),ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0) : null;
                    
                if(dateDesde != null && dateHasta != null) {
                    if(anio != null) {
                        if((dateDesde.getYear() == anio) && (dateHasta.getYear() == anio)) {
                            if(dateDesde.isAfter(dateHasta)) {
                                errores.add("ERR_" + modificador + "_FECHA_DESDE_MAYOR_FECHA_HASTA");
                            }
                        } else {
                            if((dateDesde.getYear() != anio) && (dateHasta.getYear() != anio)) {
                                errores.add("ERR_" + modificador + "_FECHA_FUERA_RANGO");
                            } else {
                                if(dateDesde.getYear() != anio) {
                                    errores.add("ERR_" + modificador + "_FECHA_DESDE_FUERA_RANGO");
                                } else if(dateHasta.getYear() != anio) {
                                    errores.add("ERR_" + modificador + "_FECHA_HASTA_FUERA_RANGO");
                                }
                            }
                        }
                    } else {
                       if(dateDesde.isAfter(dateHasta)) {
                            errores.add("ERR_" + modificador + "_FECHA_DESDE_MAYOR_FECHA_HASTA");
                        } 
                    }
                    
                } else {
                    if(dateDesde == null && dateHasta != null) {
                        errores.add("ERR_" + modificador + "_FECHA_DESDE_BLIGATORIO");
                    } else {
                        if(anio != null && dateDesde != null && dateDesde.getYear() != anio) {
                           errores.add("ERR_" + modificador + "_FECHA_DESDE_FUERA_RANGO");
                        }
                        
                    }
                    
                    if(dateHasta == null && dateDesde != null) {
                        errores.add("ERR_" + modificador + "_FECHA_HASTA_BLIGATORIO");
                    } else {
                        if(anio !=null && dateHasta != null && dateHasta.getYear() != anio) {
                          errores.add("ERR_" + modificador + "_FECHA_HASTA_FUERA_RANGO");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return errores;
    }
}
