/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_metodo_adq")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class MetodoAdquisicion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_metodo_adq")
    @SequenceGenerator(name = "seq_metodo_adq", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_metodo_adq", allocationSize = 1)
    @Column(name = "met_id")
    private Integer id;

    @Column(name = "met_nombre")
    private String nombre;

    @Column(name = "met_habilitado")
    private Boolean habilitado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodo")
    private List<MetodoAdquisicionTarea> tareas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodo")
    private List<MetodoAdquisicionRango> rangos;

    @ManyToOne
    @JoinColumn(name = "met_normativa")
    private Normativa normativa;
    
    @Column(name = "met_es_uaci")
    private Boolean esUACI;

    //Auditoria
    @Column(name = "met_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "met_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "met_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<MetodoAdquisicionTarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<MetodoAdquisicionTarea> tareas) {
        this.tareas = tareas;
    }

    public String getUltUsuario() {
        return ultUsuario;
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

    public List<MetodoAdquisicionRango> getRangos() {
        return rangos;
    }

    public void setRangos(List<MetodoAdquisicionRango> rangos) {
        this.rangos = rangos;
    }

    public Normativa getNormativa() {
        return normativa;
    }

    public void setNormativa(Normativa normativa) {
        this.normativa = normativa;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public Boolean getEsUACI() {
        return esUACI;
    }

    public void setEsUACI(Boolean esUACI) {
        this.esUACI = esUACI;
    }

    // </editor-fold>   
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final MetodoAdquisicion other = (MetodoAdquisicion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
