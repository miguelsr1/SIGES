/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pro_adq_insumo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProcesoAdquisicionInsumo implements Serializable {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pro_adq_ins")
    @SequenceGenerator(name = "seq_pro_adq_ins", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pro_adq_ins", allocationSize = 1)
    @Column(name = "pro_ins_id")
    private Integer id;

    @Column(name = "pro_indice")
    private Integer posicion;

    @Column(name = "pro_fecha_cont")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaContratacion;/*Cambios de adjudicacion. Mover a la relación. Se setea en la relacion*/

    //la ut a la que pertenece
    @ManyToOne
    @JoinColumn(name = "poa_unidad_tecnica")
    private UnidadTecnica unidadTecnica;

    @Lob
    @Column(name = "pro_obs")
    private String observacion;

    @OneToOne
    @JoinColumn(name = "pro_po_insumo")
    private POInsumos poInsumo;

    @ManyToOne
    @JoinColumn(name = "pro_insum")
    private Insumo insumo;

    @Column(name = "pro_cant")
    private Integer cantidadAdjudicada;/*Cambios de adjudicacion*/ //Cambia por cantidad adjudicada. Se setea cada vez que se adjudica o deshace una adquisición

    @Column(name = "pro_total", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotalAdjudicado;/*Cambios de adjudicacion*/  //pasa a llamarse montoTotalAdjudicado. En la relacion se crea un montoTotalAdjudicado que es el que se usa para calcular el atributo de esta clase 

    @ManyToOne
    @JoinColumn(name = "pro_adq")
    private ProcesoAdquisicion procesoAdquisicion;

    //los pagos que se han hecho por este insumo
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo", orphanRemoval = true)
    private List<RelacionProAdqItemInsumo> relItemInsumos;

    @Transient
    private Integer cantidadAUtilizar;

    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
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

    public Integer getCantidadAdjudicada() {
        if (cantidadAdjudicada == null) {
            cantidadAdjudicada = 0;
        }
        return cantidadAdjudicada;
    }

    public void setCantidadAdjudicada(Integer cantidadAdjudicada) {
        this.cantidadAdjudicada = cantidadAdjudicada;
    }

    public BigDecimal getMontoTotalAdjudicado() {
        if (montoTotalAdjudicado == null) {
            montoTotalAdjudicado = BigDecimal.ZERO;
        }
        return montoTotalAdjudicado;
    }

    public void setMontoTotalAdjudicado(BigDecimal montoTotalAdjudicado) {
        this.montoTotalAdjudicado = montoTotalAdjudicado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public POInsumos getPoInsumo() {
        return poInsumo;
    }

    public ProcesoAdquisicion getProcesoAdquisicion() {
        return procesoAdquisicion;
    }

    public void setProcesoAdquisicion(ProcesoAdquisicion procesoAdquisicion) {
        this.procesoAdquisicion = procesoAdquisicion;
    }

    public void setPoInsumo(POInsumos poInsumo) {
        this.poInsumo = poInsumo;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<RelacionProAdqItemInsumo> getRelItemInsumos() {
        return relItemInsumos;
    }

    public void setRelItemInsumos(List<RelacionProAdqItemInsumo> relItemInsumos) {
        this.relItemInsumos = relItemInsumos;
    }

    public Integer getCantidadAUtilizar() {
        if (cantidadAUtilizar == null) {
            cantidadAUtilizar = 0;
        }
        return cantidadAUtilizar;
    }

    public void setCantidadAUtilizar(Integer cantidadAUtilizar) {
        this.cantidadAUtilizar = cantidadAUtilizar;
    }

    public Integer getCantidadRestante() {
        Integer cantidadUsada = 0;
        for (RelacionProAdqItemInsumo rel : relItemInsumos) {
            if (rel.getCantidad() != null) {
                cantidadUsada = cantidadUsada + rel.getCantidad();

            }
        }
        return poInsumo.getCantidad() - cantidadUsada;
    }

    public boolean yaEstaAsignadoAItem(ProcesoAdquisicionItem itemSeleccionado) {
        boolean esta = false;
        for (int i = 0; i < relItemInsumos.size() && !esta; i++) {
            RelacionProAdqItemInsumo rel = relItemInsumos.get(i);
            if (itemSeleccionado.equals(rel.getItem())) {
                esta = true;
            }
        }
        return esta;
    }

    public RelacionProAdqItemInsumo getRelacionItemInsumoPorItem(ProcesoAdquisicionItem itemSeleccionado) {
        RelacionProAdqItemInsumo relacion = null;
        boolean esta = false;
        for (int i = 0; i < relItemInsumos.size() && !esta; i++) {
            relacion = relItemInsumos.get(i);
            if (itemSeleccionado.equals(relacion.getItem())) {
                esta = true;
            }
        }
        return relacion;
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

        final ProcesoAdquisicionInsumo other = (ProcesoAdquisicionInsumo) obj;
        if (this.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }

}
