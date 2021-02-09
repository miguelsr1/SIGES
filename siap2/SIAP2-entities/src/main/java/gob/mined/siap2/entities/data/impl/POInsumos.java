/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_insumo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "po_tipo", discriminatorType = DiscriminatorType.STRING, length = 40)
@DiscriminatorValue("GENERAL")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POInsumos implements Serializable, ManejoLineaBaseTrabajo<POInsumos> {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_p_ins_gen")
    @SequenceGenerator(name = "seq_pog_p_ins_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_p_ins", allocationSize = 1)
    @Column(name = "poi_id")
    protected Integer id;

    @Column(name = "poi_indice")
    private Integer posicion;

    @ManyToOne
    @JoinColumn(name = "poi_act")
    private POActividadBase actividad;

    @Column(name = "poi_fecha_cont")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaContratacion;

    @Lob
    @Column(name = "poi_obs")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "poi_insum")
    private Insumo insumo;

    @Column(name = "poi_no_uaci")
    private Boolean noUACI;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poi_proceso_insumo")
    private ProcesoAdquisicionInsumo procesoInsumo;

    @Column(name = "poi_cant")
    private Integer cantidad;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POMontoFuenteInsumo> montosFuentes;

    @Column(name = "poi_monto_unit", columnDefinition = "Decimal(20,6)")
    private BigDecimal montoUnit;

    @Column(name = "poi_total", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotal;

    @Column(name = "poi_total_reserva_fondos", columnDefinition = "Decimal(20,2)")
    private BigDecimal totalReservaFondos;

    @Column(name = "poi_habilitado")
    private Boolean habilitado;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poi_tdr")
    private TDR tdr;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_fc_anio_po_insumo",
            joinColumns = {
                @JoinColumn(name = "rel_fluj_insumo")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_fluj_caj")}
    )
    private Set<FlujoCajaAnio> flujosDeCajaAnio;

    //la ut a la que pertenece
    @ManyToOne
    @JoinColumn(name = "poa_unidad_tecnica")
    private UnidadTecnica unidadTecnica;

    //al grupo del pac que pertenece el insumo
    @ManyToOne
    @JoinColumn(name = "poi_pac_grupo")
    private PACGrupo pacGrupo;

    @Column(name = "poi_total_certificado", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotalCertificado;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_insumo_cert_val",
            joinColumns = {
                @JoinColumn(name = "rel_insumo_id", referencedColumnName = "poi_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_archivo_id", referencedColumnName = "arc_id")})
    private List<Archivo> archivosCertificacionPresupuestaria;

    @Column(name = "poi_cert_disp_aprobado")
    private Boolean pasoValidacionCertificadoDeDispPresupuestaria;

    @Column(name = "poi_enviado_para_certif")
    private Boolean enviadoParaCertificar;

    @ManyToOne
    @JoinColumn(name = "poi_poa")
    private GeneralPOA poa;

    //tramo al que pertenece el insumo, null en caso de no pertenecer a ningun tramo
    @ManyToOne
    @JoinColumn(name = "poi_tramo")
    private ProyectoAporteTramoTramo tramo;

    @Column(name = "poi_rec_fis_tdr")
    private Boolean recepcionFisicaTDR;

    @Column(name = "poi_monto_comprometido", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoComprometido;

    @Column(name = "poi_monto_pep", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoPep;

    @ManyToOne
    @JoinColumn(name = "poi_modif_contrato_oc")
    private ModificativaContrato modificativa;

    @Transient
    private Boolean duplicar;

    @Transient
    private BigDecimal montoADescomprometer;

    @Column(name = "poi_fech_real_ini_ejec")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaRealInicioEjecucion;

    @Column(name = "poi_duracion")
    private Integer duracion;

    //versionado
    //funciona como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poi_linea_base")
    private POInsumos lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poi_linea_trabajo")
    private POInsumos lineaTrabajo;
    @Column(name = "poi_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    @Transient
    private BigDecimal totalProgramacionFinanciera;

    @Column(name = "poi_monto_reprog", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoReprogramaciones;

    @Column(name = "poi_monto_pep_original", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoPepOriginal;

    @Column(name = "poi_mon_act_rec_apr", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEnActasRecepcionAprobadas;
            
    @Column(name = "poi_monto_quedan", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEnQUEDAN;
    
    @Column(name = "poi_monto_pol_apl", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEnPolizasAplicadas;

    //Auditoria
    @Column(name = "poi_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "poi_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "poi_version")
    @Version
    private Integer version;

    public POInsumos() {
        this.habilitado = Boolean.TRUE;
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    }

    public Set<FlujoCajaAnio> getFlujosDeCajaAnio() {
        return flujosDeCajaAnio;
    }

    public void setFlujosDeCajaAnio(Set<FlujoCajaAnio> flujosDeCajaAnio) {
        this.flujosDeCajaAnio = flujosDeCajaAnio;
    }

    public PACGrupo getPacGrupo() {
        return pacGrupo;
    }

    public void setPacGrupo(PACGrupo pacGrupo) {
        this.pacGrupo = pacGrupo;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public Boolean getPasoValidacionCertificadoDeDispPresupuestaria() {
        return pasoValidacionCertificadoDeDispPresupuestaria;
    }

    public void setPasoValidacionCertificadoDeDispPresupuestaria(Boolean pasoValidacionCertificadoDeDispPresupuestaria) {
        this.pasoValidacionCertificadoDeDispPresupuestaria = pasoValidacionCertificadoDeDispPresupuestaria;
    }

    public Boolean getDuplicar() {
        return duplicar;
    }

    public void setDuplicar(Boolean duplicar) {
        this.duplicar = duplicar;
    }

    public BigDecimal getMontoPep() {
        return montoPep;
    }

    public void setMontoPep(BigDecimal montoPep) {
        this.montoPep = montoPep;
    }

    public List<Archivo> getArchivosCertificacionPresupuestaria() {
        return archivosCertificacionPresupuestaria;
    }

    public void setArchivosCertificacionPresupuestaria(List<Archivo> archivosCertificacionPresupuestaria) {
        this.archivosCertificacionPresupuestaria = archivosCertificacionPresupuestaria;
    }

    public BigDecimal getMontoTotalCertificado() {
        if (montoTotalCertificado == null) {
            montoTotalCertificado = BigDecimal.ZERO;
        }
        return montoTotalCertificado;
    }

    public void setMontoTotalCertificado(BigDecimal montoTotalCertificado) {
        this.montoTotalCertificado = montoTotalCertificado;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Boolean getEnviadoParaCertificar() {
        return enviadoParaCertificar;
    }

    public void setEnviadoParaCertificar(Boolean enviadoParaCertificar) {
        this.enviadoParaCertificar = enviadoParaCertificar;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public POInsumos getLineaBase() {
        return lineaBase;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public void setLineaBase(POInsumos lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POInsumos getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POInsumos lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public List<POMontoFuenteInsumo> getMontosFuentes() {
        return montosFuentes;
    }

    public void setMontosFuentes(List<POMontoFuenteInsumo> montosFuentes) {
        this.montosFuentes = montosFuentes;
    }

    public POActividadBase getActividad() {
        return actividad;
    }

    public BigDecimal getTotalReservaFondos() {
        return totalReservaFondos;
    }

    public void setTotalReservaFondos(BigDecimal totalReservaFondos) {
        this.totalReservaFondos = totalReservaFondos;
    }

    public void setActividad(POActividadBase actividad) {
        this.actividad = actividad;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Boolean getNoUACI() {
        return noUACI;
    }

    public void setNoUACI(Boolean noUACI) {
        this.noUACI = noUACI;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getVersion() {
        return version;
    }

    public TDR getTdr() {
        return tdr;
    }

    public void setTdr(TDR tdr) {
        this.tdr = tdr;
    }

    public GeneralPOA getPoa() {
        return poa;
    }

    public void setPoa(GeneralPOA poa) {
        this.poa = poa;
    }

    public String getActUsuario() {
        return actUsuario;
    }

    public void setActUsuario(String actUsuario) {
        this.actUsuario = actUsuario;
    }

    public Date getActMod() {
        return actMod;
    }

    public void setActMod(Date actMod) {
        this.actMod = actMod;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getMontoUnit() {
        return montoUnit;
    }

    public void setMontoUnit(BigDecimal montoUnit) {
        this.montoUnit = montoUnit;
    }

    public ProcesoAdquisicionInsumo getProcesoInsumo() {
        return procesoInsumo;
    }

    public void setProcesoInsumo(ProcesoAdquisicionInsumo procesoInsumo) {
        this.procesoInsumo = procesoInsumo;
    }

    public ProyectoAporteTramoTramo getTramo() {
        return tramo;
    }

    public void setTramo(ProyectoAporteTramoTramo tramo) {
        this.tramo = tramo;
    }

    public Boolean getRecepcionFisicaTDR() {
        if (recepcionFisicaTDR == null) {
            recepcionFisicaTDR = false;
        }
        return recepcionFisicaTDR;
    }

    public void setRecepcionFisicaTDR(Boolean recepcionFisicaTDR) {
        this.recepcionFisicaTDR = recepcionFisicaTDR;
    }

    public String getNombre() {
        String nombre = "--";
        if (insumo != null) {
            nombre = insumo.getCodigo() + "-" + insumo.getNombre() + "(" + getId() + ")";
        }
        return nombre;
    }

    public ModificativaContrato getModificativa() {
        return modificativa;
    }

    public void setModificativa(ModificativaContrato modificativa) {
        this.modificativa = modificativa;
    }

    public BigDecimal getMontoADescomprometer() {
        return montoADescomprometer;
    }

    public void setMontoADescomprometer(BigDecimal montoADescomprometer) {
        this.montoADescomprometer = montoADescomprometer;
    }

    public BigDecimal getTotalProgramacionFinanciera() {
        return totalProgramacionFinanciera;
    }

    public void setTotalProgramacionFinanciera(BigDecimal totalProgramacionFinanciera) {
        this.totalProgramacionFinanciera = totalProgramacionFinanciera;
    }

    public Date getFechaRealInicioEjecucion() {
        return fechaRealInicioEjecucion;
    }

    public void setFechaRealInicioEjecucion(Date fechaRealInicioEjecucion) {
        this.fechaRealInicioEjecucion = fechaRealInicioEjecucion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getMontoReprogramaciones() {
        return montoReprogramaciones;
    }

    public void setMontoReprogramaciones(BigDecimal montoReprogramaciones) {
        this.montoReprogramaciones = montoReprogramaciones;
    }

    public BigDecimal getMontoPepOriginal() {
        return montoPepOriginal;
    }

    public void setMontoPepOriginal(BigDecimal montoPepOriginal) {
        this.montoPepOriginal = montoPepOriginal;
    }

    public BigDecimal getMontoEnActasRecepcionAprobadas() {
        return montoEnActasRecepcionAprobadas;
    }

    public void setMontoEnActasRecepcionAprobadas(BigDecimal montoEnActasRecepcionAprobadas) {
        this.montoEnActasRecepcionAprobadas = montoEnActasRecepcionAprobadas;
    }

    public BigDecimal getMontoEnQUEDAN() {
        return montoEnQUEDAN;
    }

    public void setMontoEnQUEDAN(BigDecimal montoEnQUEDAN) {
        this.montoEnQUEDAN = montoEnQUEDAN;
    }

    public BigDecimal getMontoEnPolizasAplicadas() {
        return montoEnPolizasAplicadas;
    }

    public void setMontoEnPolizasAplicadas(BigDecimal montoEnPolizasAplicadas) {
        this.montoEnPolizasAplicadas = montoEnPolizasAplicadas;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
            
    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final POInsumos other = (POInsumos) obj;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }

}
