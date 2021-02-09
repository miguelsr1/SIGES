/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_mensajes", uniqueConstraints = {
    @UniqueConstraint(name = "msj_codigo_uk", columnNames = {"msj_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "msjPk", scope = SgMensaje.class)
public class SgMensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "msj_pk")
    private Long msjPk;

    @Size(max = 4)
    @Column(name = "msj_codigo", length = 4)
    @AtributoCodigo
    private String msjCodigo;

    @Size(max = 255)
    @Column(name = "msj_descripcion", length = 255)
    private String msjDescripcion;

    @Size(max = 500)
    @Column(name = "msj_valor", length = 500)
    private String msjValor;

    @Column(name = "msj_habilitado")
    @AtributoHabilitado
    private Boolean msjHabilitado;

    @Column(name = "msj_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime msjUltModFecha;

    @Size(max = 45)
    @Column(name = "msj_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String msjUltModUsuario;

    @Column(name = "msj_version")
    @Version
    private Integer msjVersion;

    public Long getMsjPk() {
        return msjPk;
    }

    public void setMsjPk(Long msjPk) {
        this.msjPk = msjPk;
    }

    public String getMsjCodigo() {
        return msjCodigo;
    }

    public void setMsjCodigo(String msjCodigo) {
        this.msjCodigo = msjCodigo;
    }

    public String getMsjDescripcion() {
        return msjDescripcion;
    }

    public void setMsjDescripcion(String msjDescripcion) {
        this.msjDescripcion = msjDescripcion;
    }

    public String getMsjValor() {
        return msjValor;
    }

    public void setMsjValor(String msjValor) {
        this.msjValor = msjValor;
    }

    public Boolean getMsjHabilitado() {
        return msjHabilitado;
    }

    public void setMsjHabilitado(Boolean msjHabilitado) {
        this.msjHabilitado = msjHabilitado;
    }

    public LocalDateTime getMsjUltModFecha() {
        return msjUltModFecha;
    }

    public void setMsjUltModFecha(LocalDateTime msjUltModFecha) {
        this.msjUltModFecha = msjUltModFecha;
    }

    public String getMsjUltModUsuario() {
        return msjUltModUsuario;
    }

    public void setMsjUltModUsuario(String msjUltModUsuario) {
        this.msjUltModUsuario = msjUltModUsuario;
    }

    public Integer getMsjVersion() {
        return msjVersion;
    }

    public void setMsjVersion(Integer msjVersion) {
        this.msjVersion = msjVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msjPk != null ? msjPk.hashCode() : 0);
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
        final SgMensaje other = (SgMensaje) obj;
        if (!Objects.equals(this.msjPk, other.msjPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesMensaje[ msjPk=" + msjPk + " ]";
    }

}
