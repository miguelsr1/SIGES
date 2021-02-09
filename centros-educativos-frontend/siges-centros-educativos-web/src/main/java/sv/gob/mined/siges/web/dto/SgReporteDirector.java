/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rdiPk", scope = SgReporteDirector.class)
public class SgReporteDirector implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rdiPk;

    private String rdiObservacionSede;

    private Boolean rdiDatosSede;

    private Boolean rdiConObservacionesSede;

    private String rdiObservacionEstudiante;

    private Boolean rdiDatosEstudiante;

    private Boolean rdiConObservacionesEstudiante;

    private String rdiObservacionPersonal;

    private Boolean rdiDatosPersonal;

    private Boolean rdiConObservacionesPersonal;

    private LocalDateTime rdiUltModFecha;

    private String rdiUltModUsuario;

    private Integer rdiVersion;

    private SgSede rdiSede;

    public SgReporteDirector() {
        rdiDatosSede = Boolean.FALSE;
        rdiConObservacionesSede = Boolean.FALSE;
        rdiDatosEstudiante = Boolean.FALSE;
        rdiConObservacionesEstudiante = Boolean.FALSE;
        rdiDatosPersonal = Boolean.FALSE;
        rdiConObservacionesPersonal = Boolean.FALSE;
    }

    public Long getRdiPk() {
        return rdiPk;
    }

    public void setRdiPk(Long rdiPk) {
        this.rdiPk = rdiPk;
    }

    public String getRdiObservacionSede() {
        return rdiObservacionSede;
    }

    public void setRdiObservacionSede(String rdiObservacionSede) {
        this.rdiObservacionSede = rdiObservacionSede;
    }

    public Boolean getRdiDatosSede() {
        return rdiDatosSede;
    }

    public void setRdiDatosSede(Boolean rdiDatosSede) {
        this.rdiDatosSede = rdiDatosSede;
    }

    public Boolean getRdiConObservacionesSede() {
        return rdiConObservacionesSede;
    }

    public void setRdiConObservacionesSede(Boolean rdiConObservacionesSede) {
        this.rdiConObservacionesSede = rdiConObservacionesSede;
    }

    public SgSede getRdiSede() {
        return rdiSede;
    }

    public void setRdiSede(SgSede rdiSede) {
        this.rdiSede = rdiSede;
    }

    public String getRdiObservacionEstudiante() {
        return rdiObservacionEstudiante;
    }

    public void setRdiObservacionEstudiante(String rdiObservacionEstudiante) {
        this.rdiObservacionEstudiante = rdiObservacionEstudiante;
    }

    public Boolean getRdiDatosEstudiante() {
        return rdiDatosEstudiante;
    }

    public void setRdiDatosEstudiante(Boolean rdiDatosEstudiante) {
        this.rdiDatosEstudiante = rdiDatosEstudiante;
    }

    public Boolean getRdiConObservacionesEstudiante() {
        return rdiConObservacionesEstudiante;
    }

    public void setRdiConObservacionesEstudiante(Boolean rdiConObservacionesEstudiante) {
        this.rdiConObservacionesEstudiante = rdiConObservacionesEstudiante;
    }

    public String getRdiObservacionPersonal() {
        return rdiObservacionPersonal;
    }

    public void setRdiObservacionPersonal(String rdiObservacionPersonal) {
        this.rdiObservacionPersonal = rdiObservacionPersonal;
    }

    public Boolean getRdiDatosPersonal() {
        return rdiDatosPersonal;
    }

    public void setRdiDatosPersonal(Boolean rdiDatosPersonal) {
        this.rdiDatosPersonal = rdiDatosPersonal;
    }

    public Boolean getRdiConObservacionesPersonal() {
        return rdiConObservacionesPersonal;
    }

    public void setRdiConObservacionesPersonal(Boolean rdiConObservacionesPersonal) {
        this.rdiConObservacionesPersonal = rdiConObservacionesPersonal;
    }

    public LocalDateTime getRdiUltModFecha() {
        return rdiUltModFecha;
    }

    public void setRdiUltModFecha(LocalDateTime rdiUltModFecha) {
        this.rdiUltModFecha = rdiUltModFecha;
    }

    public String getRdiUltModUsuario() {
        return rdiUltModUsuario;
    }

    public void setRdiUltModUsuario(String rdiUltModUsuario) {
        this.rdiUltModUsuario = rdiUltModUsuario;
    }

    public Integer getRdiVersion() {
        return rdiVersion;
    }

    public void setRdiVersion(Integer rdiVersion) {
        this.rdiVersion = rdiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rdiPk != null ? rdiPk.hashCode() : 0);
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
        final SgReporteDirector other = (SgReporteDirector) obj;
        if (!Objects.equals(this.rdiPk, other.rdiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgReporteDirector[ rdiPk=" + rdiPk + " ]";
    }

}
