/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoItem;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.ArrayList;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proceso_adq_item")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProcesoAdquisicionItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proceso_adq_item")
    @SequenceGenerator(name = "seq_proceso_adq_item", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proceso_adq_item", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @Column(name = "pro_nombre", length = 500)
    private String nombre;
    
    @Column(name = "pro_nro_item")
    private Integer nroItem;

    @ManyToOne
    @JoinColumn(name = "pro_lote")
    private ProcesoAdquisicionLote lote;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
    private List<RelacionProAdqItemInsumo> relItemInsumos;

    @ManyToOne
    @JoinColumn(name = "pro_adq")
    private ProcesoAdquisicion procesoAdquisicion;

    //coleccion de ofertas
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoAdquisicionItem")
    private List<ProcesoAdquisicionItemProvOferta> ofertasProvedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_estado")
    private EstadoItem estado;

    //oferta ganadora
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_pro_adq_item_pro_of")
    private ProcesoAdquisicionItemProvOferta proveedorOfertaAdjudicadaId;

    @Column(name = "pro_fecha_pausa")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaPausa;

    @ManyToOne
    @JoinColumn(name = "pro_contrato_oc")
    private ContratoOC contrato;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pro_tdr")
    private TDR tdr;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
    private List<RelActaItem> relActaItem;

    public List<ProcesoAdquisicionInsumo> getInsumosTemporalesDelItem() {        
        if(this.getRelItemInsumos() == null){
            this.setRelItemInsumos(new ArrayList<RelacionProAdqItemInsumo>());
        }
        List<ProcesoAdquisicionInsumo> insumosDelItem = new ArrayList<ProcesoAdquisicionInsumo>();
        for(RelacionProAdqItemInsumo relacion : this.getRelItemInsumos()){
            insumosDelItem.add(relacion.getInsumo());
        }
        return insumosDelItem;
    }
    
    public Integer getCantidadAUtilizarPorInsumo(ProcesoAdquisicionInsumo insumo) {
        Integer cantidad = 0;
        boolean encontro = false;
        for (int i = 0; i < this.relItemInsumos.size() && !encontro; i++) {
            RelacionProAdqItemInsumo rel = relItemInsumos.get(i);
            if(rel.getInsumo().getId() == insumo.getId()){
                cantidad = rel.getCantidad();
                encontro = true;
            }
        }
        return cantidad;
    }
    
    
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

    public ProcesoAdquisicionLote getLote() {
        return lote;
    }

    public void setLote(ProcesoAdquisicionLote lote) {
        this.lote = lote;
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

    public ProcesoAdquisicion getProcesoAdquisicion() {
        return procesoAdquisicion;
    }

    public void setProcesoAdquisicion(ProcesoAdquisicion procesoAdquisicion) {
        this.procesoAdquisicion = procesoAdquisicion;
    }

    public List<ProcesoAdquisicionItemProvOferta> getOfertasProvedor() {
        return ofertasProvedor;
    }

    public void setOfertasProvedor(List<ProcesoAdquisicionItemProvOferta> ofertasProvedor) {
        this.ofertasProvedor = ofertasProvedor;
    }

    public EstadoItem getEstado() {
        return estado;
    }

    public void setEstado(EstadoItem estado) {
        this.estado = estado;
    }

    public ProcesoAdquisicionItemProvOferta getProveedorOfertaAdjudicadaId() {
        return proveedorOfertaAdjudicadaId;
    }

    public void setProveedorOfertaAdjudicadaId(ProcesoAdquisicionItemProvOferta proveedorOfertaAdjudicadaId) {
        this.proveedorOfertaAdjudicadaId = proveedorOfertaAdjudicadaId;
    }

    public Date getFechaPausa() {
        return fechaPausa;
    }

    public void setFechaPausa(Date fechaPausa) {
        this.fechaPausa = fechaPausa;
    }

    public ContratoOC getContrato() {
        return contrato;
    }

    public void setContrato(ContratoOC contrato) {
        this.contrato = contrato;
    }

    public TDR getTdr() {
        return tdr;
    }

    public void setTdr(TDR tdr) {
        this.tdr = tdr;
    }
  
    public Integer getNroItem() {
        return nroItem;
    }

    public void setNroItem(Integer nroItem) {
        this.nroItem = nroItem;
    }
    
    public List<RelacionProAdqItemInsumo> getRelItemInsumos() {
        return relItemInsumos;
    }

    public void setRelItemInsumos(List<RelacionProAdqItemInsumo> relItemInsumos) {
        this.relItemInsumos = relItemInsumos;
    }    

    public List<RelActaItem> getRelActaItem() {
        return relActaItem;
    }

    public void setRelActaItem(List<RelActaItem> relActaItem) {
        this.relActaItem = relActaItem;
    }    
    
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

        final ProcesoAdquisicionItem other = (ProcesoAdquisicionItem) obj;
        if (this.id != null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }

        return this == obj;
    }

    public RelacionProAdqItemInsumo getRelacionItemInsumoPorInsumo(ProcesoAdquisicionInsumo insumoSeleccionado) {
        RelacionProAdqItemInsumo relacion = null;
        boolean esta = false;
        for(int i = 0; i < relItemInsumos.size() && !esta; i++){
            relacion = relItemInsumos.get(i);
            if(insumoSeleccionado.equals(relacion.getInsumo())){
                esta = true;
            }
        }
        return relacion;
    }    

    

}
