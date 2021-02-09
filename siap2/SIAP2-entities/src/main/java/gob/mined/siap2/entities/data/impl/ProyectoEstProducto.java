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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_proy_est_producto")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProyectoEstProducto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proy_est_prod_gen")
    @SequenceGenerator(name = "seq_proy_est_prod_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_proy_est_prod", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @Column(name = "pro_numero")
    private Integer numero;
    
    @ManyToOne
    @JoinColumn(name = "pro_proy_estruc")
    private ProyectoEstructura proyectoEstructura;
    
    @ManyToOne
    @JoinColumn(name = "pro_unidad_tecnica")
    protected UnidadTecnica unidadTecnica;
    
    @ManyToOne
    @JoinColumn(name = "pro_ind_producto")
    protected Indicador producto;
        
    @Column(name = "pro_cantidad", columnDefinition = "Decimal(20,2)")
    private BigDecimal cantidad;

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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public Indicador getProducto() {
        return producto;
    }

    public void setProducto(Indicador producto) {
        this.producto = producto;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    
    public ProyectoEstructura getProyectoEstructura() {
        return proyectoEstructura;
    }

    public void setProyectoEstructura(ProyectoEstructura proyectoEstructura) {
        this.proyectoEstructura = proyectoEstructura;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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
        final ProyectoEstProducto other = (ProyectoEstProducto) obj;
        if (this.id!= null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this==other);
    }

}
