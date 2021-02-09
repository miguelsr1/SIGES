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
import sv.gob.mined.siges.filtros.FiltroAcuerdo;
import sv.gob.mined.siges.negocio.validaciones.AcuerdoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AcuerdoDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class AcuerdoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAcuerdo
     * @throws GeneralException
     */
    public SgAcuerdo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAcuerdo> codDAO = new CodigueraDAO<>(em, SgAcuerdo.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgAcuerdo> codDAO = new CodigueraDAO<>(em, SgAcuerdo.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param acu SgAcuerdo
     * @return SgAcuerdo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAcuerdo guardar(SgAcuerdo acu) throws GeneralException {
        try {
            if (acu != null) {
                if (AcuerdoValidacionNegocio.validar(acu)) {
                    CodigueraDAO<SgAcuerdo> codDAO = new CodigueraDAO<>(em, SgAcuerdo.class);
         
                        return codDAO.guardar(acu,null);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return acu;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAcuerdo filtro) throws GeneralException {
        try {
            AcuerdoDAO codDAO = new AcuerdoDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgAcuerdo>
     * @throws GeneralException
     */
    public List<SgAcuerdo> obtenerPorFiltro(FiltroAcuerdo filtro) throws GeneralException {
        try {
            AcuerdoDAO codDAO = new AcuerdoDAO(em);
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
                CodigueraDAO<SgAcuerdo> codDAO = new CodigueraDAO<>(em, SgAcuerdo.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
