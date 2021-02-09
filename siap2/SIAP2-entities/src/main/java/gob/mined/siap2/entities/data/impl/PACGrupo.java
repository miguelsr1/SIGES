/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pac_grupo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PACGrupo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pac_grupo_gen")
    @SequenceGenerator(name = "seq_pac_grupo_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pac_grupo", allocationSize = 1)
    @Column(name = "pac_id")
    private Integer id;

    @Column(name = "pac_nombre")
    private String nombre;

    @Column(name = "pac_total", columnDefinition = "Decimal(20,2)")
    private BigDecimal total;
        
    @Column(name = "pac_iniciado")
    private Boolean iniciado;
   

    @Enumerated(EnumType.STRING)
    @Column(name = "pac_estado")
    private EstadoGrupoPAC estado;

    @Column(name = "poi_fecha_insumo")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaMinimoInsumo;

    @Column(name = "poi_fecha_gantt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaMaximaGant;

    @ManyToOne
    @JoinColumn(name = "pac_pac")
    private PAC pac;

    @ManyToOne
    @JoinColumn(name = "pac_metodo_adq")
    private MetodoAdquisicion metodoAdquisicion;

    @OneToMany(mappedBy = "pacGrupo")
    private List<POInsumos> insumos;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pac_gantt")
    private Gantt gantt;

    //Auditoria
    @Column(name = "pac_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pac_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pac_version")
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

    public EstadoGrupoPAC getEstado() {
        return estado;
    }

    public MetodoAdquisicion getMetodoAdquisicion() {
        return metodoAdquisicion;
    }

    public void setMetodoAdquisicion(MetodoAdquisicion metodoAdquisicion) {
        this.metodoAdquisicion = metodoAdquisicion;
    }

    public Boolean getIniciado() {
        return iniciado;
    }

    public void setIniciado(Boolean iniciado) {
        this.iniciado = iniciado;
    }

    
    public void setEstado(EstadoGrupoPAC estado) {
        this.estado = estado;
    }

    public List<POInsumos> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<POInsumos> insumos) {
        this.insumos = insumos;
    }

    public Gantt getGantt() {
        return gantt;
    }

    public void setGantt(Gantt gantt) {
        this.gantt = gantt;
    }

    public PAC getPac() {
        return pac;
    }

    public void setPac(PAC pac) {
        this.pac = pac;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Date getFechaMinimoInsumo() {
        return fechaMinimoInsumo;
    }

    public void setFechaMinimoInsumo(Date fechaMinimoInsumo) {
        this.fechaMinimoInsumo = fechaMinimoInsumo;
    }

    public Date getFechaMaximaGant() {
        return fechaMaximaGant;
    }

    public void setFechaMaximaGant(Date fechaMaximaGant) {
        this.fechaMaximaGant = fechaMaximaGant;
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
        final PACGrupo other = (PACGrupo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
