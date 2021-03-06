/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoMetaIndicador;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_meta_indicador")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "met_tipo", discriminatorType = DiscriminatorType.STRING, length = 25)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class MetaIndicador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_meta_indicador_gen")
    @SequenceGenerator(name = "seq_meta_indicador_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_meta_indicador", allocationSize = 1)
    @Column(name = "met_id")
    protected Integer id;
    
    // update/insert is managed by discriminator mechanics
    @Column(name = "met_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoMetaIndicador tipo;

    @ManyToOne
    @JoinColumn(name = "met_anio_fiscal")
    private AnioFiscal anioFiscal;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "met_tipo_seguimietno")
    private TipoSeguimiento tipoSeguimiento;
    
    @OneToMany(mappedBy = "metaIndicador", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "posicion")
    private List<ValoresDeIndicador> valores;

    //Auditoria
    @Column(name = "met_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "met_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "met_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoMetaIndicador getTipo() {
        return tipo;
    }

    public void setTipo(TipoMetaIndicador tipo) {
        this.tipo = tipo;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }


    public List<ValoresDeIndicador> getValores() {
        return valores;
    }

    public void setValores(List<ValoresDeIndicador> valores) {
        this.valores = valores;
    }

    

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }


    public TipoSeguimiento getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(TipoSeguimiento tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
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
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final MetaIndicador other = (MetaIndicador) obj;
        if (this.id!= null && other.id!= null) {
            return Objects.equals(this.id, other.id);
        }
        return (this==other);
    }

}
