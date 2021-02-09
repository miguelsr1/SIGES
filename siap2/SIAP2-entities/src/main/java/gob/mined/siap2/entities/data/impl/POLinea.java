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
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_linea")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POLinea implements Serializable, ManejoLineaBaseTrabajo<POLinea> {

    /**
     * maximo 31 caractres para nombre de la secuencia
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_p_linea_gen")
    @SequenceGenerator(name = "seq_pog_p_linea_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_p_linea", allocationSize = 1)
    @Column(name = "pol_id")
    private Integer id;

    @Column(name = "pol_indice")
    private Integer posicion;

    @ManyToOne
    @JoinColumn(name = "pol_est_prod")
    private ProyectoEstProducto producto;

    //fixme: esto parece que ya no tiene sentido
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_po_valor_c_producto",
            joinColumns = {
                @JoinColumn(name = "pov_linea")},
            inverseJoinColumns = {
                @JoinColumn(name = "pov_combo_valores")}
    )
    @OrderColumn(name = "anio")
    private List<ComboValorSeguimiento> valoresProducto;

    @OneToMany(mappedBy = "lineaPOProyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POIndicadorLinea> indicadores;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_po_linea_actividad",
            joinColumns = {
                @JoinColumn(name = "pol_linea")},
            inverseJoinColumns = {
                @JoinColumn(name = "pol_actividad")}
    )
    @OrderColumn(name = "posicion")
    private List<POActividadBase> actividades;

    @OneToMany
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_po_linea_colabora",
            joinColumns = {
                @JoinColumn(name = "pol_linea")},
            inverseJoinColumns = {
                @JoinColumn(name = "pol_ut")}
    )
    private List<UnidadTecnica> colaboradoras;

    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pol_linea_base")
    private POLinea lineaBase;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pol_linea_trabajo")
    private POLinea lineaTrabajo;
    @Column(name = "pol_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;

    //Auditoria
    @Column(name = "pol_ult_usuario")
    @AtributoUltimoUsuario
    private String pogUsuario;

    @Column(name = "pol_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date pogMod;

    @Column(name = "pol_version")
    @Version
    private Integer version;

    @Transient
    private String nombre;

    public String getNombre() {
        nombre = producto.getProducto().getNombre();
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<POIndicadorLinea> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<POIndicadorLinea> indicadores) {
        this.indicadores = indicadores;
    }

    public List<UnidadTecnica> getColaboradoras() {
        return colaboradoras;
    }

    public void setColaboradoras(List<UnidadTecnica> colaboradoras) {
        this.colaboradoras = colaboradoras;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public List<ComboValorSeguimiento> getValoresProducto() {
        return valoresProducto;
    }

    public void setValoresProducto(List<ComboValorSeguimiento> valoresProducto) {
        this.valoresProducto = valoresProducto;
    }

    public List<POActividadBase> getActividades() {
        return actividades;
    }

    public void setActividades(List<POActividadBase> actividades) {
        this.actividades = actividades;
    }

    public String getPogUsuario() {
        return pogUsuario;
    }

    public void setPogUsuario(String pogUsuario) {
        this.pogUsuario = pogUsuario;
    }

    public Date getPogMod() {
        return pogMod;
    }

    public void setPogMod(Date pogMod) {
        this.pogMod = pogMod;
    }

    public POLinea getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(POLinea lineaBase) {
        this.lineaBase = lineaBase;
    }

    public POLinea getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(POLinea lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ProyectoEstProducto getProducto() {
        return producto;
    }

    public void setProducto(ProyectoEstProducto producto) {
        this.producto = producto;
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
        final POLinea other = (POLinea) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }
        return (this == obj);
    }

}
