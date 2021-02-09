/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsOficina;
import gob.mined.siap2.entities.data.SsRol;
import gob.mined.siap2.entities.data.SsUsuOfiRoles;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "usuariosConsulta")
public class UsuariosConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    private UsuarioDelegate usuarioDelagate;
    @Inject
    private TextMB textMB;
    @Inject
    private UsuarioInfo usuarioInfo;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String filtroCodigo;
    private String filtroNombre1;
    private String filtroApellido1;
    private String filtroIdRol;
    private String filtroEstado;
    private String filtroOperacion;

    private final static String ESTADO_USUARIO_BLOQUEADO = "BLOQUEADO";
    private final static String ESTADO_USUARIO_DESBLOQUEADO = "DESBLOQUEADO";

    private HashMap<String, String> oficinas;
    private HashMap<String, String> roles;
    private String selectedOficina = null;
    private String selectedRol = null;

    private String passwd = null;

    private List<SsUsuOfiRoles> listaUsuOfiRoles = new ArrayList<>();
    private Boolean usuarioAdminOtrosRoles = Boolean.FALSE;

    private TreeNode selectedNode;

    /**
     * Roles administrados por el usuario (Solo se cargan valores si es
     * subadministrador) En caso de ser administrador general, no se cargan
     * roles administrados porque los administra todos.
     */
    private List<SsRol> listaRolesADM = new ArrayList<>();

    @PostConstruct
    public void init() {

        loadOficinas();
        loadRoles();
        filterTable();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        filtroCodigo = null;
        filtroNombre1 = null;
        filtroApellido1 = null;
        filtroIdRol = null;
        filtroEstado = null;
        filtroOperacion = null;
    }

    /**
     * Carga todas las oficinas
     */
    private void loadOficinas() {
        List<SsOficina> l = emd.getEntities(SsOficina.class.getName());
        oficinas = new HashMap<>();
        if (l != null) {
            for (SsOficina item : l) {
                oficinas.put(item.getOfiNombre(), String.valueOf(item.getOfiId()));
            }
        }
    }

    /**
     * Carga los roles disponibles a asignar
     */
    private void loadRoles() {
        List<SsRol> l = emd.getEntities(SsRol.class.getName(), "rolNombre");
        roles = new LinkedHashMap<>();
        if (l != null) {
            if (usuarioInfo.getUsuario().getUsuAdmGeneral() == null || usuarioInfo.getUsuario().getUsuAdmGeneral()) {//Cargo todos los roles
                for (SsRol item : l) {
                    roles.put(item.getRolNombre(), String.valueOf(item.getRolId()));
                }
            } else {//Cargo solo los roles que administra
                for (SsRol item : l) {
                    if (usuarioInfo.getUsuario().getRolesAdministrados().contains(item)) {
                        roles.put(item.getRolNombre(), String.valueOf(item.getRolId()));
                    }
                }
            }

        }
    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (!TextUtils.isEmpty(filtroCodigo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "usuCod", filtroCodigo);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroNombre1)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "usuPrimerNombre", filtroNombre1);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroApellido1)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "usuPrimerApellido", filtroApellido1);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroIdRol)) { //Si es sub administrador los roles que le apareceran seran los que él administre
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "ssUsuOfiRolesCollection.usuOfiRolesRol.rolId", Integer.valueOf(filtroIdRol));
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroEstado)) {
                if (ESTADO_USUARIO_BLOQUEADO.equals(filtroEstado)) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuCuentaBloqueada", true);
                    criterios.add(criterio);
                } else if (ESTADO_USUARIO_DESBLOQUEADO.equals(filtroEstado)) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuCuentaBloqueada", false);
                    criterios.add(criterio); 
                }
            }

            //Si no ingresó un rol para filtrar
            if (TextUtils.isEmpty(filtroIdRol)) {
                //-- Si es subadministrador solo puede ver los usuarios con los roles que administra
                if (usuarioInfo.getUsuario().getUsuAdmGeneral() != null && !usuarioInfo.getUsuario().getUsuAdmGeneral()) {
                    List<CriteriaTO> criteriosRoles = new ArrayList<CriteriaTO>();
                    for (Map.Entry<String, String> entry : roles.entrySet()) {
                        String nombreRol = entry.getKey();
                        String idRol = entry.getValue();
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ssUsuOfiRolesCollection.usuOfiRolesRol.rolId", Integer.valueOf(idRol));
                        criteriosRoles.add(criterio);                     

                    }
                    CriteriaTO criterioOR = CriteriaTOUtils.createORTO(criteriosRoles.toArray(new CriteriaTO[0]));
                    criterios.add(criterioOR);
                    //Solo puede ver los creados por el
                    //MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    //MatchCriteriaTO.types.EQUALS, "usuCreadoPor", usuarioInfo.getUsuario().getUsuCod());
                    //criterios.add(criterio);
                }

            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios
                            .toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "usuId", 1);
            }

            String[] propiedades = {"usuId", "usuCod", "usuPrimerNombre", "usuPrimerApellido", "usuCorreoElectronico","usuTelefono", "usuDesde", "usuHasta", "usuCuentaBloqueada"};

            String className = SsUsuario.class.getName();
            String[] orderBy = {"usuCod"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);

            lazyModel = new GeneralLazyDataModel(dataProvider);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    private SsUsuario editando = new SsUsuario();

    /**
     * Carga el usuario a editar
     *
     * @param id
     */
    public void cargarToEditar(Integer id) {
        listaUsuOfiRoles = new LinkedList<>();
        if (id != null && id.intValue() != 0) {
            editando = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), id);

            List<SsUsuOfiRoles> lista = usuarioDelagate.getUsuOfiRoles(id);
            if (lista != null && !lista.isEmpty()) {
                listaUsuOfiRoles = lista;
            }

        } else {
            crearUsuario();
        }
    }

    /**
     * Crea un usuario nuevo
     */
    public void crearUsuario() {
        editando = new SsUsuario();
        editando.setUsuCambioPassword(true);
        editando.setUsuAdmGeneral(false);
        editando.setUsuCuentaBloqueada(false);
        selectedOficina = null;
        selectedRol = null;
    }

    /**
     * Cuando cambia el valor del checkbox de administrador general se limpian
     * los roles,y ya no es administrador general
     *
     * @param event
     */
    public void onCheckBoxUsuAdmGeneralChanged(ValueChangeEvent event) {
        Boolean esAdmGeneral = (Boolean) event.getNewValue();
        if (esAdmGeneral) {
            usuarioAdminOtrosRoles = false;
            listaRolesADM.clear();
        }
    }

    /**
     * Cuando cambia el valor del checkbox de administrador general se limpian
     * los roles.
     *
     * @param event
     */
    public void onCkeckBoxUsuAdmOtrosRolesChanged(ValueChangeEvent event) {
        Boolean esAdmOtrosRoles = (Boolean) event.getNewValue();
        if (!esAdmOtrosRoles) {
            listaRolesADM.clear();
        }
    }

    /**
     * Carga un usuario para editar
     *
     * @param id
     */
    public void cargarToADM(Integer id) {
        if (id != null && id.intValue() != 0) {
            editando = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), id);
            listaRolesADM = editando.getRolesAdministrados();
            if (listaRolesADM != null && !listaRolesADM.isEmpty()) {
                usuarioAdminOtrosRoles = Boolean.TRUE;
            } else {
                usuarioAdminOtrosRoles = Boolean.FALSE;
            }
        } else {
            crearUsuario();
            listaRolesADM = new ArrayList<>();
        }
    }

    /**
     * Carga un usuario para cambiar la password
     *
     * @param id
     */
    public void cargarToCambioPassword(Integer id) {
        if (id != null) {
            editando = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), id);
            passwd = null;
        }

    }

    /**
     * Guarda los roles
     */
    public void guardarRolesADM() {
        try {
            editando.setRolesAdministrados(listaRolesADM);
            emd.saveOrUpdate(editando);
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#admModal').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }

    }

    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        if (guardarEditandoGenerico()) {
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        }
    }

    public void guardarEditandoRoles() {
        if (guardarEditandoGenerico()) {
            RequestContext.getCurrentInstance().execute("$('#editModalRol').modal('hide');");
        }

    }

    /**
     * Guarda un usuario
     *
     * @return
     */
    public boolean guardarEditandoGenerico() {
        try {

            //Validaciones
            List<String> mensajesError = validarGuardar();
            if (!mensajesError.isEmpty()) {
                jSFUtils.mostrarErrorByPropertieCode(mensajesError);
                return false;
            }

            editando.setSsUsuOfiRolesCollection(listaUsuOfiRoles);
            editando = usuarioDelagate.guardarUsuario(editando);

            filterTable();
            return true;

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            return false;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            return false;
        }
    }

    /**
     * Cambia la contraseña del usuario en edición
     */
    public void guardarCambioPassword() {
        try {
            //Validaciones
            List<String> mensajesError = validarCambioPassword();
            if (!mensajesError.isEmpty()) {
                jSFUtils.mostrarErrorByPropertieCode(mensajesError);
                return;
            }

            usuarioDelagate.cambiarContrasenia(editando.getUsuCod(), passwd);
            RequestContext.getCurrentInstance().execute("$('#passwdModal').modal('hide');");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public List<SsUsuOfiRoles> getListaUsuOfiRoles() {
        return listaUsuOfiRoles;
    }

    public void setListaUsuOfiRoles(List<SsUsuOfiRoles> listaUsuOfiRoles) {
        this.listaUsuOfiRoles = listaUsuOfiRoles;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public String getESTADO_USUARIO_BLOQUEADO() {
        return ESTADO_USUARIO_BLOQUEADO;
    }

    public String getESTADO_USUARIO_DESBLOQUEADO() {
        return ESTADO_USUARIO_DESBLOQUEADO;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public String getFiltroCodigo() {
        return filtroCodigo;
    }

    public void setFiltroCodigo(String filtroCodigo) {
        this.filtroCodigo = filtroCodigo;
    }

    public HashMap<String, String> getOficinas() {
        return oficinas;
    }

    public void setOficinas(HashMap<String, String> oficinas) {
        this.oficinas = oficinas;
    }

    public HashMap<String, String> getRoles() {
        return roles;
    }

    public Boolean getUsuarioAdminOtrosRoles() {
        return usuarioAdminOtrosRoles;
    }

    public void setUsuarioAdminOtrosRoles(Boolean usuarioAdminOtrosRoles) {
        this.usuarioAdminOtrosRoles = usuarioAdminOtrosRoles;
    }

    public String getFiltroEstado() {
        return filtroEstado;
    }

    public void setFiltroEstado(String filtroEstado) {
        this.filtroEstado = filtroEstado;
    }

    public void setRoles(HashMap<String, String> roles) {
        this.roles = roles;
    }

    public String getSelectedOficina() {
        return selectedOficina;
    }

    public void setSelectedOficina(String selectedOficina) {
        this.selectedOficina = selectedOficina;
    }

    public String getSelectedRol() {
        return selectedRol;
    }

    public void setSelectedRol(String selectedRol) {
        this.selectedRol = selectedRol;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFiltroIdRol() {
        return filtroIdRol;
    }

    public void setFiltroIdRol(String filtroIdRol) {
        this.filtroIdRol = filtroIdRol;
    }

    public String getFiltroNombre1() {
        return filtroNombre1;
    }

    public void setFiltroNombre1(String filtroNombre1) {
        this.filtroNombre1 = filtroNombre1;
    }

    public String getFiltroApellido1() {
        return filtroApellido1;
    }

    public void setFiltroApellido1(String filtroApellido1) {
        this.filtroApellido1 = filtroApellido1;
    }

    public SsUsuario getEditando() {
        return editando;
    }

    public void setEditando(SsUsuario editando) {
        this.editando = editando;
    }

    public List<SsRol> getListaRolesADM() {
        return listaRolesADM;
    }

    public void setListaRolesADM(List<SsRol> listaRolesADM) {
        this.listaRolesADM = listaRolesADM;
    }

    public String getFiltroOperacion() {
        return filtroOperacion;
    }

    public void setFiltroOperacion(String filtroOperacion) {
        this.filtroOperacion = filtroOperacion;
    }

    private List<String> validarGuardar() {
        List<String> respuesta = new ArrayList<>();
        if (editando.getUsuCod() == null || editando.getUsuCod().trim().length() == 0) {
            respuesta.add("ERROR.FaltaCodigoUsuario");
        }
        if (editando.getUsuPrimerNombre() == null || editando.getUsuPrimerNombre().trim().length() == 0) {
            respuesta.add("ERROR.FaltaPrimerNombre");
        }
        if (editando.getUsuPrimerApellido() == null || editando.getUsuPrimerApellido().trim().length() == 0) {
            respuesta.add("ERROR.FaltaPrimerApellido");
        }
        return respuesta;
    }

    private List<String> validarCambioPassword() {
        List<String> respuesta = new ArrayList<>();
        if (passwd == null || passwd.trim().length() == 0) {
            respuesta.add("ERROR.FaltaIngresarPasswd");
        }

        return respuesta;
    }

    public void removeUsuOfiRoles(Integer rolId, Integer utId) {
        for (int i = 0; i < listaUsuOfiRoles.size(); i++) {
            SsUsuOfiRoles usuOfiRol = listaUsuOfiRoles.get(i);
            if (utId == null || utId.intValue() == 0) {//Busco solo por rol
                if (usuOfiRol.getUsuOfiRolesUnidadTecnica() == null
                        && usuOfiRol.getUsuOfiRolesRol().getRolId().equals(rolId)) {
                    listaUsuOfiRoles.remove(i);
                    break;
                }
            } else if (usuOfiRol.getUsuOfiRolesUnidadTecnica() != null
                    && usuOfiRol.getUsuOfiRolesUnidadTecnica().getId().intValue() == utId.intValue()
                    && usuOfiRol.getUsuOfiRolesRol().getRolId().equals(rolId)) {
                listaUsuOfiRoles.remove(i);
                break;
            }
        }
    }

    public void removeRol(Integer rolId) {
        for (int i = 0; i < listaRolesADM.size(); i++) {
            SsRol rol = listaRolesADM.get(i);
            if (rol.getRolId().equals(rolId)) {
                listaRolesADM.remove(i);
                break;
            }
        }
    }

    public void agregarOficinaRol() {
        if (TextUtils.isEmpty(selectedRol)) {
            jSFUtils.mostrarErrorByPropertieCode("Error.DebeSeleccionarRol");
            return;
        }
        UnidadTecnica ut = null;
        if (getSelectedNode() != null) {//Si selecciono UT,valido que el rol no este ingresado con esa UT.
            ut = (UnidadTecnica) getSelectedNode().getData();
            for (SsUsuOfiRoles usuOfiRol : listaUsuOfiRoles) {
                if (usuOfiRol.getUsuOfiRolesUnidadTecnica() != null) {
                    if (usuOfiRol.getUsuOfiRolesUnidadTecnica().getId().intValue() == ut.getId().intValue()
                            && usuOfiRol.getUsuOfiRolesRol().getRolId().intValue() == new Integer(selectedRol).intValue()) {
                        jSFUtils.mostrarErrorByPropertieCode("Error.UnidadTecnicaRolYaFueIngresado");
                        return;
                    }
                }
            }
        } else {//Cuando no se selecciona UT, valido que el rol no se haya ingresado sin UT
            for (SsUsuOfiRoles usuOfiRol : listaUsuOfiRoles) {
                if (usuOfiRol.getUsuOfiRolesUnidadTecnica() == null) {
                    if (usuOfiRol.getUsuOfiRolesRol().getRolId().intValue() == new Integer(selectedRol).intValue()) {
                        jSFUtils.mostrarErrorByPropertieCode("Error.RolYaFueIngresado");
                        return;
                    }
                }
            }
        }

        SsRol rol = (SsRol) emd.getEntityById(SsRol.class.getName(), new Integer(selectedRol));
        SsOficina oficina = (SsOficina) emd.getEntityById(SsOficina.class.getName(), new Integer(1));
        SsUsuOfiRoles usuOfiRol = new SsUsuOfiRoles();
        usuOfiRol.setUsuOfiRolesOficina(oficina);
        usuOfiRol.setUsuOfiRolesRol(rol);
        usuOfiRol.setUsuOfiRolesUsuario(editando);
        usuOfiRol.setUsuOfiRolesOrigen("SAPI");
        usuOfiRol.setUsuOfiRolesUserCode(1);
        usuOfiRol.setUsuOfiRolesUnidadTecnica(ut);

        listaUsuOfiRoles.add(usuOfiRol);
        //TODO: Cuando se de de alta un usuOfiRol con una unidad tecnica, hay que crear usuOfiRol por los hijos
    }

    public void agregarRolADM() {
        if (TextUtils.isEmpty(selectedRol)) {
            jSFUtils.mostrarErrorByPropertieCode("Error.DebeSeleccionarRol");
            return;
        }
        //Controlo que ya no este
        for (SsRol rol : listaRolesADM) {
            if (rol.getRolId().intValue() == new Integer(selectedRol).intValue()) {
                jSFUtils.mostrarErrorByPropertieCode("Error.RolYaFueIngresado");
                return;
            }
        }
        SsRol rol = (SsRol) emd.getEntityById(SsRol.class.getName(), new Integer(selectedRol));
        listaRolesADM.add(rol);
    }

    public Boolean getRenderCheckADM() {
        if (usuarioInfo.getUsuario().getUsuAdmGeneral() != null && usuarioInfo.getUsuario().getUsuAdmGeneral()) {
            return true;
        } else {
            return false;
        }
    }
}
