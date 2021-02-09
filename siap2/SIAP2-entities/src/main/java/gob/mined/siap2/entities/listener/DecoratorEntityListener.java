/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.listener;


import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.jboss.security.SecurityContextAssociation;

/**
 *
 * @author Sofis Solutions
 */
public class DecoratorEntityListener {
    
    
    
    @PrePersist
    @PreUpdate
    public void anotar(Object entity) {
        Principal principal = SecurityContextAssociation.getPrincipal();
        DatosAuditoriaEntity datosAuditoria = new DatosAuditoriaEntity();
        String username = "SIAP2";
        if (principal != null ) {
            username = principal.getName();
        }
        try {
            datosAuditoria.registrarDatosAuditoria(entity, username, "SIAP2");
        } catch (Exception ex) {
            Logger.getLogger(DecoratorEntityListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
