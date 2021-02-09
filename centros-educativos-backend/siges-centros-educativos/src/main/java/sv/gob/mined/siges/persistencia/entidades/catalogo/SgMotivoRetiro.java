/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_motivos_retiro", uniqueConstraints = {
    @UniqueConstraint(name = "mre_codigo_uk", columnNames = {"mre_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mrePk", scope = SgMotivoRetiro.class)
public class SgMotivoRetiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mre_pk")
    private Long mrePk;

    @Size(max = 4)
    @Column(name = "mre_codigo", length = 4)
    @AtributoCodigo
    private String mreCodigo;

    @Size(max = 255)
    @Column(name = "mre_nombre", length = 255)
    @AtributoNormalizable
    private String mreNombre;

    @Size(max = 255)
    @Column(name = "mre_nombre_busqueda", length = 255)
    @AtributoNombre
    private String mreNombreBusqueda;

    @Column(name = "mre_habilitado")
    @AtributoHabilitado
    private Boolean mreHabilitado;

    @Column(name = "mre_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mreUltModFecha;

    @Size(max = 45)
    @Column(name = "mre_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mreUltModUsuario;

    @Column(name = "mre_version")
    @Version
    private Integer mreVersion;

    @Column(name = "mre_definitivo")
    private Boolean mreDefinitivo;

    @Column(name = "mre_traslado")
    private Boolean mreTraslado;

    @Column(name = "mre_cambiosecc")
    private Boolean mreCambioSecc;

    public SgMotivoRetiro() {
    }

    public String getMreNombreBusqueda() {
        return mreNombreBusqueda;
    }

    public void setMreNombreBusqueda(String mreNombreBusqueda) {
        this.mreNombreBusqueda = mreNombreBusqueda;
    }

    public Long getMrePk() {
        return mrePk;
    }

    public void setMrePk(Long mrePk) {
        this.mrePk = mrePk;
    }

    public String getMreCodigo() {
        return mreCodigo;
    }

    public void setMreCodigo(String mreCodigo) {
        this.mreCodigo = mreCodigo;
    }

    public String getMreNombre() {
        return mreNombre;
    }

    public void setMreNombre(String mreNombre) {
        this.mreNombre = mreNombre;
    }

    public Boolean getMreHabilitado() {
        return mreHabilitado;
    }

    public void setMreHabilitado(Boolean mreHabilitado) {
        this.mreHabilitado = mreHabilitado;
    }

    public LocalDateTime getMreUltModFecha() {
        return mreUltModFecha;
    }

    public void setMreUltModFecha(LocalDateTime mreUltModFecha) {
        this.mreUltModFecha = mreUltModFecha;
    }

    public String getMreUltModUsuario() {
        return mreUltModUsuario;
    }

    public void setMreUltModUsuario(String mreUltModUsuario) {
        this.mreUltModUsuario = mreUltModUsuario;
    }

    public Integer getMreVersion() {
        return mreVersion;
    }

    public void setMreVersion(Integer mreVersion) {
        this.mreVersion = mreVersion;
    }

    public Boolean getMreDefinitivo() {
        return mreDefinitivo;
    }

    public void setMreDefinitivo(Boolean mreDefinitivo) {
        this.mreDefinitivo = mreDefinitivo;
    }

    public Boolean getMreTraslado() {
        return mreTraslado;
    }

    public void setMreTraslado(Boolean mreTraslado) {
        this.mreTraslado = mreTraslado;
    }

    public Boolean getMreCambioSecc() {
        return mreCambioSecc;
    }

    public void setMreCambioSecc(Boolean mreCambioSecc) {
        this.mreCambioSecc = mreCambioSecc;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrePk != null ? mrePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoRetiro)) {
            return false;
        }
        SgMotivoRetiro other = (SgMotivoRetiro) object;
        if ((this.mrePk == null && other.mrePk != null) || (this.mrePk != null && !this.mrePk.equals(other.mrePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoRetiro[ mrePk=" + mrePk + " ]";
    }

}
