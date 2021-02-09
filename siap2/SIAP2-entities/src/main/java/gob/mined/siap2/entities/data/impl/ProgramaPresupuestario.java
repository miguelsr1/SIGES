/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */

@Entity
@DiscriminatorValue("PRESUPUESTARIO")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramaPresupuestario extends Programa  implements Serializable{
    
    @ManyToOne
    @JoinColumn(name="pro_clasificador_f")
    private ClasificadorFuncional clasificadorFuncional;
    
    
    
    @ManyToMany
    @JoinTable(
      schema = Constantes.SCHEMA_SIAP2,
      name="ss_rel_prog_p_prog_i",
      joinColumns={@JoinColumn(name="pp_id")},
      inverseJoinColumns={@JoinColumn(name="pi_id")})
    private List<ProgramaInstitucional> programasInstitucionales;

    /**
     * los techos globales
     */
    @OneToMany(mappedBy = "programaPresupuestario", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "indice")
    private List<ProgramaPresupuestarioTechoAnio> techosPresupuestarios;
        
    /**
     * los techos actuales del programa
     */
    @OneToMany(mappedBy = "programaPresupuestario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechoProgramaPresupueastarioFuente> techos;
    
    //auto relacion
    @ManyToOne
    @JoinColumn(name="pro_pre_presupuest")
    private ProgramaPresupuestario programaPresupuestario;
    @OneToMany(mappedBy="programaPresupuestario",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramaPresupuestario> programasPresupuestarios;

    /**
     * la lista de los proyectos asociados al programa presupuestario
     */
    @Transient
    private List<Proyecto> proyectos = new LinkedList<>();
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

   
    public ProgramaPresupuestario getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(ProgramaPresupuestario programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public List<ProgramaInstitucional> getProgramasInstitucionales() {
        return programasInstitucionales;
    }

    public List<ProgramaPresupuestarioTechoAnio> getTechosPresupuestarios() {
        return techosPresupuestarios;
    }

    public void setTechosPresupuestarios(List<ProgramaPresupuestarioTechoAnio> techosPresupuestarios) {
        this.techosPresupuestarios = techosPresupuestarios;
    }

    public void setProgramasInstitucionales(List<ProgramaInstitucional> programasInstitucionales) {
        this.programasInstitucionales = programasInstitucionales;
    }

    public ClasificadorFuncional getClasificadorFuncional() {
        return clasificadorFuncional;
    }

    public void setClasificadorFuncional(ClasificadorFuncional clasificadorFuncional) {
        this.clasificadorFuncional = clasificadorFuncional;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
    

    public List<TechoProgramaPresupueastarioFuente> getTechos() {
        return techos;
    }

    public void setTechos(List<TechoProgramaPresupueastarioFuente> techos) {
        this.techos = techos;
    }

    public List<ProgramaPresupuestario> getProgramasPresupuestarios() {
        return programasPresupuestarios;
    }
    

    public void setProgramasPresupuestarios(List<ProgramaPresupuestario> programasPresupuestarios) {
        this.programasPresupuestarios = programasPresupuestarios;
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
        final ProgramaPresupuestario other = (ProgramaPresupuestario) obj;
        if (this.getId()!= null && other.getId()!=null) {
            return Objects.equals(this.getId(), other.getId());
        }
        return this == other;
    }
    
    
}
