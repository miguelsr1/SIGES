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
import sv.gob.mined.siges.negocio.validaciones.AlcanceCapacitacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAlcanceCapacitacion;

@Stateless
public class AlcanceCapacitacionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAlcanceCapacitacion
     * @throws GeneralException
     */
    public SgAlcanceCapacitacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
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
                CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
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
     * @param aca SgAlcanceCapacitacion
     * @return SgAlcanceCapacitacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAlcanceCapacitacion guardar(SgAlcanceCapacitacion aca) throws GeneralException {
        try {
            if (aca != null) {
                if (AlcanceCapacitacionValidacionNegocio.validar(aca)) {
                    CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
                    boolean guardar = true;
                    if (aca.getAcaPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(aca.getClass(), aca.getAcaPk() , aca.getAcaVersion());
                        SgAlcanceCapacitacion valorAnterior = (SgAlcanceCapacitacion) valorAnt;
                        guardar = !AlcanceCapacitacionValidacionNegocio.compararParaGrabar(valorAnterior, aca);
                    }
                    if (guardar) {
                        return codDAO.guardar(aca);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return aca;
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
                CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
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
            CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgAlcanceCapacitacion 
     * @throws GeneralException
     */
    public List<SgAlcanceCapacitacion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgAlcanceCapacitacion> codDAO = new CodigueraDAO<>(em, SgAlcanceCapacitacion.class);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
