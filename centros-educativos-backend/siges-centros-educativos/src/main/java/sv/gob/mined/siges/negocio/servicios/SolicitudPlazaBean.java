/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAplicacionPlaza;
import sv.gob.mined.siges.filtros.FiltroSolicitudPlaza;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.SolicitudPlazaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SolicitudPlazaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudPlaza;

@Stateless
@Traced
public class SolicitudPlazaBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean segBean;
    
    @Inject
    private AplicacionPlazaBean aplicacionPlazaBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSolicitudPlaza
     * @throws GeneralException
     */
    public SgSolicitudPlaza obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudPlaza> codDAO = new CodigueraDAO<>(em, SgSolicitudPlaza.class);
                return codDAO.obtenerPorId(id, null);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudPlaza> codDAO = new CodigueraDAO<>(em, SgSolicitudPlaza.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgSolicitudPlaza
     * @return SgSolicitudPlaza
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudPlaza guardar(SgSolicitudPlaza entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SolicitudPlazaValidacionNegocio.validar(entity)) {             
                    
                    CodigueraDAO<SgSolicitudPlaza> codDAO = new CodigueraDAO<>(em, SgSolicitudPlaza.class);
                    return codDAO.guardar(entity, null);
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
     * @param entity SgSolicitudPlaza
     * @return SgSolicitudPlaza
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudPlaza publicarSolicitud(SgSolicitudPlaza entity) throws GeneralException {
        try {
            if (entity != null) {
                FiltroAplicacionPlaza fap=new FiltroAplicacionPlaza();
                fap.setAplPlaza(entity.getSplPk());
                fap.setSeleccionadoEnMatriz(Boolean.TRUE);
                fap.setMaxResults(1L);
                fap.setIncluirCampos(new String[]{"aplVersion"});
                List<SgAplicacionPlaza> list= aplicacionPlazaBean.obtenerPorFiltro(fap);
                if(list.isEmpty()){
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_PUBLICACION_SOLICITUD_SIN_DOCENTE_ASIGNADO);
                    throw ge;
                }
                
                SgSolicitudPlaza sp=this.obtenerPorId(entity.getSplPk());
                sp.setSplEstado(EnumEstadoSolicitudPlaza.PUBLICADA);
                
                
                
                CodigueraDAO<SgSolicitudPlaza> codDAO = new CodigueraDAO<>(em, SgSolicitudPlaza.class);
                return codDAO.guardar(sp, null);
                
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
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSolicitudPlaza filtro) throws GeneralException {
        try {
            SolicitudPlazaDAO codDAO = new SolicitudPlazaDAO(em, segBean);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSolicitudPlaza
     * @throws GeneralException
     */
    public List<SgSolicitudPlaza> obtenerPorFiltro(FiltroSolicitudPlaza filtro) throws GeneralException {
        try {
            SolicitudPlazaDAO codDAO = new SolicitudPlazaDAO(em, segBean);
            List<SgSolicitudPlaza> list=  codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            
            if(BooleanUtils.isTrue(filtro.getEsPosiblePublicar())){
                List<SgSolicitudPlaza> l= list.stream().filter(c->c.getSplEstado().equals(EnumEstadoSolicitudPlaza.APROBADA)).collect(Collectors.toList());
                
                if(!l.isEmpty()){
                    List<Long> solicitudesPk=l.stream().map(c->c.getSplPk()).collect(Collectors.toList());
                    
                    FiltroAplicacionPlaza fap=new FiltroAplicacionPlaza();
                    fap.setAplPlazas(solicitudesPk);
                    fap.setSeleccionadoEnMatriz(Boolean.TRUE);
                    fap.setMaxResults(1L);
                    fap.setIncluirCampos(new String[]{"aplPlaza.splPk", "aplVersion"});
                    List<SgAplicacionPlaza> listApl= aplicacionPlazaBean.obtenerPorFiltro(fap);
                    
                    for(SgAplicacionPlaza apl:listApl){
                        List<SgSolicitudPlaza> sol=list.stream().filter(c->c.getSplPk().equals(apl.getAplPlaza().getSplPk())).collect(Collectors.toList());
                        if(!sol.isEmpty()){
                            sol.get(0).setSplPuedeSerPublicado(Boolean.TRUE);
                        }
                    }
                    
                }
            }
            
            return list;
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
                CodigueraDAO<SgSolicitudPlaza> codDAO = new CodigueraDAO<>(em, SgSolicitudPlaza.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
