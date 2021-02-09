/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.eclipse.microprofile.opentracing.Traced;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConfigAlerta;

@Stateless
@Traced
public class AlertaConfigBean {

    @PersistenceContext
    private EntityManager em;

    public List<SgConfigAlerta> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgConfigAlerta> codDAO = new CodigueraDAO<>(em, SgConfigAlerta.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param coa SgCargoOAE
     * @return SgCargoOAE
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfigAlerta guardar(SgConfigAlerta coa) throws GeneralException {
        try {
            if (coa != null) {
                CodigueraDAO<SgConfigAlerta> codDAO = new CodigueraDAO<>(em, SgConfigAlerta.class);
                return codDAO.guardar(coa, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return coa;
    }

}
