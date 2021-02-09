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
import sv.gob.mined.siges.filtros.FiltroMotivoRetiro;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.MotivoRetiroValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MotivoRetiroDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoRetiro;


@Stateless
public class MotivoRetiroBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMotivoRetiro
     * @throws GeneralException
     */
    public SgMotivoRetiro obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgMotivoRetiro> codDAO = new CodigueraDAO<>(em, SgMotivoRetiro.class);
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
                CodigueraDAO<SgMotivoRetiro> codDAO = new CodigueraDAO<>(em, SgMotivoRetiro.class);
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
     * @param mre SgMotivoRetiro
     * @return SgMotivoRetiro
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMotivoRetiro guardar(SgMotivoRetiro mre) throws GeneralException {
        try {
            if (mre != null) {
                if (MotivoRetiroValidacionNegocio.validar(mre)) {
                    CodigueraDAO<SgMotivoRetiro> codDAO = new CodigueraDAO<>(em, SgMotivoRetiro.class);
                    boolean guardar = true;
                    if (mre.getMrePk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mre.getClass(), mre.getMrePk() , mre.getMreVersion());
                        SgMotivoRetiro valorAnterior = (SgMotivoRetiro) valorAnt;
                        guardar = !MotivoRetiroValidacionNegocio.compararParaGrabar(valorAnterior, mre);
                    }
                    if (guardar) {
                        BusinessException ge = new BusinessException();
                        if(mre.getMreTraslado()){
                            FiltroMotivoRetiro fil = new FiltroMotivoRetiro();
                            fil.setExcluirCodigo(mre.getMreCodigo());
                            fil.setTraslado(Boolean.TRUE);
                            if(obtenerTotalPorFiltro(fil)>0){
                                guardar = Boolean.FALSE;
                                ge.addError(Errores.ERROR_EXISTE_MOTIVO_TRASLADO);
                                throw ge;
                            }
                        }
                        if(mre.getMreCambioSecc()){
                            FiltroMotivoRetiro fil = new FiltroMotivoRetiro();
                            fil.setExcluirCodigo(mre.getMreCodigo());
                            fil.setCambioSeccion(Boolean.TRUE);
                            if(obtenerTotalPorFiltro(fil)>0){
                                guardar = Boolean.FALSE;
                                ge.addError(Errores.ERROR_EXISTE_MOTIVO_CAMBIO_SECCION);
                                throw ge;
                            }
                        }
                        if(guardar){
                            return codDAO.guardar(mre);
                        }
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mre;
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
                CodigueraDAO<SgMotivoRetiro> codDAO = new CodigueraDAO<>(em, SgMotivoRetiro.class);
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
    public Long obtenerTotalPorFiltro(FiltroMotivoRetiro filtro) throws GeneralException {
        try {
            MotivoRetiroDAO codDAO = new MotivoRetiroDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMotivoRetiro 
     * @throws GeneralException
     */
    public List<SgMotivoRetiro> obtenerPorFiltro(FiltroMotivoRetiro filtro) throws GeneralException {
        try {
            MotivoRetiroDAO codDAO = new MotivoRetiroDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
