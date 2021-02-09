/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.dto.SgAfBienDepreciableInmDTO;
import sv.gob.mined.siges.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.persistencia.utilidades.OperacionesGenerales;


public class BienDepreciableValidacionNegocio {

    public static boolean validarCategoria(SgAfBienDepreciable entity ) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getBdeTipoBien()== null) {
               ge.addError("bdeTipoBien", Errores.ERROR_TIPO_BIEN_VACIO);
            } else {
                if(entity.getBdeTipoBien().getTbiCategoriaBienes() == null ) {
                    ge.addError("tbiCategoriaBienes", Errores.ERROR_CATEGORIA_VACIO);
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
     * @param entity SgAfBienDepreciable
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfBienDepreciable entity, Integer anioLimiteMenorAdmitido, Boolean validarDatosVehiculo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        SgAfCategoriaBienes categoria = null;
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getBdeTipoBien()== null) {
               ge.addError("bdeTipoBien", Errores.ERROR_TIPO_BIEN_VACIO);
            }
 
            if(entity.getBdeVidaUtil() == null) {
               ge.addError("bdeVidaUtil", Errores.ERROR_VIDA_UTIL);
            } else if(BigDecimal.ZERO.compareTo(new BigDecimal(entity.getBdeVidaUtil())) >= 0 ) {
                ge.addError("bdeVidaUtil", Errores.ERROR_VIDA_UTIL_CERO);
            }
                
            if(entity.getBdeCategoriaFk() == null && entity.getBdeTipoBien().getTbiCategoriaBienes() == null) {
                ge.addError("bdeCategoriaFk", Errores.ERROR_CATEGORIA_VACIO);
            } else {
                categoria = entity.getBdeCategoriaFk() != null ? entity.getBdeCategoriaFk() : entity.getBdeTipoBien().getTbiCategoriaBienes();
                        
                if(categoria.getCabValorMaximo() == null) {
                    ge.addError("cabValorMaximo", Errores.ERROR_CATEGORIA_VALOR_MAXIMO_VACIO);
                }
                
                if(OperacionesGenerales.existeEnArray(categoria.getSgAfSeccionesCategoriaList(), EnumSeccionesCargoBienes.SECCION_VEHICULOS) ) {
                    if(validarDatosVehiculo != null && validarDatosVehiculo) {
                        if(entity.getBdeNoPlaca() == null) {
                            ge.addError("bdeNoPlaca", Errores.ERROR_NUM_PLACA_VACIO);
                        }
                        if(entity.getBdeNoChasis() == null) {
                            ge.addError("BdeNoChasis", Errores.ERROR_NUM_CHASIS_VACIO);
                        }
                        if(entity.getBdeNoMotor() == null) {
                            ge.addError("bdeNoMotor", Errores.ERROR_NUM_MOTOR_VACIO);
                        } 
                    }
                    
                } else if(OperacionesGenerales.existeEnArray(categoria.getSgAfSeccionesCategoriaList(), EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
                    if(entity.getBdeInmuebleId() == null) {
                        ge.addError("bdeInmuebleId", Errores.ERROR_ID_INMUEBLE_VACIO);
                    }
                }
            }
            
            if (entity.getBdeUnidadesAdministrativas() == null && entity.getBdeSede() == null) {
               ge.addError("bdeUnidadesAdministrativas", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }

            //Validacion para bienes de categoria terrenos y edificios. Para bienes de tipo inmueble no se valida el 
            //correlativo ni código de inventario la primera vez, ya que el sistema las genera.
            if(OperacionesGenerales.existeEnArray(entity.getBdeTipoBien().getTbiCategoriaBienes().getSgAfSeccionesCategoriaList(), EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
                if(entity.getBdePk() != null) {
                    if (StringUtils.isBlank(entity.getBdeCodigoCorrelativo())) {
                        ge.addError("bdeCodigoCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_VACIO);
                    } else {
                        if(BigDecimal.ZERO.compareTo(new BigDecimal(entity.getBdeCodigoCorrelativo())) >= 0 ) {
                            ge.addError("beNumCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_CERO);
                        }
                    }
                }
            } else {
                if (StringUtils.isBlank(entity.getBdeCodigoCorrelativo())) {
                    ge.addError("bdeCodigoCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_VACIO);
                } else {
                    if(BigDecimal.ZERO.compareTo(new BigDecimal(entity.getBdeCodigoCorrelativo())) >= 0 ) {
                        ge.addError("beNumCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_CERO);
                    }
                }
            }
            
            if (StringUtils.isBlank(entity.getBdeCodigoInventario())) {
               ge.addError("bdeCodigoInventario", Errores.ERROR_CODIGO_INVENTARIO_VACIO);
            }
            if (entity.getBdeEstadosBienes()== null) {
               ge.addError("bdeEstadosBienes", Errores.ERROR_ESTADO_BIEN_VACIO);
            }
            if (entity.getBdeEstadoCalidad() == null) {
               ge.addError("bdeEstadoCalidad", Errores.ERROR_ESTADO_CALIDAD_VACIO);
            }
            if (StringUtils.isBlank(entity.getBdeAsignadoA()) && entity.getBdeEmpleadoFk() == null) {
               ge.addError("bdeAsignadoA", Errores.ERROR_ASIGNADOA_VACIO);
            }
            
            if (StringUtils.isBlank(entity.getBdeDescripcion())) {
               ge.addError("bdeDescripcion", Errores.ERROR_DESCRIPCION_VACIO);
            } else if(entity.getBdeDescripcion().length() > 500) {
               ge.addError("bdeDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
            }
            if(entity.getBdeFormaAdquisicion() == null) {
                ge.addError("bdeFormaAdquisicion", Errores.ERROR_FORMA_ADQUISICION_VACIO);
            } 
            if(entity.getBdeFuenteFinanciamiento() == null) {
                ge.addError("bdeFuenteFinanciamiento", Errores.ERROR_FUENTE_FINANCIAMIENTO_VACIO);
            } else {
                if(entity.getBdeFuenteFinanciamiento().getFfiRequiereProyecto() != null && entity.getBdeFuenteFinanciamiento().getFfiRequiereProyecto()) {
                    if(entity.getBdeProyectos() == null) {
                        ge.addError("bdeProyectos", Errores.ERROR_PROYECTO_VACIO);
                    }
                }
            }
            
            if(entity.getBdeFechaAdquisicion() == null) {
                ge.addError("bdeFechaAdquisicion", Errores.ERROR_FECHA_ADQUISICION_VACIO);
            } else {
                //La fecha de adquisiciòn no puede ser mayor a la fecha actual y tampoco puede ser menor que el año de limite menor
                if(entity.getBdeFechaAdquisicion().isAfter(LocalDate.now())) {
                    ge.addError("bdeFechaAdquisicion", Errores.ERROR_FECHA_ADQUISICION_MAYOR_FECHA_ACTUAL);
                } else if(entity.getBdeFechaAdquisicion().getYear() < anioLimiteMenorAdmitido) {
                    ge.addError("bdeFechaAdquisicionMenor", Errores.ERROR_FECHA_ADQUISICION_MENOR_FECHA_LIMITE_MENOR);
                }
            }
            
            if(entity.getBdeValorAdquisicion() == null) {
                ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_VACIO);
            } else {
                if(BigDecimal.ZERO.compareTo(entity.getBdeValorAdquisicion()) >= 0 ) {
                    ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_CERO_MENOR_CERO);
                }
                if(categoria != null && categoria.getCabValorMaximo() != null) {
                    if(entity.getBdeValorAdquisicion().compareTo(categoria.getCabValorMaximo()) > 0) {
                        ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_MAYOR_CATEGORIA_VALOR_MAXIMO_VACIO);
                    }
                }
            }
            if(entity.getBdeEsValorEstimado() == null) {
                ge.addError("bdeEsValorEstimado", Errores.ERROR_ES_VALOR_ESTIMADO_VACIO);
            }
            
            if(entity.getBdeEsLote() != null && entity.getBdeEsLote()) {
                if(entity.getBdeCantidadLote() == null || entity.getBdeCantidadLote().compareTo(0) == 0) {
                    ge.addError("bdeCantidadLote", Errores.ERROR_CANTIDAD_LOTE_CERO);
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
     * @param entity SgAfBienDepreciable
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarObligatoriosBase(SgAfBienDepreciable entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getBdeTipoBien()== null) {
               ge.addError("bdeTipoBien", Errores.ERROR_TIPO_BIEN_VACIO);
            }

            if (entity.getBdeUnidadesAdministrativas() == null && entity.getBdeSede() == null) {
               ge.addError("bdeUnidadesAdministrativas", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }

            if (StringUtils.isBlank(entity.getBdeCodigoCorrelativo())) {
                ge.addError("bdeCodigoCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_VACIO);
            }
            if (StringUtils.isBlank(entity.getBdeCodigoInventario())) {
               ge.addError("bdeCodigoInventario", Errores.ERROR_CODIGO_INVENTARIO_VACIO);
            }
            if (entity.getBdeEstadosBienes()== null) {
               ge.addError("bdeEstadosBienes", Errores.ERROR_ESTADO_BIEN_VACIO);
            }
            if (entity.getBdeEstadoCalidad() == null) {
               ge.addError("bdeEstadoCalidad", Errores.ERROR_ESTADO_CALIDAD_VACIO);
            }
            
            if(entity.getBdeFechaAdquisicion() == null) {
                ge.addError("bdeFechaAdquisicion", Errores.ERROR_FECHA_ADQUISICION_VACIO);
            }
            
            if(entity.getBdeValorAdquisicion() == null) {
                ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_VACIO);
            } else {
                if(BigDecimal.ZERO.compareTo(entity.getBdeValorAdquisicion()) >= 0 ) {
                    ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_CERO_MENOR_CERO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    /**
     * Valida que los datos del elemento sean correctos desde infraestructura
     *
     * @param entity SgAfBienDepreciable
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarDesdeInfraestructura(SgAfBienDepreciable entity, Boolean validarRangoFecha, Integer anioLimiteMenorAdmitido) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        SgAfCategoriaBienes categoria = null;
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getBdeTipoBien()== null) {
               ge.addError("bdeTipoBien", Errores.ERROR_TIPO_BIEN_VACIO);
            } 
            if(entity.getBdeCategoriaFk() == null && entity.getBdeTipoBien().getTbiCategoriaBienes() == null) {
                ge.addError("bdeCategoriaFk", Errores.ERROR_CATEGORIA_VACIO);
            } else {
                categoria = entity.getBdeCategoriaFk() != null ? entity.getBdeCategoriaFk() : entity.getBdeTipoBien().getTbiCategoriaBienes();
                
                if(categoria.getCabValorMaximo() == null) {
                    ge.addError("cabValorMaximo", Errores.ERROR_CATEGORIA_VALOR_MAXIMO_VACIO);
                }
                if(OperacionesGenerales.existeEnArray(categoria.getSgAfSeccionesCategoriaList(), EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
                    if(entity.getBdeInmuebleId() == null) {
                        ge.addError("bdeInmuebleId", Errores.ERROR_ID_INMUEBLE_VACIO);
                    }
                }
            }
            
            if (entity.getBdeUnidadesAdministrativas() == null && entity.getBdeSede() == null) {
               ge.addError("bdeUnidadesAdministrativas", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }
            
            if (StringUtils.isBlank(entity.getBdeCodigoCorrelativo())) {
                ge.addError("bdeCodigoCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_VACIO);
            } else if(BigDecimal.ZERO.compareTo(new BigDecimal(entity.getBdeCodigoCorrelativo())) >= 0 ) {
                ge.addError("beNumCorrelativo", Errores.ERROR_CODIGO_CORRELATIVO_CERO);
            }
                
            if (StringUtils.isBlank(entity.getBdeCodigoInventario())) {
               ge.addError("bdeCodigoInventario", Errores.ERROR_CODIGO_INVENTARIO_VACIO);
            }
            if (entity.getBdeEstadosBienes()== null) {
               ge.addError("bdeEstadosBienes", Errores.ERROR_ESTADO_BIEN_VACIO);
            }
            
            if (entity.getBdeEstadoCalidad() == null) {
               ge.addError("bdeEstadoCalidad", Errores.ERROR_ESTADO_CALIDAD_VACIO);
            }

            if(StringUtils.isNotBlank(entity.getBdeDescripcion()) && entity.getBdeDescripcion().length() > 500) {
               ge.addError("bdeDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
            }
            
            if(entity.getBdeValorAdquisicion() == null) {
                ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_VACIO);
            } else {
                if(BigDecimal.ZERO.compareTo(entity.getBdeValorAdquisicion()) >= 0 ) {
                    ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_CERO_MENOR_CERO);
                }
                if(categoria != null && categoria.getCabValorMaximo() != null) {
                    if(entity.getBdeValorAdquisicion().compareTo(entity.getBdeCategoriaFk().getCabValorMaximo()) > 0) {
                        ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_MAYOR_CATEGORIA_VALOR_MAXIMO_VACIO);
                    }
                }
            }
            if(entity.getBdeEsValorEstimado() == null) {
                ge.addError("bdeEsValorEstimado", Errores.ERROR_ES_VALOR_ESTIMADO_VACIO);
            }
            
            if(entity.getBdeFechaAdquisicion() == null) {
                ge.addError("bdeFechaAdquisicion", Errores.ERROR_FECHA_ADQUISICION_VACIO);
            } else {
                if(validarRangoFecha != null && validarRangoFecha) {
                    //La fecha de adquisiciòn no puede ser mayor a la fecha actual y tampoco puede ser menor que el año de limite menor
                    if(entity.getBdeFechaAdquisicion().isAfter(LocalDate.now())) {
                        ge.addError("bdeFechaAdquisicion", Errores.ERROR_FECHA_ADQUISICION_MAYOR_FECHA_ACTUAL);
                    } else if(entity.getBdeFechaAdquisicion().getYear() < anioLimiteMenorAdmitido) {
                        ge.addError("bdeFechaAdquisicionMenor", Errores.ERROR_FECHA_ADQUISICION_MENOR_FECHA_LIMITE_MENOR);
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
     * @param entity SgAfBienDepreciable
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfBienDepreciableInmDTO entity, Integer anioLimiteMenorAdmitido) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(entity.getBdeInmueblePk() == null && entity.getBdeEdificioPk() == null) {
                ge.addError("bdeInmueblePk", Errores.ERROR_ID_INMUEBLE_VACIO);
            }
            
            if (entity.getBdeTipoBien()== null) {
               ge.addError("bdeTipoBien", Errores.ERROR_TIPO_BIEN_VACIO);
            } else {
                if(entity.getBdeTipoBien().getTbiCategoriaBienes() == null) {
                    ge.addError("tbiCategoriaBienes", Errores.ERROR_CATEGORIA_VACIO);
                }
            }
            if (entity.getBdeUnidadesAdministrativas() == null && entity.getBdeSede() == null) {
               ge.addError("bdeUnidadesAdministrativas", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }

            if (entity.getBdeEstadosBienes()== null) {
               ge.addError("bdeEstadosBienes", Errores.ERROR_ESTADO_BIEN_VACIO);
            }
            if (entity.getBdeEstadoCalidad() == null) {
               ge.addError("bdeEstadoCalidad", Errores.ERROR_ESTADO_CALIDAD_VACIO);
            }
            
            if(StringUtils.isNotBlank(entity.getBdeDescripcion()) && entity.getBdeDescripcion().length() > 500) {
               ge.addError("bdeDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
            }
            
            if(entity.getBdeValorAdquisicion() == null) {
                ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_VACIO);
            } else {
                if(BigDecimal.ZERO.compareTo(entity.getBdeValorAdquisicion()) >= 0 ) {
                    ge.addError("bdeValorAdquisicion", Errores.ERROR_VALOR_ADQUISICION_CERO_MENOR_CERO);
                }
            }
            if(entity.getBdeEsValorEstimado() == null) {
                ge.addError("bdeEsValorEstimado", Errores.ERROR_ES_VALOR_ESTIMADO_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}