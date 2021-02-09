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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = TipoPOA.Values.POA_CON_ACTIVIDADES)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POAConActividades extends GeneralPOA implements Serializable {

    @ManyToOne
    @JoinColumn(name = "poa_con_monto_por_a")
    private ConMontoPorAnio conMontoPorAnio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_poa_con_act_act",
            joinColumns = {
                @JoinColumn(name = "poa_linea")},
            inverseJoinColumns = {
                @JoinColumn(name = "poa_actividad")}
    )
    @OrderColumn(name = "posicion")
    private List<POActividadBase> actividades;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<POActividadBase> getActividades() {
        return actividades;
    }

    public void setActividades(List<POActividadBase> actividades) {
        this.actividades = actividades;
    }

    public Date getFechaFijacion() {
        return fechaFijacion;
    }

    public void setFechaFijacion(Date fechaFijacion) {
        this.fechaFijacion = fechaFijacion;
    }
 
    public ConMontoPorAnio getConMontoPorAnio() {
        return conMontoPorAnio;
    }

    public void setConMontoPorAnio(ConMontoPorAnio conMontoPorAnio) {
        this.conMontoPorAnio = conMontoPorAnio;
    }

    public void setLineaBase(POAConActividades lineaBase) {
        this.lineaBase = lineaBase;
    }

    public void setLineaTrabajo(POAConActividades lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
    }

    @Override
    public String getNombre() {
        return getAnioFiscal().getAnio() +  " - "+ getUnidadTecnica().getNombre() + " - "+ conMontoPorAnio.getNombre();
    }
   
    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final POAConActividades other = (POAConActividades) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
