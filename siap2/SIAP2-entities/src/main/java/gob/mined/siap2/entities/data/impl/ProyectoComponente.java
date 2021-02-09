/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@DiscriminatorValue(value = TipoEstructura.Values.COMPONENTE)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProyectoComponente extends ProyectoEstructura implements Serializable {


    @Column(name = "pro_nombre")
    private String nombre;

    //AUTORELACION
    @ManyToOne
    @JoinColumn(name = "pro_componente_padre")
    private ProyectoComponente componentePadre;
    @OneToMany(mappedBy = "componentePadre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoComponente> componenteHijos;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public SsUsuario getResponsable() {
        return responsable;
    }

    public void setResponsable(SsUsuario responsable) {
        this.responsable = responsable;
    }


    public ProyectoComponente getComponentePadre() {
        return componentePadre;
    }

    public void setComponentePadre(ProyectoComponente componentePadre) {
        this.componentePadre = componentePadre;
    }

    public List<ProyectoComponente> getComponenteHijos() {
        return componenteHijos;
    }

    public void setComponenteHijos(List<ProyectoComponente> componenteHijos) {
        this.componenteHijos = componenteHijos;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {        
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProyectoComponente other = (ProyectoComponente) obj;
        if ((this.id!=null)&&(other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }
        return (this==other);
    }

}
