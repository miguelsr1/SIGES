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
import sv.gob.mined.siges.negocio.validaciones.ImpresoraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgImpresora;

@Stateless
public class ImpresorasBean {
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgImpresora
     * @throws GeneralException
     */
    public SgImpresora obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
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
                CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
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
     * @param etn SgImpresora
     * @return SgImpresora
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgImpresora guardar(SgImpresora etn) throws GeneralException {
        try {
            if (etn != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ImpresoraValidacionNegocio.validar(etn)) {
                    CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
                    return codDAO.guardar(etn);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return etn;
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
            CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEtnia 
     * @throws GeneralException
     */
    public List<SgImpresora> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
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
                CodigueraDAO<SgImpresora> codDAO = new CodigueraDAO<>(em, SgImpresora.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}

