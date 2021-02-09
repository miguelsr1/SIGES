/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
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
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoPEA;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_plan_escolar_anual",uniqueConstraints = {
    @UniqueConstraint(name = "pea_sede_propuesta_uk", columnNames = {"pea_sede_fk", "pea_propuesta_pedagogica_fk", "pea_anio_lectivo_fk"})}, 
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "peaPk", scope = SgPlanEscolarAnual.class)
public class SgPlanEscolarAnual implements Serializable , DataSecurity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pea_pk")
    private Long peaPk;
    
    @JoinColumn(name = "pea_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede peaSede;
    
    @JoinColumn(name = "pea_anio_lectivo_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo peaAnioLectivo;
    
    @JoinColumn(name = "pea_propuesta_pedagogica_fk", referencedColumnName = "ppe_pk")
    @ManyToOne
    private SgPropuestaPedagogica peaPropuestaPedagogica;
    
    @Column(name = "pea_fecha_presentado")
    private LocalDate peaFechaPresentado;
    
    @Size(max = 500)
    @Column(name = "pea_evaluacion_uno", length = 500)
    private String peaEvaluacionUno;
    
    @Column(name = "pea_porcentaje_logro_uno")
    private Integer peaPorcentajeLogroUno;
    
    @Size(max = 500)
    @Column(name = "pea_evaluacion_dos", length = 500)
    private String peaEvaluacionDos;
    
    @Column(name = "pea_porcentaje_logro_dos")
    private Integer peaPorcentajeLogroDos;
    
    @Column(name = "pea_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoPEA peaEstado;
    
    @Column(name = "pea_fecha_valido")
    private LocalDate peaFechaValido;
    
    @JoinColumn(name = "pea_usuario_valido_fk")
    @ManyToOne
    private SgUsuario peaUsuarioValido;
    
    @Column(name = "pea_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime peaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pea_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String peaUltModUsuario;
    
    @Column(name = "pea_version")
    @Version
    private Integer peaVersion;
    
    @OneToMany(mappedBy = "dpePlanEscolarAnual",cascade = CascadeType.ALL)
    private List<SgDetallePlanEscolar> peaDetallePlanEscolar;

    public SgPlanEscolarAnual() {
    }

    public SgPlanEscolarAnual(Long peaPk) {
        this.peaPk = peaPk;
    }

    public Long getPeaPk() {
        return peaPk;
    }

    public void setPeaPk(Long peaPk) {
        this.peaPk = peaPk;
    }

    public SgSede getPeaSede() {
        return peaSede;
    }

    public void setPeaSede(SgSede peaSede) {
        this.peaSede = peaSede;
    }

    public SgAnioLectivo getPeaAnioLectivo() {
        return peaAnioLectivo;
    }

    public void setPeaAnioLectivo(SgAnioLectivo peaAnioLectivo) {
        this.peaAnioLectivo = peaAnioLectivo;
    }

    public SgPropuestaPedagogica getPeaPropuestaPedagogica() {
        return peaPropuestaPedagogica;
    }

    public void setPeaPropuestaPedagogica(SgPropuestaPedagogica peaPropuestaPedagogica) {
        this.peaPropuestaPedagogica = peaPropuestaPedagogica;
    }

    public LocalDate getPeaFechaPresentado() {
        return peaFechaPresentado;
    }

    public void setPeaFechaPresentado(LocalDate peaFechaPresentado) {
        this.peaFechaPresentado = peaFechaPresentado;
    }

    public String getPeaEvaluacionUno() {
        return peaEvaluacionUno;
    }

    public void setPeaEvaluacionUno(String peaEvaluacionUno) {
        this.peaEvaluacionUno = peaEvaluacionUno;
    }

    public Integer getPeaPorcentajeLogroUno() {
        return peaPorcentajeLogroUno;
    }

    public void setPeaPorcentajeLogroUno(Integer peaPorcentajeLogroUno) {
        this.peaPorcentajeLogroUno = peaPorcentajeLogroUno;
    }

    public String getPeaEvaluacionDos() {
        return peaEvaluacionDos;
    }

    public void setPeaEvaluacionDos(String peaEvaluacionDos) {
        this.peaEvaluacionDos = peaEvaluacionDos;
    }

    public Integer getPeaPorcentajeLogroDos() {
        return peaPorcentajeLogroDos;
    }

    public void setPeaPorcentajeLogroDos(Integer peaPorcentajeLogroDos) {
        this.peaPorcentajeLogroDos = peaPorcentajeLogroDos;
    }

    public EnumEstadoPEA getPeaEstado() {
        return peaEstado;
    }

    public void setPeaEstado(EnumEstadoPEA peaEstado) {
        this.peaEstado = peaEstado;
    }

    public LocalDate getPeaFechaValido() {
        return peaFechaValido;
    }

    public void setPeaFechaValido(LocalDate peaFechaValido) {
        this.peaFechaValido = peaFechaValido;
    }

    public SgUsuario getPeaUsuarioValido() {
        return peaUsuarioValido;
    }

    public void setPeaUsuarioValido(SgUsuario peaUsuarioValido) {
        this.peaUsuarioValido = peaUsuarioValido;
    }

    public LocalDateTime getPeaUltModFecha() {
        return peaUltModFecha;
    }

    public void setPeaUltModFecha(LocalDateTime peaUltModFecha) {
        this.peaUltModFecha = peaUltModFecha;
    }

    public String getPeaUltModUsuario() {
        return peaUltModUsuario;
    }

    public void setPeaUltModUsuario(String peaUltModUsuario) {
        this.peaUltModUsuario = peaUltModUsuario;
    }

    public Integer getPeaVersion() {
        return peaVersion;
    }

    public void setPeaVersion(Integer peaVersion) {
        this.peaVersion = peaVersion;
    }
    
    public List<SgDetallePlanEscolar> getPeaDetallePlanEscolar() {
        return peaDetallePlanEscolar;
    }

    public void setPeaDetallePlanEscolar(List<SgDetallePlanEscolar> peaDetallePlanEscolar) {
        this.peaDetallePlanEscolar = peaDetallePlanEscolar;
    }

    
    

    @Override
    public String securityAmbitCreate() {
       return "peaSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "peaSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "peaSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "peaPk", -1L);
        } 
    }

   
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peaPk != null ? peaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlanEscolarAnual)) {
            return false;
        }
        SgPlanEscolarAnual other = (SgPlanEscolarAnual) object;
        if ((this.peaPk == null && other.peaPk != null) || (this.peaPk != null && !this.peaPk.equals(other.peaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlanEscolarAnual[ peaPk=" + peaPk + " ]";
    }
    
}
