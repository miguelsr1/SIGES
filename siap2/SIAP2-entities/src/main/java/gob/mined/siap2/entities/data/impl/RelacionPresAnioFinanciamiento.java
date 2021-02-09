package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Basic;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_pres_anio_financiamiento")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class RelacionPresAnioFinanciamiento implements Serializable {

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_rel_presanio_financiamiento", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_presanio_financiamiento", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_presanio_financiamiento")
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rel_anio_presupuesto")
    private RelacionGesPresEsAnioFiscal relAnioPresupuesto;
    
    @ManyToOne
    @JoinColumn(name = "fuente_financiamiento")
    private FuenteFinanciamiento fuenteFinanciamiento;
    
    @ManyToOne
    @JoinColumn(name = "fuente_recursos")
    private FuenteRecursos fuenteRecursos;

    @Column(name = "cifrado_presupuestario")
    private String cifradoPresupuestario;
    
    @Column(name = "monto_total_formulado")
    private BigDecimal montoTotalFormulado;
    
    @Column(name = "monto_total_aprobado")
    private BigDecimal montoTotalAprobado;
  
    @Column(name = "ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "version")
    @Version
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RelacionGesPresEsAnioFiscal getRelAnioPresupuesto() {
        return relAnioPresupuesto;
    }

    public void setRelAnioPresupuesto(RelacionGesPresEsAnioFiscal relAnioPresupuesto) {
        this.relAnioPresupuesto = relAnioPresupuesto;
    }

    public FuenteFinanciamiento getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCifradoPresupuestario() {
        return cifradoPresupuestario;
    }

    public void setCifradoPresupuestario(String cifradoPresupuestario) {
        this.cifradoPresupuestario = cifradoPresupuestario;
    }

    public BigDecimal getMontoTotalFormulado() {
        return montoTotalFormulado;
    }

    public void setMontoTotalFormulado(BigDecimal montoTotalFormulado) {
        this.montoTotalFormulado = montoTotalFormulado;
    }

    public BigDecimal getMontoTotalAprobado() {
        return montoTotalAprobado;
    }

    public void setMontoTotalAprobado(BigDecimal montoTotalAprobado) {
        this.montoTotalAprobado = montoTotalAprobado;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelacionPresAnioFinanciamiento other = (RelacionPresAnioFinanciamiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    
}
