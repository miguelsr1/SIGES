/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoExtraccion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstDatasets;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_extracciones", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "extPk", scope = SgExtraccion.class)
public class SgExtraccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ext_pk", nullable = false)
    private Long extPk;

    @Column(name = "ext_anio")
    private Integer extAnio;
    
    @ManyToOne
    @JoinColumn(name = "ext_dataset_fk")
    private SgEstDatasets extDataset;
    
    @Column(name = "ext_descripcion")
    private String extDescripcion;
    
    @Column(name = "ext_fecha_matriculados")
    private LocalDate extFechaMatriculados; // Sin uso. Ahora se utiliza extAlcance
    
    @ManyToOne
    @JoinColumn(name = "ext_nombre_fk")
    private SgEstNombreExtraccion extNombre;
    
    @Column(name = "ext_estado")
    @Enumerated(EnumType.STRING)
    private EnumEstadoExtraccion extEstado;
    
    
    @AtributoUltimaModificacion
    @Column(name = "ext_ult_mod_fecha")
    private LocalDateTime extUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "ext_ult_mod_usuario", length = 45)
    private String extUltmodUsuario;

    @Column(name = "ext_version")
    @Version
    private Integer extVersion;
    
    
    @OneToMany(mappedBy = "alcExtraccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAlcanceExtraccion> extAlcance;
    
    @OneToMany(mappedBy = "repExtraccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgGradoReportaEn> extGradoReportaEn;
    
   
    public Long getExtPk() {
        return extPk;
    }

    public void setExtPk(Long extPk) {
        this.extPk = extPk;
    }

    public List<SgAlcanceExtraccion> getExtAlcance() {
        return extAlcance;
    }

    public void setExtAlcance(List<SgAlcanceExtraccion> extAlcance) {
        this.extAlcance = extAlcance;
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
