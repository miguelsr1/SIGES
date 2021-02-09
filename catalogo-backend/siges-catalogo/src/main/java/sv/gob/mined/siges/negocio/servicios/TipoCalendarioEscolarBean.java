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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.TipoCalendarioEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoCalendarioEscolar;

@Stateless
public class TipoCalendarioEscolarBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoCalendarioEscolar
     * @throws GeneralException
     */
    public SgTipoCalendarioEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
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
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
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
     * @param tce SgTipoCalendarioEscolar
     * @return SgTipoCalendarioEscolar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoCalendarioEscolar guardar(SgTipoCalendarioEscolar tce) throws GeneralException {
        try {
            if (tce != null) {
                if (TipoCalendarioEscolarValidacionNegocio.validar(tce)) {
                    CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
                    boolean guardar = true;
                    if (tce.getTcePk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tce.getClass(), tce.getTcePk() , tce.getTceVersion());
                        SgTipoCalendarioEscolar valorAnterior = (SgTipoCalendarioEscolar) valorAnt;
                        guardar = !TipoCalendarioEscolarValidacionNegocio.compararParaGrabar(valorAnterior, tce);
                    }
                    if (guardar) {
                        return codDAO.guardar(tce);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tce;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgTipoCalendarioEscolar 
     * @throws GeneralException
     */
    public List<SgTipoCalendarioEscolar> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
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
                CodigueraDAO<SgTipoCalendarioEscolar> codDAO = new CodigueraDAO<>(em, SgTipoCalendarioEscolar.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
