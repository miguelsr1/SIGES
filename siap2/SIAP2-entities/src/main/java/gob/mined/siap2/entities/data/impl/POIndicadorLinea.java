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
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.OneToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_ind_lin")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POIndicadorLinea implements Serializable, ManejoLineaBaseTrabajo<POIndicadorLinea> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_po_ind_lin_gen")
    @SequenceGenerator(name = "seq_po_ind_lin_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_po_ind_lin", allocationSize = 1)
    @Column(name = "poi_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "poi_indicador")
    private Indicador indicador;

    @ManyToOne
    @JoinColumn(name = "poi_l_pog_porog")
    private POLinea lineaPOProyecto;

    
    /**
     * estas serian las metas por pog
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_po_valores_c_indicador",
            joinColumns = {
                @JoinColumn(name = "pov_linea")},
            inverseJoinColumns = {
                @JoinColumn(name = "pov_combo_valores")}
    )
    @OrderColumn(name = "anio")
    private List<ComboValorSeguimiento> comboValoresSeguimiento;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poi_linea_base")
    private POIndicadorLinea lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poi_linea_trabajo")
    private POIndicadorLinea lineaTrabajo;
    @Column(name = "poi_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "poi_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "poi_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "poi_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public List<ComboValorSeguimiento> getComboValoresSeguimiento() {
        return comboValoresSeguimiento;
    }

    public void setComboValoresSeguimiento(List<ComboValorSeguimiento> comboValoresSeguimiento) {
        this.comboValoresSeguimiento = comboValoresSeguimiento;
    }

    public POLinea getLineaPOProyecto() {
        return lineaPOProyecto;
    }

    public void setLineaPOProyecto(POLinea lineaPOGProyecto) {
        this.lineaPOProyecto = lineaPOGProyecto;
    }

    public POIndicadorLinea getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POIndicadorLinea lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POIndicadorLinea getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POIndicadorLinea lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
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
        final POIndicadorLinea other = (POIndicadorLinea) obj;
        if (this.id != null) {
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
        }
        return (this == obj);
    }

}
