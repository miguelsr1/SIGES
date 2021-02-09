/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Gustavo Cirigliano
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "SS_REPROG_DETALLE")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ReprogramacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rep_det_gen")
    @SequenceGenerator(name = "seq_rep_det_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rep_det", allocationSize = 1)
    @Column(name = "rde_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "RDE_REP_ID")
    private Reprogramacion reprogramacionId;

    @ManyToOne
    @JoinColumn(name = "RDE_POA_GENERAL_ID")
    private GeneralPOA poa;

    @ManyToOne
    @JoinColumn(name = "RDE_POA_LINEA_ID")
    private POLinea poaLinea;
    
    @ManyToOne
    @JoinColumn(name = "RDE_ACT_ID")
    private POActividadBase poaActividad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RDE_INS_ID")
    private POInsumos poaInsumo;

    
    @Column(name = "RDE_NUEVA_ACTIVIDAD")
    private Boolean nuevaActividad;
    
    
    @Column(name = "RDE_NUEVO_INSUMO")
    private Boolean nuevoInsumo;
    
    
    @ManyToOne
    @JoinColumn(name = "RDE_INS_N")
    private Insumo insumoNuevo;

    @Column(name = "RDE_INS_N_OBS")
    @Size(max = 1000)
    private String insumoNuevoObservaciones;

    
    @Column(name = "RDE_INS_N_CANTIDAD")
    private Integer insumoNuevoCantidad;

    
    @Column(name = "RDE_INS_N__UNITARIO")
    private BigDecimal insumoNuevoUnitario;

    @Column(name = "RDE_INS_N__TOTAL")
    private BigDecimal insumoNuevoTotal;

    
    @Column(name = "RDE_INS_N_NO_UACI")
    private Boolean insumoNuevoNoUaci;
    
    
    @Column(name = "RDE_INS_N_FC")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insumoNuevoFechaContratacion;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        schema = Constantes.SCHEMA_SIAP2,
        name = "ss_rel_reprog_fuentes",
        joinColumns = {
            @JoinColumn(name = "rel_reprog")},
        inverseJoinColumns = {
            @JoinColumn(name = "rel_fuente")}
    )
    private List<POMontoFuenteInsumo> insumoNuevoMontosFuentes;
    
    
    @ManyToOne
    @JoinColumn(name = "RDE_INSUMO_NUEVO_TRAMO")
    private ProyectoAporteTramoTramo insumoNuevoTramo;
    
    
    
    @Column(name = "RDE_NUEVA_ACTIVIDAD_AC")
    private String actividadNuevaAccionCentral;

    
    @ManyToOne
    @JoinColumn(name = "RDE_NUEVA_ACTIVIDAD_PROY")
    private ActividadPOProyecto actividadNuevaProyecto;
    
    
    @ManyToOne
    @JoinColumn(name = "RDE_NUEVA_ACTIVIDAD_NP")
    private ActividadAsignacionNP actividadNuevaAsigNP;
    
    
           
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RDE_PAC_ID")
    private PACGrupo grupoPAC;

    @Column(name = "RDE_MES1")
    private BigDecimal rdeMes1;
    @Column(name = "RDE_MES2")
    private BigDecimal rdeMes2;
    @Column(name = "RDE_MES3")
    private BigDecimal rdeMes3;
    @Column(name = "RDE_MES4")
    private BigDecimal rdeMes4;
    @Column(name = "RDE_MES5")
    private BigDecimal rdeMes5;
    @Column(name = "RDE_MES6")
    private BigDecimal rdeMes6;
    @Column(name = "RDE_MES7")
    private BigDecimal rdeMes7;
    @Column(name = "RDE_MES8")
    private BigDecimal rdeMes8;
    @Column(name = "RDE_MES9")
    private BigDecimal rdeMes9;
    @Column(name = "RDE_MES10")
    private BigDecimal rdeMes10;
    @Column(name = "RDE_MES11")
    private BigDecimal rdeMes11;
    @Column(name = "RDE_MES12")
    private BigDecimal rdeMes12;

    @Transient
    private BigDecimal diferencia;
    @Transient
    private Set<UnidadTecnica> conjUnidadesTecnicas;

    //Auditoria
    @Column(name = "RDE_ULTMOD_USU")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "RDE_ULTMOD")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "RDE_VERSION")
    @Version
    private Integer version;

    @PostConstruct
    public void init() {

    }

    public ReprogramacionDetalle() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Reprogramacion getReprogramacionId() {
        return reprogramacionId;
    }

    public void setReprogramacionId(Reprogramacion reprogramacionId) {
        this.reprogramacionId = reprogramacionId;
    }

    public GeneralPOA getPoa() {
        return poa;
    }

    public void setPoa(GeneralPOA poa) {
        this.poa = poa;
    }

    public POLinea getPoaLinea() {
        return poaLinea;
    }
    

    public void setPoaLinea(POLinea poaLinea) {
        this.poaLinea = poaLinea;
    }

    public void setActividadNuevaProyecto(ActividadPOProyecto actividadNuevaProyecto) {
        this.actividadNuevaProyecto = actividadNuevaProyecto;
    }

    public List<POMontoFuenteInsumo> getInsumoNuevoMontosFuentes() {
        return insumoNuevoMontosFuentes;
    }

    public void setInsumoNuevoMontosFuentes(List<POMontoFuenteInsumo> insumoNuevoMontosFuentes) {
        this.insumoNuevoMontosFuentes = insumoNuevoMontosFuentes;
    }

    public ProyectoAporteTramoTramo getInsumoNuevoTramo() {
        return insumoNuevoTramo;
    }

    public void setInsumoNuevoTramo(ProyectoAporteTramoTramo insumoNuevoTramo) {
        this.insumoNuevoTramo = insumoNuevoTramo;
    }
    
    public POActividadBase getPoaActividad() {
        return poaActividad;
    }

    public void setPoaActividad(POActividadBase poaActividad) {
        this.poaActividad = poaActividad;
    }

    public POInsumos getPoaInsumo() {
        return poaInsumo;
    }

    public void setPoaInsumo(POInsumos poaInsumo) {
        this.poaInsumo = poaInsumo;
    }

    public String getInsumoNuevoObservaciones() {
        return insumoNuevoObservaciones;
    }

    public void setInsumoNuevoObservaciones(String insumoNuevoObservaciones) {
        this.insumoNuevoObservaciones = insumoNuevoObservaciones;
    }

    public Integer getInsumoNuevoCantidad() {
        return insumoNuevoCantidad;
    }

    public void setInsumoNuevoCantidad(Integer insumoNuevoCantidad) {
        this.insumoNuevoCantidad = insumoNuevoCantidad;
    }

    public BigDecimal getInsumoNuevoUnitario() {
        return insumoNuevoUnitario;
    }

    public void setInsumoNuevoUnitario(BigDecimal insumoNuevoUnitario) {
        this.insumoNuevoUnitario = insumoNuevoUnitario;
    }

    public BigDecimal getInsumoNuevoTotal() {
        return insumoNuevoTotal;
    }

    public void setInsumoNuevoTotal(BigDecimal insumoNuevoTotal) {
        this.insumoNuevoTotal = insumoNuevoTotal;
    }

    public Boolean getInsumoNuevoNoUaci() {
        return insumoNuevoNoUaci;
    }

    public void setInsumoNuevoNoUaci(Boolean insumoNuevoNoUaci) {
        this.insumoNuevoNoUaci = insumoNuevoNoUaci;
    }

    public Date getInsumoNuevoFechaContratacion() {
        return insumoNuevoFechaContratacion;
    }

    public void setInsumoNuevoFechaContratacion(Date insumoNuevoFechaContratacion) {
        this.insumoNuevoFechaContratacion = insumoNuevoFechaContratacion;
    }

    public Boolean getNuevaActividad() {
        return nuevaActividad;
    }

    public void setNuevaActividad(Boolean nuevaActividad) {
        this.nuevaActividad = nuevaActividad;
    }

    public ActividadPOProyecto getActividadNuevaProyecto() {
        return actividadNuevaProyecto;
    }
    
    

    public Boolean getNuevoInsumo() {
        return nuevoInsumo;
    }

    public void setNuevoInsumo(Boolean nuevoInsumo) {
        this.nuevoInsumo = nuevoInsumo;
    }

    public String getActividadNuevaAccionCentral() {
        return actividadNuevaAccionCentral;
    }

    public void setActividadNuevaAccionCentral(String actividadNuevaAccionCentral) {
        this.actividadNuevaAccionCentral = actividadNuevaAccionCentral;
    }

    public ActividadAsignacionNP getActividadNuevaAsigNP() {
        return actividadNuevaAsigNP;
    }

    public void setActividadNuevaAsigNP(ActividadAsignacionNP actividadNuevaAsigNP) {
        this.actividadNuevaAsigNP = actividadNuevaAsigNP;
    }

    
    
    
    

    public BigDecimal getRdeMes1() {
        return rdeMes1;
    }

    public void setRdeMes1(BigDecimal rdeMes1) {
        this.rdeMes1 = rdeMes1;
    }

    public BigDecimal getRdeMes2() {
        return rdeMes2;
    }

    public void setRdeMes2(BigDecimal rdeMes2) {
        this.rdeMes2 = rdeMes2;
    }

    public BigDecimal getRdeMes3() {
        return rdeMes3;
    }

    public void setRdeMes3(BigDecimal rdeMes3) {
        this.rdeMes3 = rdeMes3;
    }

    public BigDecimal getRdeMes4() {
        return rdeMes4;
    }

    public void setRdeMes4(BigDecimal rdeMes4) {
        this.rdeMes4 = rdeMes4;
    }

    public BigDecimal getRdeMes5() {
        return rdeMes5;
    }

    public void setRdeMes5(BigDecimal rdeMes5) {
        this.rdeMes5 = rdeMes5;
    }

    public BigDecimal getRdeMes6() {
        return rdeMes6;
    }

    public void setRdeMes6(BigDecimal rdeMes6) {
        this.rdeMes6 = rdeMes6;
    }

    public BigDecimal getRdeMes7() {
        return rdeMes7;
    }

    public void setRdeMes7(BigDecimal rdeMes7) {
        this.rdeMes7 = rdeMes7;
    }

    public BigDecimal getRdeMes8() {
        return rdeMes8;
    }

    public void setRdeMes8(BigDecimal rdeMes8) {
        this.rdeMes8 = rdeMes8;
    }

    public BigDecimal getRdeMes9() {
        return rdeMes9;
    }

    public void setRdeMes9(BigDecimal rdeMes9) {
        this.rdeMes9 = rdeMes9;
    }

    public BigDecimal getRdeMes10() {
        return rdeMes10;
    }

    public void setRdeMes10(BigDecimal rdeMes10) {
        this.rdeMes10 = rdeMes10;
    }

    public BigDecimal getRdeMes11() {
        return rdeMes11;
    }

    public void setRdeMes11(BigDecimal rdeMes11) {
        this.rdeMes11 = rdeMes11;
    }

    public BigDecimal getRdeMes12() {
        return rdeMes12;
    }

    public void setRdeMes12(BigDecimal rdeMes12) {
        this.rdeMes12 = rdeMes12;
    }

   

    public void setPoa(POAConActividades poa) {
        this.poa = poa;
    }

   
    public BigDecimal getDiferencia() {
        diferencia = this.insumoNuevoTotal;
        if (this.poaInsumo != null) {
            diferencia = this.insumoNuevoTotal.subtract(this.poaInsumo.getMontoTotal());
        }
        return diferencia;
    }

    public void setDiferencia(BigDecimal diferencia) {

        this.diferencia = diferencia;
    }

    
    public Insumo getInsumoNuevo() {
        return insumoNuevo;
    }

    public void setInsumoNuevo(Insumo insumoNuevo) {
        this.insumoNuevo = insumoNuevo;
    }

   

    public Set<UnidadTecnica> getConjUnidadesTecnicas() {

        return conjUnidadesTecnicas;
    }

    public void setConjUnidadesTecnicas(Set<UnidadTecnica> conjUnidadesTecnicas) {
        this.conjUnidadesTecnicas = conjUnidadesTecnicas;
    }

    public PACGrupo getGrupoPAC() {
        return grupoPAC;
    }

    public void setGrupoPAC(PACGrupo grupoPAC) {
        this.grupoPAC = grupoPAC;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final ReprogramacionDetalle other = (ReprogramacionDetalle) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }

    @Override
    public String toString() {
        return "gob.mined.siap2.entities.data.impl.Reprogramacion[ id=" + id + " ]";
    }

}
