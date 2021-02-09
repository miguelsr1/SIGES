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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SolicitudTraslado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.filtros.FiltroTrasladoDetalle;
import sv.gob.mined.siges.negocio.validaciones.SolicitudTrasladoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TrasladosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfBienDepreciable;
import sv.gob.mined.siges.persistencia.entidades.SgAfTraslado;
import sv.gob.mined.siges.persistencia.entidades.SgAfTrasladosDetalle;

@Stateless
@Traced
public class TrasladosBean {
    
    private static final Logger LOGGER = Logger.getLogger(TrasladosBean.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private BienesDepreciablesBean bienesDAO;
    
    @Inject
    private TrasladosDetalleBean trasladosDetalleBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfTraslado
     * @throws GeneralException
     * 
     */
    public SgAfTraslado obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfTraslado> codDAO = new CodigueraDAO<>(em, SgAfTraslado.class);
                SgAfTraslado traslado = null;
                if (BooleanUtils.isTrue(dataSecurity)){
                    traslado = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
                } else {
                    traslado = codDAO.obtenerPorId(id, null);
                }
                if(traslado != null) {
                    traslado.getSgAfTrasladosDetalle().size();
                    traslado.getSgAfMotivosRechazoTraslado().size();
                }
                return traslado;
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
                CodigueraDAO<SgAfTraslado> codDAO = new CodigueraDAO<>(em, SgAfTraslado.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
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
     * @param entity SgAfTraslado
     * @return SgAfTraslado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfTraslado guardar(SolicitudTraslado solicitud, Boolean dataSecurity) throws GeneralException {
        SgAfTraslado entity = solicitud.getTraslado();
        Boolean trasladado = Boolean.FALSE;
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudTrasladoValidacionNegocio.validar(solicitud)) {
                    if(entity.getTraPk() != null) {
                        if(entity.getSgAfTrasladosDetalle() != null) {
                            entity.getSgAfTrasladosDetalle().size();
                        }
                        
                        if(!entity.getTraTipoTrasladoFk().getEtrCodigo().trim().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
                            entity.setTraUnidadAdmDestinoFk(null);
                            entity.setTraSedeDestinoFk(null);
                        } else {
                            entity.setTraUnidadDestino("");
                        } 
                        //Primero se obtiene lista de items antes de guardar
                        FiltroTrasladoDetalle filtro = new FiltroTrasladoDetalle();
                        filtro.setIncluirCampos(new String[]{"tdePk","tdeVersion","tdeBienesDepreciablesFk.bdePk","tdeBienesDepreciablesFk.bdeEstadosBienes.ebiPk","tdeBienesDepreciablesFk.bdeEstadosBienes.ebiVersion",
                            "tdeBienesDepreciablesFk.bdeEstadosSolicitud.ebiPk","tdeBienesDepreciablesFk.bdeEstadosSolicitud.ebiVersion",
                            "tdeBienesDepreciablesFk.bdeSede.sedPk","tdeBienesDepreciablesFk.bdeSede.sedTipo","tdeBienesDepreciablesFk.bdeSede.sedVersion",
                            "tdeBienesDepreciablesFk.bdeUnidadesAdministrativas.uadPk","tdeBienesDepreciablesFk.bdeUnidadesAdministrativas.uadVersion",
                            "tdeBienesDepreciablesFk.bdeEmpleadoFk.empPk","tdeBienesDepreciablesFk.bdeEmpleadoFk.empVersion","tdeBienesDepreciablesFk.bdeAsignadoA"});
                        
                        filtro.setTrasladoId(entity.getTraPk());
                        List<SgAfTrasladosDetalle> listaDetalle = trasladosDetalleBean.obtenerPorFiltro(filtro);
                        if(!listaDetalle.isEmpty()) {
                            List<Long> idsAcualizar = new ArrayList();
                            Boolean actualizarEstado = Boolean.TRUE;
                            SgAfBienDepreciable bienReferencia = listaDetalle.get(0).getTdeBienesDepreciablesFk();
                            bienReferencia.setBdeEstadosSolicitud(null);
                            for(SgAfTrasladosDetalle detalleAntes : listaDetalle) {
                                SgAfBienDepreciable bienAntes = detalleAntes.getTdeBienesDepreciablesFk();
                                for(SgAfTrasladosDetalle detalleActual : entity.getSgAfTrasladosDetalle()) {
                                    SgAfBienDepreciable bienActual = detalleActual.getTdeBienesDepreciablesFk();
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
                                bienesDAO.guardarLite(idsAcualizar, bienReferencia, Boolean.FALSE, trasladado);
                            }
                        }
                    }
                    

                    CodigueraDAO<SgAfTraslado> codDAO = new CodigueraDAO<>(em, SgAfTraslado.class);
                    if (BooleanUtils.isTrue(dataSecurity)){
                        entity = codDAO.guardar(entity, entity.getTraPk()== null ? ConstantesOperaciones.CREAR_SOLICITUD_TRASLADO_BIENES : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_TRASLADO_BIENES);
                    } else {
                        entity = codDAO.guardar(entity, null);
                    }
                    
                    FiltroTrasladoDetalle filtro = new FiltroTrasladoDetalle();
                    filtro.setIncluirCampos(new String[]{"tdePk","tdeVersion","tdeBienesDepreciablesFk.bdePk","tdeBienesDepreciablesFk.bdeEstadosBienes.ebiPk","tdeBienesDepreciablesFk.bdeEstadosBienes.ebiVersion",
                            "tdeBienesDepreciablesFk.bdeEstadosSolicitud.ebiPk","tdeBienesDepreciablesFk.bdeEstadosSolicitud.ebiVersion",
                            "tdeBienesDepreciablesFk.bdeSede.sedPk","tdeBienesDepreciablesFk.bdeSede.sedTipo","tdeBienesDepreciablesFk.bdeSede.sedVersion",
                            "tdeBienesDepreciablesFk.bdeUnidadesAdministrativas.uadPk","tdeBienesDepreciablesFk.bdeUnidadesAdministrativas.uadVersion",
                            "tdeBienesDepreciablesFk.bdeEmpleadoFk.empPk","tdeBienesDepreciablesFk.bdeEmpleadoFk.empVersion","tdeBienesDepreciablesFk.bdeAsignadoA"});
                        
                    filtro.setTrasladoId(entity.getTraPk());
                    List<SgAfTrasladosDetalle> listaDetalleTraslado = trasladosDetalleBean.obtenerPorFiltro(filtro);
                    
                    if(listaDetalleTraslado != null && !listaDetalleTraslado.isEmpty()) {
                        List<Long> idsDescargar = new ArrayList();
                        SgAfBienDepreciable bienReferencia = listaDetalleTraslado.get(0).getTdeBienesDepreciablesFk();
                        bienReferencia.setBdeEstadosSolicitud(entity.getTraEstadoFk());
                        bienReferencia.setBdeEstadosBienes(solicitud.getEstado());
                        //LOGGER.info("ESTADO DE TRASLADO: " + entity.getTraEstadoFk().getEbiCodigo());
                        if(entity.getTraEstadoFk().getEbiCodigo().trim().equals(Constantes.CODIGO_ESTADO_TRASLADADO)) {
                            //LOGGER.info("ES TRASLADADO..");
                            trasladado = Boolean.TRUE;
                            if(entity.getTraUnidadAdmDestinoFk() != null) {
                                bienReferencia.setBdeUnidadesAdministrativas(entity.getTraUnidadAdmDestinoFk());
                                bienReferencia.setBdeSede(null);
                            } else if(entity.getTraSedeDestinoFk() != null) {
                                bienReferencia.setBdeSede(entity.getTraSedeDestinoFk());
                                bienReferencia.setBdeUnidadesAdministrativas(null);
                            }
                            bienReferencia.setBdeEmpleadoFk(entity.getTraEmpleadoBienes());
                            bienReferencia.setBdeAsignadoA(entity.getTraAsignadoABienes());
                        }
                        
                        for(SgAfTrasladosDetalle detalleActual : listaDetalleTraslado) {
                             //Se actualiza estado para los bienes que estan en la nueva lista
                            idsDescargar.add(detalleActual.getTdeBienesDepreciablesFk().getBdePk());
                        }
                         //LOGGER.info("TOTAL DE BIENES A TRASLADAR: " + idsDescargar.size());
                         bienesDAO.guardarLite(idsDescargar, bienReferencia, Boolean.TRUE, trasladado);
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
            TrasladosDAO codDAO = new TrasladosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfTraslados>
     * @throws GeneralException
     */
    public List<SgAfTraslado> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            TrasladosDAO codDAO = new TrasladosDAO(em);
            List<SgAfTraslado> listaTraslados = codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            
            for (SgAfTraslado tra : listaTraslados) {
                tra.getSgAfTrasladosDetalle().size();
                tra.getSgAfMotivosRechazoTraslado().size();
            }
            return listaTraslados;
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
    public void eliminar(SolicitudTraslado solicitud) throws GeneralException {
        try {
            //Validar el elemento a eliminar. Si no valida, se lanza una excepcion
            if (SolicitudTrasladoValidacionNegocio.validarSolicitud(solicitud)) {
                CodigueraDAO<SgAfTraslado> codDAO = new CodigueraDAO<>(em, SgAfTraslado.class);
                SgAfTraslado entity = obtenerPorId(solicitud.getTraslado().getTraPk(), Boolean.TRUE);
                if(!entity.getSgAfTrasladosDetalle().isEmpty()) {
                    List<Long> idsActualizar = new ArrayList();
                    SgAfBienDepreciable bienReferencia = entity.getSgAfTrasladosDetalle().get(0).getTdeBienesDepreciablesFk();
                    bienReferencia.setBdeEstadosBienes(solicitud.getEstado());
                    bienReferencia.setBdeEstadosSolicitud(null);
                    //Se revierte el bien a su unidad de origen
                    if(entity.getTraUnidadAdmOrigenFk() != null) {
                        bienReferencia.setBdeUnidadesAdministrativas(entity.getTraUnidadAdmOrigenFk()); 
                    } else if(entity.getTraSedeOrigenFk() != null) {
                        bienReferencia.setBdeSede(entity.getTraSedeOrigenFk());
                    }
                    for(SgAfTrasladosDetalle detalle : entity.getSgAfTrasladosDetalle()) {
                        SgAfBienDepreciable bien = detalle.getTdeBienesDepreciablesFk();
                        if(bien != null) {
                            idsActualizar.add(bien.getBdePk());
                        }
                    }
                    bienesDAO.guardarLite(idsActualizar,bienReferencia,  Boolean.TRUE, Boolean.FALSE);
                }
                codDAO.eliminarPorId(entity.getTraPk(), null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
}