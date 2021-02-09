/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.FuenteFinanciamiento;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraFuenteRecursos")
public class CodigueraFuenteRecursos extends CodigueraGenerico<FuenteRecursos> implements Serializable {

    private String tmpIdFuenteFinanciamiento;

    @PostConstruct
    public void init() {
        super.init();
    }

    public String[] getPropiedades() {
        String[] propiedades = {"id", "habilitado", "orden", "codigo", "nombre", "fuenteFinanciamiento.nombre"};
        return propiedades;
    }

    public void cargarToEditar(Integer id) {
        super.cargarToEditar(id);
        if (editando.getFuenteFinanciamiento() != null) {
            tmpIdFuenteFinanciamiento = String.valueOf(editando.getFuenteFinanciamiento().getId());
        } else {
            tmpIdFuenteFinanciamiento = null;
        }
    }

    
    /**
     * Guarda el objeto en edición
     */
    @Override
    public void guardarEditando() {
        if (!TextUtils.isEmpty(tmpIdFuenteFinanciamiento)) {
            editando.setFuenteFinanciamiento((FuenteFinanciamiento) emd.getEntityById(FuenteFinanciamiento.class.getName(), Integer.valueOf(tmpIdFuenteFinanciamiento)));
        } else {
            editando.setFuenteFinanciamiento(null);
        }
        super.guardarEditando();
    }

    public String getTmpIdFuenteFinanciamiento() {
        return tmpIdFuenteFinanciamiento;
    }

    public void setTmpIdFuenteFinanciamiento(String tmpIdFuenteFinanciamiento) {
        this.tmpIdFuenteFinanciamiento = tmpIdFuenteFinanciamiento;
    }

}
