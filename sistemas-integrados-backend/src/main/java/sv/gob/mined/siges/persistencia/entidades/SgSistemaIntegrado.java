/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSiPromotor;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sistemas_integrados", schema = Constantes.SCHEMA_SISTEMAS_INTEGRADOS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sinPk", scope = SgSistemaIntegrado.class)
public class SgSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sin_pk")
    private Long sinPk;
    
    @Size(max = 15)
    @Column(name = "sin_codigo")
    private String sinCodigo;
    
    @Size(max = 100)
    @Column(name = "sin_nombre")
    private String sinNombre;
    
    @Size(max = 100)
    @Column(name = "sin_nombre_busqueda")
    private String sinNombreBusqueda;
    
    @Column(name = "sin_habilitado")
    private Boolean sinHabilitado;
    
    @Size(max = 4000)
    @Column(name = "sin_descripcion")
    private String sinDescripcion;
    
    @Column(name = "sin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sinUltModFecha;
    
    @Size(max = 45)
    @Column(name = "sin_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String sinUltModUsuario;
    
    @Column(name = "sin_version")
    @Version
    private Integer sinVersion;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sinPk")
    @NotAudited
    private List<SgSistemaSede> sinSedes;
    
    @Transient
    private Long totalSedes;
    
    @JoinColumn(name = "sin_promotor_fk")
    @ManyToOne
    private SgSiPromotor sinPromotor;

    public SgSistemaIntegrado() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sinNombre);
    }

    public Long getSinPk() {
        return sinPk;
    }

    public void setSinPk(Long sinPk) {
        this.sinPk = sinPk;
    }

    public String getSinCodigo() {
        return sinCodigo;
    }

    public void setSinCodigo(String sinCodigo) {
        this.sinCodigo = sinCodigo;
    }

    public String getSinNombre() {
        return sinNombre;
    }

    public void setSinNombre(String sinNombre) {
        this.sinNombre = sinNombre;
    }

    public Boolean getSinHabilitado() {
        return sinHabilitado;
    }

    public void setSinHabilitado(Boolean sinHabilitado) {
        this.sinHabilitado = sinHabilitado;
    }

    public String getSinDescripcion() {
        return sinDescripcion;
    }

    public void setSinDescripcion(String sinDescripcion) {
        this.sinDescripcion = sinDescripcion;
    }

    public LocalDateTime getSinUltModFecha() {
        return sinUltModFecha;
    }

    public void setSinUltModFecha(LocalDateTime sinUltModFecha) {
        this.sinUltModFecha = sinUltModFecha;
    }   

    public String getSinUltModUsuario() {
        return sinUltModUsuario;
    }

    public void setSinUltModUsuario(String sinUltModUsuario) {
        this.sinUltModUsuario = sinUltModUsuario;
    }

    public Integer getSinVersion() {
        return sinVersion;
    }

    public void setSinVersion(Integer sinVersion) {
        this.sinVersion = sinVersion;
    }

    public List<SgSistemaSede> getSinSedes() {
        return sinSedes;
    }

    public void setSinSedes(List<SgSistemaSede> sinSedes) {
        this.sinSedes = sinSedes;
    }
    
    public Long getTotalSedes() {
        return totalSedes;
    }

    public void setTotalSedes(Long totalSedes) {
        this.totalSedes = totalSedes;
    }

    public SgSiPromotor getSinPromotor() {
        return sinPromotor;
    }

    public void setSinPromotor(SgSiPromotor sinPromotor) {
        this.sinPromotor = sinPromotor;
    }

    public String getSinNombreBusqueda() {
        return sinNombreBusqueda;
    }

    public void setSinNombreBusqueda(String sinNombreBusqueda) {
        this.sinNombreBusqueda = sinNombreBusqueda;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sinPk != null ? sinPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSistemaIntegrado)) {
            return false;
        }
        SgSistemaIntegrado other = (SgSistemaIntegrado) object;
        if ((this.sinPk == null && other.sinPk != null) || (this.sinPk != null && !this.sinPk.equals(other.sinPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSistemaIntegrado[ sinPk=" + sinPk + " ]";
    }
    
}
