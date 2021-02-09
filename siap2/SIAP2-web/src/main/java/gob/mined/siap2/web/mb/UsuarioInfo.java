/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "userInfo")
@SessionScoped
public class UsuarioInfo implements Serializable {

    @Inject
    protected UsuarioDelegate usuDelegate;
    private String userCod;
    private String userName;
    private SsUsuario usuario;
    private List<UnidadTecnica> unidadTecnicas = new LinkedList();
    private PoliticaContrasenia pol;

    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @PostConstruct
    public void init() {
        String username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        if (!TextUtils.isEmpty(username)) {
            SsUsuario usu = usuDelegate.obtenerSsUsuarioPorCodigo(username);
            usuario = usu;
            userName = usu.getUsuCod();
            if (usu.getUsuPrimerNombre() != null) {
                userName = usu.getUsuPrimerNombre();
            }
            if (usu.getUsuSegundoNombre() != null) {
                userName = userName + " " + usu.getUsuSegundoNombre();
            }
            if (usu.getUsuPrimerApellido() != null) {
                userName = userName + " " + usu.getUsuPrimerApellido();
            }
            if (usu.getUsuSegundoApellido() != null) {
                userName = userName + " " + usu.getUsuSegundoApellido();
            }

            userCod = usu.getUsuCod();
            unidadTecnicas = usuDelegate.getUnidadesTecniasDeUsuario(usuario.getUsuId());
            pol = usuDelegate.obtenerPoliticaContrasenia();
        }
    }

    /**
     * Retorna una lista de unidades técnicas del usuario que tienen dicha operación
     * 
     * @param opeCodigo
     * @return 
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacion(String opeCodigo) {
        return usuDelegate.getUTDeUsuarioConOperacion(usuario.getUsuId(), opeCodigo);
    }

    /**
     * Retorna las ut del usuario que tienen una operación, en un formato para utilizar en los select.
     * 
     * @param operacion
     * @return 
     */
    public Map getMapUTConOperacion(String operacion) {
        List<UnidadTecnica> usuarioUnidadTecnicas = getUTDeUsuarioConOperacion(operacion);
        Map unidades = new LinkedHashMap();
        for (UnidadTecnica u : usuarioUnidadTecnicas) {
            unidades.put(u.getNombre(), String.valueOf(u.getId()));
        }
        return unidades;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public UsuarioDelegate getUsuDelegate() {
        return usuDelegate;
    }

    public void setUsuDelegate(UsuarioDelegate usuDelegate) {
        this.usuDelegate = usuDelegate;
    }

    public SsUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SsUsuario usuario) {
        this.usuario = usuario;
    }

    public List<UnidadTecnica> getUnidadTecnicas() {
        return unidadTecnicas;
    }

    public void setUnidadTecnicas(List<UnidadTecnica> unidadTecnicas) {
        this.unidadTecnicas = unidadTecnicas;
    }

    public String getUserCod() {
        return userCod;
    }

    public void setUserCod(String userCod) {
        this.userCod = userCod;
    }

    public PoliticaContrasenia getPol() {
        return pol;
    }

    public void setPol(PoliticaContrasenia pol) {
        this.pol = pol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // </editor-fold>
}
