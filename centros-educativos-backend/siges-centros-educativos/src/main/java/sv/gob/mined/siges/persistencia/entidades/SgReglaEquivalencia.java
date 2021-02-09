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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author bruno
 */
@Entity
@Table(name = "sg_regla_equivalencia", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "reqPk", scope = SgRangoFecha.class)
public class SgReglaEquivalencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "req_pk", nullable = false)
    private Long reqPk;
    
    @Size(max = 255)
    @Column(name = "req_nombre")
    private String reqNombre;
    
    @Size(max = 1000)
    @Column(name = "req_descripcion")
    private String reqDescripcion;
    
    @Size(max = 255)
    @Column(name = "req_normativa")
    private String reqNormativa;
    
    
    @Column(name = "req_habilitado")
    @AtributoHabilitado
    private Boolean reqHabilitado;
    
    @Column(name = "req_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime reqUltModFecha;  
    
    @Size(max = 45)
    @Column(name = "req_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String reqUltModUsuario;
        
    @Column(name = "req_version")
    @Version
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
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.reqPk);
        hash = 79 * hash + Objects.hashCode(this.reqNombre);
        hash = 79 * hash + Objects.hashCode(this.reqDescripcion);
        hash = 79 * hash + Objects.hashCode(this.reqNormativa);
        hash = 79 * hash + Objects.hashCode(this.reqHabilitado);
        hash = 79 * hash + Objects.hashCode(this.reqUltModFecha);
        hash = 79 * hash + Objects.hashCode(this.reqUltModUsuario);
        hash = 79 * hash + Objects.hashCode(this.reqVersion);
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

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalencia{" + "reqPk=" + reqPk + '}';
    }
    
}
