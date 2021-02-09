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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoReimpresion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "titPk", scope = SgTitulo.class)
public class SgTitulo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long titPk;
 
    private SgEstudiante titEstudianteFk;
 
    private String titNombreEstudiante;    
   
    private String titNombreEstudiantePartida;
    
  
    private String titNombreCertificado;
    
  
    private LocalDate titFechaValidez;
    
  
    private LocalDate titFechaEmision;
    

    private SgSelloFirma titSelloFirmaDirectorFk;
    
  
    private SgSelloFirmaTitular titSelloFirmaTitularMinistroFk;
 
    private SgSelloFirmaTitular titSelloFirmaTitularAutenticaFk;    

    private String titNombreDirector;
    

    private String titNombreMinistro;
    

    private Integer titAnio;
    
  
    private SgSede titSedeFk;
    
   
    private String titSedeCodigo;
    
 
    private String titSedeNombre;
    

    private SgServicioEducativo titServicioEducativoFk;
    

    private SgSolicitudImpresion titSolicitudImpresionFk;
    

    private String titUsuarioEnviaImprimir;

    
    private String titHash;

    private LocalDateTime titUltModFecha;

    private String titUltModUsuario;

    private Integer titVersion;
    
    private Boolean titAnulada;

    private SgMotivoReimpresion titMotivoReimpresion; 
    
    private String titNombreTitular;
    
    private String titDuiEstudiante;
    
    private LocalDate titFechaLegalizacionTitulo;
    
    private String titTituloAnterior2008;
    
    private String titNumeroResolucion;
    
    private String titNumeroRegistro;
    
    private Boolean titReposicion;
    
    private SgSeccion titSeccionFk;
    
    private Boolean titEsAnterior2008; 

    private String titNombreTituloPosterior2008;  
    
    
    public SgTitulo() {
       
    }
    
    public String getSedCodigoNombre() {
        return (StringUtils.isBlank(this.titSedeCodigo)?"":(this.titSedeCodigo+" ")) + this.titSedeNombre;
    }

    public Long getTitPk() {
        return titPk;
    }

    public void setTitPk(Long titPk) {
        this.titPk = titPk;
    }

    public SgEstudiante getTitEstudianteFk() {
        return titEstudianteFk;
    }

    public void setTitEstudianteFk(SgEstudiante titEstudianteFk) {
        this.titEstudianteFk = titEstudianteFk;
    }

    public String getTitNombreEstudiante() {
        return titNombreEstudiante;
    }

    public void setTitNombreEstudiante(String titNombreEstudiante) {
        this.titNombreEstudiante = titNombreEstudiante;
    }

    public String getTitNombreEstudiantePartida() {
        return titNombreEstudiantePartida;
    }

    public void setTitNombreEstudiantePartida(String titNombreEstudiantePartida) {
        this.titNombreEstudiantePartida = titNombreEstudiantePartida;
    }

    public String getTitNombreCertificado() {
        return titNombreCertificado;
    }

    public void setTitNombreCertificado(String titNombreCertificado) {
        this.titNombreCertificado = titNombreCertificado;
    }

    public LocalDate getTitFechaValidez() {
        return titFechaValidez;
    }

    public void setTitFechaValidez(LocalDate titFechaValidez) {
        this.titFechaValidez = titFechaValidez;
    }

    public LocalDate getTitFechaEmision() {
        return titFechaEmision;
    }

    public void setTitFechaEmision(LocalDate titFechaEmision) {
        this.titFechaEmision = titFechaEmision;
    }

    public SgSelloFirma getTitSelloFirmaDirectorFk() {
        return titSelloFirmaDirectorFk;
    }

    public void setTitSelloFirmaDirectorFk(SgSelloFirma titSelloFirmaDirectorFk) {
        this.titSelloFirmaDirectorFk = titSelloFirmaDirectorFk;
    }

    public SgSelloFirmaTitular getTitSelloFirmaTitularMinistroFk() {
        return titSelloFirmaTitularMinistroFk;
    }

    public void setTitSelloFirmaTitularMinistroFk(SgSelloFirmaTitular titSelloFirmaTitularMinistroFk) {
        this.titSelloFirmaTitularMinistroFk = titSelloFirmaTitularMinistroFk;
    }

    public SgSelloFirmaTitular getTitSelloFirmaTitularAutenticaFk() {
        return titSelloFirmaTitularAutenticaFk;
    }

    public void setTitSelloFirmaTitularAutenticaFk(SgSelloFirmaTitular titSelloFirmaTitularAutenticaFk) {
        this.titSelloFirmaTitularAutenticaFk = titSelloFirmaTitularAutenticaFk;
    }

    public String getTitNombreDirector() {
        return titNombreDirector;
    }

    public void setTitNombreDirector(String titNombreDirector) {
        this.titNombreDirector = titNombreDirector;
    }

    public String getTitNombreMinistro() {
        return titNombreMinistro;
    }

    public void setTitNombreMinistro(String titNombreMinistro) {
        this.titNombreMinistro = titNombreMinistro;
    }

    public Integer getTitAnio() {
        return titAnio;
    }

    public void setTitAnio(Integer titAnio) {
        this.titAnio = titAnio;
    }

    public SgSede getTitSedeFk() {
        return titSedeFk;
    }

    public void setTitSedeFk(SgSede titSedeFk) {
        this.titSedeFk = titSedeFk;
    }

    public String getTitSedeCodigo() {
        return titSedeCodigo;
    }

    public void setTitSedeCodigo(String titSedeCodigo) {
        this.titSedeCodigo = titSedeCodigo;
    }

    public String getTitSedeNombre() {
        return titSedeNombre;
    }

    public void setTitSedeNombre(String titSedeNombre) {
        this.titSedeNombre = titSedeNombre;
    }

    public SgServicioEducativo getTitServicioEducativoFk() {
        return titServicioEducativoFk;
    }

    public void setTitServicioEducativoFk(SgServicioEducativo titServicioEducativoFk) {
        this.titServicioEducativoFk = titServicioEducativoFk;
    }

    public SgSolicitudImpresion getTitSolicitudImpresionFk() {
        return titSolicitudImpresionFk;
    }

    public void setTitSolicitudImpresionFk(SgSolicitudImpresion titSolicitudImpresionFk) {
        this.titSolicitudImpresionFk = titSolicitudImpresionFk;
    }
    

    public String getTitUsuarioEnviaImprimir() {
        return titUsuarioEnviaImprimir;
    }

    public void setTitUsuarioEnviaImprimir(String titUsuarioEnviaImprimir) {
        this.titUsuarioEnviaImprimir = titUsuarioEnviaImprimir;
    }

    public String getTitHash() {
        return titHash;
    }

    public void setTitHash(String titHash) {
        this.titHash = titHash;
    }

    public LocalDateTime getTitUltModFecha() {
        return titUltModFecha;
    }

    public void setTitUltModFecha(LocalDateTime titUltModFecha) {
        this.titUltModFecha = titUltModFecha;
    }

    public String getTitUltModUsuario() {
        return titUltModUsuario;
    }

    public void setTitUltModUsuario(String titUltModUsuario) {
        this.titUltModUsuario = titUltModUsuario;
    }

    public Integer getTitVersion() {
        return titVersion;
    }

    public void setTitVersion(Integer titVersion) {
        this.titVersion = titVersion;
    }

    public Boolean getTitAnulada() {
        return titAnulada;
    }

    public void setTitAnulada(Boolean titAnulada) {
        this.titAnulada = titAnulada;
    }

    public SgMotivoReimpresion getTitMotivoReimpresion() {
        return titMotivoReimpresion;
    }

    public void setTitMotivoReimpresion(SgMotivoReimpresion titMotivoReimpresion) {
        this.titMotivoReimpresion = titMotivoReimpresion;
    }

    public String getTitNombreTitular() {
        return titNombreTitular;
    }

    public void setTitNombreTitular(String titNombreTitular) {
        this.titNombreTitular = titNombreTitular;
    }

    public String getTitDuiEstudiante() {
        return titDuiEstudiante;
    }

    public void setTitDuiEstudiante(String titDuiEstudiante) {
        this.titDuiEstudiante = titDuiEstudiante;
    }

    public LocalDate getTitFechaLegalizacionTitulo() {
        return titFechaLegalizacionTitulo;
    }

    public void setTitFechaLegalizacionTitulo(LocalDate titFechaLegalizacionTitulo) {
        this.titFechaLegalizacionTitulo = titFechaLegalizacionTitulo;
    }

    public String getTitTituloAnterior2008() {
        return titTituloAnterior2008;
    }

    public void setTitTituloAnterior2008(String titTituloAnterior2008) {
        this.titTituloAnterior2008 = titTituloAnterior2008;
    }

    public String getTitNumeroResolucion() {
        return titNumeroResolucion;
    }

    public void setTitNumeroResolucion(String titNumeroResolucion) {
        this.titNumeroResolucion = titNumeroResolucion;
    }

    public String getTitNumeroRegistro() {
        return titNumeroRegistro;
    }

    public void setTitNumeroRegistro(String titNumeroRegistro) {
        this.titNumeroRegistro = titNumeroRegistro;
    }

    public Boolean getTitReposicion() {
        return titReposicion;
    }

    public void setTitReposicion(Boolean titReposicion) {
        this.titReposicion = titReposicion;
    }

    public SgSeccion getTitSeccionFk() {
        return titSeccionFk;
    }

    public void setTitSeccionFk(SgSeccion titSeccionFk) {
        this.titSeccionFk = titSeccionFk;
    }

    public Boolean getTitEsAnterior2008() {
        return titEsAnterior2008;
    }

    public void setTitEsAnterior2008(Boolean titEsAnterior2008) {
        this.titEsAnterior2008 = titEsAnterior2008;
    }

    public String getTitNombreTituloPosterior2008() {
        return titNombreTituloPosterior2008;
    }

    public void setTitNombreTituloPosterior2008(String titNombreTituloPosterior2008) {
        this.titNombreTituloPosterior2008 = titNombreTituloPosterior2008;
    }

   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (titPk != null ? titPk.hashCode() : 0);
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
        final SgTitulo other = (SgTitulo) obj;
        if (!Objects.equals(this.titPk, other.titPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTitulo[ titPk=" + titPk + " ]";
    }
    
}
