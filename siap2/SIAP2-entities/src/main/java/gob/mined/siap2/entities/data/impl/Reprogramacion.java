/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "SS_REPROGRAMACIONES")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Reprogramacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rep_gen")
    @SequenceGenerator(name = "seq_rep_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rep", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rep_estado")
    private EstadoReprogramacion estado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "rep_tipo")
    private TipoReprogramacion tipoReprogramacion;

    @Transient
    private BigDecimal diferenciaAumentoTotal;
    @Transient
    private BigDecimal diferenciaDisminucionTotal;
    @Column(name = "rep_descripcion")
    @Size(max = 1000)
    private String repDescripcion;

    @Transient
    private List<UnidadTecnica> conjUnidadesTecnicas;

    //Auditoria
    @Column(name = "rep_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "rep_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "rep_version")
    @Version
    private Integer version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reprogramacionId")
    private List<ReprogramacionDetalle> rep_detalle_lista;

    public Reprogramacion() {
        estado = EstadoReprogramacion.FORMULACION;
    }
    public Reprogramacion(TipoReprogramacion tipo) {
        estado = EstadoReprogramacion.FORMULACION;
        tipoReprogramacion=tipo;
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

    public List<ReprogramacionDetalle> getRep_detalle_lista() {
        return rep_detalle_lista;
    }

    public void setRep_detalle_lista(List<ReprogramacionDetalle> rep_detalle_lista) {
        this.rep_detalle_lista = rep_detalle_lista;
    }

    public BigDecimal getDiferenciaAumentoTotal() {
        this.diferenciaAumentoTotal = BigDecimal.ZERO;
        if (this.rep_detalle_lista != null) {
            for (ReprogramacionDetalle det : this.rep_detalle_lista) {
                if (det.getDiferencia() != null && det.getDiferencia().longValue()>0) {
                    diferenciaAumentoTotal = diferenciaAumentoTotal.add(det.getDiferencia());
                }
            }
        }
         
        return diferenciaAumentoTotal;
    }

    public void setDiferenciaAumentoTotal(BigDecimal diferenciaAumentoTotal) {
        this.diferenciaAumentoTotal = diferenciaAumentoTotal;
    }

    public BigDecimal getDiferenciaDisminucionTotal() {
         this.diferenciaDisminucionTotal = BigDecimal.ZERO;
        if (this.rep_detalle_lista != null) {
            for (ReprogramacionDetalle det : this.rep_detalle_lista) {
                if (det.getDiferencia() != null && det.getDiferencia().longValue()<0) {
                    diferenciaDisminucionTotal = diferenciaDisminucionTotal.add(det.getDiferencia());
                }
            }
        }
        return diferenciaDisminucionTotal;
    }

    public void setDiferenciaDisminucionTotal(BigDecimal diferenciaDisminucionTotal) {
        this.diferenciaDisminucionTotal = diferenciaDisminucionTotal;
    }

    
    
    
    
     

    public EstadoReprogramacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReprogramacion estado) {
        this.estado = estado;
    }

    public String getRepDescripcion() {
        return repDescripcion;
    }

    public void setRepDescripcion(String repDescripcion) {
        this.repDescripcion = repDescripcion;
    }

    public List<UnidadTecnica> getConjUnidadesTecnicas() {
        Set conjUT = new HashSet<>();
        for (ReprogramacionDetalle det : rep_detalle_lista) {
            if (det.getPoa() != null) {
                conjUT.add(det.getPoa().getUnidadTecnica());
            }
        }
        conjUnidadesTecnicas = new ArrayList(conjUT);

        return conjUnidadesTecnicas;
    }

    public void setConjUnidadesTecnicas(List<UnidadTecnica> conjUnidadesTecnicas) {
        this.conjUnidadesTecnicas = conjUnidadesTecnicas;
    }

    public TipoReprogramacion getTipoReprogramacion() {
        return tipoReprogramacion;
    }

    public void setTipoReprogramacion(TipoReprogramacion tipoReprogramacion) {
        this.tipoReprogramacion = tipoReprogramacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reprogramacion)) {
            return false;
        }
        Reprogramacion other = (Reprogramacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gob.mined.siap2.entities.data.impl.Reprogramacion[ id=" + id + " ]";
    }

}
