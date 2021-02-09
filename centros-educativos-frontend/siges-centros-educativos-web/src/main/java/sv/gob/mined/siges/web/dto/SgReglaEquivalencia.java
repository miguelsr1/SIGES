/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "reqPk", scope = SgReglaEquivalencia.class)
public class SgReglaEquivalencia implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long reqPk;

    private String reqNombre;

    private String reqDescripcion;

    private String reqNormativa;    

    private Boolean reqHabilitado;
    
    private LocalDateTime reqUltModFecha;

    private String reqUltModUsuario;

    private Integer reqVersion;

    public Long getReqPk() {
        return reqPk;
    }

    public void setReqPk(Long reqPk) {
        this.reqPk = reqPk;
    }

    public String getReqNombre() {
        return reqNombre;
    }

    public void setReqNombre(String reqNombre) {
        this.reqNombre = reqNombre;
    }

    public String getReqDescripcion() {
        return reqDescripcion;
    }

    public void setReqDescripcion(String reqDescripcion) {
        this.reqDescripcion = reqDescripcion;
    }

    public String getReqNormativa() {
        return reqNormativa;
    }

    public void setReqNormativa(String reqNormativa) {
        this.reqNormativa = reqNormativa;
    }

    public Boolean getReqHabilitado() {
        return reqHabilitado;
    }

    public void setReqHabilitado(Boolean reqHabilitado) {
        this.reqHabilitado = reqHabilitado;
    }

    public LocalDateTime getReqUltModFecha() {
        return reqUltModFecha;
    }

    public void setReqUltModFecha(LocalDateTime reqUltModFecha) {
        this.reqUltModFecha = reqUltModFecha;
    }

    public String getReqUltModUsuario() {
        return reqUltModUsuario;
    }

    public void setReqUltModUsuario(String reqUltModUsuario) {
        this.reqUltModUsuario = reqUltModUsuario;
    }

    public Integer getReqVersion() {
        return reqVersion;
    }

    public void setReqVersion(Integer reqVersion) {
        this.reqVersion = reqVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.reqPk);
        hash = 53 * hash + Objects.hashCode(this.reqNombre);
        hash = 53 * hash + Objects.hashCode(this.reqDescripcion);
        hash = 53 * hash + Objects.hashCode(this.reqNormativa);
        hash = 53 * hash + Objects.hashCode(this.reqHabilitado);
        hash = 53 * hash + Objects.hashCode(this.reqUltModFecha);
        hash = 53 * hash + Objects.hashCode(this.reqUltModUsuario);
        hash = 53 * hash + Objects.hashCode(this.reqVersion);
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
        final SgReglaEquivalencia other = (SgReglaEquivalencia) obj;
        if (!Objects.equals(this.reqPk, other.reqPk)) {
            return false;
        }
        return true;
    }
}
