/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_departamentos", uniqueConstraints = {
    @UniqueConstraint(name = "dep_codigo_uk", columnNames = {"dep_codigo"})
    ,
    @UniqueConstraint(name = "dep_nombre_uk", columnNames = {"dep_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "depPk", scope = SgDepartamento.class)
public class SgDepartamento implements Serializable , DataSecurity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dep_pk", nullable = false)
    private Long depPk;

    @OneToMany(mappedBy = "munDepartamento", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SgMunicipio> depMunicipios;

    @Size(max = 4)
    @Column(name = "dep_codigo", length = 4)
    @AtributoCodigo
    private String depCodigo;

    @Size(max = 255)
    @Column(name = "dep_nombre", length = 255)
    @AtributoNormalizable
    private String depNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "dep_nombre_busqueda", length = 255)
    private String depNombreBusqueda;

    @Column(name = "dep_habilitado")
    @AtributoHabilitado
    private Boolean depHabilitado;

    @Column(name = "dep_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime depUltModFecha;

    @Size(max = 45)
    @Column(name = "dep_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String depUltModUsuario;

    @Column(name = "dep_version")
    @Version
    private Integer depVersion;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dirDepartamento")
    @NotAudited
    private List<SgDireccion> depDireccion;

    public SgDepartamento(Long depPk, Integer depVersion) {
        this.depPk = depPk;
        this.depVersion = depVersion;
    }
    


    public SgDepartamento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.depNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.depNombre);
    }

    public Long getDepPk() {
        return depPk;
    }

    public void setDepPk(Long depPk) {
        this.depPk = depPk;
    }

    public String getDepCodigo() {
        return depCodigo;
    }

    public void setDepCodigo(String depCodigo) {
        this.depCodigo = depCodigo;
    }

    public String getDepNombre() {
        return depNombre;
    }

    public void setDepNombre(String depNombre) {
        this.depNombre = depNombre;
    }

    public String getDepNombreBusqueda() {
        return depNombreBusqueda;
    }

    public void setDepNombreBusqueda(String depNombreBusqueda) {
        this.depNombreBusqueda = depNombreBusqueda;
    }

    public Boolean getDepHabilitado() {
        return depHabilitado;
    }

    public void setDepHabilitado(Boolean depHabilitado) {
        this.depHabilitado = depHabilitado;
    }

    public LocalDateTime getDepUltModFecha() {
        return depUltModFecha;
    }

    public void setDepUltModFecha(LocalDateTime depUltModFecha) {
        this.depUltModFecha = depUltModFecha;
    }

    public String getDepUltModUsuario() {
        return depUltModUsuario;
    }

    public void setDepUltModUsuario(String depUltModUsuario) {
        this.depUltModUsuario = depUltModUsuario;
    }

    public Integer getDepVersion() {
        return depVersion;
    }

    public void setDepVersion(Integer depVersion) {
        this.depVersion = depVersion;
    }

    public List<SgMunicipio> getDepMunicipios() {
        return depMunicipios;
    }

    public void setDepMunicipios(List<SgMunicipio> depMunicipios) {
        this.depMunicipios = depMunicipios;
    }

    public List<SgDireccion> getDepDireccion() {
        return depDireccion;
    }

    public void setDepDireccion(List<SgDireccion> depDireccion) {
        this.depDireccion = depDireccion;
    }
    
    @Override
    public String securityAmbitCreate() {
        return null; //TODO: para permitir crear adscriptas en un departamento distinto al de la sede padre, hay que modificar el DAO
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depPk", o.getContext());                       
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depDireccion.dirSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depDireccion.dirSede.sedServicioEducativo.sduSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.depPk);
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
        final SgDepartamento other = (SgDepartamento) obj;
        if (!Objects.equals(this.depPk, other.depPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesDepartamento{" + "depPk=" + depPk + '}';
    }

    public SgDepartamento(Long depPk) {
        this.depPk = depPk;
    }
}
