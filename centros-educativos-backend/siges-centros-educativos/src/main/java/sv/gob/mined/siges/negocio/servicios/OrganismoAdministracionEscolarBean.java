/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.dto.SgOAEyMiembros;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.negocio.validaciones.OrganismoAdministracionEscolarValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.RelOAEIdentificacionOAEValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.OrganismoAdministrativoEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgItemsEvaluarOAE;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolarLite;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.persistencia.entidades.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class OrganismoAdministracionEscolarBean {

    private static final Logger LOGGER = Logger.getLogger(OrganismoAdministracionEscolarBean.class.getName());
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private RelOAEIdentificacionOAEBean relIdentificacionOAE;

    @Inject
    private PersonaOrganismoAdministracionBean personaOrganismoAdministracionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOrganismoAdministracionEscolar
     * @throws GeneralException
     */
    public SgOrganismoAdministracionEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
                SgOrganismoAdministracionEscolar entidad = codDAO.obtenerPorId(id, null);
                if (entidad != null && entidad.getItemsEvaluarOAE() != null) {
                    entidad.getItemsEvaluarOAE().size();
                }
                if(entidad!=null && entidad.getOaeMiembrosRenovadoPadreFk()!=null){
                    entidad.getOaeMiembrosRenovadoPadreFk().getOaePk();
                }
                return entidad;
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
                CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
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
     * @param oae SgOrganismoAdministracionEscolar
     * @return SgOrganismoAdministracionEscolar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgOrganismoAdministracionEscolar guardar(SgOrganismoAdministracionEscolar oae) throws GeneralException {
        try {
            if (oae != null) {
                if (OrganismoAdministracionEscolarValidacionNegocio.validar(oae)) {
                    if (oae.getOaePk() != null && EnumEstadoOrganismoAdministrativo.APROBADO.equals(oae.getOaeEstado())) {
                        if (oae.getOaeMiembrosRenovadoPadreFk() != null) {
                            SgOrganismoAdministracionEscolarLite orgLite = oae.getOaeMiembrosRenovadoPadreFk();
                            orgLite.setOaeEstado(EnumEstadoOrganismoAdministrativo.RENOVADO);
                            CodigueraDAO<SgOrganismoAdministracionEscolarLite> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolarLite.class);
                            orgLite = codDAO.guardar(orgLite, null);
                            oae.setOaeMiembrosRenovadoPadreFk(orgLite);
                        }
                    }

                    if (oae.getOaeIdentificaciones() != null && !oae.getOaeIdentificaciones().isEmpty()) {

                        for (SgRelOAEIdentificacionOAE idoae : oae.getOaeIdentificaciones()) {
                            if (!RelOAEIdentificacionOAEValidacionNegocio.validar(idoae)) {
                                return oae;
                            }
                        }
                    }

                    CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
                    oae = codDAO.guardar(oae, null);
                    return oae;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return oae;
    }

    @Interceptors(AuditInterceptor.class)
    public SgOrganismoAdministracionEscolar guardarRenovarMiembro(SgOAEyMiembros oae) throws GeneralException {
        try {
            if (oae != null) {
                List<SgPersonaOrganismoAdministracion> miembros = oae.getListMiembros();
                if (OrganismoAdministracionEscolarValidacionNegocio.validar(oae.getOrganismo())) {

                    if (oae.getOrganismo().getOaeIdentificaciones() != null && !oae.getOrganismo().getOaeIdentificaciones().isEmpty()) {

                        for (SgRelOAEIdentificacionOAE idoae : oae.getOrganismo().getOaeIdentificaciones()) {
                            if (!RelOAEIdentificacionOAEValidacionNegocio.validar(idoae)) {
                                return oae.getOrganismo();
                            }
                        }
                    }

                    CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
                    SgOrganismoAdministracionEscolar org = codDAO.guardar(oae.getOrganismo(), null);
                    em.flush();

                    if (miembros != null && !miembros.isEmpty()) {
                        for (SgPersonaOrganismoAdministracion per : miembros) {
                            per.setPoaOrganismoAdministrativoEscolar(org);
                            per = personaOrganismoAdministracionBean.guardar(per);
                        }
                        org.setOaePersonasOrganismoAdministriativo(miembros);
                    }
                    if (oae.getOrganismo().getOaeMiembrosRenovadoPadreFk() != null && oae.getOrganismo().getOaeMiembrosRenovadoPadreFk().getOaePk() != null) {
                        List<SgItemsEvaluarOAE> listItems = obtenerItemEvaluarOae(oae.getOrganismo().getOaeMiembrosRenovadoPadreFk().getOaePk());
                        if (listItems != null && !listItems.isEmpty()) {
                           
                            for (SgItemsEvaluarOAE item : listItems) {
                                SgItemsEvaluarOAE item_nuevo=new SgItemsEvaluarOAE();
                                item_nuevo.setOaiItemFk(item.getOaiItemFk());
                                item_nuevo.setOaiOrganismoFk(org);
                                CodigueraDAO<SgItemsEvaluarOAE> codDAOItem = new CodigueraDAO<>(em, SgItemsEvaluarOAE.class);
                                codDAOItem.guardar(item_nuevo, null);
                            }
                        }
                    }

                    return org;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return oae.getOrganismo();
    }

    public List<SgItemsEvaluarOAE> obtenerItemEvaluarOae(Long fichaPk) {
        String query = "select * from "
                + "centros_educativos.sg_oae_items_evaluar spd\n"
                + "join catalogo.sg_items_evaluar_organismos dis on spd.oai_item_fk = dis.ieo_pk\n"
                + "where oai_organismo_fk = :perPk";
        javax.persistence.Query q = em.createNativeQuery(query, SgItemsEvaluarOAE.class);
        q.setParameter("perPk", fichaPk);

        List<SgItemsEvaluarOAE> objs = q.getResultList();
        return objs;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param oae SgOrganismoAdministracionEscolar
     * @return SgOrganismoAdministracionEscolar
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void enviar(Long pk) throws GeneralException {
        try {
            if (pk != null) {
                CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
                SgOrganismoAdministracionEscolar oae = em.find(SgOrganismoAdministracionEscolar.class, pk);
                oae.setOaeEstado(EnumEstadoOrganismoAdministrativo.ENVIADO);

                if (OrganismoAdministracionEscolarValidacionNegocio.validar(oae)) {
                    codDAO.guardar(oae, null);
                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroOrganismoAdministrativoEscolar filtro) throws GeneralException {
        try {
            OrganismoAdministrativoEscolarDAO codDAO = new OrganismoAdministrativoEscolarDAO(em);
            return codDAO.cantidadTotal(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgOrganismoAdministracionEscolar>
     * @throws GeneralException
     */
    public List<SgOrganismoAdministracionEscolar> obtenerPorFiltro(FiltroOrganismoAdministrativoEscolar filtro) throws GeneralException {
        try {
            OrganismoAdministrativoEscolarDAO codDAO = new OrganismoAdministrativoEscolarDAO(em);
            List<SgOrganismoAdministracionEscolar> lista = codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            if (lista != null && !lista.isEmpty()) {
                lista.forEach(o -> {
                    if (o.getItemsEvaluarOAE() != null) {
                        o.getItemsEvaluarOAE().size();
                    }
                });
                
            }

            return lista;
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
                CodigueraDAO<SgOrganismoAdministracionEscolar> codDAO = new CodigueraDAO<>(em, SgOrganismoAdministracionEscolar.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve un objeto en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return SgCuentasBancarias
     */
    public SgOrganismoAdministracionEscolar obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgOrganismoAdministracionEscolar> respuesta = reader.createQuery().forEntitiesAtRevision(SgOrganismoAdministracionEscolar.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgOrganismoAdministracionEscolar ret = respuesta.get(0);
                InitializeObjectUtils.inicializarPersonas(ret);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
