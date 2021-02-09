package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
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

@Entity
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_direcciones_departamentales")
@EntityListeners({DecoratorEntityListener.class})
public class DireccionDepartamental implements Serializable{
    
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_direccion_departamental_ded_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_direccion_departamental_ded_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_direccion_departamental_ded_pk_seq")
    @Column(name = "ded_pk")
    private Integer pk;
    
    @Column(name = "ded_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ded_departamento_fk")
    private Departamento departamento;

    

    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return "DireccionDepartamental{" + "pk=" + pk + ", nombre=" + nombre + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.pk);
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
        final DireccionDepartamental other = (DireccionDepartamental) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    

    
    
    
    
}
