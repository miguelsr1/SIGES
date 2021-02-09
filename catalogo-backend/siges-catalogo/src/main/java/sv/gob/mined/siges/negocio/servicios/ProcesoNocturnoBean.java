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
import sv.gob.mined.siges.negocio.validaciones.ProcesoNocturnoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgProcesoNocturno;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ProcesoNocturnoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgProcesoNocturno
     * @throws GeneralException
     */
    public SgProcesoNocturno obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
                SgProcesoNocturno procNoc=codDAO.obtenerPorId(id);
                if(procNoc.getEjecucionesProcesoNocturno()!=null){
                    procNoc.getEjecucionesProcesoNocturno().size();
                }
                return procNoc;
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
                CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
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
     * @param prn SgProcesoNocturno
     * @return SgProcesoNocturno
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgProcesoNocturno guardar(SgProcesoNocturno prn) throws GeneralException {
        try {
            if (prn != null) {
                if (ProcesoNocturnoValidacionNegocio.validar(prn)) {
                    CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
                    return codDAO.guardar(prn);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return prn;
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
            CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgProcesoNocturno>
     * @throws GeneralException
     */
    public List<SgProcesoNocturno> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
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
                CodigueraDAO<SgProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgProcesoNocturno.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}