/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoParipassu;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_cat_conv_proyecto")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public  class ProyectoCategoriaConvenio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ss_cat_conv_pro_gen")
    @SequenceGenerator(name = "ss_cat_conv_pro_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".ss_cat_conv_pro", allocationSize = 1)
    @Column(name = "cat_id")
    protected Integer id;    
      
    @ManyToOne
    @JoinColumn(name = "cat_proyecto")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "cat_cat_conv")
    private CategoriaConvenio categoriaConvenio;
    
    @Column(name = "cat_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    
    @Column(name = "cat_tipo_paripassu")
    @Enumerated(EnumType.STRING)
    private TipoParipassu tipoParipassu; 
    @Column(name = "cat_temp_tipo_paripassu")
    @Enumerated(EnumType.STRING)
    private TipoParipassu tipoParipassuEnConstruccion;
           
    @OrderColumn(name = "pro_posicion")
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProyectoAporteTramoTramo> tramos;
    
    
    
    
    @Column(name = "cat_posicion")
    protected Integer posicion;

    
    //Auditoria
    @Column(name = "cat_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cat_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cat_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
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

    public CategoriaConvenio getCategoriaConvenio() {
        return categoriaConvenio;
    }

    public void setCategoriaConvenio(CategoriaConvenio categoriaConvenio) {
        this.categoriaConvenio = categoriaConvenio;
    }

    public TipoParipassu getTipoParipassu() {
        return tipoParipassu;
    }

    public void setTipoParipassu(TipoParipassu tipoParipassu) {
        this.tipoParipassu = tipoParipassu;
    }



    public List<ProyectoAporteTramoTramo> getTramos() {
        return tramos;
    }

    public void setTramos(List<ProyectoAporteTramoTramo> tramos) {
        this.tramos = tramos;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public TipoParipassu getTipoParipassuEnConstruccion() {
        return tipoParipassuEnConstruccion;
    }

    public void setTipoParipassuEnConstruccion(TipoParipassu tipoParipassuEnConstruccion) {
        this.tipoParipassuEnConstruccion = tipoParipassuEnConstruccion;
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
        final ProyectoCategoriaConvenio other = (ProyectoCategoriaConvenio) obj;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }
    
    

}
