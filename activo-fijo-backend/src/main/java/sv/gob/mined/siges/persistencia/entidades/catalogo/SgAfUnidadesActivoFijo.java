/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
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
@Table(name = "sg_af_unidades_activo_fijo", uniqueConstraints = {
    @UniqueConstraint(name = "uaf_codigo_uk", columnNames = {"uaf_codigo"})
    ,
    @UniqueConstraint(name = "auf_nombre_uk", columnNames = {"uaf_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uafPk", resolver = JsonIdentityResolver.class,scope = SgAfUnidadesActivoFijo.class)
public class SgAfUnidadesActivoFijo implements DataSecurity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "uaf_pk", nullable = false)
    private Long uafPk;

    @Size(max = 5)
    @Column(name = "uaf_codigo", length = 5)
    @AtributoCodigo
    private String uafCodigo;

    @Size(max = 255)
    @AtributoNormalizable
    @Column(name = "uaf_nombre", length = 255)
    private String uafNombre;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "uaf_nombre_busqueda", length = 255)
    private String uafNombreBusqueda;
    
    @Column(name = "uaf_habilitado")
    @AtributoHabilitado
    private Boolean uafHabilitado;

    @JoinColumn(name = "uaf_departamento_fk", referencedColumnName = "dep_pk")
    @ManyToOne
    private SgDepartamento uafDepartamento;

    @Column(name = "uaf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime uafUltModFecha;

    @Size(max = 45)
    @Column(name = "uaf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String uafUltModUsuario;

    @Size(max = 100)
    @Column(name = "uaf_responsable_af",  length = 100)
    private String uafResponsableAF;
    
    @Size(max = 100)
    @Column(name = "uaf_cargo_responsable_af",  length = 100)
    private String uafCargoResponsableAF;
    
    @Column(name = "uaf_version")
    @Version
    private Integer uafVersion;
    
    @OneToMany(mappedBy = "uadUnidadActivoFijoFk", fetch = FetchType.LAZY)
    @NotAudited
    private List<SgAfUnidadesAdministrativas> uafUnidadesAdministrativas;
    
    public SgAfUnidadesActivoFijo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.uafNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.uafNombre);
    }
    
    public Long getUafPk() {
        return uafPk;
    }

    public void setUafPk(Long uafPk) {
        this.uafPk = uafPk;
    }

    public String getUafCodigo() {
        return uafCodigo;
    }

    public void setUafCodigo(String uafCodigo) {
        this.uafCodigo = uafCodigo;
    }

    public String getUafNombre() {
        return uafNombre;
    }

    public void setUafNombre(String uafNombre) {
        this.uafNombre = uafNombre;
    }

    public String getUafNombreBusqueda() {
        return uafNombreBusqueda;
    }

    public void setUafNombreBusqueda(String uafNombreBusqueda) {
        this.uafNombreBusqueda = uafNombreBusqueda;
    }

    public Boolean getUafHabilitado() {
        return uafHabilitado;
    }

    public void setUafHabilitado(Boolean uafHabilitado) {
        this.uafHabilitado = uafHabilitado;
    }

    public SgDepartamento getUafDepartamento() {
        return uafDepartamento;
    }

    public void setUafDepartamento(SgDepartamento uafDepartamento) {
        this.uafDepartamento = uafDepartamento;
    }

    public LocalDateTime getUafUltModFecha() {
        return uafUltModFecha;
    }

    public void setUafUltModFecha(LocalDateTime uafUltModFecha) {
        this.uafUltModFecha = uafUltModFecha;
    }

    public String getUafUltModUsuario() {
        return uafUltModUsuario;
    }

    public void setUafUltModUsuario(String uafUltModUsuario) {
        this.uafUltModUsuario = uafUltModUsuario;
    }

    public Integer getUafVersion() {
        return uafVersion;
    }

    public void setUafVersion(Integer uafVersion) {
        this.uafVersion = uafVersion;
    }

    public List<SgAfUnidadesAdministrativas> getUafUnidadesAdministrativas() {
        return uafUnidadesAdministrativas;
    }

    public void setUafUnidadesAdministrativas(List<SgAfUnidadesAdministrativas> uafUnidadesAdministrativas) {
        this.uafUnidadesAdministrativas = uafUnidadesAdministrativas;
    }

    public String getUafResponsableAF() {
        return uafResponsableAF;
    }

    public void setUafResponsableAF(String uafResponsableAF) {
        this.uafResponsableAF = uafResponsableAF;
    }

    public String getUafCargoResponsableAF() {
        return uafCargoResponsableAF;
    }

    public void setUafCargoResponsableAF(String uafCargoResponsableAF) {
        this.uafCargoResponsableAF = uafCargoResponsableAF;
    }
    
    @Override
    public String securityAmbitCreate() {
       return "uafDepartamento";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "uafDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.UNIDAD_ADMINISTRATIVA.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "uafUnidadesAdministrativas.uadPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
             return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "uafDepartamento.depDirecciones.dirSedes.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "uaf", -1L);
        } 
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.uafPk);
        return hash;
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
        final SgAfUnidadesActivoFijo other = (SgAfUnidadesActivoFijo) obj;
        if (!Objects.equals(this.uafPk, other.uafPk)) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesActivoFijo{" + "uafPk=" + uafPk + '}';
    }
}