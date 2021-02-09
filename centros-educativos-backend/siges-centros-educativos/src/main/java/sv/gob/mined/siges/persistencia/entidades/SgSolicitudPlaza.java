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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCaracterizacionPlaza;
import sv.gob.mined.siges.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.enumerados.EnumTipoPlaza;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_solicitudes_plazas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "splPk", scope = SgSolicitudPlaza.class)
@Audited
public class SgSolicitudPlaza implements Serializable, DataSecurity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "spl_pk")
    private Long splPk;
            
    @JoinColumn(name = "spl_sede_solicitante_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede splSedeSolicitante;
    
    @Column(name = "spl_tipo_plaza")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoPlaza splTipoPlaza;
            
    @JoinColumn(name = "spl_nivel_fk", referencedColumnName = "niv_pk")
    @ManyToOne
    private SgNivel splNivel;
            
    @JoinColumn(name = "spl_modalidad_educativa_fk", referencedColumnName = "mod_pk")
    @ManyToOne
    private SgModalidad splModalidadEducativa;
            
    @JoinColumn(name = "spl_modalidad_atencion_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgModalidadAtencion splModalidadAtencion;
            
    @JoinColumn(name = "spl_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne
    private SgOpcion splOpcion;
            
    @JoinColumn(name = "spl_componente_plan_estudio_fk", referencedColumnName = "cpe_pk")
    @ManyToOne
    private SgComponentePlanEstudio splComponentePlanEstudio;
            
    @JoinColumn(name = "spl_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad splEspecialidad;
            
    @JoinColumn(name = "spl_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgFuenteFinanciamiento splFuenteFinanciamiento;
    
    @Column(name = "spl_fecha_desde")
    private LocalDate splFechaDesde;
    
    @Column(name = "spl_fecha_hasta")
    private LocalDate splFechaHasta;
            
    @JoinColumn(name = "spl_jornada_fk", referencedColumnName = "jla_pk")
    @ManyToOne
    private SgJornadaLaboral splJornada;
    
    @Size(max = 400)
    @Column(name = "spl_descripcion", length = 400)
    private String splDescripcion;
    
    @Column(name = "spl_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudPlaza splEstado;
            
    @JoinColumn(name = "spl_tipo_nombramiento_fk", referencedColumnName = "car_pk")
    @ManyToOne
    private SgCargo splTipoNombramiento;
    
    @Column(name = "spl_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime splUltModFecha;
    
    @Size(max = 45)
    @Column(name = "spl_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String splUltModUsuario;
    
    @Column(name = "spl_version")
    @Version
    private Integer splVersion;
    
    @Size(max = 10)
    @Column(name = "spl_codigo", length = 10)
    private String splCodigo;
    
    @Column(name = "spl_id_plaza")
    private Integer splIdPlaza;
    
    @Column(name = "spl_anio_partida")
    private Integer splAnioPartida;
    
    @Size(max = 255)
    @Column(name = "spl_nombre", length = 255)
    private String splNombre;
    
    @Column(name = "spl_caracterizacion")
    @Enumerated(value = EnumType.STRING)
    private EnumCaracterizacionPlaza splCaracterizacion;
    
    @Column(name = "spl_estado_plaza")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoPlaza splEstadoPlaza;
        
    @Column(name = "spl_postulacion_inicio")
    private LocalDate splPostulacionInicio;
    
    @Column(name = "spl_postulacion_fin")
    private LocalDate splPostulacionFin;
    
    @Column(name = "spl_plaza_existente")
    private Boolean splPlazaExistente;
    
    @JoinColumn(name = "spl_plaza_fk", referencedColumnName = "pla_pk")
    @ManyToOne
    private SgPlaza splPlazaFk;
    
    @OneToMany(mappedBy = "aplPlaza")
    @NotAudited
    private List<SgAplicacionPlaza> splAplicacionesPlaza;

    @Transient
    private Boolean splPuedeSerPublicado;
    
    public SgSolicitudPlaza() {
        
    }

    public SgSolicitudPlaza(Long splPk) {
        this.splPk = splPk;
    }

    public Long getSplPk() {
        return splPk;
    }

    public void setSplPk(Long splPk) {
        this.splPk = splPk;
    }

    public SgSede getSplSedeSolicitante() {
        return splSedeSolicitante;
    }

    public void setSplSedeSolicitante(SgSede splSedeSolicitante) {
        this.splSedeSolicitante = splSedeSolicitante;
    }

    public EnumTipoPlaza getSplTipoPlaza() {
        return splTipoPlaza;
    }

    public void setSplTipoPlaza(EnumTipoPlaza splTipoPlaza) {
        this.splTipoPlaza = splTipoPlaza;
    }

    public SgNivel getSplNivel() {
        return splNivel;
    }

    public void setSplNivel(SgNivel splNivel) {
        this.splNivel = splNivel;
    }

    public SgModalidad getSplModalidadEducativa() {
        return splModalidadEducativa;
    }

    public void setSplModalidadEducativa(SgModalidad splModalidadEducativa) {
        this.splModalidadEducativa = splModalidadEducativa;
    }

    public SgModalidadAtencion getSplModalidadAtencion() {
        return splModalidadAtencion;
    }

    public void setSplModalidadAtencion(SgModalidadAtencion splModalidadAtencion) {
        this.splModalidadAtencion = splModalidadAtencion;
    }

    public SgOpcion getSplOpcion() {
        return splOpcion;
    }

    public void setSplOpcion(SgOpcion splOpcion) {
        this.splOpcion = splOpcion;
    }

    public SgComponentePlanEstudio getSplComponentePlanEstudio() {
        return splComponentePlanEstudio;
    }

    public void setSplComponentePlanEstudio(SgComponentePlanEstudio splComponentePlanEstudio) {
        this.splComponentePlanEstudio = splComponentePlanEstudio;
    }

    public SgEspecialidad getSplEspecialidad() {
        return splEspecialidad;
    }

    public void setSplEspecialidad(SgEspecialidad splEspecialidad) {
        this.splEspecialidad = splEspecialidad;
    }

    public SgFuenteFinanciamiento getSplFuenteFinanciamiento() {
        return splFuenteFinanciamiento;
    }

    public void setSplFuenteFinanciamiento(SgFuenteFinanciamiento splFuenteFinanciamiento) {
        this.splFuenteFinanciamiento = splFuenteFinanciamiento;
    }

    public LocalDate getSplFechaDesde() {
        return splFechaDesde;
    }

    public void setSplFechaDesde(LocalDate splFechaDesde) {
        this.splFechaDesde = splFechaDesde;
    }

    public LocalDate getSplFechaHasta() {
        return splFechaHasta;
    }

    public void setSplFechaHasta(LocalDate splFechaHasta) {
        this.splFechaHasta = splFechaHasta;
    }

    public SgJornadaLaboral getSplJornada() {
        return splJornada;
    }

    public void setSplJornada(SgJornadaLaboral splJornada) {
        this.splJornada = splJornada;
    }

    public String getSplDescripcion() {
        return splDescripcion;
    }

    public void setSplDescripcion(String splDescripcion) {
        this.splDescripcion = splDescripcion;
    }

    public EnumEstadoSolicitudPlaza getSplEstado() {
        return splEstado;
    }

    public void setSplEstado(EnumEstadoSolicitudPlaza splEstado) {
        this.splEstado = splEstado;
    }

    public SgCargo getSplTipoNombramiento() {
        return splTipoNombramiento;
    }

    public void setSplTipoNombramiento(SgCargo splTipoNombramiento) {
        this.splTipoNombramiento = splTipoNombramiento;
    }

    public LocalDateTime getSplUltModFecha() {
        return splUltModFecha;
    }

    public void setSplUltModFecha(LocalDateTime splUltModFecha) {
        this.splUltModFecha = splUltModFecha;
    }

    public String getSplUltModUsuario() {
        return splUltModUsuario;
    }

    public void setSplUltModUsuario(String splUltModUsuario) {
        this.splUltModUsuario = splUltModUsuario;
    }

    public Integer getSplVersion() {
        return splVersion;
    }

    public void setSplVersion(Integer splVersion) {
        this.splVersion = splVersion;
    }

    public String getSplCodigo() {
        return splCodigo;
    }

    public void setSplCodigo(String splCodigo) {
        this.splCodigo = splCodigo;
    }

    public Integer getSplIdPlaza() {
        return splIdPlaza;
    }

    public void setSplIdPlaza(Integer splIdPlaza) {
        this.splIdPlaza = splIdPlaza;
    }

    public Integer getSplAnioPartida() {
        return splAnioPartida;
    }

    public void setSplAnioPartida(Integer splAnioPartida) {
        this.splAnioPartida = splAnioPartida;
    }

    public String getSplNombre() {
        return splNombre;
    }

    public void setSplNombre(String splNombre) {
        this.splNombre = splNombre;
    }

    public EnumCaracterizacionPlaza getSplCaracterizacion() {
        return splCaracterizacion;
    }

    public void setSplCaracterizacion(EnumCaracterizacionPlaza splCaracterizacion) {
        this.splCaracterizacion = splCaracterizacion;
    }

    public EnumEstadoPlaza getSplEstadoPlaza() {
        return splEstadoPlaza;
    }

    public void setSplEstadoPlaza(EnumEstadoPlaza splEstadoPlaza) {
        this.splEstadoPlaza = splEstadoPlaza;
    }

    public LocalDate getSplPostulacionInicio() {
        return splPostulacionInicio;
    }

    public void setSplPostulacionInicio(LocalDate splPostulacionInicio) {
        this.splPostulacionInicio = splPostulacionInicio;
    }

    public LocalDate getSplPostulacionFin() {
        return splPostulacionFin;
    }

    public void setSplPostulacionFin(LocalDate splPostulacionFin) {
        this.splPostulacionFin = splPostulacionFin;
    }

    public SgPlaza getSplPlazaFk() {
        return splPlazaFk;
    }

    public void setSplPlazaFk(SgPlaza splPlazaFk) {
        this.splPlazaFk = splPlazaFk;
    }

    public Boolean getSplPlazaExistente() {
        return splPlazaExistente;
    }

    public void setSplPlazaExistente(Boolean splPlazaExistente) {
        this.splPlazaExistente = splPlazaExistente;
    }

    public List<SgAplicacionPlaza> getSplAplicacionesPlaza() {
        return splAplicacionesPlaza;
    }

    public void setSplAplicacionesPlaza(List<SgAplicacionPlaza> splAplicacionesPlaza) {
        this.splAplicacionesPlaza = splAplicacionesPlaza;
    }    

    public Boolean getSplPuedeSerPublicado() {
        return splPuedeSerPublicado;
    }

    public void setSplPuedeSerPublicado(Boolean splPuedeSerPublicado) {
        this.splPuedeSerPublicado = splPuedeSerPublicado;
    }
    
    
    
    @Override
    public String securityAmbitCreate() {
       return "splSedeSolicitante";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splSedeSolicitante.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splSedeSolicitante.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splPk", -1L);
        } 
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (splPk != null ? splPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSolicitudPlaza)) {
            return false;
        }
        SgSolicitudPlaza other = (SgSolicitudPlaza) object;
        if ((this.splPk == null && other.splPk != null) || (this.splPk != null && !this.splPk.equals(other.splPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudPlaza[ splPk=" + splPk + " ]";
    }
    
}
