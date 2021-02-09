/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEmpleados;
import sv.gob.mined.siges.negocio.validaciones.EmpleadosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EmpleadosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfEmpleados;

@Stateless
public class EmpleadosBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfEmpleados
     * @throws GeneralException
     */
    public SgAfEmpleados obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfEmpleados> codDAO = new CodigueraDAO<>(em, SgAfEmpleados.class);
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
                CodigueraDAO<SgAfEmpleados> codDAO = new CodigueraDAO<>(em, SgAfEmpleados.class);
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
     * @param etn SgAfEmpleados
     * @return SgAfEmpleados
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfEmpleados guardar(SgAfEmpleados emp) throws GeneralException {
        try {
            if (emp != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EmpleadosValidacionNegocio.validar(emp)) {
                    CodigueraDAO<SgAfEmpleados> codDAO = new CodigueraDAO<>(em, SgAfEmpleados.class);
                    return codDAO.guardar(emp);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return emp;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroEmpleados
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEmpleados filtro) throws GeneralException {
        try {
            EmpleadosDAO codDAO = new EmpleadosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEmpleados
     * @return Lista de SgAfEmpleados 
     * @throws GeneralException
     */
    public List<SgAfEmpleados> obtenerPorFiltro(FiltroEmpleados filtro) throws GeneralException {
        try {
            EmpleadosDAO codDAO = new EmpleadosDAO(em);
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
                CodigueraDAO<SgAfEmpleados> codDAO = new CodigueraDAO<>(em, SgAfEmpleados.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}