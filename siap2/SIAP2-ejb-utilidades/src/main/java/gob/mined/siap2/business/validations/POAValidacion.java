/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAActividadMeta;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class POAValidacion {

    public boolean validar(POA p, Boolean indicador, Boolean metasActividadesObligatorias, Boolean validarSeguimiento) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (p == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if(StringUtils.isBlank(p.getNombre())) {
               ge.addError(ConstantesErrores.ERR_NOMBRE_VACIO); 
            } else if(p.getNombre().trim().length() > 100) {
                ge.addError(ConstantesErrores.ERR_NOMBRE_LARGO_100); 
            }
                    
                   
            if (p.getUnidadTecnica()== null) {
                ge.addError(ConstantesErrores.ERR_UNIDAD_TECNICA_VACIA);
            }
            if (p.getProgramaInstitucional() == null) {
                ge.addError(ConstantesErrores.ERR_PROGRAMA_INSTITUCIONAL_VACIO);
            }
            if (p.getAnio() == null) {
                ge.addError(ConstantesErrores.ERR_ANIO_FISCAL_NO_SELECCIONADO);
            }
            if (p.getEstado() == null) {
                ge.addError(ConstantesErrores.ERR_ESTADO_NO_SELECCIONADO);
            }
            if(metasActividadesObligatorias != null && metasActividadesObligatorias) {
                if(p.getMetasAnuales() == null || p.getMetasAnuales().isEmpty()) {
                    ge.addError(ConstantesErrores.ERR_DEBE_AGREGAR_ALMENOS_UNA_TAREA_POA);
                }
            }
            if(p.getMetasAnuales() != null && !p.getMetasAnuales().isEmpty() && p.getMetasAnuales().size() > 0) {
                if(p.getAnio() != null && p.getAnio().getAnio() != null && p.getAnio().getAnio() > 0) {
                    List<String> erroresMetas = validarMetas(p.getMetasAnuales(), p.getAnio().getAnio(), indicador, metasActividadesObligatorias, validarSeguimiento);
                    if(!erroresMetas.isEmpty() && erroresMetas.size() > 0) {
                        ge.getErrores().addAll(erroresMetas);
                    }
                }
            } 
            
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    } 
    
    public List<String> validarMetas(List<POAMetaAnual> lista, Integer anio, Boolean indicador, Boolean metasActividadesObligatorias, Boolean validarSeguimiento) throws BusinessException {
        List<String> errores = new ArrayList();
        List<String> erroresAgregar = new ArrayList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            for(POAMetaAnual meta : lista) {
                //Meta Anual
                if(StringUtils.isBlank(meta.getMetaAnual())) {
                    erroresAgregar.add(ConstantesErrores.ERR_META_ANUAL_VACIO);
                } else if(meta.getMetaAnual().length() > 500) {
                    erroresAgregar.add(ConstantesErrores.ERR_META_ANUAL_LARGO_500);
                }
                //Indicador
                if(meta.getIndicador() == null) {
                   erroresAgregar.add(ConstantesErrores.ERR_INDICADOR_VACIO);
                }
                //Medio de verificación
                if(StringUtils.isBlank(meta.getMedioVerificacionIndicador())) {
                    erroresAgregar.add(ConstantesErrores.ERR_MEDIO_VERIFICACION_VACIO);
                } else if(meta.getMedioVerificacionIndicador().length() > 500) {
                    erroresAgregar.add(ConstantesErrores.ERR_MEDIO_VERIFICACION_LARGO_500);
                }
                
                if(meta.getTipoMedicion() == null) {
                    erroresAgregar.add(ConstantesErrores.ERR_FORMA_MEDICION_VACIO);
                }

                //Programa Trimestral
                if((meta.getProgramaPrimerTrimestre() == null || meta.getProgramaPrimerTrimestre().compareTo(0) == 0)
                        && (meta.getProgramaSegundoTrimestre() == null || meta.getProgramaSegundoTrimestre().compareTo(0) == 0)
                        && (meta.getProgramaTercerTrimestre() == null || meta.getProgramaTercerTrimestre().compareTo(0) == 0)
                        && (meta.getProgramaCuartoTrimestre() == null || meta.getProgramaCuartoTrimestre().compareTo(0) == 0)) {
                    erroresAgregar.add(ConstantesErrores.ERR_PROGRAMA_TRIMESTRAL_VACIO);
                }
                
                if(validarSeguimiento != null && validarSeguimiento) {
                    if(StringsUtils.isEmpty(meta.getAlcanceLimitaciones())) {
                        erroresAgregar.add(ConstantesErrores.ERR_SEGUIMIENTO_ALCANCE_LIMITACIONES_VACIO);
                    }
                    if(StringsUtils.isEmpty(meta.getMedioVerificacion())) {
                        erroresAgregar.add(ConstantesErrores.ERR_SEGUIMIENTO_MEDIO_VERIFICACION_VACIO);
                    }
                }
                if(erroresAgregar.size() > 0) {
                    errores.add(ConstantesErrores.ERR_METAS_DATOS_INCOMPLETOS);
                    errores.addAll(erroresAgregar);
                    break;
                }
                
                
                
                if(metasActividadesObligatorias != null && metasActividadesObligatorias) {
                    if(meta.getActividades() == null || meta.getActividades().isEmpty()) {
                        errores.add(ConstantesErrores.ERR_DEBE_AGREGAR_ALMENOS_UNA_ACTIVIDAD_A_TAREA_POA);
                        errores.addAll(erroresAgregar);
                        break;
                    }
                }
                if(meta.getActividades() != null && !meta.getActividades().isEmpty() && meta.getActividades().size() > 0) {
                    List<String> erroresActividades = validarActividades(meta.getActividades(), anio, indicador);
                    if(erroresActividades != null && !erroresActividades.isEmpty() && erroresActividades.size() > 0) {
                        errores.add(ConstantesErrores.ERR_ACTIVIDADES_DATOS_INCOMPLETOS);
                        errores.addAll(erroresActividades);
                    }
                }
            }
        }
        return errores;
    }
    
    public List<String> validarActividades(List<POAActividadMeta> lista, Integer anio, Boolean indicador) throws BusinessException {
        List<String> errores = new ArrayList();
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            for(POAActividadMeta act : lista) {
                if(StringUtils.isBlank(act.getNombre())) {
                    errores.add(ConstantesErrores.ERR_NOMBRE_VACIO);
                } else if(act.getNombre().length() > 500) {
                    errores.add(ConstantesErrores.ERR_NOMBRE_LARGO_500);
                }

                if(act.getPeriodoEjecusionDesde() == null) {
                   errores.add(ConstantesErrores.ERR_PERIOD0_EJECUCION_DESDE_VACIO);
                } else {
                    LocalDate date = act.getPeriodoEjecusionDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if(anio.compareTo(date.getYear()) != 0) {
                        errores.add(ConstantesErrores.ERR_FECHA_DESDE_FUERA_ANIO_FISCAL);
                    }
                }
                
                if(act.getPeriodoEjecusionHasta() == null) {
                   errores.add(ConstantesErrores.ERR_PERIOD0_EJECUCION_HASTA_VACIO);
                } else {
                    LocalDate date = act.getPeriodoEjecusionHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if(anio.compareTo(date.getYear()) != 0) {
                        errores.add(ConstantesErrores.ERR_FECHA_HASTA_FUERA_ANIO_FISCAL);
                    }
                }
                
                if(act.getPeriodoEjecusionDesde() != null && act.getPeriodoEjecusionHasta() != null) {
                    if(act.getPeriodoEjecusionHasta().before(act.getPeriodoEjecusionDesde())) {
                        errores.add(ConstantesErrores.ERR_PERIOD0_EJECUCION_FECHA_MAYOR_MENOR);
                    }
                }
                
                if(StringUtils.isBlank(act.getFuncionarioResponsable())) {
                    errores.add(ConstantesErrores.ERR_FUNCIONARIO_RESPONSABLE_VACIO);
                } else if(act.getFuncionarioResponsable().length() > 50) {
                    errores.add(ConstantesErrores.ERR_FUNCIONARIO_RESPONSABLE_LARGO_50);
                }
                
                if(act.getUnidadTecnicaResponsable() == null) {
                   errores.add(ConstantesErrores.ERR_UNIDAD_TECNICA_VACIA);
                }
                
                if(indicador != null && indicador) {
                    if(act.getPorcentajeAvancePrimerTrimestre() != null && (act.getPorcentajeAvancePrimerTrimestre().compareTo(0) < 0 || act.getPorcentajeAvancePrimerTrimestre().compareTo(100) > 0)) {
                        errores.add(ConstantesErrores.ERR_PORCENTAJE_PRIMER_TRIMESTRE_INVALIDO);
                    }
                    if(act.getPorcentajeAvanceSegundoTrimestre() != null && (act.getPorcentajeAvanceSegundoTrimestre().compareTo(0) < 0 || act.getPorcentajeAvanceSegundoTrimestre().compareTo(100) > 0)) {
                        errores.add(ConstantesErrores.ERR_PORCENTAJE_SEGUNDO_TRIMESTRE_INVALIDO);
                    }
                    if(act.getPorcentajeAvanceTercerTrimestre() != null && (act.getPorcentajeAvanceTercerTrimestre().compareTo(0) < 0 || act.getPorcentajeAvanceTercerTrimestre().compareTo(100) > 0)) {
                        errores.add(ConstantesErrores.ERR_PORCENTAJE_TERCER_TRIMESTRE_INVALIDO);
                    }
                    if(act.getPorcentajeAvanceCuartoTrimestre() != null && (act.getPorcentajeAvanceCuartoTrimestre().compareTo(0) < 0 || act.getPorcentajeAvanceCuartoTrimestre().compareTo(100) > 0)) {
                        errores.add(ConstantesErrores.ERR_PORCENTAJE_CUARTO_TRIMESTRE_INVALIDO);
                    }
                }
                if(errores.size() > 0) {
                    break;
                }
            }
        }
        return errores;
    }

}