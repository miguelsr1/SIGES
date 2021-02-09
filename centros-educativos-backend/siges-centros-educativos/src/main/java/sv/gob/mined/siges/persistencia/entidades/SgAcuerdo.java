/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_acuerdo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "acuPk", scope = SgAcuerdo.class)
@Audited
public class SgAcuerdo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acu_pk", nullable = false)
    private Long acuPk;
    
    @Column(name = "acu_nombre_acuerdo")
    private String acuNombreAcuerdo;
    
    @Column(name = "acu_descripcion")
    private String acuDescripcion;
    
    @Column(name = "acu_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoAcuerdo acuEstado;
    
    @Column(name = "acu_fecha")
    private LocalDate acuFecha;
    
    @Column(name = "acu_aplica_sistemas_integrados")
    private Boolean acuAplicaSistemasIntegrados;

    @Column(name = "acu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime acuUltModFecha;

    @Size(max = 45)
    @Column(name = "acu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String acuUltModUsuario;

    @Column(name = "acu_version")
    @Version
    private Integer acuVersion;
    
    @JoinColumn(name = "acu_propuesta_pedagogica_fk", referencedColumnName = "ppe_pk")
    @ManyToOne
    private SgPropuestaPedagogica acuPropuestaPedagogica;

    public SgAcuerdo() {
    }

 

    public Long getAcuPk() {
        return acuPk;
    }

    public void setAcuPk(Long acuPk) {
        this.acuPk = acuPk;
    }

    public String getAcuNombreAcuerdo() {
        return acuNombreAcuerdo;
    }

    public void setAcuNombreAcuerdo(String acuNombreAcuerdo) {
        this.acuNombreAcuerdo = acuNombreAcuerdo;
    }

    public String getAcuDescripcion() {
        return acuDescripcion;
    }

    public void setAcuDescripcion(String acuDescripcion) {
        this.acuDescripcion = acuDescripcion;
    }

    public EnumEstadoAcuerdo getAcuEstado() {
        return acuEstado;
    }

    public void setAcuEstado(EnumEstadoAcuerdo acuEstado) {
        this.acuEstado = acuEstado;
    }

    public LocalDate getAcuFecha() {
        return acuFecha;
    }

    public void setAcuFecha(LocalDate acuFecha) {
        this.acuFecha = acuFecha;
    }

    public Boolean getAcuAplicaSistemasIntegrados() {
        return acuAplicaSistemasIntegrados;
    }

    public void setAcuAplicaSistemasIntegrados(Boolean acuAplicaSistemasIntegrados) {
        this.acuAplicaSistemasIntegrados = acuAplicaSistemasIntegrados;
    }

 

    public LocalDateTime getAcuUltModFecha() {
        return acuUltModFecha;
    }

    public void setAcuUltModFecha(LocalDateTime acuUltModFecha) {
        this.acuUltModFecha = acuUltModFecha;
    }

    public String getAcuUltModUsuario() {
        return acuUltModUsuario;
    }

    public void setAcuUltModUsuario(String acuUltModUsuario) {
        this.acuUltModUsuario = acuUltModUsuario;
    }

    public Integer getAcuVersion() {
        return acuVersion;
    }

    public void setAcuVersion(Integer acuVersion) {
        this.acuVersion = acuVersion;
    }

    public SgPropuestaPedagogica getAcuPropuestaPedagogica() {
        return acuPropuestaPedagogica;
    }

    public void setAcuPropuestaPedagogica(SgPropuestaPedagogica acuPropuestaPedagogica) {
        this.acuPropuestaPedagogica = acuPropuestaPedagogica;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.acuPk);
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
        final SgAcuerdo other = (SgAcuerdo) obj;
        if (!Objects.equals(this.acuPk, other.acuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAcuerdo{" + "acuPk=" + acuPk + " acuUltModFecha=" + acuUltModFecha + ", acuUltModUsuario=" + acuUltModUsuario + ", acuVersion=" + acuVersion + '}';
    }
    
    

}
