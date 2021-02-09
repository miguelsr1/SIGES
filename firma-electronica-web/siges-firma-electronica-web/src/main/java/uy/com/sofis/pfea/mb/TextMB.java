
package uy.com.sofis.pfea.mb;

import uy.com.sofis.pfea.sb.TextoSB;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import uy.com.sofis.pfea.entities.Configuracion;
import uy.com.sofis.pfea.entities.Texto;
import uy.com.sofis.pfea.sb.ConfiguracionSB;



@Named
@ApplicationScoped
public class TextMB implements Serializable {
    
    
    private static final Logger LOGGER = Logger.getLogger(TextMB.class.getName());

    
    
    @Inject
    private TextoSB textSB;
    @Inject
    private ConfiguracionSB configuarcionSB;
    private final Map<String, String> cacheText = new HashMap<>();
    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void init() {
        recargarTextos();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> comprobarTextos(), 5L, 5L, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    private void comprobarTextos() {
        Configuracion conf = configuarcionSB.obtenerCnfPorCodigo("RECARGAR_TEXTOS_FRONTEND");
        if (conf != null && ("true".equalsIgnoreCase(conf.getCnfValor()) || "1".equalsIgnoreCase(conf.getCnfValor()))) {
            recargarTextos();
            conf.setCnfValor("false");
            configuarcionSB.guardar(conf);
        }
    }

    private void recargarTextos() {
        List<Texto> textos= textSB.obtenerTodosLosTextos();
        for (Texto txt : textos) {
            cacheText.put(txt.getTexCodigo(), txt.getTexValor());
        }
        cacheText.put(null, "");
        cacheText.put("", "");
    }

    public String obtenerTexto(String codigo) {
        String txt = cacheText.get(codigo);
        if (txt == null) {
            txt = textSB.obtenerTextoPorCodigos(codigo);
            cacheText.put(codigo, txt);
        }
        return txt;
    }

    public void actualizaCache(String codigo, String valor) {
        cacheText.put(codigo, valor);
    }
    
}