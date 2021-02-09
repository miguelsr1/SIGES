/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "sg_planes_estudio", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Cache(expiry = 150000)
public class SgPlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pes_pk")
    private Long pesPk;
    
    @Size(max = 15)
    @Column(name = "pes_codigo",length = 15)
    private String pesCodigo;
    
    @Size(max = 255)
    @Column(name = "pes_nombre",length = 255)
    private String pesNombre;
    
    @Column(name = "pes_version")
    @Version
    private Integer pesVersion;
    
    @JoinColumn(name = "pes_relacion_modalidad_fk", referencedColumnName = "rea_pk")
    @ManyToOne(optional = false)
    private SgRelModEdModAten pesRelacionModalidad;
    
    @JoinColumn(name = "pes_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne(optional = true)
    private SgOpcion pesOpcion;

    public SgPlanEstudio(Long pesPk, Integer pesVersion) {
        this.pesPk = pesPk;
        this.pesVersion = pesVersion;
    }
     
    public SgPlanEstudio() {
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
    }

    public SgRelModEdModAten getPesRelacionModalidad() {
        return pesRelacionModalidad;
    }

    public void setPesRelacionModalidad(SgRelModEdModAten pesRelacionModalidad) {
        this.pesRelacionModalidad = pesRelacionModalidad;
    }

    public SgOpcion getPesOpcion() {
        return pesOpcion;
    }

    public void setPesOpcion(SgOpcion pesOpcion) {
        this.pesOpcion = pesOpcion;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pesPk != null ? pesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlanEstudio)) {
            return false;
        }
        SgPlanEstudio other = (SgPlanEstudio) object;
        if ((this.pesPk == null && other.pesPk != null) || (this.pesPk != null && !this.pesPk.equals(other.pesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlanesEstudio[ pesPk=" + pesPk + " ]";
    }
    
}
