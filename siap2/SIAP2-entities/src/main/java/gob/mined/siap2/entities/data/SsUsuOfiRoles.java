/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data;

import gob.mined.siap2.entities.data.impl.UnidadTecnica;
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
import javax.persistence.Cacheable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_usu_ofi_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsUsuOfiRoles.findAll", query = "SELECT s FROM SsUsuOfiRoles s"),
    @NamedQuery(name = "SsUsuOfiRoles.findByUsuOfiRolesId", query = "SELECT s FROM SsUsuOfiRoles s WHERE s.usuOfiRolesId = :usuOfiRolesId"),
    @NamedQuery(name = "SsUsuOfiRoles.findByUsuOfiRolesOrigen", query = "SELECT s FROM SsUsuOfiRoles s WHERE s.usuOfiRolesOrigen = :usuOfiRolesOrigen"),
    @NamedQuery(name = "SsUsuOfiRoles.findByUsuOfiRolesUserCode", query = "SELECT s FROM SsUsuOfiRoles s WHERE s.usuOfiRolesUserCode = :usuOfiRolesUserCode")})
@Cacheable(false)
public class SsUsuOfiRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "seq_usu_ofi_rol", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_usu_ofi_rol", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usu_ofi_rol")
    @Basic(optional = false)
    @Column(name = "usu_ofi_roles_id")
    private Integer usuOfiRolesId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usu_ofi_roles_origen", nullable = false, length = 255)
    private String usuOfiRolesOrigen;

    @Basic(optional = false)
    @NotNull
    @Column(name = "usu_ofi_roles_user_code", nullable = false)
    private int usuOfiRolesUserCode;

    @JoinColumn(name = "usu_ofi_roles_rol", referencedColumnName = "rol_id", nullable = false)
    @ManyToOne(optional = false)
    private SsRol usuOfiRolesRol;

    @JoinColumn(name = "usu_ofi_roles_usuario", referencedColumnName = "usu_id", nullable = false)
    @ManyToOne(optional = false)
    private SsUsuario usuOfiRolesUsuario;

    @JoinColumn(name = "usu_ofi_roles_oficina", referencedColumnName = "ofi_id", nullable = false)
    @ManyToOne(optional = false)
    private SsOficina usuOfiRolesOficina;

    @JoinColumn(name = "usu_ofi_roles_unidad", referencedColumnName = "uni_id", nullable = true)
    @ManyToOne(optional = true)
    private UnidadTecnica usuOfiRolesUnidadTecnica;

    public SsUsuOfiRoles() {
    }

    public SsUsuOfiRoles(Integer usuOfiRolesId) {
        this.usuOfiRolesId = usuOfiRolesId;
    }

    public SsUsuOfiRoles(Integer usuOfiRolesId, String usuOfiRolesOrigen, int usuOfiRolesUserCode) {
        this.usuOfiRolesId = usuOfiRolesId;
        this.usuOfiRolesOrigen = usuOfiRolesOrigen;
        this.usuOfiRolesUserCode = usuOfiRolesUserCode;
    }

    public Integer getUsuOfiRolesId() {
        return usuOfiRolesId;
    }

    public void setUsuOfiRolesId(Integer usuOfiRolesId) {
        this.usuOfiRolesId = usuOfiRolesId;
    }

    public String getUsuOfiRolesOrigen() {
        return usuOfiRolesOrigen;
    }

    public void setUsuOfiRolesOrigen(String usuOfiRolesOrigen) {
        this.usuOfiRolesOrigen = usuOfiRolesOrigen;
    }

    public int getUsuOfiRolesUserCode() {
        return usuOfiRolesUserCode;
    }

    public void setUsuOfiRolesUserCode(int usuOfiRolesUserCode) {
        this.usuOfiRolesUserCode = usuOfiRolesUserCode;
    }

    public SsRol getUsuOfiRolesRol() {
        return usuOfiRolesRol;
    }

    public void setUsuOfiRolesRol(SsRol usuOfiRolesRol) {
        this.usuOfiRolesRol = usuOfiRolesRol;
    }

    public SsUsuario getUsuOfiRolesUsuario() {
        return usuOfiRolesUsuario;
    }

    public void setUsuOfiRolesUsuario(SsUsuario usuOfiRolesUsuario) {
        this.usuOfiRolesUsuario = usuOfiRolesUsuario;
    }

    public SsOficina getUsuOfiRolesOficina() {
        return usuOfiRolesOficina;
    }

    public void setUsuOfiRolesOficina(SsOficina usuOfiRolesOficina) {
        this.usuOfiRolesOficina = usuOfiRolesOficina;
    }

    public UnidadTecnica getUsuOfiRolesUnidadTecnica() {
        return usuOfiRolesUnidadTecnica;
    }

    public void setUsuOfiRolesUnidadTecnica(UnidadTecnica usuOfiRolesUnidadTecnica) {
        this.usuOfiRolesUnidadTecnica = usuOfiRolesUnidadTecnica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuOfiRolesId != null ? usuOfiRolesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsUsuOfiRoles)) {
            return false;
        }
        SsUsuOfiRoles other = (SsUsuOfiRoles) object;
        if ((this.usuOfiRolesId == null && other.usuOfiRolesId != null) || (this.usuOfiRolesId != null && !this.usuOfiRolesId.equals(other.usuOfiRolesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ usuOfiRolesId=" + usuOfiRolesId + " ]";
    }

}
