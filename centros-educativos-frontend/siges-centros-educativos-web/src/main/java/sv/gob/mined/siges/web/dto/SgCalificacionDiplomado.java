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
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTiposCalificacionDiplomado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cadPk", scope = SgCalificacionDiplomado.class)
public class SgCalificacionDiplomado implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cadPk;

    private SgModuloDiplomado cadModuloDiplomado;
    
    private SgSede cadSedeFk;    
   
    private SgAnioLectivo cadAnioLectivoFk;
    
    private Integer cadNumeroPeriodo;
    
    private EnumTiposCalificacionDiplomado cadTipoCalificacion;

    private LocalDateTime cadUltModFecha;

    private String cadUltModUsuario;

    private Integer cadVersion;
    
    private List<SgCalificacionDiplomadoEstudiante> cadCalificacionDiplomadoEstudiantes;
    
    public SgCalificacionDiplomado() {
      
    }

    public Long getCadPk() {
        return cadPk;
    }

    public void setCadPk(Long cadPk) {
        this.cadPk = cadPk;
    }

    public SgModuloDiplomado getCadModuloDiplomado() {
        return cadModuloDiplomado;
    }

    public void setCadModuloDiplomado(SgModuloDiplomado cadModuloDiplomado) {
        this.cadModuloDiplomado = cadModuloDiplomado;
    }

    public Integer getCadNumeroPeriodo() {
        return cadNumeroPeriodo;
    }

    public void setCadNumeroPeriodo(Integer cadNumeroPeriodo) {
        this.cadNumeroPeriodo = cadNumeroPeriodo;
    }

    public EnumTiposCalificacionDiplomado getCadTipoCalificacion() {
        return cadTipoCalificacion;
    }

    public void setCadTipoCalificacion(EnumTiposCalificacionDiplomado cadTipoCalificacion) {
        this.cadTipoCalificacion = cadTipoCalificacion;
    }

    public List<SgCalificacionDiplomadoEstudiante> getCadCalificacionDiplomadoEstudiantes() {
        return cadCalificacionDiplomadoEstudiantes;
    }

    public void setCadCalificacionDiplomadoEstudiantes(List<SgCalificacionDiplomadoEstudiante> cadCalificacionDiplomadoEstudiantes) {
        this.cadCalificacionDiplomadoEstudiantes = cadCalificacionDiplomadoEstudiantes;
    }

    public LocalDateTime getCadUltModFecha() {
        return cadUltModFecha;
    }

    public void setCadUltModFecha(LocalDateTime cadUltModFecha) {
        this.cadUltModFecha = cadUltModFecha;
    }

    public String getCadUltModUsuario() {
        return cadUltModUsuario;
    }

    public void setCadUltModUsuario(String cadUltModUsuario) {
        this.cadUltModUsuario = cadUltModUsuario;
    }

    public Integer getCadVersion() {
        return cadVersion;
    }

    public void setCadVersion(Integer cadVersion) {
        this.cadVersion = cadVersion;
    }

    public SgSede getCadSedeFk() {
        return cadSedeFk;
    }

    public void setCadSedeFk(SgSede cadSedeFk) {
        this.cadSedeFk = cadSedeFk;
    }

    public SgAnioLectivo getCadAnioLectivoFk() {
        return cadAnioLectivoFk;
    }

    public void setCadAnioLectivoFk(SgAnioLectivo cadAnioLectivoFk) {
        this.cadAnioLectivoFk = cadAnioLectivoFk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cadPk != null ? cadPk.hashCode() : 0);
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
        final SgCalificacionDiplomado other = (SgCalificacionDiplomado) obj;
        if (!Objects.equals(this.cadPk, other.cadPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalificacionDiplomado[ cadPk=" + cadPk + " ]";
    }
    
}
