/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEtiqueta;
import sv.gob.mined.siges.filtros.FiltroPagina;
import sv.gob.mined.siges.negocio.validaciones.EtiquetaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EtiquetaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRhEtiqueta;
import sv.gob.mined.siges.persistencia.entidades.SgRhPagina;
import sv.gob.mined.siges.rest.PaginaRecurso;

@Stateless
public class EtiquetaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private PaginaBean paginaBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRhEtiqueta
     * @throws GeneralException
     */
    public SgRhEtiqueta obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRhEtiqueta> codDAO = new CodigueraDAO<>(em, SgRhEtiqueta.class);
                SgRhEtiqueta c = codDAO.obtenerPorId(id, null);
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
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRhEtiqueta> codDAO = new CodigueraDAO<>(em, SgRhEtiqueta.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    private static final Logger LOGGER = Logger.getLogger(PaginaRecurso.class.getName());

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgRhEtiqueta
     * @return SgRhEtiqueta
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgRhEtiqueta guardar(SgRhEtiqueta entity) throws GeneralException {
        try {
            if (entity != null) {
                if (EtiquetaValidacionNegocio.validar(entity)) {
                    FiltroPagina fp = new FiltroPagina();
                    fp.setPagNivelPk(entity.getEtiPagina().getPagNivel().getNivPk());
                    fp.setPagAnio(entity.getEtiPagina().getPagAnio());
                    fp.setPagPagina(entity.getEtiPagina().getPagPagina());
                    fp.setPagLibro(entity.getEtiPagina().getPagLibro());
                    List<SgRhPagina> listPagina = paginaBean.obtenerPorFiltro(fp);
                    if (!listPagina.isEmpty()) {
                        entity.setEtiPagina(listPagina.get(0));
                    }
                    if (listPagina.isEmpty() && entity.getEtiPagina().getPagPk() != null) {
                        SgRhPagina rgPag = new SgRhPagina();
                        rgPag.setPagAnio(entity.getEtiPagina().getPagAnio());
                        rgPag.setPagNivel(entity.getEtiPagina().getPagNivel());
                        rgPag.setPagLibro(entity.getEtiPagina().getPagPagina());
                        rgPag.setPagPagina(entity.getEtiPagina().getPagLibro());
                        
                        CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
                        SgRhPagina pag =  codDAO.guardar(rgPag, null);

                        entity.setEtiPagina(pag);
                    } else {
                        if (listPagina.isEmpty() && entity.getEtiPagina().getPagPk() == null) {
                            CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
                            SgRhPagina pag =  codDAO.guardar(entity.getEtiPagina(), null);
                            entity.setEtiPagina(pag);
                        }
                    }
                    CodigueraDAO<SgRhEtiqueta> codDAO = new CodigueraDAO<>(em, SgRhEtiqueta.class);
                    entity = codDAO.guardar(entity, null);
                    return entity;
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
                CodigueraDAO<SgRhEtiqueta> codDAO = new CodigueraDAO<>(em, SgRhEtiqueta.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroEtiqueta filtro) throws GeneralException {
        try {
            EtiquetaDAO codDAO = new EtiquetaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgRhEtiqueta
     * @throws GeneralException
     */
    public List<SgRhEtiqueta> obtenerPorFiltro(FiltroEtiqueta filtro) throws GeneralException {
        try {
            EtiquetaDAO codDAO = new EtiquetaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
