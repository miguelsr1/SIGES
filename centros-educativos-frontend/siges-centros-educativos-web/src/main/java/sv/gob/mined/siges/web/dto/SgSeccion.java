/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.enumerados.EnumRegimenSeccion;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "secPk", scope = SgSeccion.class)
public class SgSeccion implements Serializable {

    private Long secPk;

    private String secCodigo;

    private String secNombre;

    private EnumSeccionEstado secEstado;

    private SgAnioLectivo secAnioLectivo;

    private SgPlanEstudio secPlanEstudio;

    private SgJornadaLaboral secJornadaLaboral;

    private SgServicioEducativo secServicioEducativo;
    
    private Boolean secAccesoInternet;
    
    private Boolean secAccesoComputadora;
    
    private Boolean secIntegrada;

    private LocalDateTime secUltModFecha;

    private String secUltModUsuario;

    private Integer secVersion;

    private List<SgMatricula> secMatricula;
    
    private Boolean secCopiadaAnioSiguiente;
    
    private SgAula secAula;
    
    private SgAsociacion secAsociacion;
    
    private EnumRegimenSeccion secRegimen;
    
    //Las secciones pueden ser anuales o pertencer a un periodo especifico del año
    //Cuando no es anual se especifica a que periodo del año pertenece con secNumeroPeriodo
    private EnumTipoPeriodoSeccion secTipoPeriodo;   
    
    private Integer secNumeroPeriodo;
    
    private LocalDate secFechaCierreSeccion;
    
    public SgSeccion() {
        secAccesoInternet=Boolean.FALSE;
        secAccesoComputadora=Boolean.FALSE;
        secCopiadaAnioSiguiente = Boolean.FALSE;
        secTipoPeriodo=EnumTipoPeriodoSeccion.ANUAL;
    }

    public Long getSecPk() {
        return secPk;
    }

    @JsonIgnore
    public String getNombreSeccion() {
        if (secJornadaLaboral != null) {
            return secNombre + " - " + secJornadaLaboral.getJlaNombre();
        } else {
            return secNombre;
        }
    }
    
    @JsonIgnore
    public Integer getSecCantidadAlumnosHombres(){
        if (this.secMatricula != null){
            return (int) this.secMatricula.stream()
                    .map(m -> m.getMatEstudiante().getEstPersona().getPerSexo())
                    .filter(s -> Constantes.CODIGO_SEXO_MASCULINO.equals(s.getSexCodigo()))
                    .count();
        }
        return null;
    }
    
    @JsonIgnore
    public Integer getSecCantidadAlumnosMujeres(){
        if (this.secMatricula != null){
            return (int) this.secMatricula.stream()
                    .map(m -> m.getMatEstudiante().getEstPersona().getPerSexo())
                    .filter(s -> Constantes.CODIGO_SEXO_FEMENINO.equals(s.getSexCodigo()))
                    .count();
        }
        return null;
    }
    
    @JsonIgnore
    public Integer getSecCantidadAlumnos(){
        if (this.secMatricula != null){
            return this.secMatricula.size();
        }
        return null;
    }
    
    @JsonIgnore
    public String getNombreSeccionTipoPeriodo() {
        if (secTipoPeriodo != null && !EnumTipoPeriodoSeccion.ANUAL.equals(secTipoPeriodo) && secNumeroPeriodo!=null) {
            switch(secNumeroPeriodo){
                case 1: return secNombre+" - Primer período";
                case 2: return secNombre+" - Segundo período";
            }
            return secNombre;
        } else {
            return secNombre;
        }
    }

    
    @JsonIgnore
    public String getNumeroPeriodo() {
        if (secTipoPeriodo != null && !EnumTipoPeriodoSeccion.ANUAL.equals(secTipoPeriodo) && secNumeroPeriodo!=null) {
            switch(secNumeroPeriodo){
                case 1: return "- Primer período";
                case 2: return "- Segundo período";
            }
            
        } 
        return null;
    }
    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public EnumSeccionEstado getSecEstado() {
        return secEstado;
    }

    public void setSecEstado(EnumSeccionEstado secEstado) {
        this.secEstado = secEstado;
    }

    public Boolean getSecIntegrada() {
        return secIntegrada;
    }

