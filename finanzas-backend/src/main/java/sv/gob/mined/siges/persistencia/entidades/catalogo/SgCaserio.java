/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_caserios", uniqueConstraints = {
    @UniqueConstraint(name = "cas_codigo_uk", columnNames = {"cas_codigo"})
    ,
    @UniqueConstraint(name = "cas_nombre_uk", columnNames = {"cas_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "casPk", scope = SgCaserio.class)
/**
 * Entidad correspondiente a los caseríos.
 */

public class SgCaserio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cas_pk", nullable = false)
    private Long casPk;

    @Size(max = 8)
    @Column(name = "cas_codigo", length = 8)
    @AtributoCodigo
    private String casCodigo;

    @Column(name = "cas_habilitado")
    @AtributoHabilitado
    private Boolean casHabilitado;

    @Size(max = 255)
    @Column(name = "cas_nombre", length = 255)
    @AtributoNormalizable
    private String casNombre;

    @Size(max = 255)
    @Column(name = "cas_nombre_busqueda", length = 255)
    @AtributoNombre
    private String casNombreBusqueda;

    @Column(name = "cas_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime casUltModFecha;

    @Size(max = 45)
    @Column(name = "cas_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String casUltModUsuario;

    @Column(name = "cas_version")
    @Version
    private Integer casVersion;

    @JoinColumn(name = "cas_canton_fk", referencedColumnName = "can_pk")
    @ManyToOne(optional = false)
    private SgCanton casCanton;

    public SgCaserio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.casNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.casNombre);
    }

    public Long getCasPk() {
        return casPk;
    }

    public void setCasPk(Long casPk) {
        this.casPk = casPk;
    }

    public String getCasCodigo() {
        return casCodigo;
    }

    public void setCasCodigo(String casCodigo) {
        this.casCodigo = casCodigo;
    }

    public Boolean getCasHabilitado() {
        return casHabilitado;
    }

    public void setCasHabilitado(Boolean casHabilitado) {
        this.casHabilitado = casHabilitado;
    }

    public String getCasNombre() {
        return casNombre;
    }

    public void setCasNombre(String casNombre) {
        this.casNombre = casNombre;
    }

    public String getCasNombreBusqueda() {
        return casNombreBusqueda;
    }

    public void setCasNombreBusqueda(String casNombreBusqueda) {
        this.casNombreBusqueda = casNombreBusqueda;
    }

    public LocalDateTime getCasUltModFecha() {
        return casUltModFecha;
    }

    public void setCasUltModFecha(LocalDateTime casUltModFecha) {
        this.casUltModFecha = casUltModFecha;
    }

    public String getCasUltModUsuario() {
        return casUltModUsuario;
    }

    public void setCasUltModUsuario(String casUltModUsuario) {
        this.casUltModUsuario = casUltModUsuario;
    }

    public Integer getCasVersion() {
        return casVersion;
    }

    public void setCasVersion(Integer casVersion) {
        this.casVersion = casVersion;
    }

    public SgCanton getCasCantonFk() {
        return casCanton;
    }

    public void setCasCantonFk(SgCanton casCanton) {
        this.casCanton = casCanton;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (casPk != null ? casPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCaserio)) {
            return false;
        }
        SgCaserio other = (SgCaserio) object;
        if ((this.casPk == null && other.casPk != null) || (this.casPk != null && !this.casPk.equals(other.casPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesTipoCaserio{" + "casPk=" + casPk + '}';
    }

}
