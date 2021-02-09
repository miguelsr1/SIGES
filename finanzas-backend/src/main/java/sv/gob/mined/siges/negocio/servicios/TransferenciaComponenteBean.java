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
import sv.gob.mined.siges.filtros.FiltroTransferenciaComponente;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TransferenciaComponenteDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferenciaComponente;

/**
 * Servicio que gestiona las transferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class TransferenciaComponenteBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SsTransferenciaComponente
     * @throws GeneralException
     */
    public SsTransferenciaComponente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SsTransferenciaComponente> codDAO = new CodigueraDAO<>(em, SsTransferenciaComponente.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
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
                CodigueraDAO<SsTransferenciaComponente> codDAO = new CodigueraDAO<>(em, SsTransferenciaComponente.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaComponente filtro) throws GeneralException {
        try {
            TransferenciaComponenteDAO codDAO = new TransferenciaComponenteDAO(em,seguridadBean);
            
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferencia>
     * @throws GeneralException
     */
    public List<SsTransferenciaComponente> obtenerPorFiltro(FiltroTransferenciaComponente filtro) throws GeneralException {
        try {
            TransferenciaComponenteDAO  codDAO = new TransferenciaComponenteDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_TRANSFERENCIAS_COMPONENTES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
