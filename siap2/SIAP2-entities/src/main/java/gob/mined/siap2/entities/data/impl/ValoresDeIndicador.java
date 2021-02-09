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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * ESTA TABLA TIENE UNA  UNIQUE Constraint PARA QUE NO EXISTAN VALORES REPETIDOS
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_valor_de_indicador")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ValoresDeIndicador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_valor_ind_gen")
    @SequenceGenerator(name = "seq_valor_ind_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_valor_ind", allocationSize = 1)
    @Column(name = "val_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "val_meta_indicador")
    private MetaIndicador metaIndicador;

    @Column(name = "val_meta", columnDefinition = "Decimal(20,2)")
    private BigDecimal meta;
    
    @Column(name = "val_valor", columnDefinition = "Decimal(20,2)")
    private BigDecimal valor;

    @Lob
    @Column(name = "val_alcance_lim")
    private String alcanceYLimitante;
    
    @Lob
    @Column(name = "val_medio_verif")
    private String medioVerificacion;
    
    
    
    @Column(name = "posicion")
    private Integer posicionjpa;
    
   
    //Auditoria
    @Column(name = "val_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "val_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "val_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public MetaIndicador getMetaIndicador() {
        return metaIndicador;
    }

    public void setMetaIndicador(MetaIndicador metaIndicador) {
        this.metaIndicador = metaIndicador;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    
    public Integer getPosicionjpa() {
        return posicionjpa;
    }

    public void setPosicionjpa(Integer posicionjpa) {
        this.posicionjpa = posicionjpa;
    }


    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public String getAlcanceYLimitante() {
        return alcanceYLimitante;
    }

    public void setAlcanceYLimitante(String alcanceYLimitante) {
        this.alcanceYLimitante = alcanceYLimitante;
    }

    public String getMedioVerificacion() {
        return medioVerificacion;
    }

    public void setMedioVerificacion(String medioVerificacion) {
        this.medioVerificacion = medioVerificacion;
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
        final ValoresDeIndicador other = (ValoresDeIndicador) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
