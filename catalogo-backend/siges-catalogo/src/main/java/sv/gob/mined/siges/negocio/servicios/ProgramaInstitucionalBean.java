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
import sv.gob.mined.siges.negocio.validaciones.ProgramaInstitucionalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgProgramaInstitucional;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ProgramaInstitucionalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgProgramaInstitucional
     * @throws GeneralException
     */
    public SgProgramaInstitucional obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
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
                CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
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
     * @param pin SgProgramaInstitucional
     * @return SgProgramaInstitucional
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgProgramaInstitucional guardar(SgProgramaInstitucional pin) throws GeneralException {
        try {
            if (pin != null) {
                if (ProgramaInstitucionalValidacionNegocio.validar(pin)) {
                    CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
                    boolean guardar = true;
                    if (pin.getPinPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(pin.getClass(), pin.getPinPk() , pin.getPinVersion());
                        SgProgramaInstitucional valorAnterior = (SgProgramaInstitucional) valorAnt;
                        guardar = !ProgramaInstitucionalValidacionNegocio.compararParaGrabar(valorAnterior, pin);
                    }
                    if (guardar) {
                        return codDAO.guardar(pin);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pin;
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
            CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgProgramaInstitucional>
     * @throws GeneralException
     */
    public List<SgProgramaInstitucional> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
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
                CodigueraDAO<SgProgramaInstitucional> codDAO = new CodigueraDAO<>(em, SgProgramaInstitucional.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
