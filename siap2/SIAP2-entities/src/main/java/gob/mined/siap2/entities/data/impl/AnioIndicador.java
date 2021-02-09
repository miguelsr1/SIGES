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
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_anio_indicador")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class AnioIndicador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_anio_indi_gen")
    @SequenceGenerator(name = "seq_anio_indi_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_anio_indi", allocationSize = 1)
    @Column(name = "ani_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "ani_prog_ind")
    private ProgramaIndicador programaIndicador;

    @Column(name = "ani_anio")
    private Integer anio;

    @Column(name = "ani_meta", columnDefinition = "Decimal(20,2)")
    private BigDecimal meta;

    @Size(min = 0, max = 12)
    @OneToMany(mappedBy = "anioIndicador")
    private List<ValorIndicador> valoresIndicadores;

    //Auditoria
    @Column(name = "ani_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ani_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ani_version")
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

    public ProgramaIndicador getProgramaIndicador() {
        return programaIndicador;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public List<ValorIndicador> getValoresIndicadores() {
        return valoresIndicadores;
    }

    public void setValoresIndicadores(List<ValorIndicador> valoresIndicadores) {
        this.valoresIndicadores = valoresIndicadores;
    }

    public void setProgramaIndicador(ProgramaIndicador programaIndicador) {
        this.programaIndicador = programaIndicador;
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
        final AnioIndicador other = (AnioIndicador) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
