/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = TipoPOA.Values.POA_PROYECTO)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POAProyecto extends GeneralPOA implements Serializable {

    @ManyToOne
    @JoinColumn(name = "poa_proy")
    private Proyecto proyecto;

    @Column(name = "poa_desde")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaDesde;

    @Column(name = "poa_hasta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHasta;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_poa_rel_poa_linea",
            joinColumns = {
                @JoinColumn(name = "poa_proy")},
            inverseJoinColumns = {
                @JoinColumn(name = "poa_linea")}
    )
    @OrderColumn(name = "posicion")
    private List<POLinea> lineas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_poa_rel_poa_riesgo",
            joinColumns = {
                @JoinColumn(name = "poa_proy")},
            inverseJoinColumns = {
                @JoinColumn(name = "poa_riesgo")}
    )
    private List<POARiesgo> riesgos;
    
    @Transient
    private String nombre;
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    @Override
    public String getNombre() {
        nombre= getAnioFiscal().getAnio() +  " - "+  getUnidadTecnica().getNombre() + " - "+ proyecto.getNombre();
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<POLinea> getLineas() {
        return lineas;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<POARiesgo> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(List<POARiesgo> riesgos) {
        this.riesgos = riesgos;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

   

    public void setLineas(List<POLinea> lineas) {
        this.lineas = lineas;
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
        final POAProyecto other = (POAProyecto) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
