/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.centros;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgDetalleSede;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgClasificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgVelocidadInternet;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_sedes", uniqueConstraints = {
    @UniqueConstraint(name = "sed_codigo_uk", columnNames = {"sed_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "sed_tipo", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "sedTipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SgCentroEducativoOficial.class, name = TipoSede.Codes.CENTRO_EDUCATIVO_OFICIAL)
    ,@JsonSubTypes.Type(value = SgCentroEducativoPrivado.class, name = TipoSede.Codes.CENTRO_EDUCATIVO_PRIVADO)
    ,@JsonSubTypes.Type(value = SgCirculoAlfabetizacion.class, name = TipoSede.Codes.CIRCULO_ALFABETIZACION)
    ,@JsonSubTypes.Type(value = SgCirculoInfancia.class, name = TipoSede.Codes.CIRCULO_PRIMERA_INFANCIA)
    ,@JsonSubTypes.Type(value = SgSedeEducame.class, name = TipoSede.Codes.EDUCAME)
})
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sedPk", scope = SgSede.class)
@Audited
public class SgSede implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "sed_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sedPk;

    @Column(name = "sed_codigo")
    @Size(max = 15)
    private String sedCodigo;

    @Column(name = "sed_nombre")
    @Size(max = 500)
    private String sedNombre;

    @Column(name = "sed_nombre_busqueda")
    @Size(max = 500)
    private String sedNombreBusqueda;

    @Column(name = "sed_habilitado")
    private Boolean sedHabilitado;

    @JoinColumn(name = "sed_direccion_fk")
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private SgDireccion sedDireccion;

    @JoinColumn(name = "sed_centro_adscrito")
    @ManyToOne
    private SgSede sedSedeAdscritaDe;

    @Column(name = "sed_telefono")
    @Size(max = 14)
    private String sedTelefono;

    @Column(name = "sed_telefono_movil")
    @Size(max = 14)
    private String sedTelefonoMovil;

    @Column(name = "sed_correo_electronico")
    @Size(max = 255)
    private String sedCorreoElectronico;

    @Column(name = "sed_correo_electronico_2")
    @Size(max = 255)
    private String sedCorreoElectronico2;

    @Column(name = "sed_sitio_web")
    @Size(max = 255)
    private String sedSitioWeb;

    @Column(name = "sed_anio_inicio_act")
    private Integer sedAnioInicioAct;

    @Column(name = "sed_propietario")
    @Size(max = 500)
    private String sedPropietario;

    @Column(name = "sed_nota")
    @Size(max = 4000)
    private String sedNota;

    @JoinColumn(name = "sed_clasificacion_fk")
    @ManyToOne
    private SgClasificacion sedClasificacion;

    @JoinColumn(name = "sed_tipo_calendario_fk")
    @ManyToOne
    private SgTipoCalendarioEscolar sedTipoCalendario;

    @Column(name = "sed_tipo", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoSede sedTipo;

    @Column(name = "sed_internet")
    private Boolean sedInternet;

    @JoinColumn(name = "sed_velocidad_internet_fk")
    @ManyToOne
    private SgVelocidadInternet sedVelocidadInternet;

    @Column(name = "sed_proveedor_internet")
    @Size(max = 255)
    private String sedProveedorInternet;

    @Column(name = "sed_religiosa")
    private Boolean sedReligiosa;

    @Column(name = "sed_religion")
    @Size(max = 255)
    private String sedReligion;

    @Column(name = "sed_comite_pedagogico")
    private Boolean sedComitePedagogico;

    @Column(name = "sed_comite_padres_madres")
    private Boolean sedComitePadresMadres;

    @Column(name = "sed_comite_estudiantil")
    private Boolean sedComiteEstudiantil;

    @Column(name = "sed_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sedUltModFecha;

    @Size(max = 45)
    @Column(name = "sed_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sedUltModUsuario;

    @Column(name = "sed_version")
    @Version
    private Integer sedVersion;

    @Column(name = "sed_migracion")
    private Boolean sedMigracion;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_sedes_jornadas",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "sed_pk"),
            inverseJoinColumns = @JoinColumn(name = "jla_pk"))
    private List<SgJornadaLaboral> sedJornadasLaborales;

    @Column(name = "sed_direccion_notificacion", length = 500)
    private String sedDireccionNotificacion;

    @Transient
    private Boolean sedOrigenExterno;

    @Transient
    private Boolean sedTieneAdscritas;

    @Column(name = "sed_activo")
    private Boolean sedActivo;
    
    @Transient
    private String jornadas;
    
    @Transient
    private List<SgDetalleSede> detalles;

    public SgSede() {
    }

    public SgSede(Long sedPk) {
        this.sedPk = sedPk;
    }

    public SgSede(Long sedPk, Integer sedVersion) {
        this.sedPk = sedPk;
        this.sedVersion = sedVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sedNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sedNombre);
        if (BooleanUtils.isFalse(this.sedHabilitado)) {
            this.sedActivo = Boolean.FALSE;
        }
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public String getSedTelefono() {
        return sedTelefono;
    }

    public void setSedTelefono(String sedTelefono) {
        this.sedTelefono = sedTelefono;
    }

    public String getSedCorreoElectronico() {
        return sedCorreoElectronico;
    }

    public void setSedCorreoElectronico(String sedCorreoElectronico) {
        this.sedCorreoElectronico = sedCorreoElectronico;
    }

    public String getSedCorreoElectronico2() {
        return sedCorreoElectronico2;
    }

    public void setSedCorreoElectronico2(String sedCorreoElectronico2) {
        this.sedCorreoElectronico2 = sedCorreoElectronico2;
    }

    public String getSedSitioWeb() {
        return sedSitioWeb;
    }

    public void setSedSitioWeb(String sedSitioWeb) {
        this.sedSitioWeb = sedSitioWeb;
    }

    public Integer getSedAnioInicioAct() {
        return sedAnioInicioAct;
    }

    public void setSedAnioInicioAct(Integer sedAnioInicioAct) {
        this.sedAnioInicioAct = sedAnioInicioAct;
    }

    public String getSedPropietario() {
        return sedPropietario;
    }

    public void setSedPropietario(String sedPropietario) {
        this.sedPropietario = sedPropietario;
    }

    public String getSedTelefonoMovil() {
        return sedTelefonoMovil;
    }

    public void setSedTelefonoMovil(String sedTelefonoMovil) {
        this.sedTelefonoMovil = sedTelefonoMovil;
    }

    public String getSedNota() {
        return sedNota;
    }

    public void setSedNota(String sedNota) {
        this.sedNota = sedNota;
    }

    public SgClasificacion getSedClasificacion() {
        return sedClasificacion;
    }

    public void setSedClasificacion(SgClasificacion sedClasificacion) {
        this.sedClasificacion = sedClasificacion;
    }

    public Boolean getSedInternet() {
        return sedInternet;
    }

    public void setSedInternet(Boolean sedInternet) {
        this.sedInternet = sedInternet;
    }

    public SgVelocidadInternet getSedVelocidadInternet() {
        return sedVelocidadInternet;
    }

    public void setSedVelocidadInternet(SgVelocidadInternet sedVelocidadInternet) {
        this.sedVelocidadInternet = sedVelocidadInternet;
    }

    public String getSedProveedorInternet() {
        return sedProveedorInternet;
    }

    public void setSedProveedorInternet(String sedProveedorInternet) {
        this.sedProveedorInternet = sedProveedorInternet;
    }

    public Boolean getSedReligiosa() {
        return sedReligiosa;
    }

    public void setSedReligiosa(Boolean sedReligiosa) {
        this.sedReligiosa = sedReligiosa;
    }

    public String getSedReligion() {
        return sedReligion;
    }

    public void setSedReligion(String sedReligion) {
        this.sedReligion = sedReligion;
    }

    public Boolean getSedComitePedagogico() {
        return sedComitePedagogico;
    }

    public void setSedComitePedagogico(Boolean sedComitePedagogico) {
        this.sedComitePedagogico = sedComitePedagogico;
    }

    public Boolean getSedComitePadresMadres() {
        return sedComitePadresMadres;
    }

    public void setSedComitePadresMadres(Boolean sedComitePadresMadres) {
        this.sedComitePadresMadres = sedComitePadresMadres;
    }

    public Boolean getSedComiteEstudiantil() {
        return sedComiteEstudiantil;
    }

    public void setSedComiteEstudiantil(Boolean sedComiteEstudiantil) {
        this.sedComiteEstudiantil = sedComiteEstudiantil;
    }

    public LocalDateTime getSedUltModFecha() {
        return sedUltModFecha;
    }

    public void setSedUltModFecha(LocalDateTime sedUltModFecha) {
        this.sedUltModFecha = sedUltModFecha;
    }

    public String getSedUltModUsuario() {
        return sedUltModUsuario;
    }

    public void setSedUltModUsuario(String sedUltModUsuario) {
        this.sedUltModUsuario = sedUltModUsuario;
    }

    public Integer getSedVersion() {
        return sedVersion;
    }

    public void setSedVersion(Integer sedVersion) {
        this.sedVersion = sedVersion;
    }

    public Boolean getSedMigracion() {
        return sedMigracion;
    }

    public void setSedMigracion(Boolean sedMigracion) {
        this.sedMigracion = sedMigracion;
    }

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }

    public SgTipoCalendarioEscolar getSedTipoCalendario() {
        return sedTipoCalendario;
    }

    public void setSedTipoCalendario(SgTipoCalendarioEscolar sedTipoCalendario) {
        this.sedTipoCalendario = sedTipoCalendario;
    }
    
    public SgDireccion getSedDireccion() {
        return sedDireccion;
    }

    public void setSedDireccion(SgDireccion sedDireccion) {
        this.sedDireccion = sedDireccion;
    }

    public String getSedNombreBusqueda() {
        return sedNombreBusqueda;
    }

    public void setSedNombreBusqueda(String sedNombreBusqueda) {
        this.sedNombreBusqueda = sedNombreBusqueda;
    }

    public List<SgJornadaLaboral> getSedJornadasLaborales() {
        return sedJornadasLaborales;
    }

    public void setSedJornadasLaborales(List<SgJornadaLaboral> sedJornadasLaborales) {
        this.sedJornadasLaborales = sedJornadasLaborales;
    }

    public SgSede getSedSedeAdscritaDe() {
        return sedSedeAdscritaDe;
    }

    public void setSedSedeAdscritaDe(SgSede sedSedeAdscritaDe) {
        this.sedSedeAdscritaDe = sedSedeAdscritaDe;
    }

    public String getSedDireccionNotificacion() {
        return sedDireccionNotificacion;
    }

    public void setSedDireccionNotificacion(String sedDireccionNotificacion) {
        this.sedDireccionNotificacion = sedDireccionNotificacion;
    }

    public Boolean getSedOrigenExterno() {
        return sedOrigenExterno;
    }

    public void setSedOrigenExterno(Boolean sedOrigenExterno) {
        this.sedOrigenExterno = sedOrigenExterno;
    }

    public Boolean getSedTieneAdscritas() {
        return sedTieneAdscritas;
    }

    public void setSedTieneAdscritas(Boolean sedTieneAdscritas) {
        this.sedTieneAdscritas = sedTieneAdscritas;
    }

    @Override
    public String securityAmbitCreate() {
        return "sedDireccion.dirDepartamento"; //TODO: para permitir crear adscriptas en un departamento distinto al de la sede padre, hay que modificar el DAO
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.sedPk);
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
        final SgSede other = (SgSede) obj;
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSede[ sedPk=" + sedPk + " ]";
    }

    public Boolean getSedActivo() {
        return sedActivo;
    }

    public void setSedActivo(Boolean sedActivo) {
        this.sedActivo = sedActivo;
    }

    public String getJornadas() {
        return jornadas;
    }

    public void setJornadas(String jornadas) {
        this.jornadas = jornadas;
    }

    public List<SgDetalleSede> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<SgDetalleSede> detalles) {
        this.detalles = detalles;
    }

}
