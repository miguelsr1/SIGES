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
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_techo_prog_pres_f",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tec_fuente_f", "tec_fuente_r", "tec_prog_p"})
        })
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class TechoProgramaPresupueastarioFuente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tech_pp_gen")
    @SequenceGenerator(name = "seq_tech_pp_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_tech_pp", allocationSize = 1)
    @Column(name = "tec_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tec_fuente_f")
    private FuenteFinanciamiento fuenteFinanciamiento;
    
    @ManyToOne
    @JoinColumn(name = "tec_fuente_r")
    private FuenteRecursos fuenteRecursos;

   
    @ManyToOne
    @JoinColumn(name = "tec_prog_p")
    private ProgramaPresupuestario programaPresupuestario;

    /**
     * los techos por año
     */
    @OneToMany(mappedBy = "techoProgramaPresupueastarioFuente", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "indice")
    private List<TechoProgramaPresupueastarioAnio> techos;
    
    
    
    //Auditoria
    @Column(name = "tec_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "tec_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tec_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public FuenteFinanciamiento getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public List<TechoProgramaPresupueastarioAnio> getTechos() {
        return techos;
    }

    public void setTechos(List<TechoProgramaPresupueastarioAnio> techos) {
        this.techos = techos;
    }
    

    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
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
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final TechoProgramaPresupueastarioFuente other = (TechoProgramaPresupueastarioFuente) obj;        
        if (this.id != null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return this==other;
    }

}
