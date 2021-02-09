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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAsignacionPerfilPersonal;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.filtros.catalogo.FiltroPerfilRol;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.validaciones.AsignacionPerfilPersonalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AsignacionPerfilPersonalDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.PerfilRolDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsignacionPerfilPersonal;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPerfilRoles;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgContexto;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgRol;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioRol;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class AsignacionPerfilPersonalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private UsuarioBean usuariosBean;

    @Inject
    private PersonaBean personaBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAsignacionPerfilPersonal
     * @throws GeneralException
     */
    public SgAsignacionPerfilPersonal obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAsignacionPerfilPersonal> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfilPersonal.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgAsignacionPerfilPersonal> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfilPersonal.class);
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
     * @param app SgAsignacionPerfilPersonal
     * @return SgAsignacionPerfilPersonal
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAsignacionPerfilPersonal guardar(SgAsignacionPerfilPersonal app) throws GeneralException {
        try {
            if (app != null) {
                if (AsignacionPerfilPersonalValidacionNegocio.validar(app)) {
                    CodigueraDAO<SgAsignacionPerfilPersonal> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfilPersonal.class);
                    crearUsuarioRol(app);
                    return codDAO.guardar(app, null);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return app;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAsignacionPerfilPersonal filtro) throws GeneralException {
        try {
            AsignacionPerfilPersonalDAO codDAO = new AsignacionPerfilPersonalDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgAsignacionPerfilPersonal>
     * @throws GeneralException
     */
    public List<SgAsignacionPerfilPersonal> obtenerPorFiltro(FiltroAsignacionPerfilPersonal filtro) throws GeneralException {
        try {
            AsignacionPerfilPersonalDAO codDAO = new AsignacionPerfilPersonalDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, null);
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
                eliminarUsuarioRol(id);
                CodigueraDAO<SgAsignacionPerfilPersonal> codDAO = new CodigueraDAO<>(em, SgAsignacionPerfilPersonal.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public void crearUsuarioRol(SgAsignacionPerfilPersonal perfilPersonal) {
        try {

            if (perfilPersonal.getAppPerfilFk().getPuiPk() != null) {
                //1-Busco roles de perfil               

                FiltroPerfilRol fpr = new FiltroPerfilRol();
                fpr.setPerfilPk(perfilPersonal.getAppPerfilFk().getPuiPk());
                fpr.setIncluirCampos(new String[]{"uirRol.rolPk", "uirRol.rolVersion"});

                PerfilRolDAO perRolDAO = new PerfilRolDAO(em);
                List<SgPerfilRoles> perfilRoles = perRolDAO.obtenerPorFiltro(fpr);

                if (!perfilRoles.isEmpty()) {

                    //rolList es la lista de roles del perfil
                    List<SgRol> rolList = perfilRoles.stream().map(c -> c.getUirRol()).collect(Collectors.toList());

                    //rolListBorrarUsuarioAnterior esta lista se guarda a modo de borrarle los roles al usuario q antes tenia el perfil
                    List<SgRol> rolListBorrarUsuarioAnterior = new ArrayList(rolList);

                    FiltroPersona fp = new FiltroPersona();
                    fp.setPerPk(perfilPersonal.getAppPersonalFk().getPsePersona().getPerPk());
                    fp.setIncluirCampos(new String[]{"perUsuarioPk", "perDui", "perNip",
                        "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido", "perPk"});

                    List<SgPersona> per = personaBean.obtenerPorFiltro(fp);
                    if (!per.isEmpty()) {
                        SgUsuario usu = usuariosBean.crearObtenerUsuarioPersona(per.get(0));

                        if (usu != null) {

                            //Busco roles del usuario para ambito sede y contexto sedePk, para no dar de alta un rol q ya tiene
                            FiltroUsuarioRol fur = new FiltroUsuarioRol();
                            fur.setUsuario(usu.getUsuPk());
                            fur.setRoles(perfilRoles.stream().map(c -> c.getUirRol().getRolPk()).collect(Collectors.toList()));
                            fur.setAmbito(EnumAmbito.SEDE);
                            fur.setContexto(perfilPersonal.getAppAsignacionPerfilFk().getApeSedeFk().getSedPk());
                            fur.setIncluirCampos(new String[]{"purRol.rolPk", "purRol.rolVersion"});
                            List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(fur);

                            List<SgRol> rolesDelUsuario = usuRolList.stream().map(c -> c.getPurRol()).collect(Collectors.toList());

                            //rolList queda solo con los roles a dar de alta
                            rolList.removeAll(rolesDelUsuario);

                            //SE DAN DE ALTA LOS ROLES
                            for (SgRol rol : rolList) {
                                SgContexto contexto = new SgContexto();
                                contexto.setConAmbito(EnumAmbito.SEDE);
                                contexto.setContextoId(perfilPersonal.getAppAsignacionPerfilFk().getApeSedeFk().getSedPk());

                                SgUsuarioRol usuRol = new SgUsuarioRol();
                                usuRol.setPurUsuario(usu);
                                usuRol.setPurRol(rol);
                                usuRol.setPurContexto(contexto);
                                seguridadBean.guardarUsuarioRol(usuRol);
                            }
                        } else {
                            BusinessException be = new BusinessException();
                            be.addError("ERROR_USUARIO_VACIO");
                            throw be;
                        }
                    } else {
                        BusinessException be = new BusinessException();
                        be.addError("ERROR_PERSONA_VACIO");
                        throw be;
                    }

                    //2-BORRO ROLES DEL USUARIO Q ANTERIORMENTE TENIA EL PERFIL EN SEDE
                    if (perfilPersonal.getAppPk() != null) {
                        //rolListBorrarUsuarioAnterior tiene los roles del perfil
                        //A rolListBorrarUsuarioAnterior hay que sacarle los roles de otros perfiles en misma sede

                        //Busco a la persona que anteriormente estaba dada de alta en la relacion perfil personal
                        FiltroAsignacionPerfilPersonal fap = new FiltroAsignacionPerfilPersonal();
                        fap.setAppPk(perfilPersonal.getAppPk());
                        fap.setIncluirCampos(new String[]{"appPersonalFk.psePersona.perPk"});
                        List<SgAsignacionPerfilPersonal> listAPP = this.obtenerPorFiltro(fap);

                        if (!listAPP.isEmpty()) {
                            FiltroPersona fper = new FiltroPersona();
                            fper.setPerPk(listAPP.get(0).getAppPersonalFk().getPsePersona().getPerPk());
                            fper.setIncluirCampos(new String[]{"perUsuarioPk", "perDui", "perNip",
                                "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido", "perPk", "perUsuarioPk"});

                            List<SgPersona> personaBorrarRolesList = personaBean.obtenerPorFiltro(fper);

                            if (!personaBorrarRolesList.isEmpty()) {
                                //Busco PerfilPersonal para la persona a borrar dentro de misma sede
                                FiltroAsignacionPerfilPersonal fapp = new FiltroAsignacionPerfilPersonal();
                                fapp.setAppPersonaFk(personaBorrarRolesList.get(0).getPerPk());
                                fapp.setAppAsignacionPerfilFk(perfilPersonal.getAppAsignacionPerfilFk().getApePk());
                                fapp.setIncluirCampos(new String[]{"appPerfilFk.puiPk"});
                                List<SgAsignacionPerfilPersonal> perfilPersonalBorrar = this.obtenerPorFiltro(fapp);

                                List<SgRol> rolesADescartar = new ArrayList();
                                if (!perfilPersonalBorrar.isEmpty()) {
                                    List<Long> perfilesPk = perfilPersonalBorrar.stream().filter(c -> !c.getAppPerfilFk().getPuiPk().equals(perfilPersonal.getAppPerfilFk().getPuiPk())).map(c -> c.getAppPerfilFk().getPuiPk()).collect(Collectors.toList());
                                    if (perfilesPk != null && !perfilesPk.isEmpty()) {
                                        fpr = new FiltroPerfilRol();
                                        fpr.setPerfilesPk(perfilesPk);
                                        fpr.setIncluirCampos(new String[]{"uirRol.rolPk", "uirRol.rolVersion"});

                                        List<SgPerfilRoles> perfilRolesBorrar = perRolDAO.obtenerPorFiltro(fpr);

                                        rolesADescartar.addAll(perfilRolesBorrar.stream().map(c -> c.getUirRol()).collect(Collectors.toList()));
                                        rolListBorrarUsuarioAnterior.removeAll(rolesADescartar);
                                    }
                                }

                                if (!rolListBorrarUsuarioAnterior.isEmpty()) {
                                    FiltroUsuarioRol furBorrar = new FiltroUsuarioRol();

                                    Long usuPk = null;
                                    if (personaBorrarRolesList.get(0).getPerUsuarioPk() != null) {
                                        usuPk = personaBorrarRolesList.get(0).getPerUsuarioPk();
                                    } else {
                                        SgUsuario usu = usuariosBean.crearObtenerUsuarioPersona(personaBorrarRolesList.get(0));
                                        usuPk = usu.getUsuPk();
                                    }

                                    if (usuPk != null) {
                                        furBorrar.setUsuario(usuPk);
                                        furBorrar.setRoles(rolListBorrarUsuarioAnterior.stream().map(c -> c.getRolPk()).collect(Collectors.toList()));
                                        furBorrar.setAmbito(EnumAmbito.SEDE);
                                        furBorrar.setContexto(perfilPersonal.getAppAsignacionPerfilFk().getApeSedeFk().getSedPk());
                                        furBorrar.setIncluirCampos(new String[]{"purRol.rolPk", "purRol.rolVersion"});
                                        List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(furBorrar);

                                        if (!usuRolList.isEmpty()) {
                                            List<Long> usuarioRolBorrarPk = usuRolList.stream().map(c -> c.getPurPk()).collect(Collectors.toList());
                                            em.createQuery("DELETE FROM  SgUsuarioRol where purPk IN (:ids)")
                                                    .setParameter("ids", usuarioRolBorrarPk)
                                                    .executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                BusinessException be = new BusinessException();
                be.addError("ERROR_PERFIL_VACIO");
                throw be;
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public void eliminarUsuarioRol(Long perfilPersonalPk) {
        try {

            FiltroAsignacionPerfilPersonal fap = new FiltroAsignacionPerfilPersonal();
            fap.setAppPk(perfilPersonalPk);
            fap.setIncluirCampos(new String[]{"appPersonalFk.psePersona.perPk", "appPerfilFk.puiPk",
                "appAsignacionPerfilFk.apeSedeFk.sedPk", "appAsignacionPerfilFk.apeSedeFk.sedTipo",
                "appAsignacionPerfilFk.apePk"});
            List<SgAsignacionPerfilPersonal> listAPP = this.obtenerPorFiltro(fap);

            if (listAPP != null && !listAPP.isEmpty()) {
                //busco roles de perfil a borrar
                FiltroPerfilRol fpr = new FiltroPerfilRol();
                fpr.setPerfilPk(listAPP.get(0).getAppPerfilFk().getPuiPk());
                fpr.setIncluirCampos(new String[]{"uirRol.rolPk", "uirRol.rolVersion"});

                PerfilRolDAO perRolDAO = new PerfilRolDAO(em);
                List<SgPerfilRoles> perfilRoles = perRolDAO.obtenerPorFiltro(fpr);

                if (perfilRoles != null && !perfilRoles.isEmpty()) {
                    //LISTA DE ROLES Q HAY Q BORRAR AL USUARIO
                    //PUEDE SER Q EL USUARIO POR OTROS PERFILES DE MISMA SEDE TENGA ROLES COMPARTIDOS Q NO HAY Q BORRAR
                    List<SgRol> rolesBorrar = perfilRoles.stream().map(c -> c.getUirRol()).collect(Collectors.toList());

                    FiltroPersona fper = new FiltroPersona();
                    fper.setPerPk(listAPP.get(0).getAppPersonalFk().getPsePersona().getPerPk());
                    fper.setIncluirCampos(new String[]{"perUsuarioPk", "perDui", "perNip",
                        "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido", "perPk", "perUsuarioPk"});

                    List<SgPersona> personaBorrarRolesList = personaBean.obtenerPorFiltro(fper);

                    if (!personaBorrarRolesList.isEmpty()) {
                        //Busco PerfilPersonal para la persona a borrar dentro de misma sede
                        FiltroAsignacionPerfilPersonal fapp = new FiltroAsignacionPerfilPersonal();
                        fapp.setAppPersonaFk(personaBorrarRolesList.get(0).getPerPk());
                        fapp.setAppAsignacionPerfilFk(listAPP.get(0).getAppAsignacionPerfilFk().getApePk());
                        fapp.setIncluirCampos(new String[]{"appPk", "appPerfilFk.puiPk"});
                        List<SgAsignacionPerfilPersonal> perfilPersonalBorrar = this.obtenerPorFiltro(fapp);

                        if (!perfilPersonalBorrar.isEmpty()) {
                            List<Long> perfilesPk = perfilPersonalBorrar.stream().filter(c -> !c.getAppPk().equals(perfilPersonalPk)).map(c -> c.getAppPerfilFk().getPuiPk()).collect(Collectors.toList());
                            if (perfilesPk != null && !perfilesPk.isEmpty()) {
                                fpr = new FiltroPerfilRol();
                                fpr.setPerfilesPk(perfilesPk);
                                fpr.setIncluirCampos(new String[]{"uirRol.rolPk", "uirRol.rolVersion"});

                                List<SgPerfilRoles> perfilRolesBorrar = perRolDAO.obtenerPorFiltro(fpr);

                                List<SgRol> rolADescartar = new ArrayList(perfilRolesBorrar.stream().map(c -> c.getUirRol()).collect(Collectors.toList()));
                                rolesBorrar.removeAll(rolADescartar);
                            }
                        }

                        if (!rolesBorrar.isEmpty()) {
                            FiltroUsuarioRol furBorrar = new FiltroUsuarioRol();

                            Long usuPk = null;
                            if (personaBorrarRolesList.get(0).getPerUsuarioPk() != null) {
                                usuPk = personaBorrarRolesList.get(0).getPerUsuarioPk();
                            } else {
                                SgUsuario usu = usuariosBean.crearObtenerUsuarioPersona(personaBorrarRolesList.get(0));
                                usuPk = usu.getUsuPk();
                            }

                            if (usuPk != null) {
                                furBorrar.setUsuario(usuPk);
                                furBorrar.setRoles(rolesBorrar.stream().map(c -> c.getRolPk()).collect(Collectors.toList()));
                                furBorrar.setAmbito(EnumAmbito.SEDE);
                                furBorrar.setContexto(listAPP.get(0).getAppAsignacionPerfilFk().getApeSedeFk().getSedPk());
                                furBorrar.setIncluirCampos(new String[]{"purRol.rolPk", "purRol.rolVersion"});
                                List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(furBorrar);

                                if (!usuRolList.isEmpty()) {
                                    List<Long> usuarioRolBorrarPk = usuRolList.stream().map(c -> c.getPurPk()).collect(Collectors.toList());
                                    em.createQuery("DELETE FROM  SgUsuarioRol where purPk IN (:ids)")
                                            .setParameter("ids", usuarioRolBorrarPk)
                                            .executeUpdate();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
