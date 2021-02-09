/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.negocio.validaciones.CalificacionDiplomadoEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalificacionDiplomadoEstudianteDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomadoEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CalificacionDiplomadoEstudianteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalificacionDiplomadoEstudiante
     * @throws GeneralException
     */
    public SgCalificacionDiplomadoEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionDiplomadoEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomadoEstudiante.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO_ESTUDIANTE);
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
                CodigueraDAO<SgCalificacionDiplomadoEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomadoEstudiante.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO_ESTUDIANTE);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param cde SgCalificacionDiplomadoEstudiante
     * @return SgCalificacionDiplomadoEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionDiplomadoEstudiante guardar(SgCalificacionDiplomadoEstudiante cde) throws GeneralException {
        try {
            if (cde != null) {
                if (CalificacionDiplomadoEstudianteValidacionNegocio.validar(cde)) {
                    CodigueraDAO<SgCalificacionDiplomadoEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomadoEstudiante.class);
         
                        return codDAO.guardar(cde,null);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cde;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCalificacionDiplomadoEstudiante filtro) throws GeneralException {
        try {
            CalificacionDiplomadoEstudianteDAO codDAO = new CalificacionDiplomadoEstudianteDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO_ESTUDIANTE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalificacionDiplomadoEstudiante>
     * @throws GeneralException
     */
    public List<SgCalificacionDiplomadoEstudiante> obtenerPorFiltro(FiltroCalificacionDiplomadoEstudiante filtro) throws GeneralException {
        try {
            CalificacionDiplomadoEstudianteDAO codDAO = new CalificacionDiplomadoEstudianteDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACION_DIPLOMADO_ESTUDIANTE);
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
                CodigueraDAO<SgCalificacionDiplomadoEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionDiplomadoEstudiante.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
