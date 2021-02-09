/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.constantes.togled;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class ToggleBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(ToggleBean.class.getName());
    
    private String tablaId;
    private Map<String, Boolean> colVisibilityMap = new HashMap();
    private Map<Integer, String> colIndexMap = new HashMap();

    @PostConstruct
    public void init() {
        try {
            if(tablaId != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                DataTable table = (DataTable) context.getViewRoot().findComponent(tablaId);
                List<UIColumn> columns = table.getColumns();
                for (int i = 0; i < columns.size(); i++) {
                    final String columnId = this.getColumnId(columns.get(i).getClientId());
                    colIndexMap.put(i, columnId);
                    colVisibilityMap.put(columnId, true);
                }
            } else {
                throw  new Exception("El Id de la tabla no puede ser nulo...");
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void onToggle(ToggleEvent e) {
        this.colVisibilityMap.put(this.colIndexMap.get((Integer) e.getData()), e.getVisibility() == Visibility.VISIBLE);
    }
    
    public Map<String, Boolean> getColVisibilityMap() {
        return Collections.unmodifiableMap(colVisibilityMap);
    }

    private String getColumnId(String fullId) {
        String[] idParts = fullId.split(":");
        return idParts[idParts.length - 1];
    }
    
    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }
    
}
