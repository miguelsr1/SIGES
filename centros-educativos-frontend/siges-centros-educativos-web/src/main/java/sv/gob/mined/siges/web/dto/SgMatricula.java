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
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoRetiro;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumPromocionGradoMatricula;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "matPk", scope = SgMatricula.class)
public class SgMatricula implements Serializable {

    private Long matPk;

    private SgSeccion matSeccion;

    private SgEstudiante matEstudiante;

    private EnumMatriculaEstado matEstado;

    private LocalDate matFechaHasta;

    private LocalDateTime matFechaRegistro;

    private LocalDate matFechaIngreso;

    private String matObservaciones;

    private String matObservacioneProvisional;
    
    private String matCodigoConstancia;

    private SgMotivoRetiro matMotivoRetiro;
    
    private Boolean matRetirada;
    
    private Boolean matAnulada;

    private LocalDateTime matUltModFecha;

    private String matUltModUsuario;
    
    private String matCreacionUsuario;

    private Integer matVersion;

    private Boolean matProvisional;
    
    private String matObsAnuRetiro;  
    
    private Boolean matValidacionAcademica;
    
    private LocalDateTime matValidacionAcademicaFecha;
    
    private String matValidacionAcademicaUsuario;
    
    private EnumPromocionGradoMatricula matPromocionGrado;
    
    private Boolean matRepetidor;
    
    private Boolean matCerradoPorCierreAnio;
                  
    public SgMatricula() {
        this.matProvisional = Boolean.FALSE;
        this.matRetirada = Boolean.FALSE;
        this.matAnulada = Boolean.FALSE;
        this.matRepetidor = Boolean.FALSE;
    }

    public SgMatricula(Long matPk) {
        this.matPk = matPk;
    }
    
    public Boolean getMatTieneObservaciones(){
        return StringUtils.isNotBlank(matObservaciones);
    }
    
    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public SgSeccion getMatSeccion() {
        return matSeccion;
    }

    public void setMatSeccion(SgSeccion matSeccion) {
        this.matSeccion = matSeccion;
    }

    public SgEstudiante getMatEstudiante() {
        return matEstudiante;
    }

    public void setMatEstudiante(SgEstudiante matEstudiante) {
        this.matEstudiante = matEstudiante;
    }

    public EnumMatriculaEstado getMatEstado() {
        return matEstado;
    }

    public void setMatEstado(EnumMatriculaEstado matEstado) {
        this.matEstado = matEstado;
    }

    public LocalDate getMatFechaHasta() {
        return matFechaHasta;
    }

    public void setMatFechaHasta(LocalDate matFechaHasta) {
        this.matFechaHasta = matFechaHasta;
    }

    public SgMotivoRetiro getMatMotivoRetiro() {
        return matMotivoRetiro;
    }

    public void setMatMotivoRetiro(SgMotivoRetiro matMotivoRetiro) {
        this.matMotivoRetiro = matMotivoRetiro;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }

    public Boolean getMatProvisional() {
        return matProvisional;
    }

    public void setMatProvisional(Boolean matProvisional) {
        this.matProvisional = matProvisional;
    }

    public Boolean getMatAnulada() {
        return matAnulada;
    }

    public void setMatAnulada(Boolean matAnulada) {
        this.matAnulada = matAnulada;
    }
        
    public String getMatObservaciones() {
        return matObservaciones;
    }

    public void setMatObservaciones(String matObservaciones) {
        this.matObservaciones = matObservaciones;
    }

    public String getMatObservacioneProvisional() {
        return matObservacioneProvisional;
    }

    public void setMatObservacioneProvisional(String matObservacioneProvisional) {
        this.matObservacioneProvisional = matObservacioneProvisional;
    }

    public LocalDateTime getMatFechaRegistro() {
        return matFechaRegistro;
    }

    public void setMatFechaRegistro(LocalDateTime matFechaRegistro) {
        this.matFechaRegistro = matFechaRegistro;
    }

    public LocalDate getMatFechaIngreso() {
        return matFechaIngreso;
    }

    public void setMatFechaIngreso(LocalDate matFechaIngreso) {
        this.matFechaIngreso = matFechaIngreso;
    }

    public String getMatObsAnuRetiro() {
        return matObsAnuRetiro;
    }

    public void setMatObsAnuRetiro(String matObsAnuRetiro) {
        this.matObsAnuRetiro = matObsAnuRetiro;
    }

    public String getMatCodigoConstancia() {
        return matCodigoConstancia;
    }

    public void setMatCodigoConstancia(String matCodigoConstancia) {
        this.matCodigoConstancia = matCodigoConstancia;
    }
    public String getMatCreacionUsuario() {
        return matCreacionUsuario;
    }

    public void setMatCreacionUsuario(String matCreacionUsuario) {
        this.matCreacionUsuario = matCreacionUsuario;
    }

    public Boolean getMatValidacionAcademica() {
        return matValidacionAcademica;
    }

    public void setMatValidacionAcademica(Boolean matValidacionAcademica) {
        this.matValidacionAcademica = matValidacionAcademica;
    }

    public LocalDateTime getMatValidacionAcademicaFecha() {
        return matValidacionAcademicaFecha;
    }

    public void setMatValidacionAcademicaFecha(LocalDateTime matValidacionAcademicaFecha) {
        this.matValidacionAcademicaFecha = matValidacionAcademicaFecha;
    }

    public String getMatValidacionAcademicaUsuario() {
        return matValidacionAcademicaUsuario;
    }

    public void setMatValidacionAcademicaUsuario(String matValidacionAcademicaUsuario) {
        this.matValidacionAcademicaUsuario = matValidacionAcademicaUsuario;
    }

    public Boolean getMatRetirada() {
        return matRetirada;
    }

    public void setMatRetirada(Boolean matRetirada) {
        this.matRetirada = matRetirada;
    }

    public EnumPromocionGradoMatricula getMatPromocionGrado() {
        return matPromocionGrado;
    }

    public void setMatPromocionGrado(EnumPromocionGradoMatricula matPromocionGrado) {
        this.matPromocionGrado = matPromocionGrado;
    }

    public Boolean getMatRepetidor() {
        return matRepetidor;
    }

    public void setMatRepetidor(Boolean matRepetidor) {
        this.matRepetidor = matRepetidor;
    }

    public Boolean getMatCerradoPorCierreAnio() {
        return matCerradoPorCierreAnio;
    }

    public void setMatCerradoPorCierreAnio(Boolean matCerradoPorCierreAnio) {
        this.matCerradoPorCierreAnio = matCerradoPorCierreAnio;
    }
    
    
       
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
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
        final SgMatricula other = (SgMatricula) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMatricula[ matPk=" + matPk + " ]";
    }

}
