/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.validaciones.ProyectosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;

@Stateless
public class ProyectosBean {
    @PersistenceContext
    private EntityManager em;
    
    
    public SgAfProyectos obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfProyectos> codDAO = new CodigueraDAO<>(em, SgAfProyectos.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param etn SgAfProyectos
     * @return SgAfProyectos
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfProyectos guardar(SgAfProyectos etn) throws GeneralException {
        try {
            if (etn != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ProyectosValidacionNegocio.validar(etn)) {
                    CodigueraDAO<SgAfProyectos> codDAO = new CodigueraDAO<>(em, SgAfProyectos.class);
                    return codDAO.guardar(etn, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return etn;
    }
    
    public void actualizarLiquidado(Long id) {
        try {
            if(id != null) {
                em.createQuery("update SgAfProyectos p SET p.proProyectoLiquidado = true where p.proPk= :id")
                    .setParameter("id", id);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } 
    }
}