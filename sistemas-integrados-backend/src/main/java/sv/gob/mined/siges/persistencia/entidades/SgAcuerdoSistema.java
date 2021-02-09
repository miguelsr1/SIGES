/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoAcuerdoSistema;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author sofis2
 */
@Entity
@Table(name = "sg_acuerdos_sistemas", schema = Constantes.SCHEMA_SISTEMAS_INTEGRADOS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "asiPk", scope = SgAcuerdoSistema.class)
public class SgAcuerdoSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asi_pk")
    private Long asiPk;
    
    @JoinColumn(name = "asi_sistema_integrado_fk")
    @ManyToOne
    private SgSistemaIntegrado asiSistemaIntegrado;
    
    @JoinColumn(name = "asi_tipo_acuerdo_fk")
    @ManyToOne
    private SgTipoAcuerdo asiTipoAcuerdo;
    
    @Column(name = "asi_numero_acuerdo")
    private Long asiNumeroAcuerdo;
    
    @Column(name = "asi_fecha_creacion")
    private LocalDate asiFechaCreacion;
    
    @Column(name = "asi_estado")
    @Enumerated(EnumType.STRING)
    private EnumEstadoAcuerdoSistema asiEstado;
    
    @Size(max = 4000)
    @Column(name = "asi_observaciones", length = 4000)
    private String asiObservaciones;
    
    @Column(name = "asi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime asiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "asi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String asiUltModUsuario;
    
    @Column(name = "asi_version")
    @Version
    private Integer asiVersion;

    public SgAcuerdoSistema() {
    }

    public Long getAsiPk() {
        return asiPk;
    }

    public void setAsiPk(Long asiPk) {
        this.asiPk = asiPk;
    }

    public SgSistemaIntegrado getAsiSistemaIntegrado() {
        return asiSistemaIntegrado;
    }

    public void setAsiSistemaIntegrado(SgSistemaIntegrado asiSistemaIntegrado) {
        this.asiSistemaIntegrado = asiSistemaIntegrado;
    }

    public SgTipoAcuerdo getAsiTipoAcuerdo() {
        return asiTipoAcuerdo;
    }

    public void setAsiTipoAcuerdo(SgTipoAcuerdo asiTipoAcuerdo) {
        this.asiTipoAcuerdo = asiTipoAcuerdo;
    }

    public Long getAsiNumeroAcuerdo() {
        return asiNumeroAcuerdo;
    }

    public void setAsiNumeroAcuerdo(Long asiNumeroAcuerdo) {
        this.asiNumeroAcuerdo = asiNumeroAcuerdo;
    }

    public LocalDate getAsiFechaCreacion() {
        return asiFechaCreacion;
    }

    public void setAsiFechaCreacion(LocalDate asiFechaCreacion) {
        this.asiFechaCreacion = asiFechaCreacion;
    }

    public EnumEstadoAcuerdoSistema getAsiEstado() {
        return asiEstado;
    }

    public void setAsiEstado(EnumEstadoAcuerdoSistema asiEstado) {
        this.asiEstado = asiEstado;
    }

    public String getAsiObservaciones() {
        return asiObservaciones;
    }

    public void setAsiObservaciones(String asiObservaciones) {
        this.asiObservaciones = asiObservaciones;
    }

    public LocalDateTime getAsiUltModFecha() {
        return asiUltModFecha;
    }

    public void setAsiUltModFecha(LocalDateTime asiUltModFecha) {
        this.asiUltModFecha = asiUltModFecha;
    }

    public String getAsiUltModUsuario() {
        return asiUltModUsuario;
    }

    public void setAsiUltModUsuario(String asiUltModUsuario) {
        this.asiUltModUsuario = asiUltModUsuario;
    }

    public Integer getAsiVersion() {
        return asiVersion;
    }

    public void setAsiVersion(Integer asiVersion) {
        this.asiVersion = asiVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asiPk != null ? asiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAcuerdoSistema)) {
            return false;
        }
        SgAcuerdoSistema other = (SgAcuerdoSistema) object;
        if ((this.asiPk == null && other.asiPk != null) || (this.asiPk != null && !this.asiPk.equals(other.asiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSistema[ asiPk=" + asiPk + " ]";
    }
    
}
