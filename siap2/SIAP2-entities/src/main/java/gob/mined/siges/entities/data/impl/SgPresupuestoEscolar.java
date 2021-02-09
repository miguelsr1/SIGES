package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.enums.EstadoPresupuestoEscolar;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_presupuesto_escolar")
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgPresupuestoEscolar implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_presupuesto_escolar_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_presupuesto_escolar_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_presupuesto_escolar_pk_seq")
    @Column(name = "pes_pk")
    private Integer pk;
    
    @Column(name = "pes_codigo")
    private String codigo;
    
    @Column(name = "pes_habilitado")
    private Boolean habilitado;
    
    @Column(name = "pes_nombre")
    private String nombre;
    
    @Column(name = "pes_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "pes_descripcion")
    private String descripcion;
    
    @Column(name = "pes_estado")
    @Enumerated(EnumType.STRING)
    private EstadoPresupuestoEscolar estado;
    
    @ManyToOne
    @JoinColumn(name = "pes_sede_fk")
    private SgSede sede;
    
    @ManyToOne
    @JoinColumn(name = "pes_anio_fiscal_fk")
    private AnioFiscal anioFiscal;
    
    @Column(name = "pes_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pes_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "pes_version")
    @Version
    private Integer version;

    
    
    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoPresupuestoEscolar getEstado() {
        return estado;
    }

    public void setEstado(EstadoPresupuestoEscolar estado) {
        this.estado = estado;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PresupuestoEscolar{" + "pk=" + pk + ", codigo=" + codigo + ", habilitado=" + habilitado + ", nombre=" + nombre + ", nombreBusqueda=" + nombreBusqueda + ", descripcion=" + descripcion + ", estado=" + estado + ", sede=" + sede + ", anioFiscal=" + anioFiscal + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + '}';
    }
    
    
}
