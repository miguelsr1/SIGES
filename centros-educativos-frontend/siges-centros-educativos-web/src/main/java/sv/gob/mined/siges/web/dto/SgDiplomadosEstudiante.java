/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author bruno
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "depPk", scope = SgDiplomadosEstudiante.class)
public class SgDiplomadosEstudiante {
    private static final long serialVersionUID = 1L;

    private Long depPk;

    private SgAnioLectivo depAnioLectivo;
    
    private SgEstudiante depEstudiante;
    
    private SgDiplomado depDiplomado;
    
    private SgSede depSedeFk;
    
    private LocalDateTime depUltModFecha;

    private String depUltModUsuario;

    private Integer depVersion;
    
    private Long depNieSeleccionado;

    public SgDiplomadosEstudiante() {
    }

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public SgAnioLectivo getDepAnioLectivo() {
        return depAnioLectivo;
    }

    public void setDepAnioLectivo(SgAnioLectivo depAnioLectivo) {
        this.depAnioLectivo = depAnioLectivo;
    }

    public SgEstudiante getDepEstudiante() {
        return depEstudiante;
    }

    public void setDepEstudiante(SgEstudiante depEstudiante) {
        this.depEstudiante = depEstudiante;
    }

    public SgDiplomado getDepDiplomado() {
        return depDiplomado;
    }

    public void setDepDiplomado(SgDiplomado depDiplomado) {
        this.depDiplomado = depDiplomado;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    public SgSede getDepSedeFk() {
        return depSedeFk;
    }

    public void setDepSedeFk(SgSede depSedeFk) {
        this.depSedeFk = depSedeFk;
    }

    public Long getDepNieSeleccionado() {
        return depNieSeleccionado;
    }

    public void setDepNieSeleccionado(Long depNieSeleccionado) {
        this.depNieSeleccionado = depNieSeleccionado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.depPk);
        hash = 97 * hash + Objects.hashCode(this.depAnioLectivo);
        hash = 97 * hash + Objects.hashCode(this.depEstudiante);
        hash = 97 * hash + Objects.hashCode(this.depDiplomado);
        hash = 97 * hash + Objects.hashCode(this.depUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.depUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.depVersion);
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
        final SgDiplomadosEstudiante other = (SgDiplomadosEstudiante) obj;
        if (!Objects.equals(this.depPk, other.depPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgDiplomadosEstudiante{" + "depPk=" + depPk + '}';
    }
}