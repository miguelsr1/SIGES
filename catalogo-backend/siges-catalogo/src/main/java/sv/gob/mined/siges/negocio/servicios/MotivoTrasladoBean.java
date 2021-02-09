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
import sv.gob.mined.siges.negocio.validaciones.MotivoTrasladoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoTraslado;

@Stateless
public class MotivoTrasladoBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMotivoTraslado
     * @throws GeneralException
     */
    public SgMotivoTraslado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
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
                CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
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
     * @param mot SgMotivoTraslado
     * @return SgMotivoTraslado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMotivoTraslado guardar(SgMotivoTraslado mot) throws GeneralException {
        try {
            if (mot != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (MotivoTrasladoValidacionNegocio.validar(mot)) {
                    CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
                    boolean guardar = true;
                    if (mot.getMotPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores.
                        //En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mot.getClass(), mot.getMotPk(), mot.getMotVersion());
                        SgMotivoTraslado valorAnterior = (SgMotivoTraslado) valorAnt;
                        guardar = !MotivoTrasladoValidacionNegocio.compararParaGrabar(valorAnterior, mot);
                    }
                    if (guardar) {
                        return codDAO.guardar(mot);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mot;
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
            CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMotivoTraslado 
     * @throws GeneralException
     */
    public List<SgMotivoTraslado> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
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
                CodigueraDAO<SgMotivoTraslado> codDAO = new CodigueraDAO<>(em, SgMotivoTraslado.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
