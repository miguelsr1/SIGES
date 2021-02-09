/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Transient;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "psePk", scope = SgPersonalSedeEducativa.class)
public class SgPersonalSedeEducativa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long psePk;

    private String pseCodigo;

    private SgPersona psePersona;

    private Boolean pseAccesoInternet;

    private Boolean pseAccesoEquipoInformatico;

    private SgPersona psePersonaContacto;

    private Boolean psePensionado;

    private SgNivelEscalafon pseNivelEscalafon;

    private SgCategoriaEscalafon pseCategoriaEscalafon;

    private LocalDateTime pseUltModFecha;

    private String pseUltModUsuario;

    private Integer pseVersion;

    private SgDatoEmpleado pseDatoEmpleado;

    private SgEstudioRealizado pseEstudioRealizado;

    private List<SgFormacionDocente> pseFormacionDocente;
    
    private List<SgRelPersonalEspecialidad> pseRelEspecialidades;
    
    private Integer pseAnioServicio;
    
    @Transient
    private Boolean psePuedeAplicarPlaza; //Utilizado para cuando se da de alta desde registro escalafonario. En el backend se guarda este dato en la entidad DatoEmpleado
    
    @Transient
    private SgDatoContratacion pseCrearConDatoContratacion; //Utilizado para cuando se da de alta desde ficha. En el backend se guarda este dato en la entidad DatoEmpleado

    public SgPersonalSedeEducativa() {
        this.psePersona = new SgPersona();
    }

    public SgPersonalSedeEducativa(Long psePk, Integer pseVersion) {
        this.psePk = psePk;
        this.pseVersion = pseVersion;
    }

    @JsonIgnore
    public String getEspecialidadesNombres() {
        if (this.pseRelEspecialidades != null) {
            return this.pseRelEspecialidades.stream()
                    .filter(d -> d.getRpeEspecialidad() != null)
                    .map(d -> d.getRpeEspecialidad().getEspNombre())
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return null;
    }


    @JsonIgnore
    public String getRelEspecialidades() {
        if (this.pseRelEspecialidades != null) {
            return this.pseRelEspecialidades.stream().map(r -> r.getRelCompleto()).collect(Collectors.joining(", "));
        }
        return null;
    }
    

    @JsonIgnore
    public String getPseNombreCompleto() {
        return this.psePersona.getPerNombreCompleto();
    }

    @JsonIgnore
    public String getPseDuiNombreCompleto() {
        return this.psePersona.getPerDuiNombreCompleto();
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

    public SgNivelEscalafon getPseNivelEscalafon() {
        return pseNivelEscalafon;
    }

    public void setPseNivelEscalafon(SgNivelEscalafon pseNivelEscalafon) {
        this.pseNivelEscalafon = pseNivelEscalafon;
    }

    public SgCategoriaEscalafon getPseCategoriaEscalafon() {
        return pseCategoriaEscalafon;
    }

    public void setPseCategoriaEscalafon(SgCategoriaEscalafon pseCategoriaEscalafon) {
        this.pseCategoriaEscalafon = pseCategoriaEscalafon;
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

    public SgDatoEmpleado getPseDatoEmpleado() {
        return pseDatoEmpleado;
    }

    public void setPseDatoEmpleado(SgDatoEmpleado pseDatoEmpleado) {
        this.pseDatoEmpleado = pseDatoEmpleado;
    }

    public SgEstudioRealizado getPseEstudioRealizado() {
        return pseEstudioRealizado;
    }

    public void setPseEstudioRealizado(SgEstudioRealizado pseEstudioRealizado) {
        this.pseEstudioRealizado = pseEstudioRealizado;
    }

    public List<SgFormacionDocente> getPseFormacionDocente() {
        return pseFormacionDocente;
    }

    public void setPseFormacionDocente(List<SgFormacionDocente> pseFormacionDocente) {
        this.pseFormacionDocente = pseFormacionDocente;
    }
    
    public List<SgRelPersonalEspecialidad> getPseRelEspecialidades() {
        return pseRelEspecialidades;
    }

    public void setPseRelEspecialidades(List<SgRelPersonalEspecialidad> pseRelEspecialidades) {
        this.pseRelEspecialidades = pseRelEspecialidades;
    }

    public Integer getPseAnioServicio() {
        return pseAnioServicio;
    }

    public void setPseAnioServicio(Integer pseAnioServicio) {
        this.pseAnioServicio = pseAnioServicio;
    }

    public SgDatoContratacion getPseCrearConDatoContratacion() {
        return pseCrearConDatoContratacion;
    }

    public void setPseCrearConDatoContratacion(SgDatoContratacion pseCrearConDatoContratacion) {
        this.pseCrearConDatoContratacion = pseCrearConDatoContratacion;
    }

    public Boolean getPsePuedeAplicarPlaza() {
        return psePuedeAplicarPlaza;
    }

    public void setPsePuedeAplicarPlaza(Boolean psePuedeAplicarPlaza) {
        this.psePuedeAplicarPlaza = psePuedeAplicarPlaza;
    }
    
    

    //Debe ser igual al hashCode de SgPersonalSedeEducativaLite
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (psePk != null ? psePk.hashCode() : 0);
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
        final SgPersonalSedeEducativa other = (SgPersonalSedeEducativa) obj;
        if (!Objects.equals(this.psePk, other.psePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa[ psePk=" + psePk + " ]";
    }

}
