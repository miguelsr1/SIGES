/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTransferenciaGDep;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TransferenciaGDepDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaGDep;

/**
 * Servicio que gestionas las transferencias globales departamentales.
 * @author Sofis Solutions
 */
@Stateless
public class TransferenciaGDepBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTransferenciaGDep
     * @throws GeneralException
     */
    public SgTransferenciaGDep obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTransferenciaGDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaGDep.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_GLOBAL_DEP);
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
                CodigueraDAO<SgTransferenciaGDep> codDAO = new CodigueraDAO<>(em, SgTransferenciaGDep.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_GLOBAL_DEP);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroTransferenciaGDep
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaGDep filtro) throws GeneralException {
        try {
            TransferenciaGDepDAO codDAO = new TransferenciaGDepDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_GLOBAL_DEP);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroTransferenciaGDep
     * @return Lista de <SgTransferenciaGDep>
     * @throws GeneralException
     */
    public List<SgTransferenciaGDep> obtenerPorFiltro(FiltroTransferenciaGDep filtro) throws GeneralException {
        try {
            TransferenciaGDepDAO codDAO = new TransferenciaGDepDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_GLOBAL_DEP);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }


}
