/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "titPk", scope = SgTitulo.class)
public class SgTitulo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long titPk;
 
    private SgEstudiante titEstudianteFk;
 
    private String titNombreEstudiante;    
   
    private String titNombreEstudiantePartida;
    
  
    private String titNombreCertificado;
    
  
    private LocalDate titFechaValidez;
    
  
    private LocalDate titFechaEmision;  

    

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
    
    
    public SgTitulo() {
       
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
