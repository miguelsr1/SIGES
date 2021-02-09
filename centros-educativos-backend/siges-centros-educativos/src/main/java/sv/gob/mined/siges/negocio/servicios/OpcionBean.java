/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOpcion;
import sv.gob.mined.siges.negocio.validaciones.OpcionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.OpcionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgOpcion;

@Stateless
@Traced
public class OpcionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOpcion
     * @throws GeneralException
     */
    public SgOpcion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgOpcion> codDAO = new CodigueraDAO<>(em, SgOpcion.class);
                SgOpcion op = codDAO.obtenerPorId(id, null);
                if (op != null){
                    if (op.getOpcRelacionOpcionProgramaEdu() != null){
                        op.getOpcRelacionOpcionProgramaEdu().size();
                    }
                }
                return op;
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOpcion> codDAO = new CodigueraDAO<>(em, SgOpcion.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgOpcion
     * @return SgOpcion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgOpcion guardar(SgOpcion entity) throws GeneralException {
        try {
            if (entity != null) {
                if (OpcionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgOpcion> codDAO = new CodigueraDAO<>(em, SgOpcion.class);
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
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
                CodigueraDAO<SgOpcion> codDAO = new CodigueraDAO<>(em, SgOpcion.class);
                codDAO.eliminarPorId(id, null);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroOpcion filtro) throws GeneralException {
        try {
            OpcionDAO codDAO = new OpcionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgOpcion
     * @throws GeneralException
     */   
    public List<SgOpcion> obtenerPorFiltro(FiltroOpcion filtro) throws GeneralException {
        try {
            OpcionDAO codDAO = new OpcionDAO(em);
            List<SgOpcion> listOp=codDAO.obtenerPorFiltro(filtro);
            if(BooleanUtils.isTrue(filtro.getInicializarProgramaEducativo())){
                for(SgOpcion op: listOp){
                    if(op.getOpcRelacionOpcionProgramaEdu()!=null){
                        op.getOpcRelacionOpcionProgramaEdu().size(); 
                    }                   
                }
            }
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
