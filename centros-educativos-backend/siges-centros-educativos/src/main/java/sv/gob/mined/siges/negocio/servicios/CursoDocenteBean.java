/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoCurso;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCursoDocente;
import sv.gob.mined.siges.negocio.validaciones.CursoDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CursoDocenteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCursoDocente;

@Stateless
@Traced
public class CursoDocenteBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgCursoDocente
     * @throws GeneralException
     */
    public SgCursoDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                SgCursoDocente c = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CURSOS_DOCENTES);
                if (c.getCdsCeldasDiaHora() != null){
                    c.getCdsCeldasDiaHora().size();
                }
                return c;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CURSOS_DOCENTES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return SgCursoDocente
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCursoDocente guardar(SgCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                if (CursoDocenteValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                    return codDAO.guardar(entity, entity.getCdsPk() == null ? ConstantesOperaciones.CREAR_CURSOS_DOCENTES : ConstantesOperaciones.ACTUALIZAR_CURSOS_DOCENTES);
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
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCursoDocente filtro) throws GeneralException {
        try {
            CursoDocenteDAO codDAO = new CursoDocenteDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CURSOS_DOCENTES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgCursoDocente> obtenerPorFiltro(FiltroCursoDocente filtro) throws GeneralException {
        try {
            CursoDocenteDAO codDAO = new CursoDocenteDAO(em);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CURSOS_DOCENTES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CURSOS_DOCENTES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }


    /**
     * Publica el objeto indicado
     *
     * @param entity
     * @return SgCursoDocente
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCursoDocente publicar(SgCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                if (CursoDocenteValidacionNegocio.validar(entity)) {
                    entity.setCdsEstado(EnumEstadoCurso.PUBLICADO);
                    CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                    return codDAO.guardar(entity, ConstantesOperaciones.PUBLICAR_CURSOS_DOCENTES);
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
     * Cerrar el objeto indicado
     *
     * @param entity
     * @return SgCursoDocente
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCursoDocente cerrar(SgCursoDocente entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                if (CursoDocenteValidacionNegocio.validar(entity)) {
                    entity.setCdsEstado(EnumEstadoCurso.CERRADO);
                    CodigueraDAO<SgCursoDocente> codDAO = new CodigueraDAO<>(em, SgCursoDocente.class);
                    return codDAO.guardar(entity, ConstantesOperaciones.CERRAR_CURSOS_DOCENTES);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
}
