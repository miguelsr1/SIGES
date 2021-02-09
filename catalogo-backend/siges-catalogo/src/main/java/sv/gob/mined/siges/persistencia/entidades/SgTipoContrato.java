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
@Table(name = "sg_tipo_contrato", uniqueConstraints = {
    @UniqueConstraint(name = "tco_codigo_uk", columnNames = {"tco_codigo"})
    ,
    @UniqueConstraint(name = "tco_nombre_uk", columnNames = {"tco_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcoPk", scope = SgTipoContrato.class)
public class SgTipoContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tco_pk")
    private Long tcoPk;
    
    @Size(max = 45)
    @Column(name = "tco_codigo",length = 45)
    @AtributoCodigo
    private String tcoCodigo;
    
    @Column(name = "tco_habilitado")
    @AtributoHabilitado
    private Boolean tcoHabilitado;
    
    @Size(max = 255)
    @Column(name = "tco_nombre",length = 255)
    @AtributoNormalizable
    private String tcoNombre;
    
    @Size(max = 255)
    @Column(name = "tco_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tcoNombreBusqueda;
    
    @Column(name = "tco_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tcoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tco_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tcoUltModUsuario;
    
    @Column(name = "tco_version")
    @Version
    private Integer tcoVersion;
    
    @Column(name = "tco_es_interinato")
    private Boolean tcoEsInterinato;
    
    @Column(name = "tco_requiere_fecha_hasta")
    private Boolean tcoRequiereFechaHasta;
    
    @Column(name = "tco_aplica_acuerdo")
    private Boolean tcoAplicaAcuerdo;
    
    @Column(name = "tco_aplica_contrato")
    private Boolean tcoAplicaContrato;
    
    @Column(name = "tco_aplica_otros")
    private Boolean tcoAplicaOtros;

    public SgTipoContrato() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcoNombre);
    }

    public SgTipoContrato(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public Long getTcoPk() {
        return tcoPk;
    }

    public void setTcoPk(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public String getTcoCodigo() {
        return tcoCodigo;
    }

    public void setTcoCodigo(String tcoCodigo) {
        this.tcoCodigo = tcoCodigo;
    }

    public Boolean getTcoHabilitado() {
        return tcoHabilitado;
    }

    public void setTcoHabilitado(Boolean tcoHabilitado) {
        this.tcoHabilitado = tcoHabilitado;
    }

    public String getTcoNombre() {
        return tcoNombre;
    }

    public void setTcoNombre(String tcoNombre) {
        this.tcoNombre = tcoNombre;
    }

    public String getTcoNombreBusqueda() {
        return tcoNombreBusqueda;
    }

    public void setTcoNombreBusqueda(String tcoNombreBusqueda) {
        this.tcoNombreBusqueda = tcoNombreBusqueda;
    }

    public LocalDateTime getTcoUltModFecha() {
        return tcoUltModFecha;
    }

    public void setTcoUltModFecha(LocalDateTime tcoUltModFecha) {
        this.tcoUltModFecha = tcoUltModFecha;
    }

    public String getTcoUltModUsuario() {
        return tcoUltModUsuario;
    }

    public void setTcoUltModUsuario(String tcoUltModUsuario) {
        this.tcoUltModUsuario = tcoUltModUsuario;
    }

    public Integer getTcoVersion() {
        return tcoVersion;
    }

    public void setTcoVersion(Integer tcoVersion) {
        this.tcoVersion = tcoVersion;
    }

    public Boolean getTcoEsInterinato() {
        return tcoEsInterinato;
    }

    public void setTcoEsInterinato(Boolean tcoEsInterinato) {
        this.tcoEsInterinato = tcoEsInterinato;
    }

    public Boolean getTcoRequiereFechaHasta() {
        return tcoRequiereFechaHasta;
    }

    public void setTcoRequiereFechaHasta(Boolean tcoRequiereFechaHasta) {
        this.tcoRequiereFechaHasta = tcoRequiereFechaHasta;
    }

    public Boolean getTcoAplicaAcuerdo() {
        return tcoAplicaAcuerdo;
    }

    public void setTcoAplicaAcuerdo(Boolean tcoAplicaAcuerdo) {
        this.tcoAplicaAcuerdo = tcoAplicaAcuerdo;
    }

    public Boolean getTcoAplicaContrato() {
        return tcoAplicaContrato;
    }

    public void setTcoAplicaContrato(Boolean tcoAplicaContrato) {
        this.tcoAplicaContrato = tcoAplicaContrato;
    }

    public Boolean getTcoAplicaOtros() {
        return tcoAplicaOtros;
    }

    public void setTcoAplicaOtros(Boolean tcoAplicaOtros) {
        this.tcoAplicaOtros = tcoAplicaOtros;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcoPk != null ? tcoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoContrato)) {
            return false;
        }
        SgTipoContrato other = (SgTipoContrato) object;
        if ((this.tcoPk == null && other.tcoPk != null) || (this.tcoPk != null && !this.tcoPk.equals(other.tcoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoContrato[ tcoPk=" + tcoPk + " ]";
    }
    
}
