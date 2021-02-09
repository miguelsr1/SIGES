/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_techo_pres_fr")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class RelTechoPresupuestarioFR  implements Serializable{
    
    /**maximo 31 caractres para nombre de la secuencia **/
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_tech_p_f_gen")
    @SequenceGenerator(name = "seq_rel_tech_p_f_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_tech_p_f", allocationSize = 1)
    @Column(name = "rel_id")
    private Integer id;
         
    @ManyToOne
    @JoinColumn(name="rel_tech_pres")
    private TechoPresupuestarioGOES techoPresupuestarioGOES;
    
    @ManyToOne
    @JoinColumn(name="rel_fuente_recurso")
    private FuenteRecursos fuenteRecursos;
    
    
    @OneToMany(mappedBy="techoPresupuestarioFR", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelTechoPresupuestarioUT> techoPresupuestarioUT;

    
    //Auditoria
    @Column(name = "rel_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "rel_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "rel_version")
    @Version
    private Integer version;

    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TechoPresupuestarioGOES getTechoPresupuestarioGOES() {
        return techoPresupuestarioGOES;
    }

    public void setTechoPresupuestarioGOES(TechoPresupuestarioGOES techoPresupuestarioGOES) {
        this.techoPresupuestarioGOES = techoPresupuestarioGOES;
    }

    public List<RelTechoPresupuestarioUT> getTechoPresupuestarioUT() {
        return techoPresupuestarioUT;
    }

    public void setTechoPresupuestarioUT(List<RelTechoPresupuestarioUT> techoPresupuestarioUT) {
        this.techoPresupuestarioUT = techoPresupuestarioUT;
    }

   
    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
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
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final RelTechoPresupuestarioFR other = (RelTechoPresupuestarioFR) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    

    
    
    
    
    
}

