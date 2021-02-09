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
import sv.gob.mined.siges.filtros.FiltroDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.negocio.validaciones.DiaLectivoHorarioEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DiaLectivoHorarioEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDiaLectivoHorarioEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class DiaLectivoHorarioEscolarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDiaLectivoHorarioEscolar
     * @throws GeneralException
     */
    public SgDiaLectivoHorarioEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDiaLectivoHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgDiaLectivoHorarioEscolar.class);
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
                CodigueraDAO<SgDiaLectivoHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgDiaLectivoHorarioEscolar.class);
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
     * @param dlh SgDiaLectivoHorarioEscolar
     * @return SgDiaLectivoHorarioEscolar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDiaLectivoHorarioEscolar guardar(SgDiaLectivoHorarioEscolar dlh) throws GeneralException {
        try {
            if (dlh != null) {
                if (DiaLectivoHorarioEscolarValidacionNegocio.validar(dlh)) {
                    CodigueraDAO<SgDiaLectivoHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgDiaLectivoHorarioEscolar.class);
                    boolean guardar = true;
          
                        return codDAO.guardar(dlh,null);
               
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dlh;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDiaLectivoHorarioEscolar filtro) throws GeneralException {
        try {
            DiaLectivoHorarioEscolarDAO codDAO = new DiaLectivoHorarioEscolarDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDiaLectivoHorarioEscolar>
     * @throws GeneralException
     */
    public List<SgDiaLectivoHorarioEscolar> obtenerPorFiltro(FiltroDiaLectivoHorarioEscolar filtro) throws GeneralException {
        try {
            DiaLectivoHorarioEscolarDAO codDAO = new DiaLectivoHorarioEscolarDAO(em);
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
                CodigueraDAO<SgDiaLectivoHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgDiaLectivoHorarioEscolar.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
