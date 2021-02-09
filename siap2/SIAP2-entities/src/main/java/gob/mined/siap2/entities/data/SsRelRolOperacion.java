/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_rol_operacion")
@XmlRootElement
public class SsRelRolOperacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_rel_rol_operac", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_rol_operac", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_rol_operac")
    @Basic(optional = false)
    @Column(name = "rel_rol_operacion_id")
    private Integer relRolOperacionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rel_rol_operacion_editable", nullable = false)
    private boolean relRolOperacionEditable;
    @Column(name = "rel_rol_operacion_origen")
    private String relRolOperacionOrigen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rel_rol_operacion_visible", nullable = false)
    private boolean relRolOperacionVisible;
    @JoinColumn(name = "rel_rol_operacion_operacion_id", referencedColumnName = "ope_id", nullable = false)
    @ManyToOne(optional = false)
    private SsOperacion relRolOperacionOperacionId;
    @JoinColumn(name = "rel_rol_operacion_rol_id", referencedColumnName = "rol_id", nullable = false)
    @ManyToOne(optional = false)
    private SsRol relRolOperacionRolId;

    public SsRelRolOperacion() {
    }

    public SsRelRolOperacion(Integer relRolOperacionId) {
        this.relRolOperacionId = relRolOperacionId;
    }

    public Integer getRelRolOperacionId() {
        return relRolOperacionId;
    }

    public void setRelRolOperacionId(Integer relRolOperacionId) {
        this.relRolOperacionId = relRolOperacionId;
    }

    public boolean getRelRolOperacionEditable() {
        return relRolOperacionEditable;
    }

    public void setRelRolOperacionEditable(boolean relRolOperacionEditable) {
        this.relRolOperacionEditable = relRolOperacionEditable;
    }

    public String getRelRolOperacionOrigen() {
        return relRolOperacionOrigen;
    }

    public void setRelRolOperacionOrigen(String relRolOperacionOrigen) {
        this.relRolOperacionOrigen = relRolOperacionOrigen;
    }

    public boolean getRelRolOperacionVisible() {
        return relRolOperacionVisible;
    }

    public void setRelRolOperacionVisible(boolean relRolOperacionVisible) {
        this.relRolOperacionVisible = relRolOperacionVisible;
    }

    public SsOperacion getRelRolOperacionOperacionId() {
        return relRolOperacionOperacionId;
    }

    public void setRelRolOperacionOperacionId(SsOperacion relRoloperacionOperacionId) {
        this.relRolOperacionOperacionId = relRoloperacionOperacionId;
    }

    public SsRol getRelRolOperacionRolId() {
        return relRolOperacionRolId;
    }

    public void setRelRolOperacionRolId(SsRol relRolOperacionRolId) {
        this.relRolOperacionRolId = relRolOperacionRolId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relRolOperacionId != null ? relRolOperacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsRelRolOperacion)) {
            return false;
        }
        SsRelRolOperacion other = (SsRelRolOperacion) object;
        if ((this.relRolOperacionId == null && other.relRolOperacionId != null) || (this.relRolOperacionId != null && !this.relRolOperacionId.equals(other.relRolOperacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ relRolOperacionId=" + relRolOperacionId + " ]";
    }

}
