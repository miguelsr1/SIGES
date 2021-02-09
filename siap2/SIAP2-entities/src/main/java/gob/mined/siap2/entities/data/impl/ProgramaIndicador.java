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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_prog_ind")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramaIndicador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prog_ind_gen")
    @SequenceGenerator(name = "seq_prog_ind_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_prog_ind", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_ind")
    private Indicador indicador;

    @ManyToOne
    @JoinColumn(name = "pro_programa")
    private Programa programa;

    @ManyToOne
    @JoinColumn(name = "pro_ut")
    private UnidadTecnica utResponsable;
    
    
    @OneToMany(mappedBy = "programaIndicador", cascade = CascadeType.ALL)
    private List<AnioIndicador> anioIndicadors;

    //Auditoria
    @Column(name = "ind_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ind_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ind_version")
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

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public List<AnioIndicador> getAnioIndicadors() {
        return anioIndicadors;
    }

    public void setAnioIndicadors(List<AnioIndicador> anioIndicadors) {
        this.anioIndicadors = anioIndicadors;
    }

    
    
    public UnidadTecnica getUtResponsable() {
        return utResponsable;
    }

    /*
    public ProgramaIndicador getLineaBase() {
    return lineaBase;
    }
    public void setLineaBase(ProgramaIndicador lineaBase) {
    this.lineaBase = lineaBase;
    }
    public ProgramaIndicador getLineaTrabajo() {
    return lineaTrabajo;
    }
    public void setLineaTrabajo(ProgramaIndicador lineaTrabajo) {
    this.lineaTrabajo = lineaTrabajo;
    }
     */
    public void setUtResponsable(UnidadTecnica utResponsable) {
        this.utResponsable = utResponsable;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
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
        final ProgramaIndicador other = (ProgramaIndicador) obj;
        if ( this.id!= null &&  !Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
