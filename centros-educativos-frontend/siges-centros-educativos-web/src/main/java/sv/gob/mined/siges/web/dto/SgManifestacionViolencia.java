/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mviPk", scope = SgManifestacionViolencia.class)
public class SgManifestacionViolencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mviPk;

    private LocalDate mviFecha;

    private String mviObservaciones;

    private String mviTratamiento;

    private LocalDateTime mviUltModFecha;

    private String mviUltModUsuario;

    private Integer mviVersion;

    private SgEstudiante mviEstudiante;

    private SgTipoManifestacion mviTipoManifestacion;
    
    private String mviCreacionUsuario;

    public SgManifestacionViolencia() {
    }

    public Long getMviPk() {
        return mviPk;
    }

    public void setMviPk(Long mviPk) {
        this.mviPk = mviPk;
    }

    public SgTipoManifestacion getMviTipoManifestacion() {
        return mviTipoManifestacion;
    }

    public void setMviTipoManifestacion(SgTipoManifestacion mviTipoManifestacion) {
        this.mviTipoManifestacion = mviTipoManifestacion;
    }

    public LocalDate getMviFecha() {
        return mviFecha;
    }

    public void setMviFecha(LocalDate mviFecha) {
        this.mviFecha = mviFecha;
    }

    public String getMviObservaciones() {
        return mviObservaciones;
    }

    public void setMviObservaciones(String mviObservaciones) {
        this.mviObservaciones = mviObservaciones;
    }

    public String getMviTratamiento() {
        return mviTratamiento;
    }

    public void setMviTratamiento(String mviTratamiento) {
        this.mviTratamiento = mviTratamiento;
    }

    public LocalDateTime getMviUltModFecha() {
        return mviUltModFecha;
    }

    public void setMviUltModFecha(LocalDateTime mviUltModFecha) {
        this.mviUltModFecha = mviUltModFecha;
    }

    public String getMviUltModUsuario() {
        return mviUltModUsuario;
    }

    public void setMviUltModUsuario(String mviUltModUsuario) {
        this.mviUltModUsuario = mviUltModUsuario;
    }

    public Integer getMviVersion() {
        return mviVersion;
    }

    public void setMviVersion(Integer mviVersion) {
        this.mviVersion = mviVersion;
    }

    public SgEstudiante getMviEstudiante() {
        return mviEstudiante;
    }

    public void setMviEstudiante(SgEstudiante mviEstudiante) {
        this.mviEstudiante = mviEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mviPk != null ? mviPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgManifestacionViolencia)) {
            return false;
        }
        SgManifestacionViolencia other = (SgManifestacionViolencia) object;
        if ((this.mviPk == null && other.mviPk != null) || (this.mviPk != null && !this.mviPk.equals(other.mviPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgManifestacionViolencia[ mviPk=" + mviPk + " ]";
    }

    public String getMviCreacionUsuario() {
        return mviCreacionUsuario;
    }

    public void setMviCreacionUsuario(String mviCreacionUsuario) {
        this.mviCreacionUsuario = mviCreacionUsuario;
    }

}
