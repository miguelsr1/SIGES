/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Cacheable;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsRol.findAll", query = "SELECT s FROM SsRol s"),
    @NamedQuery(name = "SsRol.findByRolId", query = "SELECT s FROM SsRol s WHERE s.rolId = :rolId"),
    @NamedQuery(name = "SsRol.findByRolCod", query = "SELECT s FROM SsRol s WHERE s.rolCod = :rolCod"),
    @NamedQuery(name = "SsRol.findByRolNombre", query = "SELECT s FROM SsRol s WHERE s.rolNombre = :rolNombre"),
    @NamedQuery(name = "SsRol.findByRolOrigen", query = "SELECT s FROM SsRol s WHERE s.rolOrigen = :rolOrigen"),
    @NamedQuery(name = "SsRol.findByRolUserCode", query = "SELECT s FROM SsRol s WHERE s.rolUserCode = :rolUserCode"),
    @NamedQuery(name = "SsRol.findByRolVersion", query = "SELECT s FROM SsRol s WHERE s.rolVersion = :rolVersion"),
    @NamedQuery(name = "SsRol.findByRolVigente", query = "SELECT s FROM SsRol s WHERE s.rolVigente = :rolVigente")})
@Cacheable(false)
public class SsRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "seq_rol", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rol", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rol")
    @Basic(optional = false)
    @Column(name = "rol_id", nullable = false)
    private Integer rolId;
    
    @Column(name = "rol_cod")
    private String rolCod;
    
    @Column(name = "rol_descripcion")
    private String rolDescripcion;
    
    @Column(name = "rol_nombre")
    private String rolNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "rol_origen", nullable = false, length = 255)
    private String rolOrigen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol_user_code", nullable = false)
    private int rolUserCode;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol_vigente", nullable = false)
    private boolean rolVigente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuOfiRolesRol")
    private Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection;
    
    //Auditoria
    @Column(name = "rol_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "rol_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;
    
    @Column(name = "rol_version")
    @Version
    private Integer rolVersion;
    

    public SsRol() {
    }

    public SsRol(Integer rolId) {
        this.rolId = rolId;
    }

    public SsRol(Integer rolId, String rolCod, String rolNombre, String rolOrigen, int rolUserCode, boolean rolVigente) {
        this.rolId = rolId;
        this.rolCod = rolCod;
        this.rolNombre = rolNombre;
        this.rolOrigen = rolOrigen;
        this.rolUserCode = rolUserCode;
        this.rolVigente = rolVigente;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getRolCod() {
        return rolCod;
    }

    public void setRolCod(String rolCod) {
        this.rolCod = rolCod;
    }

    public String getRolDescripcion() {
        return rolDescripcion;
    }

    public void setRolDescripcion(String rolDescripcion) {
        this.rolDescripcion = rolDescripcion;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getRolOrigen() {
        return rolOrigen;
    }

    public void setRolOrigen(String rolOrigen) {
        this.rolOrigen = rolOrigen;
    }

    public int getRolUserCode() {
        return rolUserCode;
    }

    public void setRolUserCode(int rolUserCode) {
        this.rolUserCode = rolUserCode;
    }

    public Integer getRolVersion() {
        return rolVersion;
    }

    public void setRolVersion(Integer rolVersion) {
        this.rolVersion = rolVersion;
    }

    public boolean getRolVigente() {
        return rolVigente;
    }

    public void setRolVigente(boolean rolVigente) {
        this.rolVigente = rolVigente;
    }

    @XmlTransient
    public Collection<SsUsuOfiRoles> getSsUsuOfiRolesCollection() {
        return ssUsuOfiRolesCollection;
    }

    public void setSsUsuOfiRolesCollection(Collection<SsUsuOfiRoles> ssUsuOfiRolesCollection) {
        this.ssUsuOfiRolesCollection = ssUsuOfiRolesCollection;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolId != null ? rolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsRol)) {
            return false;
        }
        SsRol other = (SsRol) object;
        if ((this.rolId == null && other.rolId != null) || (this.rolId != null && !this.rolId.equals(other.rolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ rolId=" + rolId + " ]";
    }

}
