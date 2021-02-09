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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.ArchivoListener;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "com_audit_trail", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners({EntidadListener.class, ArchivoListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ComAuditTrail.class)
public class ComAuditTrail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "aud_action", length = 255)
    private String audAction;

    @Size(max = 255)
    @Column(name = "applic_cd", length = 255)
    private String applicCd;

    @Size(max = 255)
    @Column(name = "aud_client_ip", length = 255)
    private String audClientIp;

    @Column(name = "aud_date")
    private LocalDateTime audDate;

    @Size(max = 255)
    @Column(name = "aud_resource", length = 255)
    private String audResource;

    @Size(max = 255)
    @Column(name = "aud_server_ip", length = 255)
    private String audServerIp;

    @Size(max = 255)
    @Column(name = "aud_user", length = 255)
    private String audUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAudAction() {
        return audAction;
    }

    public void setAudAction(String audAction) {
        this.audAction = audAction;
    }

    public String getApplicCd() {
        return applicCd;
    }

    public void setApplicCd(String applicCd) {
        this.applicCd = applicCd;
    }

    public String getAudClientIp() {
        return audClientIp;
    }

    public void setAudClientIp(String audClientIp) {
        this.audClientIp = audClientIp;
    }

    public LocalDateTime getAudDate() {
        return audDate;
    }

    public void setAudDate(LocalDateTime audDate) {
        this.audDate = audDate;
    }

    public String getAudResource() {
        return audResource;
    }

    public void setAudResource(String audResource) {
        this.audResource = audResource;
    }

    public String getAudServerIp() {
        return audServerIp;
    }

    public void setAudServerIp(String audServerIp) {
        this.audServerIp = audServerIp;
    }

    public String getAudUser() {
        return audUser;
    }

    public void setAudUser(String audUser) {
        this.audUser = audUser;
    }
    
    


    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
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
        final ComAuditTrail other = (ComAuditTrail) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
