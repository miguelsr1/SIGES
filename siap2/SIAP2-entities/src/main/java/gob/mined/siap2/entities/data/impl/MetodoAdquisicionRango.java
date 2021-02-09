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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_metodo_adq_rango")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class MetodoAdquisicionRango implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_metodo_adq_rango")
    @SequenceGenerator(name = "seq_metodo_adq_rango", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_metodo_adq_rango", allocationSize = 1)
    @Column(name = "met_id")
    private Integer id;

    @Column(name = "met_anio")
    private Integer anio;

    @Column(name = "met_monto_min", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoMin;

    @Column(name = "met_monto_max", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoMax;

    @ManyToOne
    @JoinColumn(name = "met_metodo")
    private MetodoAdquisicion metodo;

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
    public String getUltUsuario() {
        return ultUsuario;
    }

    public MetodoAdquisicion getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoAdquisicion metodo) {
        this.metodo = metodo;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getMontoMin() {
        return montoMin;
    }

    public void setMontoMin(BigDecimal montoMin) {
        this.montoMin = montoMin;
    }

    public BigDecimal getMontoMax() {
        return montoMax;
    }

    public void setMontoMax(BigDecimal montoMax) {
        this.montoMax = montoMax;
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

        final MetodoAdquisicionRango other = (MetodoAdquisicionRango) obj;
        if (this.id != null) {

            return Objects.equals(this.id, other.id);
        }

        return this == obj;
    }

}
