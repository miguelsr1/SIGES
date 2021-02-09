/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.eclipse.persistence.annotations.Customizer;

/**
 * 
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue("INSTITUCIONAL")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramaInstitucional extends Programa implements Serializable {



    //auto relacion
    @ManyToOne
    @JoinColumn(name = "pro_inst_instit")
    private ProgramaInstitucional programaInstitucional;
    @OneToMany(mappedBy = "programaInstitucional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramaInstitucional> programasInstitucionales;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public ProgramaInstitucional getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(ProgramaInstitucional programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public List<ProgramaInstitucional> getProgramasInstitucionales() {
        return programasInstitucionales;
    }

    public void setProgramasInstitucionales(List<ProgramaInstitucional> programasInstitucionales) {
        this.programasInstitucionales = programasInstitucionales;
    }

    // </editor-fold>
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProgramaInstitucional other = (ProgramaInstitucional) obj;
        if (this.getId()!= null && other.getId()!=null) {
            return this.getId().equals(other.getId());
        }
        return this == other;
    }
}
