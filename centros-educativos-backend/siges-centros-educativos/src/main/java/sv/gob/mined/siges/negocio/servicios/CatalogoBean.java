/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.catalogo.FiltroCargoRol;
import sv.gob.mined.siges.filtros.catalogo.FiltroMotivoRetiro;
import sv.gob.mined.siges.filtros.catalogo.FiltroTipoAcuerdo;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.CargoRolDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.MotivoRetiroDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.TipoAcuerdoDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargoRoles;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoInasistencia;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAcuerdo;

@Stateless
@Traced
public class CatalogoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCasoViolacion
     * @return Lista de SgCasoViolacion
     * @throws GeneralException
     */
    public List<SgCargoRoles> obtenerPorFiltroCargoRol(FiltroCargoRol filtro) throws GeneralException {
        try {
            CargoRolDAO codDAO = new CargoRolDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @CacheResult(cacheName = Constantes.MOTIVOS_RETIRO_CACHE)
    public List<SgMotivoRetiro> buscarMotivoRetiro(FiltroMotivoRetiro filtro) throws GeneralException {
        try {
            MotivoRetiroDAO motDAO = new MotivoRetiroDAO(em);
            return motDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @CacheResult(cacheName = Constantes.MOTIVOS_INASISTENCIA_CACHE)
    public List<SgMotivoInasistencia> buscarMotivoInasistencia(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivoInasistencia> codDAO = new CodigueraDAO<>(em, SgMotivoInasistencia.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public List<SgTipoAcuerdo> buscarTipoAcuerdo(FiltroTipoAcuerdo filtro) throws GeneralException {
        try {
            TipoAcuerdoDAO codDAO = new TipoAcuerdoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
