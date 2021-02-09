/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoActa;
import gob.mined.siap2.entities.enums.TipoActaContrato;
import gob.mined.siap2.entities.enums.TipoPagoActa;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pago_contrato")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ActaContrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago_contrato")
    @SequenceGenerator(name = "seq_pago_contrato", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pago_contrato", allocationSize = 1)
    @Column(name = "pag_id")
    private Integer id;

    @Column(name = "pag_nro")
    private Integer nroActa;

    @Column(name = "pag_posicion")
    private Integer posicion;

    @ManyToOne
    @JoinColumn(name = "con_contrato_oc")
    private ContratoOC contratoOC;
 
    @Lob
    @Column(name = "pag_observ")
    private String observaciones;

    @Column(name = "pag_fech_generacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    
    @Column(name = "pag_lugar")
    private String lugarRecepcion;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoInsumo> pagosInsumo;

    @Enumerated(EnumType.STRING)
    @Column(name = "pag_tipo")
    private TipoActaContrato tipo;

    @Column(name = "pag_con_detalle")
    private Boolean conDetalle;

    @Column(name = "pag_con_bienes_activo_fijo")
    private Boolean conBienesActivoFijo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pag_estado")
    private EstadoActa estado;

    @OneToMany(mappedBy = "actaContrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaActaContratoOC> facturas;

    @Column(name = "pag_num_solicitud_pago")
    private Integer numeroSolicitudPago;

    @Column(name = "pag_cant_recibida")
    private Integer cantidadRecibida;

    @Column(name = "pag_monto_recibido", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoRecibido;

    @Column(name = "pag_porcentaje", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentaje;//Sirve para anticipo y devolución

    @Enumerated(EnumType.STRING)
    @Column(name = "pag_tipo_pago")
    private TipoPagoActa tipoPago;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acta", orphanRemoval = true)
    private List<RelActaItem> relActaItem;

    @ManyToOne
    @JoinColumn(name = "con_num_comp_acta")
    private NumeroComprobanteRecepcionDePAgo numeroComprobanteRecepcionPago;

    @Column(name = "pag_quedan_emitido")
    private Boolean quedanEmitido;

    @ManyToOne
    @JoinColumn(name = "con_quedan")
    private QuedanEmitido quedan;

    @ManyToOne
    @JoinColumn(name = "con_comp_rep_ex_pa")
    private ComprobanteDeRecepcionDeExpedienteDePago comprobanteDeRecepcionDeExpedienteDePago;

    @ManyToOne
    @JoinColumn(name = "pag_anio_pago")
    private FlujoCajaAnio anioPago;

    @ManyToOne
    @JoinColumn(name = "pag_mes_pago")
    private POFlujoCajaMenusal mesPago;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actaContrato", orphanRemoval = true)
    private List<RelActaContratoCategoriaConvenio> relacionesActaCategoria;
    
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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getLugarRecepcion() {
        return lugarRecepcion;
    }

    public void setFechaGeneracion(Date fechaRecepcion) {
        this.fechaGeneracion = fechaRecepcion;
    }

    public List<PagoInsumo> getPagosInsumo() {
        return pagosInsumo;
    }

    public void setPagosInsumo(List<PagoInsumo> pagosInsumo) {
        this.pagosInsumo = pagosInsumo;
    }

    public Integer getNroActa() {
        return nroActa;
    }

    public void setNroActa(Integer nroActa) {
        this.nroActa = nroActa;
    }

    public ComprobanteDeRecepcionDeExpedienteDePago getComprobanteDeRecepcionDeExpedienteDePago() {
        return comprobanteDeRecepcionDeExpedienteDePago;
    }

    public void setComprobanteDeRecepcionDeExpedienteDePago(ComprobanteDeRecepcionDeExpedienteDePago comprobanteDeRecepcionDeExpedienteDePago) {
        this.comprobanteDeRecepcionDeExpedienteDePago = comprobanteDeRecepcionDeExpedienteDePago;
    }

    public QuedanEmitido getQuedan() {
        return quedan;
    }

    public void setQuedan(QuedanEmitido quedan) {
        this.quedan = quedan;
    }

    public List<FacturaActaContratoOC> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaActaContratoOC> facturas) {
        this.facturas = facturas;
    }

    public NumeroComprobanteRecepcionDePAgo getNumeroComprobanteRecepcionPago() {
        return numeroComprobanteRecepcionPago;
    }

    public void setNumeroComprobanteRecepcionPago(NumeroComprobanteRecepcionDePAgo numeroComprobanteRecepcionPago) {
        this.numeroComprobanteRecepcionPago = numeroComprobanteRecepcionPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setLugarRecepcion(String lugarRecepcion) {
        this.lugarRecepcion = lugarRecepcion;
    }

    public ContratoOC getContratoOC() {
        return contratoOC;
    }

    public void setContratoOC(ContratoOC contratoOC) {
        this.contratoOC = contratoOC;
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

    public TipoActaContrato getTipo() {
        return tipo;
    }

    public void setTipo(TipoActaContrato tipo) {
        this.tipo = tipo;
    }

    public Boolean getConDetalle() {
        return conDetalle;
    }

    public void setConDetalle(Boolean conDetalle) {
        this.conDetalle = conDetalle;
    }

    public EstadoActa getEstado() {
        return estado;
    }

    public void setEstado(EstadoActa estado) {
        this.estado = estado;
    }

    public Integer getNumeroSolicitudPago() {
        return numeroSolicitudPago;
    }

    public void setNumeroSolicitudPago(Integer numeroSolicitudPago) {
        this.numeroSolicitudPago = numeroSolicitudPago;
    }

    public Boolean getQuedanEmitido() {
        return quedanEmitido;
    }

    public void setQuedanEmitido(Boolean quedanEmitido) {
        this.quedanEmitido = quedanEmitido;
    }

    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public BigDecimal getMontoRecibido() {
        return montoRecibido;
    }

    public void setMontoRecibido(BigDecimal montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public TipoPagoActa getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPagoActa tipoPago) {
        this.tipoPago = tipoPago;
    }

    public List<RelActaItem> getRelActaItem() {
        return relActaItem;
    }

    public void setRelActaItem(List<RelActaItem> relActaItem) {
        this.relActaItem = relActaItem;
    }

    public FlujoCajaAnio getAnioPago() {
        return anioPago;
    }

    public void setAnioPago(FlujoCajaAnio anioPago) {
        this.anioPago = anioPago;
    }

    public POFlujoCajaMenusal getMesPago() {
        return mesPago;
    }

    public void setMesPago(POFlujoCajaMenusal mesPago) {
        this.mesPago = mesPago;
    }

    public List<RelActaContratoCategoriaConvenio> getRelacionesActaCategoria() {
        return relacionesActaCategoria;
    }

    public void setRelacionesActaCategoria(List<RelActaContratoCategoriaConvenio> relacionesActaCategoria) {
        this.relacionesActaCategoria = relacionesActaCategoria;
    }

    public Boolean getConBienesActivoFijo() {
        return conBienesActivoFijo;
    }

    public void setConBienesActivoFijo(Boolean conBienesActivoFijo) {
        this.conBienesActivoFijo = conBienesActivoFijo;
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
        final ActaContrato other = (ActaContrato) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
