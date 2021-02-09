/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SgOAEyMiembros implements Serializable {

    private static final long serialVersionUID = 1L;

    private SgOrganismoAdministracionEscolar organismo;

    private List<SgPersonaOrganismoAdministracion> listMiembros;


    public SgOAEyMiembros() {
    }

    public SgOrganismoAdministracionEscolar getOrganismo() {
        return organismo;
    }

    public void setOrganismo(SgOrganismoAdministracionEscolar organismo) {
        this.organismo = organismo;
    }

    public List<SgPersonaOrganismoAdministracion> getListMiembros() {
        return listMiembros;
    }

    public void setListMiembros(List<SgPersonaOrganismoAdministracion> listMiembros) {
        this.listMiembros = listMiembros;
    }


}
