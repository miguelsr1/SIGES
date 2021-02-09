/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_categoper")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsCategoper.findAll", query = "SELECT s FROM SsCategoper s"),
    @NamedQuery(name = "SsCategoper.findByCatId", query = "SELECT s FROM SsCategoper s WHERE s.catId = :catId"),
    @NamedQuery(name = "SsCategoper.findByCatNombre", query = "SELECT s FROM SsCategoper s WHERE s.catNombre = :catNombre"),
    @NamedQuery(name = "SsCategoper.findByCatOrigen", query = "SELECT s FROM SsCategoper s WHERE s.catOrigen = :catOrigen"),
    @NamedQuery(name = "SsCategoper.findByCatUserCode", query = "SELECT s FROM SsCategoper s WHERE s.catUserCode = :catUserCode"),
    @NamedQuery(name = "SsCategoper.findByCatVersion", query = "SELECT s FROM SsCategoper s WHERE s.catVersion = :catVersion"),
    @NamedQuery(name = "SsCategoper.findByCatVigente", query = "SELECT s FROM SsCategoper s WHERE s.catVigente = :catVigente")})
public class SsCategoper implements Serializable {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_categoper", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_categoper", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoper")
    @Basic(optional = false)
    @Column(name = "cat_id")
    private Integer catId;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "cat_descripcion", length = 2147483647)
    private String catDescripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cat_nombre", nullable = false, length = 255)
    private String catNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cat_origen", nullable = false, length = 255)
    private String catOrigen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cat_user_code", nullable = false)
    private int catUserCode;
    @Column(name = "cat_version")
    private Integer catVersion;
    @Column(name = "cat_vigente")
    private Boolean catVigente;
    @OneToMany(mappedBy = "opeCategoriaId")
    private Collection<SsOperacion> ssOperacionCollection;

    public SsCategoper() {
    }

    public SsCategoper(Integer catId) {
        this.catId = catId;
    }

    public SsCategoper(Integer catId, String catNombre, String catOrigen, int catUserCode) {
        this.catId = catId;
        this.catNombre = catNombre;
        this.catOrigen = catOrigen;
        this.catUserCode = catUserCode;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatDescripcion() {
        return catDescripcion;
    }

    public void setCatDescripcion(String catDescripcion) {
        this.catDescripcion = catDescripcion;
    }

    public String getCatNombre() {
        return catNombre;
    }

    public void setCatNombre(String catNombre) {
        this.catNombre = catNombre;
    }

    public String getCatOrigen() {
        return catOrigen;
    }

    public void setCatOrigen(String catOrigen) {
        this.catOrigen = catOrigen;
    }

    public int getCatUserCode() {
        return catUserCode;
    }

    public void setCatUserCode(int catUserCode) {
        this.catUserCode = catUserCode;
    }

    public Integer getCatVersion() {
        return catVersion;
    }

    public void setCatVersion(Integer catVersion) {
        this.catVersion = catVersion;
    }

    public Boolean getCatVigente() {
        return catVigente;
    }

    public void setCatVigente(Boolean catVigente) {
        this.catVigente = catVigente;
    }

    @XmlTransient
    public Collection<SsOperacion> getSsOperacionCollection() {
        return ssOperacionCollection;
    }

    public void setSsOperacionCollection(Collection<SsOperacion> ssOperacionCollection) {
        this.ssOperacionCollection = ssOperacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catId != null ? catId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsCategoper)) {
            return false;
        }
        SsCategoper other = (SsCategoper) object;
        if ((this.catId == null && other.catId != null) || (this.catId != null && !this.catId.equals(other.catId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ catId=" + catId + " ]";
    }

}
