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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_presupuesto_escolar", uniqueConstraints = {
    @UniqueConstraint(name = "pes_codigo_uk", columnNames = {"pes_codigo"})
    ,
    @UniqueConstraint(name = "pes_nombre_uk", columnNames = {"pes_nombre"})}, schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "pesPk", scope = SgPresupuestoEscolar.class)
/**
 * Entidad correspondiente al presupuesto escolar.
 */
public class SgPresupuestoEscolar implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pes_pk", nullable = false)
    private Long pesPk;

    @Size(max = 255)
    @Column(name = "pes_nombre", length = 255)
    @AtributoNormalizable
    private String pesNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pes_nombre_busqueda", length = 255)
    private String pesNombreBusqueda;

    @Size(max = 45)
    @AtributoCodigo
    @Column(name = "pes_codigo", length = 45)
    private String pesCodigo;

    @Size(max = 500)
    @Column(name = "pes_descripcion", length = 500)
    private String pesDescripcion;

    @Size(max = 500)
    @Column(name = "pes_observacion", length = 500)
    private String pesObservacion;

    @Column(name = "pes_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumPresupuestoEscolarEstado pesEstado;

    @Column(name = "pes_habilitado")
    private Boolean pesHabilitado;

    @AtributoUltimaModificacion
    @Column(name = "pes_ult_mod_fecha")
    private LocalDateTime pesUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "pes_ult_mod_usuario", length = 45)
    private String pesUltmodUsuario;

    @Column(name = "pes_version")
    @Version
    private Integer pesVersion;

    @JoinColumn(name = "pes_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede pesSedeFk;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "pes_anio_fiscal_fk", referencedColumnName = "ani_id")
    @ManyToOne
    private SgAnioFiscal pesAnioFiscalFk;

    @NotAudited
    @OneToMany(mappedBy = "movPresupuestoFk", fetch = FetchType.LAZY)
    private List<SgMovimientos> pesMovimientos;

    @OneToMany(mappedBy = "docPresupuestoFk", fetch = FetchType.LAZY)
    private List<SgDocumentos> pesDocumentos;

    @Column(name = "pes_edicion")
    private Boolean pesEdicion;

    public SgPresupuestoEscolar() {
    }

    public SgPresupuestoEscolar(Long pesPk) {
        this.pesPk = pesPk;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public String getPesDescripcion() {
        return pesDescripcion;
    }

    public void setPesDescripcion(String pesDescripcion) {
        this.pesDescripcion = pesDescripcion;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public LocalDateTime getPesUltmodFecha() {
        return pesUltmodFecha;
    }

    public void setPesUltmodFecha(LocalDateTime pesUltmodFecha) {
        this.pesUltmodFecha = pesUltmodFecha;
    }

    public String getPesUltmodUsuario() {
        return pesUltmodUsuario;
    }

    public void setPesUltmodUsuario(String pesUltmodUsuario) {
        this.pesUltmodUsuario = pesUltmodUsuario;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
    }

    public SgSede getPesSedeFk() {
        return pesSedeFk;
    }

    public void setPesSedeFk(SgSede pesSedeFk) {
        this.pesSedeFk = pesSedeFk;
    }

    public String getPesNombreBusqueda() {
        return pesNombreBusqueda;
    }

    public void setPesNombreBusqueda(String pesNombreBusqueda) {
        this.pesNombreBusqueda = pesNombreBusqueda;
    }

    public Boolean getPesHabilitado() {
        return pesHabilitado;
    }

    public void setPesHabilitado(Boolean pesHabilitado) {
        this.pesHabilitado = pesHabilitado;
    }

    public SgAnioFiscal getPesAnioFiscalFk() {
        return pesAnioFiscalFk;
    }

    public void setPesAnioFiscalFk(SgAnioFiscal pesAnioFiscalFk) {
        this.pesAnioFiscalFk = pesAnioFiscalFk;
    }

    public List<SgMovimientos> getPesMovimientos() {
        return pesMovimientos;
    }

    public void setPesMovimientos(List<SgMovimientos> pesMovimientos) {
        this.pesMovimientos = pesMovimientos;
    }

    public EnumPresupuestoEscolarEstado getPesEstado() {
        return pesEstado;
    }

    public void setPesEstado(EnumPresupuestoEscolarEstado pesEstado) {
        this.pesEstado = pesEstado;
    }

    public Boolean getPesEdicion() {
        return pesEdicion;
    }

    public void setPesEdicion(Boolean pesEdicion) {
        this.pesEdicion = pesEdicion;
    }

    public String getPesObservacion() {
        return pesObservacion;
    }

    public void setPesObservacion(String pesObservacion) {
        this.pesObservacion = pesObservacion;
    }

    public List<SgDocumentos> getPesDocumentos() {
        return pesDocumentos;
    }

    public void setPesDocumentos(List<SgDocumentos> pesDocumentos) {
        this.pesDocumentos = pesDocumentos;
    }

    // </editor-fold>
    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pesSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pesSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pesPk != null ? pesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPresupuestoEscolar)) {
            return false;
        }
        SgPresupuestoEscolar other = (SgPresupuestoEscolar) object;
        if ((this.pesPk == null && other.pesPk != null) || (this.pesPk != null && !this.pesPk.equals(other.pesPk))) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar[ pesPk=" + pesPk + " ]";
    }
    // </editor-fold>

}
