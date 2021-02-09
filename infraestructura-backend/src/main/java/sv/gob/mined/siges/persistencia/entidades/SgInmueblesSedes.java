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
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumEstado;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipoAbastecimiento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPropietariosTerreno;
import sv.gob.mined.siges.persistencia.entidades.centros.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.centros.SgSedeLite;

@Entity
@Table(name = "sg_inmuebles_sedes", schema = Constantes.SCHEMA_INFRAESTRUCTURA, uniqueConstraints = {
    @UniqueConstraint(name = "tis_codigo_uk", columnNames = {"tis_codigo"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tisPk", scope = SgInmueblesSedes.class)
@Audited
public class SgInmueblesSedes implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    // se usa como código
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tis_pk")
    private Long tisPk;

    // no se usa 
    @Size(max = 10)
    @Column(name = "tis_codigo", length = 10)
    @AtributoCodigo
    private String tisCodigo;

    // autogenerado por el sistema (reemplaza al tisCodigo)
    @Column(name = "tis_num_correlativo")
    private Long tisNumeroCorrelativo;

    @Size(max = 1000)
    @Column(name = "tis_observaciones", length = 1000)
    private String tisObservaciones;

    @Column(name = "tis_terreno_principal")
    private Boolean tisTerrenoPrincipal;

    @Column(name = "tis_activo_fijo")
    private Boolean tisActivoFijo;

    @Column(name = "tis_propietario")
    private Boolean tisPropietario; //Propietario es MINED

    @JoinColumn(name = "tis_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSedeLite tisSedeFk;

    @JoinColumn(name = "tis_direccion_fk", referencedColumnName = "dir_pk")
    @OneToOne(cascade = CascadeType.ALL)
    private SgDireccion tisDireccion;

    @Size(max = 45)
    @Column(name = "tis_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tisUltModUsuario;

    @Column(name = "tis_area_total")
    private BigDecimal tisAreaTotal;

    @Column(name = "tis_area_disponible")
    private BigDecimal tisAreaDisponible;

    @Column(name = "tis_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tisUltModFecha;

    @Column(name = "tis_version")
    @Version
    private Integer tisVersion;

    @OneToMany(mappedBy = "ivuInmueblesSedesFK", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgInmueblesVulnerabilidades> ivuInmueblesSede;

    @OneToMany(mappedBy = "rieInmuebleSede")
    private List<SgRelInmuebleEspacioComun> tisRelInmuebleEspacioComun;

    @OneToMany(mappedBy = "risInmuebleSede")
    private List<SgRelInmuebleServicioBasico> tisServicioBasico;

    @JoinColumn(name = "tis_estado_inmueble_fk", referencedColumnName = "ein_pk")
    @ManyToOne
    private SgEstadoInmueble tisEstadoInmueble;

    @JoinColumn(name = "tis_propietario_terreno_fk", referencedColumnName = "pdt_pk")
    @ManyToOne
    private SgPropietariosTerreno tisPropietariosTerreno;

    @Column(name = "tis_detalle_propietario")
    private String tisDetallePropietario;

    @JoinColumn(name = "tis_fuente_financiamiento_fk", referencedColumnName = "ffi_pk")
    @ManyToOne
    private SgFuenteFinanciamiento tisFuenteFinanciamiento;

    @Column(name = "tis_valor_adquisicion")
    private BigDecimal tisValorAdquisicion;

    @Column(name = "tis_fecha_adquisicion")
    private LocalDate tisFechaAdquisicion;

    @JoinColumn(name = "tis_proceso_legalizacion_fk", referencedColumnName = "ple_pk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgProcesoLegalizacion tisProcesoLegalizacion;

    @Column(name = "tis_tiene_transformador")
    private Boolean tisTieneTransformador;

    @Column(name = "tis_capacidad_transformador")
    private Integer tisCapacidadTransformador;

    @Column(name = "tis_condiciones_instalaciones_elec")
    @Enumerated(value = EnumType.STRING)
    private EnumEstado tisCondicionesInstalacionesElec;

    @Column(name = "tis_posee_area_acopio")
    private Boolean tisPoseeAreaAcopio;

    @Column(name = "tis_condiciones_acceso")
    private String tisCondicionesAcceso;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_inmueble_tipo_abastecimiento",
            schema = Constantes.SCHEMA_INFRAESTRUCTURA,
            joinColumns = @JoinColumn(name = "tis_pk"),
            inverseJoinColumns = @JoinColumn(name = "tab_pk"))
    private List<SgInfTipoAbastecimiento> tisTipoAbastecimiento;

    @Column(name = "tis_pertenece_sede")
    private Boolean tisPerteneceSede;

    @JoinColumn(name = "tis_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas tisAfUnidadAdministrativa;

    @Column(name = "tis_existe_otras_sedes_unidad_admi")
    private Boolean tisExisteOtrasSedesUnidadAdmi;

    @OneToMany(mappedBy = "riuInmuebleFk")
    private List<SgRelInmuebleUnidadResp> tisUnidadesResponsables;

    public SgInmueblesSedes() {
    }

    public SgInmueblesSedes(Long tisPk, Integer tisVersion) {
        this.tisPk = tisPk;
        this.tisVersion = tisVersion;
    }

    public Long getTisPk() {
        return tisPk;
    }

    public void setTisPk(Long tisPk) {
        this.tisPk = tisPk;
    }

    public Boolean getTisActivoFijo() {
        return tisActivoFijo;
    }

    public void setTisActivoFijo(Boolean tisActivoFijo) {
        this.tisActivoFijo = tisActivoFijo;
    }

    public String getTisObservaciones() {
        return tisObservaciones;
    }

    public void setTisObservaciones(String tisObservaciones) {
        this.tisObservaciones = tisObservaciones;
    }

    public Boolean getTisTerrenoPrincipal() {
        return tisTerrenoPrincipal;
    }

    public void setTisTerrenoPrincipal(Boolean tisTerrenoPrincipal) {
        this.tisTerrenoPrincipal = tisTerrenoPrincipal;
    }

    public Boolean getTisPropietario() {
        return tisPropietario;
    }

    public void setTisPropietario(Boolean tisPropietario) {
        this.tisPropietario = tisPropietario;
    }

    public Long getTisNumeroCorrelativo() {
        return tisNumeroCorrelativo;
    }

    public void setTisNumeroCorrelativo(Long tisNumeroCorrelativo) {
        this.tisNumeroCorrelativo = tisNumeroCorrelativo;
    }

    public SgSedeLite getTisSedeFk() {
        return tisSedeFk;
    }

    public void setTisSedeFk(SgSedeLite tisSedeFk) {
        this.tisSedeFk = tisSedeFk;
    }

    public String getTisUltModUsuario() {
        return tisUltModUsuario;
    }

    public void setTisUltModUsuario(String tisUltModUsuario) {
        this.tisUltModUsuario = tisUltModUsuario;
    }

    public BigDecimal getTisAreaTotal() {
        return tisAreaTotal;
    }

    public void setTisAreaTotal(BigDecimal tisAreaTotal) {
        this.tisAreaTotal = tisAreaTotal;
    }

    public BigDecimal getTisAreaDisponible() {
        return tisAreaDisponible;
    }

    public void setTisAreaDisponible(BigDecimal tisAreaDisponible) {
        this.tisAreaDisponible = tisAreaDisponible;
    }

    public LocalDateTime getTisUltModFecha() {
        return tisUltModFecha;
    }

    public void setTisUltModFecha(LocalDateTime tisUltModFecha) {
        this.tisUltModFecha = tisUltModFecha;
    }

    public Integer getTisVersion() {
        return tisVersion;
    }

    public void setTisVersion(Integer tisVersion) {
        this.tisVersion = tisVersion;
    }

    public List<SgInmueblesVulnerabilidades> getIvuInmueblesSede() {
        return ivuInmueblesSede;
    }

    public void setIvuInmueblesSede(List<SgInmueblesVulnerabilidades> ivuInmueblesSede) {
        this.ivuInmueblesSede = ivuInmueblesSede;
    }

    public String getTisCodigo() {
        return tisCodigo;
    }

    public void setTisCodigo(String tisCodigo) {
        this.tisCodigo = tisCodigo;
    }

    public SgDireccion getTisDireccion() {
        return tisDireccion;
    }

    public void setTisDireccion(SgDireccion tisDireccion) {
        this.tisDireccion = tisDireccion;
    }

    public SgEstadoInmueble getTisEstadoInmueble() {
        return tisEstadoInmueble;
    }

    public void setTisEstadoInmueble(SgEstadoInmueble tisEstadoInmueble) {
        this.tisEstadoInmueble = tisEstadoInmueble;
    }

    public List<SgRelInmuebleEspacioComun> getTisRelInmuebleEspacioComun() {
        return tisRelInmuebleEspacioComun;
    }

    public void setTisRelInmuebleEspacioComun(List<SgRelInmuebleEspacioComun> tisRelInmuebleEspacioComun) {
        this.tisRelInmuebleEspacioComun = tisRelInmuebleEspacioComun;
    }

    public List<SgRelInmuebleServicioBasico> getTisServicioBasico() {
        return tisServicioBasico;
    }

    public void setTisServicioBasico(List<SgRelInmuebleServicioBasico> tisServicioBasico) {
        this.tisServicioBasico = tisServicioBasico;
    }

    public SgPropietariosTerreno getTisPropietariosTerreno() {
        return tisPropietariosTerreno;
    }

    public void setTisPropietariosTerreno(SgPropietariosTerreno tisPropietariosTerreno) {
        this.tisPropietariosTerreno = tisPropietariosTerreno;
    }

    public String getTisDetallePropietario() {
        return tisDetallePropietario;
    }

    public void setTisDetallePropietario(String tisDetallePropietario) {
        this.tisDetallePropietario = tisDetallePropietario;
    }

    public SgFuenteFinanciamiento getTisFuenteFinanciamiento() {
        return tisFuenteFinanciamiento;
    }

    public void setTisFuenteFinanciamiento(SgFuenteFinanciamiento tisFuenteFinanciamiento) {
        this.tisFuenteFinanciamiento = tisFuenteFinanciamiento;
    }

    public BigDecimal getTisValorAdquisicion() {
        return tisValorAdquisicion;
    }

    public void setTisValorAdquisicion(BigDecimal tisValorAdquisicion) {
        this.tisValorAdquisicion = tisValorAdquisicion;
    }

    public LocalDate getTisFechaAdquisicion() {
        return tisFechaAdquisicion;
    }

    public void setTisFechaAdquisicion(LocalDate tisFechaAdquisicion) {
        this.tisFechaAdquisicion = tisFechaAdquisicion;
    }

    public SgProcesoLegalizacion getTisProcesoLegalizacion() {
        return tisProcesoLegalizacion;
    }

    public void setTisProcesoLegalizacion(SgProcesoLegalizacion tisProcesoLegalizacion) {
        this.tisProcesoLegalizacion = tisProcesoLegalizacion;
    }

    public Boolean getTisTieneTransformador() {
        return tisTieneTransformador;
    }

    public void setTisTieneTransformador(Boolean tisTieneTransformador) {
        this.tisTieneTransformador = tisTieneTransformador;
    }

    public Integer getTisCapacidadTransformador() {
        return tisCapacidadTransformador;
    }

    public void setTisCapacidadTransformador(Integer tisCapacidadTransformador) {
        this.tisCapacidadTransformador = tisCapacidadTransformador;
    }

    public EnumEstado getTisCondicionesInstalacionesElec() {
        return tisCondicionesInstalacionesElec;
    }

    public void setTisCondicionesInstalacionesElec(EnumEstado tisCondicionesInstalacionesElec) {
        this.tisCondicionesInstalacionesElec = tisCondicionesInstalacionesElec;
    }

    public Boolean getTisPoseeAreaAcopio() {
        return tisPoseeAreaAcopio;
    }

    public void setTisPoseeAreaAcopio(Boolean tisPoseeAreaAcopio) {
        this.tisPoseeAreaAcopio = tisPoseeAreaAcopio;
    }

    public String getTisCondicionesAcceso() {
        return tisCondicionesAcceso;
    }

    public void setTisCondicionesAcceso(String tisCondicionesAcceso) {
        this.tisCondicionesAcceso = tisCondicionesAcceso;
    }

    public List<SgInfTipoAbastecimiento> getTisTipoAbastecimiento() {
        return tisTipoAbastecimiento;
    }

    public void setTisTipoAbastecimiento(List<SgInfTipoAbastecimiento> tisTipoAbastecimiento) {
        this.tisTipoAbastecimiento = tisTipoAbastecimiento;
    }

    public Boolean getTisPerteneceSede() {
        return tisPerteneceSede;
    }

    public void setTisPerteneceSede(Boolean tisPerteneceSede) {
        this.tisPerteneceSede = tisPerteneceSede;
    }

    public SgAfUnidadesAdministrativas getTisAfUnidadAdministrativa() {
        return tisAfUnidadAdministrativa;
    }

    public void setTisAfUnidadAdministrativa(SgAfUnidadesAdministrativas tisAfUnidadAdministrativa) {
        this.tisAfUnidadAdministrativa = tisAfUnidadAdministrativa;
    }

    public Boolean getTisExisteOtrasSedesUnidadAdmi() {
        return tisExisteOtrasSedesUnidadAdmi;
    }

    public void setTisExisteOtrasSedesUnidadAdmi(Boolean tisExisteOtrasSedesUnidadAdmi) {
        this.tisExisteOtrasSedesUnidadAdmi = tisExisteOtrasSedesUnidadAdmi;
    }

    public List<SgRelInmuebleUnidadResp> getTisUnidadesResponsables() {
        return tisUnidadesResponsables;
    }

    public void setTisUnidadesResponsables(List<SgRelInmuebleUnidadResp> tisUnidadesResponsables) {
        this.tisUnidadesResponsables = tisUnidadesResponsables;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.tisPk);
        return hash;
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
        final SgInmueblesSedes other = (SgInmueblesSedes) obj;
        if (!Objects.equals(this.tisPk, other.tisPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + tisPk + " ]";
    }

    @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisUnidadesResponsables.riuSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisUnidadesResponsables.riuSedeFk.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SECCION.name())) {
            return CriteriaTOUtils.createORTO(
                     CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisUnidadesResponsables.riuSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedServicioEducativo.sduSeccion.secPk", o.getContext()));
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.PERSONA.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tisSedeFk.sedServicioEducativo.sduSeccion.secMatricula.matEstudiante.estPersona.perPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
        }
    }

}
