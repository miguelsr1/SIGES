/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_componentes", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpnPk", scope = SgComponente.class)
@Audited
public class SgComponente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpn_pk", nullable = false)
    private Long cpnPk;

    @JoinColumn(name = "cpn_proyecto_institucional_fk", referencedColumnName = "pin_pk")
    @ManyToOne(optional = false)
    private SgProyectoInstitucional cpnProyectoInstitucional;
    
    @Size(max = 100)
    @Column(name = "cpn_nombre", length = 100)
    private String cpnNombre;

    @Size(max = 255)
    @Column(name = "cpn_descripcion", length = 255)
    private String cpnDescripcion;

    @Column(name = "cpn_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cpnUltModFecha;

    @Size(max = 45)
    @Column(name = "cpn_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cpnUltModUsuario;

    @Column(name = "cpn_version")
    @Version
    private Integer cpnVersion;

    public SgComponente() {
    }

    public Long getCpnPk() {
        return cpnPk;
    }

    public void setCpnPk(Long cpnPk) {
        this.cpnPk = cpnPk;
    }

    public SgProyectoInstitucional getCpnProyectoInstitucional() {
        return cpnProyectoInstitucional;
    }

    public void setCpnProyectoInstitucional(SgProyectoInstitucional cpnProyectoInstitucional) {
        this.cpnProyectoInstitucional = cpnProyectoInstitucional;
    }

    public String getCpnNombre() {
        return cpnNombre;
    }

    public void setCpnNombre(String cpnNombre) {
        this.cpnNombre = cpnNombre;
    }

    public String getCpnDescripcion() {
        return cpnDescripcion;
    }

    public void setCpnDescripcion(String cpnDescripcion) {
        this.cpnDescripcion = cpnDescripcion;
    }

    public LocalDateTime getCpnUltModFecha() {
        return cpnUltModFecha;
    }

    public void setCpnUltModFecha(LocalDateTime cpnUltModFecha) {
        this.cpnUltModFecha = cpnUltModFecha;
    }

    public String getCpnUltModUsuario() {
        return cpnUltModUsuario;
    }

    public void setCpnUltModUsuario(String cpnUltModUsuario) {
        this.cpnUltModUsuario = cpnUltModUsuario;
    }

    public Integer getCpnVersion() {
        return cpnVersion;
    }

    public void setCpnVersion(Integer cpnVersion) {
        this.cpnVersion = cpnVersion;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cpnPk);
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
        final SgComponente other = (SgComponente) obj;
        if (!Objects.equals(this.cpnPk, other.cpnPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgComponente{" + "cpnPk=" + cpnPk + ", cpnNombre=" + cpnNombre + ", cpnDescripcion=" + cpnDescripcion + ", cpnUltModFecha=" + cpnUltModFecha + ", cpnUltModUsuario=" + cpnUltModUsuario + ", cpnVersion=" + cpnVersion + '}';
    }
    
    

}
