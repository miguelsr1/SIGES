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
import sv.gob.mined.siges.filtros.FiltroEspecialidad;
import sv.gob.mined.siges.negocio.validaciones.EspecialidadValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EspecialidadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEspecialidad;

@Stateless
public class EspecialidadBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEspecialidad
     * @throws GeneralException
     */
    public SgEspecialidad obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEspecialidad> codDAO = new CodigueraDAO<>(em, SgEspecialidad.class);
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
                CodigueraDAO<SgEspecialidad> codDAO = new CodigueraDAO<>(em, SgEspecialidad.class);
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
     * @param esp SgEspecialidad
     * @return SgEspecialidad
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEspecialidad guardar(SgEspecialidad esp) throws GeneralException {
        try {
            if (esp != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EspecialidadValidacionNegocio.validar(esp)) {
                    CodigueraDAO<SgEspecialidad> codDAO = new CodigueraDAO<>(em, SgEspecialidad.class);
                    boolean guardar = true;
                    if (esp.getEspPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(esp.getClass(), esp.getEspPk(), esp.getEspVersion());
                        SgEspecialidad valorAnterior = (SgEspecialidad) valorAnt;
                        guardar = !EspecialidadValidacionNegocio.compararParaGrabar(valorAnterior, esp);
                    }
                    if (guardar) {
                        return codDAO.guardar(esp);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return esp;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEspecialidad filtro) throws GeneralException {
        try {
            EspecialidadDAO codDAO = new EspecialidadDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEspecialidad 
     * @throws GeneralException
     */
    public List<SgEspecialidad> obtenerPorFiltro(FiltroEspecialidad filtro) throws GeneralException {
        try {
            EspecialidadDAO codDAO = new EspecialidadDAO(em);
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
                CodigueraDAO<SgEspecialidad> codDAO = new CodigueraDAO<>(em, SgEspecialidad.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
