/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_definiciones_titulo",schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "dti_codigo_uk", columnNames = {"dti_codigo"})
    ,
    @UniqueConstraint(name = "dti_nombre_uk", columnNames = {"dti_nombre"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dtiPk", scope = SgDefinicionTitulo.class)
@Audited
public class SgDefinicionTitulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dti_pk", nullable = false)
    private Long dtiPk;

    @Size(max = 45)
    @Column(name = "dti_codigo", length = 45)
    @AtributoCodigo
    private String dtiCodigo;

    @Size(max = 255)
    @Column(name = "dti_nombre", length = 255)
    @AtributoNormalizable
    private String dtiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "dti_nombre_busqueda", length = 255)
    private String dtiNombreBusqueda;

    @Column(name = "dti_habilitado")
    @AtributoHabilitado
    private Boolean dtiHabilitado;
    
    @Column(name = "dti_fecha_desde")
    private LocalDate dtiFechaDesde;
    
   
    @Column(name = "dti_fecha_hasta")
    private LocalDate dtiFechaHasta;

    @Column(name = "dti_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dtiUltModFecha;

    @Size(max = 45)
    @Column(name = "dti_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dtiUltModUsuario;

    @Column(name = "dti_version")
    @Version
    private Integer dtiVersion;
    
    @JoinColumn(name = "dti_plantilla_fk")
    @ManyToOne
    private SgPlantilla dtiPlantilla;
    
    @Column(name = "dti_nombre_certificado")
    private String dtiNombreCertificado;
    
    @JoinColumn(name = "dti_formula_fk", referencedColumnName = "fom_pk")
    @ManyToOne
    private SgFormula dtiFormula;
    
    @Column(name = "dti_es_tipo_reposicion")
    private String dtiEsTipoReposicion;

    public SgDefinicionTitulo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.dtiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.dtiNombre);
    }

    public Long getDtiPk() {
        return dtiPk;
    }

    public void setDtiPk(Long dtiPk) {
        this.dtiPk = dtiPk;
    }

    public String getDtiCodigo() {
        return dtiCodigo;
    }

    public void setDtiCodigo(String dtiCodigo) {
        this.dtiCodigo = dtiCodigo;
    }

    public String getDtiNombre() {
        return dtiNombre;
    }

    public void setDtiNombre(String dtiNombre) {
        this.dtiNombre = dtiNombre;
    }

    public String getDtiNombreBusqueda() {
        return dtiNombreBusqueda;
    }

    public void setDtiNombreBusqueda(String dtiNombreBusqueda) {
        this.dtiNombreBusqueda = dtiNombreBusqueda;
    }

    public Boolean getDtiHabilitado() {
        return dtiHabilitado;
    }

    public void setDtiHabilitado(Boolean dtiHabilitado) {
        this.dtiHabilitado = dtiHabilitado;
    }

    public LocalDateTime getDtiUltModFecha() {
        return dtiUltModFecha;
    }

    public void setDtiUltModFecha(LocalDateTime dtiUltModFecha) {
        this.dtiUltModFecha = dtiUltModFecha;
    }

    public String getDtiUltModUsuario() {
        return dtiUltModUsuario;
    }

    public void setDtiUltModUsuario(String dtiUltModUsuario) {
        this.dtiUltModUsuario = dtiUltModUsuario;
    }

    public Integer getDtiVersion() {
        return dtiVersion;
    }

    public void setDtiVersion(Integer dtiVersion) {
        this.dtiVersion = dtiVersion;
    }

    public SgPlantilla getDtiPlantilla() {
        return dtiPlantilla;
    }

    public void setDtiPlantilla(SgPlantilla dtiPlantilla) {
        this.dtiPlantilla = dtiPlantilla;
    }

    public LocalDate getDtiFechaDesde() {
        return dtiFechaDesde;
    }

    public void setDtiFechaDesde(LocalDate dtiFechaDesde) {
        this.dtiFechaDesde = dtiFechaDesde;
    }

    public LocalDate getDtiFechaHasta() {
        return dtiFechaHasta;
    }

    public void setDtiFechaHasta(LocalDate dtiFechaHasta) {
        this.dtiFechaHasta = dtiFechaHasta;
    }

    public String getDtiNombreCertificado() {
        return dtiNombreCertificado;
    }

    public void setDtiNombreCertificado(String dtiNombreCertificado) {
        this.dtiNombreCertificado = dtiNombreCertificado;
    }

    public SgFormula getDtiFormula() {
        return dtiFormula;
    }

    public void setDtiFormula(SgFormula dtiFormula) {
        this.dtiFormula = dtiFormula;
    }

    public String getDtiEsTipoReposicion() {
        return dtiEsTipoReposicion;
    }

    public void setDtiEsTipoReposicion(String dtiEsTipoReposicion) {
        this.dtiEsTipoReposicion = dtiEsTipoReposicion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dtiPk);
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
        final SgDefinicionTitulo other = (SgDefinicionTitulo) obj;
        if (!Objects.equals(this.dtiPk, other.dtiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDefinicionTitulo{" + "dtiPk=" + dtiPk + ", dtiCodigo=" + dtiCodigo + ", dtiNombre=" + dtiNombre + ", dtiNombreBusqueda=" + dtiNombreBusqueda + ", dtiHabilitado=" + dtiHabilitado + ", dtiUltModFecha=" + dtiUltModFecha + ", dtiUltModUsuario=" + dtiUltModUsuario + ", dtiVersion=" + dtiVersion + '}';
    }
    
    

}
