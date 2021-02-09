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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_l_pog_proy_ind")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class LineaPOGIndicador  implements Serializable, ManejoLineaBaseTrabajo<LineaPOGIndicador> {
    /**maximo 31 caractres para nombre de la secuencia **/
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_l_pog_proy_ind_gen")
    @SequenceGenerator(name = "seq_l_pog_proy_ind_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_l_pog_proy_ind", allocationSize = 1)
    @Column(name = "lpo_id")
    private Integer id;    
    
    @ManyToOne
    @JoinColumn(name="lpo_lpadre")
    private POLinea linea;
        
    @ManyToOne
    @JoinColumn(name="lpo_ind")
    private Indicador indicador;
    
        
    //versionado
    //funcina como una lista doblemente encadenada donde anterior es la línea base y siguiente es la línea de trabajo
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="lpo_linea_base")
    private LineaPOGIndicador lineaBase;   
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="lpo_linea_trabajo")
    private LineaPOGIndicador lineaTrabajo;
    @Column(name = "log_fecha_fijacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFijacion;
    
    
    //Auditoria
    @Column(name = "lpo_ult_usuario")
    @AtributoUltimoUsuario
    private String pogUsuario;

    @Column(name = "lpo_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date pogMod;

    @Column(name = "lpo_version")
    @Version
    private Integer version;

    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public POLinea getLinea() {
        return linea;
    }

    public void setLinea(POLinea linea) {
        this.linea = linea;
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

    public LineaPOGIndicador getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(LineaPOGIndicador lineaBase) {
        this.lineaBase = lineaBase;
    }

    public LineaPOGIndicador getLineaTrabajo() {
        return lineaTrabajo;
    }

    public void setLineaTrabajo(LineaPOGIndicador lineaTrabajo) {
        this.lineaTrabajo = lineaTrabajo;
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
        final LineaPOGIndicador other = (LineaPOGIndicador) obj;
        if ( (this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }      
        
        return (this == obj);
    }
    
    
    
    
}
