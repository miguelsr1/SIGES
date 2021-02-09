/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoPruebaPaes;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAreaIndicador;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_componente_plan_estudio", uniqueConstraints = {
    @UniqueConstraint(name = "cpe_codigo_uk", columnNames = {"cpe_codigo"})}, 
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "cpe_tipo", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible=true,
        property = "cpeTipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SgActividadEspecial.class, name = TipoComponentePlanEstudio.Codes.ACTIVIDAD_ESPECIAL)
    , 
    @JsonSubTypes.Type(value = SgActividadEspecialSeccion.class, name = TipoComponentePlanEstudio.Codes.ACTIVIDAD_ESPECIAL_SECCION)
    , 
  @JsonSubTypes.Type(value = SgActividadTiempoExtendido.class, name = TipoComponentePlanEstudio.Codes.ACTIVIDAD_TIEMPO_EXTENDIDO)
        , 
  @JsonSubTypes.Type(value = SgArea.class, name = TipoComponentePlanEstudio.Codes.INDICADORES)
        , 
  @JsonSubTypes.Type(value = SgAsignatura.class, name = TipoComponentePlanEstudio.Codes.ASIGNATURA)
        , 
  @JsonSubTypes.Type(value = SgModulo.class, name = TipoComponentePlanEstudio.Codes.MODULO)
        , 
  @JsonSubTypes.Type(value = SgPrueba.class, name = TipoComponentePlanEstudio.Codes.PRUEBA)
})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgComponentePlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpe_pk")
    private Long cpePk;
    
    @Size(max = 10)
    @Column(name = "cpe_codigo",length = 10)
    @AtributoCodigo
    private String cpeCodigo;
    
    @Size(max = 255)
    @Column(name = "cpe_nombre",length = 255)
    @AtributoNormalizable
    private String cpeNombre;
    
    @Size(max = 255)
    @Column(name = "cpe_nombre_busqueda", length = 255)
    @AtributoNombre
    private String cpeNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "cpe_nombre_publicable",length = 255)
    private String cpeNombrePublicable;
    
    @Size(max = 500)
    @Column(name = "cpe_descripcion",length = 500)
    private String cpeDescripcion;
    
    @Column(name = "cpe_habilitado")
    @AtributoHabilitado
    private Boolean cpeHabilitado;
    
    @Column(name = "cpe_codigo_externo")
    private Integer cpeCodigoExterno;
    
    @Column(name = "cpe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cpeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cpe_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String cpeUltModUsuario;
    
    @Column(name = "cpe_version")
    @Version
    private Integer cpeVersion;
    
    @Column(name = "cpe_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoComponentePlanEstudio cpeTipo;
    
    @OneToMany(mappedBy = "cpgComponentePlanEstudio")
    private List<SgComponentePlanGrado> cpeComponentesPlanGrado;
    
    @Column(name = "cpe_es_paes")
    private Boolean cpeEsPaes;
    
    @Column(name = "cpe_tipo_paes")
    @Enumerated(EnumType.STRING)
    private EnumTipoPruebaPaes cpeTipoPaes;
    
    
    @Transient
    private SgAreaIndicador indAreaIndicador; //Utilizado para incluirCampos
    
    @Transient
    private SgAreaAsignaturaModulo asigAreaAsignaturaModulo; //Utilizado para incluirCampos
        
    @Transient
    private SgAreaAsignaturaModulo modAreaAsignaturaModulo; //Utilizado para incluirCampos
    
    @Transient
    private Double cpeMinimoAprobacion; //Mínimo aprobación de la escala del componente plan grado asociado para un estudiante dado. Utilizado en obtenerComponentesPlanEstudioNumericosActualesEstudiante


    public SgComponentePlanEstudio() {
    }

    public SgComponentePlanEstudio(Long cpePk) {
            this.cpePk = cpePk;
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cpeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cpeNombre);
        if (this.cpeEsPaes == null){
            this.cpeEsPaes = Boolean.FALSE;
        }
    }

    public Long getCpePk() {
        return cpePk;
    }

    public void setCpePk(Long cpePk) {
        this.cpePk = cpePk;
    }

    public String getCpeCodigo() {
        return cpeCodigo;
    }

    public void setCpeCodigo(String cpeCodigo) {
        this.cpeCodigo = cpeCodigo;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public String getCpeNombreBusqueda() {
        return cpeNombreBusqueda;
    }

    public void setCpeNombreBusqueda(String cpeNombreBusqueda) {
        this.cpeNombreBusqueda = cpeNombreBusqueda;
    }    
    
    public String getCpeNombrePublicable() {
        return cpeNombrePublicable;
    }

    public void setCpeNombrePublicable(String cpeNombrePublicable) {
        this.cpeNombrePublicable = cpeNombrePublicable;
    }

    public String getCpeDescripcion() {
        return cpeDescripcion;
    }

    public void setCpeDescripcion(String cpeDescripcion) {
        this.cpeDescripcion = cpeDescripcion;
    }

    public Boolean getCpeHabilitado() {
        return cpeHabilitado;
    }

    public void setCpeHabilitado(Boolean cpeHabilitado) {
        this.cpeHabilitado = cpeHabilitado;
    }

    public LocalDateTime getCpeUltModFecha() {
        return cpeUltModFecha;
    }

    public void setCpeUltModFecha(LocalDateTime cpeUltModFecha) {
        this.cpeUltModFecha = cpeUltModFecha;
    }

    public String getCpeUltModUsuario() {
        return cpeUltModUsuario;
    }

    public void setCpeUltModUsuario(String cpeUltModUsuario) {
        this.cpeUltModUsuario = cpeUltModUsuario;
    }

    public Integer getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeVersion(Integer cpeVersion) {
        this.cpeVersion = cpeVersion;
    }

    public TipoComponentePlanEstudio getCpeTipo() {
        return cpeTipo;
    }

    public void setCpeTipo(TipoComponentePlanEstudio cpeTipo) {
        this.cpeTipo = cpeTipo;
    }

    public List<SgComponentePlanGrado> getCpeComponentesPlanGrado() {
        return cpeComponentesPlanGrado;
    }

    public void setCpeComponentesPlanGrado(List<SgComponentePlanGrado> cpeComponentesPlanGrado) {
        this.cpeComponentesPlanGrado = cpeComponentesPlanGrado;
    }

    public Integer getCpeCodigoExterno() {
        return cpeCodigoExterno;
    }

    public void setCpeCodigoExterno(Integer cpeCodigoExterno) {
        this.cpeCodigoExterno = cpeCodigoExterno;
    }

    public Boolean getCpeEsPaes() {
        return cpeEsPaes;
    }

    public void setCpeEsPaes(Boolean cpeEsPaes) {
        this.cpeEsPaes = cpeEsPaes;
    }

    public SgAreaIndicador getIndAreaIndicador() {
        return indAreaIndicador;
    }

    public void setIndAreaIndicador(SgAreaIndicador indAreaIndicador) {
        this.indAreaIndicador = indAreaIndicador;
    }

    public SgAreaAsignaturaModulo getAsigAreaAsignaturaModulo() {
        return asigAreaAsignaturaModulo;
    }

    public void setAsigAreaAsignaturaModulo(SgAreaAsignaturaModulo asigAreaAsignaturaModulo) {
        this.asigAreaAsignaturaModulo = asigAreaAsignaturaModulo;
    }

    public SgAreaAsignaturaModulo getModAreaAsignaturaModulo() {
        return modAreaAsignaturaModulo;
    }

    public void setModAreaAsignaturaModulo(SgAreaAsignaturaModulo modAreaAsignaturaModulo) {
        this.modAreaAsignaturaModulo = modAreaAsignaturaModulo;
    }

    public EnumTipoPruebaPaes getCpeTipoPaes() {
        return cpeTipoPaes;
    }

    public void setCpeTipoPaes(EnumTipoPruebaPaes cpeTipoPaes) {
        this.cpeTipoPaes = cpeTipoPaes;
    }

    public Double getCpeMinimoAprobacion() {
        return cpeMinimoAprobacion;
    }

    public void setCpeMinimoAprobacion(Double cpeMinimoAprobacion) {
        this.cpeMinimoAprobacion = cpeMinimoAprobacion;
    }
     
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpePk != null ? cpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgComponentePlanEstudio)) {
            return false;
        }
        SgComponentePlanEstudio other = (SgComponentePlanEstudio) object;
        if ((this.cpePk == null && other.cpePk != null) || (this.cpePk != null && !this.cpePk.equals(other.cpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio[ cpePk=" + cpePk + " ]";
    }
    
}
