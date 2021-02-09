/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.dto.SgEtiqueta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEtiqueta;
import sv.gob.mined.siges.web.restclient.EtiquetaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class EtiquetaCacheBean implements Serializable {

    @Inject
    private EtiquetaRestClient restClient;

    private static final Logger LOGGER = Logger.getLogger(EtiquetaCacheBean.class.getName());
    private Map<String, String> cacheText = new HashMap();

    public EtiquetaCacheBean() {

    }

    @PostConstruct
    public void init() {
        try {
            List<SgEtiqueta> etiquetas = restClient.buscar(new FiltroEtiqueta());
            for (SgEtiqueta eti : etiquetas) {
                cacheText.put(eti.getEtiCodigo(), eti.getEtiValor());
            }
            cacheText.put(null, "");
            cacheText.put("", "");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public String obtenerEtiqueta(String codigo) {
        try {
            String txt = cacheText.get(codigo);
            if (txt == null) {
                FiltroEtiqueta filtro = new FiltroEtiqueta();
                filtro.setCodigoExacto(codigo);
                List<SgEtiqueta> etiquetas = restClient.buscar(filtro);
                if (!etiquetas.isEmpty()) {
                    txt = etiquetas.get(0).getEtiValor();
                    cacheText.put(codigo, txt);
                } else {
                    txt = "??_" + codigo + "_??";
                }
            }
            return txt;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return codigo;
        }
    }

    public String obtenerTextoConParams(String codigo, String[] parmas) {
        String txt = obtenerEtiqueta(codigo);
        txt = MessageFormat.format(txt, parmas);
        return txt;
    }

    public void actualizaCache(String codigo, String valor) {
        cacheText.put(codigo, valor);
    }

    public void eliminaTextoDelCache(String codigo) {
        cacheText.remove(codigo);
    }

}
