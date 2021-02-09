/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDefinicionTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoReimpresion;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioLite;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_solicitudes_impresion",  schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "simPk", scope = SgSolicitudImpresion.class)
@Audited
public class SgSolicitudImpresion implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sim_pk", nullable = false)
    private Long simPk;

    @Column(name = "sim_fecha_solicitud")
    private LocalDate simFechaSolicitud;
    
    @Column(name = "sim_fecha_enviado_imprimir")
    private LocalDate simFechaEnviadoImprimir;
    
    @Column(name = "sim_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudImpresion simEstado;
    
    @Column(name = "sim_tipo_impresion")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoSolicitudImpresion simTipoImpresion;
    
    @JoinColumn(name = "sim_seccion_fk")
    @ManyToOne
    private SgSeccion simSeccion;
    
    @JoinColumn(name = "sim_usuario_fk")
    @ManyToOne
    private SgUsuarioLite simUsuario;
   
    @Column(name = "sim_inicio_numero_correlativo")
    private Integer simInicioNumeroCorrelativo;

    @Column(name = "sim_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime simUltModFecha;

    @Size(max = 45)
    @Column(name = "sim_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String simUltModUsuario;

    @Column(name = "sim_version")
    @Version
    private Integer simVersion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eimSolicitudImpresion")
    @NotAudited
    private List<SgEstudianteImpresion> simEstudianteImpresion;
    
    
    @JoinColumn(name = "sim_definicion_titulo_fk")
    @ManyToOne
    private SgDefinicionTitulo simDefinicionTitulo;
    
    @JoinColumn(name = "sim_sello_firma_fk")
    @ManyToOne
    private SgSelloFirma simSelloDirector;
    
    @JoinColumn(name = "sim_sello_firma_titular_ministro_fk")
    @ManyToOne
    private SgSelloFirmaTitular simSelloMinistro;
    
    @JoinColumn(name = "sim_sello_firma_titular_autentica_fk")
    @ManyToOne
    private SgSelloFirmaTitular simSelloAutentica;    

    @OneToMany(cascade ={ CascadeType.PERSIST,CascadeType.MERGE}, mappedBy = "titSolicitudImpresionFk")
    @NotAudited
    private List<SgTitulo> simTitulos;   

    @JoinColumn(name = "sim_motivo_reimpresion_fk")
    @ManyToOne
    private SgMotivoReimpresion simMotivoReimpresion; 
    
    @JoinColumn(name = "sim_reposicion_titulo_fk")
    @OneToOne(cascade ={ CascadeType.PERSIST,CascadeType.MERGE})
    private SgReposicionTitulo simReposicionTitulo;
    
    @Column(name = "sim_titulo_entregado_departamental")
    private Boolean simTituloEntregadoDepartamental;
    
    @Column(name = "sim_fecha_entregado_departamental")
    private LocalDate simFechaEntregadoDepartamental;
    
    @Column(name = "sim_titulo_entregado_centro_educativo")
    private Boolean simTituloEntregadoCentroEducativo;
    
    @Column(name = "sim_fecha_entregado_centro_educativo")
    private LocalDate simFechaEntregadoCentroEducativo;
    
    @Column(name = "sim_impresora")
    private String simImpresora;   
    
    @Column(name = "sim_fecha_validez")
    private LocalDate simFechaValidez;    
    
    @JoinColumn(name = "sim_resolucion_fk")
    @ManyToOne
    private SgArchivo simResolucionFk;       
        
    @Column(name = "sim_numero_registro")
    private String simNumeroRegistro;
     
    @Column(name = "sim_numero_resolucion")
    private String simNumeroResolucion;
    
    
    //True si la reposición es para un estudiante existente en siges
    @Column(name = "sim_reposicion_estudiante_siges")
    private Boolean simReposicionEstudianteSiges;  
    
    @Transient
    private Integer simCantidadEstudianteImpresion;
    
    
    //Este dato sirve para cuando se esta dando de alta una solicitud, en caso de ser una reposición de la pantalla de títulos
    //no se valide si tiene sede tiene sello firma
    @Transient
    private Boolean simEvitarValidacionSelloFirma;
   
    
    public SgSolicitudImpresion(Long simPk, Integer simVersion) {
        this.simPk = simPk;
        this.simVersion = simVersion;
        simTituloEntregadoCentroEducativo=Boolean.FALSE;
        simTituloEntregadoDepartamental=Boolean.FALSE;
    }
        
    public SgSolicitudImpresion() {
    }

   

    public Long getSimPk() {
        return simPk;
    }

    public void setSimPk(Long simPk) {
        this.simPk = simPk;
    }

    public Integer getSimCantidadEstudianteImpresion() {
        return simCantidadEstudianteImpresion;
    }

    public void setSimCantidadEstudianteImpresion(Integer simCantidadEstudianteImpresion) {
        this.simCantidadEstudianteImpresion = simCantidadEstudianteImpresion;
    }

    public LocalDate getSimFechaSolicitud() {
        return simFechaSolicitud;
    }

    public void setSimFechaSolicitud(LocalDate simFechaSolicitud) {
        this.simFechaSolicitud = simFechaSolicitud;
    }

    public EnumEstadoSolicitudImpresion getSimEstado() {
        return simEstado;
    }

    public void setSimEstado(EnumEstadoSolicitudImpresion simEstado) {
        this.simEstado = simEstado;
    }

    public SgSeccion getSimSeccion() {
        return simSeccion;
    }

    public void setSimSeccion(SgSeccion simSeccion) {
        this.simSeccion = simSeccion;
    }

    public SgUsuarioLite getSimUsuario() {
        return simUsuario;
    }

    public void setSimUsuario(SgUsuarioLite simUsuario) {
        this.simUsuario = simUsuario;
    }

    public LocalDateTime getSimUltModFecha() {
        return simUltModFecha;
    }

    public void setSimUltModFecha(LocalDateTime simUltModFecha) {
        this.simUltModFecha = simUltModFecha;
    }

    public String getSimUltModUsuario() {
        return simUltModUsuario;
    }

    public void setSimUltModUsuario(String simUltModUsuario) {
        this.simUltModUsuario = simUltModUsuario;
    }

    public Integer getSimVersion() {
        return simVersion;
    }

    public void setSimVersion(Integer simVersion) {
        this.simVersion = simVersion;
    }

    public List<SgEstudianteImpresion> getSimEstudianteImpresion() {
        return simEstudianteImpresion;
    }

    public void setSimEstudianteImpresion(List<SgEstudianteImpresion> simEstudianteImpresion) {
        this.simEstudianteImpresion = simEstudianteImpresion;
    }

    public Integer getSimInicioNumeroCorrelativo() {
        return simInicioNumeroCorrelativo;
    }

    public void setSimInicioNumeroCorrelativo(Integer simInicioNumeroCorrelativo) {
        this.simInicioNumeroCorrelativo = simInicioNumeroCorrelativo;
    }

    public LocalDate getSimFechaEnviadoImprimir() {
        return simFechaEnviadoImprimir;
    }

    public void setSimFechaEnviadoImprimir(LocalDate simFechaEnviadoImprimir) {
        this.simFechaEnviadoImprimir = simFechaEnviadoImprimir;
    }

    public SgDefinicionTitulo getSimDefinicionTitulo() {
        return simDefinicionTitulo;
    }

    public void setSimDefinicionTitulo(SgDefinicionTitulo simDefinicionTitulo) {
        this.simDefinicionTitulo = simDefinicionTitulo;
    }

    public SgSelloFirma getSimSelloDirector() {
        return simSelloDirector;
    }

    public void setSimSelloDirector(SgSelloFirma simSelloDirector) {
        this.simSelloDirector = simSelloDirector;
    }

    public SgSelloFirmaTitular getSimSelloMinistro() {
        return simSelloMinistro;
    }

    public void setSimSelloMinistro(SgSelloFirmaTitular simSelloMinistro) {
        this.simSelloMinistro = simSelloMinistro;
    }

    public SgSelloFirmaTitular getSimSelloAutentica() {
        return simSelloAutentica;
    }

    public void setSimSelloAutentica(SgSelloFirmaTitular simSelloAutentica) {
        this.simSelloAutentica = simSelloAutentica;
    }

    public EnumTipoSolicitudImpresion getSimTipoImpresion() {
        return simTipoImpresion;
    }

    public void setSimTipoImpresion(EnumTipoSolicitudImpresion simTipoImpresion) {
        this.simTipoImpresion = simTipoImpresion;
    }

    public List<SgTitulo> getSimTitulos() {
        return simTitulos;
    }

    public void setSimTitulos(List<SgTitulo> simTitulos) {
        this.simTitulos = simTitulos;
    }

    public SgMotivoReimpresion getSimMotivoReimpresion() {
        return simMotivoReimpresion;
    }

    public void setSimMotivoReimpresion(SgMotivoReimpresion simMotivoReimpresion) {
        this.simMotivoReimpresion = simMotivoReimpresion;
    }

    public SgReposicionTitulo getSimReposicionTitulo() {
        return simReposicionTitulo;
    }

    public void setSimReposicionTitulo(SgReposicionTitulo simReposicionTitulo) {
        this.simReposicionTitulo = simReposicionTitulo;
    }

    public Boolean getSimTituloEntregadoDepartamental() {
        return simTituloEntregadoDepartamental;
    }

    public void setSimTituloEntregadoDepartamental(Boolean simTituloEntregadoDepartamental) {
        this.simTituloEntregadoDepartamental = simTituloEntregadoDepartamental;
    }

    public LocalDate getSimFechaEntregadoDepartamental() {
        return simFechaEntregadoDepartamental;
    }

    public void setSimFechaEntregadoDepartamental(LocalDate simFechaEntregadoDepartamental) {
        this.simFechaEntregadoDepartamental = simFechaEntregadoDepartamental;
    }

    public Boolean getSimTituloEntregadoCentroEducativo() {
        return simTituloEntregadoCentroEducativo;
    }

    public void setSimTituloEntregadoCentroEducativo(Boolean simTituloEntregadoCentroEducativo) {
        this.simTituloEntregadoCentroEducativo = simTituloEntregadoCentroEducativo;
    }

    public LocalDate getSimFechaEntregadoCentroEducativo() {
        return simFechaEntregadoCentroEducativo;
    }

    public void setSimFechaEntregadoCentroEducativo(LocalDate simFechaEntregadoCentroEducativo) {
        this.simFechaEntregadoCentroEducativo = simFechaEntregadoCentroEducativo;
    }

    public String getSimImpresora() {
        return simImpresora;
    }

    public void setSimImpresora(String simImpresora) {
        this.simImpresora = simImpresora;
    }

    public LocalDate getSimFechaValidez() {
        return simFechaValidez;
    }

    public void setSimFechaValidez(LocalDate simFechaValidez) {
        this.simFechaValidez = simFechaValidez;
    }

    public SgArchivo getSimResolucionFk() {
        return simResolucionFk;
    }

    public void setSimResolucionFk(SgArchivo simResolucionFk) {
        this.simResolucionFk = simResolucionFk;
    }

    public Boolean getSimReposicionEstudianteSiges() {
        return simReposicionEstudianteSiges;
    }

    public void setSimReposicionEstudianteSiges(Boolean simReposicionEstudianteSiges) {
        this.simReposicionEstudianteSiges = simReposicionEstudianteSiges;
    }

    public String getSimNumeroRegistro() {
        return simNumeroRegistro;
    }

    public void setSimNumeroRegistro(String simNumeroRegistro) {
        this.simNumeroRegistro = simNumeroRegistro;
    }

    public String getSimNumeroResolucion() {
        return simNumeroResolucion;
    }

    public void setSimNumeroResolucion(String simNumeroResolucion) {
        this.simNumeroResolucion = simNumeroResolucion;
    }

    public Boolean getSimEvitarValidacionSelloFirma() {
        return simEvitarValidacionSelloFirma;
    }

    public void setSimEvitarValidacionSelloFirma(Boolean simEvitarValidacionSelloFirma) {
        this.simEvitarValidacionSelloFirma = simEvitarValidacionSelloFirma;
    }
   

   

  
    @Override
    public String securityAmbitCreate() {
        return null; //TODO: para permitir crear adscriptas en un departamento distinto al de la sede padre, hay que modificar el DAO
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secPk", -1L);
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.simPk);
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
        final SgSolicitudImpresion other = (SgSolicitudImpresion) obj;
        if (!Objects.equals(this.simPk, other.simPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSolicitudImpresion{" + "simPk=" + simPk + ", simUltModFecha=" + simUltModFecha + ", simUltModUsuario=" + simUltModUsuario + ", simVersion=" + simVersion + '}';
    }
    
    

}
