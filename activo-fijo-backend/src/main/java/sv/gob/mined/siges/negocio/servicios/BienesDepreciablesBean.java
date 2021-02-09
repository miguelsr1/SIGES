/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.CantidadesDTO;
import sv.gob.mined.siges.dto.CodigosDTO;
import sv.gob.mined.siges.dto.SgAfBienDepreciableInmDTO;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.enumerados.TipoUnidad;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.filtros.FiltroEmpleados;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.negocio.validaciones.BienDepreciableValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.BienesDepreciablesDAO;
import sv.gob.mined.siges.persistencia.daos.BienesDepreciablesLiteDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciableLite;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgPersona;

@Stateless
@Traced
public class BienesDepreciablesBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    @ConfigProperty(name = "valor-activo-fijo-limite", defaultValue = "600")
    private BigDecimal valorActivoFijoLimite;
    
    @Inject
    @ConfigProperty(name = "anio-limite-menor-admitido", defaultValue = "1960")
    private Integer anioLimiteMenorAdmitido;
    
    @Inject
    @ConfigProperty(name = "centro-educativo.codigo-correlativo.size", defaultValue = "4")
    private Integer codigoCorrelativoCentroEducativoSize;
    
    @Inject
    @ConfigProperty(name = "unidad-administrativa.codigo-correlativo.size", defaultValue = "3")
    private Integer codigoCorrelativoUnidadAdministrativaSize;
    
    @Inject
    private DescargosDetalleBean descargosDetalleBean;
    
    @Inject
    private TrasladosDetalleBean trasladosDetalleBean;
    
    @Inject
    private DepreciacionBean depreciacionBean;
    
    @Inject
    private DepreciacionMaestroBean depreciacionMaestroBean;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private EmpleadosBean empleadosBean;
    
    @Inject
    private LoteBienesBean loteBienesBean;

    @Inject
    private PersonaBean personaBean;
    
    private static final Logger LOGGER = Logger.getLogger(BienesDepreciablesBean.class.getName());
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfBienDepreciable
     * @throws GeneralException
     * 
     */
    public SgAfBienDepreciable obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                SgAfBienDepreciable bien = null;
                CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    bien = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_BIENES_DEPRECIABLES);
                } else {
                    bien = codDAO.obtenerPorId(id, null);
                }
                if(bien != null && bien.getBdeTipoBien() != null && bien.getBdeTipoBien().getTbiCategoriaBienes() != null && bien.getBdeTipoBien().getTbiCategoriaBienes().getSgAfSeccionesCategoriaList() != null) {
                    bien.getBdeTipoBien().getTbiCategoriaBienes().getSgAfSeccionesCategoriaList().size();
                }
                return bien;
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
                CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_BIENES_DEPRECIABLES);
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
     * Guarda el objeto indicado
     *
     * @param entity SgAfBienDepreciable
     * @return SgAfBienDepreciable
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfBienDepreciable guardar(SgAfBienDepreciable entity, Boolean dataSecurity, Boolean crear) throws GeneralException {
        Integer correlativo = 0;
        try {
            if (entity != null) {
                if(BienDepreciableValidacionNegocio.validarCategoria(entity)) {
                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                    if (BienDepreciableValidacionNegocio.validar(entity, anioLimiteMenorAdmitido, Boolean.TRUE)) {
                        CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);

                        if(StringUtils.isNotBlank(entity.getBdeCodigoCorrelativo())) {
                            correlativo = Integer.parseInt(entity.getBdeCodigoCorrelativo());
                            
                            if(((crear != null && crear) || (entity.getBdeEsLoteSiap() != null && entity.getBdeEsLoteSiap())) && entity.getBdeEsLote() != null && entity.getBdeEsLote() && entity.getBdeCantidadLote() != null && entity.getBdeCantidadLote() > 0) {
                                correlativo = correlativo + (entity.getBdeCantidadLote() - 1);
                                crear = Boolean.TRUE;
                            }
                        }
                        entity = generarCodigoInventario(entity, correlativo);
                        
                        //Si no se envía la categoría, entonces se toma la categoría del tipo de bien
                        entity.setBdeCategoriaFk(entity.getBdeCategoriaFk() != null ? entity.getBdeCategoriaFk() : entity.getBdeTipoBien().getTbiCategoriaBienes());
                        
                        
                        entity.setBdeCompletado(Boolean.TRUE);
                        entity.setBdeBienEsFuenteSiap(entity.getBdeBienEsFuenteSiap() != null ? entity.getBdeBienEsFuenteSiap() : Boolean.FALSE);
                        
                        SgAfEmpleados empleado = entity.getBdeEmpleadoFk();
                        if(empleado != null) {
                            empleado.setEmpUnidadAdministrativaFk(entity.getBdeUnidadesAdministrativas());
                            
                            FiltroEmpleados femp = new FiltroEmpleados();
                            femp.setMaxResults(1L);
                            femp.setPerPk(empleado.getEmpPersonaFk() != null ? empleado.getEmpPersonaFk().getPerPk() : null);
                            femp.setDui(empleado.getEmpPersonaFk().getPerDui());
                            femp.setUnidadAdministrativaId(entity.getBdeUnidadesAdministrativas().getUadPk());
                            femp.setIncluirCampos(new String[]{"empPk","empVersion"});

                            //Primero buscamos en los empleados de la unidad, de lo contrario lo obtenemos de las personas
                            List<SgAfEmpleados> empleados = empleadosBean.obtenerPorFiltro(femp);
                            if(empleados != null && !empleados.isEmpty() && empleados.size() > 0) {
                                empleado = empleados.get(0);
                            } else {
                                FiltroPersona fper = new FiltroPersona();
                                fper.setMaxResults(1L);
                                fper.setPerPk(empleado.getEmpPersonaFk().getPerPk() != null ? empleado.getEmpPersonaFk().getPerPk() : null);
                                fper.setDui(empleado.getEmpPersonaFk().getPerDui());
                                fper.setIncluirCampos(new String[]{"perPk","perVersion","perPrimerNombre","perSegundoNombre","perTercerNombre","perPrimerApellido",
                                                            "perSegundoApellido","perTercerApellido","perDui"});
                        
                                List<SgPersona> personas = personaBean.obtenerPorFiltro(fper);
                                if(personas != null && !personas.isEmpty() && personas.size() > 0) {
                                    empleado.setEmpPersonaFk(personas.get(0));
                                    empleado.setEmpUnidadAdministrativaFk(entity.getBdeUnidadesAdministrativas());
                                    empleado = empleadosBean.guardar(empleado);
                                }
                                
                            }
                        }
                        
                        entity.setBdeEmpleadoFk(empleado);
                        
                        Object valorAnt = ch.obtenerEnVersion(entity.getClass(), entity.getBdePk() , entity.getBdeVersion());
                        SgAfBienDepreciable valorAnterior = (SgAfBienDepreciable) valorAnt;

                        //Si ya ha sido depreciado y se ha modificado el valor o la fecha de adquisicion, entonces se debe recalcular su depreciacion
                        if(entity.getBdeDepreciado() != null && entity.getBdeDepreciado()
                                && (entity.getBdeFechaAdquisicion() != null && valorAnterior.getBdeFechaAdquisicion() != null && (!entity.getBdeFechaAdquisicion().equals(valorAnterior.getBdeFechaAdquisicion())
                                    || entity.getBdeValorAdquisicion().compareTo(valorAnterior.getBdeValorAdquisicion()) != 0))) {
                            SgAfDepreciacionMaestro maestro = new SgAfDepreciacionMaestro();
                            maestro.setDmaCodigoInventario(entity.getBdeCodigoInventario());

                            Object object = depreciacionMaestroBean.procesar(maestro, dataSecurity,null, null, null, Boolean.TRUE, entity, null,0L,1L);
                            if(object instanceof SgAfBienDepreciable) {
                                entity = (SgAfBienDepreciable) object;
                            }
                        } else {
                            if (BooleanUtils.isTrue(dataSecurity)){
                                entity = codDAO.guardar(entity, entity.getBdePk()== null ? ConstantesOperaciones.CREAR_BIEN_DEPRECIABLE : ConstantesOperaciones.ACTUALIZAR_BIEN_DEPRECIABLE);
                            } else {
                                entity = codDAO.guardar(entity, null);
                            }

                            if(crear != null && crear && entity.getBdeEsLote() != null && entity.getBdeEsLote()) {
                                SgAfLoteBienes lote = new SgAfLoteBienes();
                                lote.setLbiCodigoInventarioPadre(entity.getBdeCodigoInventario());
                                lote.setLbiEstado(EnumEstadosProceso.PENDIENTE);
                                lote.setLbiSede(entity.getBdeSede());
                                lote.setLbiUnidadesAdministrativas(entity.getBdeUnidadesAdministrativas());
                                lote.setLbiCantidadBienesReplicar(entity.getBdeCantidadLote() - 1);
                                lote.setLbiVersion(0);
                                lote = loteBienesBean.guardar(lote); //Lote se ejecutará después.
                            }
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
    
    public SgAfBienDepreciableInmDTO guardarBienDepreciableDeInfraestructura(SgAfBienDepreciableInmDTO dto) {
        Integer correlativo = 0;
        SgAfBienDepreciable bien = null;
        Boolean validarRangoFecha = Boolean.FALSE;
        try {
            if (BienDepreciableValidacionNegocio.validar(dto,anioLimiteMenorAdmitido)) {
                FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                filtro.setIdInmueble(dto.getBdeInmueblePk() != null ? dto.getBdeInmueblePk() : dto.getBdeEdificioPk());

                List<SgAfBienDepreciable> bienes = obtenerPorFiltro(filtro);
                if(bienes != null && !bienes.isEmpty() && bienes.size() > 0) {
                    bien = bienes.get(0);
                } else {
                    bien = new SgAfBienDepreciable();
                    bien.setBdeFechaCreacion(LocalDateTime.now());
                    bien.setBdeCompletado(Boolean.FALSE);
                    bien.setBdeTipoBienCategoriaVinculada(Boolean.TRUE);
                }
                //Si la fecha de adquisicion es nula, entonces se pone como fecha por defecto 0001/01/01
                if(dto.getBdeFechaAdquisicion() == null) {
                    bien.setBdeFechaAdquisicion(LocalDate.of(0001, 1, 1));
                } else {
                    bien.setBdeFechaAdquisicion(dto.getBdeFechaAdquisicion());
                    validarRangoFecha = Boolean.TRUE;
                }
                bien.setBdeDepreciado(dto.getBdeDepreciado());
                bien.setBdeDescripcion(dto.getBdeDescripcion());
                bien.setBdeEsValorEstimado(dto.getBdeEsValorEstimado());
                bien.setBdeSede(dto.getBdeSede());
                bien.setBdeUnidadesAdministrativas(dto.getBdeUnidadesAdministrativas());
                bien.setBdeValorAdquisicion(dto.getBdeValorAdquisicion());
                bien.setBdeTipoBien(dto.getBdeTipoBien());
                bien.setBdeEstadoCalidad(dto.getBdeEstadoCalidad());
                bien.setBdeEstadosBienes(dto.getBdeEstadosBienes());            
                bien.setBdeInmuebleId(dto.getBdeInmueblePk() != null ? dto.getBdeInmueblePk() : dto.getBdeEdificioPk());
                
                //Si la categoría obtenida del bien encontrado es nula, entonces se toma la categoría del tipo de bien
                bien.setBdeCategoriaFk(bien.getBdeCategoriaFk() != null ? bien.getBdeCategoriaFk() : bien.getBdeTipoBien().getTbiCategoriaBienes());

                bien.setBdeCategoriaFk(dto.getBdeTipoBien().getTbiCategoriaBienes());
                
                if(bien.getBdePk() == null || StringUtils.isBlank(bien.getBdeCodigoInventario())) {
                    filtro = new FiltroBienesDepreciables();
                    if(bien.getBdeUnidadesAdministrativas() != null) {
                        filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                        filtro.setUnidadAdministrativaId(bien.getBdeUnidadesAdministrativas().getUadPk());
                    } else if(bien.getBdeSede() != null) {
                        filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                        filtro.setUnidadAdministrativaId(bien.getBdeSede().getSedPk());
                    }
                    filtro.setTipoBienId(bien.getBdeTipoBien().getTbiPk());

                    correlativo = obtenerUltimoCorrelativo(filtro, Boolean.TRUE);
                } else {
                    correlativo = Integer.parseInt(bien.getBdeCodigoCorrelativo());
                }
                bien = generarCodigoInventario(bien, correlativo);//Genera el formato del codigo de inventario
                bien = guardarDesdeInfraestructura(bien, validarRangoFecha, Boolean.TRUE);
                dto.setBdePk(bien.getBdePk());
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return dto;
   } 
    /**
     * Se obtiene el último correlativo
     * @param filtro Filtro de bienes
     * @param sumarUno Si true suma 1
     * @return último correlativo
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Integer obtenerUltimoCorrelativo(FiltroBienesDepreciables filtro, Boolean sumarUno) {
        Integer result = 0;
        try {
            filtro.setIncluirCampos(new String[]{"bdeNumCorrelativo"});
            filtro.setValidarEstadoSolicitudVacio(Boolean.TRUE);
            filtro.setMaxResults(1L);
            filtro.setOrderBy(new String[]{"bdeNumCorrelativo"});
            filtro.setAscending(new boolean[]{false});
            filtro.setSecurityOperation(null);
            
            BienesDepreciablesLiteDAO DAO = new BienesDepreciablesLiteDAO(em);
            List<SgAfBienDepreciableLite> ultimoBien = DAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            if(ultimoBien != null && !ultimoBien.isEmpty()) {
                if(ultimoBien.get(0).getBdeNumCorrelativo() != null) {
                    result = ultimoBien.get(0).getBdeNumCorrelativo();
                } 
            }
              
            if(sumarUno != null && sumarUno) {
                result = result + 1;
            }
            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return result;
    }
    /**
     * Genera el código de inventario cn el número de  caracteres necesarios
     * @param entity
     * @param correlativo
     * @return Bien con el códgo de inventario generado
     */
    public SgAfBienDepreciable generarCodigoInventario(SgAfBienDepreciable entity, Integer correlativo) {
        String codigoCorrelativo = StringUtils.EMPTY;
        Integer codigoSize = 0;
        String codigoCorrelativoMaxVal = StringUtils.EMPTY;
        String codigoInventarioBase = StringUtils.EMPTY;

        if(entity.getBdeEstadosSolicitud() == null) {
            if(entity.getBdeUnidadesAdministrativas() != null) {
                codigoInventarioBase = entity.getBdeUnidadesAdministrativas().getUadCodigo() + "-" + entity.getBdeTipoBien().getTbiCodigo();
                codigoSize = codigoCorrelativoUnidadAdministrativaSize;
            } else if(entity.getBdeSede() != null) {
                codigoInventarioBase = entity.getBdeSede().getSedCodigo() + "-" + entity.getBdeTipoBien().getTbiCodigo();
                codigoSize = codigoCorrelativoCentroEducativoSize;
            }
            if(correlativo != null) {
                if(correlativo.compareTo(Integer.valueOf(StringUtils.leftPad("", codigoSize, "9"))) > 0) {
                    codigoCorrelativoMaxVal = StringUtils.leftPad("", correlativo.toString().length(), "9");
                } else {
                    codigoCorrelativoMaxVal = StringUtils.leftPad("", codigoSize, "9");
                }

                codigoCorrelativo = StringUtils.leftPad(correlativo.toString(), codigoCorrelativoMaxVal.length(), "0");

                codigoCorrelativo = StringUtils.leftPad(String.valueOf(correlativo), codigoCorrelativoMaxVal.length(), "0");
                entity.setBdeCodigoCorrelativo(codigoCorrelativo);
                entity.setBdeCodigoInventario(codigoInventarioBase + "-" + codigoCorrelativo);
                entity.setBdeNumCorrelativo(correlativo);
            }
        }
        return entity;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAfBienDepreciable
     * @return SgAfBienDepreciable
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public SgAfBienDepreciable guardarNormal(SgAfBienDepreciable entity, Boolean dataSecurity, Boolean validarDatosVehiculo, Boolean validarObligatoriosBase) throws GeneralException {
        try {
            Boolean guardar = Boolean.FALSE;
            if (entity != null) {
                //Validar el elemento a guardar. Estos son los elementos obligatorios en BD
                if(validarObligatoriosBase != null && validarObligatoriosBase) {
                    guardar = BienDepreciableValidacionNegocio.validarObligatoriosBase(entity);
                } else {
                    //Valida todo
                    guardar = BienDepreciableValidacionNegocio.validar(entity, anioLimiteMenorAdmitido, validarDatosVehiculo);
                }
                if (guardar) {                   
                    CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);
                    if (BooleanUtils.isTrue(dataSecurity)){
                        entity = codDAO.guardar(entity, entity.getBdePk()== null ? ConstantesOperaciones.CREAR_BIEN_DEPRECIABLE : ConstantesOperaciones.ACTUALIZAR_BIEN_DEPRECIABLE);
                    } else {
                        entity = codDAO.guardar(entity, null);
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
     * Guarda el objeto indicado
     *
     * @param entity SgAfBienDepreciable
     * @return SgAfBienDepreciable
     * @throws GeneralException
     */
    //@Interceptors({AuditInterceptor.class})
    public void guardarLite(List<Long> ids, SgAfBienDepreciable entity, Boolean traslado, Boolean trasladado) throws GeneralException {
        try {
            if(traslado != null && traslado) {
                if(trasladado != null && trasladado) {
                    em.createQuery("update SgAfBienDepreciable b SET b.bdeEstadosBienes = :estado,  "
                            + "b.bdeEstadosSolicitud= :estadoSolicitud, "
                            + "b.bdeUnidadesAdministrativas = :unidadPk, "
                            + "b.bdeSede = :sedePk, "
                            + "b.bdeEmpleadoFk = :empleadoPk, "
                            + "b.bdeAsignadoA = :asignadoA "
                            + "where b.bdePk IN :listIds")
                    .setParameter("estado", entity.getBdeEstadosBienes())
                    .setParameter("estadoSolicitud", entity.getBdeEstadosSolicitud())
                    .setParameter("unidadPk", entity.getBdeUnidadesAdministrativas())
                    .setParameter("sedePk", entity.getBdeSede())
                    .setParameter("empleadoPk", entity.getBdeEmpleadoFk())
                    .setParameter("asignadoA", entity.getBdeAsignadoA() != null ? entity.getBdeAsignadoA().trim() : "")
                    .setParameter("listIds", ids)
                    .executeUpdate();
                } else {
                   em.createQuery("update SgAfBienDepreciable b SET b.bdeEstadosBienes = :estado,  b.bdeEstadosSolicitud= :estadoSolicitud, b.bdeUnidadesAdministrativas = :unidadPk, b.bdeSede = :sedePk where b.bdePk IN :listIds")
                    .setParameter("estado", entity.getBdeEstadosBienes())
                    .setParameter("estadoSolicitud", entity.getBdeEstadosSolicitud())
                    .setParameter("unidadPk", entity.getBdeUnidadesAdministrativas())
                    .setParameter("sedePk", entity.getBdeSede())
                    .setParameter("listIds", ids)
                    .executeUpdate(); 
                }
            } else {
               em.createQuery("update SgAfBienDepreciable b SET b.bdeEstadosBienes = :estado, b.bdeFechaDescargo = :fechaDescargo, b.bdeEstadosSolicitud = :estadoSolicitud,b.bdeEstadoCalidad = :estadoCalidad where b.bdePk in :listIds")
                    .setParameter("estado", entity.getBdeEstadosBienes())
                    .setParameter("fechaDescargo", entity.getBdeFechaDescargo())
                    .setParameter("estadoSolicitud", entity.getBdeEstadosSolicitud())
                    .setParameter("estadoCalidad", entity.getBdeEstadoCalidad())
                    .setParameter("listIds", ids)
                    .executeUpdate();
            }  
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAfBienDepreciable
     * @return SgAfBienDepreciable
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfBienDepreciable guardarDesdeInfraestructura(SgAfBienDepreciable entity, Boolean validarRangoFecha, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (BienDepreciableValidacionNegocio.validarDesdeInfraestructura(entity, validarRangoFecha, anioLimiteMenorAdmitido)) {
                    CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);
                    entity = codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    
    public CodigosDTO obtenerRangoCodigosPorLote(Long loteId) {
        CodigosDTO codigos = new CodigosDTO();
        try {
            if(loteId != null && loteId > 0) {
                Session session = em.unwrap(Session.class);
                String consulta = "SELECT MIN(bde_codigo_inventario) as primerCod , MAX(bde_codigo_inventario) as ultimoCod,MAX(bde_num_correlativo) as ultimoCorrelativo FROM " +  Constantes.SCHEMA_ACTIVO_FIJO + ".sg_af_bienes_depreciables where bde_lote_id= :loteId";
                SQLQuery query = session.createSQLQuery(consulta);
                query.setParameter("loteId", loteId);
                query.addScalar("primerCod", new StringType());
                query.addScalar("ultimoCod", new StringType());
                query.addScalar("ultimoCorrelativo", new IntegerType());  
                List<CodigosDTO> lista = query.setResultTransformer(Transformers.aliasToBean(CodigosDTO.class)).list();
                if(lista != null && !lista.isEmpty()) {
                    codigos = lista.get(0);
                }        
                        
            }
            
            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return codigos;
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
            BienesDepreciablesDAO codDAO = new BienesDepreciablesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, valorActivoFijoLimite, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfBienesDepreciables>
     * @throws GeneralException
     */
    public List<SgAfBienDepreciable> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        List<SgAfBienDepreciable> listaRetornar =  new ArrayList();
        try {
            Session session = em.unwrap(Session.class);
            BienesDepreciablesDAO codDAO = new BienesDepreciablesDAO(em);
            List<SgAfBienDepreciable> bienes = codDAO.obtenerPorFiltro(filtro, valorActivoFijoLimite, filtro.getSecurityOperation());
            Boolean agregar = false;
            if((filtro.getParaDepreciar() != null && filtro.getParaDepreciar() && filtro.getAnio() != null && filtro.getAnio() > 0) || (filtro.getObtenerDepreciacion() != null && filtro.getObtenerDepreciacion())) {
                for(SgAfBienDepreciable bien : bienes) {
                    agregar = true;
                     
                    if(filtro.getParaDepreciar() != null && filtro.getParaDepreciar()) {
                        if(bien.getBdeCategoriaFk() != null) {
                          bien.getBdeCategoriaFk().getSgAfSeccionesCategoriaList().size();
                          BigDecimal vidaUtil = bien.getBdeVidaUtil() != null ? new BigDecimal(bien.getBdeVidaUtil()) :
                                  (new BigDecimal((bien.getBdeCategoriaFk() != null
                                  && bien.getBdeCategoriaFk().getCabVidaUtil() != null) ? bien.getBdeCategoriaFk().getCabVidaUtil() : 0));

                          LocalDate fechaDepreciacionLimite = bien.getBdeFechaAdquisicion().plusYears(vidaUtil.intValue());

                          //Si ya finalizo su vida util al año de proceso, entonces no se agrega a la lista
                          if((Long.valueOf(fechaDepreciacionLimite.getYear()).compareTo(filtro.getAnio()) < 0)) {
                              agregar = false;
                          }
                      }
                    }
                    if(agregar && filtro.getObtenerDepreciacion() != null && filtro.getObtenerDepreciacion()) {
                        agregar = true;
                        
                        String queryMontos = "select coalesce(SUM(dep.dep_monto_depreciacion),0)  as montoDepreciado " 
                            + "from " + Constantes.SCHEMA_ACTIVO_FIJO +".sg_af_depreciaciones dep " +
                            " INNER JOIN " + Constantes.SCHEMA_ACTIVO_FIJO +".sg_af_bienes_depreciables bien on (dep.dep_bienes_depreciables_fk = bien.bde_pk) " +
                            " where dep.dep_bienes_depreciables_fk= :id";
                        
                            SQLQuery query = session.createSQLQuery(queryMontos);
                            query.addScalar("montoDepreciado", new BigDecimalType());

                            query.setParameter("id", bien.getBdePk());

                            List<CantidadesDTO> lista = query.setResultTransformer(Transformers.aliasToBean(CantidadesDTO.class)).list();
                            if(lista != null && !lista.isEmpty() && lista.size()> 0) {
                                bien.setBdeMontoDepreciado(lista.get(0).getMontoDepreciado());
                            }
                    }
                    
                    if(agregar) {
                        listaRetornar.add(bien);
                    }
                }
            } else {
                listaRetornar = bienes;
            }
            return listaRetornar;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
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
                CodigueraDAO<SgAfBienDepreciable> codDAO = new CodigueraDAO<>(em, SgAfBienDepreciable.class);
                depreciacionBean.eliminarPorIdBien(id);
                descargosDetalleBean.eliminarPorIdBien(id);
                trasladosDetalleBean.eliminarPorIdBien(id);
                codDAO.eliminarPorId(id, null);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Elimina la lista de objetos
     *
     * @param List<SgAfBienesDepreciables>
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarLista(List<SgAfBienDepreciable> lista) throws GeneralException {
        if (lista != null && !lista.isEmpty()) {
            try {
                for(SgAfBienDepreciable bien : lista) {
                   eliminar(bien.getBdePk());
                } 
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
