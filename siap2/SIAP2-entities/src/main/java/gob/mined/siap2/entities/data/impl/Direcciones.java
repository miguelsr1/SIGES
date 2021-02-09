package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_direcciones")
@EntityListeners({DecoratorEntityListener.class})
public class Direcciones implements Serializable{
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_direcciones_dir_pk_seq", sequenceName = Constantes.SCHEMA_CENTRO_EDUCATIVO + ".sg_direcciones_dir_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_direcciones_dir_pk_seq")
    @Column(name = "dir_pk")
    private Integer pk;
    
    @Column(name = "dir_direccion")
    private String dirDireccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dir_departamento") 
    private Departamento departamento;

    
    
    
    
    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    
    
}
