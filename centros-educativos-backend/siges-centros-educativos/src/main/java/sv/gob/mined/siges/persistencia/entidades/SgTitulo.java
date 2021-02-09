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
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoReimpresion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_titulo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "titPk", scope = SgTitulo.class)
@Audited
public class SgTitulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tit_pk", nullable = false)
    private Long titPk;
    
    @JoinColumn(name = "tit_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante titEstudianteFk;

    @Column(name = "tit_nombre_estudiante")
    private String titNombreEstudiante;
    
    @Column(name = "tit_nombre_estudiante_busqueda")
    private String titNombreEstudianteBusqueda;
    
    @Column(name = "tit_nombre_estudiante_partida")
    private String titNombreEstudiantePartida;
    
    @Column(name = "tit_nombre_certificado")
    private String titNombreCertificado;
    
    @Column(name = "tit_fecha_validez")
    private LocalDate titFechaValidez;
    
    @Column(name = "tit_fecha_emision")
    private LocalDate titFechaEmision;
    
    @JoinColumn(name = "tit_sello_firma_director_fk", referencedColumnName = "sfi_pk")
    @ManyToOne
    private SgSelloFirma titSelloFirmaDirectorFk;
    
    @JoinColumn(name = "tit_sello_firma_titular_ministro_fk", referencedColumnName = "sft_pk")
    @ManyToOne
    private SgSelloFirmaTitular titSelloFirmaTitularMinistroFk;
    
    @JoinColumn(name = "tit_sello_firma_titular_fk", referencedColumnName = "sft_pk")
    @ManyToOne
    private SgSelloFirmaTitular titSelloFirmaTitularAutenticaFk;
   
    @Column(name = "tit_nombre_director")
    private String titNombreDirector;
    
    @Column(name = "tit_nombre_ministro")
    private String titNombreMinistro;
    
    @Column(name = "tit_nombre_titular")
    private String titNombreTitular;
    
    @Column(name = "tit_anio")
    private Integer titAnio;
    
    @JoinColumn(name = "tit_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede titSedeFk;
    
    @Column(name = "tit_sede_codigo")
    private String titSedeCodigo;
    
    @Column(name = "tit_sede_nombre")
    private String titSedeNombre;
    
    @JoinColumn(name = "tit_servicio_educativo_fk", referencedColumnName = "sdu_pk")
    @ManyToOne
    private SgServicioEducativo titServicioEducativoFk;
    
    @JoinColumn(name = "tit_solicitud_impresion_fk", referencedColumnName = "sim_pk")
    @ManyToOne
    private SgSolicitudImpresion titSolicitudImpresionFk;
    
    @Column(name = "tit_usuario_envia_imprimir")
    private String titUsuarioEnviaImprimir;
    
    @Column(name = "tit_hash")
    private String titHash;

    @Column(name = "tit_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime titUltModFecha;

    @Size(max = 45)
    @Column(name = "tit_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String titUltModUsuario;

    @Column(name = "tit_version")
    @Version
    private Integer titVersion;
    
    @Column(name = "tit_anulado")
    private Boolean titAnulada;
    
    @JoinColumn(name = "tit_motivo_reimpresion_fk")
    @ManyToOne
    private SgMotivoReimpresion titMotivoReimpresion; 
    
    @Column(name = "tit_dui_estudiante")
    private String titDuiEstudiante;
    
    @Column(name = "tit_fecha_legalizacion_titulo")
    private LocalDate titFechaLegalizacionTitulo;
    
    @Column(name = "tit_titulo_anterior_2008")
    private String titTituloAnterior2008;
    
        //En caso de ser true se completa el catalogo retTituloAnterior2008, sino se completa retNombreTituloPosterior2008
    @Column(name = "tit_es_anterior_2008")
    private Boolean titEsAnterior2008; 
    
    @Column(name = "tit_nombre_titulo_posterior_2008")
    private String titNombreTituloPosterior2008;  
    
    @Column(name = "tit_numero_resolucion")
    private String titNumeroResolucion;
    
    @Column(name = "tit_numero_registro")
    private String titNumeroRegistro;
    
    @Column(name = "tit_reposicion")
    private Boolean titReposicion;
    
    @JoinColumn(name = "tit_sec_fk", referencedColumnName = "sec_pk")
    @ManyToOne
    private SgSeccion titSeccionFk;

    public SgTitulo() {
        this.titAnulada=Boolean.FALSE;
    }
    
    @PostPersist
    @PostUpdate
    public void generadorHash(){       
        titHash = calcularHash();
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.titNombreEstudianteBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getTitNombreEstudiante());
    }
    
    private String calcularHash(){
        String codigo=this.titPk+""+(this.titEstudianteFk!=null?this.titEstudianteFk.getEstPk():null)+""+titNombreEstudiante+""+titNombreEstudiantePartida+""+titNombreCertificado+""+titFechaValidez+""+titFechaEmision+""+(titSelloFirmaDirectorFk!=null?titSelloFirmaDirectorFk.getSfiPk():null)+""+(titSelloFirmaTitularMinistroFk!=null?titSelloFirmaTitularMinistroFk.getSftPk():null)+""+titNombreDirector+""+titNombreMinistro+""+titAnio+""+titSedeFk+""+""+titSedeCodigo+""+titSedeNombre+""+(titServicioEducativoFk!=null?titServicioEducativoFk.getSduPk():null)+""+(titSolicitudImpresionFk!=null?titSolicitudImpresionFk.getSimPk():null)+""+titUsuarioEnviaImprimir;
        return DigestUtils.sha256Hex(codigo);
    }

    public Long getTitPk() {
        return titPk;
    }

    public void setTitPk(Long titPk) {
        this.titPk = titPk;
    }
    
 
    public LocalDateTime getTitUltModFecha() {
        return titUltModFecha;
    }

    public void setTitUltModFecha(LocalDateTime titUltModFecha) {
        this.titUltModFecha = titUltModFecha;
    }

    public String getTitUltModUsuario() {
        return titUltModUsuario;
    }

    public void setTitUltModUsuario(String titUltModUsuario) {
        this.titUltModUsuario = titUltModUsuario;
    }

    public Integer getTitVersion() {
        return titVersion;
    }

    public void setTitVersion(Integer titVersion) {
        this.titVersion = titVersion;
    }

    public SgEstudiante getTitEstudianteFk() {
        return titEstudianteFk;
    }

    public void setTitEstudianteFk(SgEstudiante titEstudianteFk) {
        this.titEstudianteFk = titEstudianteFk;
    }

    public String getTitNombreEstudiante() {
        return titNombreEstudiante;
    }

    public void setTitNombreEstudiante(String titNombreEstudiante) {
        this.titNombreEstudiante = titNombreEstudiante;
    }

    public String getTitNombreEstudiantePartida() {
        return titNombreEstudiantePartida;
    }

    public void setTitNombreEstudiantePartida(String titNombreEstudiantePartida) {
        this.titNombreEstudiantePartida = titNombreEstudiantePartida;
    }

    public String getTitNombreCertificado() {
        return titNombreCertificado;
    }

    public void setTitNombreCertificado(String titNombreCertificado) {
        this.titNombreCertificado = titNombreCertificado;
    }

    public LocalDate getTitFechaValidez() {
        return titFechaValidez;
    }

    public void setTitFechaValidez(LocalDate titFechaValidez) {
        this.titFechaValidez = titFechaValidez;
    }

    public LocalDate getTitFechaEmision() {
        return titFechaEmision;
    }

    public void setTitFechaEmision(LocalDate titFechaEmision) {
        this.titFechaEmision = titFechaEmision;
    }

    public SgSelloFirma getTitSelloFirmaDirectorFk() {
        return titSelloFirmaDirectorFk;
    }

    public void setTitSelloFirmaDirectorFk(SgSelloFirma titSelloFirmaDirectorFk) {
        this.titSelloFirmaDirectorFk = titSelloFirmaDirectorFk;
    }

    public SgSelloFirmaTitular getTitSelloFirmaTitularMinistroFk() {
        return titSelloFirmaTitularMinistroFk;
    }

    public void setTitSelloFirmaTitularMinistroFk(SgSelloFirmaTitular titSelloFirmaTitularMinistroFk) {
        this.titSelloFirmaTitularMinistroFk = titSelloFirmaTitularMinistroFk;
    }

    public SgSelloFirmaTitular getTitSelloFirmaTitularAutenticaFk() {
        return titSelloFirmaTitularAutenticaFk;
    }

    public void setTitSelloFirmaTitularAutenticaFk(SgSelloFirmaTitular titSelloFirmaTitularAutenticaFk) {
        this.titSelloFirmaTitularAutenticaFk = titSelloFirmaTitularAutenticaFk;
    }    

    public String getTitNombreDirector() {
        return titNombreDirector;
    }

    public void setTitNombreDirector(String titNombreDirector) {
        this.titNombreDirector = titNombreDirector;
    }

    public String getTitNombreMinistro() {
        return titNombreMinistro;
    }

    public void setTitNombreMinistro(String titNombreMinistro) {
        this.titNombreMinistro = titNombreMinistro;
    }

    public Integer getTitAnio() {
        return titAnio;
    }

    public void setTitAnio(Integer titAnio) {
        this.titAnio = titAnio;
    }

    public SgSede getTitSedeFk() {
        return titSedeFk;
    }

    public void setTitSedeFk(SgSede titSedeFk) {
        this.titSedeFk = titSedeFk;
    }

    public String getTitSedeCodigo() {
        return titSedeCodigo;
    }

    public void setTitSedeCodigo(String titSedeCodigo) {
        this.titSedeCodigo = titSedeCodigo;
    }

    public String getTitSedeNombre() {
        return titSedeNombre;
    }

    public void setTitSedeNombre(String titSedeNombre) {
        this.titSedeNombre = titSedeNombre;
    }

    public SgServicioEducativo getTitServicioEducativoFk() {
        return titServicioEducativoFk;
    }

    public void setTitServicioEducativoFk(SgServicioEducativo titServicioEducativoFk) {
        this.titServicioEducativoFk = titServicioEducativoFk;
    }

    public SgSolicitudImpresion getTitSolicitudImpresionFk() {
        return titSolicitudImpresionFk;
    }

    public void setTitSolicitudImpresionFk(SgSolicitudImpresion titSolicitudImpresionFk) {
        this.titSolicitudImpresionFk = titSolicitudImpresionFk;
    }

    

    public String getTitUsuarioEnviaImprimir() {
        return titUsuarioEnviaImprimir;
    }

    public void setTitUsuarioEnviaImprimir(String titUsuarioEnviaImprimir) {
        this.titUsuarioEnviaImprimir = titUsuarioEnviaImprimir;
    }

    public String getTitHash() {
        return titHash;
    }

    public void setTitHash(String titHash) {
        this.titHash = titHash;
    }

    public Boolean getTitAnulada() {
        return titAnulada;
    }

    public void setTitAnulada(Boolean titAnulada) {
        this.titAnulada = titAnulada;
    }

    public SgMotivoReimpresion getTitMotivoReimpresion() {
        return titMotivoReimpresion;
    }

    public void setTitMotivoReimpresion(SgMotivoReimpresion titMotivoReimpresion) {
        this.titMotivoReimpresion = titMotivoReimpresion;
    }

    public String getTitNombreTitular() {
        return titNombreTitular;
    }

    public void setTitNombreTitular(String titNombreTitular) {
        this.titNombreTitular = titNombreTitular;
    }

    public String getTitNombreEstudianteBusqueda() {
        return titNombreEstudianteBusqueda;
    }

    public void setTitNombreEstudianteBusqueda(String titNombreEstudianteBusqueda) {
        this.titNombreEstudianteBusqueda = titNombreEstudianteBusqueda;
    }

    public String getTitDuiEstudiante() {
        return titDuiEstudiante;
    }

    public void setTitDuiEstudiante(String titDuiEstudiante) {
        this.titDuiEstudiante = titDuiEstudiante;
    }    

    public LocalDate getTitFechaLegalizacionTitulo() {
        return titFechaLegalizacionTitulo;
    }

    public void setTitFechaLegalizacionTitulo(LocalDate titFechaLegalizacionTitulo) {
        this.titFechaLegalizacionTitulo = titFechaLegalizacionTitulo;
    }

    public String getTitTituloAnterior2008() {
        return titTituloAnterior2008;
    }

    public void setTitTituloAnterior2008(String titTituloAnterior2008) {
        this.titTituloAnterior2008 = titTituloAnterior2008;
    }

    public String getTitNumeroResolucion() {
        return titNumeroResolucion;
    }

    public void setTitNumeroResolucion(String titNumeroResolucion) {
        this.titNumeroResolucion = titNumeroResolucion;
    }

    public String getTitNumeroRegistro() {
        return titNumeroRegistro;
    }

    public void setTitNumeroRegistro(String titNumeroRegistro) {
        this.titNumeroRegistro = titNumeroRegistro;
    }

    public Boolean getTitReposicion() {
        return titReposicion;
    }

    public void setTitReposicion(Boolean titReposicion) {
        this.titReposicion = titReposicion;
    }

    public SgSeccion getTitSeccionFk() {
        return titSeccionFk;
    }

    public void setTitSeccionFk(SgSeccion titSeccionFk) {
        this.titSeccionFk = titSeccionFk;
    }

    public Boolean getTitEsAnterior2008() {
        return titEsAnterior2008;
    }

    public void setTitEsAnterior2008(Boolean titEsAnterior2008) {
        this.titEsAnterior2008 = titEsAnterior2008;
    }

    public String getTitNombreTituloPosterior2008() {
        return titNombreTituloPosterior2008;
    }

    public void setTitNombreTituloPosterior2008(String titNombreTituloPosterior2008) {
        this.titNombreTituloPosterior2008 = titNombreTituloPosterior2008;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.titPk);
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
        final SgTitulo other = (SgTitulo) obj;
        if (!Objects.equals(this.titPk, other.titPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTitulo{" + "titPk=" + titPk + ", titUltModFecha=" + titUltModFecha + ", titUltModUsuario=" + titUltModUsuario + ", titVersion=" + titVersion + '}';
    }
    
    

}
