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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDependenciaEconomica;
import sv.gob.mined.siges.web.dto.catalogo.SgMedioTransporte;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "estPk", scope = SgEstudiante.class)
public class SgEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long estPk;

    private Double estDisKmCentro;

    private String estFacRiesgo;

    private Boolean estEmbarazo;

    private SgMedioTransporte estMedioTransporte;

    private SgDependenciaEconomica estDependenciaEconomica;

    private Boolean estHabilitado;
    
    private Boolean estRealizoServicioSocial;

    private LocalDate estFechaServicioSocial;

    private Integer estCantidadHorasServicioSocial;

    private String estDescripcionServicioSocial;

    private LocalDateTime estUltModFecha;

    private String estUltModUsuario;

    private Integer estVersion;

    private SgPersona estPersona;

    private SgFichaSalud estFichaSalud;

    private List<SgManifestacionViolencia> estManifestacionesViolencia;

    private List<SgAsistencia> estAsitencia;

    private List<SgMatricula> estMatriculas;
    
    private SgMatricula estUltimaMatricula;
    
    private Long estUltimaEncuesta;
    
    private Long estUltimaSedePk;
    
    private Boolean estSintonizaCanal10;
    
    private Boolean estSintonizaFranjaEducativa;
    
    public SgEstudiante() {
        this.estPersona = new SgPersona();
        this.estManifestacionesViolencia = new ArrayList<>();
        this.estHabilitado = Boolean.TRUE;
        this.estRealizoServicioSocial=Boolean.FALSE;
    }
    
    public Boolean getEstadoMatricula(){
        if(this.estUltimaMatricula!=null && this.estUltimaMatricula.getMatEstado()!=null){
            return this.estUltimaMatricula.getMatEstado().equals(EnumMatriculaEstado.ABIERTO)? Boolean.TRUE : Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    public SgEstudiante(Long estPk) {
        this.estPk = estPk;
    }

    public Long getEstPk() {
        return estPk;
    }

    public void setEstPk(Long estPk) {
        this.estPk = estPk;
    }

    public Double getEstDisKmCentro() {
        return estDisKmCentro;
    }

    public void setEstDisKmCentro(Double estDisKmCentro) {
        this.estDisKmCentro = estDisKmCentro;
    }

    public String getEstFacRiesgo() {
        return estFacRiesgo;
    }

    public void setEstFacRiesgo(String estFacRiesgo) {
        this.estFacRiesgo = estFacRiesgo;
    }

    public Boolean getEstEmbarazo() {
        return estEmbarazo;
    }

    public void setEstEmbarazo(Boolean estEmbarazo) {
        this.estEmbarazo = estEmbarazo;
    }

    public SgMedioTransporte getEstMedioTransporte() {
        return estMedioTransporte;
    }

    public void setEstMedioTransporte(SgMedioTransporte estMedioTransporte) {
        this.estMedioTransporte = estMedioTransporte;
    }

    public Boolean getEstHabilitado() {
        return estHabilitado;
    }

    public void setEstHabilitado(Boolean estHabilitado) {
        this.estHabilitado = estHabilitado;
    }

    public LocalDateTime getEstUltModFecha() {
        return estUltModFecha;
    }

    public void setEstUltModFecha(LocalDateTime estUltModFecha) {
        this.estUltModFecha = estUltModFecha;
    }

    public String getEstUltModUsuario() {
        return estUltModUsuario;
    }

    public void setEstUltModUsuario(String estUltModUsuario) {
        this.estUltModUsuario = estUltModUsuario;
    }

    public Integer getEstVersion() {
        return estVersion;
    }

    public void setEstVersion(Integer estVersion) {
        this.estVersion = estVersion;
    }

    public SgPersona getEstPersona() {
        return estPersona;
    }

    public void setEstPersona(SgPersona estPersona) {
        this.estPersona = estPersona;
    }

    public SgFichaSalud getEstFichaSalud() {
        return estFichaSalud;
    }

    public void setEstFichaSalud(SgFichaSalud estFichaSalud) {
        this.estFichaSalud = estFichaSalud;
    }

    public List<SgManifestacionViolencia> getEstManifestacionesViolencia() {
        return estManifestacionesViolencia;
    }

    public void setEstManifestacionesViolencia(List<SgManifestacionViolencia> estManifestacionesViolencia) {
        this.estManifestacionesViolencia = estManifestacionesViolencia;
    }

    public List<SgAsistencia> getEstAsitencia() {
        return estAsitencia;
    }

    public void setEstAsitencia(List<SgAsistencia> estAsitencia) {
        this.estAsitencia = estAsitencia;
    }

    public List<SgMatricula> getEstMatriculas() {
        return estMatriculas;
    }

    public void setEstMatriculas(List<SgMatricula> estMatriculas) {
        this.estMatriculas = estMatriculas;
    }

    public SgDependenciaEconomica getEstDependenciaEconomica() {
        return estDependenciaEconomica;
    }

    public void setEstDependenciaEconomica(SgDependenciaEconomica estDependenciaEconomica) {
        this.estDependenciaEconomica = estDependenciaEconomica;
    }

    public SgMatricula getEstUltimaMatricula() {
        return estUltimaMatricula;
    }

    public void setEstUltimaMatricula(SgMatricula estUltimaMatricula) {
        this.estUltimaMatricula = estUltimaMatricula;
    }

    public Long getEstUltimaSedePk() {
        return estUltimaSedePk;
    }

    public void setEstUltimaSedePk(Long estUltimaSedePk) {
        this.estUltimaSedePk = estUltimaSedePk;
    }

    public Boolean getEstRealizoServicioSocial() {
        return estRealizoServicioSocial;
    }

    public void setEstRealizoServicioSocial(Boolean estRealizoServicioSocial) {
        this.estRealizoServicioSocial = estRealizoServicioSocial;
    }

    public LocalDate getEstFechaServicioSocial() {
        return estFechaServicioSocial;
    }

    public void setEstFechaServicioSocial(LocalDate estFechaServicioSocial) {
        this.estFechaServicioSocial = estFechaServicioSocial;
    }

    public Integer getEstCantidadHorasServicioSocial() {
        return estCantidadHorasServicioSocial;
    }

    public void setEstCantidadHorasServicioSocial(Integer estCantidadHorasServicioSocial) {
        this.estCantidadHorasServicioSocial = estCantidadHorasServicioSocial;
    }

    public String getEstDescripcionServicioSocial() {
        return estDescripcionServicioSocial;
    }

    public void setEstDescripcionServicioSocial(String estDescripcionServicioSocial) {
        this.estDescripcionServicioSocial = estDescripcionServicioSocial;
    }

    public Long getEstUltimaEncuesta() {
        return estUltimaEncuesta;
    }

    public void setEstUltimaEncuesta(Long estUltimaEncuesta) {
        this.estUltimaEncuesta = estUltimaEncuesta;
    }

    public Boolean getEstSintonizaCanal10() {
        return estSintonizaCanal10;
    }

    public void setEstSintonizaCanal10(Boolean estSintonizaCanal10) {
        this.estSintonizaCanal10 = estSintonizaCanal10;
    }

    public Boolean getEstSintonizaFranjaEducativa() {
        return estSintonizaFranjaEducativa;
    }

    public void setEstSintonizaFranjaEducativa(Boolean estSintonizaFranjaEducativa) {
        this.estSintonizaFranjaEducativa = estSintonizaFranjaEducativa;
    }

 
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estPk != null ? estPk.hashCode() : 0);
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
        final SgEstudiante other = (SgEstudiante) obj;
        if (!Objects.equals(this.estPk, other.estPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudiante[ estPk=" + estPk + " ]";
    }

}
