/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.EnumEstadoSustitucionMiembroOAE;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.filtros.FiltroRelSustitucionMiembroOAE;
import sv.gob.mined.siges.negocio.validaciones.PersonaOrganismoAdministracionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PersonaOrganismoAdministrativoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.persistencia.entidades.SgRelSustitucionMiembroOAE;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class PersonaOrganismoAdministracionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private RelSustitucionMiembroOAEBean relSustitucionMiembroOAEBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersonaOrganismoAdministracion
     * @throws GeneralException
     */
    public SgPersonaOrganismoAdministracion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPersonaOrganismoAdministracion> codDAO = new CodigueraDAO<>(em, SgPersonaOrganismoAdministracion.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgPersonaOrganismoAdministracion> codDAO = new CodigueraDAO<>(em, SgPersonaOrganismoAdministracion.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param poa SgPersonaOrganismoAdministracion
     * @return SgPersonaOrganismoAdministracion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPersonaOrganismoAdministracion guardar(SgPersonaOrganismoAdministracion poa) throws GeneralException {
        try {
            if (poa != null) {
                if (PersonaOrganismoAdministracionValidacionNegocio.validar(poa)) {
                    CodigueraDAO<SgPersonaOrganismoAdministracion> codDAO = new CodigueraDAO<>(em, SgPersonaOrganismoAdministracion.class);
                    poa = codDAO.guardar(poa,null);                   
                    return poa;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return poa;
    }


    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPersonaOrganismoAdministrativo filtro) throws GeneralException {
        try {
            PersonaOrganismoAdministrativoDAO codDAO = new PersonaOrganismoAdministrativoDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPersonaOrganismoAdministracion>
     * @throws GeneralException
     */
    public List<SgPersonaOrganismoAdministracion> obtenerPorFiltro(FiltroPersonaOrganismoAdministrativo filtro) throws GeneralException {
        try {
            PersonaOrganismoAdministrativoDAO codDAO = new PersonaOrganismoAdministrativoDAO(em);
            List<SgPersonaOrganismoAdministracion> listPer=codDAO.obtenerPorFiltro(filtro);
            if(BooleanUtils.isTrue(filtro.getIncluirDatoHabiltadoRemplazo()) && BooleanUtils.isTrue(filtro.getPoaHabilitado()) && listPer!=null && !listPer.isEmpty()){
                FiltroRelSustitucionMiembroOAE fre=new FiltroRelSustitucionMiembroOAE();
                fre.setRsmEstado(EnumEstadoSustitucionMiembroOAE.PENDIENTE);
                fre.setRsmMiembrosSustituirPkList(listPer.stream().map(c->c.getPoaPk()).collect(Collectors.toList()));
                fre.setIncluirCampos(new String[]{"rsmMiembroaSustituirFk.poaPk"});
                List<SgRelSustitucionMiembroOAE> relList=relSustitucionMiembroOAEBean.obtenerPorFiltro(fre);
                
                if(relList!=null && !relList.isEmpty()){
                    for(SgRelSustitucionMiembroOAE rel:relList){
                        List<SgPersonaOrganismoAdministracion> per=listPer.stream().filter(c->c.getPoaPk().equals(rel.getRsmMiembroaSustituirFk().getPoaPk())).collect(Collectors.toList());
                        if(per!=null && !per.isEmpty()){
                            per.get(0).setNoHabilitadoParaRemplazar(Boolean.TRUE);
                        }
                    }
                }
            }
            return listPer;
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
                CodigueraDAO<SgPersonaOrganismoAdministracion> codDAO = new CodigueraDAO<>(em, SgPersonaOrganismoAdministracion.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
