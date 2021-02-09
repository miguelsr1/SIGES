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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoReimpresion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.enumerados.EnumTipoSolicitudImpresion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "simPk", scope = SgSolicitudImpresion.class)
public class SgSolicitudImpresion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long simPk;

    private LocalDate simFechaSolicitud;

    private LocalDate simFechaEnviadoImprimir;

    private EnumEstadoSolicitudImpresion simEstado;
    
    private EnumTipoSolicitudImpresion simTipoImpresion;

    private SgSeccion simSeccion;

    private SgUsuario simUsuario;

    private List<SgEstudianteImpresion> simEstudianteImpresion;

    private Integer simInicioNumeroCorrelativo;

    private LocalDateTime simUltModFecha;

    private String simUltModUsuario;

    private Integer simVersion;
    
    private SgDefinicionTitulo simDefinicionTitulo;
    
    private SgSelloFirma simSelloDirector;    

    private SgSelloFirmaTitular simSelloMinistro;    

    private SgSelloFirmaTitular simSelloAutentica;
    
    private List<SgTitulo> simTitulos;   
    
    private SgMotivoReimpresion simMotivoReimpresion; 
    
    private SgReposicionTitulo simReposicionTitulo;
    
    private Boolean simTituloEntregadoDepartamental;    

    private LocalDate simFechaEntregadoDepartamental;    

    private Boolean simTituloEntregadoCentroEducativo;    

    private LocalDate simFechaEntregadoCentroEducativo;
    
    private Integer simCantidadEstudianteImpresion;
    
    private LocalDate simFechaValidez;
    
    private SgArchivo simResolucionFk;   
    
    //True si la reposición es para un estudiante existente en siges
    private Boolean simReposicionEstudianteSiges;  
       
    private String simNumeroRegistro;
    
    private String simNumeroResolucion;
    
    public SgSolicitudImpresion() {
        simTipoImpresion=EnumTipoSolicitudImpresion.IMP;
        simTituloEntregadoDepartamental=Boolean.FALSE;
        simTituloEntregadoCentroEducativo=Boolean.FALSE;
    }
    
    @JsonIgnore
    private Boolean simTituloAnulado;

    public Long getSimPk() {
        return simPk;
    }

    public void setSimPk(Long simPk) {
        this.simPk = simPk;
    }

    public LocalDate getSimFechaSolicitud() {
        return simFechaSolicitud;
    }

    public void setSimFechaSolicitud(LocalDate simFechaSolicitud) {
        this.simFechaSolicitud = simFechaSolicitud;
    }

    public EnumEstadoSolicitudImpresion getSimEstado() {
        return simEstado;
    }

    public void setSimEstado(EnumEstadoSolicitudImpresion simEstado) {
        this.simEstado = simEstado;
    }

    public SgSeccion getSimSeccion() {
        return simSeccion;
    }

    public void setSimSeccion(SgSeccion simSeccion) {
        this.simSeccion = simSeccion;
    }

    public SgUsuario getSimUsuario() {
        return simUsuario;
    }

    public void setSimUsuario(SgUsuario simUsuario) {
        this.simUsuario = simUsuario;
    }

    public LocalDateTime getSimUltModFecha() {
        return simUltModFecha;
    }

    public void setSimUltModFecha(LocalDateTime simUltModFecha) {
        this.simUltModFecha = simUltModFecha;
    }

    public String getSimUltModUsuario() {
        return simUltModUsuario;
    }

    public void setSimUltModUsuario(String simUltModUsuario) {
        this.simUltModUsuario = simUltModUsuario;
    }

    public Integer getSimVersion() {
        return simVersion;
    }

    public void setSimVersion(Integer simVersion) {
        this.simVersion = simVersion;
    }

    public List<SgEstudianteImpresion> getSimEstudianteImpresion() {
        return simEstudianteImpresion;
    }

    public void setSimEstudianteImpresion(List<SgEstudianteImpresion> simEstudianteImpresion) {
        this.simEstudianteImpresion = simEstudianteImpresion;
    }

    public Integer getSimInicioNumeroCorrelativo() {
        return simInicioNumeroCorrelativo;
    }

    public void setSimInicioNumeroCorrelativo(Integer simInicioNumeroCorrelativo) {
        this.simInicioNumeroCorrelativo = simInicioNumeroCorrelativo;
    }

    public LocalDate getSimFechaEnviadoImprimir() {
        return simFechaEnviadoImprimir;
    }

    public void setSimFechaEnviadoImprimir(LocalDate simFechaEnviadoImprimir) {
        this.simFechaEnviadoImprimir = simFechaEnviadoImprimir;
    }

    public SgDefinicionTitulo getSimDefinicionTitulo() {
        return simDefinicionTitulo;
    }

    public void setSimDefinicionTitulo(SgDefinicionTitulo simDefinicionTitulo) {
        this.simDefinicionTitulo = simDefinicionTitulo;
    }

    public SgSelloFirma getSimSelloDirector() {
        return simSelloDirector;
    }

    public void setSimSelloDirector(SgSelloFirma simSelloDirector) {
        this.simSelloDirector = simSelloDirector;
    }

    public SgSelloFirmaTitular getSimSelloMinistro() {
        return simSelloMinistro;
    }

    public void setSimSelloMinistro(SgSelloFirmaTitular simSelloMinistro) {
        this.simSelloMinistro = simSelloMinistro;
    }

    public SgSelloFirmaTitular getSimSelloAutentica() {
        return simSelloAutentica;
    }

    public void setSimSelloAutentica(SgSelloFirmaTitular simSelloAutentica) {
        this.simSelloAutentica = simSelloAutentica;
    }

    public EnumTipoSolicitudImpresion getSimTipoImpresion() {
        return simTipoImpresion;
    }

    public void setSimTipoImpresion(EnumTipoSolicitudImpresion simTipoImpresion) {
        this.simTipoImpresion = simTipoImpresion;
    }

    public List<SgTitulo> getSimTitulos() {
        return simTitulos;
    }

    public void setSimTitulos(List<SgTitulo> simTitulos) {
        this.simTitulos = simTitulos;
    }

    public SgMotivoReimpresion getSimMotivoReimpresion() {
        return simMotivoReimpresion;
    }

    public void setSimMotivoReimpresion(SgMotivoReimpresion simMotivoReimpresion) {
        this.simMotivoReimpresion = simMotivoReimpresion;
    }

    public SgReposicionTitulo getSimReposicionTitulo() {
        return simReposicionTitulo;
    }

    public void setSimReposicionTitulo(SgReposicionTitulo simReposicionTitulo) {
        this.simReposicionTitulo = simReposicionTitulo;
    }

    public Boolean getSimTituloAnulado() {
        return simTituloAnulado;
    }

    public void setSimTituloAnulado(Boolean simTituloAnulado) {
        this.simTituloAnulado = simTituloAnulado;
    }

    public Boolean getSimTituloEntregadoDepartamental() {
        return simTituloEntregadoDepartamental;
    }

    public void setSimTituloEntregadoDepartamental(Boolean simTituloEntregadoDepartamental) {
        this.simTituloEntregadoDepartamental = simTituloEntregadoDepartamental;
    }

    public LocalDate getSimFechaEntregadoDepartamental() {
        return simFechaEntregadoDepartamental;
    }

    public void setSimFechaEntregadoDepartamental(LocalDate simFechaEntregadoDepartamental) {
        this.simFechaEntregadoDepartamental = simFechaEntregadoDepartamental;
    }

    public Boolean getSimTituloEntregadoCentroEducativo() {
        return simTituloEntregadoCentroEducativo;
    }

    public void setSimTituloEntregadoCentroEducativo(Boolean simTituloEntregadoCentroEducativo) {
        this.simTituloEntregadoCentroEducativo = simTituloEntregadoCentroEducativo;
    }

    public LocalDate getSimFechaEntregadoCentroEducativo() {
        return simFechaEntregadoCentroEducativo;
    }

    public void setSimFechaEntregadoCentroEducativo(LocalDate simFechaEntregadoCentroEducativo) {
        this.simFechaEntregadoCentroEducativo = simFechaEntregadoCentroEducativo;
    }

    public Integer getSimCantidadEstudianteImpresion() {
        return simCantidadEstudianteImpresion;
    }

    public void setSimCantidadEstudianteImpresion(Integer simCantidadEstudianteImpresion) {
        this.simCantidadEstudianteImpresion = simCantidadEstudianteImpresion;
    }

    public LocalDate getSimFechaValidez() {
        return simFechaValidez;
    }

    public void setSimFechaValidez(LocalDate simFechaValidez) {
        this.simFechaValidez = simFechaValidez;
    }

    public SgArchivo getSimResolucionFk() {
        return simResolucionFk;
    }

    public void setSimResolucionFk(SgArchivo simResolucionFk) {
        this.simResolucionFk = simResolucionFk;
    }


    public Boolean getSimReposicionEstudianteSiges() {
        return simReposicionEstudianteSiges;
    }

    public void setSimReposicionEstudianteSiges(Boolean simReposicionEstudianteSiges) {
        this.simReposicionEstudianteSiges = simReposicionEstudianteSiges;
    }

    public String getSimNumeroRegistro() {
        return simNumeroRegistro;
    }

    public void setSimNumeroRegistro(String simNumeroRegistro) {
        this.simNumeroRegistro = simNumeroRegistro;
    }

    public String getSimNumeroResolucion() {
        return simNumeroResolucion;
    }

    public void setSimNumeroResolucion(String simNumeroResolucion) {
        this.simNumeroResolucion = simNumeroResolucion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (simPk != null ? simPk.hashCode() : 0);
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
        final SgSolicitudImpresion other = (SgSolicitudImpresion) obj;
        if (!Objects.equals(this.simPk, other.simPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgSolicitudImpresion[ simPk=" + simPk + " ]";
    }

}
