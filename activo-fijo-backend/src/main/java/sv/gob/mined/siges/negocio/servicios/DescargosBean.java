/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

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
import org.eclipse.microprofile.opentracing.Traced;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.CantidadesDTO;
import sv.gob.mined.siges.dto.SolicitudDescargo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.filtros.FiltroDescargosDetalle;
import sv.gob.mined.siges.negocio.validaciones.SolicitudDescargoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DescargosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargo;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargoDetalle;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEstadosCalidad;

@Stateless
@Traced
public class DescargosBean {
    private static final Logger LOGGER = Logger.getLogger(DescargosBean.class.getName());
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private BienesDepreciablesBean bienesDAO;
    
    @Inject
    private DescargosDetalleBean descargoDetalleBean;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDescargo
     * @throws GeneralException
     * 
     */
    public SgAfDescargo obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDescargo> codDAO = new CodigueraDAO<>(em, SgAfDescargo.class);
                SgAfDescargo descargo = null;
                if (BooleanUtils.isTrue(dataSecurity)){
                    descargo = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
                } else {
                    descargo = codDAO.obtenerPorId(id, null);
                }
                if(descargo != null) {
                    descargo.getSgAfDescargosDetalle().size();
                    descargo.getSgAfMotivosRechazoDescargo().size();
                }
                return descargo;
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
                CodigueraDAO<SgAfDescargo> codDAO = new CodigueraDAO<>(em, SgAfDescargo.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_DESCARGO);
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
     * @param entity SgAfDescargo
     * @return SgAfDescargo
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfDescargo guardar(SolicitudDescargo solicitud, Boolean dataSecurity) throws GeneralException {
        SgAfDescargo entity = solicitud.getDescargo();
        try {
            //TODO REVISAR
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudDescargoValidacionNegocio.validar(solicitud)) {
                    if(entity.getDesPk() != null) {
                        //Primero se obtiene lista de items antes de guardar
                        FiltroDescargosDetalle filtro = new FiltroDescargosDetalle();
                        filtro.setIncluirCampos(new String[]{"ddePk","ddeVersion","ddeBienesDepreciablesFk.bdePk","ddeBienesDepreciablesFk.bdeEstadosBienes.ebiPk","ddeBienesDepreciablesFk.bdeEstadosBienes.ebiVersion",
                        "ddeBienesDepreciablesFk.bdeEstadosSolicitud.ebiPk","ddeBienesDepreciablesFk.bdeEstadosSolicitud.ebiVersion","ddeBienesDepreciablesFk.bdeFechaDescargo"});
                        
                        filtro.setDescargoId(entity.getDesPk());
                        List<SgAfDescargoDetalle> listaDetalle = descargoDetalleBean.obtenerPorFiltro(filtro);

                        if(!listaDetalle.isEmpty()) {
                            List<Long> idsAcualizar = new ArrayList();
                            Boolean actualizarEstado = Boolean.TRUE;
                            SgAfBienDepreciable bienReferencia = listaDetalle.get(0).getDdeBienesDepreciablesFk();
                            bienReferencia.setBdeEstadosSolicitud(null);
                            bienReferencia.setBdeFechaDescargo(null);
                            bienReferencia.setBdeEstadosBienes(solicitud.getEstado());
                            for(SgAfDescargoDetalle detalleAntes : listaDetalle) {
                                SgAfBienDepreciable bienAntes = detalleAntes.getDdeBienesDepreciablesFk();
                                for(SgAfDescargoDetalle detalleActual : entity.getSgAfDescargosDetalle()) {
                                    SgAfBienDepreciable bienActual = detalleActual.getDdeBienesDepreciablesFk();
                                    if(bienAntes.getBdePk().equals(bienActual.getBdePk())) {
                                        actualizarEstado = Boolean.FALSE;
                                        break;
                                    }
                                }
                                if(actualizarEstado) {
                                    //Se agregan a la lista los bienes a actualizar.
                                    idsAcualizar.add(bienAntes.getBdePk());
                                }
                            }
                            if(idsAcualizar != null && !idsAcualizar.isEmpty()) {
                                //Se actualiza estado de solicitud a null, ya que deja de estar en solicitud.
                                bienesDAO.guardarLite(idsAcualizar, bienReferencia, Boolean.FALSE,Boolean.FALSE);
                            }
                        }
                    }
                    

                    CodigueraDAO<SgAfDescargo> codDAO = new CodigueraDAO<>(em, SgAfDescargo.class);
                    if (BooleanUtils.isTrue(dataSecurity)){
                        entity = codDAO.guardar(entity, entity.getDesPk()== null ? ConstantesOperaciones.CREAR_SOLICITUD_DESCARGO : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_DESCARGO);
                    } else {
                        entity = codDAO.guardar(entity, null);
                    }
                    
                    FiltroDescargosDetalle filtro = new FiltroDescargosDetalle();
                    filtro.setIncluirCampos(new String[]{"ddePk","ddeVersion","ddeBienesDepreciablesFk.bdePk","ddeBienesDepreciablesFk.bdeEstadosBienes.ebiPk","ddeBienesDepreciablesFk.bdeEstadosBienes.ebiVersion",
                        "ddeBienesDepreciablesFk.bdeEstadosSolicitud.ebiPk","ddeBienesDepreciablesFk.bdeEstadosSolicitud.ebiVersion","ddeBienesDepreciablesFk.bdeFechaDescargo",
                        "ddeBienesDepreciablesFk.bdeEstadoCalidad.ecaPk","ddeBienesDepreciablesFk.bdeEstadoCalidad.ecaVersion"});
                    
                    filtro.setDescargoId(entity.getDesPk());
                    List<SgAfDescargoDetalle> listaDetalleDescargo = descargoDetalleBean.obtenerPorFiltro(filtro);
                    
                    if(listaDetalleDescargo != null && !listaDetalleDescargo.isEmpty()) {
                       List<Long> idsDescargar = new ArrayList();
                       SgAfBienDepreciable bienReferencia = listaDetalleDescargo.get(0).getDdeBienesDepreciablesFk();
                       bienReferencia.setBdeEstadosSolicitud(entity.getDesEstadoFk());
                       if(entity.getDesEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_DESCARGADO)) {
                            bienReferencia.setBdeEstadosBienes(entity.getDesEstadoFk());
                            bienReferencia.setBdeFechaDescargo(entity.getDesFechaDescargo().atStartOfDay());
                            
                            if(entity.getDesTipoDescargoFk().getEdeCodigo().equals(Constantes.CODIGO_ESTADO_TIPO_DESCARGO_INSERVIBLE)) {
                                List<SgAfEstadosCalidad> calidad= em.createQuery("select e from SgAfEstadosCalidad e where e.ecaCodigo = :codigo")
                                                        .setParameter("codigo", Constantes.CODIGO_ESTADO_CALIDAD_MALO).getResultList();
                                if(calidad != null && !calidad.isEmpty()) {
                                    bienReferencia.setBdeEstadoCalidad(calidad.get(0));
                                }
                            }
                            
                       }
                       for(SgAfDescargoDetalle detalleActual : listaDetalleDescargo) {
                            //Se actualiza estado para los bienes que estan en la nueva lista
                            idsDescargar.add(detalleActual.getDdeBienesDepreciablesFk().getBdePk());
                        }
                       
                       bienesDAO.guardarLite(idsDescargar, bienReferencia, Boolean.FALSE, Boolean.FALSE);
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
    
    @Interceptors({AuditInterceptor.class})
    public SgAfDescargo guardarActaDescargo(SgAfDescargo entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (SolicitudDescargoValidacionNegocio.validarSolicitudActaDescargo(entity)) {
                CodigueraDAO<SgAfDescargo> codDAO = new CodigueraDAO<>(em, SgAfDescargo.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    entity = codDAO.guardar(entity, entity.getDesPk()== null ? ConstantesOperaciones.CREAR_SOLICITUD_DESCARGO : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_DESCARGO);
                } else {
                    entity = codDAO.guardar(entity, null);
                }
            }
            
        }  catch (BusinessException be) {
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
            DescargosDAO codDAO = new DescargosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfDescargos>
     * @throws GeneralException
     */
    public List<SgAfDescargo> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            DescargosDAO codDAO = new DescargosDAO(em);
            
            List<SgAfDescargo> listaDescargos = codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            Session session = em.unwrap(Session.class);
            for (SgAfDescargo e : listaDescargos) {
                if(filtro.getObtenerCantidad() != null && filtro.getObtenerCantidad()) {
                    String queryCantidades = "select COUNT(det.dde_pk) as cantidad, SUM(bien.bde_valor_adquisicion) as montoTotal "
                            + "from " + Constantes.SCHEMA_ACTIVO_FIJO +".sg_af_descargos_detalle det " +
                            " INNER JOIN " + Constantes.SCHEMA_ACTIVO_FIJO +".sg_af_bienes_depreciables bien on (det.dde_bienes_depreciables_fk = bien.bde_pk) " +
                            " where det.dde_descargo_fk= :id";
                
                    SQLQuery query = session.createSQLQuery(queryCantidades);
                    query.addScalar("cantidad", new IntegerType());
                    query.addScalar("montoTotal", new BigDecimalType());

                    query.setParameter("id", e.getDesPk());
                    
                    List<CantidadesDTO> lista = query.setResultTransformer(Transformers.aliasToBean(CantidadesDTO.class)).list();
                    if(lista != null && !lista.isEmpty() && lista.size() > 0) {
                        e.setCantidad(lista.get(0).getCantidad());
                        e.setTotalMonto(lista.get(0).getMontoTotal());
                    }
                } else {
                    e.getSgAfDescargosDetalle().size();
                    e.getSgAfMotivosRechazoDescargo().size(); 
                }
            }
            
            return listaDescargos;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con con el id indicado y actualiza los bienes a un estado determinado
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SolicitudDescargo solicitud) throws GeneralException {
        try {
            //Validar el elemento a eliminar. Si no valida, se lanza una excepcion
            if (SolicitudDescargoValidacionNegocio.validarSolicitud(solicitud)) {
                CodigueraDAO<SgAfDescargo> codDAO = new CodigueraDAO<>(em, SgAfDescargo.class);
                SgAfDescargo entity = obtenerPorId(solicitud.getId(), Boolean.TRUE);
                
                if(!entity.getSgAfDescargosDetalle().isEmpty()) {
                    List<Long> idsActualizar = new ArrayList();
                    SgAfBienDepreciable bienReferencia = entity.getSgAfDescargosDetalle().get(0).getDdeBienesDepreciablesFk();
                    bienReferencia.setBdeEstadosBienes(solicitud.getEstado());
                    bienReferencia.setBdeFechaDescargo(null);
                    bienReferencia.setBdeEstadosSolicitud(null);
                    
                    for(SgAfDescargoDetalle detalle : entity.getSgAfDescargosDetalle()) {
                        SgAfBienDepreciable bien = detalle.getDdeBienesDepreciablesFk();
                        if(bien != null) {
                            idsActualizar.add(bien.getBdePk());
                        }
                    }
                    bienesDAO.guardarLite(idsActualizar,bienReferencia,  Boolean.FALSE, Boolean.FALSE);
                }
                codDAO.eliminarPorId(entity.getDesPk(), null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}