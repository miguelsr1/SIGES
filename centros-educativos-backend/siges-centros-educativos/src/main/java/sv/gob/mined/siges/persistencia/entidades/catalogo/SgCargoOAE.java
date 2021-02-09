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
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cargo_oae", uniqueConstraints = {
    @UniqueConstraint(name = "coa_codigo_uk", columnNames = {"coa_codigo"})
    ,
    @UniqueConstraint(name = "coa_nombre_uk", columnNames = {"coa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "coaPk", scope = SgCargoOAE.class)
@Audited
public class SgCargoOAE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "coa_pk", nullable = false)
    private Long coaPk;

    @Size(max = 45)
    @Column(name = "coa_codigo", length = 45)
    @AtributoCodigo
    private String coaCodigo;

    @Size(max = 255)
    @Column(name = "coa_nombre", length = 255)
    @AtributoNormalizable
    private String coaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "coa_nombre_busqueda", length = 255)
    private String coaNombreBusqueda;

    @Column(name = "coa_habilitado")
    @AtributoHabilitado
    private Boolean coaHabilitado;

    @Column(name = "coa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime coaUltModFecha;

    @Size(max = 45)
    @Column(name = "coa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String coaUltModUsuario;

    @Column(name = "coa_version")
    @Version
    private Integer coaVersion;
    
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_cargos_tipo_oae",
            schema = Constantes.SCHEMA_CATALOGO,
            joinColumns = @JoinColumn(name = "coa_pk"),
            inverseJoinColumns = @JoinColumn(name = "toa_pk"))
    private List<SgTipoOrganismoAdministrativo> coaTiposOrganismoAdministrativo;
    
    @Column(name = "coa_orden")
    private Integer coaOrden;
    
    @Size(max = 255)
    @Column(name = "coa_nombre_masculino", length = 255)
    @AtributoNormalizable
    private String coaNombreMasculino;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "coa_nombre_busqueda_masculino", length = 255)
    private String coaNombreBusquedaMasculino;
    
    public SgCargoOAE() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.coaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.coaNombre);
        this.coaNombreBusquedaMasculino = SofisStringUtils.normalizarParaBusqueda(this.coaNombreMasculino);
    }

    public Long getCoaPk() {
        return coaPk;
    }

    public void setCoaPk(Long coaPk) {
        this.coaPk = coaPk;
    }

    public String getCoaCodigo() {
        return coaCodigo;
    }

    public void setCoaCodigo(String coaCodigo) {
        this.coaCodigo = coaCodigo;
    }

    public String getCoaNombre() {
        return coaNombre;
    }

    public void setCoaNombre(String coaNombre) {
        this.coaNombre = coaNombre;
    }

    public String getCoaNombreBusqueda() {
        return coaNombreBusqueda;
    }

    public void setCoaNombreBusqueda(String coaNombreBusqueda) {
        this.coaNombreBusqueda = coaNombreBusqueda;
    }

    public Boolean getCoaHabilitado() {
        return coaHabilitado;
    }

    public void setCoaHabilitado(Boolean coaHabilitado) {
        this.coaHabilitado = coaHabilitado;
    }

    public LocalDateTime getCoaUltModFecha() {
        return coaUltModFecha;
    }

    public void setCoaUltModFecha(LocalDateTime coaUltModFecha) {
        this.coaUltModFecha = coaUltModFecha;
    }

    public String getCoaUltModUsuario() {
        return coaUltModUsuario;
    }

    public void setCoaUltModUsuario(String coaUltModUsuario) {
        this.coaUltModUsuario = coaUltModUsuario;
    }

    public Integer getCoaVersion() {
        return coaVersion;
    }

    public void setCoaVersion(Integer coaVersion) {
        this.coaVersion = coaVersion;
    }

    public List<SgTipoOrganismoAdministrativo> getCoaTiposOrganismoAdministrativo() {
        return coaTiposOrganismoAdministrativo;
    }

    public void setCoaTiposOrganismoAdministrativo(List<SgTipoOrganismoAdministrativo> coaTiposOrganismoAdministrativo) {
        this.coaTiposOrganismoAdministrativo = coaTiposOrganismoAdministrativo;
    }

    public Integer getCoaOrden() {
        return coaOrden;
    }

    public void setCoaOrden(Integer coaOrden) {
        this.coaOrden = coaOrden;
    }

    public String getCoaNombreMasculino() {
        return coaNombreMasculino;
    }

    public void setCoaNombreMasculino(String coaNombreMasculino) {
        this.coaNombreMasculino = coaNombreMasculino;
    }

    public String getCoaNombreBusquedaMasculino() {
        return coaNombreBusquedaMasculino;
    }

    public void setCoaNombreBusquedaMasculino(String coaNombreBusquedaMasculino) {
        this.coaNombreBusquedaMasculino = coaNombreBusquedaMasculino;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.coaPk);
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
        final SgCargoOAE other = (SgCargoOAE) obj;
        if (!Objects.equals(this.coaPk, other.coaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCargoOAE{" + "coaPk=" + coaPk + ", coaCodigo=" + coaCodigo + ", coaNombre=" + coaNombre + ", coaNombreBusqueda=" + coaNombreBusqueda + ", coaHabilitado=" + coaHabilitado + ", coaUltModFecha=" + coaUltModFecha + ", coaUltModUsuario=" + coaUltModUsuario + ", coaVersion=" + coaVersion + '}';
    }
    
    

}
