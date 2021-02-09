/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.filtros.FiltroSolicitudesOAE;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DesSolicitudPersonaJurValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SolicitudOAEDAO;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.persistencia.entidades.SgSolDeshabilitarPerJur;

@Stateless
@Traced
public class DeshabilitarPersonaJuridicaBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private PersonaOrganismoAdministracionBean personaOAEBean;
    
    @Inject
    private OrganismoAdministracionEscolarBean oaeBean;
    
    @Inject
    private SeguridadBean segBean;

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgSolDeshabilitarPerJur
     * @return SgSolDeshabilitarPerJur
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolDeshabilitarPerJur guardar(SgSolDeshabilitarPerJur entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DesSolicitudPersonaJurValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgSolDeshabilitarPerJur> codDAO = new CodigueraDAO(em, SgSolDeshabilitarPerJur.class);
                    //Se deshabilitan los miembros del OAE
                    deshabilitarMiembros(entity);
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
    
    public void deshabilitarMiembros(SgSolDeshabilitarPerJur entity){
        try{
            if(entity.getDpjAprobar() != null && entity.getDpjAprobar()){
                CodigueraDAO<SgPersonaOrganismoAdministracion> codDAO = new CodigueraDAO(em, SgPersonaOrganismoAdministracion.class);
                //El OAE se cambia a estado cerrado
                SgOrganismoAdministracionEscolar oae = oaeBean.obtenerPorId(entity.getDpjOaeFk().getOaePk());
                if(oae.getItemsEvaluarOAE() != null){
                    oae.getItemsEvaluarOAE().size();
                }
                oae.setOaeEstado(EnumEstadoOrganismoAdministrativo.CERRADO);
                if(entity.getDpjOaeFk()==null || entity.getDpjOaeFk().getOaeFechaCierre()==null){
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_FECHA_CIERRE_VACIO);
                    throw ge;
                }
                oae.setOaeFechaCierre(entity.getDpjOaeFk().getOaeFechaCierre());
                oaeBean.guardar(oae);
                
                //Se deshabilitan los miembros de dicho OAE
                FiltroPersonaOrganismoAdministrativo filtro = new FiltroPersonaOrganismoAdministrativo();
                filtro.setPoaOrganismoAdministrativoEscolar(entity.getDpjOaeFk().getOaePk());
                List<SgPersonaOrganismoAdministracion> listado = personaOAEBean.obtenerPorFiltro(filtro);
                for(SgPersonaOrganismoAdministracion m : listado){
                    m.setPoaHabilitado(Boolean.FALSE);
                    codDAO.guardar(m, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroSolicitudesOAE
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSolicitudesOAE filtro) throws GeneralException {
        try {
            if(filtro.getFechaDesde() != null){
                filtro.setFechaDesdeTime(filtro.getFechaDesde().atTime(LocalTime.MIN));
            }
            if(filtro.getFechaHasta() != null){
                filtro.setFechaHastaTime(filtro.getFechaHasta().atTime(LocalTime.MAX));
            }
            SolicitudOAEDAO codDAO = new SolicitudOAEDAO(em,segBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_SOLICITUDES_OAE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroSolicitudesOAE
     * @return Lista de <SgSolDeshabilitarPerJur>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgSolDeshabilitarPerJur> obtenerPorFiltro(FiltroSolicitudesOAE filtro) throws GeneralException {
        try {
            if(filtro.getFechaDesde() != null){
                filtro.setFechaDesdeTime(filtro.getFechaDesde().atTime(LocalTime.MIN));
            }
            if(filtro.getFechaHasta() != null){
                filtro.setFechaHastaTime(filtro.getFechaHasta().atTime(LocalTime.MAX));
            }
            SolicitudOAEDAO codDAO = new SolicitudOAEDAO(em,segBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_SOLICITUDES_OAE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNie
     * @throws GeneralException
     */
    public SgSolDeshabilitarPerJur obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolDeshabilitarPerJur> codDAO = new CodigueraDAO<>(em, SgSolDeshabilitarPerJur.class);
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
     * @param dataSecurity
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolDeshabilitarPerJur> codDAO = new CodigueraDAO<>(em, SgSolDeshabilitarPerJur.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
    
}
