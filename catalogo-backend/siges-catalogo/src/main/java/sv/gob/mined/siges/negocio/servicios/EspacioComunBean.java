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
import sv.gob.mined.siges.filtros.FiltroEspacioComplementario;
import sv.gob.mined.siges.negocio.validaciones.EspacioComunValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EspacioComplementarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEspacioComun;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EspacioComunBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEspacioComun
     * @throws GeneralException
     */
    public SgEspacioComun obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEspacioComun> codDAO = new CodigueraDAO<>(em, SgEspacioComun.class);
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
                CodigueraDAO<SgEspacioComun> codDAO = new CodigueraDAO<>(em, SgEspacioComun.class);
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
     * @param eco SgEspacioComun
     * @return SgEspacioComun
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEspacioComun guardar(SgEspacioComun eco) throws GeneralException {
        try {
            if (eco != null) {
                if (EspacioComunValidacionNegocio.validar(eco)) {
                    CodigueraDAO<SgEspacioComun> codDAO = new CodigueraDAO<>(em, SgEspacioComun.class);
                   
                        return codDAO.guardar(eco);
              
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eco;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEspacioComplementario filtro) throws GeneralException {
        try {
            EspacioComplementarioDAO codDAO = new EspacioComplementarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEspacioComun>
     * @throws GeneralException
     */
    public List<SgEspacioComun> obtenerPorFiltro(FiltroEspacioComplementario filtro) throws GeneralException {
        try {
            EspacioComplementarioDAO codDAO = new EspacioComplementarioDAO(em);
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
                CodigueraDAO<SgEspacioComun> codDAO = new CodigueraDAO<>(em, SgEspacioComun.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
