/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ComAuditTrail.class)
public class ComAuditTrail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;

    private String audAction;

    private String applicCd;

    private String audClientIp;

    private LocalDateTime audDate;

    private String audResource;

    private String audServerIp;

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
