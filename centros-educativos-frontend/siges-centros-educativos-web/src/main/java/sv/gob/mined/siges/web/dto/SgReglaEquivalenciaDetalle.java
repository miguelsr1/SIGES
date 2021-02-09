/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumOperacionReglaEquivalencia;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "redPk", scope = SgReglaEquivalenciaDetalle.class)
public class SgReglaEquivalenciaDetalle implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long redPk;
    
    private SgGrado redGrado;
    
    private SgPlanEstudio redPlanEstudio;
    
    private SgReglaEquivalencia redReglaEquivalencia;
    
    private EnumOperacionReglaEquivalencia redOperacion;
    
    private Boolean redHabilitado;

    private LocalDateTime redUltModFecha;

    private String redUltModUsuario;

    private Integer redVersion;
    
    private SgOpcion redOpcion;
    
    private SgProgramaEducativo redProgramaEducativo;

    public Long getRedPk() {
        return redPk;
    }

    public void setRedPk(Long redPk) {
        this.redPk = redPk;
    }

    public SgGrado getRedGrado() {
        return redGrado;
    }

    public void setRedGrado(SgGrado redGrado) {
        this.redGrado = redGrado;
    }

    public SgPlanEstudio getRedPlanEstudio() {
        return redPlanEstudio;
    }

    public void setRedPlanEstudio(SgPlanEstudio redPlanEstudio) {
        this.redPlanEstudio = redPlanEstudio;
    }

    public EnumOperacionReglaEquivalencia getRedOperacion() {
        return redOperacion;
    }

    public void setRedOperacion(EnumOperacionReglaEquivalencia redOperacion) {
        this.redOperacion = redOperacion;
    }

    public Boolean getRedHabilitado() {
        return redHabilitado;
    }

    public void setRedHabilitado(Boolean redHabilitado) {
        this.redHabilitado = redHabilitado;
    }

    public LocalDateTime getRedUltModFecha() {
        return redUltModFecha;
    }

    public void setRedUltModFecha(LocalDateTime redUltModFecha) {
        this.redUltModFecha = redUltModFecha;
    }

    public String getRedUltModUsuario() {
        return redUltModUsuario;
    }

    public void setRedUltModUsuario(String redUltModUsuario) {
        this.redUltModUsuario = redUltModUsuario;
    }

    public Integer getRedVersion() {
        return redVersion;
    }

    public void setRedVersion(Integer redVersion) {
        this.redVersion = redVersion;
    }

    public SgReglaEquivalencia getRedReglaEquivalencia() {
        return redReglaEquivalencia;
    }

    public void setRedReglaEquivalencia(SgReglaEquivalencia redReglaEquivalencia) {
        this.redReglaEquivalencia = redReglaEquivalencia;
    }

    public SgOpcion getRedOpcion() {
        return redOpcion;
    }

    public void setRedOpcion(SgOpcion redOpcion) {
        this.redOpcion = redOpcion;
    }

    public SgProgramaEducativo getRedProgramaEducativo() {
        return redProgramaEducativo;
    }

    public void setRedProgramaEducativo(SgProgramaEducativo redProgramaEducativo) {
        this.redProgramaEducativo = redProgramaEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.redPk);
        hash = 41 * hash + Objects.hashCode(this.redGrado);
        hash = 41 * hash + Objects.hashCode(this.redPlanEstudio);
        hash = 41 * hash + Objects.hashCode(this.redReglaEquivalencia);
        hash = 41 * hash + Objects.hashCode(this.redOperacion);
        hash = 41 * hash + Objects.hashCode(this.redHabilitado);
        hash = 41 * hash + Objects.hashCode(this.redUltModFecha);
        hash = 41 * hash + Objects.hashCode(this.redUltModUsuario);
        hash = 41 * hash + Objects.hashCode(this.redVersion);
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
        final SgReglaEquivalenciaDetalle other = (SgReglaEquivalenciaDetalle) obj;
        if (!Objects.equals(this.redPk, other.redPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgReglaEquivalenciaDetalle{" + "redPk=" + redPk + ", redGrado=" + redGrado + ", redPlanEstudio=" + redPlanEstudio + ", redReglaEquivalencia=" + redReglaEquivalencia + ", redOperacion=" + redOperacion + ", redHabilitado=" + redHabilitado + ", redUltModFecha=" + redUltModFecha + ", redUltModUsuario=" + redUltModUsuario + ", redVersion=" + redVersion + ", redOpcion=" + redOpcion + ", redProgramaEducativo=" + redProgramaEducativo + '}';
    }
    
    
}
