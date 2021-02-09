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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_tipo_nombramiento", uniqueConstraints = {
    @UniqueConstraint(name = "tno_codigo_uk", columnNames = {"tno_codigo"})
    ,
    @UniqueConstraint(name = "tno_nombre_uk", columnNames = {"tno_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tnoPk", scope = SgTipoNombramiento.class)
public class SgTipoNombramiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tno_pk")
    private Long tnoPk;
    
    @Size(max = 45)
    @Column(name = "tno_codigo",length = 45)
    @AtributoCodigo
    private String tnoCodigo;
    
    @Column(name = "tno_habilitado")
    @AtributoHabilitado
    private Boolean tnoHabilitado;
    
    @Size(max = 255)
    @Column(name = "tno_nombre",length = 255)
    @AtributoNormalizable
    private String tnoNombre;
    
    @Size(max = 255)
    @Column(name = "tno_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tnoNombreBusqueda;
    
    @Column(name = "tno_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tnoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tno_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tnoUltModUsuario;
    
    @Column(name = "tno_version")
    @Version
    private Integer tnoVersion;
    
    @Column(name = "tno_aplica_acuerdo")
    private Boolean tnoAplicaAcuerdo;
    
    @Column(name = "tno_aplica_contrato")
    private Boolean tnoAplicaContrato;
    
    @Column(name = "tno_aplica_otros")
    private Boolean tnoAplicaOtros;

    public SgTipoNombramiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tnoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tnoNombre);
    }

    public SgTipoNombramiento(Long tnoPk) {
        this.tnoPk = tnoPk;
    }

    public Long getTnoPk() {
        return tnoPk;
    }

    public void setTnoPk(Long tnoPk) {
        this.tnoPk = tnoPk;
    }

    public String getTnoCodigo() {
        return tnoCodigo;
    }

    public void setTnoCodigo(String tnoCodigo) {
        this.tnoCodigo = tnoCodigo;
    }

    public Boolean getTnoHabilitado() {
        return tnoHabilitado;
    }

    public void setTnoHabilitado(Boolean tnoHabilitado) {
        this.tnoHabilitado = tnoHabilitado;
    }

    public String getTnoNombre() {
        return tnoNombre;
    }

    public void setTnoNombre(String tnoNombre) {
        this.tnoNombre = tnoNombre;
    }

    public String getTnoNombreBusqueda() {
        return tnoNombreBusqueda;
    }

    public void setTnoNombreBusqueda(String tnoNombreBusqueda) {
        this.tnoNombreBusqueda = tnoNombreBusqueda;
    }

    public LocalDateTime getTnoUltModFecha() {
        return tnoUltModFecha;
    }

    public void setTnoUltModFecha(LocalDateTime tnoUltModFecha) {
        this.tnoUltModFecha = tnoUltModFecha;
    }

    public String getTnoUltModUsuario() {
        return tnoUltModUsuario;
    }

    public void setTnoUltModUsuario(String tnoUltModUsuario) {
        this.tnoUltModUsuario = tnoUltModUsuario;
    }

    public Integer getTnoVersion() {
        return tnoVersion;
    }

    public void setTnoVersion(Integer tnoVersion) {
        this.tnoVersion = tnoVersion;
    }

    public Boolean getTnoAplicaAcuerdo() {
        return tnoAplicaAcuerdo;
    }

    public void setTnoAplicaAcuerdo(Boolean tnoAplicaAcuerdo) {
        this.tnoAplicaAcuerdo = tnoAplicaAcuerdo;
    }

    public Boolean getTnoAplicaContrato() {
        return tnoAplicaContrato;
    }

    public void setTnoAplicaContrato(Boolean tnoAplicaContrato) {
        this.tnoAplicaContrato = tnoAplicaContrato;
    }

    public Boolean getTnoAplicaOtros() {
        return tnoAplicaOtros;
    }

    public void setTnoAplicaOtros(Boolean tnoAplicaOtros) {
        this.tnoAplicaOtros = tnoAplicaOtros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tnoPk != null ? tnoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoNombramiento)) {
            return false;
        }
        SgTipoNombramiento other = (SgTipoNombramiento) object;
        if ((this.tnoPk == null && other.tnoPk != null) || (this.tnoPk != null && !this.tnoPk.equals(other.tnoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoNombramiento[ tnoPk=" + tnoPk + " ]";
    }
    
}
