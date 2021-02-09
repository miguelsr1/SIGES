/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ppePk", scope = SgPropuestaPedagogica.class)
public class SgPropuestaPedagogica implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ppePk;
    
    private SgSede ppeSede;
    
    private String ppeNombre;
    
    private String ppeCaracterizacion;
    
    private String ppeProblemasPriorizados;
    
    private String ppePerfilEstudiante;
    
    private String ppeRetosPedagogicos;
    
    private String ppeAcuerdosPedagogicos;
    
    private String ppeMetas;
    
    private String ppeIndicadores;
    
    private LocalDate ppeFechaInicio;
    
    private LocalDate ppeFechaFin;
    
    private SgArchivo ppeArchivo;
    
    private LocalDateTime ppeUltModFecha;
    
    private String ppeUltModUsuario;
    
    private Integer ppeVersion;

    public SgPropuestaPedagogica() {
    }

    public SgPropuestaPedagogica(Long ppePk) {
        this.ppePk = ppePk;
    }

    public Long getPpePk() {
        return ppePk;
    }

    public void setPpePk(Long ppePk) {
        this.ppePk = ppePk;
    }

    public SgSede getPpeSede() {
        return ppeSede;
    }

    public void setPpeSede(SgSede ppeSede) {
        this.ppeSede = ppeSede;
    }

    public String getPpeNombre() {
        return ppeNombre;
    }

    public void setPpeNombre(String ppeNombre) {
        this.ppeNombre = ppeNombre;
    }

    public String getPpeCaracterizacion() {
        return ppeCaracterizacion;
    }

    public void setPpeCaracterizacion(String ppeCaracterizacion) {
        this.ppeCaracterizacion = ppeCaracterizacion;
    }

    public String getPpeProblemasPriorizados() {
        return ppeProblemasPriorizados;
    }

    public void setPpeProblemasPriorizados(String ppeProblemasPriorizados) {
        this.ppeProblemasPriorizados = ppeProblemasPriorizados;
    }

    public String getPpePerfilEstudiante() {
        return ppePerfilEstudiante;
    }

    public void setPpePerfilEstudiante(String ppePerfilEstudiante) {
        this.ppePerfilEstudiante = ppePerfilEstudiante;
    }

    public String getPpeRetosPedagogicos() {
        return ppeRetosPedagogicos;
    }

    public void setPpeRetosPedagogicos(String ppeRetosPedagogicos) {
        this.ppeRetosPedagogicos = ppeRetosPedagogicos;
    }

    public String getPpeAcuerdosPedagogicos() {
        return ppeAcuerdosPedagogicos;
    }

    public void setPpeAcuerdosPedagogicos(String ppeAcuerdosPedagogicos) {
        this.ppeAcuerdosPedagogicos = ppeAcuerdosPedagogicos;
    }

    public String getPpeMetas() {
        return ppeMetas;
    }

    public void setPpeMetas(String ppeMetas) {
        this.ppeMetas = ppeMetas;
    }

    public String getPpeIndicadores() {
        return ppeIndicadores;
    }

    public void setPpeIndicadores(String ppeIndicadores) {
        this.ppeIndicadores = ppeIndicadores;
    }

    public LocalDate getPpeFechaInicio() {
        return ppeFechaInicio;
    }

    public void setPpeFechaInicio(LocalDate ppeFechaInicio) {
        this.ppeFechaInicio = ppeFechaInicio;
    }

    public LocalDate getPpeFechaFin() {
        return ppeFechaFin;
    }

    public void setPpeFechaFin(LocalDate ppeFechaFin) {
        this.ppeFechaFin = ppeFechaFin;
    }

    public LocalDateTime getPpeUltModFecha() {
        return ppeUltModFecha;
    }

    public void setPpeUltModFecha(LocalDateTime ppeUltModFecha) {
        this.ppeUltModFecha = ppeUltModFecha;
    }

    public String getPpeUltModUsuario() {
        return ppeUltModUsuario;
    }

    public void setPpeUltModUsuario(String ppeUltModUsuario) {
        this.ppeUltModUsuario = ppeUltModUsuario;
    }

    public Integer getPpeVersion() {
        return ppeVersion;
    }

    public void setPpeVersion(Integer ppeVersion) {
        this.ppeVersion = ppeVersion;
    }

    public SgArchivo getPpeArchivo() {
        return ppeArchivo;
    }

    public void setPpeArchivo(SgArchivo ppeArchivo) {
        this.ppeArchivo = ppeArchivo;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ppePk != null ? ppePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPropuestaPedagogica)) {
            return false;
        }
        SgPropuestaPedagogica other = (SgPropuestaPedagogica) object;
        if ((this.ppePk == null && other.ppePk != null) || (this.ppePk != null && !this.ppePk.equals(other.ppePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPropuestaPedagogica[ ppePk=" + ppePk + " ]";
    }
    
}
