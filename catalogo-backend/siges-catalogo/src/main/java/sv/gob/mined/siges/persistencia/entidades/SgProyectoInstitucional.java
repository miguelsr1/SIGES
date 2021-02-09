/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_proyectos_institucionales", uniqueConstraints = {
    @UniqueConstraint(name = "pin_codigo_uk", columnNames = {"pin_codigo"})
    ,
    @UniqueConstraint(name = "pin_nombre_uk", columnNames = {"pin_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pinPk", scope = SgProyectoInstitucional.class)
public class SgProyectoInstitucional implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pin_pk")
    private Long pinPk;
    
    @Size(max = 4)
    @Column(name = "pin_codigo",length = 4)
    @AtributoCodigo
    private String pinCodigo;
    
    @Column(name = "pin_habilitado")
    @AtributoHabilitado
    private Boolean pinHabilitado;
    
    @Size(max = 100)
    @Column(name = "pin_nombre",length = 100)
    @AtributoNormalizable
    private String pinNombre;
    
    @Size(max = 100)
    @Column(name = "pin_nombre_busqueda",length = 100)
    @AtributoNombre
    private String pinNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "pin_descripcion",length = 255)
    @AtributoDescripcion
    private String pinDescripcion;
    
    @Size(max = 100)
    @Column(name = "pin_origen_transferencia",length = 100)
    @AtributoDescripcion
    private String pinOrigenTransferencia;
    
    @Size(max = 100)
    @Column(name = "pin_convenio",length = 100)
    @AtributoDescripcion
    private String pinConvenio;
    
    @Size(max = 100)
    @Column(name = "pin_condiciones_entrega",length = 100)
    @AtributoDescripcion
    private String pinCondicionesEntrega;
    
    @Column(name = "pin_monto")
    private BigDecimal pinMonto;
    
    @Column(name = "pin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pinUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pin_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String pinUltModUsuario;
    
    @Column(name = "pin_version")
    @Version
    private Integer pinVersion;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_asociaciones_proyectos",
            schema = Constantes.SCHEMA_CATALOGO,
            joinColumns = @JoinColumn(name = "pin_pk"),
            inverseJoinColumns = @JoinColumn(name = "aso_pk"))
    private List<SgAsociacion> pinAsociaciones;

    @JoinColumn(name = "pin_programa_institucional_fk", referencedColumnName = "pin_pk")
    @ManyToOne
    private SgProgramaInstitucional pinProgramaInstitucional;
    
    @Column(name = "pin_fecha_inicio")
    private LocalDate pinFechaInicio;
    
    @Column(name = "pin_fecha_fin")
    private LocalDate pinFechaFin;
    
    @OneToMany(mappedBy = "cpnProyectoInstitucional")
    @AtributoInicializarColeccion
    private List<SgComponente> pinComponentes; 
    
    @OneToMany(mappedBy = "bnfProyectoInstitucional")
    @AtributoInicializarColeccion
    private List<SgBeneficio> pinBeneficios; 
    
    @Column(name = "pin_anio")
    private Integer pinAnio;
    
    public SgProyectoInstitucional() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pinNombre);
    }

    public Long getPinPk() {
        return pinPk;
    }

    public void setPinPk(Long pinPk) {
        this.pinPk = pinPk;
    }

    public String getPinCodigo() {
        return pinCodigo;
    }

    public void setPinCodigo(String pinCodigo) {
        this.pinCodigo = pinCodigo;
    }

    public Boolean getPinHabilitado() {
        return pinHabilitado;
    }

    public void setPinHabilitado(Boolean pinHabilitado) {
        this.pinHabilitado = pinHabilitado;
    }

    public String getPinNombre() {
        return pinNombre;
    }

    public void setPinNombre(String pinNombre) {
        this.pinNombre = pinNombre;
    }

    public String getPinNombreBusqueda() {
        return pinNombreBusqueda;
    }

    public void setPinNombreBusqueda(String pinNombreBusqueda) {
        this.pinNombreBusqueda = pinNombreBusqueda;
    }

    public LocalDateTime getPinUltModFecha() {
        return pinUltModFecha;
    }

    public void setPinUltModFecha(LocalDateTime pinUltModFecha) {
        this.pinUltModFecha = pinUltModFecha;
    }

    public String getPinUltModUsuario() {
        return pinUltModUsuario;
    }

    public void setPinUltModUsuario(String pinUltModUsuario) {
        this.pinUltModUsuario = pinUltModUsuario;
    }

    public Integer getPinVersion() {
        return pinVersion;
    }

    public void setPinVersion(Integer pinVersion) {
        this.pinVersion = pinVersion;
    }

    public String getPinDescripcion() {
        return pinDescripcion;
    }

    public void setPinDescripcion(String pinDescripcion) {
        this.pinDescripcion = pinDescripcion;
    }

    public List<SgAsociacion> getPinAsociaciones() {
        return pinAsociaciones;
    }

    public void setPinAsociaciones(List<SgAsociacion> pinAsociaciones) {
        this.pinAsociaciones = pinAsociaciones;
    }

    public Integer getPinAnio() {
        return pinAnio;
    }

    public void setPinAnio(Integer pinAnio) {
        this.pinAnio = pinAnio;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pinPk != null ? pinPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstitucional)) {
            return false;
        }
        SgProyectoInstitucional other = (SgProyectoInstitucional) object;
        if ((this.pinPk == null && other.pinPk != null) || (this.pinPk != null && !this.pinPk.equals(other.pinPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucional[ pinPk=" + pinPk + " ]";
    }

    public SgProgramaInstitucional getPinProgramaInstitucional() {
        return pinProgramaInstitucional;
    }

    public void setPinProgramaInstitucional(SgProgramaInstitucional pinProgramaInstitucional) {
        this.pinProgramaInstitucional = pinProgramaInstitucional;
    }

    public LocalDate getPinFechaInicio() {
        return pinFechaInicio;
    }

    public void setPinFechaInicio(LocalDate pinFechaInicio) {
        this.pinFechaInicio = pinFechaInicio;
    }

    public LocalDate getPinFechaFin() {
        return pinFechaFin;
    }

    public void setPinFechaFin(LocalDate pinFechaFin) {
        this.pinFechaFin = pinFechaFin;
    }

    public List<SgComponente> getPinComponentes() {
        return pinComponentes;
    }

    public void setPinComponentes(List<SgComponente> pinComponentes) {
        this.pinComponentes = pinComponentes;
    }

    public List<SgBeneficio> getPinBeneficios() {
        return pinBeneficios;
    }

    public void setPinBeneficios(List<SgBeneficio> pinBeneficios) {
        this.pinBeneficios = pinBeneficios;
    }

    public String getPinOrigenTransferencia() {
        return pinOrigenTransferencia;
    }

    public void setPinOrigenTransferencia(String pinOrigenTransferencia) {
        this.pinOrigenTransferencia = pinOrigenTransferencia;
    }

    public String getPinConvenio() {
        return pinConvenio;
    }

    public void setPinConvenio(String pinConvenio) {
        this.pinConvenio = pinConvenio;
    }

    public String getPinCondicionesEntrega() {
        return pinCondicionesEntrega;
    }

    public void setPinCondicionesEntrega(String pinCondicionesEntrega) {
        this.pinCondicionesEntrega = pinCondicionesEntrega;
    }

    public BigDecimal getPinMonto() {
        return pinMonto;
    }

    public void setPinMonto(BigDecimal pinMonto) {
        this.pinMonto = pinMonto;
    }

  
}
