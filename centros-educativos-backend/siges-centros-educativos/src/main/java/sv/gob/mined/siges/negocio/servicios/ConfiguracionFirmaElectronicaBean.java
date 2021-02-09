/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracionFirmaElectronica;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConfiguracionFirmaElectronicaBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgConfiguracionFirmaElectronica>
     * @throws GeneralException
     */
    public List<SgConfiguracionFirmaElectronica> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
        /**
     * Devuelve elemento dado el codigo
     *
     * @param codigo String
     * @return SgConfiguracion
     * @throws GeneralException
     */
    public SgConfiguracionFirmaElectronica obtenerPorCodigo(String codigo) throws GeneralException {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(codigo);
            List<SgConfiguracionFirmaElectronica> conf = this.obtenerPorFiltro(fc);
            if (!conf.isEmpty()) {
                return conf.get(0);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }


}
