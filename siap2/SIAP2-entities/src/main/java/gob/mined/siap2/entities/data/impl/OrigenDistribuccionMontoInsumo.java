/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoOrigenDistribuccionMonto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_fuente_aporte_proy")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fue_tipo", discriminatorType = DiscriminatorType.STRING, length = 50)
@DiscriminatorValue(value = TipoOrigenDistribuccionMonto.Values.FUENTE_RECURSO)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class OrigenDistribuccionMontoInsumo  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fuen_proy_gen")
    @SequenceGenerator(name = "seq_fuen_proy_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_fuen_proy", allocationSize = 1)
    @Column(name = "fue_id")
    protected Integer id;
    
    @ManyToOne
    @JoinColumn(name = "fue_recursos")
    private FuenteRecursos fuenteRecursos;
    
    
    @ManyToOne
    @JoinColumn(name = "fue_cat_conv")
    protected CategoriaConvenio categoriaConvenio;
   
    
    // update/insert is managed by discriminator mechanics
    @Column(name = "fue_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoOrigenDistribuccionMonto tipo;
    
    @Column(name = "fue_ult_usuario")
    @AtributoUltimoUsuario
    protected String ultUsuario;

    @Column(name = "fue_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "fue_version")
    @Version
    private Integer version;
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TipoOrigenDistribuccionMonto getTipo() {
        return tipo;
    }

    public void setTipo(TipoOrigenDistribuccionMonto tipo) {
        this.tipo = tipo;
    }

    public CategoriaConvenio getCategoriaConvenio() {
        return categoriaConvenio;
    }

    public void setCategoriaConvenio(CategoriaConvenio categoriaConvenio) {
        this.categoriaConvenio = categoriaConvenio;
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
        final OrigenDistribuccionMontoInsumo other = (OrigenDistribuccionMontoInsumo) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
