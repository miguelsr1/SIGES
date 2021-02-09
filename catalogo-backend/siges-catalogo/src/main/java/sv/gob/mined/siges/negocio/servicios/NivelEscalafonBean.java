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
import sv.gob.mined.siges.negocio.validaciones.NivelEscalafonValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgNivelEscalafon;

@Stateless
public class NivelEscalafonBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNivelEscalafon
     * @throws GeneralException
     */
    public SgNivelEscalafon obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
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
                CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
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
     * @param ayu SgNivelEscalafon
     * @return SgNivelEscalafon
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgNivelEscalafon guardar(SgNivelEscalafon ayu) throws GeneralException {
        try {
            if (ayu != null) {
                if (NivelEscalafonValidacionNegocio.validar(ayu)) {
                    CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
                    boolean guardar = true;
                    if (ayu.getNesPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ayu.getClass(), ayu.getNesPk() , ayu.getNesVersion());
                        SgNivelEscalafon valorAnterior = (SgNivelEscalafon) valorAnt;
                        guardar = !NivelEscalafonValidacionNegocio.compararParaGrabar(valorAnterior, ayu);
                    }
                    if (guardar) {
                        return codDAO.guardar(ayu);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ayu;
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
            CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgNivelEscalafon 
     * @throws GeneralException
     */
    public List<SgNivelEscalafon> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
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
                CodigueraDAO<SgNivelEscalafon> codDAO = new CodigueraDAO<>(em, SgNivelEscalafon.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
