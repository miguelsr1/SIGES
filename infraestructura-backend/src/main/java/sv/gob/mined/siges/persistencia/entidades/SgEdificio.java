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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoConstruccion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_edificios", schema=Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ediPk", scope = SgEdificio.class)
@Audited
public class SgEdificio implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "edi_pk", nullable = false)
    private Long ediPk;

    @Size(max = 45)
    @Column(name = "edi_codigo", length = 45)
    @AtributoCodigo
    private String ediCodigo;
    
    @Size(max = 255)
    @Column(name = "edi_nombre", length = 255)
    @AtributoNormalizable
    private String ediNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "edi_nombre_busqueda", length = 255)
    private String ediNombreBusqueda;

    @Column(name = "edi_inversion")
    private BigDecimal ediInversion;
    
    @Column(name = "edi_cantidad_niveles")
    private Integer ediCantidadNiveles;
    
    @Column(name = "edi_area")
    private BigDecimal ediArea;
    
    @Column(name = "edi_latitud")
    private Double ediLatitud;
    
    @Column(name = "edi_longitud")
    private Double ediLongitud;
    
    @Column(name = "edi_observaciones")
    private String ediObservaciones;

    @Column(name = "edi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ediUltModFecha;

    @Size(max = 45)
    @Column(name = "edi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ediUltModUsuario;

    @Column(name = "edi_version")
    @Version
    private Integer ediVersion;
    
    @JoinColumn(name = "edi_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes ediInmuebleSedeFk;
    
    @JoinColumn(name = "edi_tipo_construccion", referencedColumnName = "tco_pk")
    @ManyToOne
    private SgTipoConstruccion ediTipoConstruccion;
    
    @OneToMany(mappedBy = "reeEdificio")
    private List<SgRelEdificioEspacio> ediRelEdificioEspacio;
    
    @OneToMany(mappedBy = "resEdificio")
    private List<SgRelEdificioServicio> ediRelEdificioServicio;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_edificio_rel_inmueble_unidad_resp",
            schema = Constantes.SCHEMA_INFRAESTRUCTURA,
            joinColumns = @JoinColumn(name = "edi_pk"),
            inverseJoinColumns = @JoinColumn(name = "riu_pk"))
    private List<SgRelInmuebleUnidadResp> ediRelInmuebleUnidadResp;

    public SgEdificio() {
    }

    public SgEdificio(Long ediPk, Integer ediVersion) {
        this.ediPk = ediPk;
        this.ediVersion = ediVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ediNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ediNombre);
    }

    public Long getEdiPk() {
        return ediPk;
    }

    public void setEdiPk(Long ediPk) {
        this.ediPk = ediPk;
    }

    public String getEdiCodigo() {
        return ediCodigo;
    }

    public void setEdiCodigo(String ediCodigo) {
        this.ediCodigo = ediCodigo;
    }

    public String getEdiNombre() {
        return ediNombre;
    }

    public void setEdiNombre(String ediNombre) {
        this.ediNombre = ediNombre;
    }

    public String getEdiNombreBusqueda() {
        return ediNombreBusqueda;
    }

    public void setEdiNombreBusqueda(String ediNombreBusqueda) {
        this.ediNombreBusqueda = ediNombreBusqueda;
    }
    

    public BigDecimal getEdiInversion() {
        return ediInversion;
    }

    public void setEdiInversion(BigDecimal ediInversion) {
        this.ediInversion = ediInversion;
    }

    public Integer getEdiCantidadNiveles() {
        return ediCantidadNiveles;
    }

    public void setEdiCantidadNiveles(Integer ediCantidadNiveles) {
        this.ediCantidadNiveles = ediCantidadNiveles;
    }

    public BigDecimal getEdiArea() {
        return ediArea;
    }

    public void setEdiArea(BigDecimal ediArea) {
        this.ediArea = ediArea;
    }

    public Double getEdiLatitud() {
        return ediLatitud;
    }

    public void setEdiLatitud(Double ediLatitud) {
        this.ediLatitud = ediLatitud;
    }

    public Double getEdiLongitud() {
        return ediLongitud;
    }

    public void setEdiLongitud(Double ediLongitud) {
        this.ediLongitud = ediLongitud;
    }

    public String getEdiObservaciones() {
        return ediObservaciones;
    }

    public void setEdiObservaciones(String ediObservaciones) {
        this.ediObservaciones = ediObservaciones;
    }

    public SgInmueblesSedes getEdiInmuebleSedeFk() {
        return ediInmuebleSedeFk;
    }

    public void setEdiInmuebleSedeFk(SgInmueblesSedes ediInmuebleSedeFk) {
        this.ediInmuebleSedeFk = ediInmuebleSedeFk;
    }

    public SgTipoConstruccion getEdiTipoConstruccion() {
        return ediTipoConstruccion;
    }

    public void setEdiTipoConstruccion(SgTipoConstruccion ediTipoConstruccion) {
        this.ediTipoConstruccion = ediTipoConstruccion;
    }

 
    public LocalDateTime getEdiUltModFecha() {
        return ediUltModFecha;
    }

    public void setEdiUltModFecha(LocalDateTime ediUltModFecha) {
        this.ediUltModFecha = ediUltModFecha;
    }

    public String getEdiUltModUsuario() {
        return ediUltModUsuario;
    }

    public void setEdiUltModUsuario(String ediUltModUsuario) {
        this.ediUltModUsuario = ediUltModUsuario;
    }

    public Integer getEdiVersion() {
        return ediVersion;
    }

    public void setEdiVersion(Integer ediVersion) {
        this.ediVersion = ediVersion;
    }

    public List<SgRelEdificioEspacio> getEdiRelEdificioEspacio() {
        return ediRelEdificioEspacio;
    }

    public void setEdiRelEdificioEspacio(List<SgRelEdificioEspacio> ediRelEdificioEspacio) {
        this.ediRelEdificioEspacio = ediRelEdificioEspacio;
    }

    public List<SgRelEdificioServicio> getEdiRelEdificioServicio() {
        return ediRelEdificioServicio;
    }

    public void setEdiRelEdificioServicio(List<SgRelEdificioServicio> ediRelEdificioServicio) {
        this.ediRelEdificioServicio = ediRelEdificioServicio;
    }

    public List<SgRelInmuebleUnidadResp> getEdiRelInmuebleUnidadResp() {
        return ediRelInmuebleUnidadResp;
    }

    public void setEdiRelInmuebleUnidadResp(List<SgRelInmuebleUnidadResp> ediRelInmuebleUnidadResp) {
        this.ediRelInmuebleUnidadResp = ediRelInmuebleUnidadResp;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.ediPk);
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
        final SgEdificio other = (SgEdificio) obj;
        if (!Objects.equals(this.ediPk, other.ediPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEdificio{" + "ediPk=" + ediPk + ", ediCodigo=" + ediCodigo +", ediUltModFecha=" + ediUltModFecha + ", ediUltModUsuario=" + ediUltModUsuario + ", ediVersion=" + ediVersion + '}';
    }
    
    @Override
    public String securityAmbitCreate() {
        return null;
    }

    
        @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisSedeFk.sedPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createORTO(
                     CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisUnidadesResponsables.riuSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisSedeFk.sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        }
    }

}
