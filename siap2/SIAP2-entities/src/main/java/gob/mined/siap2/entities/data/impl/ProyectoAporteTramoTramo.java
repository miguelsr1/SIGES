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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proy_aportramo_tramo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProyectoAporteTramoTramo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proy_cat_tramo_gen")
    @SequenceGenerator(name = "seq_proy_cat_tramo_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proy_cat_tramo", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_categoria_id")
    private ProyectoCategoriaConvenio categoria;

    
    @Column(name = "pro_hasta", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoHasta;        
    @Column(name = "pro_temp_hasta", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoHastaEnConstruccion;  
    
    @OneToMany(mappedBy = "tramo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoParipassuTramo> paripassus;
    
    @Column(name = "pro_posicion")
    protected Integer posicion;

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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public BigDecimal getMontoHastaEnConstruccion() {
        return montoHastaEnConstruccion;
    }

    public void setMontoHastaEnConstruccion(BigDecimal montoHastaEnConstruccion) {
        this.montoHastaEnConstruccion = montoHastaEnConstruccion;
    }

    public List<ProyectoParipassuTramo> getParipassus() {
        return paripassus;
    }

    public void setParipassus(List<ProyectoParipassuTramo> paripassus) {
        this.paripassus = paripassus;
    }

    public ProyectoCategoriaConvenio getCategoria() {
        return categoria;
    }

    public void setCategoria(ProyectoCategoriaConvenio categoria) {
        this.categoria = categoria;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public BigDecimal getMontoHasta() {
        return montoHasta;
    }

    public void setMontoHasta(BigDecimal montoHasta) {
        this.montoHasta = montoHasta;
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
        final ProyectoAporteTramoTramo other = (ProyectoAporteTramoTramo) obj;
        if ((this.id!= null )&&( other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }

}
