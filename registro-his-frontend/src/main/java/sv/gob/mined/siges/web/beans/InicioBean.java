/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.DashboardModel;
import sv.gob.mined.siges.web.dto.catalogo.SgNoticia;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InicioBean.class.getName());

    private List<SgNoticia> listaNoticias = new ArrayList();
    private DashboardModel modeloDashboard;

    public InicioBean() {
    }

    @PostConstruct
    public void init() {
    }

    public List<SgNoticia> getListaNoticias() {
        return listaNoticias;
    }

    public void setListaNoticias(List<SgNoticia> listaNoticias) {
        this.listaNoticias = listaNoticias;
    }

    public DashboardModel getModeloDashboard() {
        return modeloDashboard;
    }

    public void setModeloDashboard(DashboardModel modeloDashboard) {
        this.modeloDashboard = modeloDashboard;
    }

}
