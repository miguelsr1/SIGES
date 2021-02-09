/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgAfp;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.enumerados.centros_educativos.TipoPersonalSedeEducativa;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "pseTipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SgAdministrativo.class, name = TipoPersonalSedeEducativa.Codes.ADMINISTRATIVO)
    ,@JsonSubTypes.Type(value = SgAtpi.class, name = TipoPersonalSedeEducativa.Codes.ASISTENTE_TECNICO_PRIMERA_INFANCIA)
    ,@JsonSubTypes.Type(value = SgDocente.class, name = TipoPersonalSedeEducativa.Codes.DOCENTE)
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "psePk", scope = SgPersonalSedeEducativa.class)
public abstract class SgPersonalSedeEducativa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long psePk;

    private String pseCodigo;
    private SgPersona psePersona;
    private SgEscolaridad pseEscolaridad;
    private Boolean pseAccesoInternet;
    private Boolean pseAccesoEquipoInformatico;
    private SgPersona psePersonaContacto;
    private SgAfp pseAfp;
    private Boolean psePensionado;
    private LocalDateTime pseUltModFecha;
    private String pseUltModUsuario;
    private Integer pseVersion;
    private TipoPersonalSedeEducativa pseTipo;
    private SgDatoEmpleado pseDatoEmpleado;
    
    public SgPersonalSedeEducativa() {
        this.psePersona = new SgPersona();
    }

    public SgPersonalSedeEducativa(Long psePk) {
        this.psePk = psePk;
    }

    public Long getPsePk() {
        return psePk;
    }

    public void setPsePk(Long psePk) {
        this.psePk = psePk;
    }

    public String getPseCodigo() {
        return pseCodigo;
    }

    public void setPseCodigo(String pseCodigo) {
        this.pseCodigo = pseCodigo;
    }

    public Boolean getPseAccesoInternet() {
        return pseAccesoInternet;
    }

    public void setPseAccesoInternet(Boolean pseAccesoInternet) {
        this.pseAccesoInternet = pseAccesoInternet;
    }

    public Boolean getPseAccesoEquipoInformatico() {
        return pseAccesoEquipoInformatico;
    }

    public void setPseAccesoEquipoInformatico(Boolean pseAccesoEquipoInformatico) {
        this.pseAccesoEquipoInformatico = pseAccesoEquipoInformatico;
    }

    public Boolean getPsePensionado() {
        return psePensionado;
    }

    public void setPsePensionado(Boolean psePensionado) {
        this.psePensionado = psePensionado;
    }

    public LocalDateTime getPseUltModFecha() {
        return pseUltModFecha;
    }

    public void setPseUltModFecha(LocalDateTime pseUltModFecha) {
        this.pseUltModFecha = pseUltModFecha;
    }

    public String getPseUltModUsuario() {
        return pseUltModUsuario;
    }

    public void setPseUltModUsuario(String pseUltModUsuario) {
        this.pseUltModUsuario = pseUltModUsuario;
    }

    public Integer getPseVersion() {
        return pseVersion;
    }

    public void setPseVersion(Integer pseVersion) {
        this.pseVersion = pseVersion;
    }

    public SgEscolaridad getPseEscolaridad() {
        return pseEscolaridad;
    }

    public void setPseEscolaridad(SgEscolaridad pseEscolaridad) {
        this.pseEscolaridad = pseEscolaridad;
    }

    public SgAfp getPseAfp() {
        return pseAfp;
    }

    public void setPseAfp(SgAfp pseAfp) {
        this.pseAfp = pseAfp;
    }

    public SgPersona getPsePersona() {
        return psePersona;
    }

    public void setPsePersona(SgPersona psePersona) {
        this.psePersona = psePersona;
    }

    public SgPersona getPsePersonaContacto() {
        return psePersonaContacto;
    }

    public void setPsePersonaContacto(SgPersona psePersonaContacto) {
        this.psePersonaContacto = psePersonaContacto;
    }

    public TipoPersonalSedeEducativa getPseTipo() {
        return pseTipo;
    }

    public void setPseTipo(TipoPersonalSedeEducativa pseTipo) {
        this.pseTipo = pseTipo;
    }

    public SgDatoEmpleado getPseDatoEmpleado() {
        return pseDatoEmpleado;
    }

    public void setPseDatoEmpleado(SgDatoEmpleado pseDatoEmpleado) {
        this.pseDatoEmpleado = pseDatoEmpleado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psePk != null ? psePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPersonalSedeEducativa)) {
            return false;
        }
        SgPersonalSedeEducativa other = (SgPersonalSedeEducativa) object;
        if ((this.psePk == null && other.psePk != null) || (this.psePk != null && !this.psePk.equals(other.psePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa[ psePk=" + psePk + " ]";
    }

}
