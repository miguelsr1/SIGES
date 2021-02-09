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

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "chePk", scope = SgCalificacionesHistoricasEstudiante.class)
public class SgCalificacionesHistoricasEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long chePk;

    
    private Long cheMatriculaExternaId;
    
    
    private Integer cheAnioMatricula;
    
    
    private String cheObservacion;
    
    
    private LocalDate cheFechaRealizado;
    
    
    private String cheProcesoDeCreacion;

    private SgEstudiante cheEstudianteFk;
 
    private SgSede cheSedeFk;
    
    
    private String cheSedeExternaCodigo;
    
    
    private String cheSedeExternaNombre;
    
    
    private String cheServicioEducativoExternoDescripcion;
    
    
    private String cheServicioEducativoExternoEtiquetaImpresion;
    
    
    private Long chePlanEstudioExternoId;
    
    
    private String chePlanEstudioExternoDescripcion;
    
    
    private String cheComponente;
    
    
    private String chePI;
    
    
    private String cheNFI;
    
    
    private String cheNPAESI;
    
    
    private String cheRIX;
    
    
    private String cheRIR;
    
    
    private String cheRIRE;
    
    
    private String cheNF;
    
    
    private String cheNPAES;
    
    
    private String chePIModif;
 
    
    private String chePI_r;    
    
    private String chePIR;

    private LocalDateTime cheUltModFecha;

    private String cheUltModUsuario;

    private Integer cheVersion;
    
    private Long cheEvaluacionExternaId;
    
    private Long cheServicioEducativoExternoId;
      
    private Long cheEstudianteNie;

    private SgPersona chePersonaFk;
    
    private String cheObservacionEdicion;
    
    private SgArchivo cheArchivoEdicion;
    
    private Boolean cheEsImportado;
    
    //Es un dato transient, se utiliza para indicar si el cambio de estudiante
    //en una calificación tiene que abarcar a todas las calificaciones
    private Boolean modificarCalificacionesEstudiante;
    
    private SgEstudiante nuevoEstudiante;
    
    public SgCalificacionesHistoricasEstudiante() {
    }
    
    public String getSedeExternaNombreCompleto(){
        return this.cheSedeExternaCodigo+" - "+this.cheSedeExternaNombre;
    }

    public Long getChePk() {
        return chePk;
    }

    public void setChePk(Long chePk) {
        this.chePk = chePk;
    }

    public Long getCheMatriculaExternaId() {
        return cheMatriculaExternaId;
    }

    public void setCheMatriculaExternaId(Long cheMatriculaExternaId) {
        this.cheMatriculaExternaId = cheMatriculaExternaId;
    }

    public Integer getCheAnioMatricula() {
        return cheAnioMatricula;
    }

    public void setCheAnioMatricula(Integer cheAnioMatricula) {
        this.cheAnioMatricula = cheAnioMatricula;
    }

    public String getCheObservacion() {
        return cheObservacion;
    }

    public void setCheObservacion(String cheObservacion) {
        this.cheObservacion = cheObservacion;
    }

    public LocalDate getCheFechaRealizado() {
        return cheFechaRealizado;
    }

    public void setCheFechaRealizado(LocalDate cheFechaRealizado) {
        this.cheFechaRealizado = cheFechaRealizado;
    }

    public String getCheProcesoDeCreacion() {
        return cheProcesoDeCreacion;
    }

    public void setCheProcesoDeCreacion(String cheProcesoDeCreacion) {
        this.cheProcesoDeCreacion = cheProcesoDeCreacion;
    }

    public SgEstudiante getCheEstudianteFk() {
        return cheEstudianteFk;
    }

    public void setCheEstudianteFk(SgEstudiante cheEstudianteFk) {
        this.cheEstudianteFk = cheEstudianteFk;
    }

    public SgSede getCheSedeFk() {
        return cheSedeFk;
    }

    public void setCheSedeFk(SgSede cheSedeFk) {
        this.cheSedeFk = cheSedeFk;
    }

    public String getCheSedeExternaCodigo() {
        return cheSedeExternaCodigo;
    }

    public void setCheSedeExternaCodigo(String cheSedeExternaCodigo) {
        this.cheSedeExternaCodigo = cheSedeExternaCodigo;
    }

    public String getCheSedeExternaNombre() {
        return cheSedeExternaNombre;
    }

    public void setCheSedeExternaNombre(String cheSedeExternaNombre) {
        this.cheSedeExternaNombre = cheSedeExternaNombre;
    }

    public String getCheServicioEducativoExternoDescripcion() {
        return cheServicioEducativoExternoDescripcion;
    }

    public void setCheServicioEducativoExternoDescripcion(String cheServicioEducativoExternoDescripcion) {
        this.cheServicioEducativoExternoDescripcion = cheServicioEducativoExternoDescripcion;
    }

    public String getCheServicioEducativoExternoEtiquetaImpresion() {
        return cheServicioEducativoExternoEtiquetaImpresion;
    }

    public void setCheServicioEducativoExternoEtiquetaImpresion(String cheServicioEducativoExternoEtiquetaImpresion) {
        this.cheServicioEducativoExternoEtiquetaImpresion = cheServicioEducativoExternoEtiquetaImpresion;
    }

    public Long getChePlanEstudioExternoId() {
        return chePlanEstudioExternoId;
    }

    public void setChePlanEstudioExternoId(Long chePlanEstudioExternoId) {
        this.chePlanEstudioExternoId = chePlanEstudioExternoId;
    }

    public String getChePlanEstudioExternoDescripcion() {
        return chePlanEstudioExternoDescripcion;
    }

    public void setChePlanEstudioExternoDescripcion(String chePlanEstudioExternoDescripcion) {
        this.chePlanEstudioExternoDescripcion = chePlanEstudioExternoDescripcion;
    }

    public String getCheComponente() {
        return cheComponente;
    }

    public void setCheComponente(String cheComponente) {
        this.cheComponente = cheComponente;
    }

    public String getChePI() {
        return chePI;
    }

    public void setChePI(String chePI) {
        this.chePI = chePI;
    }

    public String getCheNFI() {
        return cheNFI;
    }

    public void setCheNFI(String cheNFI) {
        this.cheNFI = cheNFI;
    }

    public String getCheNPAESI() {
        return cheNPAESI;
    }

    public void setCheNPAESI(String cheNPAESI) {
        this.cheNPAESI = cheNPAESI;
    }

    public String getCheRIX() {
        return cheRIX;
    }

    public void setCheRIX(String cheRIX) {
        this.cheRIX = cheRIX;
    }

    public String getCheRIR() {
        return cheRIR;
    }

    public void setCheRIR(String cheRIR) {
        this.cheRIR = cheRIR;
    }

    public String getCheRIRE() {
        return cheRIRE;
    }

    public void setCheRIRE(String cheRIRE) {
        this.cheRIRE = cheRIRE;
    }

    public String getCheNF() {
        return cheNF;
    }

    public void setCheNF(String cheNF) {
        this.cheNF = cheNF;
    }

    public String getCheNPAES() {
        return cheNPAES;
    }

    public void setCheNPAES(String cheNPAES) {
        this.cheNPAES = cheNPAES;
    }

    public String getChePIModif() {
        return chePIModif;
    }

    public void setChePIModif(String chePIModif) {
        this.chePIModif = chePIModif;
    }

    public String getChePI_r() {
        return chePI_r;
    }

    public void setChePI_r(String chePI_r) {
        this.chePI_r = chePI_r;
    }

    public String getChePIR() {
        return chePIR;
    }

    public void setChePIR(String chePIR) {
        this.chePIR = chePIR;
    }


    public LocalDateTime getCheUltModFecha() {
        return cheUltModFecha;
    }

    public void setCheUltModFecha(LocalDateTime cheUltModFecha) {
        this.cheUltModFecha = cheUltModFecha;
    }

    public String getCheUltModUsuario() {
        return cheUltModUsuario;
    }

    public void setCheUltModUsuario(String cheUltModUsuario) {
        this.cheUltModUsuario = cheUltModUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    public void setCheVersion(Integer cheVersion) {
        this.cheVersion = cheVersion;
    }

    public Long getCheEvaluacionExternaId() {
        return cheEvaluacionExternaId;
    }

    public void setCheEvaluacionExternaId(Long cheEvaluacionExternaId) {
        this.cheEvaluacionExternaId = cheEvaluacionExternaId;
    }

    public Long getCheServicioEducativoExternoId() {
        return cheServicioEducativoExternoId;
    }

    public void setCheServicioEducativoExternoId(Long cheServicioEducativoExternoId) {
        this.cheServicioEducativoExternoId = cheServicioEducativoExternoId;
    }

    public Long getCheEstudianteNie() {
        return cheEstudianteNie;
    }

    public void setCheEstudianteNie(Long cheEstudianteNie) {
        this.cheEstudianteNie = cheEstudianteNie;
    }

    public SgPersona getChePersonaFk() {
        return chePersonaFk;
    }

    public void setChePersonaFk(SgPersona chePersonaFk) {
        this.chePersonaFk = chePersonaFk;
    }

    public String getCheObservacionEdicion() {
        return cheObservacionEdicion;
    }

    public void setCheObservacionEdicion(String cheObservacionEdicion) {
        this.cheObservacionEdicion = cheObservacionEdicion;
    }

    public SgArchivo getCheArchivoEdicion() {
        return cheArchivoEdicion;
    }

    public void setCheArchivoEdicion(SgArchivo cheArchivoEdicion) {
        this.cheArchivoEdicion = cheArchivoEdicion;
    }

    public Boolean getModificarCalificacionesEstudiante() {
        return modificarCalificacionesEstudiante;
    }

    public void setModificarCalificacionesEstudiante(Boolean modificarCalificacionesEstudiante) {
        this.modificarCalificacionesEstudiante = modificarCalificacionesEstudiante;
    }

    public SgEstudiante getNuevoEstudiante() {
        return nuevoEstudiante;
    }

    public void setNuevoEstudiante(SgEstudiante nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
    }

    public Boolean getCheEsImportado() {
        return cheEsImportado;
    }

    public void setCheEsImportado(Boolean cheEsImportado) {
        this.cheEsImportado = cheEsImportado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chePk != null ? chePk.hashCode() : 0);
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
        final SgCalificacionesHistoricasEstudiante other = (SgCalificacionesHistoricasEstudiante) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalificacionesHistoricasEstudiante[ chePk=" + chePk + " ]";
    }
    
}
