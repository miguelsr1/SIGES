/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pesPk", scope = SgPlanEstudio.class)
public class SgPlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pesPk;

    private String pesCodigo;

    private String pesNombre;

    private String pesDescripcion;

    private Boolean pesVigente;

    private LocalDateTime pesUltModFecha;

    private String pesUltModUsuario;

    private Integer pesVersion;

    private SgRelModEdModAten pesRelacionModalidad;

    private SgOpcion pesOpcion;

    private SgProgramaEducativo pesProgramaEducativo;

    //private List<SgComponentePlanGrado> pesPlanGrado;
    public SgPlanEstudio() {
        this.pesVigente = Boolean.TRUE;
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public String getPesDescripcion() {
        return pesDescripcion;
    }

    public void setPesDescripcion(String pesDescripcion) {
        this.pesDescripcion = pesDescripcion;
    }

    public LocalDateTime getPesUltModFecha() {
        return pesUltModFecha;
    }

    public void setPesUltModFecha(LocalDateTime pesUltModFecha) {
        this.pesUltModFecha = pesUltModFecha;
    }

    public String getPesUltModUsuario() {
        return pesUltModUsuario;
    }

    public void setPesUltModUsuario(String pesUltModUsuario) {
        this.pesUltModUsuario = pesUltModUsuario;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
    }

    public Boolean getPesVigente() {
        return pesVigente;
    }

    public void setPesVigente(Boolean pesVigente) {
        this.pesVigente = pesVigente;
    }

    public SgRelModEdModAten getPesRelacionModalidad() {
        return pesRelacionModalidad;
    }

    public void setPesRelacionModalidad(SgRelModEdModAten pesRelacionModalidad) {
        this.pesRelacionModalidad = pesRelacionModalidad;
    }

    public SgOpcion getPesOpcion() {
        return pesOpcion;
    }

    public void setPesOpcion(SgOpcion pesOpcion) {
        this.pesOpcion = pesOpcion;
    }

    public SgProgramaEducativo getPesProgramaEducativo() {
        return pesProgramaEducativo;
    }

    public void setPesProgramaEducativo(SgProgramaEducativo pesProgramaEducativo) {
        this.pesProgramaEducativo = pesProgramaEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pesPk != null ? pesPk.hashCode() : 0);
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
        final SgPlanEstudio other = (SgPlanEstudio) obj;
        if (!Objects.equals(this.pesPk, other.pesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPlanesEstudio[ pesPk=" + pesPk + " ]";
    }

}
