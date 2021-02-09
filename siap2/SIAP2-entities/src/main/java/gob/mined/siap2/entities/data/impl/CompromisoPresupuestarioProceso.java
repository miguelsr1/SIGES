/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = TipoCompromisoPresupuestario.Values.PROCESO)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class CompromisoPresupuestarioProceso extends CompromisoPresupuestario implements Serializable {


    @OneToOne
    @JoinColumn(name = "con_proceso_id")
    private ProcesoAdquisicion procesoAdquisicion;   
        
    @OneToOne
    @JoinColumn(name = "com_contrato_oc")
    private ContratoOC contratoOC;
    
    @OneToOne
    @JoinColumn(name = "com_fuente_recursos")
    private FuenteRecursos fuenteRecursos;
            
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public ProcesoAdquisicion getProcesoAdquisicion() {
        return procesoAdquisicion;
    }

    public void setProcesoAdquisicion(ProcesoAdquisicion procesoAdquisicion) {
        this.procesoAdquisicion = procesoAdquisicion;
    }

    public ContratoOC getContratoOC() {
        return contratoOC;
    }

    public void setContratoOC(ContratoOC contratoOC) {
        this.contratoOC = contratoOC;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
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
        final CompromisoPresupuestarioProceso other = (CompromisoPresupuestarioProceso) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
