/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rangos_de_matricula")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class RangoMatricula implements Serializable{
    
    private static final long serialVersionUID = 1L;
   
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_rangos_matricula", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rangos_matricula", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rangos_matricula")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_rel_anio_presupuesto") 
    private RelacionGesPresEsAnioFiscal relacionAnioPresupuesto;
    
    @Column(name = "rango")
    private BigDecimal rango;
    
    @Column(name = "valor")
    private Integer valor;
    
    @Column(name = "ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "version")
    @Version
    private Integer version;

    
    
    
    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RelacionGesPresEsAnioFiscal getRelacionAnioPresupuesto() {
        return relacionAnioPresupuesto;
    }

    public void setRelacionAnioPresupuesto(RelacionGesPresEsAnioFiscal relacionAnioPresupuesto) {
        this.relacionAnioPresupuesto = relacionAnioPresupuesto;
    }

    public BigDecimal getRango() {
        return rango;
    }

    public void setRango(BigDecimal rango) {
        this.rango = rango;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    
    
}
