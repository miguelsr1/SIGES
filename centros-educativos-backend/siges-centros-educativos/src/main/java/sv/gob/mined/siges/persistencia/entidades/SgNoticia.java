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
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;

@Entity
@Table(name = "sg_noticias",uniqueConstraints = {
    @UniqueConstraint(name = "not_codigo_uk", columnNames = {"not_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "notPk", scope = SgNoticia.class)
public class SgNoticia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "not_pk")
    private Long notPk;
    
    @Size(max = 4)
    @Column(name = "not_codigo",length = 4)
    @AtributoCodigo
    private String notCodigo;
    
    @Size(max = 5000)
    @Column(name = "not_contenido",length = 5000)
    private String notContenido;
    
    @Column(name = "not_fecha_desde")
    private LocalDate notFechaDesde;
    
    @Column(name = "not_fecha_hasta")
    private LocalDate notFechaHasta;
    
    @Column(name = "not_habilitado")
    @AtributoHabilitado
    private Boolean notHabilitado;
    
    @Column(name = "not_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime notUltModFecha;
    
    @Size(max = 45)
    @Column(name = "not_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String notUltModUsuario;
    
    @Column(name = "not_version")
    @Version
    private Integer notVersion;
    
    @Size(max = 255)
    @Column(name = "not_titulo",length = 255)
    private String notTitulo;
    
    @Column(name = "not_orden")
    private Integer notOrden;
    
    @Column(name = "not_resaltada")
    private Boolean notResaltada;

    public SgNoticia() {
    }

    public SgNoticia(Long notPk) {
        this.notPk = notPk;
    }

    public Long getNotPk() {
        return notPk;
    }

    public void setNotPk(Long notPk) {
        this.notPk = notPk;
    }

    public String getNotCodigo() {
        return notCodigo;
    }

    public void setNotCodigo(String notCodigo) {
        this.notCodigo = notCodigo;
    }
    

    public String getNotContenido() {
        return notContenido;
    }

    public void setNotContenido(String notContenido) {
        this.notContenido = notContenido;
    }

    public LocalDate getNotFechaDesde() {
        return notFechaDesde;
    }

    public void setNotFechaDesde(LocalDate notFechaDesde) {
        this.notFechaDesde = notFechaDesde;
    }

    public LocalDate getNotFechaHasta() {
        return notFechaHasta;
    }

    public void setNotFechaHasta(LocalDate notFechaHasta) {
        this.notFechaHasta = notFechaHasta;
    }

    public Boolean getNotHabilitado() {
        return notHabilitado;
    }

    public void setNotHabilitado(Boolean notHabilitado) {
        this.notHabilitado = notHabilitado;
    }

    public LocalDateTime getNotUltModFecha() {
        return notUltModFecha;
    }

    public void setNotUltModFecha(LocalDateTime notUltModFecha) {
        this.notUltModFecha = notUltModFecha;
    }

    public String getNotUltModUsuario() {
        return notUltModUsuario;
    }

    public void setNotUltModUsuario(String notUltModUsuario) {
        this.notUltModUsuario = notUltModUsuario;
    }

    public Integer getNotVersion() {
        return notVersion;
    }

    public void setNotVersion(Integer notVersion) {
        this.notVersion = notVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notPk != null ? notPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNoticia)) {
            return false;
        }
        SgNoticia other = (SgNoticia) object;
        if ((this.notPk == null && other.notPk != null) || (this.notPk != null && !this.notPk.equals(other.notPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNoticias[ notPk=" + notPk + " ]";
    }

    public String getNotTitulo() {
        return notTitulo;
    }

    public void setNotTitulo(String notTitulo) {
        this.notTitulo = notTitulo;
    }

    public Integer getNotOrden() {
        return notOrden;
    }

    public void setNotOrden(Integer notOrden) {
        this.notOrden = notOrden;
    }

    public Boolean getNotResaltada() {
        return notResaltada;
    }

    public void setNotResaltada(Boolean notResaltada) {
        this.notResaltada = notResaltada;
    }
    
    
}
