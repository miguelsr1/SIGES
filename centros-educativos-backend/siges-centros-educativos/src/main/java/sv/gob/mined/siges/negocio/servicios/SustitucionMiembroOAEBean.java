/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.enumerados.EnumEstadoSustitucionMiembroOAE;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.filtros.FiltroRelSustitucionMiembroOAE;
import sv.gob.mined.siges.filtros.FiltroSustitucionMiembroOAE;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.RelSustitucionMiembroOAEValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.SustitucionMiembroOAEValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.SustitucionMiembroOAEDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.persistencia.entidades.SgRelSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.entidades.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class SustitucionMiembroOAEBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private RelSustitucionMiembroOAEBean relSustitucionMiembroOAEBean;

    @Inject
    private PersonaOrganismoAdministracionBean personaOrganismoAdministracionBean;
    
    @Inject
    private OrganismoAdministracionEscolarBean organismoAdministracionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSustitucionMiembroOAE
     * @throws GeneralException
     */
    public SgSustitucionMiembroOAE obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
                SgSustitucionMiembroOAE sust = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SUSTITUCION_MIEMBRO_OAE);
                if (sust.getSmoRelSustitucionMiembroOAE() != null) {
                    sust.getSmoRelSustitucionMiembroOAE().size();
                }
                return sust;
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
                CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
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
     * @param smo SgSustitucionMiembroOAE
     * @return SgSustitucionMiembroOAE
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSustitucionMiembroOAE guardar(SgSustitucionMiembroOAE smo) throws GeneralException {
        try {
            if (smo != null) {
                if (SustitucionMiembroOAEValidacionNegocio.validar(smo)) {

                    List<SgRelSustitucionMiembroOAE> listRel = new ArrayList();
                    if (smo.getSmoPk() == null && smo.getSmoRelSustitucionMiembroOAE() != null) {
                        if(EnumEstadoSustitucionMiembroOAE.PENDIENTE.equals(smo.getSmoEstado())){
                            FiltroOrganismoAdministrativoEscolar foae=new FiltroOrganismoAdministrativoEscolar();
                            foae.setOaeRenovarMiembroPadre(smo.getSmoOaeFk().getOaePk());
                            foae.setIncluirCampos(new String[]{"oaeEstado"});
                            List<SgOrganismoAdministracionEscolar> list=organismoAdministracionBean.obtenerPorFiltro(foae);
                            if(list!=null &&!list.isEmpty()){
                                List<SgOrganismoAdministracionEscolar> estados=list.stream().filter(c->EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS.equals(c.getOaeEstado())
                                        ||EnumEstadoOrganismoAdministrativo.ELABORACION.equals(c.getOaeEstado())||EnumEstadoOrganismoAdministrativo.ENVIADO.equals(c.getOaeEstado())).collect(Collectors.toList());
                                if(!estados.isEmpty()){
                                    BusinessException ge = new BusinessException();
                                    ge.addError(Errores.ERROR_EXISTE_SOLICITUD_RENOVACION_MIEMBRO);
                                    throw ge;
                                }
                            }
                        }
                        
                        listRel = new ArrayList(smo.getSmoRelSustitucionMiembroOAE());
                    }
                    CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
                    smo = codDAO.guardar(smo, null);
                    if (listRel != null && !listRel.isEmpty()) {
                        FiltroRelSustitucionMiembroOAE frel=new FiltroRelSustitucionMiembroOAE();
                        frel.setRsmMiembrosSustituirPkList(listRel.stream().map(c->c.getRsmMiembroaSustituirFk().getPoaPk()).collect(Collectors.toList()));
                        frel.setRsmEstado(EnumEstadoSustitucionMiembroOAE.PENDIENTE);
                        frel.setRsmOAEPk(smo.getSmoOaeFk().getOaePk());
                        frel.setIncluirCampos(new String[]{"rsmVersion"});
                        frel.setMaxResults(1L);
                        List<SgRelSustitucionMiembroOAE> listMiembros=  relSustitucionMiembroOAEBean.obtenerPorFiltro(frel);
                        if(!listMiembros.isEmpty()){
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_YA_EXISTE_SOLICITUD_MIEMBRO);
                            throw ge;
                        }
                        
                        for (SgRelSustitucionMiembroOAE rel : listRel) {
                            if (RelSustitucionMiembroOAEValidacionNegocio.validar(rel)) {
                                rel.setRsmSustitucionMiembroFk(smo);                                
                                SgPersonaOrganismoAdministracion per = personaOrganismoAdministracionBean.guardar(rel.getRsmMiembroSustituyenteFk());
                                em.flush();
                                rel.setRsmMiembroSustituyenteFk(per);
                                rel = relSustitucionMiembroOAEBean.guardar(rel);
                                em.flush();
                            }
                        }
                    }

                    return smo;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return smo;
    }

    public SgSustitucionMiembroOAE rechazar(SgSustitucionMiembroOAE smo) throws GeneralException {
        try {
            if (smo != null) {
                if (SustitucionMiembroOAEValidacionNegocio.validarRechazo(smo)) {
                    smo.setSmoEstado(EnumEstadoSustitucionMiembroOAE.RECHAZADO);

                    CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
                    return codDAO.guardar(smo, null);
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return smo;
    }

    public SgSustitucionMiembroOAE aprobar(SgSustitucionMiembroOAE smo) throws GeneralException {
        try {
            if (smo != null) {
                if (SustitucionMiembroOAEValidacionNegocio.validarAprobacion(smo)) {
                    smo.setSmoEstado(EnumEstadoSustitucionMiembroOAE.APROBADO);

                    for (SgRelSustitucionMiembroOAE rel : smo.getSmoRelSustitucionMiembroOAE()) {
                        rel.getRsmMiembroaSustituirFk().setPoaHabilitado(Boolean.FALSE);
                        rel.getRsmMiembroaSustituirFk().setMiembroReemplazo(rel.getRsmMiembroSustituyenteFk().getPoaPk());
                        personaOrganismoAdministracionBean.guardar(rel.getRsmMiembroaSustituirFk());

                        rel.getRsmMiembroSustituyenteFk().setPoaHabilitado(Boolean.TRUE);
                        rel.getRsmMiembroSustituyenteFk().setPoaOrganismoAdministrativoEscolar(smo.getSmoOaeFk());
                        personaOrganismoAdministracionBean.guardar(rel.getRsmMiembroSustituyenteFk());
                    }

                    CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
                    return codDAO.guardar(smo, null);
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return smo;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroSustitucionMiembroOAE
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSustitucionMiembroOAE filtro) throws GeneralException {
        try {
            SustitucionMiembroOAEDAO dao = new SustitucionMiembroOAEDAO(em);
            return dao.cantidadTotal(filtro, ConstantesOperaciones.BUSCAR_SUSTITUCION_MIEMBRO_OAE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroSustitucionMiembroOAE
     * @return Lista de <SgSustitucionMiembroOAE>
     * @throws GeneralException
     */
    public List<SgSustitucionMiembroOAE> obtenerPorFiltro(FiltroSustitucionMiembroOAE filtro) throws GeneralException {
        try {
            SustitucionMiembroOAEDAO dao = new SustitucionMiembroOAEDAO(em);
            List<SgSustitucionMiembroOAE> list = dao.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SUSTITUCION_MIEMBRO_OAE);
            if (BooleanUtils.isTrue(filtro.getIncluirCantidadMiembros()) && list != null && !list.isEmpty()) {
                FiltroRelSustitucionMiembroOAE frel = new FiltroRelSustitucionMiembroOAE();
                frel.setRsmSustituirMiembroOAEList(list.stream().map(c -> c.getSmoPk()).collect(Collectors.toList()));
                frel.setIncluirCampos(new String[]{"rsmSustitucionMiembroFk.smoPk"});
                List<SgRelSustitucionMiembroOAE> listRel = relSustitucionMiembroOAEBean.obtenerPorFiltro(frel);

                for (SgSustitucionMiembroOAE sust : list) {
                    List<SgRelSustitucionMiembroOAE> elems = listRel.stream().filter(c -> c.getRsmSustitucionMiembroFk().getSmoPk().equals(sust.getSmoPk())).collect(Collectors.toList());
                    if (elems != null && !elems.isEmpty()) {
                        sust.setCantidadMiembrosaSustituir(elems.size());
                    } else {
                        sust.setCantidadMiembrosaSustituir(0);
                    }
                }
            }

            return list;
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
                CodigueraDAO<SgSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgSustitucionMiembroOAE.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
