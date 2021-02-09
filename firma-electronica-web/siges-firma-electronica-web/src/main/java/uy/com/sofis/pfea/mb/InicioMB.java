package uy.com.sofis.pfea.mb;


import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;


@Named
@ViewScoped
public class InicioMB implements Serializable {
    private static final Logger logger = Logger.getLogger(InicioMB.class.getName());

    @PostConstruct
    public void init() {
        

    }
    
    public String irABandejaPrivada() {
        return "IR_A_BANDEJA";
    }
    
    public String crearNuevoDocumentoPublico() {
        return "IR_A_CREAR_DOC_PUBLICO";
    }
    
    public String validarFirmasPublico() {
        return "IR_A_VALIDAR_FIRMAS_PUBLICO";
    }
    }