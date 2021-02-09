/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Idioma;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.entities.data.impl.TextoAyuda;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.mb.ApplicationTextMB;
import gob.mined.siap2.web.mb.JSFUtils;
import static gob.mined.siap2.web.mb.codigueras.CodigueraGenerico.logger;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraTextoAyuda")
public class CodigueraTextoAyuda extends CodigueraGenerico<TextoAyuda> implements Serializable {

    private String idiIdioma;
    @Inject
    protected ApplicationTextMB applicationTextMB;

    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public String[] getPropiedades() {
        String[] propiedades = {"id", "habilitado", "codigo", "idioma"};
        return propiedades;
    }

    @Override
    public String[] getOrderBy() {
        String[] orderBy = {"codigo"};
        return orderBy;
    }

    @Override
    public boolean[] getAsc() {
        boolean[] asc = {true};
        return asc;
    }

    /**
     * Guarda el objeto en edición
     */
    @Override
    public void guardarEditando() {
        try {
            Idioma idioma = (Idioma) emd.getEntityById(Idioma.class.getName(), Integer.valueOf(idiIdioma));
            editando.setIdioma(idioma);
            editando =(TextoAyuda) emd.saveOrUpdate(editando);
            filterTable();
            applicationTextMB.limpiarCacheTextoAyuda(editando);
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Elimina un texto de ayuda
     *
     * @param idToEliminar
     */
    @Override
    public void eliminar(Integer idToEliminar) {
        TextoAyuda texto = (TextoAyuda) emd.getEntityById(TextoAyuda.class.getName(), idToEliminar);
        applicationTextMB.limpiarCacheTextoAyuda(texto);

        super.eliminar(idToEliminar);
    }

    @Override
    public void cargarToEditar(Integer id) {
        editando = (TextoAyuda) emd.getEntityById(TextoAyuda.class.getName(), id);
        idiIdioma = editando.getIdioma().getIdiId().toString();
    }

    public String getIdiIdioma() {
        return idiIdioma;
    }

    public void setIdiIdioma(String idiIdioma) {
        this.idiIdioma = idiIdioma;
    }
}
