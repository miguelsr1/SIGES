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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoLiquidacion;

/**
 * Entidad correspondiente a las liquidaciones de otros ingresos
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_liquidaciones_otros_ingresos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "loiPk", scope = SgLiquidacionOtroIngreso.class)
@Audited
public class SgLiquidacionOtroIngreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "loi_pk", nullable = false)
    private Long loiPk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loi_sede_fk", referencedColumnName = "sed_pk")
    private SgSede loiSedePk;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loi_anio_fk", referencedColumnName = "ale_pk")
    private SgAnioLectivo loiAnioPk;
    
    @Column(name = "loi_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoLiquidacion loiEstado;

    @Column(name = "loi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime loiUltModFecha;

    @Size(max = 45)
    @Column(name = "loi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String loiUltModUsuario;

    @Column(name = "loi_version")
    @Version
    private Integer loiVersion;

    public SgLiquidacionOtroIngreso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getLoiPk() {
        return loiPk;
    }

    public void setLoiPk(Long loiPk) {
        this.loiPk = loiPk;
    }

    public SgSede getLoiSedePk() {
        return loiSedePk;
    }

    public void setLoiSedePk(SgSede loiSedePk) {
        this.loiSedePk = loiSedePk;
    }

    public SgAnioLectivo getLoiAnioPk() {
        return loiAnioPk;
    }

    public void setLoiAnioPk(SgAnioLectivo loiAnioPk) {
        this.loiAnioPk = loiAnioPk;
    }

    public EnumEstadoLiquidacion getLoiEstado() {
        return loiEstado;
    }

    public void setLoiEstado(EnumEstadoLiquidacion loiEstado) {
        this.loiEstado = loiEstado;
    }

    public LocalDateTime getLoiUltModFecha() {
        return loiUltModFecha;
    }

    public void setLoiUltModFecha(LocalDateTime loiUltModFecha) {
        this.loiUltModFecha = loiUltModFecha;
    }

    public String getLoiUltModUsuario() {
        return loiUltModUsuario;
    }

    public void setLoiUltModUsuario(String loiUltModUsuario) {
        this.loiUltModUsuario = loiUltModUsuario;
    }

    public Integer getLoiVersion() {
        return loiVersion;
    }

    public void setLoiVersion(Integer loiVersion) {
        this.loiVersion = loiVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.loiPk);
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
        final SgLiquidacionOtroIngreso other = (SgLiquidacionOtroIngreso) obj;
        if (!Objects.equals(this.loiPk, other.loiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgLiquidacionOtroIngreso{" + "loiPk=" + loiPk + ", loiEstado=" + loiEstado + ", loiUltModFecha=" + loiUltModFecha + ", loiUltModUsuario=" + loiUltModUsuario + ", loiVersion=" + loiVersion + '}';
    }


}
