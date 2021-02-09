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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proy_paripassu_tramo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProyectoParipassuTramo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proy_paripassu_t_gen")
    @SequenceGenerator(name = "seq_proy_paripassu_t_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proy_paripassu_t", allocationSize = 1)
    @Column(name = "pro_id")
    protected Integer id;

      
    @ManyToOne
    @JoinColumn(name = "pro_tramo")
    private ProyectoAporteTramoTramo tramo;

         
    @ManyToOne
    @JoinColumn(name = "pro_fuente_recursos")
    private FuenteRecursos fuenteRecursos;

    @Column(name = "pro_monto_paripassu", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoParipassu;
    @Column(name = "pro_formula_paripassu")
    private String formulaParipassu;

    @Column(name = "pro_temp_monto_paripassu", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoParipassuEnConstruccion;
    @Column(name = "pro_temp_formula_paripassu")
    private String formulaParipassuEnConstruccion;
    
    
    
    
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

    public ProyectoAporteTramoTramo getTramo() {
        return tramo;
    }

    public void setTramo(ProyectoAporteTramoTramo tramo) {
        this.tramo = tramo;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public BigDecimal getMontoParipassu() {
        return montoParipassu;
    }

    public void setMontoParipassu(BigDecimal montoParipassu) {
        this.montoParipassu = montoParipassu;
    }

    public String getFormulaParipassu() {
        return formulaParipassu;
    }

    public void setFormulaParipassu(String formulaParipassu) {
        this.formulaParipassu = formulaParipassu;
    }
    
    
    public BigDecimal getMontoParipassuEnConstruccion() {
        return montoParipassuEnConstruccion;
    }

    public void setMontoParipassuEnConstruccion(BigDecimal montoParipassuEnConstruccion) {
        this.montoParipassuEnConstruccion = montoParipassuEnConstruccion;
    }

    public String getFormulaParipassuEnConstruccion() {
        return formulaParipassuEnConstruccion;
    }

    public void setFormulaParipassuEnConstruccion(String formulaParipassuEnConstruccion) {
        this.formulaParipassuEnConstruccion = formulaParipassuEnConstruccion;
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
        final ProyectoParipassuTramo other = (ProyectoParipassuTramo) obj;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }
    
 
}
