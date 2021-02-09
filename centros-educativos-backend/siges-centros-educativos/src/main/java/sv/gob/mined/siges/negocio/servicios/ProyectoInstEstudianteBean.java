/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroProyectoInstEstudiante;
import sv.gob.mined.siges.negocio.validaciones.ProyectoInstEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ProyectoInstEstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoInstEstudiante;

@Stateless
@Traced
public class ProyectoInstEstudianteBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgProyectoInstEstudiante
     * @throws GeneralException
     */
    public SgProyectoInstEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgProyectoInstEstudiante> codDAO = new CodigueraDAO<>(em, SgProyectoInstEstudiante.class);
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
                CodigueraDAO<SgProyectoInstEstudiante> codDAO = new CodigueraDAO<>(em, SgProyectoInstEstudiante.class);
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
     * @param entity SgProyectoInstEstudiante
     * @return SgProyectoInstEstudiante
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgProyectoInstEstudiante guardar(SgProyectoInstEstudiante entity) throws GeneralException {
        try {
            if (entity != null) {
                if (ProyectoInstEstudianteValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgProyectoInstEstudiante> codDAO = new CodigueraDAO<>(em, SgProyectoInstEstudiante.class);
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
                CodigueraDAO<SgProyectoInstEstudiante> codDAO = new CodigueraDAO<>(em, SgProyectoInstEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroProyectoInstEstudiante
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroProyectoInstEstudiante filtro) throws GeneralException {
        try {
            ProyectoInstEstudianteDAO codDAO = new ProyectoInstEstudianteDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroProyectoInstEstudiante
     * @return Lista de SgProyectoInstEstudiante
     * @throws GeneralException
     */     
    public List<SgProyectoInstEstudiante> obtenerPorFiltro(FiltroProyectoInstEstudiante filtro) throws GeneralException {
        try {
            ProyectoInstEstudianteDAO codDAO = new ProyectoInstEstudianteDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Guarda el objeto indicado
     *
     * @param estudiantes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void guardarLista(SgProyectoInstEstudiante[] estudiantes) throws GeneralException {
        try {
            CodigueraDAO<SgProyectoInstEstudiante> codDAO = new CodigueraDAO<>(em, SgProyectoInstEstudiante.class);
            for (SgProyectoInstEstudiante estudiante : estudiantes) {
                if(estudiante.getPiePk()==null){
                    codDAO.guardar(estudiante, ConstantesOperaciones.CREAR_PROYECTO_INST_ESTUDIANTE);
                }else{
                    codDAO.guardar(estudiante, ConstantesOperaciones.ACTUALIZAR_PROYECTO_INST_ESTUDIANTE);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
