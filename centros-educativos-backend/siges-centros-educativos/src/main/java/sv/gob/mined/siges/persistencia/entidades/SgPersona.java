/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEtnia;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoSangre;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscolaridad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNacionalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgOcupacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProfesion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import sv.gob.mined.siges.enumerados.EnumEstadoPersona;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoFallecimiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgReferenciasApoyo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTerapia;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTrastornoAprendizaje;

@Entity
@Table(name = "sg_personas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "perPk", scope = SgPersona.class)
@Audited
@Indexed
public class SgPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "per_pk")
    private Long perPk;

    @Size(max = 100)
    @Column(name = "per_primer_nombre")
    @AtributoNormalizable
    private String perPrimerNombre;

    @Size(max = 100)
    @Column(name = "per_segundo_nombre")
    @AtributoNormalizable
    private String perSegundoNombre;

    @Size(max = 100)
    @Column(name = "per_tercer_nombre")
    @AtributoNormalizable
    private String perTercerNombre;

    @Size(max = 100)
    @Column(name = "per_primer_apellido")
    @AtributoNormalizable
    private String perPrimerApellido;

    @Size(max = 100)
    @Column(name = "per_segundo_apellido")
    @AtributoNormalizable
    private String perSegundoApellido;

    @Size(max = 100)
    @Column(name = "per_tercer_apellido")
    @AtributoNormalizable
    private String perTercerApellido;

    @Size(max = 100)
    @Column(name = "per_primer_nombre_busqueda")
    @Fields({
        @Field(name = "perPrimerNombreBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres")),
        @Field(name = "perPrimerNombreBusquedaPhonetic", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres_Phonetic")),
        @Field(name = "perPrimerNombreBusquedaSort", index = Index.YES, analyze = Analyze.YES, store = Store.NO, normalizer = @Normalizer(definition = "Normalizador_Nombres_Sort"))}
    )
    @SortableField(forField = "perPrimerNombreBusquedaSort")
    private String perPrimerNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_segundo_nombre_busqueda")
    @Fields({
        @Field(name = "perSegundoNombreBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres")),
        @Field(name = "perSegundoNombreBusquedaPhonetic", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres_Phonetic")),
        @Field(name = "perSegundoNombreBusquedaSort", index = Index.YES, analyze = Analyze.YES, store = Store.NO, normalizer = @Normalizer(definition = "Normalizador_Nombres_Sort"))}
    )
    @SortableField(forField = "perSegundoNombreBusquedaSort")
    private String perSegundoNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_tercer_nombre_busqueda")
    @Fields({
        @Field(name = "perTercerNombreBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO)})
    @Analyzer(definition = "Analizador_Nombres")
    private String perTercerNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_primer_apellido_busqueda")
    @Fields({
        @Field(name = "perPrimerApellidoBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres")),
        @Field(name = "perPrimerApellidoBusquedaPhonetic", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres_Phonetic")),
        @Field(name = "perPrimerApellidoBusquedaSort", index = Index.YES, analyze = Analyze.YES, store = Store.NO, normalizer = @Normalizer(definition = "Normalizador_Nombres_Sort"))}
    )
    @SortableField(forField = "perPrimerApellidoBusquedaSort")
    private String perPrimerApellidoBusqueda;

    @Size(max = 100)
    @Column(name = "per_segundo_apellido_busqueda")
    @Fields({
        @Field(name = "perSegundoApellidoBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres")),
        @Field(name = "perSegundoApellidoBusquedaPhonetic", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres_Phonetic")),
        @Field(name = "perSegundoApellidoBusquedaSort", index = Index.YES, analyze = Analyze.YES, store = Store.NO, normalizer = @Normalizer(definition = "Normalizador_Nombres_Sort"))}
    )
    @SortableField(forField = "perSegundoApellidoBusquedaSort")
    private String perSegundoApellidoBusqueda;

    @Size(max = 100)
    @Column(name = "per_tercer_apellido_busqueda")
    @Fields({
        @Field(name = "perTercerApellidoBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO)})
    @Analyzer(definition = "Analizador_Nombres")
    private String perTercerApellidoBusqueda;

    @Size(max = 650)
    @Column(name = "per_nombre_busqueda", length = 650)
    @Fields({
        @Field(name = "perNombreBusqueda", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres")),
        @Field(name = "perNombreBusquedaPhonetic", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "Analizador_Nombres_Phonetic")),
        @Field(name = "perNombreBusquedaSort", index = Index.NO, analyze = Analyze.YES, store = Store.NO, normalizer = @Normalizer(definition = "Normalizador_Nombres_Sort"))}
    )
    @SortableField(forField = "perNombreBusquedaSort")
    private String perNombreBusqueda;

    @Column(name = "per_fecha_nacimiento")
    @Fields({
        @Field(name = "perFechaNacimiento", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
        @Field(name = "perFechaNacimientoSort", index = Index.NO, analyze = Analyze.NO, store = Store.NO)}
    )
    @SortableField(forField = "perFechaNacimientoSort")
    @DateBridge(resolution = Resolution.DAY)
    private LocalDate perFechaNacimiento;

    @Size(max = 100)
    @Column(name = "per_email")
    private String perEmail;

    @JoinColumn(name = "per_etnia_fk", referencedColumnName = "etn_pk")
    @ManyToOne
    private SgEtnia perEtnia;

    @JoinColumn(name = "per_estado_civil_fk", referencedColumnName = "eci_pk")
    @ManyToOne
    private SgEstadoCivil perEstadoCivil;

    @JoinColumn(name = "per_sexo_fk", referencedColumnName = "sex_pk")
    @ManyToOne
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private SgSexo perSexo;

    @JoinColumn(name = "per_tipo_sangre_fk", referencedColumnName = "tsa_pk")
    @ManyToOne
    private SgTipoSangre perTipoSangre;
    
    @Column(name = "per_cantidad_hijos")
    private Integer perCantidadHijos;

    @Column(name = "per_habilitado")
    @AtributoHabilitado
    private Boolean perHabilitado;

    @Column(name = "per_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime perUltModFecha;

    @Size(max = 45)
    @Column(name = "per_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String perUltModUsuario;

    @Column(name = "per_version")
    @Version
    private Integer perVersion;

    @JoinColumn(name = "per_direccion_fk")
    @OneToOne(cascade = CascadeType.ALL)
    private SgDireccion perDireccion;

    @OneToOne(mappedBy = "estPersona")
    private SgEstudiante perEstudiante; //TODO: ver de borrar esta relacion o hacer lazy para que el merge no la levante en memoria. Para que sea lazy no puede ser mappedBy.

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "telPersona", orphanRemoval = true)
    private List<SgTelefono> perTelefonos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePersona", orphanRemoval = true)
    private List<SgIdentificacion> perIdentificaciones;

    @Column(name = "per_dui", length = 20)
    @Size(max = 20)
    @Fields({
        @Field(name = "perDui", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
        @Field(name = "perDuiSort", index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    })
    @SortableField(forField = "perDuiSort")
    private String perDui;

    @Column(name = "per_cun")
    @Field(name = "perCun", index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private Long perCun;

    @Column(name = "per_nie")
    @Fields({
        @Field(name = "perNie", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
        @Field(name = "perNieSort", index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    })
    @SortableField(forField = "perNieSort")
    private Long perNie;

    @Column(name = "per_nip", length = 50)
    @Size(max = 50)
    @Field(name = "perNip", index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private String perNip;

    @Column(name = "per_nit", length = 20)
    @Size(max = 20)
    @Field(name = "perNit", index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private String perNit;

    @Column(name = "per_inpep")
    private String perInpep;

    @Column(name = "per_isss")
    private String perIsss;

    @Column(name = "per_nup")
    private String perNup;

    @Column(name = "per_naturalizada")
    private Boolean perNaturalizada;

    @Column(name = "per_partida_nacimiento")
    private Long perPartidaNacimiento;

    @Column(name = "per_partida_nacimiento_anio")
    private Integer perPartidaNacimientoAnio;

    @Column(name = "per_partida_nacimiento_folio")
    private String perPartidaNacimientoFolio;

    @Column(name = "per_partida_nacimiento_libro")
    private String perPartidaNacimientoLibro;

    @Column(name = "per_partida_nacimiento_complemento")
    private String perPartidaNacimientoComplemento;

    @Column(name = "per_partida_nacimiento_presenta")
    private Boolean perPartidaNacimientoPresenta;

    @JoinColumn(name = "per_partida_departamento_fk")
    @ManyToOne
    private SgDepartamento perPartidaDepartamento;

    @JoinColumn(name = "per_partida_municipio_fk")
    @ManyToOne
    private SgMunicipio perPartidaMunicipio;

    @JoinColumn(name = "per_departamento_nacimiento_fk")
    @ManyToOne
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private SgDepartamento perDepartamentoNacimento;

    @JoinColumn(name = "per_municipio_nacimiento_fk")
    @ManyToOne
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private SgMunicipio perMunicipioNacimiento;

    @JoinColumn(name = "per_foto_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo perFoto;

    @JoinColumn(name = "per_partida_nacimiento_archivo")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo perPartidaNacimientoArchivo;

    @JoinColumn(name = "per_nacionalidad_fk", referencedColumnName = "nac_pk")
    @ManyToOne
    private SgNacionalidad perNacionalidad;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_personas_discapacidades",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "per_pk"),
            inverseJoinColumns = @JoinColumn(name = "dis_pk"))
    private List<SgDiscapacidad> perDiscapacidades;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_personas_trastornos_aprendizaje",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "per_pk"),
            inverseJoinColumns = @JoinColumn(name = "tra_pk"))
    private List<SgTrastornoAprendizaje> perTrastornosAprendizaje;

    @OneToMany(mappedBy = "allPersonaReferenciada")
    @NotAudited
    private List<SgAllegado> perAllegados;

    @ManyToOne
    @JoinColumn(name = "per_profesion_fk")
    private SgProfesion perProfesion;

    @JoinColumn(name = "per_ocupacion_fk")
    @ManyToOne
    private SgOcupacion perOcupacion;

    @ManyToOne
    @JoinColumn(name = "per_escolaridad_fk")
    private SgEscolaridad perEscolaridad;

    @Column(name = "per_lugar_trabajo", length = 255)
    private String perLugarTrabajo;

    @Column(name = "per_trabaja")
    private Boolean perTrabaja;

    @JoinColumn(name = "per_tipo_trabajo_fk", referencedColumnName = "ttr_pk")
    @ManyToOne
    private SgTipoTrabajo perTipoTrabajo;

    @Column(name = "per_embarazo")
    private Boolean perEmbarazo;

    @Column(name = "per_fecha_parto")
    private LocalDate perFechaParto;

    @Column(name = "per_jornada_laboral", length = 20)
    private String perJornadaLaboral;

    @Column(name = "per_salario")
    private Double perSalario;

    @Column(name = "per_propiedad_vivienda", length = 40)
    private String perPropiedadVivienda;

    @Column(name = "per_servicios_basicos", length = 40)
    private String perServiciosBasicos;

    @Column(name = "per_acceso_internet")
    private Boolean perAccesoInternet;

    @Column(name = "per_recibe_remesas")
    private Boolean perRecibeRemesas;

//    @JoinColumn(name = "per_tipo_servicio_sanitario_fk", referencedColumnName = "tss_pk")
//    @ManyToOne
//    private SgTipoServicioSanitario perTipoServicioSanitario; //Se movió a datos residenciales. Luego borrar columna

    @Column(name = "per_familiares_emigrados")
    private Integer perFamiliaresEmigrados;

    @Column(name = "per_vive_con_cantidad")
    private Integer perViveConCantidad;

    @Size(max = 1000)
    @Column(name = "per_nombre_partida_nacimiento")
    private String perNombrePartidaNacimiento;

    @Column(name = "per_tiene_identificacion")
    private Boolean perTieneIdentificacion;

    @Column(name = "per_lucene_index_updated")
    private Boolean perLuceneIndexUpdated;

    @Transient
    private SgAllegado perResponsable;

    @Column(name = "per_usuario_pk", updatable = false, insertable = false) //Generado por trigger
    private Long perUsuarioPk;

    @Column(name = "per_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoPersona perEstado;

    @Column(name = "per_fecha_fallecimiento")
    private LocalDate perFechaFallecimiento;

    @JoinColumn(name = "per_motivo_fallecimiento_fk", referencedColumnName = "mfa_pk")
    @ManyToOne
    private SgMotivoFallecimiento perMotivoFallecimiento;

    @Column(name = "per_retornada")
    private Boolean perRetornada;

    @Column(name = "per_tiene_hijos")
    private Boolean perTieneHijos;

    @Column(name = "per_tiene_discapacidad")
    private Boolean perTieneDiscapacidades;

    @Column(name = "per_tiene_trastorno_aprendizaje")
    private Boolean perTieneTrastornoAprendizaje;

    @Column(name = "per_tiene_diagnostico")
    private Boolean perTieneDiagnostico;

    @Column(name = "per_dui_validado_rnpn")
    private Boolean perDuiValidadoRNPN;

    @Column(name = "per_dui_pendiente_validacion_rnpn")
    private Boolean perDuiPendienteValidacionRNPN;

    @Column(name = "per_cun_validado_rnpn")
    private Boolean perCunValidadoRNPN;

    @Column(name = "per_cun_pendiente_validacion_rnpn")
    private Boolean perCunPendienteValidacionRNPN;

    @Column(name = "per_partida_nac_validada_rnpn")
    private Boolean perPartidaNacValidadaRNPN;

    @Column(name = "per_partida_nac_pendiente_validacion_rnpn")
    private Boolean perPartidaNacPendienteValidacionRNPN;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_personas_terapias",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "per_pk"),
            inverseJoinColumns = @JoinColumn(name = "ter_pk"))
    private List<SgTerapia> perTerapias;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_personas_referencias_apoyo",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "per_pk"),
            inverseJoinColumns = @JoinColumn(name = "rea_pk"))
    private List<SgReferenciasApoyo> perReferenciasApoyo;
    
    @JoinColumn(name = "per_pk", insertable = false, updatable = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private SgDatosResidencialesPersona perDatosResidenciales;
    
    @Transient
    private Boolean perNoValidarDiagnostico;
    

    public SgPersona() {
        this.perDuiValidadoRNPN = Boolean.FALSE;
        this.perHabilitado = Boolean.TRUE;
        this.perDireccion = new SgDireccion();
        this.perPartidaNacimientoPresenta = Boolean.FALSE;
        this.perNaturalizada = Boolean.FALSE;
        this.perTrabaja = Boolean.FALSE;
        this.perEmbarazo = Boolean.FALSE;
        this.perTieneHijos = Boolean.FALSE;
        this.perTieneDiscapacidades = Boolean.FALSE;
        this.perTieneDiagnostico = Boolean.FALSE;
        this.perAccesoInternet = Boolean.FALSE;
        this.perRecibeRemesas = Boolean.FALSE;
        this.perTieneIdentificacion = Boolean.FALSE;
        this.perEstado = EnumEstadoPersona.VIVE;
        this.perRetornada = Boolean.FALSE;
        this.perIdentificaciones = new ArrayList<>();
    }

    public SgPersona(Long perPk) {
        this.perPk = perPk;
    }

    public SgPersona(Long perPk, Integer perVersion) {
        this.perPk = perPk;
        this.perVersion = perVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.perNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerNombreCompleto());
        this.perPrimerNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerPrimerNombre());
        this.perSegundoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerSegundoNombre());
        this.perTercerNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerTercerNombre());
        this.perPrimerApellidoBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerPrimerApellido());
        this.perSegundoApellidoBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerSegundoApellido());
        this.perTercerApellidoBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerTercerApellido());
        this.perLuceneIndexUpdated = Boolean.FALSE;
        if (this.perDuiValidadoRNPN == null) {
            this.perDuiValidadoRNPN = Boolean.FALSE;
        }
        if (BooleanUtils.isFalse(this.perTieneHijos)) {
            this.perCantidadHijos = 0;
        }
        if (BooleanUtils.isFalse(this.perTieneDiscapacidades)) {
            this.perDiscapacidades = null;
        }
        if (BooleanUtils.isFalse(this.perTieneTrastornoAprendizaje)) {
            this.perTrastornosAprendizaje = null;
        }
        this.perTieneIdentificacion = this.perCun != null
                || this.perNie != null
                || !StringUtils.isBlank(this.perDui)
                || !StringUtils.isBlank(this.perNit)
                || !StringUtils.isBlank(this.perNup)
                || !StringUtils.isBlank(this.perIsss)
                || !StringUtils.isBlank(this.perInpep)
                || !StringUtils.isBlank(this.perNip)
                || (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty());
    }

    @JsonIgnore
    public Boolean getPerEsMayorDeEdad() {
        if (this.perFechaNacimiento != null) {
            if (Period.between(perFechaNacimiento, LocalDate.now()).getYears() >= Constantes.EDAD_PERSONA_MAYOR) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @JsonIgnore
    public String getPerNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    @JsonIgnore
    public Boolean getPerIngresoIdentParaNipPersonalSede() {
        if (!StringUtils.isBlank(this.perDui)) {
            return true;
        }
        //Se valida que haya ingresado el pasaporte o el carné de residente
        if (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty()) {
            if (this.perIdentificaciones.stream().anyMatch((i) -> (i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_PASAPORTE) || i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_CARNE_RESIDENTE)))) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public Boolean getPerIngresoIdentParaNipPersonalSedeAlfabetizador() {
        if (this.perNie != null) {
            return true;
        }
        if (!StringUtils.isBlank(this.perDui)) {
            return true;
        }
        //Se valida que haya ingresado el pasaporte o el carné de residente
        if (this.perIdentificaciones != null && !this.perIdentificaciones.isEmpty()) {
            if (this.perIdentificaciones.stream().anyMatch((i) -> (i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_PASAPORTE) || i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_CARNE_RESIDENTE)))) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public String calcularNIP() {
        String codigo = null;
        if (!StringUtils.isBlank(this.getPerDui())) {
            codigo = StringUtils.leftPad(this.getPerDui(), 9, "0");
            codigo = "D" + codigo;
        } else {
            SgIdentificacion carne
                    = this.getPerIdentificaciones().stream().filter(
                            i -> i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_CARNE_RESIDENTE))
                            .findFirst().orElse(null);
            if (carne != null) {
                codigo = "C" + carne.getIdeNumeroDocumento();
            } else {
                SgIdentificacion pasaporte
                        = this.getPerIdentificaciones().stream().filter(
                                i -> i.getIdeTipoDocumento().getTinCodigo().equals(Constantes.CODIGO_PASAPORTE))
                                .findFirst().orElse(null);
                if (pasaporte != null) {
                    codigo = "P" + pasaporte.getIdePaisEmisor().getPaiCodigo() + pasaporte.getIdeNumeroDocumento();
                }
            }
        }
        return codigo;
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public LocalDate getPerFechaNacimiento() {
        return perFechaNacimiento;
    }

    public void setPerFechaNacimiento(LocalDate perFechaNacimiento) {
        this.perFechaNacimiento = perFechaNacimiento;
    }

    public String getPerEmail() {
        return perEmail;
    }

    public void setPerEmail(String perEmail) {
        this.perEmail = perEmail;
    }

    public SgEtnia getPerEtnia() {
        return perEtnia;
    }

    public void setPerEtnia(SgEtnia perEtnia) {
        this.perEtnia = perEtnia;
    }

    public SgEstadoCivil getPerEstadoCivil() {
        return perEstadoCivil;
    }

    public void setPerEstadoCivil(SgEstadoCivil perEstadoCivil) {
        this.perEstadoCivil = perEstadoCivil;
    }

    public SgSexo getPerSexo() {
        return perSexo;
    }

    public void setPerSexo(SgSexo perSexo) {
        this.perSexo = perSexo;
    }

    public SgTipoSangre getPerTipoSangre() {
        return perTipoSangre;
    }

    public void setPerTipoSangre(SgTipoSangre perTipoSangre) {
        this.perTipoSangre = perTipoSangre;
    }

    public Integer getPerCantidadHijos() {
        return perCantidadHijos;
    }

    public void setPerCantidadHijos(Integer perCantidadHijos) {
        this.perCantidadHijos = perCantidadHijos;
    }

    public Boolean getPerHabilitado() {
        return perHabilitado;
    }

    public void setPerHabilitado(Boolean perHabilitado) {
        this.perHabilitado = perHabilitado;
    }

    public LocalDateTime getPerUltModFecha() {
        return perUltModFecha;
    }

    public void setPerUltModFecha(LocalDateTime perUltModFecha) {
        this.perUltModFecha = perUltModFecha;
    }

    public String getPerUltModUsuario() {
        return perUltModUsuario;
    }

    public void setPerUltModUsuario(String perUltModUsuario) {
        this.perUltModUsuario = perUltModUsuario;
    }

    public Integer getPerVersion() {
        return perVersion;
    }

    public void setPerVersion(Integer perVersion) {
        this.perVersion = perVersion;
    }

    public SgDireccion getPerDireccion() {
        return perDireccion;
    }

    public void setPerDireccion(SgDireccion perDireccion) {
        this.perDireccion = perDireccion;
    }

    public String getPerPrimerNombreBusqueda() {
        return perPrimerNombreBusqueda;
    }

    public void setPerPrimerNombreBusqueda(String perPrimerNombreBusqueda) {
        this.perPrimerNombreBusqueda = perPrimerNombreBusqueda;
    }

    public String getPerSegundoNombreBusqueda() {
        return perSegundoNombreBusqueda;
    }

    public void setPerSegundoNombreBusqueda(String perSegundoNombreBusqueda) {
        this.perSegundoNombreBusqueda = perSegundoNombreBusqueda;
    }

    public String getPerTercerNombreBusqueda() {
        return perTercerNombreBusqueda;
    }

    public void setPerTercerNombreBusqueda(String perTercerNombreBusqueda) {
        this.perTercerNombreBusqueda = perTercerNombreBusqueda;
    }

    public String getPerPrimerApellidoBusqueda() {
        return perPrimerApellidoBusqueda;
    }

    public void setPerPrimerApellidoBusqueda(String perPrimerApellidoBusqueda) {
        this.perPrimerApellidoBusqueda = perPrimerApellidoBusqueda;
    }

    public String getPerSegundoApellidoBusqueda() {
        return perSegundoApellidoBusqueda;
    }

    public void setPerSegundoApellidoBusqueda(String perSegundoApellidoBusqueda) {
        this.perSegundoApellidoBusqueda = perSegundoApellidoBusqueda;
    }

    public String getPerTercerApellidoBusqueda() {
        return perTercerApellidoBusqueda;
    }

    public void setPerTercerApellidoBusqueda(String perTercerApellidoBusqueda) {
        this.perTercerApellidoBusqueda = perTercerApellidoBusqueda;
    }

    public String getPerNombreBusqueda() {
        return perNombreBusqueda;
    }

    public void setPerNombreBusqueda(String perNombreBusqueda) {
        this.perNombreBusqueda = perNombreBusqueda;
    }

    public SgEstudiante getPerEstudiante() {
        return perEstudiante;
    }

    public void setPerEstudiante(SgEstudiante perEstudiante) {
        this.perEstudiante = perEstudiante;
    }

    public List<SgTelefono> getPerTelefonos() {
        return perTelefonos;
    }

    public void setPerTelefonos(List<SgTelefono> perTelefonos) {
        this.perTelefonos = perTelefonos;
    }

    public List<SgIdentificacion> getPerIdentificaciones() {
        return perIdentificaciones;
    }

    public void setPerIdentificaciones(List<SgIdentificacion> perIdentificaciones) {
        this.perIdentificaciones = perIdentificaciones;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public Long getPerCun() {
        return perCun;
    }

    public void setPerCun(Long perCun) {
        this.perCun = perCun;
    }

    public Long getPerNie() {
        return perNie;
    }

    public void setPerNie(Long perNie) {
        this.perNie = perNie;
    }

    public String getPerNip() {
        return perNip;
    }

    public void setPerNip(String perNip) {
        this.perNip = perNip;
    }

    public Long getPerPartidaNacimiento() {
        return perPartidaNacimiento;
    }

    public void setPerPartidaNacimiento(Long perPartidaNacimiento) {
        this.perPartidaNacimiento = perPartidaNacimiento;
    }

    public String getPerPartidaNacimientoFolio() {
        return perPartidaNacimientoFolio;
    }

    public void setPerPartidaNacimientoFolio(String perPartidaNacimientoFolio) {
        this.perPartidaNacimientoFolio = perPartidaNacimientoFolio;
    }

    public String getPerPartidaNacimientoLibro() {
        return perPartidaNacimientoLibro;
    }

    public void setPerPartidaNacimientoLibro(String perPartidaNacimientoLibro) {
        this.perPartidaNacimientoLibro = perPartidaNacimientoLibro;
    }

    public SgDepartamento getPerDepartamentoNacimento() {
        return perDepartamentoNacimento;
    }

    public void setPerDepartamentoNacimento(SgDepartamento perDepartamentoNacimento) {
        this.perDepartamentoNacimento = perDepartamentoNacimento;
    }

    public SgMunicipio getPerMunicipioNacimiento() {
        return perMunicipioNacimiento;
    }

    public void setPerMunicipioNacimiento(SgMunicipio perMunicipioNacimiento) {
        this.perMunicipioNacimiento = perMunicipioNacimiento;
    }

    public Boolean getPerPartidaNacimientoPresenta() {
        return perPartidaNacimientoPresenta;
    }

    public void setPerPartidaNacimientoPresenta(Boolean perPartidaNacimientoPresenta) {
        this.perPartidaNacimientoPresenta = perPartidaNacimientoPresenta;
    }

    public SgArchivo getPerFoto() {
        return perFoto;
    }

    public void setPerFoto(SgArchivo perFoto) {
        this.perFoto = perFoto;
    }

    public SgNacionalidad getPerNacionalidad() {
        return perNacionalidad;
    }

    public void setPerNacionalidad(SgNacionalidad perNacionalidad) {
        this.perNacionalidad = perNacionalidad;
    }

    public List<SgDiscapacidad> getPerDiscapacidades() {
        return perDiscapacidades;
    }

    public void setPerDiscapacidades(List<SgDiscapacidad> perDiscapacidades) {
        this.perDiscapacidades = perDiscapacidades;
    }

    public List<SgAllegado> getPerAllegados() {
        return perAllegados;
    }

    public void setPerAllegados(List<SgAllegado> perAllegados) {
        this.perAllegados = perAllegados;
    }

    public SgProfesion getPerProfesion() {
        return perProfesion;
    }

    public void setPerProfesion(SgProfesion perProfesion) {
        this.perProfesion = perProfesion;
    }

    public SgEscolaridad getPerEscolaridad() {
        return perEscolaridad;
    }

    public void setPerEscolaridad(SgEscolaridad perEscolaridad) {
        this.perEscolaridad = perEscolaridad;
    }

    public String getPerNit() {
        return perNit;
    }

    public void setPerNit(String perNit) {
        this.perNit = perNit;
    }

    public Boolean getPerNaturalizada() {
        return perNaturalizada;
    }

    public void setPerNaturalizada(Boolean perNaturalizada) {
        this.perNaturalizada = perNaturalizada;
    }

    public String getPerInpep() {
        return perInpep;
    }

    public void setPerInpep(String perInpep) {
        this.perInpep = perInpep;
    }

    public String getPerIsss() {
        return perIsss;
    }

    public void setPerIsss(String perIsss) {
        this.perIsss = perIsss;
    }

    public String getPerNup() {
        return perNup;
    }

    public void setPerNup(String perNup) {
        this.perNup = perNup;
    }

    public SgOcupacion getPerOcupacion() {
        return perOcupacion;
    }

    public void setPerOcupacion(SgOcupacion perOcupacion) {
        this.perOcupacion = perOcupacion;
    }

    public Boolean getPerEmbarazo() {
        return perEmbarazo;
    }

    public void setPerEmbarazo(Boolean perEmbarazo) {
        this.perEmbarazo = perEmbarazo;
    }

    public LocalDate getPerFechaParto() {
        return perFechaParto;
    }

    public void setPerFechaParto(LocalDate perFechaParto) {
        this.perFechaParto = perFechaParto;
    }

    public String getPerJornadaLaboral() {
        return perJornadaLaboral;
    }

    public void setPerJornadaLaboral(String perJornadaLaboral) {
        this.perJornadaLaboral = perJornadaLaboral;
    }

    public Double getPerSalario() {
        return perSalario;
    }

    public void setPerSalario(Double perSalario) {
        this.perSalario = perSalario;
    }

    public String getPerPropiedadVivienda() {
        return perPropiedadVivienda;
    }

    public void setPerPropiedadVivienda(String perPropiedadVivienda) {
        this.perPropiedadVivienda = perPropiedadVivienda;
    }

    public String getPerServiciosBasicos() {
        return perServiciosBasicos;
    }

    public void setPerServiciosBasicos(String perServiciosBasicos) {
        this.perServiciosBasicos = perServiciosBasicos;
    }

    public Boolean getPerAccesoInternet() {
        return perAccesoInternet;
    }

    public void setPerAccesoInternet(Boolean perAccesoInternet) {
        this.perAccesoInternet = perAccesoInternet;
    }

    public Boolean getPerRecibeRemesas() {
        return perRecibeRemesas;
    }

    public void setPerRecibeRemesas(Boolean perRecibeRemesas) {
        this.perRecibeRemesas = perRecibeRemesas;
    }

    public String getPerLugarTrabajo() {
        return perLugarTrabajo;
    }

    public void setPerLugarTrabajo(String perLugarTrabajo) {
        this.perLugarTrabajo = perLugarTrabajo;
    }

    public Boolean getPerTrabaja() {
        return perTrabaja;
    }

    public void setPerTrabaja(Boolean perTrabaja) {
        this.perTrabaja = perTrabaja;
    }

    public SgTipoTrabajo getPerTipoTrabajo() {
        return perTipoTrabajo;
    }

    public void setPerTipoTrabajo(SgTipoTrabajo perTipoTrabajo) {
        this.perTipoTrabajo = perTipoTrabajo;
    }

    public Integer getPerFamiliaresEmigrados() {
        return perFamiliaresEmigrados;
    }

    public void setPerFamiliaresEmigrados(Integer perFamiliaresEmigrados) {
        this.perFamiliaresEmigrados = perFamiliaresEmigrados;
    }

    public String getPerNombrePartidaNacimiento() {
        return perNombrePartidaNacimiento;
    }

    public void setPerNombrePartidaNacimiento(String perNombrePartidaNacimiento) {
        this.perNombrePartidaNacimiento = perNombrePartidaNacimiento;
    }

    public SgAllegado getPerResponsable() {
        return perResponsable;
    }

    public void setPerResponsable(SgAllegado perResponsable) {
        this.perResponsable = perResponsable;
    }

    public String getPerPartidaNacimientoComplemento() {
        return perPartidaNacimientoComplemento;
    }

    public void setPerPartidaNacimientoComplemento(String perPartidaNacimientoComplemento) {
        this.perPartidaNacimientoComplemento = perPartidaNacimientoComplemento;
    }

    public Integer getPerPartidaNacimientoAnio() {
        return perPartidaNacimientoAnio;
    }

    public void setPerPartidaNacimientoAnio(Integer perPartidaNacimientoAnio) {
        this.perPartidaNacimientoAnio = perPartidaNacimientoAnio;
    }

    public SgArchivo getPerPartidaNacimientoArchivo() {
        return perPartidaNacimientoArchivo;
    }

    public void setPerPartidaNacimientoArchivo(SgArchivo perPartidaNacimientoArchivo) {
        this.perPartidaNacimientoArchivo = perPartidaNacimientoArchivo;
    }

    public SgDepartamento getPerPartidaDepartamento() {
        return perPartidaDepartamento;
    }

    public void setPerPartidaDepartamento(SgDepartamento perPartidaDepartamento) {
        this.perPartidaDepartamento = perPartidaDepartamento;
    }

    public SgMunicipio getPerPartidaMunicipio() {
        return perPartidaMunicipio;
    }

    public void setPerPartidaMunicipio(SgMunicipio perPartidaMunicipio) {
        this.perPartidaMunicipio = perPartidaMunicipio;
    }

    public Boolean getPerLuceneIndexUpdated() {
        return perLuceneIndexUpdated;
    }

    public void setPerLuceneIndexUpdated(Boolean perLuceneIndexUpdated) {
        this.perLuceneIndexUpdated = perLuceneIndexUpdated;
    }

    public Boolean getPerTieneIdentificacion() {
        return perTieneIdentificacion;
    }

    public void setPerTieneIdentificacion(Boolean perTieneIdentificacion) {
        this.perTieneIdentificacion = perTieneIdentificacion;
    }

    public Long getPerUsuarioPk() {
        return perUsuarioPk;
    }

    public void setPerUsuarioPk(Long perUsuarioPk) {
        this.perUsuarioPk = perUsuarioPk;
    }

    public EnumEstadoPersona getPerEstado() {
        return perEstado;
    }

    public void setPerEstado(EnumEstadoPersona perEstado) {
        this.perEstado = perEstado;
    }

    public LocalDate getPerFechaFallecimiento() {
        return perFechaFallecimiento;
    }

    public void setPerFechaFallecimiento(LocalDate perFechaFallecimiento) {
        this.perFechaFallecimiento = perFechaFallecimiento;
    }

    public SgMotivoFallecimiento getPerMotivoFallecimiento() {
        return perMotivoFallecimiento;
    }

    public void setPerMotivoFallecimiento(SgMotivoFallecimiento perMotivoFallecimiento) {
        this.perMotivoFallecimiento = perMotivoFallecimiento;
    }

    public Boolean getPerRetornada() {
        return perRetornada;
    }

    public void setPerRetornada(Boolean perRetornada) {
        this.perRetornada = perRetornada;
    }

    public List<SgTrastornoAprendizaje> getPerTrastornosAprendizaje() {
        return perTrastornosAprendizaje;
    }

    public void setPerTrastornosAprendizaje(List<SgTrastornoAprendizaje> perTrastornosAprendizaje) {
        this.perTrastornosAprendizaje = perTrastornosAprendizaje;
    }

    public Integer getPerViveConCantidad() {
        return perViveConCantidad;
    }

    public void setPerViveConCantidad(Integer perViveConCantidad) {
        this.perViveConCantidad = perViveConCantidad;
    }

    public Boolean getPerTieneHijos() {
        return perTieneHijos;
    }

    public void setPerTieneHijos(Boolean perTieneHijos) {
        this.perTieneHijos = perTieneHijos;
    }

    public Boolean getPerTieneDiscapacidades() {
        return perTieneDiscapacidades;
    }

    public void setPerTieneDiscapacidades(Boolean perTieneDiscapacidades) {
        this.perTieneDiscapacidades = perTieneDiscapacidades;
    }

    public Boolean getPerTieneDiagnostico() {
        return perTieneDiagnostico;
    }

    public void setPerTieneDiagnostico(Boolean perTieneDiagnostico) {
        this.perTieneDiagnostico = perTieneDiagnostico;
    }

    public List<SgTerapia> getPerTerapias() {
        return perTerapias;
    }

    public void setPerTerapias(List<SgTerapia> perTerapias) {
        this.perTerapias = perTerapias;
    }

    public List<SgReferenciasApoyo> getPerReferenciasApoyo() {
        return perReferenciasApoyo;
    }

    public void setPerReferenciasApoyo(List<SgReferenciasApoyo> perReferenciasApoyo) {
        this.perReferenciasApoyo = perReferenciasApoyo;
    }

    public Boolean getPerDuiValidadoRNPN() {
        return perDuiValidadoRNPN;
    }

    public void setPerDuiValidadoRNPN(Boolean perDuiValidadoRNPN) {
        this.perDuiValidadoRNPN = perDuiValidadoRNPN;
    }

    public Boolean getPerDuiPendienteValidacionRNPN() {
        return perDuiPendienteValidacionRNPN;
    }

    public void setPerDuiPendienteValidacionRNPN(Boolean perDuiPendienteValidacionRNPN) {
        this.perDuiPendienteValidacionRNPN = perDuiPendienteValidacionRNPN;
    }

    public Boolean getPerCunValidadoRNPN() {
        return perCunValidadoRNPN;
    }

    public void setPerCunValidadoRNPN(Boolean perCunValidadoRNPN) {
        this.perCunValidadoRNPN = perCunValidadoRNPN;
    }

    public Boolean getPerCunPendienteValidacionRNPN() {
        return perCunPendienteValidacionRNPN;
    }

    public void setPerCunPendienteValidacionRNPN(Boolean perCunPendienteValidacionRNPN) {
        this.perCunPendienteValidacionRNPN = perCunPendienteValidacionRNPN;
    }

    public Boolean getPerPartidaNacValidadaRNPN() {
        return perPartidaNacValidadaRNPN;
    }

    public void setPerPartidaNacValidadaRNPN(Boolean perPartidaNacValidadaRNPN) {
        this.perPartidaNacValidadaRNPN = perPartidaNacValidadaRNPN;
    }

    public Boolean getPerPartidaNacPendienteValidacionRNPN() {
        return perPartidaNacPendienteValidacionRNPN;
    }

    public void setPerPartidaNacPendienteValidacionRNPN(Boolean perPartidaNacPendienteValidacionRNPN) {
        this.perPartidaNacPendienteValidacionRNPN = perPartidaNacPendienteValidacionRNPN;
    }

    public Boolean getPerTieneTrastornoAprendizaje() {
        return perTieneTrastornoAprendizaje;
    }

    public void setPerTieneTrastornoAprendizaje(Boolean perTieneTrastornoAprendizaje) {
        this.perTieneTrastornoAprendizaje = perTieneTrastornoAprendizaje;
    }

    public SgDatosResidencialesPersona getPerDatosResidenciales() {
        return perDatosResidenciales;
    }

    public void setPerDatosResidenciales(SgDatosResidencialesPersona perDatosResidenciales) {
        this.perDatosResidenciales = perDatosResidenciales;
    }

    public Boolean getPerNoValidarDiagnostico() {
        return perNoValidarDiagnostico;
    }

    public void setPerNoValidarDiagnostico(Boolean perNoValidarDiagnostico) {
        this.perNoValidarDiagnostico = perNoValidarDiagnostico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perPk != null ? perPk.hashCode() : 0);
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
        final SgPersona other = (SgPersona) obj;
        if (!Objects.equals(this.perPk, other.perPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPersona{" + "perPk=" + perPk + ", perPrimerNombre=" + perPrimerNombre + ", perSegundoNombre=" + perSegundoNombre + ", perTercerNombre=" + perTercerNombre + ", perPrimerApellido=" + perPrimerApellido + ", perSegundoApellido=" + perSegundoApellido + ", perTercerApellido=" + perTercerApellido + ", perPrimerNombreBusqueda=" + perPrimerNombreBusqueda + ", perSegundoNombreBusqueda=" + perSegundoNombreBusqueda + ", perTercerNombreBusqueda=" + perTercerNombreBusqueda + ", perPrimerApellidoBusqueda=" + perPrimerApellidoBusqueda + ", perSegundoApellidoBusqueda=" + perSegundoApellidoBusqueda + ", perTercerApellidoBusqueda=" + perTercerApellidoBusqueda + ", perNombreBusqueda=" + perNombreBusqueda + ", perFechaNacimiento=" + perFechaNacimiento + ", perEmail=" + perEmail + ", perEtnia=" + perEtnia + ", perEstadoCivil=" + perEstadoCivil + ", perSexo=" + perSexo + ", perTipoSangre=" + perTipoSangre + ", perCantidadHijos=" + perCantidadHijos + ", perHabilitado=" + perHabilitado + ", perUltModFecha=" + perUltModFecha + ", perUltModUsuario=" + perUltModUsuario + ", perVersion=" + perVersion + ", perDireccion=" + perDireccion + ", perTelefonos=" + perTelefonos + ", perIdentificaciones=" + perIdentificaciones + ", perDui=" + perDui + ", perCun=" + perCun + ", perNie=" + perNie + ", perNip=" + perNip + ", perNit=" + perNit + ", perInpep=" + perInpep + ", perIsss=" + perIsss + ", perNup=" + perNup + ", perNaturalizada=" + perNaturalizada + ", perPartidaNacimiento=" + perPartidaNacimiento + ", perPartidaNacimientoAnio=" + perPartidaNacimientoAnio + ", perPartidaNacimientoFolio=" + perPartidaNacimientoFolio + ", perPartidaNacimientoLibro=" + perPartidaNacimientoLibro + ", perPartidaNacimientoComplemento=" + perPartidaNacimientoComplemento + ", perPartidaNacimientoPresenta=" + perPartidaNacimientoPresenta + ", perPartidaDepartamento=" + perPartidaDepartamento + ", perPartidaMunicipio=" + perPartidaMunicipio + ", perDepartamentoNacimento=" + perDepartamentoNacimento + ", perMunicipioNacimiento=" + perMunicipioNacimiento + ", perFoto=" + perFoto + ", perPartidaNacimientoArchivo=" + perPartidaNacimientoArchivo + ", perNacionalidad=" + perNacionalidad + ", perDiscapacidades=" + perDiscapacidades + ", perAllegados=" + perAllegados + ", perProfesion=" + perProfesion + ", perOcupacion=" + perOcupacion + ", perEscolaridad=" + perEscolaridad + ", perLugarTrabajo=" + perLugarTrabajo + ", perTrabaja=" + perTrabaja + ", perTipoTrabajo=" + perTipoTrabajo + ", perEmbarazo=" + perEmbarazo + ", perFechaParto=" + perFechaParto + ", perJornadaLaboral=" + perJornadaLaboral + ", perSalario=" + perSalario + ", perPropiedadVivienda=" + perPropiedadVivienda + ", perServiciosBasicos=" + perServiciosBasicos + ", perAccesoInternet=" + perAccesoInternet + ", perRecibeRemesas=" + perRecibeRemesas + ", perFamiliaresEmigrados=" + perFamiliaresEmigrados + ", perNombrePartidaNacimiento=" + perNombrePartidaNacimiento + ", perResponsable=" + perResponsable + '}';
    }

}
