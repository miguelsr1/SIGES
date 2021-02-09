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
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_propuestas_pedagogicas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ppePk", scope = SgPropuestaPedagogica.class)
@Audited
public class SgPropuestaPedagogica implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ppe_pk")
    private Long ppePk;
    
    @JoinColumn(name = "ppe_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede ppeSede;
    
    @Size(max = 100)
    @Column(name = "ppe_nombre", length = 100)
    private String ppeNombre;
    
    @Size(max = 4000)
    @Column(name = "ppe_caracterizacion", length = 4000)
    private String ppeCaracterizacion;
    
    @Size(max = 4000)
    @Column(name = "ppe_problemas_priorizados", length = 4000)
    private String ppeProblemasPriorizados;
    
    @Size(max = 4000)
    @Column(name = "ppe_perfil_estudiante", length = 4000)
    private String ppePerfilEstudiante;
    
    @Size(max = 4000)
    @Column(name = "ppe_retos_pedagogicos", length = 4000)
    private String ppeRetosPedagogicos;
    
    @Size(max = 4000)
    @Column(name = "ppe_acuerdos_pedagogicos", length = 4000)
    private String ppeAcuerdosPedagogicos;
    
    @Size(max = 4000)
    @Column(name = "ppe_metas", length = 4000)
    private String ppeMetas;
    
    @Size(max = 4000)
    @Column(name = "ppe_indicadores", length = 4000)
    private String ppeIndicadores;
    
    @Column(name = "ppe_fecha_inicio")
    private LocalDate ppeFechaInicio;
    
    @Column(name = "ppe_fecha_fin")
    private LocalDate ppeFechaFin;
    
    @JoinColumn(name = "ppe_archivo_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo ppeArchivo;
    
    @Column(name = "ppe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ppeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ppe_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ppeUltModUsuario;
    
    @Column(name = "ppe_version")
    @Version
    private Integer ppeVersion;

    public SgPropuestaPedagogica() {
    }

    public SgPropuestaPedagogica(Long ppePk) {
        this.ppePk = ppePk;
    }

    public Long getPpePk() {
        return ppePk;
    }

    public void setPpePk(Long ppePk) {
        this.ppePk = ppePk;
    }

    public SgSede getPpeSede() {
        return ppeSede;
    }

    public void setPpeSede(SgSede ppeSede) {
        this.ppeSede = ppeSede;
    }

    public String getPpeNombre() {
        return ppeNombre;
    }

    public void setPpeNombre(String ppeNombre) {
        this.ppeNombre = ppeNombre;
    }

    public String getPpeCaracterizacion() {
        return ppeCaracterizacion;
    }

    public void setPpeCaracterizacion(String ppeCaracterizacion) {
        this.ppeCaracterizacion = ppeCaracterizacion;
    }

    public String getPpeProblemasPriorizados() {
        return ppeProblemasPriorizados;
    }

    public void setPpeProblemasPriorizados(String ppeProblemasPriorizados) {
        this.ppeProblemasPriorizados = ppeProblemasPriorizados;
    }

    public String getPpePerfilEstudiante() {
        return ppePerfilEstudiante;
    }

    public void setPpePerfilEstudiante(String ppePerfilEstudiante) {
        this.ppePerfilEstudiante = ppePerfilEstudiante;
    }

    public String getPpeRetosPedagogicos() {
        return ppeRetosPedagogicos;
    }

    public void setPpeRetosPedagogicos(String ppeRetosPedagogicos) {
        this.ppeRetosPedagogicos = ppeRetosPedagogicos;
    }

    public String getPpeAcuerdosPedagogicos() {
        return ppeAcuerdosPedagogicos;
    }

    public void setPpeAcuerdosPedagogicos(String ppeAcuerdosPedagogicos) {
        this.ppeAcuerdosPedagogicos = ppeAcuerdosPedagogicos;
    }

    public String getPpeMetas() {
        return ppeMetas;
    }

    public void setPpeMetas(String ppeMetas) {
        this.ppeMetas = ppeMetas;
    }

    public String getPpeIndicadores() {
        return ppeIndicadores;
    }

    public void setPpeIndicadores(String ppeIndicadores) {
        this.ppeIndicadores = ppeIndicadores;
    }

    public LocalDate getPpeFechaInicio() {
        return ppeFechaInicio;
    }

    public void setPpeFechaInicio(LocalDate ppeFechaInicio) {
        this.ppeFechaInicio = ppeFechaInicio;
    }

    public LocalDate getPpeFechaFin() {
        return ppeFechaFin;
    }

    public void setPpeFechaFin(LocalDate ppeFechaFin) {
        this.ppeFechaFin = ppeFechaFin;
    }

    public LocalDateTime getPpeUltModFecha() {
        return ppeUltModFecha;
    }

    public void setPpeUltModFecha(LocalDateTime ppeUltModFecha) {
        this.ppeUltModFecha = ppeUltModFecha;
    }

    public String getPpeUltModUsuario() {
        return ppeUltModUsuario;
    }

    public void setPpeUltModUsuario(String ppeUltModUsuario) {
        this.ppeUltModUsuario = ppeUltModUsuario;
    }

    public Integer getPpeVersion() {
        return ppeVersion;
    }

    public void setPpeVersion(Integer ppeVersion) {
        this.ppeVersion = ppeVersion;
    }

    public SgArchivo getPpeArchivo() {
        return ppeArchivo;
    }

    public void setPpeArchivo(SgArchivo ppeArchivo) {
        this.ppeArchivo = ppeArchivo;
    }
    
    
    @Override
    public String securityAmbitCreate() {
       return "ppeSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ppeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ppeSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ppePk", -1L);
        } 
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ppePk != null ? ppePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPropuestaPedagogica)) {
            return false;
        }
        SgPropuestaPedagogica other = (SgPropuestaPedagogica) object;
        if ((this.ppePk == null && other.ppePk != null) || (this.ppePk != null && !this.ppePk.equals(other.ppePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPropuestaPedagogica[ ppePk=" + ppePk + " ]";
    }
    
}
