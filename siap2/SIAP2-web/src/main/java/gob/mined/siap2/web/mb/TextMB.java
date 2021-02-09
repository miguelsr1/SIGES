/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;


import gob.mined.siap2.business.ejbs.IdiomaBean;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.cache2k.Cache;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 *
 * @author Sofis Solutions
 */
@Named(value = "textMB")
@SessionScoped
public class TextMB implements Serializable {
    @Inject
    private ApplicationTextMB applicationTexts;
    
    @Inject
    private ConfiguracionDelegate configDelegate;
    @Inject
    private IdiomaBean idiBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    private Cache<String,String> cacheText;
    
    private Cache<String,String> cacheAyudaText;
    Configuracion config = null;
    private String URL_MANUAL_USUARIO;
    @PostConstruct
    public void init() {        
        cacheText = applicationTexts.obtenerCacheTextos(getIdiomaId());     
        cacheAyudaText = applicationTexts.obtenerCacheTextosAyuda(getIdiomaId());   
        config = configDelegate.obtenerCnfPorCodigo(ConstantesConfiguracion.URL_MANUAL_USUARIO);
        if(config != null) {
             URL_MANUAL_USUARIO = config.getCnfValor() != null ? config.getCnfValor().trim() : "";
        }
    }
    
    

    /**
     * Retorna el valor de un texto par un código pasado o por parámetro.
     * Si no existe el texto lo crea.
     * 
     * @param codigo
     * @return 
     */   
    public String obtenerTexto(String codigo) {
        return cacheText.get(codigo);        
    }
    
    

    /**
     * Este método retorna un texto y remplaza un parámetro entero
     * 
     * @param codigo
     * @param param
     * @return 
     */
    public String obtenerTexto(String codigo, Integer param) {
        String txt = obtenerTexto(codigo);
        txt = MessageFormat.format(txt, String.valueOf(param));
        return txt;
    }
    
    
    /**
     * Este metido retorna un texto a partir de un código y setea sus parámetros
     * 
     * @param codigo
     * @param parmas
     * @return 
     */
    public String obtenerTextoConParams(String codigo, String[] parmas) {
        String txt = obtenerTexto(codigo);
        txt = MessageFormat.format(txt, parmas);
        return txt;
    }


    

    /**
     * Retorna el valor de un texto da ayuda a partir del código.
     * Si el texto no existe lo crea.
     * 
     * @param codigo
     * @return 
     */
    public String obtenerTextoAyuda(String codigo) {
        return cacheAyudaText.get(codigo);
    }
    
    
    
    
    
    
    
    
    /**
     * Retorna el id del idioma del usuario
     * 
     * @return 
     */
    public Integer getIdiomaId() {
        Integer idiomaId = 1;
        try {
            HttpSession session = null;
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                idiomaId = (Integer) session.getAttribute("idiomaId");
            }

            if (idiomaId == null) {
                idiomaId = idiBean.obtenerIdiomaEspaniol().getIdiId();
            }
        } catch (Exception ex) {

        }
        return idiomaId;
    }

    public String getURL_MANUAL_USUARIO() {
        return URL_MANUAL_USUARIO;
    }

    public void setURL_MANUAL_USUARIO(String URL_MANUAL_USUARIO) {
        this.URL_MANUAL_USUARIO = URL_MANUAL_USUARIO;
    }
    

}
