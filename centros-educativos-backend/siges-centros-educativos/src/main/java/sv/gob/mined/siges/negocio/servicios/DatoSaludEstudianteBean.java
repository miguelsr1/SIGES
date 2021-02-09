/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDatoSaludEstudiante;
import sv.gob.mined.siges.negocio.validaciones.DatoSaludEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DatoSaludEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDatoSaludEstudiante;

/**
 *
 * @author bruno
 */
@Stateless
@Traced
public class DatoSaludEstudianteBean {
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDatoSaludEstudiante
     * @throws GeneralException
     */
    public SgDatoSaludEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoSaludEstudiante> codDAO = new CodigueraDAO<>(em, SgDatoSaludEstudiante.class);
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
                CodigueraDAO<SgDatoSaludEstudiante> codDAO = new CodigueraDAO<>(em, SgDatoSaludEstudiante.class);
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
     * @param entity SgDatoSaludEstudiante
     * @return SgDatoSaludEstudiante
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDatoSaludEstudiante guardar(SgDatoSaludEstudiante entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DatoSaludEstudianteValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDatoSaludEstudiante> codDAO = new CodigueraDAO<>(em, SgDatoSaludEstudiante.class);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoSaludEstudiante> codDAO = new CodigueraDAO<>(em, SgDatoSaludEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDatoSaludEstudiante
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDatoSaludEstudiante filtro) throws GeneralException {
        try {
            DatoSaludEstudianteDAO codDAO = new DatoSaludEstudianteDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }  
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDatoSaludEstudiante
     * @return Lista de SgDatoSaludEstudiante
     * @throws GeneralException
     */
    public List<SgDatoSaludEstudiante> obtenerPorFiltro(FiltroDatoSaludEstudiante filtro) throws GeneralException {
        try {
            DatoSaludEstudianteDAO codDAO = new DatoSaludEstudianteDAO(em);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
