package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = Constantes.SCHEMA_CATALOGO, name = "sg_departamentos")
@EntityListeners({DecoratorEntityListener.class})
public class Departamento implements Serializable{
    
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_departamentos_dep_pk_seq", sequenceName = Constantes.SCHEMA_CATALOGO + ".sg_departamentos_dep_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_departamentos_dep_pk_seq")
    @Column(name = "dep_pk")
    private Integer pk;
    
    @Column(name = "dep_codigo")
    private String codigo;
    
    @Column(name = "dep_habilitado")
    private Boolean habilitado;
    
    @Column(name = "dep_nombre")
    private String nombre;

    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Departamento{" + "pk=" + pk + ", codigo=" + codigo + ", nombre=" + nombre + '}';
    }


    

    
    
}
