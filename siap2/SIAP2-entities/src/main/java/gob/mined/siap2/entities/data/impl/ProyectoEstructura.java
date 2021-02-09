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
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.enums.TipoMontoEstructura;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proyecto_est")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pro_tipo", discriminatorType = DiscriminatorType.STRING, length = 25)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class ProyectoEstructura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proy_estructura_gen")
    @SequenceGenerator(name = "seq_proy_estructura_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proy_estructura", allocationSize = 1)
    @Column(name = "pro_id")
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_proyecto")
    protected Proyecto proyecto;

    @Column(name = "pro_numero")
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "pro_unidad_tecnica")
    protected UnidadTecnica unidadTecnica;

    @ManyToOne
    @JoinColumn(name = "pro_usu_resp")
    protected SsUsuario responsable;

    @OrderColumn(name = "pro_posicion")
    @OneToMany(mappedBy = "proyectoEstructura", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<ProyectoEstPorcentajeFuente> montosFuentes;

    @OneToMany(mappedBy = "proyectoEstructura", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<ProyectoEstProducto> productos;

    // update/insert is managed by discriminator mechanics
    @Column(name = "pro_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoEstructura tipo;
    
    @Column(name = "pro_tipo_mon_est")
    @Enumerated(EnumType.STRING)
    private TipoMontoEstructura tipoMontoEstructura;

    
    @Column(name = "pro_importe", columnDefinition = "Decimal(20,2)")
    private BigDecimal importe;
    
    @Column(name = "pro_porcentaje", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentaje;
    
    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    protected String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    protected Date ultMod;

    @Column(name = "pro_version")
    @Version
    protected Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public SsUsuario getResponsable() {
        return responsable;
    }

    public void setResponsable(SsUsuario responsable) {
        this.responsable = responsable;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public TipoMontoEstructura getTipoMontoEstructura() {
        return tipoMontoEstructura;
    }

    public void setTipoMontoEstructura(TipoMontoEstructura tipoMontoEstructura) {
        this.tipoMontoEstructura = tipoMontoEstructura;
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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public List<ProyectoEstProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<ProyectoEstProducto> productos) {
        this.productos = productos;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    
    public void setVersion(Integer version) {
        this.version = version;
    }

    public TipoEstructura getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstructura tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ProyectoEstPorcentajeFuente> getMontosFuentes() {
        return montosFuentes;
    }

    public void setMontosFuentes(List<ProyectoEstPorcentajeFuente> montosFuentes) {
        this.montosFuentes = montosFuentes;
    }

    // </editor-fold>
}
