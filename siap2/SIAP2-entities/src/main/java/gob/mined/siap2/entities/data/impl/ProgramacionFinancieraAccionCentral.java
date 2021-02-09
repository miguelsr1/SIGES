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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_prog_fin_acc_central",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "mon_anio", "mon_unidad_tecnica"})
        }
)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProgramacionFinancieraAccionCentral  implements Serializable{
    
      @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_unidad_tec_gen")
    @SequenceGenerator(name = "seq_m_unidad_tec_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_m_unidad_tec", allocationSize = 1)
    @Column(name = "mon_id")
    private Integer id;

    @Column(name = "mon_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "mon_unidad_tecnica")
    private UnidadTecnica unidadTecnica;

    
    @Column (name = "mon_anio")
    private Integer anio;

    //manejado por jpa, indice para que se mantega el orden de la lista
    @Column(name = "mon_indice")
    private Integer indice;


    //Auditoria
    @Column(name = "mon_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "mon_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "mon_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

  
    
    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
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
        final ProgramacionFinancieraAccionCentral other = (ProgramacionFinancieraAccionCentral) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
