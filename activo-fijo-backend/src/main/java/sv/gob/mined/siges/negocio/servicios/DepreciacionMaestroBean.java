/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.DatoDepreciacion;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.enumerados.EnumOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DepreciacionMaestroValidacionMaestro;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DepreciacionMaestroDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;

@Stateless
@Traced
public class DepreciacionMaestroBean {
    private static final Logger LOGGER = Logger.getLogger(DepreciacionMaestroBean.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private DepreciacionBean depreciacionBean;
    
    @Inject
    private BienesDepreciablesBean bienes;
    
    @Inject
    @ConfigProperty(name = "anio-limite-menor-admitido", defaultValue = "1960")
    private Integer anioLimiteMenorAdmitido;
    
    @Inject
    @ConfigProperty(name = "valor-activo-fijo-limite", defaultValue = "600")
    private BigDecimal valorActivoFijoLimite;
    
    @Inject
    @ConfigProperty(name = "tareas-noprocesadas-admitidas", defaultValue = "15")
    private Long tareasNoProcesadasAdmitidas;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDepreciacionMaestro
     * @throws GeneralException
     * 
     */
    public SgAfDepreciacionMaestro obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDepreciacionMaestro> codDAO = new CodigueraDAO<>(em, SgAfDepreciacionMaestro.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_DEPRECIACION_BIENES);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDepreciacionMaestro> codDAO = new CodigueraDAO<>(em, SgAfDepreciacionMaestro.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_DEPRECIACION_BIENES);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDepreciacionMaestro> codDAO = new CodigueraDAO<>(em, SgAfDepreciacionMaestro.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Valida que no hayan procesos repetidos con estado PENDIENTE O EN_PROCESO
     * @param entity
     * @return
     * @throws GeneralException 
     */
    public boolean validarExistentes(SgAfDepreciacionMaestro entity) throws GeneralException {
        Boolean resultado = Boolean.FALSE;
            try {
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            filtro.setFuenteId(entity.getDmaFuenteFinanciamientoFk() != null ? entity.getDmaFuenteFinanciamientoFk().getFfiPk() : null);
            filtro.setProyectoId(entity.getDmaProyectoFk() != null ? entity.getDmaProyectoFk().getProPk() : null);
            filtro.setAnio(entity.getDmaAnioProceso() != null ? entity.getDmaAnioProceso().longValue() : null);
            filtro.setMes(entity.getDmaMesProceso() != null ? entity.getDmaMesProceso().longValue() : null);
            filtro.setCodigoInventario(entity.getDmaCodigoInventario());
            
            List<EnumEstadosProceso> estados = new ArrayList();
            estados.add(EnumEstadosProceso.PENDIENTE);
            estados.add(EnumEstadosProceso.EN_PROCESO);
            filtro.setEstados(estados);
            
            Long total = obtenerTotalPorFiltro(filtro);
            //Si es el existente, se permite maximo 1, de lo contrario ninguno existente con el filtro induicado
            if((entity.getDmaPk() != null && total.compareTo(1L) <= 0) || (entity.getDmaPk() == null && total.compareTo(0L) == 0)) {
               resultado = Boolean.TRUE;
            } else {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_TAREA_EXISTE_PENDIENTE_PROCESAR_O_EN_PROCESO);
                throw ge;
            }
            
            filtro = new FiltroBienesDepreciables();
            filtro.setEstados(estados);
            Long totalPendientesFinalizar = obtenerTotalPorFiltro(filtro);
            if(tareasNoProcesadasAdmitidas.compareTo(totalPendientesFinalizar) <= 0) {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_LIMITE_TAREAS_NO_FINALIZADAS);
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return resultado;
    }
  
    /**
     * Procesamiento de las tareas de depreciacion. Esta depreciacion puede ser por un año especifico, para toda la vida util de un bien o hasta que un bien ha sido descargado o 
     * si un proyecto debe liquidarse.
     * @param entity SgAfDepreciacionMaestro
     * @param dataSecurity Si debe validarse si se tiene el permiso para realizar las acciones de guardar o actualizar
     * @param anioEspecifico Si solo debe procesarse un año especifico
     * @param liquidarProyecto Si debe calcularse la depreciacion para liquidar un proyecto
     * @param fechaLiquidacion Fecha de liquidación de un proyecto
     * @param bienEspecifico Si debe procesarse un bien especifico
     * @param bienProcesar Bien especifico a procesar
     * @return true/false
     * @throws GeneralException 
     */
    @Interceptors({AuditInterceptor.class,})
    public Object procesar(SgAfDepreciacionMaestro entity, Boolean dataSecurity, Boolean anioEspecifico, Boolean liquidarProyecto, LocalDate fechaLiquidacion, Boolean bienEspecifico, SgAfBienDepreciable bienProcesar, SgAfFuenteFinanciamiento fuenteDestino, Long primero, Long maxResult) throws GeneralException {
        Boolean precesado = Boolean.FALSE;
        Boolean depreciado = Boolean.FALSE;
        Object objectReturn = new Object();
        try { 
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (DepreciacionMaestroValidacionMaestro.validar(entity,anioLimiteMenorAdmitido, anioEspecifico)) {
                
                Integer mesActual = LocalDate.now().getMonthValue();
                Integer anioActual = LocalDate.now().getYear();
                
                Integer anioLiquidacionProyecto = 0;
                Integer mesLiquidacionProyecto = 0;
                
                if(fechaLiquidacion != null) {
                       anioLiquidacionProyecto = fechaLiquidacion.getYear();
                       mesLiquidacionProyecto = fechaLiquidacion.getMonthValue();
                }
                
                List<SgAfBienDepreciable> listaBienes = new ArrayList();
                
                
                if(bienEspecifico != null && bienEspecifico && bienProcesar != null && bienProcesar.getBdePk() != null) {
                    listaBienes.add(bienProcesar);
                } else {
                    // Se obtienen la lista de bienen que cumplen el criterio de la tarea a procesar. Esto solo para los bienes que son activos fijos
                    FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                    filtro.setIncluirCampos(new String[]{"bdePk","bdeCodigoCorrelativo","bdeNumCorrelativo","bdeCodigoInventario","bdeAsignadoA","bdeDescripcion","bdeObservacion","bdeMarca","bdeModelo","bdeNoSerie",
                                                        "bdeNoPlaca","bdeNoMotor","bdeAnio","bdeNoChasis","bdeNoVin","bdeColorCarroceria","bdeDocumentoAdquisicion","bdeFechaAdquisicion","bdeFechaCreacion","bdeValorAdquisicion",
                                                        "bdeEsValorEstimado","bdeDepreciado","bdeDepreciadoCompleto","bdeTipoBienCategoriaVinculada","bdeProveedor","bdeNumeroPartida","bdeEstadoProcesoLote","bdeEsLote",
                                                        "bdeCantidadLote","bdeFechaDescargo","bdeFechaRegContable","bdeFechaRecepcion","bdeValorResidual","bdeVidaUtil","bdeObservacionDde","bdeInmuebleId","bdeCompletado",
                                                        "bdeEmpleadoFk.empPk","bdeEmpleadoFk.empVersion","bdeEstadoCalidad.ecaPk","bdeEstadoCalidad.ecaVersion","bdeTipoBien.tbiPk","bdeTipoBien.tbiVersion",
                                                        "bdeCategoriaFk.cabPk","bdeCategoriaFk.cabVersion","bdeCategoriaFk.cabValorMaximo","bdeCategoriaFk.cabVidaUtil","bdeCategoriaFk.cabPorcentajeValorResidual","bdeFormaAdquisicion.fadPk","bdeFormaAdquisicion.fadVersion",
                                                        "bdeFuenteFinanciamiento.ffiPk","bdeFuenteFinanciamiento.ffiVersion","bdeProyectos.proPk","bdeProyectos.proVersion","bdeEstadosBienes.ebiPk","bdeEstadosBienes.ebiVersion",
                                                        "bdeEstadosSolicitud.ebiPk","bdeEstadosSolicitud.ebiVersion","bdeSede.sedPk","bdeSede.sedTipo","bdeSede.sedVersion",
                                                        "bdeUnidadesAdministrativas.uadPk","bdeUnidadesAdministrativas.uadVersion","bdeUltModFecha","bdeUltModUsuario","bdeVersion"});
                    filtro.setFirst(primero);
                    filtro.setMaxResults(maxResult);
                    filtro.setOrderBy(new String[]{"bdeNumCorrelativo"});
                    filtro.setAscending(new boolean[]{true});
                    if(entity.getDmaAnioProceso() != null) {
                        filtro.setMenorIgualAnio(Boolean.TRUE); 
                        filtro.setAnio(entity.getDmaAnioProceso().longValue() );
                       
                    }
                    
                    if(entity.getDmaMesProceso() != null) {
                        filtro.setMenorIgualMes(Boolean.TRUE);
                        filtro.setMes(entity.getDmaMesProceso().longValue());
                    }
                   
                    filtro.setFuenteId(entity.getDmaFuenteFinanciamientoFk() != null ? entity.getDmaFuenteFinanciamientoFk().getFfiPk(): null);
                    filtro.setProyectoId(entity.getDmaProyectoFk() != null ? entity.getDmaProyectoFk().getProPk() : null);
                    filtro.setCodigoInventario(entity.getDmaCodigoInventario());
                    filtro.setSecurityOperation(null);
                    
                    if(anioEspecifico != null && anioEspecifico) {//Para obtiene los bienes que no han sido depreciados completamente y que son activos fijos.
                        filtro.setDepreciadoCompleto(Boolean.FALSE);
                        filtro.setActivos(Boolean.TRUE);
                        filtro.setParaDepreciar(Boolean.TRUE);
                    }

                    listaBienes = bienes.obtenerPorFiltro(filtro);
                }
                
                LOGGER.log(Level.INFO, "Se obtuvieron : " + listaBienes.size() + " bienes.");
                
                Integer count = 0;
                if(!listaBienes.isEmpty()) {
                    for(SgAfBienDepreciable bien :listaBienes) {
                        count = count + 1;
                       // LOGGER.log(Level.INFO, "Num item..." + count);
                        BigDecimal valorAdquisicion = bien.getBdeValorAdquisicion();
                        BigDecimal vidaUtil = bien.getBdeVidaUtil() != null ? new BigDecimal(bien.getBdeVidaUtil()) :
                                (new BigDecimal((bien.getBdeCategoriaFk() != null
                                && bien.getBdeCategoriaFk().getCabVidaUtil() != null) ? bien.getBdeCategoriaFk().getCabVidaUtil() : 0));
                        
                        LocalDate fechaDepreciacionLimite = bien.getBdeFechaAdquisicion().plusYears(vidaUtil.intValue());
                        
                        BigDecimal porcentajeValorResidual = new BigDecimal((bien.getBdeCategoriaFk() != null
                                && bien.getBdeCategoriaFk().getCabPorcentajeValorResidual()!= null) ? bien.getBdeCategoriaFk().getCabPorcentajeValorResidual() : 0);
                        
                        BigDecimal valorResidual = bien.getBdeValorAdquisicion().multiply(porcentajeValorResidual).divide(Constantes.ONE_HUNDRED);


                        BigDecimal valorMaximoOperar = valorAdquisicion.subtract(valorResidual);

                        Integer anioAdquisicion = bien.getBdeFechaAdquisicion().getYear();
                        Integer mesAdquisicion = bien.getBdeFechaAdquisicion().getMonth().getValue();
                        Integer diaAdquisicion = bien.getBdeFechaAdquisicion().getDayOfMonth() - BigInteger.ONE.intValue();//Se resta 1 para que tome ese dia de depreciación
                        //Integer diaAdquisicion = bien.getBdeFechaAdquisicion().getDayOfMonth();
                        Integer anioProceso = 0;
                        Integer mesProceso = 0;
                        Integer anioBusqueda = 0;
                        Integer mesBusqueda = 0;
                        if(anioEspecifico != null && anioEspecifico) {
                            anioProceso = entity.getDmaAnioProceso();
                            anioBusqueda = entity.getDmaAnioProceso();
                        } else {
                            anioProceso = anioAdquisicion;
                        }

                        Boolean soloProcesarMes = Boolean.FALSE;
                        if(entity.getDmaMesProceso() != null) {
                            mesProceso = entity.getDmaMesProceso();
                            mesBusqueda = entity.getDmaMesProceso();
                            soloProcesarMes = Boolean.TRUE;
                        } else {
                            if(anioProceso.compareTo(anioAdquisicion) == 1) { // Si el año a procesar es mayor al año de adquisición, entonces se inicia desde enero
                                mesProceso = BigInteger.ONE.intValue();
                            } else {
                                mesProceso = mesAdquisicion;
                            }
                        }


                        Boolean procesar = Boolean.FALSE;

                        Boolean validarFechaDescargo = Boolean.FALSE;
                        Integer anioDescargo = 0;
                        Integer mesDescargo = 0;
                        Integer diaDescargo = 0;
                        
                        // Se valida si el bien se debe procesar
                        if(vidaUtil != null && vidaUtil.compareTo(new BigDecimal(BigInteger.ZERO)) > 0 
                                && valorAdquisicion != null && valorAdquisicion.compareTo(new BigDecimal(BigInteger.ZERO)) > 0
                                && valorResidual.compareTo(new BigDecimal(BigInteger.ZERO)) > 0 && anioProceso != null && anioProceso > 0 ) {

                            if(bien.getBdeFechaDescargo() != null) {
                                validarFechaDescargo = Boolean.TRUE;

                                anioDescargo = bien.getBdeFechaDescargo().getYear();
                                mesDescargo = bien.getBdeFechaDescargo().getMonthValue();
                                diaDescargo = bien.getBdeFechaDescargo().getDayOfMonth();
                            }

                            if(anioProceso.compareTo(anioAdquisicion) >= 0) {
                                if(soloProcesarMes) {
                                    if(mesProceso.compareTo(mesAdquisicion) >= 0 ) {
                                       procesar = Boolean.TRUE;
                                    }
                                } else {
                                    procesar = Boolean.TRUE;
                                }
                            }
                            Long anioAdquisicionVidaUtil = anioAdquisicion.longValue() + vidaUtil.longValue();
                    
                            //Si ya finalizo su vida util al año de proceso, entonces no se procesa, de igual modo, si su fecha de descargo es mejor al año de proceso
                            if((anioAdquisicionVidaUtil.compareTo(anioProceso.longValue()) < 0) || (anioDescargo.compareTo(0) > 0 && anioDescargo.compareTo(anioProceso)< 0)) {
                                procesar = Boolean.FALSE;
                            }
                        }  
                        
                        if(procesar) {
                                Boolean calcular = Boolean.TRUE;
                                Integer diasCalcular = 0;
                                Integer totalDiasMes = 0;
                                BigDecimal restaValorAdquisicon = new BigDecimal(BigInteger.ZERO);
                                BigDecimal depreciacionAnual = valorAdquisicion.subtract(valorResidual).divide(vidaUtil, Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                BigDecimal depreciacionMensual = depreciacionAnual.divide(new BigDecimal(Constantes.MESES_ANIO), Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                BigDecimal depreciacionDiaria = BigDecimal.ZERO;


                                BigDecimal depreciacionMes = BigDecimal.ZERO;

                                BigDecimal depreciacionAcumulada = BigDecimal.ZERO;
                                List<DatoDepreciacion> listaActualizar= new ArrayList();
                                //Si el bien ya se ha depreciado completamente, entonces no se calcula nuevamente su depreciación
                                if(bien.getBdeDepreciadoCompleto() != null && bien.getBdeDepreciadoCompleto()) {
                                    calcular = Boolean.FALSE;
                                } else {
                                    Boolean eliminarTodos = Boolean.FALSE;
                                   // SgAfBienDepreciable bien,Boolean anioEspecifico, Boolean eliminarREgistros, Long anioProceso, Long mesProceso, BigDecimal depreciacionMensual, BigDecimal valorMaximoOperar
                                    //Se obtienen lista de registros de depreciacion ya existentes a la fecha de proceso(Anio,mes)
                                    if(bienEspecifico != null && bienEspecifico)  {
                                        eliminarTodos = Boolean.TRUE;
                                    } 

                                    Map<String, Object> listaRetornar = obtenerDepreciacionAcumuladaYlistaActualizar(bien, anioEspecifico, eliminarTodos, 
                                            anioBusqueda != null? anioBusqueda.longValue() : null, mesBusqueda != null? mesBusqueda.longValue() : null, depreciacionMensual, valorMaximoOperar);

                                    if(listaRetornar != null) {
                                        listaActualizar = (List<DatoDepreciacion>) listaRetornar.get("listaActualizar");
                                        depreciacionAcumulada = (BigDecimal) listaRetornar.get("depreciacionAcumulada");

                                        if(listaActualizar == null) {
                                            throw new GeneralException("Error al obtener la lista de depreciación a actualizar");
                                        }

                                        if(depreciacionAcumulada == null) {
                                            throw new GeneralException("Error al obtener la depreciación acumulada");
                                        }

                                    } else {
                                        throw new GeneralException("No se estan retornado los objetos");
                                    }

                                    //Si la depreciación acumulada a alcanzado el valor máximo (Valor Adquisición - Valor Residual)o si el valor de adquisición es menor al valor limite menor
                                    //entonces la depreciación no debe calcularse
                                    if(depreciacionAcumulada.compareTo(valorMaximoOperar) >= 0  || bien.getBdeValorAdquisicion().compareTo(valorActivoFijoLimite) == -1) {
                                        calcular = Boolean.FALSE;
                                    }
                                   
                                }
                                LOGGER.log(Level.INFO, "calcular : " + calcular);
                                while(calcular) {
                                        //Año de proceso es el mismo que el año de adquisición
                                        if(anioProceso.compareTo(anioAdquisicion) == 0 && mesProceso.compareTo(mesAdquisicion) == 0) { 
                                         
                                            //La depreciación parcial solo debe calcularse al principio del proceso.Por lo demas se toman 30 dias como referencia
                                            YearMonth yearMonth = YearMonth.of(anioAdquisicion.intValue(), mesAdquisicion.intValue());
                                            totalDiasMes = yearMonth.lengthOfMonth();
                                            
                                            //Si ha sido descargado en el mismo mes y anio de adquisicion, entonces se obtienen los dias a calcular la depreciacion(diaDescargo - diaAdquisicion)
                                            if(validarFechaDescargo && anioDescargo.compareTo(anioAdquisicion) == 0 && mesDescargo.compareTo(mesAdquisicion) == 0) {
                                                diasCalcular = diaDescargo - diaAdquisicion;
                                                calcular = Boolean.FALSE;
                                            } else {
                                                //Se obtienen los dias a calcular depreciacion
                                                if(mesAdquisicion.compareTo(Constantes.MES_FEBRERO) == 0) {
                                                    totalDiasMes = yearMonth.lengthOfMonth();
                                                    diasCalcular = totalDiasMes - diaAdquisicion;
                                                } else {
                                                    //Si el dia del mes de referencia(30 dias)
                                                    if(totalDiasMes.compareTo(Constantes.DIAS_REFERENCIA_MES) == 0) {//MES DE 30 DIAS
                                                        //if(diaAdquisicion.compareTo(Constantes.DIAS_REFERENCIA_MES) == 0) {
                                                         //  diasCalcular = BigInteger.ONE.intValue();
                                                        //} else {
                                                            diasCalcular = Constantes.DIAS_REFERENCIA_MES - diaAdquisicion;
                                                        //}
                                                    } if(totalDiasMes.compareTo(Constantes.DIAS_REFERENCIA_MES_31_DIAS) == 0) {//MES 31 DIAS
                                                       // if(diaAdquisicion.compareTo(Constantes.DIAS_REFERENCIA_MES_31_DIAS) == 0) {
                                                       //     diasCalcular = BigInteger.ONE.intValue();
                                                       // } else {
                                                            diasCalcular = Constantes.DIAS_REFERENCIA_MES_31_DIAS - diaAdquisicion;
                                                       // }
                                                    } else {
                                                        diasCalcular = Constantes.DIAS_REFERENCIA_MES - diaAdquisicion;
                                                    } 
                                                }
                                            }
                                            depreciacionDiaria = depreciacionMensual.divide(new BigDecimal(totalDiasMes), Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                            //Se obtiene la depreciacion parcial del primer mes(mes de adquisicion)
                                            depreciacionMes = depreciacionDiaria.multiply(new BigDecimal(diasCalcular)).setScale(Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                        } else {
                                            //Si se ha descargado y el mes y anio de proceso es igual al de descargo, los dias a calcular es el dia de descarg y se caklcula la depreciacion parcial de mes
                                            //De lo contrario se toma la depreciacion mensual
                                            if(validarFechaDescargo && anioDescargo.compareTo(anioProceso) == 0 && mesProceso.compareTo(mesDescargo) == 0) {
                                                /**totalDiasMes = Constantes.DIAS_REFERENCIA_MES;
                                                Integer diasMes=bien.getBdeFechaDescargo().getMonth().getValue();
                                                if(diasMes.compareTo(Constantes.MES_FEBRERO) == 0) {//Solo febrero se toma tal como es, de lo contrario se toman 30 dias
                                                    totalDiasMes = diasMes;
                                                } else {
                                                    totalDiasMes = Constantes.DIAS_REFERENCIA_MES; 
                                                }**/
                                                
                                                YearMonth yearMonth = YearMonth.of(bien.getBdeFechaDescargo().getYear(), bien.getBdeFechaDescargo().getMonth().getValue());
                                                totalDiasMes = yearMonth.lengthOfMonth();
                                                
                                                depreciacionDiaria = depreciacionMensual.divide(new BigDecimal(totalDiasMes), Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                                
                                                diasCalcular = diaDescargo;
                                                depreciacionMes = depreciacionDiaria.multiply(new BigDecimal(diasCalcular)).setScale(Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                                calcular = Boolean.FALSE;
                                                bien.setBdeDepreciadoCompleto(Boolean.TRUE);//Pone como depreciado si ya está descargado
                                                
                                                
                                            } else if(fechaDepreciacionLimite != null && (anioProceso != null && anioProceso > 0) && (mesProceso != null && mesProceso> 0)
                                                    && (anioProceso.compareTo(fechaDepreciacionLimite.getYear()) == 0)  //Si ya se cumplio la fecha de depreciacion, anio y mes, 
                                                    && (mesProceso.compareTo(fechaDepreciacionLimite.getMonthValue()) == 0)){ //se toman los dias, segun dia de adquisicion
                                                /**totalDiasMes = Constantes.DIAS_REFERENCIA_MES;
                                                Integer diasMes=fechaDepreciacionLimite.getMonth().getValue();
                                                if(diasMes.compareTo(Constantes.MES_FEBRERO) == 0) {//Solo febrero se toma tal como es, de lo contrario se toman 30 dias
                                                    totalDiasMes = diasMes;
                                                } else {
                                                    totalDiasMes = Constantes.DIAS_REFERENCIA_MES; 
                                                }**/
                                                YearMonth yearMonth = YearMonth.of(fechaDepreciacionLimite.getYear(), fechaDepreciacionLimite.getMonth().getValue());
                                                totalDiasMes = yearMonth.lengthOfMonth();
                                                
                                                //diaAdquisicion = fechaDepreciacionLimite.getDayOfMonth();
                                                
                                                depreciacionDiaria = depreciacionMensual.divide(new BigDecimal(totalDiasMes), Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                                
                                                diasCalcular = fechaDepreciacionLimite.getDayOfMonth() - 1;
                                                depreciacionMes = depreciacionDiaria.multiply(new BigDecimal(diasCalcular)).setScale(Constantes.DECIMALES_OPERACION, RoundingMode.HALF_UP);
                                                calcular = Boolean.FALSE;
                                                if(depreciacionAcumulada.compareTo(valorMaximoOperar) >= 0) {
                                                    bien.setBdeDepreciadoCompleto(Boolean.TRUE);//Pone como depreciado si ya cumplio el valor maximo de depreciacion
                                                }
                                                
                                            } else {
                                                diasCalcular = Constantes.DIAS_REFERENCIA_MES;
                                                depreciacionMes = depreciacionMensual;
                                            }
                                        }
                                        //Si la depreciacion acumulada no ha superado el valor maximo a operar, entonces se agrega la depreciacion del mes a la depreciacion y la diferencia se guarda en restaValorAdquisicon
                                        //De lo contrario, la depreciacion del mes es la diferencia anterio entre valorMaximoOperar y depreciacionAcumulada
                                        if(valorMaximoOperar.compareTo(depreciacionAcumulada) == 1) { 
                                            depreciacionAcumulada = depreciacionAcumulada.add(depreciacionMes);
                                            restaValorAdquisicon = valorMaximoOperar.subtract(depreciacionAcumulada);
                                        } else {
                                            depreciacionMes = restaValorAdquisicon;
                                            calcular = Boolean.FALSE;
                                        }

                                        //si la depreciacion acumulada ha alcanzado el valor maximo a operar, entonces el proceso debe detenerse en esta iteracion
                                        if(depreciacionAcumulada.compareTo(valorMaximoOperar) >= 0) {
                                            calcular = Boolean.FALSE;
                                            bien.setBdeDepreciadoCompleto(Boolean.TRUE);
                                        }
                                        //Se obtiene el registro de depreciacion del mes(si ya existia uno en base de datos)
                                        SgAfDepreciacion dep = obtenerExistente(listaActualizar, anioProceso, mesProceso);
                                        if(dep == null) {
                                            dep = new SgAfDepreciacion();
                                            dep.setDepVersion(0);
                                        }
                                        
                                        if(dep.getDepPk() == null) {
                                            dep.setDepMontoDepreciacion(depreciacionMes);
                                            dep.setDepUltimoMontoDepreciacion(depreciacionMes);
                                            if(bienEspecifico != null && bienEspecifico) {
                                               dep.setDepOperacion(EnumOperaciones.UPDATE.getText());
                                                dep.setDepProcesado(Boolean.TRUE);  
                                            } else {
                                                dep.setDepOperacion(EnumOperaciones.INSERT.getText());
                                                dep.setDepProcesado(Boolean.TRUE); 
                                            }
                                        } else {
                                            if(dep.getDepMontoDepreciacion().compareTo(depreciacionMes) != 0) {
                                                dep.setDepUltimoMontoDepreciacion(depreciacionMes);
                                                dep.setDepOperacion(EnumOperaciones.UPDATE.getText());
                                                dep.setDepProcesado(Boolean.FALSE);
                                            }
                                        }
                                       
                                        
                                        dep.setDepAnio(anioProceso);//Año
                                        dep.setDepMes(mesProceso);//Mes;
                                        dep.setDepNoDiasDepreciados(diasCalcular);

                                        dep.setDepBienesDepreciablesFk(bien);//Bien
                                        dep.setDepFechaCalculo(LocalDateTime.now());//Fecha de Calculo
                                        dep.setDepFuenteFinanciamientoFk(bien.getBdeFuenteFinanciamiento());//Fuente de financiamiento
                                        dep.setDepProyectoFk(bien.getBdeProyectos());//Proyecto  
                                        
                                        dep = depreciacionBean.guardar(dep, dataSecurity);
                                        
                                        //Si ya guardó registro de depreciación, inició su depreciación
                                        depreciado = Boolean.TRUE;
                                        bien.setBdeDepreciado(depreciado);
                                        if(anioEspecifico != null && anioEspecifico) {
                                            //Si el mes es menor a Diciembre(mes 12), y es el año solicitado por procesar, se suma 1, de lo contrario es la ultima iteracion
                                            if(mesProceso.compareTo(Constantes.MESES_ANIO) == -1 && !soloProcesarMes) {
                                                mesProceso = mesProceso + BigDecimal.ONE.intValue();
                                                if(mesProceso.compareTo(mesActual) == 1 && anioProceso.compareTo(anioActual) == 0) {
                                                    calcular = Boolean.FALSE;
                                                }
                                            } else {
                                                calcular = Boolean.FALSE;
                                            }
                                        } else {
                                            if(mesProceso.compareTo(Constantes.MESES_ANIO) == -1) {
                                                 mesProceso = mesProceso + BigDecimal.ONE.intValue();
                                                if( (mesProceso.compareTo(mesActual) == 1 && anioProceso.compareTo(anioActual) == 0) || (mesProceso.compareTo(mesLiquidacionProyecto) == 1 && anioProceso.compareTo(anioLiquidacionProyecto) == 0)) {
                                                    calcular = Boolean.FALSE;
                                                }
                                            } else {
                                                mesProceso = BigInteger.ONE.intValue();
                                                anioProceso = anioProceso + 1;
                                            }
                                        }
                                        
                                }
                                
                                //Si ya tiene calculo de depreciación parcial o completa o se debe liquidar un proyecto.
                                if((depreciado != null && depreciado) || (liquidarProyecto != null && liquidarProyecto)
                                        || (bien.getBdeDepreciadoCompleto() != null && bien.getBdeDepreciadoCompleto())) {
                                    if (liquidarProyecto != null && liquidarProyecto) {
                                        
                                        //LOGGER.info("fuenteDestino: " + fuenteDestino.getFfiNombre());
                                        bien.setBdeFuenteFinanciamiento(fuenteDestino);
                                        bien.setBdeProyectos(entity.getDmaProyectoFk());
                                        //Si el bien es activo fijo, y se ha depreciado
                                        if(bien.getBdeValorAdquisicion() != null && bien.getBdeValorAdquisicion().compareTo(valorActivoFijoLimite)>=0 && (depreciado != null && depreciado)){
                                            bien.setBdeDepreciado(Boolean.TRUE);
                                        }
                                    } else {
                                        bien.setBdeDepreciado(Boolean.TRUE);
                                    }
                                    
                                    bien.setBdeValorResidual(valorResidual);
                                    
                                    bien = bienes.guardarNormal(bien, dataSecurity, Boolean.FALSE, Boolean.TRUE);
                                    
                                    if(bienEspecifico != null && bienEspecifico && bienProcesar != null) {
                                        objectReturn = bien;
                                    }
                                }
                        }
                    }
                }
                
                precesado = Boolean.TRUE;
                
                if(bienEspecifico == null || !bienEspecifico) {
                    objectReturn = precesado;
                }
                
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return objectReturn;
    }
    /**
     * Obtiene un map con la lista de registros de depreciación a darles tratamiento y su depreciación acumulada. Si la bandera bienEspecifico, entonces deben eliminarse todos los registros de depreciacion existentes.
     * @param bien Bien del cual se estraen los resgitrso de depreciación existentes
     * @param anioEspecifico Si es un anio especifico el que debe procesarse
     * @param eliminarRegistros si los registros obtenidos deben eliminarse
     * @param anioProceso Año del proceso
     * @param mesProceso Mes del proceso
     * @param depreciacionMensual Depreciación mensual
     * @param valorMaximoOperar Valor maximo a operar
     * @return Map de objetos
     */
    public Map<String, Object> obtenerDepreciacionAcumuladaYlistaActualizar(SgAfBienDepreciable bien,Boolean anioEspecifico, Boolean eliminarRegistros, Long anioProceso, Long mesProceso, BigDecimal depreciacionMensual, BigDecimal valorMaximoOperar) {
        Map<String, Object> listaRetornar = new HashMap();
        
        BigDecimal depreciacionAcumulada = BigDecimal.ZERO;
        //Se obtienen lista de registros de depreciacion ya existentes a la fecha de proceso(Anio,mes)
        FiltroBienesDepreciables filtroDep = new FiltroBienesDepreciables();
        filtroDep.setIdBien(bien.getBdePk());
        filtroDep.setCompletado(Boolean.TRUE);
        if(anioEspecifico != null && anioEspecifico) {
            if(anioProceso != null && anioProceso > 0) {
                filtroDep.setAnio(anioProceso);
                filtroDep.setMenorIgualAnio(Boolean.TRUE);
            }
            if(mesProceso != null && mesProceso > 0) {
                filtroDep.setMes(mesProceso);
                filtroDep.setMenorIgualMes(Boolean.TRUE);
            }
        }

        filtroDep.setOrderBy(new String[] {"depAnio", "depMes"});
        filtroDep.setAscending(new boolean[] {Boolean.TRUE});
        //SI los registros solo se eliminaran, entonces solo se obtiene su pk y su versión
        if(eliminarRegistros != null && eliminarRegistros) {
            filtroDep.setIncluirCampos(new String[]{"depPk","depVersion"});
        } else {
            filtroDep.setIncluirCampos(new String[]{"depPk","depVersion","depAnio","depMes","depMontoDepreciacion","depUltimoMontoDepreciacion","depOperacion","depProcesado",
                    "depFuenteFinanciamientoFk.ffiPk","depFuenteFinanciamientoFk.ffiVersion",
                    "depProyectoFk.proPk","depProyectoFk.proVersion",
                    "depBienesDepreciablesFk.bdePk","depBienesDepreciablesFk.bdeVersion","depBienesDepreciablesFk.bdeFechaAdquisicion","depBienesDepreciablesFk.bdeValorAdquisicion"});
        }
        filtroDep.setSecurityOperation(null);
        List<SgAfDepreciacion> lista = depreciacionBean.obtenerPorFiltro(filtroDep);
        
        List<DatoDepreciacion> listaActualizar= new ArrayList();
        List<Long> listaIdsEliminar = new ArrayList();
        if(lista != null && !lista.isEmpty()) {
            for(SgAfDepreciacion depHistorico : lista) {
                Boolean eliminar = Boolean.FALSE;
                if(eliminarRegistros !=null && eliminarRegistros) {
                    eliminar = Boolean.TRUE;
                } else {
                    //Si es un año menor o igual al año actual de adquisicoon y hay registros menores al mes de adquisicion, entonces debe eliminarse
                    if((depHistorico.getDepAnio().compareTo(bien.getBdeFechaAdquisicion().getYear()) == -1 )
                            || (depHistorico.getDepAnio().compareTo(bien.getBdeFechaAdquisicion().getYear()) == 0 
                            && depHistorico.getDepMes().compareTo(bien.getBdeFechaAdquisicion().getMonthValue()) == -1)) {
                        eliminar = Boolean.TRUE;
                    //Si la depreciacion acumulada ya llegó a su valor maximo, entonces el registro debe eliminarse    
                    } else if(depreciacionAcumulada.compareTo(valorMaximoOperar) >= 0) {
                        eliminar = Boolean.TRUE;
                    } else {
                        //Los menores o iguales al año o mes en proceso los agrego a la lista a actualizar, ya que puede darse el caso que anteriormente haya sido una depreciación parcial
                        if(anioEspecifico != null && anioEspecifico && anioProceso != null) {
                            if(depHistorico.getDepAnio().equals(anioProceso.intValue())) {
                                 if(mesProceso != null && mesProceso > 0) {
                                     if(depHistorico.getDepMes() <= mesProceso.intValue()) {
                                         listaActualizar.add(new DatoDepreciacion(depHistorico.getDepAnio(), depHistorico.getDepMes(), depHistorico));
                                     }
                                 } else {
                                     listaActualizar.add(new DatoDepreciacion(depHistorico.getDepAnio(), depHistorico.getDepMes(), depHistorico));
                                 }
                             //Si la depreciacion mensual no ha cambiado, entonces se agrega ala depreciacion acumulada    
                             } else if(depHistorico.getDepAnio() < anioProceso.intValue() && (mesProceso == null || (depHistorico.getDepMes() < mesProceso && mesProceso > 0)
                                     && depHistorico.getDepMontoDepreciacion().compareTo(depreciacionMensual) == 0)) {
                                 //La depreciación que se toma en cuenta es la anterior al proceso
                                 depreciacionAcumulada = depreciacionAcumulada.add(depHistorico.getDepMontoDepreciacion()); 
                             }
                        } else {
                            listaActualizar.add(new DatoDepreciacion(depHistorico.getDepAnio(), depHistorico.getDepMes(), depHistorico));
                            
                            if(depHistorico.getDepMontoDepreciacion().compareTo(depreciacionMensual) == 0) {
                                depreciacionAcumulada = depreciacionAcumulada.add(depHistorico.getDepMontoDepreciacion()); 
                            }
                        }
                    }
                }
                if(eliminarRegistros != null && eliminarRegistros) {
                     listaIdsEliminar.add(depHistorico.getDepPk()); 
                } else if(eliminar) {
                    depHistorico.setDepProcesado(Boolean.FALSE);
                    depHistorico.setDepOperacion(EnumOperaciones.DELETE.getText());
                    depreciacionBean.guardar(depHistorico, null);
                }
            } 
            //Esto solo es para cuando es un bien especifico, en este caso las lineas de depreciación se eliminan de una vez
            if(listaIdsEliminar != null && !listaIdsEliminar.isEmpty() && eliminarRegistros != null && eliminarRegistros) {
                depreciacionBean.eliminarLista(listaIdsEliminar);
            }
        }
        listaRetornar.put("depreciacionAcumulada", depreciacionAcumulada);
        listaRetornar.put("listaActualizar", listaActualizar);

        return listaRetornar;
    }
    
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAfDepreciacionMaestro
     * @return SgAfDepreciacionMaestro
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfDepreciacionMaestro guardar(SgAfDepreciacionMaestro entity, Boolean dataSecurity, Boolean anioEspecifico, Boolean validarExistente) throws GeneralException {
        try {
            Boolean guardar = Boolean.FALSE;
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DepreciacionMaestroValidacionMaestro.validar(entity, anioLimiteMenorAdmitido, anioEspecifico)) {
                    if(validarExistente != null && validarExistente) {
                        guardar = validarExistentes(entity);
                    } else {
                        guardar = Boolean.TRUE;
                    }
                    if(guardar) {
                        CodigueraDAO<SgAfDepreciacionMaestro> codDAO = new CodigueraDAO<>(em, SgAfDepreciacionMaestro.class);
                        if (BooleanUtils.isTrue(dataSecurity)){
                            entity = codDAO.guardar(entity, entity.getDmaPk() != null ? ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO : ConstantesOperaciones.CREAR_DEPRECIACION_MAESTRO);
                        } else {
                            entity = codDAO.guardar(entity, null);
                        }
                    }
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            DepreciacionMaestroDAO codDAO = new DepreciacionMaestroDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfDepreciacionMaestro>
     * @throws GeneralException
     */
    public List<SgAfDepreciacionMaestro> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            DepreciacionMaestroDAO codDAO = new DepreciacionMaestroDAO(em);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

   /**
    * Extrae de una lista un registro de depreciacion a partir del año y mes
    * @param lista
    * @param anio
    * @param mes
    * @return 
    */
    public SgAfDepreciacion obtenerExistente(List<DatoDepreciacion> lista, Integer anio, Integer mes) {
        if(lista != null && !lista.isEmpty()) {
            for(DatoDepreciacion dato : lista) {
                if(dato.getAnio() != null && dato.getMes() != null && dato.getAnio().compareTo(anio) == 0 && dato.getMes().compareTo(mes) == 0) {
                    return dato.getDepreciacion();
                }
            }
        }
        return null;
    }
}