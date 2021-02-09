/*
 * Sofis Solutions 2015.
 */
package sv.gob.mined.siges.web.lazymodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgNominaDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

public class LazyNominaDocenteDataModel extends LazyDataModel<SgNominaDocente> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LazyNominaDocenteDataModel.class.getName());
    private static final long serialVersionUID = 1L;
    private PersonalSedeEducativaRestClient restClient;
    private FiltroPersonalSedeEducativa filtro;

    public LazyNominaDocenteDataModel(PersonalSedeEducativaRestClient restClient, FiltroPersonalSedeEducativa filtro, Long rowCount, Integer pageSize) {
        this.restClient = restClient;
        this.filtro = filtro;
        this.setRowCount(rowCount.intValue());
        this.setPageSize(pageSize);
    }


    @Override
    public Object getRowKey(SgNominaDocente c) {
        return null;
    }

    @Override
    public List<SgNominaDocente> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            filtro.setFirst(new Long(first));
            filtro.setMaxResults(new Long(pageSize));
            if (sortField != null) {
                filtro.setOrderBy(new String[]{sortField});
                filtro.setAscending(new boolean[]{sortOrder.equals(SortOrder.ASCENDING)});
            }
            return restClient.buscarNominaDocente(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            return new ArrayList<>();
        }
    }

    public void setFiltro(FiltroPersonalSedeEducativa filtro) {
        this.filtro = filtro;
    }

}
