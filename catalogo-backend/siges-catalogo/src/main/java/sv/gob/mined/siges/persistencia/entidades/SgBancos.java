/**
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_bancos", uniqueConstraints = {
    @UniqueConstraint(name = "bnc_codigo_uk", columnNames = {"bnc_codigo"}),
    @UniqueConstraint(name = "bnc_nombre_uk", columnNames = {"bnc_nombre"})
}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bncPk", scope = SgBancos.class)
@Audited
public class SgBancos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "bnc_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bncPk;

    @Column(name = "bnc_codigo")
    @Size(max = 45)
    private String bncCodigo;

    @Column(name = "bnc_nombre")
    @Size(max = 500)
    private String bncNombre;
    
    @Column(name = "bnc_telefono")
    @Size(max = 20)
    private String bncTelefono;
    
    @Column(name = "bnc_correo_electronico")
    @Size(max = 50)
    private String bncCorreoElectronico;

    @Column(name = "bnc_codigo_busqueda")
    @AtributoCodigo
    @Size(max = 45)
    private String bncCodigoBusqueda;
    
    @Column(name = "bnc_nombre_busqueda")
    @AtributoNombre
    @Size(max = 500)
    private String bncNombreBusqueda;

    @Column(name = "bnc_habilitado")
    @AtributoHabilitado
    private Boolean bncHabilitado;

    @Column(name = "bnc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime bncUltModFecha;

    @Size(max = 45)
    @Column(name = "bnc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String bncUltModUsuario;

    @Column(name = "bnc_version")
    @Version
    private Integer bncVersion;

    public SgBancos() {
    }

    public SgBancos(Long bncPk) {
        this.bncPk = bncPk;
    }

    public SgBancos(Long bncPk, Integer bncVersion) {
        this.bncPk = bncPk;
        this.bncVersion = bncVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.bncNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.bncNombre);
        this.bncCodigoBusqueda = SofisStringUtils.normalizarParaBusqueda(this.bncCodigo);
    }

    public Long getBncPk() {
        return bncPk;
    }

    public void setBncPk(Long bncPk) {
        this.bncPk = bncPk;
    }

    public String getBncCodigo() {
        return bncCodigo;
    }

    public void setBncCodigo(String bncCodigo) {
        this.bncCodigo = bncCodigo;
    }

    public String getBncNombre() {
        return bncNombre;
    }

    public void setBncNombre(String bncNombre) {
        this.bncNombre = bncNombre;
    }

    public String getBncTelefono() {
        return bncTelefono;
    }

    public void setBncTelefono(String bncTelefono) {
        this.bncTelefono = bncTelefono;
    }

    public String getBncCorreoElectronico() {
        return bncCorreoElectronico;
    }

    public void setBncCorreoElectronico(String bncCorreoElectronico) {
        this.bncCorreoElectronico = bncCorreoElectronico;
    }

    public String getBncCodigoBusqueda() {
        return bncCodigoBusqueda;
    }

    public void setBncCodigoBusqueda(String bncCodigoBusqueda) {
        this.bncCodigoBusqueda = bncCodigoBusqueda;
    }

    public String getBncNombreBusqueda() {
        return bncNombreBusqueda;
    }

    public void setBncNombreBusqueda(String bncNombreBusqueda) {
        this.bncNombreBusqueda = bncNombreBusqueda;
    }

    public Boolean getBncHabilitado() {
        return bncHabilitado;
    }

    public void setBncHabilitado(Boolean bncHabilitado) {
        this.bncHabilitado = bncHabilitado;
    }

    public LocalDateTime getBncUltModFecha() {
        return bncUltModFecha;
    }

    public void setBncUltModFecha(LocalDateTime bncUltModFecha) {
        this.bncUltModFecha = bncUltModFecha;
    }

    public String getBncUltModUsuario() {
        return bncUltModUsuario;
    }

    public void setBncUltModUsuario(String bncUltModUsuario) {
        this.bncUltModUsuario = bncUltModUsuario;
    }

    public Integer getBncVersion() {
        return bncVersion;
    }

    public void setBncVersion(Integer bncVersion) {
        this.bncVersion = bncVersion;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.bncPk);
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
        final SgBancos other = (SgBancos) obj;
        if (!Objects.equals(this.bncPk, other.bncPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSede[ bncPk=" + bncPk + " ]";
    }

}
