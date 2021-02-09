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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfMinisterioOtorgante;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfNaturalezaContrato;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_proceso_legalizacion", schema=Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plePk", scope = SgProcesoLegalizacion.class)
@Audited
public class SgProcesoLegalizacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ple_pk", nullable = false)
    private Long plePk;
    
    @Column(name = "ple_numero_escritura")
    private String pleNumeroEscritura;

    @Column(name = "ple_numero_presentacion")
    private Integer pleNumeroPresentacion;

    @Column(name = "ple_fecha_presentacion")
    private LocalDate pleFechaPresentacion;

    @Column(name = "ple_presentante")
    private String plePresentante;

    @Column(name = "ple_observaciones_presentacion")
    private String pleObservacionesPresentacion;
    
    @Column(name = "ple_codigo_anterior")
    private String pleCodigoAnterior;
    
    @Column(name = "ple_notario")
    private String pleNotario;
    
    @Column(name = "ple_otorgante")
    private String pleOtorgante;   
    
    
    @Column(name = "ple_plazo_anio")
    private Integer plePlazoAnio;
    
    @Column(name = "ple_libro_protocolo")
    private String pleLibroProtocolo;
    
    @Column(name = "ple_fecha_contrato")
    private LocalDate pleFechaContrato;
    
    @Column(name = "ple_numero_antecedente")
    private Integer pleNumeroAntecedente;
    
    @Column(name = "ple_libro")
    private String pleLibro;
    
    @Column(name = "ple_matricula")
    private String pleMatricula;
    
    @Column(name = "ple_numero_inscripcion")
    private Integer pleNumeroInscripcion;
    
    @Column(name = "ple_fecha_inscripcion")
    private LocalDate pleFechaInscripcion;
    
    @Column(name = "ple_asiento")
    private String pleAsiento;
    
    @Column(name = "ple_numero_matricula")
    private String pleNumeroMatricula;
    
    @Column(name = "ple_fecha_matricula")
    private LocalDate pleFechaMatricula;
    
    @Column(name = "ple_observaciones_inscripcion")
    private String pleObservacionesInscripciones;
    
    @Column(name = "ple_fecha_asignacion")
    private LocalDate pleFechaAsignacion;
    
    @Column(name = "ple_colaborador_asignado")
    private String pleColaboradorAsignado;
    
    @Column(name = "ple_numero_expediente")
    private Integer pleNumeroExpediente;
    
    @Column(name = "ple_observaciones_estado")
    private String pleObservacionesEstado;
    
    @Column(name = "ple_numero_acuerdo")
    private Integer pleNumeroAcuerdo;
    
    @Column(name = "ple_fecha_acuerdo")
    private LocalDate pleFechaAcuerdo;  
        

    @Column(name = "ple_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pleUltModFecha;

    @Size(max = 45)
    @Column(name = "ple_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pleUltModUsuario;

    @Column(name = "ple_version")
    @Version
    private Integer pleVersion;
    
    @JoinColumn(name = "ple_estado", referencedColumnName = "epl_pk")
    @ManyToOne
    private SgEstadoProcesoLegalizacion pleEstado;
    
    @JoinColumn(name = "ple_naturaleza_contrato", referencedColumnName = "nac_pk")
    @ManyToOne
    private SgInfNaturalezaContrato pleNaturalezaContrato;
    
    @JoinColumn(name = "ple_estado_proceso", referencedColumnName = "epl_pk")
    @ManyToOne
    private SgEstadoProcesoLegalizacion pleEstadoProceso;
    
    @JoinColumn(name = "ple_ministerio_otorgante", referencedColumnName = "mio_pk")
    @ManyToOne
    private SgInfMinisterioOtorgante pleMinisterioOtorgante; 
       
    @Column(name = "ple_asiento_antecedente")
    private String pleAsientoAntecedente;
    
    @Column(name = "ple_matricula_antecedente")
    private String pleMatriculaAntecedente;
    
    @Column(name = "ple_libro_antecedente")
    private String pleLibroAntecedente;
    
    @Column(name = "ple_numero_inscripcion_libro")
    private Integer pleNumeroInscripcionLibro;
    
    @Column(name = "ple_libro_inscripcion_libro")
    private String pleLibroInscripcionLibro;
    
    @Column(name = "ple_fecha_inscripcion_libro")
    private LocalDate pleFechaInscripcionLibro;
    
    @Column(name = "ple_matricula_inscripcion_matricula")
    private String pleMatriculaInscripcionMatricula;
    
    @Column(name = "ple_asiento_inscripcion_matricula")
    private String pleAsientoInscripcionMatricula;
    
    @Column(name = "ple_fecha_inscripcion_matricula")
    private LocalDate pleFechaInscripcionMatricula;
    
    @Column(name = "ple_observaciones")
    private String pleObservaciones;

    public SgProcesoLegalizacion() {
    }

    public Long getPlePk() {
        return plePk;
    }

    public void setPlePk(Long plePk) {
        this.plePk = plePk;
    }

    public Integer getPleNumeroPresentacion() {
        return pleNumeroPresentacion;
    }

    public void setPleNumeroPresentacion(Integer pleNumeroPresentacion) {
        this.pleNumeroPresentacion = pleNumeroPresentacion;
    }

    public LocalDate getPleFechaPresentacion() {
        return pleFechaPresentacion;
    }

    public void setPleFechaPresentacion(LocalDate pleFechaPresentacion) {
        this.pleFechaPresentacion = pleFechaPresentacion;
    }

    public String getPlePresentante() {
        return plePresentante;
    }

    public void setPlePresentante(String plePresentante) {
        this.plePresentante = plePresentante;
    }

    public String getPleObservacionesPresentacion() {
        return pleObservacionesPresentacion;
    }

    public void setPleObservacionesPresentacion(String pleObservacionesPresentacion) {
        this.pleObservacionesPresentacion = pleObservacionesPresentacion;
    }

    public String getPleCodigoAnterior() {
        return pleCodigoAnterior;
    }

    public void setPleCodigoAnterior(String pleCodigoAnterior) {
        this.pleCodigoAnterior = pleCodigoAnterior;
    }

    public String getPleNotario() {
        return pleNotario;
    }

    public void setPleNotario(String pleNotario) {
        this.pleNotario = pleNotario;
    }

    public String getPleOtorgante() {
        return pleOtorgante;
    }

    public void setPleOtorgante(String pleOtorgante) {
        this.pleOtorgante = pleOtorgante;
    }

    public String getPleNumeroEscritura() {
        return pleNumeroEscritura;
    }

    public void setPleNumeroEscritura(String pleNumeroEscritura) {
        this.pleNumeroEscritura = pleNumeroEscritura;
    }

    public Integer getPlePlazoAnio() {
        return plePlazoAnio;
    }

    public void setPlePlazoAnio(Integer plePlazoAnio) {
        this.plePlazoAnio = plePlazoAnio;
    }

    public String getPleLibroProtocolo() {
        return pleLibroProtocolo;
    }

    public void setPleLibroProtocolo(String pleLibroProtocolo) {
        this.pleLibroProtocolo = pleLibroProtocolo;
    }

    public LocalDate getPleFechaContrato() {
        return pleFechaContrato;
    }

    public void setPleFechaContrato(LocalDate pleFechaContrato) {
        this.pleFechaContrato = pleFechaContrato;
    }

    public Integer getPleNumeroAntecedente() {
        return pleNumeroAntecedente;
    }

    public void setPleNumeroAntecedente(Integer pleNumeroAntecedente) {
        this.pleNumeroAntecedente = pleNumeroAntecedente;
    }

    public String getPleLibro() {
        return pleLibro;
    }

    public void setPleLibro(String pleLibro) {
        this.pleLibro = pleLibro;
    }

    public String getPleMatricula() {
        return pleMatricula;
    }

    public void setPleMatricula(String pleMatricula) {
        this.pleMatricula = pleMatricula;
    }

    public Integer getPleNumeroInscripcion() {
        return pleNumeroInscripcion;
    }

    public void setPleNumeroInscripcion(Integer pleNumeroInscripcion) {
        this.pleNumeroInscripcion = pleNumeroInscripcion;
    }

    public LocalDate getPleFechaInscripcion() {
        return pleFechaInscripcion;
    }

    public void setPleFechaInscripcion(LocalDate pleFechaInscripcion) {
        this.pleFechaInscripcion = pleFechaInscripcion;
    }

    public String getPleAsiento() {
        return pleAsiento;
    }

    public void setPleAsiento(String pleAsiento) {
        this.pleAsiento = pleAsiento;
    }
    

    public String getPleNumeroMatricula() {
        return pleNumeroMatricula;
    }

    public void setPleNumeroMatricula(String pleNumeroMatricula) {
        this.pleNumeroMatricula = pleNumeroMatricula;
    }

    public LocalDate getPleFechaMatricula() {
        return pleFechaMatricula;
    }

    public void setPleFechaMatricula(LocalDate pleFechaMatricula) {
        this.pleFechaMatricula = pleFechaMatricula;
    }

    public String getPleObservacionesInscripciones() {
        return pleObservacionesInscripciones;
    }

    public void setPleObservacionesInscripciones(String pleObservacionesInscripciones) {
        this.pleObservacionesInscripciones = pleObservacionesInscripciones;
    }

    public LocalDate getPleFechaAsignacion() {
        return pleFechaAsignacion;
    }

    public void setPleFechaAsignacion(LocalDate pleFechaAsignacion) {
        this.pleFechaAsignacion = pleFechaAsignacion;
    }

    public String getPleColaboradorAsignado() {
        return pleColaboradorAsignado;
    }

    public void setPleColaboradorAsignado(String pleColaboradorAsignado) {
        this.pleColaboradorAsignado = pleColaboradorAsignado;
    }

    public Integer getPleNumeroExpediente() {
        return pleNumeroExpediente;
    }

    public void setPleNumeroExpediente(Integer pleNumeroExpediente) {
        this.pleNumeroExpediente = pleNumeroExpediente;
    }

    public String getPleObservacionesEstado() {
        return pleObservacionesEstado;
    }

    public void setPleObservacionesEstado(String pleObservacionesEstado) {
        this.pleObservacionesEstado = pleObservacionesEstado;
    }

    public Integer getPleNumeroAcuerdo() {
        return pleNumeroAcuerdo;
    }

    public void setPleNumeroAcuerdo(Integer pleNumeroAcuerdo) {
        this.pleNumeroAcuerdo = pleNumeroAcuerdo;
    }

    public LocalDate getPleFechaAcuerdo() {
        return pleFechaAcuerdo;
    }

    public void setPleFechaAcuerdo(LocalDate pleFechaAcuerdo) {
        this.pleFechaAcuerdo = pleFechaAcuerdo;
    }

    public LocalDateTime getPleUltModFecha() {
        return pleUltModFecha;
    }

    public void setPleUltModFecha(LocalDateTime pleUltModFecha) {
        this.pleUltModFecha = pleUltModFecha;
    }

    public String getPleUltModUsuario() {
        return pleUltModUsuario;
    }

    public void setPleUltModUsuario(String pleUltModUsuario) {
        this.pleUltModUsuario = pleUltModUsuario;
    }

    public Integer getPleVersion() {
        return pleVersion;
    }

    public void setPleVersion(Integer pleVersion) {
        this.pleVersion = pleVersion;
    }

    public SgEstadoProcesoLegalizacion getPleEstado() {
        return pleEstado;
    }

    public void setPleEstado(SgEstadoProcesoLegalizacion pleEstado) {
        this.pleEstado = pleEstado;
    }

    public SgInfNaturalezaContrato getPleNaturalezaContrato() {
        return pleNaturalezaContrato;
    }

    public void setPleNaturalezaContrato(SgInfNaturalezaContrato pleNaturalezaContrato) {
        this.pleNaturalezaContrato = pleNaturalezaContrato;
    }

    public SgEstadoProcesoLegalizacion getPleEstadoProceso() {
        return pleEstadoProceso;
    }

    public void setPleEstadoProceso(SgEstadoProcesoLegalizacion pleEstadoProceso) {
        this.pleEstadoProceso = pleEstadoProceso;
    }

    public SgInfMinisterioOtorgante getPleMinisterioOtorgante() {
        return pleMinisterioOtorgante;
    }

    public void setPleMinisterioOtorgante(SgInfMinisterioOtorgante pleMinisterioOtorgante) {
        this.pleMinisterioOtorgante = pleMinisterioOtorgante;
    }

    public String getPleAsientoAntecedente() {
        return pleAsientoAntecedente;
    }

    public void setPleAsientoAntecedente(String pleAsientoAntecedente) {
        this.pleAsientoAntecedente = pleAsientoAntecedente;
    }

    public String getPleMatriculaAntecedente() {
        return pleMatriculaAntecedente;
    }

    public void setPleMatriculaAntecedente(String pleMatriculaAntecedente) {
        this.pleMatriculaAntecedente = pleMatriculaAntecedente;
    }

    public String getPleLibroAntecedente() {
        return pleLibroAntecedente;
    }

    public void setPleLibroAntecedente(String pleLibroAntecedente) {
        this.pleLibroAntecedente = pleLibroAntecedente;
    }

    public Integer getPleNumeroInscripcionLibro() {
        return pleNumeroInscripcionLibro;
    }

    public void setPleNumeroInscripcionLibro(Integer pleNumeroInscripcionLibro) {
        this.pleNumeroInscripcionLibro = pleNumeroInscripcionLibro;
    }

    public String getPleLibroInscripcionLibro() {
        return pleLibroInscripcionLibro;
    }

    public void setPleLibroInscripcionLibro(String pleLibroInscripcionLibro) {
        this.pleLibroInscripcionLibro = pleLibroInscripcionLibro;
    }

    public LocalDate getPleFechaInscripcionLibro() {
        return pleFechaInscripcionLibro;
    }

    public void setPleFechaInscripcionLibro(LocalDate pleFechaInscripcionLibro) {
        this.pleFechaInscripcionLibro = pleFechaInscripcionLibro;
    }

    public String getPleMatriculaInscripcionMatricula() {
        return pleMatriculaInscripcionMatricula;
    }

    public void setPleMatriculaInscripcionMatricula(String pleMatriculaInscripcionMatricula) {
        this.pleMatriculaInscripcionMatricula = pleMatriculaInscripcionMatricula;
    }

    public String getPleAsientoInscripcionMatricula() {
        return pleAsientoInscripcionMatricula;
    }

    public void setPleAsientoInscripcionMatricula(String pleAsientoInscripcionMatricula) {
        this.pleAsientoInscripcionMatricula = pleAsientoInscripcionMatricula;
    }

    public LocalDate getPleFechaInscripcionMatricula() {
        return pleFechaInscripcionMatricula;
    }

    public void setPleFechaInscripcionMatricula(LocalDate pleFechaInscripcionMatricula) {
        this.pleFechaInscripcionMatricula = pleFechaInscripcionMatricula;
    }

    public String getPleObservaciones() {
        return pleObservaciones;
    }

    public void setPleObservaciones(String pleObservaciones) {
        this.pleObservaciones = pleObservaciones;
    }
  
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.plePk);
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
        final SgProcesoLegalizacion other = (SgProcesoLegalizacion) obj;
        if (!Objects.equals(this.plePk, other.plePk)) {
            return false;
        }
        return true;
    }

   

}
