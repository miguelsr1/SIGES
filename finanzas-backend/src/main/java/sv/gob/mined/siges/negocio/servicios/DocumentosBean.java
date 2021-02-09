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
import sv.gob.mined.siges.filtros.FiltroDocumentos;
import sv.gob.mined.siges.negocio.validaciones.DocumentosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DocumentosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgDocumentos;

/**
 * Servicio que gestiona documentos
 *
 * @author Sofis Solutions
 */
@Stateless
public class DocumentosBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ArchivoBean archivoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDocumentos
     * @throws GeneralException
     */
    public SgDocumentos obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDocumentos> codDAO = new CodigueraDAO<>(em, SgDocumentos.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_DOCUMENTOS);
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
                CodigueraDAO<SgDocumentos> codDAO = new CodigueraDAO<>(em, SgDocumentos.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_DOCUMENTOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param doc SgDocumentos
     * @return SgDocumentos
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDocumentos guardar(SgDocumentos doc) throws GeneralException {
        try {
            if (doc != null) {
                if (DocumentosValidacionNegocio.validar(doc)) {
                    CodigueraDAO<SgDocumentos> codDAO = new CodigueraDAO<>(em, SgDocumentos.class);
                    CodigueraDAO<SgArchivo> arcDAO = new CodigueraDAO<>(em, SgArchivo.class);
                    arcDAO.guardar(doc.getDocArchivoFk(), ConstantesOperaciones.CREAR_DOCUMENTOS);
                    return codDAO.guardar(doc, ConstantesOperaciones.CREAR_DOCUMENTOS);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return doc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroDocumentos
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDocumentos filtro) throws GeneralException {
        try {
            DocumentosDAO codDAO = new DocumentosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroDocumentos
     * @return Lista de <SgDocumentos>
     * @throws GeneralException
     */
    public List<SgDocumentos> obtenerPorFiltro(FiltroDocumentos filtro) throws GeneralException {
        try {
            DocumentosDAO codDAO = new DocumentosDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgDocumentos> codDAO = new CodigueraDAO<>(em, SgDocumentos.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_DOCUMENTOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
