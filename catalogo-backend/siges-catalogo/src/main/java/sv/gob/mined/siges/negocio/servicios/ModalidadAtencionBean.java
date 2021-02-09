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
import sv.gob.mined.siges.filtros.FiltroModalidadAtencion;
import sv.gob.mined.siges.negocio.validaciones.ModalidadAtencionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ModalidadAtencionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgModalidadAtencion;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

@Stateless
public class ModalidadAtencionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgModalidadAtencion
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgModalidadAtencion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgModalidadAtencion> codDAO = new CodigueraDAO<>(em, SgModalidadAtencion.class);
                return codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgModalidadAtencion> codDAO = new CodigueraDAO<>(em, SgModalidadAtencion.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param mat
     * @return SgModalidadAtencion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgModalidadAtencion guardar(SgModalidadAtencion mat) throws GeneralException {
        try {
            if (mat != null) {
                if (ModalidadAtencionValidacionNegocio.validar(mat)) {
                    CodigueraDAO<SgModalidadAtencion> codDAO = new CodigueraDAO<>(em, SgModalidadAtencion.class);
                    boolean guardar = true;
                    if (mat.getMatPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mat.getClass(), mat.getMatPk() , mat.getMatVersion());
                        SgModalidadAtencion valorAnterior = (SgModalidadAtencion) valorAnt;
                        guardar = !ModalidadAtencionValidacionNegocio.compararParaGrabar(valorAnterior, mat);
                    }
                    if (guardar) {
                        return codDAO.guardar(mat);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mat;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroModalidadAtencion filtro) throws GeneralException {
        try {
            ModalidadAtencionDAO modDAO = new ModalidadAtencionDAO(em);
            return modDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgModalidadAtencion> obtenerPorFiltro(FiltroModalidadAtencion filtro) throws GeneralException {
        try {
            ModalidadAtencionDAO modDAO = new ModalidadAtencionDAO(em);
            List<SgModalidadAtencion> ret = modDAO.obtenerPorFiltro(filtro);
            if (filtro.getInicializarSubModalidades()){
                for (SgModalidadAtencion m : ret){
                    m.getMatSubmodalidades().size();
                }
            }
            return ret;
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
                CodigueraDAO<SgModalidadAtencion> codDAO = new CodigueraDAO<>(em, SgModalidadAtencion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
