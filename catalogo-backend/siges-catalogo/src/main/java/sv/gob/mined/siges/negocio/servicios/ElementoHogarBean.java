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
import sv.gob.mined.siges.negocio.validaciones.ElementoHogarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgElementoHogar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ElementoHogarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgElementoHogar
     * @throws GeneralException
     */
    public SgElementoHogar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
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
                CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
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
     * @param eho SgElementoHogar
     * @return SgElementoHogar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgElementoHogar guardar(SgElementoHogar eho) throws GeneralException {
        try {
            if (eho != null) {
                if (ElementoHogarValidacionNegocio.validar(eho)) {
                    CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
                    boolean guardar = true;
                    if (eho.getEhoPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(eho.getClass(), eho.getEhoPk() , eho.getEhoVersion());
                        SgElementoHogar valorAnterior = (SgElementoHogar) valorAnt;
                        guardar = !ElementoHogarValidacionNegocio.compararParaGrabar(valorAnterior, eho);
                    }
                    if (guardar) {
                        return codDAO.guardar(eho);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eho;
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
            CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgElementoHogar>
     * @throws GeneralException
     */
    public List<SgElementoHogar> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
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
                CodigueraDAO<SgElementoHogar> codDAO = new CodigueraDAO<>(em, SgElementoHogar.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
