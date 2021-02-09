package uy.com.sofis.pfea.mb;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import uy.com.sofis.pfea.entities.Configuracion;
import uy.com.sofis.pfea.sb.ConfiguracionSB;

@Named
@ApplicationScoped
public class ConfigMB implements Serializable {
    
    @Inject
    private ConfiguracionSB configuracionSB;
    private static final Logger logger = Logger.getLogger(ConfigMB.class.getName());
    private Map<String, String> cacheText = new HashMap<String, String>();
    private static final String CKEDITOR_TOOLBAR = "[['Source','RemoveFormat','-', 'Cut','Copy','Paste','PasteText','PasteFromWord','Undo','Redo', 'Find','Replace', '-','Bold','Italic','Underline','Strike','Styles','Format','Font','FontSize', 'TextColor','BGColor', '-','NumberedList','BulletedList','-','Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','Link','Unlink','Anchor','-','Image','HorizontalRule']]";
    private static final String PAGINADOR_TEMPLATE_SIMPLE = "{FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}";
    private static final String PAGINADOR_TEMPLATE_SIN_EXPORTADOR = "Elementos por p\u00e1gina: {RowsPerPageDropdown} {FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink}";
    private static final String PAGINADOR_TEMPLATE_CON_EXPORTADOR = "Elementos por p\u00e1gina: {RowsPerPageDropdown} {FirstPageLink} {PreviousPageLink} {CurrentPageReport} {NextPageLink} {LastPageLink} {Exporters}";
    private static final String PAGINADOR_TEMPLATE_PAGINA_ACTUAL = "P\u00e1gina {currentPage} de {totalPages}";

    @PostConstruct
    public void init() {
        
        List<Configuracion> configuraciones = configuracionSB.obtenerTodasLasConfiguraciones();
        for (Configuracion txt : configuraciones) {
            cacheText.put(txt.getCnfCodigo(), txt.getCnfValor());
        }
        cacheText.put(null, "");
        cacheText.put("", "");
    }

    public String obtenerConfiguracion(String codigo) {
        String txt = cacheText.get(codigo);
        if (txt == null) {
            txt = configuracionSB.obtenerConfiguracionPorCodigos(codigo);
            cacheText.put(codigo, txt);
        }
        return txt;
    }

    public String obtenerConfiguracion(String codigo, Object obj) {
        return obtenerConfiguracion(codigo);
    }

    public String obtenerConfiguracion(String codigo, Integer param) {
        String txt = obtenerConfiguracion(codigo);
        txt = MessageFormat.format(txt, String.valueOf(param));
        return txt;
    }

    public String obtenerConfiguracionConParams(String codigo, String[] params) {
        String txt = obtenerConfiguracion(codigo);
        txt = MessageFormat.format(txt, params);
        return txt;
    }

    public void actualizaCache(String codigo, String valor) {
        cacheText.put(codigo, valor);
    }

    public void eliminaConfiguracionDelCache(String codigo) {
        cacheText.remove(codigo);
    }

    public String getCkEditorToolbar() {
        return CKEDITOR_TOOLBAR;
    }

    public String getPaginadorTemplateSimple() {
        return PAGINADOR_TEMPLATE_SIMPLE;
    }

    public String getPaginadorTemplateSinExportador() {
        return PAGINADOR_TEMPLATE_SIN_EXPORTADOR;
    }

    public String getPaginadorTemplateConExportador() {
        return PAGINADOR_TEMPLATE_CON_EXPORTADOR;
    }

    public String getPaginadorTemplatePaginaActual() {
        return PAGINADOR_TEMPLATE_PAGINA_ACTUAL;
    }
    
}