    public void setSecIntegrada(Boolean secIntegrada) {
        this.secIntegrada = secIntegrada;
    }
    
    public SgAnioLectivo getSecAnioLectivo() {
        return secAnioLectivo;
    }

    public void setSecAnioLectivo(SgAnioLectivo secAnioLectivo) {
        this.secAnioLectivo = secAnioLectivo;
    }

    public SgPlanEstudio getSecPlanEstudio() {
        return secPlanEstudio;
    }

    public void setSecPlanEstudio(SgPlanEstudio secPlanEstudio) {
        this.secPlanEstudio = secPlanEstudio;
    }

    public SgJornadaLaboral getSecJornadaLaboral() {
        return secJornadaLaboral;
    }

    public void setSecJornadaLaboral(SgJornadaLaboral secJornadaLaboral) {
        this.secJornadaLaboral = secJornadaLaboral;
    }

    public SgServicioEducativo getSecServicioEducativo() {
        return secServicioEducativo;
    }

    public void setSecServicioEducativo(SgServicioEducativo secServicioEducativo) {
        this.secServicioEducativo = secServicioEducativo;
    }

    public LocalDateTime getSecUltModFecha() {
        return secUltModFecha;
    }

    public void setSecUltModFecha(LocalDateTime secUltModFecha) {
        this.secUltModFecha = secUltModFecha;
    }

    public String getSecUltModUsuario() {
        return secUltModUsuario;
    }

    public void setSecUltModUsuario(String secUltModUsuario) {
        this.secUltModUsuario = secUltModUsuario;
    }

    public Integer getSecVersion() {
        return secVersion;
    }

    public void setSecVersion(Integer secVersion) {
        this.secVersion = secVersion;
    }

    public List<SgMatricula> getSecMatricula() {
        return secMatricula;
    }

    public void setSecMatricula(List<SgMatricula> secMatricula) {
        this.secMatricula = secMatricula;
    }

    public Boolean getSecAccesoInternet() {
        return secAccesoInternet;
    }

    public void setSecAccesoInternet(Boolean secAccesoInternet) {
        this.secAccesoInternet = secAccesoInternet;
    }

    public Boolean getSecAccesoComputadora() {
        return secAccesoComputadora;
    }

    public void setSecAccesoComputadora(Boolean secAccesoComputadora) {
        this.secAccesoComputadora = secAccesoComputadora;
    }

    public Boolean getSecCopiadaAnioSiguiente() {
        return secCopiadaAnioSiguiente;
    }

    public void setSecCopiadaAnioSiguiente(Boolean secCopiadaAnioSiguiente) {
        this.secCopiadaAnioSiguiente = secCopiadaAnioSiguiente;
    }

    public SgAula getSecAula() {
        return secAula;
    }

    public void setSecAula(SgAula secAula) {
        this.secAula = secAula;
    }

    public SgAsociacion getSecAsociacion() {
        return secAsociacion;
    }

    public void setSecAsociacion(SgAsociacion secAsociacion) {
        this.secAsociacion = secAsociacion;
    }

    public EnumRegimenSeccion getSecRegimen() {
        return secRegimen;
    }

    public void setSecRegimen(EnumRegimenSeccion secRegimen) {
        this.secRegimen = secRegimen;
    }

    public EnumTipoPeriodoSeccion getSecTipoPeriodo() {
        return secTipoPeriodo;
    }

    public void setSecTipoPeriodo(EnumTipoPeriodoSeccion secTipoPeriodo) {
        this.secTipoPeriodo = secTipoPeriodo;
    }    

    public Integer getSecNumeroPeriodo() {
        return secNumeroPeriodo;
    }

    public void setSecNumeroPeriodo(Integer secNumeroPeriodo) {
        this.secNumeroPeriodo = secNumeroPeriodo;
    }

    public LocalDate getSecFechaCierreSeccion() {
        return secFechaCierreSeccion;
    }

    public void setSecFechaCierreSeccion(LocalDate secFechaCierreSeccion) {
        this.secFechaCierreSeccion = secFechaCierreSeccion;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.secPk);
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
        final SgSeccion other = (SgSeccion) obj;
        if (!Objects.equals(this.secPk, other.secPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSeccion{" + "secPk=" + secPk + ", secNombre=" + secNombre + ", secVersion=" + secVersion + '}';
    }

}
