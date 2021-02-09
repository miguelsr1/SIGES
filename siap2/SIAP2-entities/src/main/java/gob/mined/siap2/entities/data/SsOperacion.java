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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_operacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsOperacion.findAll", query = "SELECT s FROM SsOperacion s"),
    @NamedQuery(name = "SsOperacion.findByOpeId", query = "SELECT s FROM SsOperacion s WHERE s.opeId = :opeId"),
    @NamedQuery(name = "SsOperacion.findByOpeCodigo", query = "SELECT s FROM SsOperacion s WHERE s.opeCodigo = :opeCodigo"),
    @NamedQuery(name = "SsOperacion.findByOpeNombre", query = "SELECT s FROM SsOperacion s WHERE s.opeNombre = :opeNombre"),
    @NamedQuery(name = "SsOperacion.findByOpeOrigen", query = "SELECT s FROM SsOperacion s WHERE s.opeOrigen = :opeOrigen"),
    @NamedQuery(name = "SsOperacion.findByOpeTipocampo", query = "SELECT s FROM SsOperacion s WHERE s.opeTipocampo = :opeTipocampo"),
    @NamedQuery(name = "SsOperacion.findByOpeUserCode", query = "SELECT s FROM SsOperacion s WHERE s.opeUserCode = :opeUserCode"),
    @NamedQuery(name = "SsOperacion.findByOpeVersion", query = "SELECT s FROM SsOperacion s WHERE s.opeVersion = :opeVersion"),
    @NamedQuery(name = "SsOperacion.findByOpeVigente", query = "SELECT s FROM SsOperacion s WHERE s.opeVigente = :opeVigente")})
public class SsOperacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "seq_operacion", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_operacion", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operacion")
    @Basic(optional = false)
    @Column(name = "ope_id", nullable = false)
    private Integer opeId;
    @Column(name = "ope_codigo")
    private String opeCodigo;
    @Column(name = "ope_descripcion")
    private String opeDescripcion;
    @Column(name = "ope_nombre")
    private String opeNombre;
    @Column(name = "ope_origen")
    private String opeOrigen;
    @Column(name = "ope_tipocampo")
    private String opeTipocampo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ope_user_code", nullable = false)
    private int opeUserCode;
    @Column(name = "ope_version")
    private Integer opeVersion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ope_vigente", nullable = false)
    private boolean opeVigente;
    @JoinColumn(name = "ope_categoria_id")
    @ManyToOne
    private SsCategoper opeCategoriaId;

    public SsOperacion() {
    }

    public SsOperacion(Integer opeId) {
        this.opeId = opeId;
    }

    public SsOperacion(Integer opeId, String opeCodigo, String opeDescripcion, String opeNombre, String opeOrigen, String opeTipocampo, int opeUserCode, boolean opeVigente) {
        this.opeId = opeId;
        this.opeCodigo = opeCodigo;
        this.opeDescripcion = opeDescripcion;
        this.opeNombre = opeNombre;
        this.opeOrigen = opeOrigen;
        this.opeTipocampo = opeTipocampo;
        this.opeUserCode = opeUserCode;
        this.opeVigente = opeVigente;
    }

    public Integer getOpeId() {
        return opeId;
    }

    public void setOpeId(Integer opeId) {
        this.opeId = opeId;
    }

    public String getOpeCodigo() {
        return opeCodigo;
    }

    public void setOpeCodigo(String opeCodigo) {
        this.opeCodigo = opeCodigo;
    }

    public String getOpeDescripcion() {
        return opeDescripcion;
    }

    public void setOpeDescripcion(String opeDescripcion) {
        this.opeDescripcion = opeDescripcion;
    }

    public String getOpeNombre() {
        return opeNombre;
    }

    public void setOpeNombre(String opeNombre) {
        this.opeNombre = opeNombre;
    }

    public String getOpeOrigen() {
        return opeOrigen;
    }

    public void setOpeOrigen(String opeOrigen) {
        this.opeOrigen = opeOrigen;
    }

    public String getOpeTipocampo() {
        return opeTipocampo;
    }

    public void setOpeTipocampo(String opeTipocampo) {
        this.opeTipocampo = opeTipocampo;
    }

    public int getOpeUserCode() {
        return opeUserCode;
    }

    public void setOpeUserCode(int opeUserCode) {
        this.opeUserCode = opeUserCode;
    }

    public Integer getOpeVersion() {
        return opeVersion;
    }

    public void setOpeVersion(Integer opeVersion) {
        this.opeVersion = opeVersion;
    }

    public boolean getOpeVigente() {
        return opeVigente;
    }

    public void setOpeVigente(boolean opeVigente) {
        this.opeVigente = opeVigente;
    }

    public SsCategoper getOpeCategoriaId() {
        return opeCategoriaId;
    }

    public void setOpeCategoriaId(SsCategoper opeCategoriaId) {
        this.opeCategoriaId = opeCategoriaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opeId != null ? opeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsOperacion)) {
            return false;
        }
        SsOperacion other = (SsOperacion) object;
        if ((this.opeId == null && other.opeId != null) || (this.opeId != null && !this.opeId.equals(other.opeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ opeId=" + opeId + " ]";
    }

}
