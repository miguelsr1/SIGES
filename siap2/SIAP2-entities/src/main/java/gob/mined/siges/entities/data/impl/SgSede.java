package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.data.impl.Direcciones;
import gob.mined.siap2.entities.enums.TipoSede;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_sedes")
@Cache(expiry = 150000)
//@Cacheable(false)
public class SgSede implements Serializable{
    
    @Id
    @Column(name = "sed_pk")
    private Integer id;
    
    @Column(name = "sed_codigo")
    private String codigo;
    
    @Column(name = "sed_nombre")
    private String nombre;
    
    @Column(name = "sed_tipo")
    @Enumerated(EnumType.STRING)
    private TipoSede tipo;
    
    @JoinColumn(name = "sed_clasificacion_fk")
    @ManyToOne
    private SgClasificacion sedClasificacion;
    
    @ManyToOne
    @JoinColumn(name = "sed_direccion_fk") 
    private Direcciones direccion;

    @OneToMany(mappedBy = "oaeSedeFk", fetch = FetchType.LAZY)
    private List<SgOrganismoAdministracionEscolar> organismosAdministracionEscolar;

    @OneToMany(mappedBy = "sduSede", fetch = FetchType.LAZY)
    private List<SgServicioEducativo> sedServicioEducativo;
    
    @Column(name = "sed_version")
    @Version
    private Integer version;
        
    public SgSede() {
    }

    public SgSede(Integer sedPk) {
        this.id = sedPk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoSede getTipo() {
        return tipo;
    }

    public void setTipo(TipoSede tipo) {
        this.tipo = tipo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Direcciones getDireccion() {
        return direccion;
    }

    public void setDireccion(Direcciones direccion) {
        this.direccion = direccion;
    }

    public SgClasificacion getSedClasificacion() {
        return sedClasificacion;
    }

    public void setSedClasificacion(SgClasificacion sedClasificacion) {
        this.sedClasificacion = sedClasificacion;
    }

    public List<SgOrganismoAdministracionEscolar> getOrganismosAdministracionEscolar() {
        return organismosAdministracionEscolar;
    }

    public void setOrganismosAdministracionEscolar(List<SgOrganismoAdministracionEscolar> organismosAdministracionEscolar) {
        this.organismosAdministracionEscolar = organismosAdministracionEscolar;
    }

    public List<SgServicioEducativo> getSedServicioEducativo() {
        return sedServicioEducativo;
    }

    public void setSedServicioEducativo(List<SgServicioEducativo> sedServicioEducativo) {
        this.sedServicioEducativo = sedServicioEducativo;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final SgSede other = (SgSede) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "SgSedes{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", tipo=" + tipo + ", direccion=" + direccion + '}';
    }

    
    
    

    

    
}
