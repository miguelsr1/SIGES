/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgEstDatasets;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoExtraccion;



/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "extPk", scope = SgExtraccion.class)
public class SgExtraccion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long extPk;

    private Integer extAnio;
    
    private SgEstDatasets extDataset;
    
    private SgEstNombreExtraccion extNombre;
    
    private String extDescripcion;
   
    private LocalDate extFechaMatriculados;
    
    private LocalDateTime extUltmodFecha;
    
    private EnumEstadoExtraccion extEstado;

    private String extUltmodUsuario;

    private Integer extVersion;
    
    private List<SgAlcanceExtraccion> extAlcance;
    
    private List<SgGradoReportaEn> extGradoReportaEn;

    public Long getExtPk() {
        return extPk;
    }

    public void setExtPk(Long extPk) {
        this.extPk = extPk;
    }

    public Integer getExtAnio() {
        return extAnio;
    }

    public void setExtAnio(Integer extAnio) {
        this.extAnio = extAnio;
    }

    public LocalDateTime getExtUltmodFecha() {
        return extUltmodFecha;
    }

    public void setExtUltmodFecha(LocalDateTime extUltmodFecha) {
        this.extUltmodFecha = extUltmodFecha;
    }

    public String getExtUltmodUsuario() {
        return extUltmodUsuario;
    }

    public void setExtUltmodUsuario(String extUltmodUsuario) {
        this.extUltmodUsuario = extUltmodUsuario;
    }

    public Integer getExtVersion() {
        return extVersion;
    }

    public void setExtVersion(Integer extVersion) {
        this.extVersion = extVersion;
    }

    public SgEstDatasets getExtDataset() {
        return extDataset;
    }

    public void setExtDataset(SgEstDatasets extDataset) {
        this.extDataset = extDataset;
    }

    public SgEstNombreExtraccion getExtNombre() {
        return extNombre;
    }

    public void setExtNombre(SgEstNombreExtraccion extNombre) {
        this.extNombre = extNombre;
    }

    public EnumEstadoExtraccion getExtEstado() {
        return extEstado;
    }

    public void setExtEstado(EnumEstadoExtraccion extEstado) {
        this.extEstado = extEstado;
    }

    public String getExtDescripcion() {
        return extDescripcion;
    }

    public void setExtDescripcion(String extDescripcion) {
        this.extDescripcion = extDescripcion;
    }

    public LocalDate getExtFechaMatriculados() {
        return extFechaMatriculados;
    }

    public void setExtFechaMatriculados(LocalDate extFechaMatriculados) {
        this.extFechaMatriculados = extFechaMatriculados;
    }

    public List<SgAlcanceExtraccion> getExtAlcance() {
        return extAlcance;
    }

    public void setExtAlcance(List<SgAlcanceExtraccion> extAlcance) {
        this.extAlcance = extAlcance;
    }

    public List<SgGradoReportaEn> getExtGradoReportaEn() {
        return extGradoReportaEn;
    }

    public void setExtGradoReportaEn(List<SgGradoReportaEn> extGradoReportaEn) {
        this.extGradoReportaEn = extGradoReportaEn;
    }
    
    


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.extPk);
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
        final SgExtraccion other = (SgExtraccion) obj;
        if (!Objects.equals(this.extPk, other.extPk)) {
            return false;
        }
        return true;
    }

}
