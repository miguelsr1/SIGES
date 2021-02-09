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
import sv.gob.mined.siges.negocio.validaciones.EstadoCivilValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoCivil;

@Stateless
public class EstadoCivilBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstadoCivil
     * @throws GeneralException
     */
    public SgEstadoCivil obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
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
                CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
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
     * @param eci SgEstadoCivil
     * @return SgEstadoCivil
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstadoCivil guardar(SgEstadoCivil eci) throws GeneralException {
        try {
            if (eci != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EstadoCivilValidacionNegocio.validar(eci)) {
                    CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
                    boolean guardar = true;
                    if (eci.getEciPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(eci.getClass(), eci.getEciPk(), eci.getEciVersion());
                        SgEstadoCivil valorAnterior = (SgEstadoCivil) valorAnt;
                        guardar = !EstadoCivilValidacionNegocio.compararParaGrabar(valorAnterior, eci);
                    }
                    if (guardar) {
                        return codDAO.guardar(eci);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eci;
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEstadoCivil 
     * @throws GeneralException
     */
    public List<SgEstadoCivil> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
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
            CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
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
                CodigueraDAO<SgEstadoCivil> codDAO = new CodigueraDAO<>(em, SgEstadoCivil.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
