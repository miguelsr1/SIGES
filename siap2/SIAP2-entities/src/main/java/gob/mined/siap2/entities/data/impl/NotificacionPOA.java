/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = "POA_NOTIFICACION")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class NotificacionPOA extends Notificacion implements Serializable {

    @Column(name = "poa_poa_anio")
    private Integer poaAnio;
    @Column(name = "poa_poa_ut")
    private Integer poaUT;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getPoaAnio() {
        return poaAnio;
    }

    public void setPoaAnio(Integer poaAnio) {
        this.poaAnio = poaAnio;
    }

    public Integer getPoaUT() {
        return poaUT;
    }

    public void setPoaUT(Integer poaUT) {
        this.poaUT = poaUT;
    }

    // </editor-fold>
}
