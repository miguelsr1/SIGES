package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoNormalizable;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_cuentas", uniqueConstraints = {
    @UniqueConstraint(name = "codigo_cuentas_uk", columnNames = {"cu_codigo_busqueda","cu_anio_fiscal"})})
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Cuentas implements Serializable{
    
    private static final long serialVersionUID = 1L;

    public Cuentas() {
        this.codigoBusqueda = this.codigo != null ? this.codigo.toLowerCase().trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.trim() : "";
    }
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_cuentas", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_cuentas", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cuentas")
    @Column(name = "cu_id")
    private Integer id;
    
    @Column(name = "cu_codigo")
    @AtributoNormalizable
    private String codigo;
    
    @Column(name = "cu_codigo_busqueda")
    private String codigoBusqueda;
    
    @Column(name = "cu_nombre")
    @AtributoNormalizable
    private String nombre;
    
    @Column(name = "cu_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "cu_descripcion")
    private String descripcion;
    
    @Column(name = "cu_habilitado")
    private Boolean habilitado;
    
    @Column(name = "cu_orden")
    private Integer orden;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "cu_cuenta_padre")
    private Cuentas cuentaPadre;

    @ManyToOne
    @JoinColumn(name = "cu_anio_fiscal") 
    private AnioFiscal anioFiscal;
    
    @OneToMany(mappedBy = "cuentaPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuentas> cuentas;

    //Auditoria
    @Column(name = "cu_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cu_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "cu_version")
    @Version
    private Integer version;

    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.codigo = this.codigo != null ? this.codigo.trim() : "";
        this.codigoBusqueda = this.codigo != null ? this.codigo.toLowerCase().trim() : "";
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.trim() : "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Cuentas getCuentaPadre() {
        return cuentaPadre;
    }

    public void setCuentaPadre(Cuentas cuentaPadre) {
        this.cuentaPadre = cuentaPadre;
    }

    public List<Cuentas> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuentas> cuentas) {
        this.cuentas = cuentas;
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

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }
    
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Cuentas other = (Cuentas) obj;
        if (this.id!= null && other.getId()!=null) {
            return Objects.equals(this.id, other.getId());
        }
        return this == other;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    
    public Cuentas clone() {
        Cuentas cuenta = new Cuentas();
        cuenta.setAnioFiscal(anioFiscal);
        cuenta.setCodigo(codigo);
        cuenta.setCuentaPadre(cuentaPadre);
        cuenta.setCuentas(cuentas);
        cuenta.setDescripcion(descripcion);
        cuenta.setHabilitado(habilitado);
        cuenta.setNombre(nombre);
        cuenta.setOrden(orden);
        return cuenta;
    }

    public String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public void setCodigoBusqueda(String codigoBusqueda) {
        this.codigoBusqueda = codigoBusqueda;
    }

    @Override
    public String toString() {
        return "Cuentas{" + "id=" + id + ", codigo=" + codigo + ", codigoBusqueda=" + codigoBusqueda + ", nombre=" + nombre + ", nombreBusqueda=" + nombreBusqueda + ", descripcion=" + descripcion + ", habilitado=" + habilitado + ", orden=" + orden + ", cuentaPadre=" + cuentaPadre + ", anioFiscal=" + anioFiscal + ", cuentas=" + cuentas + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + '}';
    }
    
    
}
