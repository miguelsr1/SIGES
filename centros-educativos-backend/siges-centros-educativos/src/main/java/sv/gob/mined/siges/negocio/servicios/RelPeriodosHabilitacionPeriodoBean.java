/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.negocio.validaciones.RelPeriodosHabilitacionPeriodoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelPeriodosHabilitacionPeriodoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;
import sv.gob.mined.siges.persistencia.entidades.SgRelPeriodosHabilitacionPeriodo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelPeriodosHabilitacionPeriodoBean {
    
    @Inject
    private SeguridadBean seguridadBean;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelPeriodosHabilitacionPeriodo
     * @throws GeneralException
     */
    public SgRelPeriodosHabilitacionPeriodo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelPeriodosHabilitacionPeriodo> codDAO = new CodigueraDAO<>(em, SgRelPeriodosHabilitacionPeriodo.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgRelPeriodosHabilitacionPeriodo> codDAO = new CodigueraDAO<>(em, SgRelPeriodosHabilitacionPeriodo.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param rph SgRelPeriodosHabilitacionPeriodo
     * @return SgRelPeriodosHabilitacionPeriodo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelPeriodosHabilitacionPeriodo guardar(SgRelPeriodosHabilitacionPeriodo rph) throws GeneralException {
        try {
            if (rph != null) {
                if (RelPeriodosHabilitacionPeriodoValidacionNegocio.validar(rph)) {
                    CodigueraDAO<SgRelPeriodosHabilitacionPeriodo> codDAO = new CodigueraDAO<>(em, SgRelPeriodosHabilitacionPeriodo.class);
    
                        return codDAO.guardar(rph,null);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rph;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelPeriodosHabilitacionPeriodo filtro) throws GeneralException {
        try {
            RelPeriodosHabilitacionPeriodoDAO codDAO = new RelPeriodosHabilitacionPeriodoDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelPeriodosHabilitacionPeriodo>
     * @throws GeneralException
     */
    public List<SgRelPeriodosHabilitacionPeriodo> obtenerPorFiltro(FiltroRelPeriodosHabilitacionPeriodo filtro) throws GeneralException {
        try {
            RelPeriodosHabilitacionPeriodoDAO codDAO = new RelPeriodosHabilitacionPeriodoDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
            
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelPeriodosHabilitacionPeriodo>
     * @throws GeneralException
     */
    public List<SgRangoFecha> buscarRangosConSolicitud(FiltroRelPeriodosHabilitacionPeriodo filtro) throws GeneralException {
        try {
            List<SgRangoFecha> listRangoFechas=new ArrayList();
            
            RelPeriodosHabilitacionPeriodoDAO codDAO = new RelPeriodosHabilitacionPeriodoDAO(em,seguridadBean);
            if(filtro.getRangoFechaFkList()!=null && !filtro.getRangoFechaFkList().isEmpty()){
                List<Long> rangos=filtro.getRangoFechaFkList();
                filtro.setRangoFechaFkList(null);
                filtro.setIncluirCampos(new String[]{"rphPk","rphVersion","rfeCodigo",
                    "rphRangoFechaFk.rfeFechaDesde",
                    "rphRangoFechaFk.rfeFechaDesde",
                    "rphRangoFechaFk.rfeFechaHasta",
                    "rphRangoFechaFk.rfeHabilitado",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaPk",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaNombre",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaNumero",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaVersion",
                    "rphRangoFechaFk.rfePeriodoCalificacion.pcaEsPrueba",
                    "rphRangoFechaFkrfeVersion"});
                filtro.setMaxResults(1L);
                for(Long rango:rangos){
                    filtro.setRangoFechaFk(rango);
                    List<SgRelPeriodosHabilitacionPeriodo> rel=codDAO.obtenerPorFiltro(filtro,null);
                    if(rel!=null && !rel.isEmpty()){
                        listRangoFechas.add(rel.get(0).getRphRangoFechaFk());
                    }
                }
            }              
            
            
            return listRangoFechas;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }     
    
     /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelPeriodosHabilitacionPeriodo>
     * @throws GeneralException
     */
    public List<EnumCalendarioEscolar> buscarPeriodoExtraordinarioConSolicitud(FiltroRelPeriodosHabilitacionPeriodo filtro) throws GeneralException {
        try {
            List<EnumCalendarioEscolar> listCalendarioEscolar=new ArrayList();
            
            RelPeriodosHabilitacionPeriodoDAO codDAO = new RelPeriodosHabilitacionPeriodoDAO(em,seguridadBean);
            if(filtro.getRphTipoCalendarioEscolarList()!=null && !filtro.getRphTipoCalendarioEscolarList().isEmpty()){
                List<EnumCalendarioEscolar> list=filtro.getRphTipoCalendarioEscolarList();
                filtro.setRphTipoCalendarioEscolarList(null);
                filtro.setIncluirCampos(new String[]{"rphTipoCalendarioEscolar","rphPk","rphVersion"});
                filtro.setMaxResults(1L);
                for(EnumCalendarioEscolar elem:list){
                    filtro.setRphTipoCalendarioEscolar(elem);
                    List<SgRelPeriodosHabilitacionPeriodo> rel=codDAO.obtenerPorFiltro(filtro,null);
                    if(rel!=null && !rel.isEmpty()){
                        listCalendarioEscolar.add(rel.get(0).getRphTipoCalendarioEscolar());
                    }
                }
            }              
            
            
            return listCalendarioEscolar;
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
                CodigueraDAO<SgRelPeriodosHabilitacionPeriodo> codDAO = new CodigueraDAO<>(em, SgRelPeriodosHabilitacionPeriodo.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
