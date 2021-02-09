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
import sv.gob.mined.siges.negocio.validaciones.InstitucionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgInstitucion;

@Stateless
public class InstitucionBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgInstitucion
     * @throws GeneralException
     */
    public SgInstitucion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
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
                CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
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
     * @param ins SgInstitucion
     * @return SgInstitucion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgInstitucion guardar(SgInstitucion ins) throws GeneralException {
        try {
            if (ins != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (InstitucionValidacionNegocio.validar(ins)) {
                    CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
                    boolean guardar = true;
                    if (ins.getInsPk()!= null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores.
                        //En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ins.getClass(), ins.getInsPk(), ins.getInsVersion());
                        SgInstitucion valorAnterior = (SgInstitucion) valorAnt;
                        guardar = !InstitucionValidacionNegocio.compararParaGrabar(valorAnterior, ins);
                    }
                    if (guardar) {
                        return codDAO.guardar(ins);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ins;
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
            CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgInstitucion 
     * @throws GeneralException
     */
    public List<SgInstitucion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
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
                CodigueraDAO<SgInstitucion> codDAO = new CodigueraDAO<>(em, SgInstitucion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
