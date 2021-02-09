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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_techo_pres_ut")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class RelTechoPresupuestarioUT  implements Serializable{
    
    /**maximo 31 caractres para nombre de la secuencia **/
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_tech_p_u_gen")
    @SequenceGenerator(name = "seq_rel_tech_p_u_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_tech_p_u", allocationSize = 1)
    @Column(name = "rel_id")
    private Integer id;
         
    @ManyToOne
    @JoinColumn(name="rel_tech_pres")
    private RelTechoPresupuestarioFR techoPresupuestarioFR;
    
    
    
    @Column(name = "rel_techo_pres", columnDefinition="Decimal(20,2)")
    private BigDecimal techoPresupuestario;    
    
         
    @ManyToOne
    @JoinColumn(name="rel_unidad_tecnica")
    private UnidadTecnica unidadTecnica;    
    
    
    
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

    public RelTechoPresupuestarioFR getTechoPresupuestarioFR() {
        return techoPresupuestarioFR;
    }

    public void setTechoPresupuestarioFR(RelTechoPresupuestarioFR techoPresupuestarioFR) {
        this.techoPresupuestarioFR = techoPresupuestarioFR;
    }

    
    

    public BigDecimal getTechoPresupuestario() {
        return techoPresupuestario;
    }

    public void setTechoPresupuestario(BigDecimal techoPresupuestario) {
        this.techoPresupuestario = techoPresupuestario;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
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
        final RelTechoPresupuestarioUT other = (RelTechoPresupuestarioUT) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    

    
    
    
    
    
}

