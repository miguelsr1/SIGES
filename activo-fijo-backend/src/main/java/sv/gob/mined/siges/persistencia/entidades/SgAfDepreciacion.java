/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfProyectos;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_depreciaciones", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "depPk", scope = SgAfDepreciacion.class)
public class SgAfDepreciacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dep_pk")
    private Long depPk;
    
    @Basic(optional = false)
    @Column(name = "dep_anio")
    private Integer depAnio;
    
    @Basic(optional = false)
    @Column(name = "dep_mes")
    private Integer depMes;
    
    @Basic(optional = false)
    @Column(name = "dep_monto_depreciacion")
    private BigDecimal depMontoDepreciacion;
    
    @Basic(optional = false)
    @Column(name = "dep_ultimo_monto_depreciacion")
    private BigDecimal depUltimoMontoDepreciacion;
    
    @Size(max = 10)
    @Column(name = "dep_opreacion", length = 10)
    private String depOperacion;
    
    @Column(name = "dep_procesado")
    private Boolean depProcesado;
    
    @Column(name = "dep_no_dias_depreciados")
    private Integer depNoDiasDepreciados;
    
    @Basic(optional = false)
    @Column(name = "dep_fecha_calculo")
    private LocalDateTime depFechaCalculo;
    
    @JoinColumn(name = "dep_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgAfFuenteFinanciamiento depFuenteFinanciamientoFk;
      
    @JoinColumn(name = "dep_proyecto_fk", referencedColumnName = "pro_pk")
    @ManyToOne
    private SgAfProyectos depProyectoFk;

    @JoinColumn(name = "dep_bienes_depreciables_fk", referencedColumnName = "bde_pk")
    @ManyToOne(optional = false)
    private SgAfBienDepreciable depBienesDepreciablesFk;
    
    @Column(name = "dep_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime depUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dep_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String depUltModUsuario;
    
    @Column(name = "dep_version")
    @Version
    private Integer depVersion;
    

    public SgAfDepreciacion() {
    }

    public SgAfDepreciacion(Long depPk) {
        this.depPk = depPk;
    }

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public Integer getDepAnio() {
        return depAnio;
    }

    public void setDepAnio(Integer depAnio) {
        this.depAnio = depAnio;
    }

    public Integer getDepMes() {
        return depMes;
    }

    public void setDepMes(Integer depMes) {
        this.depMes = depMes;
    }

    public BigDecimal getDepMontoDepreciacion() {
        return depMontoDepreciacion;
    }

    public void setDepMontoDepreciacion(BigDecimal depMontoDepreciacion) {
        this.depMontoDepreciacion = depMontoDepreciacion;
    }

    public Integer getDepNoDiasDepreciados() {
        return depNoDiasDepreciados;
    }

    public void setDepNoDiasDepreciados(Integer depNoDiasDepreciados) {
        this.depNoDiasDepreciados = depNoDiasDepreciados;
    }

    public LocalDateTime getDepFechaCalculo() {
        return depFechaCalculo;
    }

    public void setDepFechaCalculo(LocalDateTime depFechaCalculo) {
        this.depFechaCalculo = depFechaCalculo;
    }

    public SgAfFuenteFinanciamiento getDepFuenteFinanciamientoFk() {
        return depFuenteFinanciamientoFk;
    }

    public void setDepFuenteFinanciamientoFk(SgAfFuenteFinanciamiento depFuenteFinanciamientoFk) {
        this.depFuenteFinanciamientoFk = depFuenteFinanciamientoFk;
    }

    public SgAfProyectos getDepProyectoFk() {
        return depProyectoFk;
    }

    public void setDepProyectoFk(SgAfProyectos depProyectoFk) {
        this.depProyectoFk = depProyectoFk;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    public SgAfBienDepreciable getDepBienesDepreciablesFk() {
        return depBienesDepreciablesFk;
    }

    public void setDepBienesDepreciablesFk(SgAfBienDepreciable depBienesDepreciablesFk) {
        this.depBienesDepreciablesFk = depBienesDepreciablesFk;
    }

    public BigDecimal getDepUltimoMontoDepreciacion() {
        return depUltimoMontoDepreciacion;
    }

    public void setDepUltimoMontoDepreciacion(BigDecimal depUltimoMontoDepreciacion) {
        this.depUltimoMontoDepreciacion = depUltimoMontoDepreciacion;
    }

    public String getDepOperacion() {
        return depOperacion;
    }

    public void setDepOperacion(String depOperacion) {
        this.depOperacion = depOperacion;
    }

    public Boolean getDepProcesado() {
        return depProcesado;
    }

    public void setDepProcesado(Boolean depProcesado) {
        this.depProcesado = depProcesado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depPk != null ? depPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDepreciacion)) {
            return false;
        }
        SgAfDepreciacion other = (SgAfDepreciacion) object;
        if ((this.depPk == null && other.depPk != null) || (this.depPk != null && !this.depPk.equals(other.depPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion[ depPk=" + depPk + " ]";
    }
    
}
