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
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calificaciones_historicas_estudiante", schema = Constantes.SCHEMA_ACREDITACION)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "chePk", scope = SgCalificacionesHistoricasEstudiante.class)
@Audited
public class SgCalificacionesHistoricasEstudiante implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "che_pk", nullable = false)
    private Long chePk;
   
    @Column(name = "che_matricula_externa_id")
    private Long cheMatriculaExternaId;
    
    @Column(name = "che_anio_matricula")
    private Integer cheAnioMatricula;
    
    @Column(name = "che_observacion")
    private String cheObservacion;
    
    @Column(name = "che_fecha_realizado")
    private LocalDate cheFechaRealizado;
    
    @Column(name = "che_proceso_de_creacion")
    private String cheProcesoDeCreacion;
    
    @JoinColumn(name = "che_estudiante_fk")
    @ManyToOne(optional = false)
    private SgEstudiante cheEstudianteFk;
    
    @JoinColumn(name = "che_sede_fk")
    @ManyToOne
    private SgSede cheSedeFk;
    
    @Column(name = "che_sede_externa_codigo")
    private String cheSedeExternaCodigo;
    
    @Column(name = "che_sede_externa_nombre")
    private String cheSedeExternaNombre;
    
    @Column(name = "che_sede_externa_nombre_busqueda")
    private String cheSedeExternaNombreBusqueda;
    
    @Column(name = "che_servicio_educativo_externo_descripcion")
    private String cheServicioEducativoExternoDescripcion;
    
    @Column(name = "che_servicio_educativo_externo_etiqueta_impresion")
    private String cheServicioEducativoExternoEtiquetaImpresion;
    
    @Column(name = "che_plan_estudio_externo_id")
    private Long chePlanEstudioExternoId;
    
    @Column(name = "che_plan_estudio_externo_descripcion")
    private String chePlanEstudioExternoDescripcion;
    
    @Column(name = "che_componente")
    private String cheComponente;
    
    @Column(name = "che_componente_busqueda")
    private String cheComponenteBusqueda;
    
    @Column(name = "che_pi")
    private String chePI;
    
    @Column(name = "che_nfi")
    private String cheNFI;
    
    @Column(name = "che_npaesi")
    private String cheNPAESI;
    
    @Column(name = "che_rix")
    private String cheRIX;
    
    @Column(name = "che_rir")
    private String cheRIR;
    
    @Column(name = "che_rire")
    private String cheRIRE;
    
    @Column(name = "che_nf")
    private String cheNF;
    
    @Column(name = "che_npaes")
    private String cheNPAES;
    
    @Column(name = "che_pi_modif")
    private String chePIModif;
 
    @Column(name = "che_pi_r")
    private String chePI_r;
    
    @Column(name = "che_pir")
    private String chePIR;

    @Column(name = "che_evaluacion_externa_id")
    private Long cheEvaluacionExternaId;
    
    @Column(name = "che_servicio_educativo_externo_id")
    private Long cheServicioEducativoExternoId;
    
    @Column(name = "che_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cheUltModFecha;

    @Size(max = 45)
    @Column(name = "che_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cheUltModUsuario;

    @Column(name = "che_version")
    @Version
    private Integer cheVersion;
    
    @Column(name = "che_estudiante_nie")
    private Long cheEstudianteNie;
    
    @JoinColumn(name = "che_estudiante_per_pk")
    @ManyToOne(optional = false)
    private SgPersona chePersonaFk;
    
    @Column(name = "che_observacion_edicion")
    private String cheObservacionEdicion;
    
    @JoinColumn(name = "che_archivo_edicion_fk", referencedColumnName = "ach_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgArchivo cheArchivoEdicion;    

    @Column(name = "che_es_importado")
    private Boolean cheEsImportado;
    
    //Es un dato transient, se utiliza para indicar si el cambio de estudiante
    //en una calificación tiene que abarcar a todas las calificaciones
    @Transient
    private Boolean modificarCalificacionesEstudiante;
    
    @Transient
    private SgEstudiante nuevoEstudiante;

    public SgCalificacionesHistoricasEstudiante() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cheSedeExternaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cheSedeExternaNombre);
        this.cheComponenteBusqueda= SofisStringUtils.normalizarParaBusqueda(this.cheComponente);
        if(this.cheEstudianteFk!=null){
            if(this.cheEstudianteFk.getEstPersona()!=null){
                this.chePersonaFk=cheEstudianteFk.getEstPersona();
                if(this.chePersonaFk.getPerNie()!=null){
                    this.cheEstudianteNie=this.chePersonaFk.getPerNie();
                }
            }
        }
    }

    public SgEstudiante getCheEstudianteFk() {
        return cheEstudianteFk;
    }

    public void setCheEstudianteFk(SgEstudiante cheEstudianteFk) {
        this.cheEstudianteFk = cheEstudianteFk;
    }

    public String getCheSedeExternaNombreBusqueda() {
        return cheSedeExternaNombreBusqueda;
    }

    public void setCheSedeExternaNombreBusqueda(String cheSedeExternaNombreBusqueda) {
        this.cheSedeExternaNombreBusqueda = cheSedeExternaNombreBusqueda;
    }

    public String getCheComponenteBusqueda() {
        return cheComponenteBusqueda;
    }

    public void setCheComponenteBusqueda(String cheComponenteBusqueda) {
        this.cheComponenteBusqueda = cheComponenteBusqueda;
    }


    public Long getChePk() {
        return chePk;
    }

    public void setChePk(Long chePk) {
        this.chePk = chePk;
    }

    public Long getCheMatriculaExternaId() {
        return cheMatriculaExternaId;
    }

    public void setCheMatriculaExternaId(Long cheMatriculaExternaId) {
        this.cheMatriculaExternaId = cheMatriculaExternaId;
    }

    public Integer getCheAnioMatricula() {
        return cheAnioMatricula;
    }

    public void setCheAnioMatricula(Integer cheAnioMatricula) {
        this.cheAnioMatricula = cheAnioMatricula;
    }

    public String getCheObservacion() {
        return cheObservacion;
    }

    public void setCheObservacion(String cheObservacion) {
        this.cheObservacion = cheObservacion;
    }

    public LocalDate getCheFechaRealizado() {
        return cheFechaRealizado;
    }

    public void setCheFechaRealizado(LocalDate cheFechaRealizado) {
        this.cheFechaRealizado = cheFechaRealizado;
    }

    public String getCheProcesoDeCreacion() {
        return cheProcesoDeCreacion;
    }

    public void setCheProcesoDeCreacion(String cheProcesoDeCreacion) {
        this.cheProcesoDeCreacion = cheProcesoDeCreacion;
    }

    public SgSede getCheSedeFk() {
        return cheSedeFk;
    }

    public void setCheSedeFk(SgSede cheSedeFk) {
        this.cheSedeFk = cheSedeFk;
    }

    public String getCheSedeExternaCodigo() {
        return cheSedeExternaCodigo;
    }

    public void setCheSedeExternaCodigo(String cheSedeExternaCodigo) {
        this.cheSedeExternaCodigo = cheSedeExternaCodigo;
    }

    public String getCheSedeExternaNombre() {
        return cheSedeExternaNombre;
    }

    public void setCheSedeExternaNombre(String cheSedeExternaNombre) {
        this.cheSedeExternaNombre = cheSedeExternaNombre;
    }

    public String getCheServicioEducativoExternoDescripcion() {
        return cheServicioEducativoExternoDescripcion;
    }

    public void setCheServicioEducativoExternoDescripcion(String cheServicioEducativoExternoDescripcion) {
        this.cheServicioEducativoExternoDescripcion = cheServicioEducativoExternoDescripcion;
    }

    public String getCheServicioEducativoExternoEtiquetaImpresion() {
        return cheServicioEducativoExternoEtiquetaImpresion;
    }

    public void setCheServicioEducativoExternoEtiquetaImpresion(String cheServicioEducativoExternoEtiquetaImpresion) {
        this.cheServicioEducativoExternoEtiquetaImpresion = cheServicioEducativoExternoEtiquetaImpresion;
    }

    public Long getChePlanEstudioExternoId() {
        return chePlanEstudioExternoId;
    }

    public void setChePlanEstudioExternoId(Long chePlanEstudioExternoId) {
        this.chePlanEstudioExternoId = chePlanEstudioExternoId;
    }

    public String getChePlanEstudioExternoDescripcion() {
        return chePlanEstudioExternoDescripcion;
    }

    public void setChePlanEstudioExternoDescripcion(String chePlanEstudioExternoDescripcion) {
        this.chePlanEstudioExternoDescripcion = chePlanEstudioExternoDescripcion;
    }

    public String getCheComponente() {
        return cheComponente;
    }

    public void setCheComponente(String cheComponente) {
        this.cheComponente = cheComponente;
    }

    public String getChePI() {
        return chePI;
    }

    public void setChePI(String chePI) {
        this.chePI = chePI;
    }

    public String getCheNFI() {
        return cheNFI;
    }

    public void setCheNFI(String cheNFI) {
        this.cheNFI = cheNFI;
    }

    public String getCheNPAESI() {
        return cheNPAESI;
    }

    public void setCheNPAESI(String cheNPAESI) {
        this.cheNPAESI = cheNPAESI;
    }

    public String getCheRIX() {
        return cheRIX;
    }

    public void setCheRIX(String cheRIX) {
        this.cheRIX = cheRIX;
    }

    public String getCheRIR() {
        return cheRIR;
    }

    public void setCheRIR(String cheRIR) {
        this.cheRIR = cheRIR;
    }

    public String getCheRIRE() {
        return cheRIRE;
    }

    public void setCheRIRE(String cheRIRE) {
        this.cheRIRE = cheRIRE;
    }

    public String getCheNF() {
        return cheNF;
    }

    public void setCheNF(String cheNF) {
        this.cheNF = cheNF;
    }

    public String getCheNPAES() {
        return cheNPAES;
    }

    public void setCheNPAES(String cheNPAES) {
        this.cheNPAES = cheNPAES;
    }

    public String getChePIModif() {
        return chePIModif;
    }

    public void setChePIModif(String chePIModif) {
        this.chePIModif = chePIModif;
    }

    public String getChePI_r() {
        return chePI_r;
    }

    public void setChePI_r(String chePI_r) {
        this.chePI_r = chePI_r;
    }

    public String getChePIR() {
        return chePIR;
    }

    public void setChePIR(String chePIR) {
        this.chePIR = chePIR;
    }

    public Long getCheEstudianteNie() {
        return cheEstudianteNie;
    }

    public void setCheEstudianteNie(Long cheEstudianteNie) {
        this.cheEstudianteNie = cheEstudianteNie;
    }

    public SgPersona getChePersonaFk() {
        return chePersonaFk;
    }

    public void setChePersonaFk(SgPersona chePersonaFk) {
        this.chePersonaFk = chePersonaFk;
    }

    public String getCheObservacionEdicion() {
        return cheObservacionEdicion;
    }

    public void setCheObservacionEdicion(String cheObservacionEdicion) {
        this.cheObservacionEdicion = cheObservacionEdicion;
    }

    public SgArchivo getCheArchivoEdicion() {
        return cheArchivoEdicion;
    }

    public void setCheArchivoEdicion(SgArchivo cheArchivoEdicion) {
        this.cheArchivoEdicion = cheArchivoEdicion;
    }

    public Boolean getModificarCalificacionesEstudiante() {
        return modificarCalificacionesEstudiante;
    }

    public void setModificarCalificacionesEstudiante(Boolean modificarCalificacionesEstudiante) {
        this.modificarCalificacionesEstudiante = modificarCalificacionesEstudiante;
    }

   

    public LocalDateTime getCheUltModFecha() {
        return cheUltModFecha;
    }

    public void setCheUltModFecha(LocalDateTime cheUltModFecha) {
        this.cheUltModFecha = cheUltModFecha;
    }

    public String getCheUltModUsuario() {
        return cheUltModUsuario;
    }

    public void setCheUltModUsuario(String cheUltModUsuario) {
        this.cheUltModUsuario = cheUltModUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    public void setCheVersion(Integer cheVersion) {
        this.cheVersion = cheVersion;
    }

    public Long getCheEvaluacionExternaId() {
        return cheEvaluacionExternaId;
    }

    public void setCheEvaluacionExternaId(Long cheEvaluacionExternaId) {
        this.cheEvaluacionExternaId = cheEvaluacionExternaId;
    }

    public Long getCheServicioEducativoExternoId() {
        return cheServicioEducativoExternoId;
    }

    public void setCheServicioEducativoExternoId(Long cheServicioEducativoExternoId) {
        this.cheServicioEducativoExternoId = cheServicioEducativoExternoId;
    }

    public SgEstudiante getNuevoEstudiante() {
        return nuevoEstudiante;
    }

    public void setNuevoEstudiante(SgEstudiante nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
    }

    public Boolean getCheEsImportado() {
        return cheEsImportado;
    }

    public void setCheEsImportado(Boolean cheEsImportado) {
        this.cheEsImportado = cheEsImportado;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.chePk);
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
        final SgCalificacionesHistoricasEstudiante other = (SgCalificacionesHistoricasEstudiante) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String securityAmbitCreate() {
        return null; //Estudiantes se crean mediante matricula.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SISTEMA_INTEGRADO.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estMatriculas.matSeccion.secServicioEducativo.sduSede.sedSistemas.sinPk.sinPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estMatriculas.matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estMatriculas.matSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "cheEstudianteFk.estMatriculas.matSeccion.secServicioEducativo.sduSede.sedPk", o.getRegla());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MODALIDADES_FLEXIBLES.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.IMPLEMENTADORA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estUltimaMatricula.matSeccion.secAsociacion.asoPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SECCION.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estUltimaSeccionPk", o.getContext());
            //No hacer join con otra tabla. Al hacer joins si la persona tiene muchos ambitos, se forma query con ORs degradando la performance
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cheEstudianteFk.estPk", -1L);
        }
    }

    @Override
    public String toString() {
        return "SgCalificacionesHistoricasEstudiante{" + "chePk=" + chePk +  ", cheUltModFecha=" + cheUltModFecha + ", cheUltModUsuario=" + cheUltModUsuario + ", cheVersion=" + cheVersion + '}';
    }
    
    

}
