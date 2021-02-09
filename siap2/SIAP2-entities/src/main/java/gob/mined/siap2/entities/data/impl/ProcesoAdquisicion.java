/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.enums.EstadoProcesoAdq;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoGeneracionContrato;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proceso_adq")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProcesoAdquisicion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proceso_adq_gen")
    @SequenceGenerator(name = "seq_proceso_adq_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proceso_adq", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @Column(name = "pro_secuencia_anio")
    private Integer secuenciaAnio;
    @Column(name = "pro_secuencia_nro")
    private Integer secuenciaNumero;

    //la suma de todos los inumos del proceso
    @Column(name = "pro_total", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotal;

    //se actualiza a medida que se emiten los quedan
    @Column(name = "pro_ejecutado", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEjecutado;
    
    @Column(name = "pro_fecha_menor_i")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date menorFechaInsumo;

    @Column(name = "pro_fecha_menor_gantt")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date menorFechaGantt;

    @Column(name = "pro_fecha_mayor_gantt")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date mayorFechaGantt;

    @Column(name = "pro_nro_contrato_ord_ini")
    private Integer reservaNroContratoInicial;

    @Column(name = "pro_nro_contrato_ord_fin")
    private Integer reservaNroContratoFinal;

    @ManyToOne
    @JoinColumn(name = "poa_anio_fiscal_id")
    private AnioFiscal anio;

    @Column(name = "pro_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pro_metodo_adq")
    private MetodoAdquisicion metodoAdquisicion;

    @ManyToOne
    @JoinColumn(name = "pro_usuario_responsabe")
    private SsUsuario responsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_estado")
    private PasosProcesoAdquisicion estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_estado_proceso")
    private EstadoProcesoAdq estadoProceso;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pro_tipo_contratos")
    private TipoGeneracionContrato tipoContratos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicion")
    private List<ProcesoAdquisicionInsumo> insumos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicion", orphanRemoval = true)
    private List<ProcesoAdquisicionLote> lotes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicion")
    private List<ProcesoAdquisicionItem> items;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicion")
    private List<ProcesoAdquisicionProveedor> proveedores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicion", orphanRemoval = true)
    private List<ContratoOC> contratos;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pro_gantt")
    private Gantt gantt;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pro_gantt_planificado")
    private Gantt ganttPlanificado;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pro_tdr")
    private TDR tdr;
    
    @Column(name = "pro_dias_en_pausa")
    private Integer diasEnPausa;
    
    @Column(name = "pro_fecha_ultima_pausa")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaUltimapausa;

    @Lob
    @Column(name = "pro_obs_inv_prov")
    private String observaciones;
        
    @OneToMany(mappedBy ="procesoAdquisicion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompromisoPresupuestarioProceso> compromisosPresupuestarios;
    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Integer getSecuenciaAnio() {
        return secuenciaAnio;
    }

    public Date getMenorFechaGantt() {
        return menorFechaGantt;
    }

    public void setMenorFechaGantt(Date menorFechaGantt) {
        this.menorFechaGantt = menorFechaGantt;
    }

    public void setSecuenciaAnio(Integer secuenciaAnio) {
        this.secuenciaAnio = secuenciaAnio;
    }

    public Gantt getGantt() {
        return gantt;
    }

    public void setGantt(Gantt gantt) {
        this.gantt = gantt;
    }

    public Date getMayorFechaGantt() {
        return mayorFechaGantt;
    }

    public void setMayorFechaGantt(Date mayorFechaGantt) {
        this.mayorFechaGantt = mayorFechaGantt;
    }

    public Integer getSecuenciaNumero() {
        return secuenciaNumero;
    }

    public Date getMenorFechaInsumo() {
        return menorFechaInsumo;
    }

    public void setMenorFechaInsumo(Date menorFechaInsumo) {
        this.menorFechaInsumo = menorFechaInsumo;
    }

    public List<CompromisoPresupuestarioProceso> getCompromisosPresupuestarios() {
        return compromisosPresupuestarios;
    }

    public void setCompromisosPresupuestarios(List<CompromisoPresupuestarioProceso> compromisosPresupuestarios) {
        this.compromisosPresupuestarios = compromisosPresupuestarios;
    }

    
   

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setSecuenciaNumero(Integer secuenciaNumero) {
        this.secuenciaNumero = secuenciaNumero;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public AnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(AnioFiscal anio) {
        this.anio = anio;
    }

    public MetodoAdquisicion getMetodoAdquisicion() {
        return metodoAdquisicion;
    }

    public void setMetodoAdquisicion(MetodoAdquisicion metodoAdquisicion) {
        this.metodoAdquisicion = metodoAdquisicion;
    }

    public SsUsuario getResponsable() {
        return responsable;
    }

    public void setResponsable(SsUsuario responsable) {
        this.responsable = responsable;
    }

    public PasosProcesoAdquisicion getEstado() {
        return estado;
    }

    public void setEstado(PasosProcesoAdquisicion estado) {
        this.estado = estado;
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

    public List<ProcesoAdquisicionLote> getLotes() {
        return lotes;
    }

    public void setLotes(List<ProcesoAdquisicionLote> lotes) {
        this.lotes = lotes;
    }

    public List<ProcesoAdquisicionItem> getItems() {
        return items;
    }

    public void setItems(List<ProcesoAdquisicionItem> items) {
        this.items = items;
    }

    public List<ProcesoAdquisicionProveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<ProcesoAdquisicionProveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public Integer getReservaNroContratoInicial() {
        return reservaNroContratoInicial;
    }

    public void setReservaNroContratoInicial(Integer reservaNroContratoInicial) {
        this.reservaNroContratoInicial = reservaNroContratoInicial;
    }

    public Integer getReservaNroContratoFinal() {
        return reservaNroContratoFinal;
    }

    public void setReservaNroContratoFinal(Integer reservaNroContratoFinal) {
        this.reservaNroContratoFinal = reservaNroContratoFinal;
    }

    public List<ContratoOC> getContratos() {
        return contratos;
    }

    public void setContratos(List<ContratoOC> contratos) {
        this.contratos = contratos;
    }

    public TDR getTdr() {
        return tdr;
    }

    public void setTdr(TDR tdr) {
        this.tdr = tdr;
    }   
    
    public Integer getDiasEnpausa() {
        return diasEnPausa;
    }

    public void setDiasEnpausa(Integer diasEnpausa) {
        if(diasEnpausa==null){
            diasEnpausa = 0;
        }
        this.diasEnPausa = diasEnpausa;
    }

    public Date getFechaUltimapausa() {
        return fechaUltimapausa;
    }

    public void setFechaUltimapausa(Date fechaUltimapausa) {
        this.fechaUltimapausa = fechaUltimapausa;
    }

    public List<ProcesoAdquisicionInsumo> getInsumos() {
        return insumos;
    }

    public TipoGeneracionContrato getTipoContratos() {
        return tipoContratos;
    }

    public void setTipoContratos(TipoGeneracionContrato tipoContratos) {
        this.tipoContratos = tipoContratos;
    }

    public Integer getDiasEnPausa() {
        return diasEnPausa;
    }

    public void setDiasEnPausa(Integer diasEnPausa) {
        this.diasEnPausa = diasEnPausa;
    }

    public BigDecimal getMontoEjecutado() {
        return montoEjecutado;
    }

    public void setMontoEjecutado(BigDecimal montoEjecutado) {
        this.montoEjecutado = montoEjecutado;
    }
    
    
    

    public void setInsumos(List<ProcesoAdquisicionInsumo> insumos) {
        this.insumos = insumos;
    }

    public EstadoProcesoAdq getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadoProcesoAdq estadoProceso) {
        this.estadoProceso = estadoProceso;
    }
    
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Gantt getGanttPlanificado() {
        return ganttPlanificado;
    }

    public void setGanttPlanificado(Gantt ganttPlanificado) {
        this.ganttPlanificado = ganttPlanificado;
    }

    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final ProcesoAdquisicion other = (ProcesoAdquisicion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
