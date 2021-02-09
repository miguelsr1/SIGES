/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_metodo_adq_tarea")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class MetodoAdquisicionTarea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_metodo_adq_tarea")
    @SequenceGenerator(name = "seq_metodo_adq_tarea", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_metodo_adq_tarea", allocationSize = 1)
    @Column(name = "met_id")
    private Integer id;

    @Column(name = "met_nombre")
    private String nombre;
    
    @Column(name = "met_ini_dias")
    private Integer inicioEnDias;
    
    @Column(name = "met_dur_dias")
    private Integer duracionEnDias;
   
    @ManyToOne
    @JoinColumn(name = "met_predecesora")
    private MetodoAdquisicionTarea predecesora;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "met_tipo_tarea")
    private TipoTarea tipoTarea;

    @ManyToOne
    @JoinColumn(name="met_metodo")
    private MetodoAdquisicion metodo;
    
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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Integer getInicioEnDias() {
        return inicioEnDias;
    }

    public TipoTarea getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(TipoTarea tipoTarea) {
        this.tipoTarea = tipoTarea;
    }
    
    
    

    public void setInicioEnDias(Integer inicioEnDias) {
        this.inicioEnDias = inicioEnDias;
    }

    public Integer getDuracionEnDias() {
        return duracionEnDias;
    }

    public void setDuracionEnDias(Integer duracionEnDias) {
        this.duracionEnDias = duracionEnDias;
    }

    public MetodoAdquisicionTarea getPredecesora() {
        return predecesora;
    }

    public void setPredecesora(MetodoAdquisicionTarea predecesora) {
        this.predecesora = predecesora;
    }


    public MetodoAdquisicion getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoAdquisicion metodo) {
        this.metodo = metodo;
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
        
        final MetodoAdquisicionTarea other = (MetodoAdquisicionTarea) obj;
        if (this.id!=null || other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
         
        
        return this==obj;
    }

}
