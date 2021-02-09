/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.persistencia.daos.PersonaDAO;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgPersona;

@Stateless
@Traced
public class PersonaBean {

    @PersistenceContext
    private EntityManager em;



    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersona
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgPersona> obtenerPorFiltro(FiltroPersona filtro) throws GeneralException {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            return DAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
