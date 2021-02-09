/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author bruno
 */
public class SgResponsable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long usuPk;
    private String documento;
    private String nombreCompleto;
    private String email;
    private Long personaEstudianteId;
    private Long personaResponsableId;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPersonaEstudianteId() {
        return personaEstudianteId;
    }

    public void setPersonaEstudianteId(Long personaEstudianteId) {
        this.personaEstudianteId = personaEstudianteId;
    }

    public Long getPersonaResponsableId() {
        return personaResponsableId;
    }

    public void setPersonaResponsableId(Long personaResponsableId) {
        this.personaResponsableId = personaResponsableId;
    }

    public Long getUsuPk() {
        return usuPk;
    }

    public void setUsuPk(Long usuPk) {
        this.usuPk = usuPk;
    }

    

    @Override
    public int hashCode() {
        int hash = 5;
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
        final SgResponsable other = (SgResponsable) obj;
        if (!Objects.equals(this.documento, other.documento)) {
            return false;
        }
        if (!Objects.equals(this.nombreCompleto, other.nombreCompleto)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SpResponsable{" + "nombreCompleto=" + nombreCompleto + ", email=" + email +  '}';
    }
}