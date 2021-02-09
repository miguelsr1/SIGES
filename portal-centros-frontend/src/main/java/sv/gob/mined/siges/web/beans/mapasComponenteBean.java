/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import sv.gob.mined.siges.web.dto.SgDireccion;

@Named
@ViewScoped
public class mapasComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(mapasComponenteBean.class.getName());

    private SgDireccion direccion = new SgDireccion();
    private MapModel mapaModel;

    private String urlMapa;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mapaModel = new DefaultMapModel();

    }

    public void actualizar(SgDireccion dir) {
        direccion = dir;
        mostrarMapa();
    }

    public void mostrarMapa() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath).append("/mapa/mapa.html?");

        urlMapa = url.toString();
        if (direccion != null && direccion.getDirLatitud() != null && direccion.getDirLongitud() != null) {
            urlMapa = urlMapa + "lat=" + direccion.getDirLatitud() + "&lon=" + direccion.getDirLongitud();
        }
    }

    public String getUrlMapa() {
        if (urlMapa == null) {
            mostrarMapa();
        }
        return urlMapa;
    }

    public SgDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(SgDireccion direccion) {
        this.direccion = direccion;
    }

    public MapModel getMapaModel() {
        return mapaModel;
    }

    public void setMapaModel(MapModel mapaModel) {
        this.mapaModel = mapaModel;
    }


}
