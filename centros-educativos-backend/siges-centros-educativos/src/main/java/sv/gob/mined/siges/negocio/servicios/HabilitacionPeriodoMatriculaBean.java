/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.filtros.FiltroRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.negocio.validaciones.HabilitacionPeriodoMatriculaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.HabilitacionPeriodoMatriculaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgRelHabilitacionMatriculaNivel;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class HabilitacionPeriodoMatriculaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject
    private RelHabilitacionMatriculaNivelBean relHabilitacionMatriculaNivelBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgHabilitacionPeriodoMatricula
     * @throws GeneralException
     */
    public SgHabilitacionPeriodoMatricula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHabilitacionPeriodoMatricula> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoMatricula.class);
                SgHabilitacionPeriodoMatricula periodo=codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_HABILITACION_PERIODO_MATRICULA);
                if(periodo.getHmpRelHabilitacionMatriculaNivel()!=null){
                    periodo.getHmpRelHabilitacionMatriculaNivel().size();
                }
                if(periodo.getHmpSedeFk()!=null){
                    periodo.getHmpSedeFk().getSedPk();
                }
                return periodo;
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
                CodigueraDAO<SgHabilitacionPeriodoMatricula> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoMatricula.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_HABILITACION_PERIODO_MATRICULA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param hmp SgHabilitacionPeriodoMatricula
     * @return SgHabilitacionPeriodoMatricula
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoMatricula guardar(SgHabilitacionPeriodoMatricula hmp) throws GeneralException {
        try {
            if (hmp != null) {
                if (hmp.getHmpPk()== null) {
                    hmp.setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula.PENDIENTE);
                    hmp.setHmpFechaSolicitud(LocalDate.now());
                }
                if (HabilitacionPeriodoMatriculaValidacionNegocio.validar(hmp)) {
                    CodigueraDAO<SgHabilitacionPeriodoMatricula> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoMatricula.class);
               
                        return codDAO.guardar(hmp,null);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hmp;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoMatricula aprobarSolicitud(SgHabilitacionPeriodoMatricula hpc) throws GeneralException {
        try {
            if (hpc != null) {
                hpc.setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula.APROBADA);
                hpc = guardar(hpc);               

                return hpc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hpc;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoMatricula rechazarSolicitud(SgHabilitacionPeriodoMatricula hpc) throws GeneralException {
        try {
            if (hpc != null) {
                hpc.setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula.RECHAZADA);
                return guardar(hpc);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hpc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroHabilitacionPeriodoMatricula filtro) throws GeneralException {
        try {
            HabilitacionPeriodoMatriculaDAO codDAO = new HabilitacionPeriodoMatriculaDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_HABILITACION_PERIODO_MATRICULA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgHabilitacionPeriodoMatricula>
     * @throws GeneralException
     */
    public List<SgHabilitacionPeriodoMatricula> obtenerPorFiltro(FiltroHabilitacionPeriodoMatricula filtro) throws GeneralException {
        try {
            HabilitacionPeriodoMatriculaDAO codDAO = new HabilitacionPeriodoMatriculaDAO(em, seguridadBean);
            List<SgHabilitacionPeriodoMatricula> hpm=codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_HABILITACION_PERIODO_MATRICULA);
            
            if(filtro.getIncluyendoNivel()!=null){
                List<Long> pks=hpm.stream().map(c->c.getHmpPk()).collect(Collectors.toList());
                FiltroRelHabilitacionMatriculaNivel frm=new FiltroRelHabilitacionMatriculaNivel();
                frm.setRhnHabilitacionPeriodosPkList(pks);
                frm.setIncluirCampos(new String[]{"rhnHabilitacionPeriodoMatriculaFk.hmpPk","rhnNivelFk.nivPk","rhnNivelFk.nivNombre"});
                List<SgRelHabilitacionMatriculaNivel> ret= relHabilitacionMatriculaNivelBean.obtenerPorFiltro(frm);
                
                for(SgHabilitacionPeriodoMatricula hp:hpm){
                    hp.setHmpRelHabilitacionMatriculaNivel(ret.stream().filter(c->c.getRhnHabilitacionPeriodoMatriculaFk().getHmpPk().equals(hp.getHmpPk())).collect(Collectors.toList()));
                }
            }
            
            return hpm;
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
                CodigueraDAO<SgHabilitacionPeriodoMatricula> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoMatricula.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
