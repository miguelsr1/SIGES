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
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_act")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pro_tipo", discriminatorType = DiscriminatorType.STRING, length = 40)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class POActividadBase implements Serializable, ManejoLineaBaseTrabajo<POActividadBase> {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_p_act_gen")
    @SequenceGenerator(name = "seq_pog_p_act_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_p_act", allocationSize = 1)
    @Column(name = "poa_id")
    private Integer id;

    @Column(name = "poa_posicion")
    private Integer posicion;

    @Column(name = "poa_ubicacionn")
    private String ubicacion;

    @Column(name = "poa_duracion_meses")
    private Integer duracion;
    
    @Column(name = "poa_estado_act")
    @Enumerated(EnumType.STRING)
    private EstadoActividadPOA estado;
    @Column(name = "poa_fec_cam_est")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCambioEstado;


    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    @OrderColumn(name = "posicion")
    private List<POInsumos> insumos;

    @ManyToOne
    @JoinColumn(name = "poa_ut_responsable")
    private UnidadTecnica utResponsable;
    
    @ManyToOne
    @JoinColumn(name = "poa_usuario_responsabe")
    private SsUsuario responsable;

    //versionado
    //funciona como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poa_linea_base")
    private POActividadBase lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poa_linea_trabajo")
    private POActividadBase lineaTrabajo;
    @Column(name = "poa_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "poa_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "poa_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "poa_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public List<POInsumos> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<POInsumos> insumos) {
        this.insumos = insumos;
    }

    public SsUsuario getResponsable() {
        return responsable;
    }

    public EstadoActividadPOA getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividadPOA estado) {
        this.estado = estado;
    }


    public Date getFechaCambioEstado() {
        return fechaCambioEstado;
    }

    public void setFechaCambioEstado(Date fechaCambioEstado) {
        this.fechaCambioEstado = fechaCambioEstado;
    }
    

    public void setResponsable(SsUsuario responsable) {
        this.responsable = responsable;
    }

    public UnidadTecnica getUtResponsable() {
        return utResponsable;
    }

    public void setUtResponsable(UnidadTecnica utResponsable) {
        this.utResponsable = utResponsable;
    }

    public POActividadBase getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POActividadBase lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POActividadBase getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POActividadBase lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public String getActUsuario() {
        return actUsuario;
    }

    public void setActUsuario(String actUsuario) {
        this.actUsuario = actUsuario;
    }

    public Date getActMod() {
        return actMod;
    }

    public void setActMod(Date actMod) {
        this.actMod = actMod;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getNombre() {
        return this.lineaTrabajo.getNombre();
    }

    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final POActividadBase other = (POActividadBase) obj;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }
}
