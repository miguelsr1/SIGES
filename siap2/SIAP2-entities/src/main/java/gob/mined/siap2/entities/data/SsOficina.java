/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_oficina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsOficina.findAll", query = "SELECT s FROM SsOficina s"),
    @NamedQuery(name = "SsOficina.findByOfiId", query = "SELECT s FROM SsOficina s WHERE s.ofiId = :ofiId"),
    @NamedQuery(name = "SsOficina.findByOfiFechaCreacion", query = "SELECT s FROM SsOficina s WHERE s.ofiFechaCreacion = :ofiFechaCreacion"),
    @NamedQuery(name = "SsOficina.findByOfiNombre", query = "SELECT s FROM SsOficina s WHERE s.ofiNombre = :ofiNombre"),
    @NamedQuery(name = "SsOficina.findByOfiOrigen", query = "SELECT s FROM SsOficina s WHERE s.ofiOrigen = :ofiOrigen"),
    @NamedQuery(name = "SsOficina.findByOfiUsuario", query = "SELECT s FROM SsOficina s WHERE s.ofiUsuario = :ofiUsuario"),
    @NamedQuery(name = "SsOficina.findByOfiVersion", query = "SELECT s FROM SsOficina s WHERE s.ofiVersion = :ofiVersion")})
public class SsOficina implements Serializable {
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "seq_oficina2", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_oficina2", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_oficina2")
    @Basic(optional = false)
    @Column(name = "ofi_id")
    private Integer ofiId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ofi_fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ofiFechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ofi_nombre", nullable = false, length = 255)
    private String ofiNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ofi_origen", nullable = false, length = 255)
    private String ofiOrigen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ofi_usuario", nullable = false)
    private int ofiUsuario;
    @Column(name = "ofi_version")
    private Integer ofiVersion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuOfiRolesOficina")
    private Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection;

    public SsOficina() {
    }

    public SsOficina(Integer ofiId) {
        this.ofiId = ofiId;
    }


    public Integer getOfiId() {
        return ofiId;
    }

    public void setOfiId(Integer ofiId) {
        this.ofiId = ofiId;
    }

    public Date getOfiFechaCreacion() {
        return ofiFechaCreacion;
    }

    public void setOfiFechaCreacion(Date ofiFechaCreacion) {
        this.ofiFechaCreacion = ofiFechaCreacion;
    }

    public String getOfiNombre() {
        return ofiNombre;
    }

    public void setOfiNombre(String ofiNombre) {
        this.ofiNombre = ofiNombre;
    }

    public String getOfiOrigen() {
        return ofiOrigen;
    }

    public void setOfiOrigen(String ofiOrigen) {
        this.ofiOrigen = ofiOrigen;
    }

    public int getOfiUsuario() {
        return ofiUsuario;
    }

    public void setOfiUsuario(int ofiUsuario) {
        this.ofiUsuario = ofiUsuario;
    }
    
    public Integer getOfiVersion() {
        return ofiVersion;
    }

    public void setOfiVersion(Integer ofiVersion) {
        this.ofiVersion = ofiVersion;
    }

    @XmlTransient
    public Collection<SsUsuOfiRoles> getSsUsuOfiRolesCollection() {
        return ssUsuOfiRolesCollection;
    }

    public void setSsUsuOfiRolesCollection(Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection) {
        this.ssUsuOfiRolesCollection = ssUsuOfiRolesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ofiId != null ? ofiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsOficina)) {
            return false;
        }
        SsOficina other = (SsOficina) object;
        if ((this.ofiId == null && other.ofiId != null) || (this.ofiId != null && !this.ofiId.equals(other.ofiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ ofiId=" + ofiId + " ]";
    }
    
    
    
    public boolean isTipoOficina(String tipo){
        if (ofiOrigen != null && tipo != null ) {
            return ofiOrigen.equals(tipo);
        }else{
            return (tipo == null && ofiOrigen == null);
        }      
    }
}
