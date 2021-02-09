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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_progp_tech_anio" ,
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "pro_anio_fiscal", "pro_programa_p"})
        }
) 
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramaPresupuestarioTechoAnio implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pp_techo_a_gen")
    @SequenceGenerator(name = "seq_pp_techo_a_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pp_techo_a", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;
    
    @Column(name = "pro_indice")
    private Integer indice;
           
    @ManyToOne
    @JoinColumn(name = "pro_anio_fiscal")
    private AnioFiscal anioFiscal;
    
    @Column(name = "pro_techo", columnDefinition = "Decimal(20,2)")
    private BigDecimal techo;
   
    @ManyToOne
    @JoinColumn(name = "pro_programa_p")
    private ProgramaPresupuestario programaPresupuestario;


    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }




    

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }


    public BigDecimal getTecho() {
        return techo;
    }

    public void setTecho(BigDecimal techo) {
        this.techo = techo;
    }

    public ProgramaPresupuestario getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(ProgramaPresupuestario programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
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
        final ProgramaPresupuestarioTechoAnio other = (ProgramaPresupuestarioTechoAnio) obj;
        if (this.id!= null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }
    
    
    
    
    
}
