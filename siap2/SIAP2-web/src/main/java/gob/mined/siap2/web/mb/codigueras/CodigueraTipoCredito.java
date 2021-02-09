package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.TipoCredito;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;


@ViewScoped
@Named(value = "codigueraTipoCredito")
public class CodigueraTipoCredito extends CodigueraGenerico<TipoCredito>implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }

}
