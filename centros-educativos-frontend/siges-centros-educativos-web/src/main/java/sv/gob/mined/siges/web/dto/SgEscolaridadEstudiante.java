/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.web.enumerados.EnumPromovidoCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "escPk", scope = SgEscolaridadEstudiante.class)
public class SgEscolaridadEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long escPk;

    private SgEstudiante escEstudiante;
    
    private SgServicioEducativo escServicioEducativo;

    private SgAnioLectivo escAnio;

    private EnumPromovidoCalificacion escResultado;
    
    private Integer escAsistencias;
    
    private Integer escInasistencias;

    private LocalDateTime escUltModFecha;

    private String escUltModUsuario;

    private Integer escVersion;
    
    private SgMatricula escMatriculaFk;
    
    private Boolean escCreadoCierre;
    
    private Boolean escGeneradaPorEquivalencia;
    
    private String escEqNumeroTramite;
    
    private Integer escEqAnio;
    
    private SgGrado escEqGrado;
    
    private SgOpcion escEqOpcion;
    
    private SgProgramaEducativo escEqProgramaEducativo;
    
    private String escNombreSede;
    
    
    public SgEscolaridadEstudiante() {
       
    }
    
    @JsonIgnore
    public Integer getAnio(){
        if (BooleanUtils.isTrue(escGeneradaPorEquivalencia)){
            return escEqAnio;
        } else {
            return escAnio.getAleAnio();
        }
    }

    public Long getEscPk() {
        return escPk;
    }

    public void setEscPk(Long escPk) {
        this.escPk = escPk;
    }

    public SgEstudiante getEscEstudiante() {
        return escEstudiante;
    }

    public void setEscEstudiante(SgEstudiante escEstudiante) {
        this.escEstudiante = escEstudiante;
    }

    public SgServicioEducativo getEscServicioEducativo() {
        return escServicioEducativo;
    }

    public void setEscServicioEducativo(SgServicioEducativo escServicioEducativo) {
        this.escServicioEducativo = escServicioEducativo;
    }

    public SgAnioLectivo getEscAnio() {
        return escAnio;
    }

    public void setEscAnio(SgAnioLectivo escAnio) {
        this.escAnio = escAnio;
    }

    public EnumPromovidoCalificacion getEscResultado() {
        return escResultado;
    }

    public void setEscResultado(EnumPromovidoCalificacion escResultado) {
        this.escResultado = escResultado;
    }

    public Integer getEscAsistencias() {
        return escAsistencias;
    }

    public void setEscAsistencias(Integer escAsistencias) {
        this.escAsistencias = escAsistencias;
    }

    public Integer getEscInasistencias() {
        return escInasistencias;
    }

    public void setEscInasistencias(Integer escInasistencias) {
        this.escInasistencias = escInasistencias;
    }

   
    public LocalDateTime getEscUltModFecha() {
        return escUltModFecha;
    }

    public void setEscUltModFecha(LocalDateTime escUltModFecha) {
        this.escUltModFecha = escUltModFecha;
    }

    public String getEscUltModUsuario() {
        return escUltModUsuario;
    }

    public void setEscUltModUsuario(String escUltModUsuario) {
        this.escUltModUsuario = escUltModUsuario;
    }

    public Integer getEscVersion() {
        return escVersion;
    }

    public void setEscVersion(Integer escVersion) {
        this.escVersion = escVersion;
    }

    public SgMatricula getEscMatriculaFk() {
        return escMatriculaFk;
    }

    public void setEscMatriculaFk(SgMatricula escMatriculaFk) {
        this.escMatriculaFk = escMatriculaFk;
    } 

    public Boolean getEscCreadoCierre() {
        return escCreadoCierre;
    }

    public void setEscCreadoCierre(Boolean escCreadoCierre) {
        this.escCreadoCierre = escCreadoCierre;
    }

    public Boolean getEscGeneradaPorEquivalencia() {
        return escGeneradaPorEquivalencia;
    }

    public void setEscGeneradaPorEquivalencia(Boolean escGeneradaPorEquivalencia) {
        this.escGeneradaPorEquivalencia = escGeneradaPorEquivalencia;
    }

    public String getEscEqNumeroTramite() {
        return escEqNumeroTramite;
    }

    public void setEscEqNumeroTramite(String escEqNumeroTramite) {
        this.escEqNumeroTramite = escEqNumeroTramite;
    }

    public Integer getEscEqAnio() {
        return escEqAnio;
    }

    public void setEscEqAnio(Integer escEqAnio) {
        this.escEqAnio = escEqAnio;
    }

    public SgGrado getEscEqGrado() {
        return escEqGrado;
    }

    public void setEscEqGrado(SgGrado escEqGrado) {
        this.escEqGrado = escEqGrado;
    }

    public SgOpcion getEscEqOpcion() {
        return escEqOpcion;
    }

    public void setEscEqOpcion(SgOpcion escEqOpcion) {
        this.escEqOpcion = escEqOpcion;
    }

    public SgProgramaEducativo getEscEqProgramaEducativo() {
        return escEqProgramaEducativo;
    }

    public void setEscEqProgramaEducativo(SgProgramaEducativo escEqProgramaEducativo) {
        this.escEqProgramaEducativo = escEqProgramaEducativo;
    }

    public String getEscNombreSede() {
        return escNombreSede;
    }

    public void setEscNombreSede(String escNombreSede) {
        this.escNombreSede = escNombreSede;
    }
    
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escPk != null ? escPk.hashCode() : 0);
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
        final SgEscolaridadEstudiante other = (SgEscolaridadEstudiante) obj;
        if (!Objects.equals(this.escPk, other.escPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEscolaridadEstudiante[ escPk=" + escPk + " ]";
    }
    
}
