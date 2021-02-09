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
 * @author eduardo
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_acta_categoria")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)

public class RelActaContratoCategoriaConvenio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_acta_categoria")
    @SequenceGenerator(name = "seq_rel_acta_categoria", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_acta_categoria", allocationSize = 1)
    @Column(name = "rel_id")
    private Integer id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "rel_acta_id", nullable = true)
    private ActaContrato actaContrato;

    @ManyToOne(optional = true)
    @JoinColumn(name = "rel_categoria_id", nullable = true)
    private CategoriaConvenio categoriaConvenio;

    @Column(name = "rel_monto_goes")
    private BigDecimal montoGoes;

    @Column(name = "rel_monto_no_goes")
    private BigDecimal montoNoGoes;

    //Auditoria
    @Column(name = "rel_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "rel_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "rel_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActaContrato getActaContrato() {
        return actaContrato;
    }

    public void setActaContrato(ActaContrato actaContrato) {
        this.actaContrato = actaContrato;
    }

    public CategoriaConvenio getCategoriaConvenio() {
        return categoriaConvenio;
    }

    public void setCategoriaConvenio(CategoriaConvenio categoriaConvenio) {
        this.categoriaConvenio = categoriaConvenio;
    }

    public BigDecimal getMontoGoes() {
        return montoGoes;
    }

    public void setMontoGoes(BigDecimal montoGoes) {
        this.montoGoes = montoGoes;
    }

    public BigDecimal getMontoNoGoes() {
        return montoNoGoes;
    }

    public void setMontoNoGoes(BigDecimal montoNoGoes) {
        this.montoNoGoes = montoNoGoes;
    }
 
    public Date getUltMod() {
        return actMod;
    }

    public void setUltMod(Date ultMod) {
        this.actMod = ultMod;
    }

    public String getUltUsuario() {
        return actUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.actUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

            
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

        final RelActaContratoCategoriaConvenio other = (RelActaContratoCategoriaConvenio) obj;
        if (this.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }

}
