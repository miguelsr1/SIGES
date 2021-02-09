/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoNormalizable;
import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "sg_opciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Cache(expiry = 150000)
public class SgOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "opc_pk")
    private Long opcPk;
    
    @Size(max = 4)
    @Column(name = "opc_codigo",length = 4)
    private String opcCodigo;
    
    @Size(max = 500)
    @Column(name = "opc_nombre",length = 500)
    @AtributoNormalizable
    private String opcNombre;
    
    @Column(name = "opc_version")
    @Version
    private Integer opcVersion;
    
    @JoinColumn(name = "opc_modalidad_fk", referencedColumnName = "mod_pk")
    @ManyToOne(optional = false)
    private SgModalidad opcModalidad;   

    public SgOpcion() {
    }

    public SgOpcion(Long opcPk, Integer opcVersion) {
        this.opcPk = opcPk;
        this.opcVersion = opcVersion;
    }

    public Long getOpcPk() {
        return opcPk;
    }

    public void setOpcPk(Long opcPk) {
        this.opcPk = opcPk;
    }

    public String getOpcCodigo() {
        return opcCodigo;
    }

    public void setOpcCodigo(String opcCodigo) {
        this.opcCodigo = opcCodigo;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }

    public Integer getOpcVersion() {
        return opcVersion;
    }

    public void setOpcVersion(Integer opcVersion) {
        this.opcVersion = opcVersion;
    }

    public SgModalidad getOpcModalidad() {
        return opcModalidad;
    }

    public void setOpcModalidad(SgModalidad opcModalidad) {
        this.opcModalidad = opcModalidad;
    }
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opcPk != null ? opcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgOpcion)) {
            return false;
        }
        SgOpcion other = (SgOpcion) object;
        if ((this.opcPk == null && other.opcPk != null) || (this.opcPk != null && !this.opcPk.equals(other.opcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesOpcion[ opcPk=" + opcPk + " ]";
    }
    
}
