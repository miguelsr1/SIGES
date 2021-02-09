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
import java.math.BigDecimal;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoAula;
import sv.gob.mined.siges.enumerados.EnumTipoAula;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipologiaConstruccion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_aulas", schema=Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aulPk", scope = SgAula.class)
@Audited
public class SgAula implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aul_pk", nullable = false)
    private Long aulPk;

    @Size(max = 45)
    @Column(name = "aul_codigo", length = 45)
    @AtributoCodigo
    private String aulCodigo;

    @Size(max = 255)
    @Column(name = "aul_nombre", length = 255)
    @AtributoNormalizable
    private String aulNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "aul_nombre_busqueda", length = 255)
    private String aulNombreBusqueda;

    @Column(name = "aul_nivel")
    private Integer aulNivel;

    @Column(name = "aul_observaciones")
    private String aulObservaciones;
    
    @Column(name = "aul_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoAula aulEstado;
    
    @Column(name = "aul_tipo")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoAula aulTipo;
    
    @Column(name = "aul_area")
    private BigDecimal aulArea;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_aula_tipologia_construccion",
            schema = Constantes.SCHEMA_INFRAESTRUCTURA,
            joinColumns = @JoinColumn(name = "aul_pk"),
            inverseJoinColumns = @JoinColumn(name = "tic_pk"))
    private List<SgInfTipologiaConstruccion> aulTipologiaConstruccion;
    

    @Column(name = "aul_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aulUltModFecha;

    @Size(max = 45)
    @Column(name = "aul_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aulUltModUsuario;

    @Column(name = "aul_version")
    @Version
    private Integer aulVersion;
    
    @JoinColumn(name = "aul_edificio_fk", referencedColumnName = "edi_pk")
    @ManyToOne
    private SgEdificio aulEdificioFk;
    
    @OneToMany(mappedBy = "raeAula")
    private List<SgRelAulaEspacio> aulRelAulaEspacio;
    
    @OneToMany(mappedBy = "rasAula")
    private List<SgRelAulaServicio> aulRelAulaServicio;

    public SgAula() {
    }

    public SgAula(Long aulPk, Integer aulVersion) {
        this.aulPk = aulPk;
        this.aulVersion = aulVersion;
    }   
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.aulNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.aulNombre);
    }

    public Long getAulPk() {
        return aulPk;
    }

    public void setAulPk(Long aulPk) {
        this.aulPk = aulPk;
    }

    public String getAulCodigo() {
        return aulCodigo;
    }

    public void setAulCodigo(String aulCodigo) {
        this.aulCodigo = aulCodigo;
    }

    public String getAulNombre() {
        return aulNombre;
    }

    public void setAulNombre(String aulNombre) {
        this.aulNombre = aulNombre;
    }

    public String getAulNombreBusqueda() {
        return aulNombreBusqueda;
    }

    public void setAulNombreBusqueda(String aulNombreBusqueda) {
        this.aulNombreBusqueda = aulNombreBusqueda;
    }

    public Integer getAulNivel() {
        return aulNivel;
    }

    public void setAulNivel(Integer aulNivel) {
        this.aulNivel = aulNivel;
    }

    public String getAulObservaciones() {
        return aulObservaciones;
    }

    public void setAulObservaciones(String aulObservaciones) {
        this.aulObservaciones = aulObservaciones;
    }

    public SgEdificio getAulEdificioFk() {
        return aulEdificioFk;
    }

    public void setAulEdificioFk(SgEdificio aulEdificioFk) {
        this.aulEdificioFk = aulEdificioFk;
    }


    public LocalDateTime getAulUltModFecha() {
        return aulUltModFecha;
    }

    public void setAulUltModFecha(LocalDateTime aulUltModFecha) {
        this.aulUltModFecha = aulUltModFecha;
    }

    public String getAulUltModUsuario() {
        return aulUltModUsuario;
    }

    public void setAulUltModUsuario(String aulUltModUsuario) {
        this.aulUltModUsuario = aulUltModUsuario;
    }

    public Integer getAulVersion() {
        return aulVersion;
    }

    public void setAulVersion(Integer aulVersion) {
        this.aulVersion = aulVersion;
    }

    public List<SgRelAulaEspacio> getAulRelAulaEspacio() {
        return aulRelAulaEspacio;
    }

    public void setAulRelAulaEspacio(List<SgRelAulaEspacio> aulRelAulaEspacio) {
        this.aulRelAulaEspacio = aulRelAulaEspacio;
    }

    public List<SgRelAulaServicio> getAulRelAulaServicio() {
        return aulRelAulaServicio;
    }

    public void setAulRelAulaServicio(List<SgRelAulaServicio> aulRelAulaServicio) {
        this.aulRelAulaServicio = aulRelAulaServicio;
    }

    public EnumEstadoAula getAulEstado() {
        return aulEstado;
    }

    public void setAulEstado(EnumEstadoAula aulEstado) {
        this.aulEstado = aulEstado;
    }

    public EnumTipoAula getAulTipo() {
        return aulTipo;
    }

    public void setAulTipo(EnumTipoAula aulTipo) {
        this.aulTipo = aulTipo;
    }

    public BigDecimal getAulArea() {
        return aulArea;
    }

    public void setAulArea(BigDecimal aulArea) {
        this.aulArea = aulArea;
    }

    public List<SgInfTipologiaConstruccion> getAulTipologiaConstruccion() {
        return aulTipologiaConstruccion;
    }

    public void setAulTipologiaConstruccion(List<SgInfTipologiaConstruccion> aulTipologiaConstruccion) {
        this.aulTipologiaConstruccion = aulTipologiaConstruccion;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.aulPk);
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
        final SgAula other = (SgAula) obj;
        if (!Objects.equals(this.aulPk, other.aulPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAula{" + "aulPk=" + aulPk + ", aulCodigo=" + aulCodigo + ", aulNombre=" + aulNombre + ", aulNombreBusqueda=" + aulNombreBusqueda + ", aulUltModFecha=" + aulUltModFecha + ", aulUltModUsuario=" + aulUltModUsuario + ", aulVersion=" + aulVersion + '}';
    }
    
    @Override
    public String securityAmbitCreate() {
        return null;
    }

    

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisSedeFk.sedPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createORTO(
                     CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisSedeFk.sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        }
    }
    
}
