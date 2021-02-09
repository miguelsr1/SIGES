/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumPlanCompraEstado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_encabezado_plan_compra", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plaPk", scope = SgEncabezadoPlanCompras.class)
@Audited
/**
 * Entidad correspondiente al plan de compras de los centros educativos.
 */
public class SgEncabezadoPlanCompras implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pla_pk", nullable = false)
    private Long plaPk;

    @JoinColumn(name = "pla_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPresupuestoEscolar plaPresupuestoFk;

    @Column(name = "pla_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumPlanCompraEstado plaEstado;

    @Size(max = 8000)
    @Column(name = "pla_comentario", length = 8000)
    private String plaComentario;

    @Column(name = "pla_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime plaUltModFecha;

    @Size(max = 45)
    @Column(name = "pla_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String plaUltModUsuario;

    @Column(name = "pla_version")
    @Version
    private Integer plaVersion;

    public SgEncabezadoPlanCompras() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public SgPresupuestoEscolar getPlaPresupuestoFk() {
        return plaPresupuestoFk;
    }

    public void setPlaPresupuestoFk(SgPresupuestoEscolar plaPresupuestoFk) {
        this.plaPresupuestoFk = plaPresupuestoFk;
    }

    public EnumPlanCompraEstado getPlaEstado() {
        return plaEstado;
    }

    public void setPlaEstado(EnumPlanCompraEstado plaEstado) {
        this.plaEstado = plaEstado;
    }

    public String getPlaComentario() {
        return plaComentario;
    }

    public void setPlaComentario(String plaComentario) {
        this.plaComentario = plaComentario;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        return Objects.hashCode(this.plaPk);
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
        final SgEncabezadoPlanCompras other = (SgEncabezadoPlanCompras) obj;
        if (!Objects.equals(this.plaPk, other.plaPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgEncabezadoPlanCompras[ plaPk=" + plaPk + " ]";
    }
    // </editor-fold>

}
