/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Transient;
import sv.gob.mined.siges.web.enumerados.EnumTipoPruebaPaes;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;

/**
 *
 * @author Sofis Solutions
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpePk", scope = SgComponentePlanEstudio.class)
public class SgComponentePlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cpePk;

    private String cpeCodigo;

    private String cpeNombre;

    private String cpeNombrePublicable;

    private String cpeDescripcion;

    private Boolean cpeHabilitado;
    
    private Integer cpeCodigoExterno;

    private LocalDateTime cpeUltModFecha;

    private String cpeUltModUsuario;

    private Integer cpeVersion;

    private TipoComponentePlanEstudio cpeTipo;

    private List<SgComponentePlanGrado> cpeComponentesPlanGrado;
    
    private EnumTipoPruebaPaes cpeTipoPaes;
    
    private Boolean cpeEsPaes;
    
    @Transient
    private Double cpeMinimoAprobacion; //Mínimo aprobación de la escala del componente plan grado asociado para un estudiante dado. Utilizado en obtenerComponentesPlanEstudioNumericosActualesEstudiante


    public SgComponentePlanEstudio() {
        this.cpeHabilitado = Boolean.TRUE;
        cpeEsPaes=Boolean.FALSE;        
    }
    
    @Transient
    public String getNombreArea(){
        if(this instanceof SgAsignatura){
            SgAsignatura asig= (SgAsignatura) this;
            return asig.getAsigAreaAsignaturaModulo()!=null?asig.getAsigAreaAsignaturaModulo().getAamNombre():null;
        }
        if(this instanceof SgModulo){
            SgModulo asig= (SgModulo) this;
            return asig.getModAreaAsignaturaModulo()!=null?asig.getModAreaAsignaturaModulo().getAamNombre():null;
        }
        if(this instanceof SgArea){
            SgArea ind=(SgArea) this;
            return ind.getIndAreaIndicador()!=null?ind.getIndAreaIndicador().getAriNombre():null;
        }
        return null;
    }

    public SgComponentePlanEstudio(Long cpePk) {
        this.cpePk = cpePk;
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgComponentePlanEstudio other = (SgComponentePlanEstudio) obj;
        if (!Objects.equals(this.cpePk, other.cpePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgComponentePlanEstudio[ cpePk=" + cpePk + " ]";
    }

}
