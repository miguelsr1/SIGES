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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pla_de_ins_nodo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class NodoPlantillaDeInsumos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pla_de_insumos_gen")
    @SequenceGenerator(name = "seq_pla_de_insumos_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pla_de_insumos", allocationSize = 1)
    @Column(name = "pla_id")
    private Integer id;

    @Column(name = "pla_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pla_padre")
    private NodoPlantillaDeInsumos padre;

    @OneToMany(mappedBy = "padre")
    private List<NodoPlantillaDeInsumos> hijos;

    @OneToMany(mappedBy = "plnatillaDeInsumos")
    private List<Insumo> insumos;

    //Auditoria
    @Column(name = "pla_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pla_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pla_version")
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

    public List<Insumo> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<Insumo> insumos) {
        this.insumos = insumos;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public NodoPlantillaDeInsumos getPadre() {
        return padre;
    }

    public void setPadre(NodoPlantillaDeInsumos padre) {
        this.padre = padre;
    }

    public List<NodoPlantillaDeInsumos> getHijos() {
        return hijos;
    }

    public void setHijos(List<NodoPlantillaDeInsumos> hijos) {
        this.hijos = hijos;
    }

    public Integer getVersion() {
        return version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        final NodoPlantillaDeInsumos other = (NodoPlantillaDeInsumos) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
