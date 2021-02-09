/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumEstadoDatoEmpleado;

@Entity
@Table(name = "sg_datos_empleado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "demPk", scope = SgDatoEmpleadoLite.class)
@Audited
public class SgDatoEmpleadoLite implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dem_pk")
    private Long demPk;
       
    @Size(max = 45)
    @Column(name = "dem_codigo_empleado", length = 45)
    private String demCodigoEmpleado;
    
    @Column(name = "dem_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoDatoEmpleado demEstado;
    
    @Column(name = "dem_fecha_ingreso_gob")
    private LocalDate demFechaIngresoGob;
    
    @Column(name = "dem_fecha_ingreso_mined")
    private LocalDate demFechaIngresoMined;
    
    @Column(name = "dem_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime demUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dem_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String demUltModUsuario;
    
    @Column(name = "dem_version")
    @Version
    private Integer demVersion;
 
    
    public SgDatoEmpleadoLite() {
    }

    public SgDatoEmpleadoLite(Long demPk, Integer demVersion) {
        this.demPk = demPk;
        this.demVersion = demVersion;
    }    
    
    
    @PrePersist
    @PreUpdate
    public void preSave() throws Exception {
        throw new Exception("Esta clase no debe ser guardada por si sola. Debe utilizarse para asociaciones EntidadPadre - SgDatoEmpleado");
    }

    public SgDatoEmpleadoLite(Long demPk) {
        this.demPk = demPk;
    }

    public Long getDemPk() {
        return demPk;
    }

    public void setDemPk(Long demPk) {
        this.demPk = demPk;
    }

    public String getDemCodigoEmpleado() {
        return demCodigoEmpleado;
    }

    public void setDemCodigoEmpleado(String demCodigoEmpleado) {
        this.demCodigoEmpleado = demCodigoEmpleado;
    }

    public EnumEstadoDatoEmpleado getDemEstado() {
        return demEstado;
    }

    public void setDemEstado(EnumEstadoDatoEmpleado demEstado) {
        this.demEstado = demEstado;
    }


    public LocalDate getDemFechaIngresoGob() {
        return demFechaIngresoGob;
    }

    public void setDemFechaIngresoGob(LocalDate demFechaIngresoGob) {
        this.demFechaIngresoGob = demFechaIngresoGob;
    }

    public LocalDate getDemFechaIngresoMined() {
        return demFechaIngresoMined;
    }

    public void setDemFechaIngresoMined(LocalDate demFechaIngresoMined) {
        this.demFechaIngresoMined = demFechaIngresoMined;
    }


    public LocalDateTime getDemUltModFecha() {
        return demUltModFecha;
    }

    public void setDemUltModFecha(LocalDateTime demUltModFecha) {
        this.demUltModFecha = demUltModFecha;
    }

    public String getDemUltModUsuario() {
        return demUltModUsuario;
    }

    public void setDemUltModUsuario(String demUltModUsuario) {
        this.demUltModUsuario = demUltModUsuario;
    }

    public Integer getDemVersion() {
        return demVersion;
    }

    public void setDemVersion(Integer demVersion) {
        this.demVersion = demVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (demPk != null ? demPk.hashCode() : 0);
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
        final SgDatoEmpleadoLite other = (SgDatoEmpleadoLite) obj;
        if (!Objects.equals(this.demPk, other.demPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado[ demPk=" + demPk + " ]";
    }
    
}
