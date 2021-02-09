/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

@Entity
@Table(name = "sg_control_asistencia_personal_cabezal", uniqueConstraints = {
    @UniqueConstraint(name = "cpc_fecha_sede_uk", columnNames = {"cpc_fecha","cpc_sede_fk"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpcPk", scope = SgControlAsistenciaPersonalCabezal.class)
public class SgControlAsistenciaPersonalCabezal implements Serializable , DataSecurity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpc_pk")
    private Long cpcPk;
    
    @JoinColumn(name = "cpc_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cpcSede;
    
    @Column(name = "cpc_fecha")
    private LocalDate cpcFecha;
    
    @Column(name = "cpc_personal_en_lista")
    private Integer cpcPersonalEnLista;
    
    @Column(name = "cpc_personal_presente")
    private Integer cpcPersonalPresente;
    
    @Column(name = "cpc_personal_ausentes_justificados")
    private Integer cpcPersonalAusentesJustificados;
    
    @Column(name = "cpc_personal_ausentes_sin_justificar")
    private Integer cpcPersonalAusentesSinJustificar;
    
    @Column(name = "cpc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cpcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cpc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cpcUltModUsuario;
    
    @Column(name = "cpc_version")
    @Version
    private Integer cpcVersion;
    
    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apeControl", orphanRemoval = true)
    private List<SgAsistenciaPersonal> cpcAsistenciaPersonal;

    public SgControlAsistenciaPersonalCabezal() {
    }

    public SgControlAsistenciaPersonalCabezal(Long cpcPk) {
        this.cpcPk = cpcPk;
    }

    public Long getCpcPk() {
        return cpcPk;
    }

    public void setCpcPk(Long cpcPk) {
        this.cpcPk = cpcPk;
    }

    public SgSede getCpcSede() {
        return cpcSede;
    }

    public void setCpcSede(SgSede cpcSede) {
        this.cpcSede = cpcSede;
    }

    public LocalDate getCpcFecha() {
        return cpcFecha;
    }

    public void setCpcFecha(LocalDate cpcFecha) {
        this.cpcFecha = cpcFecha;
    }

    public Integer getCpcPersonalEnLista() {
        return cpcPersonalEnLista;
    }

    public void setCpcPersonalEnLista(Integer cpcPersonalEnLista) {
        this.cpcPersonalEnLista = cpcPersonalEnLista;
    }

    public Integer getCpcPersonalPresente() {
        return cpcPersonalPresente;
    }

    public void setCpcPersonalPresente(Integer cpcPersonalPresente) {
        this.cpcPersonalPresente = cpcPersonalPresente;
    }

    public Integer getCpcPersonalAusentesJustificados() {
        return cpcPersonalAusentesJustificados;
    }

    public void setCpcPersonalAusentesJustificados(Integer cpcPersonalAusentesJustificados) {
        this.cpcPersonalAusentesJustificados = cpcPersonalAusentesJustificados;
    }

    public Integer getCpcPersonalAusentesSinJustificar() {
        return cpcPersonalAusentesSinJustificar;
    }

    public void setCpcPersonalAusentesSinJustificar(Integer cpcPersonalAusentesSinJustificar) {
        this.cpcPersonalAusentesSinJustificar = cpcPersonalAusentesSinJustificar;
    }

    public LocalDateTime getCpcUltModFecha() {
        return cpcUltModFecha;
    }

    public void setCpcUltModFecha(LocalDateTime cpcUltModFecha) {
        this.cpcUltModFecha = cpcUltModFecha;
    }

    public String getCpcUltModUsuario() {
        return cpcUltModUsuario;
    }

    public void setCpcUltModUsuario(String cpcUltModUsuario) {
        this.cpcUltModUsuario = cpcUltModUsuario;
    }

    public Integer getCpcVersion() {
        return cpcVersion;
    }

    public void setCpcVersion(Integer cpcVersion) {
        this.cpcVersion = cpcVersion;
    }

    public List<SgAsistenciaPersonal> getCpcAsistenciaPersonal() {
        return cpcAsistenciaPersonal;
    }

    public void setCpcAsistenciaPersonal(List<SgAsistenciaPersonal> cpcAsistenciaPersonal) {
        this.cpcAsistenciaPersonal = cpcAsistenciaPersonal;
    }


    
    @Override
    public String securityAmbitCreate() {
       return "cpcSede";
    }
    
    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SISTEMA_INTEGRADO.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedSistemas.sinPk.sinPk", o.getContext()); 
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())){
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())){
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PARTICION_SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN_SUBQUERY, "cpcSede.sedPk", o.getRegla());
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpcPk", -1L);
        } 
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpcPk != null ? cpcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgControlAsistenciaPersonalCabezal)) {
            return false;
        }
        SgControlAsistenciaPersonalCabezal other = (SgControlAsistenciaPersonalCabezal) object;
        if ((this.cpcPk == null && other.cpcPk != null) || (this.cpcPk != null && !this.cpcPk.equals(other.cpcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaPersonalCabezal[ cpcPk=" + cpcPk + " ]";
    }
    
}
