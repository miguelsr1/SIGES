package com.sofis.persistence.listener;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author Sofis Solutions
 */
public class DecoratorEntityListener {
    
    
    
    @PrePersist
    @PreUpdate
    public void anotar(Object entity) {
        DatosAuditoriaEntity datosAuditoria = new DatosAuditoriaEntity();
        String username = "PFEA";
        try {
            datosAuditoria.registrarDatosAuditoria(entity, username, "PFEA");
        } catch (Exception ex) {
            Logger.getLogger(DecoratorEntityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
