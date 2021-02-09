/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.impl.ImpuestoDelegate;
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
@Named(value = "codigueraImpuestos")
public class CodigueraImpuestos extends CodigueraGenerico<Impuesto> implements Serializable {
    
    
    @Inject
    protected ImpuestoDelegate impuestoDelegate;

    @PostConstruct
    public void init() {
        super.init();
    }

    static String[] propiedades = {"id", "habilitado", "orden", "codigo", "nombre", "tipoImpuesto", "valor"};
    @Override
    public String[] getPropiedades() {
        return propiedades;
    }
    
    
    /**
     * Guarda el objeto en edición
     */    
    @Override
    public void guardarEditando() {
        try {
            editando = impuestoDelegate.guardarImpuesto(editando);
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
}
