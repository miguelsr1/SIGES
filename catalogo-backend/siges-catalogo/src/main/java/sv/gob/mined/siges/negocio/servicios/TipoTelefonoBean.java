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
import sv.gob.mined.siges.negocio.validaciones.TipoTelefonoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoTelefono;

@Stateless
public class TipoTelefonoBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoTelefono
     * @throws GeneralException
     */
    public SgTipoTelefono obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
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
                CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
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
     * @param tto SgTipoTelefono
     * @return SgTipoTelefono
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoTelefono guardar(SgTipoTelefono tto) throws GeneralException {
        try {
            if (tto != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (TipoTelefonoValidacionNegocio.validar(tto)) {
                    CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
                    boolean guardar = true;
                    if (tto.getTtoPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(tto.getClass(), tto.getTtoPk(), tto.getTtoVersion());
                        SgTipoTelefono valorAnterior = (SgTipoTelefono) valorAnt;
                        guardar = !TipoTelefonoValidacionNegocio.compararParaGrabar(valorAnterior, tto);
                    }
                    if (guardar) {
                        return codDAO.guardar(tto);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tto;
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
            CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgTipoTelefono 
     * @throws GeneralException
     */
    public List<SgTipoTelefono> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
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
                CodigueraDAO<SgTipoTelefono> codDAO = new CodigueraDAO<>(em, SgTipoTelefono.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
