/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.time.LocalDate;
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
import javax.persistence.OneToMany;
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
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgClasificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgImplementadora;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoCierreSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgVelocidadInternet;
import sv.gob.mined.siges.persistencia.entidades.si.SgSistemaSede;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sedPk")
    @NotAudited
    private List<SgSistemaSede> sedSistemas; // Modelado como lista para lazy loading, pero actualmente es solamente 1

    @JoinColumn(name = "sed_centro_adscrito")
    @ManyToOne
    private SgSedeLite sedSedeAdscritaDe;

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

    @OneToMany(mappedBy = "sduSede")
    @NotAudited
    private List<SgServicioEducativo> sedServicioEducativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dcoCentroEducativo")
    @NotAudited
    private List<SgDatoContratacion> sedDatoContratacion;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_sedes_jornadas",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "sed_pk"),
            inverseJoinColumns = @JoinColumn(name = "jla_pk"))
    private List<SgJornadaLaboral> sedJornadasLaborales;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rssSede")
    private List<SgRelSedeServicioInfra> sedRelServicioInfra;

    @OneToMany(mappedBy = "rdiSede")
    @NotAudited
    private List<SgReporteDirector> sedReporteDirector;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_sedes_factor_riesgo",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "sed_pk"),
            inverseJoinColumns = @JoinColumn(name = "rar_pk"))
    @NotAudited
    private List<SgRelSedeRiesgoAfectacion> sedFactoresRiesgoSocial;

    @Column(name = "sed_direccion_notificacion", length = 500)
    private String sedDireccionNotificacion;

    @Transient
    private Boolean sedOrigenExterno;

    @Transient
    private Boolean sedTieneAdscritas;

    @OneToMany(mappedBy = "cviSede")
    @NotAudited
    private List<SgCasoViolacion> sedCasoViolacion;

    @OneToMany(mappedBy = "apeSede")
    @NotAudited
    private List<SgAccionPrevenirEmbarazo> sedAccionPrevenirEmbarazo;

    @OneToMany(mappedBy = "aseSede")
    @NotAudited
    private List<SgAsistenciaSede> sedAsistencia;

    @OneToMany(mappedBy = "friSede")
    @NotAudited
    private List<SgFactorRiesgoSede> sedFactorRiesgo;

    @OneToMany(mappedBy = "ocsSede")
    @NotAudited
    private List<SgOrganismoCESede> sedOrganismoCE;

    @Column(name = "sed_activo")
    private Boolean sedActivo;

    @JoinColumn(name = "sed_implementadora_fk")
    @ManyToOne
    private SgImplementadora sedImplementadora;
    
    @Column(name = "sed_numero_tramite_creacion")
    private String sedNumeroTramiteCreacion;
    
    @Column(name = "sed_numero_tramite_cambio_nominacion")
    private String sedNumeroTramiteCambioNominacion;
    
    @Column(name = "sed_numero_tramite_cambio_domicilio")
    private String sedNumeroTramiteCambioDomicilio;
    
    //DATOS CIERRE
    
    @Column(name = "sed_numero_tramite_revocatoria")
    private String sedNumeroTramiteRevocatoria;
    
    @JoinColumn(name = "sed_motivo_cierre_fk")
    @ManyToOne        
    private SgMotivoCierreSede sedMotivoCierre;
    
    @Column(name = "sed_numero_acta_cierre")
    private String sedNumeroActaCierre;
    
    @Column(name = "sed_observacion_cierre")
    private String sedObservacionCierre;
    
    @Column(name = "sed_fecha_cierre")
    private LocalDate sedFechaCierre;
    
    //FIN DATOS CIERRE


    @Transient
    private Boolean sedEsAdscriptaAOtraSede;
    
    @Transient
    private Long sedCantidadMatriculasValidadasAnioCorriente;
    
    @Transient
    private Long sedCantidadMatriculasNoValidadasAnioCorriente;
    
    @Transient
    private Long sedCantidadSeccionesAbiertasAnioCorriente;
    
    @Transient
    private SgTipoOrganismoAdministrativo cedTipoOrganismoAdministrativo;

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
    
    @JsonIgnore
    public String getSedCodigoNombre() {
        return this.sedCodigo + " - " + this.sedNombre;
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

    public List<SgServicioEducativo> getSedServicioEducativo() {
        return sedServicioEducativo;
    }

    public void setSedServicioEducativo(List<SgServicioEducativo> sedServicioEducativo) {
        this.sedServicioEducativo = sedServicioEducativo;
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

    public SgSedeLite getSedSedeAdscritaDe() {
        return sedSedeAdscritaDe;
    }

    public void setSedSedeAdscritaDe(SgSedeLite sedSedeAdscritaDe) {
        this.sedSedeAdscritaDe = sedSedeAdscritaDe;
    }

    public List<SgDatoContratacion> getSedDatoContratacion() {
        return sedDatoContratacion;
    }

    public void setSedDatoContratacion(List<SgDatoContratacion> sedDatoContratacion) {
        this.sedDatoContratacion = sedDatoContratacion;
    }

    public List<SgRelSedeServicioInfra> getSedRelServicioInfra() {
        return sedRelServicioInfra;
    }

    public void setSedRelServicioInfra(List<SgRelSedeServicioInfra> sedRelServicioInfra) {
        this.sedRelServicioInfra = sedRelServicioInfra;
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

    public List<SgReporteDirector> getSedReporteDirector() {
        return sedReporteDirector;
    }

    public void setSedReporteDirector(List<SgReporteDirector> sedReporteDirector) {
        this.sedReporteDirector = sedReporteDirector;
    }

    public List<SgRelSedeRiesgoAfectacion> getSedFactoresRiesgoSocial() {
        return sedFactoresRiesgoSocial;
    }

    public void setSedFactoresRiesgoSocial(List<SgRelSedeRiesgoAfectacion> sedFactoresRiesgoSocial) {
        this.sedFactoresRiesgoSocial = sedFactoresRiesgoSocial;
    }

    public List<SgOrganismoCESede> getSedOrganismoCE() {
        return sedOrganismoCE;
    }

    public void setSedOrganismoCE(List<SgOrganismoCESede> sedOrganismoCE) {
        this.sedOrganismoCE = sedOrganismoCE;
    }
    

    public SgImplementadora getSedImplementadora() {
        return sedImplementadora;
    }

    public void setSedImplementadora(SgImplementadora sedImplementadora) {
        this.sedImplementadora = sedImplementadora;
    }

    public List<SgCasoViolacion> getSedCasoViolacion() {
        return sedCasoViolacion;
    }

    public void setSedCasoViolacion(List<SgCasoViolacion> sedCasoViolacion) {
        this.sedCasoViolacion = sedCasoViolacion;
    }

    public List<SgAccionPrevenirEmbarazo> getSedAccionPrevenirEmbarazo() {
        return sedAccionPrevenirEmbarazo;
    }

    public void setSedAccionPrevenirEmbarazo(List<SgAccionPrevenirEmbarazo> sedAccionPrevenirEmbarazo) {
        this.sedAccionPrevenirEmbarazo = sedAccionPrevenirEmbarazo;
    }

    public List<SgAsistenciaSede> getSedAsistencia() {
        return sedAsistencia;
    }

    public void setSedAsistencia(List<SgAsistenciaSede> sedAsistencia) {
        this.sedAsistencia = sedAsistencia;
    }

    public List<SgFactorRiesgoSede> getSedFactorRiesgo() {
        return sedFactorRiesgo;
    }

    public void setSedFactorRiesgo(List<SgFactorRiesgoSede> sedFactorRiesgo) {
        this.sedFactorRiesgo = sedFactorRiesgo;
    }

    public Boolean getSedActivo() {
        return sedActivo;
    }

    public void setSedActivo(Boolean sedActivo) {
        this.sedActivo = sedActivo;
    }

    public List<SgSistemaSede> getSedSistemas() {
        return sedSistemas;
    }

    public void setSedSistemas(List<SgSistemaSede> sedSistemas) {
        this.sedSistemas = sedSistemas;
    }

    public Boolean getSedEsAdscriptaAOtraSede() {
        return sedEsAdscriptaAOtraSede;
    }

    public void setSedEsAdscriptaAOtraSede(Boolean sedEsAdscriptaAOtraSede) {
        this.sedEsAdscriptaAOtraSede = sedEsAdscriptaAOtraSede;
    }

    public Long getSedCantidadMatriculasValidadasAnioCorriente() {
        return sedCantidadMatriculasValidadasAnioCorriente;
    }

    public void setSedCantidadMatriculasValidadasAnioCorriente(Long sedCantidadMatriculasValidadasAnioCorriente) {
        this.sedCantidadMatriculasValidadasAnioCorriente = sedCantidadMatriculasValidadasAnioCorriente;
    }

    public Long getSedCantidadMatriculasNoValidadasAnioCorriente() {
        return sedCantidadMatriculasNoValidadasAnioCorriente;
    }

    public void setSedCantidadMatriculasNoValidadasAnioCorriente(Long sedCantidadMatriculasNoValidadasAnioCorriente) {
        this.sedCantidadMatriculasNoValidadasAnioCorriente = sedCantidadMatriculasNoValidadasAnioCorriente;
    }

    public Long getSedCantidadSeccionesAbiertasAnioCorriente() {
        return sedCantidadSeccionesAbiertasAnioCorriente;
    }

    public void setSedCantidadSeccionesAbiertasAnioCorriente(Long sedCantidadSeccionesAbiertasAnioCorriente) {
        this.sedCantidadSeccionesAbiertasAnioCorriente = sedCantidadSeccionesAbiertasAnioCorriente;
    }

    public String getSedNumeroTramiteRevocatoria() {
        return sedNumeroTramiteRevocatoria;
    }

    public void setSedNumeroTramiteRevocatoria(String sedNumeroTramiteRevocatoria) {
        this.sedNumeroTramiteRevocatoria = sedNumeroTramiteRevocatoria;
    }

    public String getSedNumeroTramiteCambioNominacion() {
        return sedNumeroTramiteCambioNominacion;
    }

    public void setSedNumeroTramiteCambioNominacion(String sedNumeroTramiteCambioNominacion) {
        this.sedNumeroTramiteCambioNominacion = sedNumeroTramiteCambioNominacion;
    }

    public String getSedNumeroTramiteCambioDomicilio() {
        return sedNumeroTramiteCambioDomicilio;
    }

    public void setSedNumeroTramiteCambioDomicilio(String sedNumeroTramiteCambioDomicilio) {
        this.sedNumeroTramiteCambioDomicilio = sedNumeroTramiteCambioDomicilio;
    }

    public String getSedNumeroTramiteCreacion() {
        return sedNumeroTramiteCreacion;
    }

    public void setSedNumeroTramiteCreacion(String sedNumeroTramiteCreacion) {
        this.sedNumeroTramiteCreacion = sedNumeroTramiteCreacion;
    }

    public SgMotivoCierreSede getSedMotivoCierre() {
        return sedMotivoCierre;
    }

    public void setSedMotivoCierre(SgMotivoCierreSede sedMotivoCierre) {
        this.sedMotivoCierre = sedMotivoCierre;
    }

    public String getSedNumeroActaCierre() {
        return sedNumeroActaCierre;
    }

    public void setSedNumeroActaCierre(String sedNumeroActaCierre) {
        this.sedNumeroActaCierre = sedNumeroActaCierre;
    }

    public String getSedObservacionCierre() {
        return sedObservacionCierre;
    }

    public void setSedObservacionCierre(String sedObservacionCierre) {
        this.sedObservacionCierre = sedObservacionCierre;
    }

    public LocalDate getSedFechaCierre() {
        return sedFechaCierre;
    }

    public void setSedFechaCierre(LocalDate sedFechaCierre) {
        this.sedFechaCierre = sedFechaCierre;
    }

    public SgTipoOrganismoAdministrativo getCedTipoOrganismoAdministrativo() {
        return cedTipoOrganismoAdministrativo;
    }

    public void setCedTipoOrganismoAdministrativo(SgTipoOrganismoAdministrativo cedTipoOrganismoAdministrativo) {
        this.cedTipoOrganismoAdministrativo = cedTipoOrganismoAdministrativo;
    }
    
       
     
    @Override
    public String securityAmbitCreate() {
        if (this.sedSedeAdscritaDe != null) {
            return null;
        }
        return "sedDireccion.dirDepartamento";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduSeccion.secAsociacion.asoPk", o.getContext());
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

}
