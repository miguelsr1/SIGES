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
import sv.gob.mined.siges.negocio.validaciones.PropietariosTerrenoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPropietariosTerreno;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class PropietariosTerrenoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPropietariosTerreno
     * @throws GeneralException
     */
    public SgPropietariosTerreno obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
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
                CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
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
     * @param pdt SgPropietariosTerreno
     * @return SgPropietariosTerreno
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPropietariosTerreno guardar(SgPropietariosTerreno pdt) throws GeneralException {
        try {
            if (pdt != null) {
                if (PropietariosTerrenoValidacionNegocio.validar(pdt)) {
                    CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
                    boolean guardar = true;
                    if (pdt.getPdtPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(pdt.getClass(), pdt.getPdtPk() , pdt.getPdtVersion());
                        SgPropietariosTerreno valorAnterior = (SgPropietariosTerreno) valorAnt;
                        guardar = !PropietariosTerrenoValidacionNegocio.compararParaGrabar(valorAnterior, pdt);
                    }
                    if (guardar) {
                        return codDAO.guardar(pdt);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pdt;
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
            CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPropietariosTerreno>
     * @throws GeneralException
     */
    public List<SgPropietariosTerreno> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
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
                CodigueraDAO<SgPropietariosTerreno> codDAO = new CodigueraDAO<>(em, SgPropietariosTerreno.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
