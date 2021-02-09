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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.web.dto.catalogo.SgTituloAnterior;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "retPk", scope = SgReposicionTitulo.class)
public class SgReposicionTitulo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long retPk;

    private String retNombreEstudiantePartida;

    private Integer retAnio;
    
    private String retSedeNombre;
    
    private String retEstadoReposicion;
    
    private String retUsuarioEnviaImprimir;


    private LocalDateTime retUltModFecha;

    private String retUltModUsuario;

    private Integer retVersion;
    
    private SgSede retSede;
    
    private SgSolicitudImpresion retSolicitudImpresion;
    
    private Boolean retAnulada;

    private SgMotivoAnulacionTitulo retMotivoAnulacion;
 
    private String retObservacionAnulada;    
    
    private String retOpcionBachillerato;   
    
    private SgTituloAnterior retTituloAnterior2008;
    
    private String retDuiSolicitante;   
    
    private LocalDate retFechaLegalizacionTitulo;
    
    private Boolean retEsAnterior2008;  

    private String retNombreTituloPosterior2008;  
    
    
    public SgReposicionTitulo() {
        retAnulada=Boolean.FALSE;
        retEsAnterior2008=Boolean.TRUE;
    }

    public Long getRetPk() {
        return retPk;
    }

    public void setRetPk(Long retPk) {
        this.retPk = retPk;
    }

    public String getRetNombreEstudiantePartida() {
        return retNombreEstudiantePartida;
    }

    public void setRetNombreEstudiantePartida(String retNombreEstudiantePartida) {
        this.retNombreEstudiantePartida = retNombreEstudiantePartida;
    }

    public Integer getRetAnio() {
        return retAnio;
    }

    public void setRetAnio(Integer retAnio) {
        this.retAnio = retAnio;
    }
    
    public String getRetSedeNombre() {
        return retSedeNombre;
    }

    public void setRetSedeNombre(String retSedeNombre) {
        this.retSedeNombre = retSedeNombre;
    }

    public String getRetEstadoReposicion() {
        return retEstadoReposicion;
    }

    public void setRetEstadoReposicion(String retEstadoReposicion) {
        this.retEstadoReposicion = retEstadoReposicion;
    }

    public String getRetUsuarioEnviaImprimir() {
        return retUsuarioEnviaImprimir;
    }

    public void setRetUsuarioEnviaImprimir(String retUsuarioEnviaImprimir) {
        this.retUsuarioEnviaImprimir = retUsuarioEnviaImprimir;
    }

    public LocalDateTime getRetUltModFecha() {
        return retUltModFecha;
    }

    public void setRetUltModFecha(LocalDateTime retUltModFecha) {
        this.retUltModFecha = retUltModFecha;
    }

    public String getRetUltModUsuario() {
        return retUltModUsuario;
    }

    public void setRetUltModUsuario(String retUltModUsuario) {
        this.retUltModUsuario = retUltModUsuario;
    }

    public Integer getRetVersion() {
        return retVersion;
    }

    public void setRetVersion(Integer retVersion) {
        this.retVersion = retVersion;
    }

    public SgSede getRetSede() {
        return retSede;
    }

    public void setRetSede(SgSede retSede) {
        this.retSede = retSede;
    }

    public SgSolicitudImpresion getRetSolicitudImpresion() {
        return retSolicitudImpresion;
    }

    public void setRetSolicitudImpresion(SgSolicitudImpresion retSolicitudImpresion) {
        this.retSolicitudImpresion = retSolicitudImpresion;
    }  

    public Boolean getRetAnulada() {
        return retAnulada;
    }

    public void setRetAnulada(Boolean retAnulada) {
        this.retAnulada = retAnulada;
    }

    public SgMotivoAnulacionTitulo getRetMotivoAnulacion() {
        return retMotivoAnulacion;
    }

    public void setRetMotivoAnulacion(SgMotivoAnulacionTitulo retMotivoAnulacion) {
        this.retMotivoAnulacion = retMotivoAnulacion;
    }

    public String getRetObservacionAnulada() {
        return retObservacionAnulada;
    }

    public void setRetObservacionAnulada(String retObservacionAnulada) {
        this.retObservacionAnulada = retObservacionAnulada;
    }

    public String getRetOpcionBachillerato() {
        return retOpcionBachillerato;
    }

    public void setRetOpcionBachillerato(String retOpcionBachillerato) {
        this.retOpcionBachillerato = retOpcionBachillerato;
    }

    public SgTituloAnterior getRetTituloAnterior2008() {
        return retTituloAnterior2008;
    }

    public void setRetTituloAnterior2008(SgTituloAnterior retTituloAnterior2008) {
        this.retTituloAnterior2008 = retTituloAnterior2008;
    }

    public String getRetDuiSolicitante() {
        return retDuiSolicitante;
    }

    public void setRetDuiSolicitante(String retDuiSolicitante) {
        this.retDuiSolicitante = retDuiSolicitante;
    }

    public LocalDate getRetFechaLegalizacionTitulo() {
        return retFechaLegalizacionTitulo;
    }

    public void setRetFechaLegalizacionTitulo(LocalDate retFechaLegalizacionTitulo) {
        this.retFechaLegalizacionTitulo = retFechaLegalizacionTitulo;
    }

    public Boolean getRetEsAnterior2008() {
        return retEsAnterior2008;
    }

    public void setRetEsAnterior2008(Boolean retEsAnterior2008) {
        this.retEsAnterior2008 = retEsAnterior2008;
    }

    public String getRetNombreTituloPosterior2008() {
        return retNombreTituloPosterior2008;
    }

    public void setRetNombreTituloPosterior2008(String retNombreTituloPosterior2008) {
        this.retNombreTituloPosterior2008 = retNombreTituloPosterior2008;
    }

    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (retPk != null ? retPk.hashCode() : 0);
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
        final SgReposicionTitulo other = (SgReposicionTitulo) obj;
        if (!Objects.equals(this.retPk, other.retPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgReposicionTitulo[ retPk=" + retPk + " ]";
    }
    
}
