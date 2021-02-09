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
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoContrato;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
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
 * @author Sofis Solutions
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_contrato_oc")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ContratoOC implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contrato_oc_gen")
    @SequenceGenerator(name = "seq_contrato_oc_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_contrato_oc", allocationSize = 1)
    @Column(name = "con_id")
    private Integer id;

    @Column(name = "con_nro")
    private Integer nroContrato;

    @Column(name = "con_anio")
    private Integer nroAnio;

    @Enumerated(EnumType.STRING)
    @Column(name = "con_estado")
    private EstadoContrato estado;
    
    @Column(name = "con_imp_validados")
    private Boolean impuestosValidados;

    @ManyToOne
    @JoinColumn(name = "con_pro_adq_proveedor")
    private ProcesoAdquisicionProveedor procesoAdquisicionProveedor;

    @Column(name = "con_fechas_desde_oi")
    private Boolean fechasDesdeOrdenInicio;

    @ManyToOne
    @JoinColumn(name = "con_pro_adq")
    private ProcesoAdquisicion procesoAdquisicion;

    @ManyToOne
    @JoinColumn(name = "con_unidad_tec")
    private UnidadTecnica unidadTecnica;

    @ManyToOne
    @JoinColumn(name = "con_usuario_admin")
    private SsUsuario administrador;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "contrato")
    private List<ProcesoAdquisicionItem> items;

    @Size(max = 500)
    @Column(name = "con_observaciones")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "con_tipo")
    private TipoContrato tipo;

    //plazo de entrega en dias
    @Column(name = "con_plazo_entrega")
    private Integer plazoEntrega;

    // monto total del contrato
    @Column(name = "con_monto_adjudicado", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoAdjudicado;

    @Column(name = "con_monto_comprometido", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoComprometido;

    @Column(name = "con_monto_ejecutado", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoEjecutado;

    @Column(name = "con_por_anticipo")
    private Integer porcentajeAnticipo;

    @Column(name = "con_por_devolucion")
    private Integer porcentajeDevolucion;

    @Column(name = "con_fecha_emision")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEmision;

    @Column(name = "con_fecha_inicio")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "con_fecha_fin")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratoOC")
    @OrderColumn(name = "posicion")
    private List<ActaContrato> pagos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_impuesto_cont",
            joinColumns = { @JoinColumn(name = "con_id")},
            inverseJoinColumns = {@JoinColumn(name = "imp_id")}
    )
    private List<Impuesto> impuestos;
    
    @Transient
    private BigDecimal saldo;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_pro_fluj_caj_anio",
            joinColumns = {
                @JoinColumn(name = "rel_contrato")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_fluj_caj")}
    )
    private Set<FlujoCajaAnio> programacionPagosProceso;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_cont_fluj_caj_anio",
            joinColumns = {
                @JoinColumn(name = "rel_rel_ins")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_fluj_caj")}
    )
    private Set<FlujoCajaAnio> programacionPagosContrato;
    
    @ManyToOne
    @JoinColumn(name = "con_firmante_Orden_inicio")
    private SsUsuario firmanteOrdenInicio;
        
    @Transient
    private DataFile tempUploadedFile;
    
    @OneToOne
    @JoinColumn(name = "con_archivo_orden_inicio")
    private Archivo archivoOrdenInicio;
    
    @Column(name = "con_extension_plazo_habil")
    private Boolean extensionPlazoContratoHabilitada;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratoOC", orphanRemoval = true)
    private List<ModificativaContrato> modificativas;
    
    @OneToOne
    @JoinColumn(name = "con_archivo_rescision")
    private Archivo archivoResicion;
    
    //Auditoria
    @Column(name = "con_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "con_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "con_version")
    @Version
    private Integer version;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        BigDecimal totalActas = BigDecimal.ZERO;
        for(ActaContrato acta:this.getPagos()){
            if (acta.getTipo().equals(TipoActaContrato.RECEPCION) && acta.getEstado().equals(EstadoActa.APROBADA)){
                totalActas = totalActas.add(acta.getMontoRecibido());
            }

        }
        return montoAdjudicado.subtract(totalActas);
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
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

    public Integer getNroAnio() {
        return nroAnio;
    }

    public Set<FlujoCajaAnio> getProgramacionPagosProceso() {
        return programacionPagosProceso;
    }

    public void setProgramacionPagosProceso(Set<FlujoCajaAnio> programacionPagosProceso) {
        this.programacionPagosProceso = programacionPagosProceso;
    }

    public Set<FlujoCajaAnio> getProgramacionPagosContrato() {
        return programacionPagosContrato;
    }

    public void setProgramacionPagosContrato(Set<FlujoCajaAnio> programacionPagosContrato) {
        this.programacionPagosContrato = programacionPagosContrato;
    }

    public void setNroAnio(Integer nroAnio) {
        this.nroAnio = nroAnio;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public BigDecimal getMontoEjecutado() {
        return montoEjecutado;
    }

    public List<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    public Boolean getImpuestosValidados() {
        return impuestosValidados;
    }

    public void setImpuestosValidados(Boolean impuestosValidados) {
        this.impuestosValidados = impuestosValidados;
    }
        
    public void setMontoEjecutado(BigDecimal montoEjecutado) {
        this.montoEjecutado = montoEjecutado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public ProcesoAdquisicionProveedor getProcesoAdquisicionProveedor() {
        return procesoAdquisicionProveedor;
    }

    public Integer getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(Integer plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public void setProcesoAdquisicionProveedor(ProcesoAdquisicionProveedor procesoAdquisicionProveedor) {
        this.procesoAdquisicionProveedor = procesoAdquisicionProveedor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoContrato getTipo() {
        return tipo;
    }

    public void setTipo(TipoContrato tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMontoAdjudicado() {
        return montoAdjudicado;
    }

    public void setMontoAdjudicado(BigDecimal montoAdjudicado) {
        this.montoAdjudicado = montoAdjudicado;
    }

    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    }

    public Integer getPorcentajeAnticipo() {
        return porcentajeAnticipo;
    }

    public void setPorcentajeAnticipo(Integer porcentajeAnticipo) {
        this.porcentajeAnticipo = porcentajeAnticipo;
    }

    public Integer getPorcentajeDevolucion() {
        return porcentajeDevolucion;
    }

    public void setPorcentajeDevolucion(Integer porcentajeDevolucion) {
        this.porcentajeDevolucion = porcentajeDevolucion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public SsUsuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(SsUsuario administrador) {
        this.administrador = administrador;
    }


    public ProcesoAdquisicion getProcesoAdquisicion() {
        return procesoAdquisicion;
    }

    public void setProcesoAdquisicion(ProcesoAdquisicion procesoAdquisicion) {
        this.procesoAdquisicion = procesoAdquisicion;
    }

    public Boolean getFechasDesdeOrdenInicio() {
        return fechasDesdeOrdenInicio;
    }

    public void setFechasDesdeOrdenInicio(Boolean fechasDesdeOrdenInicio) {
        this.fechasDesdeOrdenInicio = fechasDesdeOrdenInicio;
    }

    public List<ProcesoAdquisicionItem> getItems() {
        return items;
    }

    public void setItems(List<ProcesoAdquisicionItem> items) {
        this.items = items;
    }

    public List<ActaContrato> getPagos() {
        return pagos;
    }

    public void setPagos(List<ActaContrato> pagos) {
        this.pagos = pagos;
    }

    public Integer getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(Integer nroContrato) {
        this.nroContrato = nroContrato;
    }
    
    public SsUsuario getFirmanteOrdenInicio() {
        return firmanteOrdenInicio;
    }

    public void setFirmanteOrdenInicio(SsUsuario firmanteOrdenInicio) {
        this.firmanteOrdenInicio = firmanteOrdenInicio;
    }
    
    public DataFile getTempUploadedFile() {
        return tempUploadedFile;
    }
    
    public void setTempUploadedFile(DataFile tempUploadedFile) {
        this.tempUploadedFile = tempUploadedFile;
    }
    
    public Archivo getArchivoOrdenInicio() {
        return archivoOrdenInicio;
    }

    public void setArchivoOrdenInicio(Archivo archivo) {
        this.archivoOrdenInicio = archivo;
    }    
    
    public Boolean getExtensionPlazoContratoHabilitada() {
        return extensionPlazoContratoHabilitada;
    }

    public void setExtensionPlazoContratoHabilitada(Boolean extensionPlazoContratoHabilitada) {
        this.extensionPlazoContratoHabilitada = extensionPlazoContratoHabilitada;
    }    
    
    public List<ModificativaContrato> getModificativas() {
        return modificativas;
    }

    public void setModificativas(List<ModificativaContrato> modificativas) {
        this.modificativas = modificativas;
    }

    public Archivo getArchivoResicion() {
        return archivoResicion;
    }

    public void setArchivoResicion(Archivo archivoResicion) {
        this.archivoResicion = archivoResicion;
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
        final ContratoOC other = (ContratoOC) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this== other);
    }    
    
}
