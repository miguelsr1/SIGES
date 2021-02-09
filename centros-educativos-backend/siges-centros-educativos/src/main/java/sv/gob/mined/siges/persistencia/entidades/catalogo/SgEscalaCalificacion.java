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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_escalas_calificacion", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ecaPk", scope = SgEscalaCalificacion.class) 
public class SgEscalaCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eca_pk")
    private Long ecaPk;

    @Size(max = 45)
    @Column(name = "eca_codigo", length = 45)
    @AtributoCodigo
    private String ecaCodigo;

    @Column(name = "eca_habilitado")
    @AtributoHabilitado
    private Boolean ecaHabilitado;

    @Size(max = 255)
    @Column(name = "eca_nombre", length = 255)
    @AtributoNormalizable
    private String ecaNombre;

    @Size(max = 255)
    @Column(name = "eca_nombre_busqueda", length = 255)
    @AtributoNombre
    private String ecaNombreBusqueda;

    @Column(name = "eca_tipo_escala")
    @Enumerated(EnumType.STRING)
    private TipoEscalaCalificacion ecaTipoEscala;

    @Column(name = "eca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ecaUltModFecha;

    @Size(max = 45)
    @Column(name = "eca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ecaUltModUsuario;

    @Column(name = "eca_version")
    @Version
    private Integer ecaVersion;
    
    @Column(name = "eca_minimo")
    private Double ecaMinimo;
    
    @Column(name = "eca_maximo")
    private Double ecaMaximo;
    
    @Column(name = "eca_minimo_aprobacion")
    private Double ecaMinimoAprobacion;
    
    @Column(name = "eca_precision")
    private Integer ecaPrecision;

    @JoinColumn(name = "eca_valor_minimo", referencedColumnName = "cal_pk")
    @OneToOne
    private SgCalificacion ecaValorMinimo;

    @OneToMany(mappedBy = "calEscala")
    private List<SgCalificacion> ecaCalificaciones;

    public SgEscalaCalificacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ecaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ecaNombre);
    }

    public Long getEcaPk() {
        return ecaPk;
    }

    public void setEcaPk(Long ecaPk) {
        this.ecaPk = ecaPk;
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreBusqueda() {
        return ecaNombreBusqueda;
    }

    public void setEcaNombreBusqueda(String ecaNombreBusqueda) {
        this.ecaNombreBusqueda = ecaNombreBusqueda;
    }

    public TipoEscalaCalificacion getEcaTipoEscala() {
        return ecaTipoEscala;
    }

    public void setEcaTipoEscala(TipoEscalaCalificacion ecaTipoEscala) {
        this.ecaTipoEscala = ecaTipoEscala;
    }

    public LocalDateTime getEcaUltModFecha() {
        return ecaUltModFecha;
    }

    public void setEcaUltModFecha(LocalDateTime ecaUltModFecha) {
        this.ecaUltModFecha = ecaUltModFecha;
    }

    public String getEcaUltModUsuario() {
        return ecaUltModUsuario;
    }

    public void setEcaUltModUsuario(String ecaUltModUsuario) {
        this.ecaUltModUsuario = ecaUltModUsuario;
    }

    public Integer getEcaVersion() {
        return ecaVersion;
    }

    public void setEcaVersion(Integer ecaVersion) {
        this.ecaVersion = ecaVersion;
    }

    public Double getEcaMinimo() {
        return ecaMinimo;
    }

    public void setEcaMinimo(Double ecaMinimo) {
        this.ecaMinimo = ecaMinimo;
    }

    public Double getEcaMinimoAprobacion() {
        return ecaMinimoAprobacion;
    }

    public void setEcaMinimoAprobacion(Double ecaMinimoAprobacion) {
        this.ecaMinimoAprobacion = ecaMinimoAprobacion;
    }

    public Double getEcaMaximo() {
        return ecaMaximo;
    }

    public void setEcaMaximo(Double ecaMaximo) {
        this.ecaMaximo = ecaMaximo;
    }

    public Integer getEcaPrecision() {
        return ecaPrecision;
    }

    public void setEcaPrecision(Integer ecaPrecision) {
        this.ecaPrecision = ecaPrecision;
    }    

    public SgCalificacion getEcaValorMinimo() {
        return ecaValorMinimo;
    }

    public void setEcaValorMinimo(SgCalificacion ecaValorMinimo) {
        this.ecaValorMinimo = ecaValorMinimo;
    }

    public List<SgCalificacion> getEcaCalificaciones() {
        return ecaCalificaciones;
    }

    public void setEcaCalificaciones(List<SgCalificacion> ecaCalificaciones) {
        this.ecaCalificaciones = ecaCalificaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecaPk != null ? ecaPk.hashCode() : 0);
        return hash;
    }

    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEscalaCalificacion)) {
            return false;
        }
        SgEscalaCalificacion other = (SgEscalaCalificacion) object;
        if ((this.ecaPk == null && other.ecaPk != null) || (this.ecaPk != null && !this.ecaPk.equals(other.ecaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEscalaCalificacion[ ecaPk=" + ecaPk + " ]";
    }

}
