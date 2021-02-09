/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue("POG")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POGInsumo extends POInsumos implements Serializable{
    
    
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "posicion")
    private List<POGInsumoAnio> distribucionAnios;
    
    
    

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    
    public POGInsumo() {
    }

   
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public List<POGInsumoAnio> getDistribucionAnios() {
        return distribucionAnios;
    }

    public void setDistribucionAnios(List<POGInsumoAnio> distribucionAnios) {
        this.distribucionAnios = distribucionAnios;
    }
    
    
    

    // </editor-fold>

    
    
    
    
}
