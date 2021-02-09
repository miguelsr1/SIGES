/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_formulas", uniqueConstraints = {
    @UniqueConstraint(name = "fom_codigo_uk", columnNames = {"fom_codigo"})
    ,
    @UniqueConstraint(name = "fom_nombre_uk", columnNames = {"fom_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fomPk", scope = SgFormula.class)
@Audited
public class SgFormula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fom_pk", nullable = false)
    private Long fomPk;

    @Size(max = 45)
    @Column(name = "fom_codigo", length = 45)
    @AtributoCodigo
    private String fomCodigo;

    @Size(max = 255)
    @Column(name = "fom_nombre", length = 255)
    @AtributoNormalizable
    private String fomNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "fom_nombre_busqueda", length = 255)
    private String fomNombreBusqueda;

    @Column(name = "fom_habilitado")
    @AtributoHabilitado
    private Boolean fomHabilitado;

    @Size(max = 4000)
    @Column(name = "fom_texto_largo", length = 4000)
    private String fomTextoLargo;

    @Size(max = 255)
    @Column(name = "fom_descripcion", length = 255)
    private String fomDescripcion;
    
    @Column(name = "fom_tipo_formula")
    @Enumerated(EnumType.STRING)
    private EnumTipoFormula fomTipoFormula;

    @Column(name = "fom_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime fomUltModFecha;

    @Size(max = 45)
    @Column(name = "fom_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String fomUltModUsuario;

    @Column(name = "fom_version")
    @Version
    private Integer fomVersion;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_rel_formula_formula",
            schema = Constantes.SCHEMA_CATALOGO,
            joinColumns = @JoinColumn(name = "fom_pk"),
            inverseJoinColumns = @JoinColumn(name = "sub_fom_pk"))
    private List<SgFormula> fomSubFormula;

    
    @Column(name = "fom_tiene_subformula")
    private Boolean fomTienSubformula;

    public SgFormula() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.fomNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.fomNombre);
        
        if(fomSubFormula!=null && !fomSubFormula.isEmpty()){
            fomTienSubformula=Boolean.TRUE;
        }else{
            fomTienSubformula=Boolean.FALSE;
        }
    }

    public Long getFomPk() {
        return fomPk;
    }

    public void setFomPk(Long fomPk) {
        this.fomPk = fomPk;
    }

    public String getFomCodigo() {
        return fomCodigo;
    }

    public void setFomCodigo(String fomCodigo) {
        this.fomCodigo = fomCodigo;
    }

    public String getFomNombre() {
        return fomNombre;
    }

    public void setFomNombre(String fomNombre) {
        this.fomNombre = fomNombre;
    }

    public String getFomNombreBusqueda() {
        return fomNombreBusqueda;
    }

    public void setFomNombreBusqueda(String fomNombreBusqueda) {
        this.fomNombreBusqueda = fomNombreBusqueda;
    }

    public Boolean getFomHabilitado() {
        return fomHabilitado;
    }

    public void setFomHabilitado(Boolean fomHabilitado) {
        this.fomHabilitado = fomHabilitado;
    }

    public LocalDateTime getFomUltModFecha() {
        return fomUltModFecha;
    }

    public void setFomUltModFecha(LocalDateTime fomUltModFecha) {
        this.fomUltModFecha = fomUltModFecha;
    }

    public String getFomUltModUsuario() {
        return fomUltModUsuario;
    }

    public void setFomUltModUsuario(String fomUltModUsuario) {
        this.fomUltModUsuario = fomUltModUsuario;
    }

    public Integer getFomVersion() {
        return fomVersion;
    }

    public void setFomVersion(Integer fomVersion) {
        this.fomVersion = fomVersion;
    }

    public String getFomTextoLargo() {
        return fomTextoLargo;
    }

    public void setFomTextoLargo(String fomTextoLargo) {
        this.fomTextoLargo = fomTextoLargo;
    }

    public String getFomDescripcion() {
        return fomDescripcion;
    }

    public void setFomDescripcion(String fomDescripcion) {
        this.fomDescripcion = fomDescripcion;
    }

    public EnumTipoFormula getFomTipoFormula() {
        return fomTipoFormula;
    }

    public void setFomTipoFormula(EnumTipoFormula fomTipoFormula) {
        this.fomTipoFormula = fomTipoFormula;
    }

    public List<SgFormula> getFomSubFormula() {
        return fomSubFormula;
    }

    public void setFomSubFormula(List<SgFormula> fomSubFormula) {
        this.fomSubFormula = fomSubFormula;
    }

    public Boolean getFomTienSubformula() {
        return fomTienSubformula;
    }

    public void setFomTienSubformula(Boolean fomTienSubformula) {
        this.fomTienSubformula = fomTienSubformula;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.fomPk);
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
        final SgFormula other = (SgFormula) obj;
        if (!Objects.equals(this.fomPk, other.fomPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgFormula{" + "fomPk=" + fomPk + ", fomCodigo=" + fomCodigo + ", fomNombre=" + fomNombre + ", fomNombreBusqueda=" + fomNombreBusqueda + ", fomHabilitado=" + fomHabilitado + ", fomUltModFecha=" + fomUltModFecha + ", fomUltModUsuario=" + fomUltModUsuario + ", fomVersion=" + fomVersion + '}';
    }
    
    

}
