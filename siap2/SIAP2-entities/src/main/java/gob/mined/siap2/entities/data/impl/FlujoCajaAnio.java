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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_flujo_caja_anio")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class FlujoCajaAnio implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_flujo_cj_grupo_gen")
    @SequenceGenerator(name = "seq_flujo_cj_grupo_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_flujo_cj_grupo", allocationSize = 1)
    @Column(name = "flu_id")
    private Integer id;
    
    @Column(name = "flu_anio")
    private Integer anio;
    
   
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_fluj_anio_fluj_mes",
            joinColumns = {
                @JoinColumn(name = "rel_anio")},
            inverseJoinColumns = {
                @JoinColumn(name = "rel_mes")}
    )
    @OrderColumn(name = "mes")
    private List<POFlujoCajaMenusal> flujoCajaMenusal;
    
    @OneToMany(mappedBy = "anioPago", cascade = CascadeType.ALL)
    private List<ActaContrato> actas;

    //Auditoria
    @Column(name = "flu_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "flu_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "flu_version")
    @Version
    private Integer version;
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

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

    public String getUltUsuario() {
        return ultUsuario;
    }

    public List<POFlujoCajaMenusal> getFlujoCajaMenusal() {
        return flujoCajaMenusal;
    }

    public void setFlujoCajaMenusal(List<POFlujoCajaMenusal> flujoCajaMenusal) {
        this.flujoCajaMenusal = flujoCajaMenusal;
    }

    public FlujoCajaAnio() {
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

    public List<ActaContrato> getActas() {
        return actas;
    }

    public void setActas(List<ActaContrato> actas) {
        this.actas = actas;
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
        final FlujoCajaAnio other = (FlujoCajaAnio) obj;
        if (this.id!= null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }
    
    
    
    
    
}
