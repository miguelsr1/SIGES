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
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDiplomaEstudiante;
import sv.gob.mined.siges.negocio.validaciones.DiplomaEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DiplomaEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomaEstudiante;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class DiplomaEstudianteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean segBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDiplomaEstudiante
     * @throws GeneralException
     */
    public SgDiplomaEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiplomaEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomaEstudiante.class);
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
                CodigueraDAO<SgDiplomaEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomaEstudiante.class);
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
     * @param die SgDiplomaEstudiante
     * @return SgDiplomaEstudiante
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDiplomaEstudiante guardar(SgDiplomaEstudiante die) throws GeneralException {
        try {
            if (die != null) {
                if (DiplomaEstudianteValidacionNegocio.validar(die)) {
                    CodigueraDAO<SgDiplomaEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomaEstudiante.class);
              
                        return codDAO.guardar(die,null);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return die;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDiplomaEstudiante filtro) throws GeneralException {
        try {
            DiplomaEstudianteDAO codDAO = new DiplomaEstudianteDAO(em,segBean);
            return codDAO.obtenerTotalPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDiplomaEstudiante>
     * @throws GeneralException
     */
    public List<SgDiplomaEstudiante> obtenerPorFiltro(FiltroDiplomaEstudiante filtro) throws GeneralException {
        try {
            DiplomaEstudianteDAO codDAO = new DiplomaEstudianteDAO(em,segBean);
            return codDAO.obtenerPorFiltro(filtro,null);
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
                CodigueraDAO<SgDiplomaEstudiante> codDAO = new CodigueraDAO<>(em, SgDiplomaEstudiante.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
