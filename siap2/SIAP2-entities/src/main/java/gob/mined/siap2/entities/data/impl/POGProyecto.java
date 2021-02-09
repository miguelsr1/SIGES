/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoPOGProyecto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pog_proyecto")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POGProyecto implements Serializable, ManejoLineaBaseTrabajo<POGProyecto> {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_proy_gen")
    @SequenceGenerator(name = "seq_pog_proy_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_proy", allocationSize = 1)
    @Column(name = "pog_id")
    private Integer id;

    @Column(name = "pog_estado")
    @Enumerated(EnumType.STRING)
    private EstadoPOGProyecto estado;
        
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_pog_rel_pog_linea",
            joinColumns = {
                @JoinColumn(name = "pog_proy")},
            inverseJoinColumns = {
                @JoinColumn(name = "pog_linea")}
    )
    @OrderColumn(name = "posicion")
    private List<POLinea> lineas;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_linea_base")
    private POGProyecto lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ver_linea_trabajo")
    private POGProyecto lineaTrabajo;
    @Column(name = "ver_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "pog_ult_usuario")
    @AtributoUltimoUsuario
    private String pogUsuario;

    @Column(name = "pog_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date pogMod;

    @Column(name = "pog_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPogUsuario() {
        return pogUsuario;
    }

    public void setPogUsuario(String pogUsuario) {
        this.pogUsuario = pogUsuario;
    }

    public Date getPogMod() {
        return pogMod;
    }

    public void setPogMod(Date pogMod) {
        this.pogMod = pogMod;
    }

    public List<POLinea> getLineas() {
        return lineas;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public EstadoPOGProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoPOGProyecto estado) {
        this.estado = estado;
    }
    

    public void setLineas(List<POLinea> lineas) {
        this.lineas = lineas;
    }

    public POGProyecto getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POGProyecto lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POGProyecto getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POGProyecto lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
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
        final POGProyecto other = (POGProyecto) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
