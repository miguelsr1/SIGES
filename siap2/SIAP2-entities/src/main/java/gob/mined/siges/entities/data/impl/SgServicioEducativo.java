/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

/**
*
* NOTA: Si se agrega un nuevo campo a esta entidad, verificar el método copiarServicios en la clase ServicioEducativoBean.java
*       de ser necesario agregar dicho campo al filtro IncluirCampos
*
*/

@Entity
@Table(name = "sg_servicio_educativo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Cache(expiry = 150000)
public class SgServicioEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sdu_pk")
    private Long sduPk;       
    
    @JoinColumn(name = "sdu_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(optional = false)
    private SgSede sduSede;   
    
    @Column(name = "sdu_version")
    @Version
    private Integer sduVersion;
    
    @JoinColumn(name = "sdu_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne
    private SgOpcion sduOpcion;
    
    @OneToMany(mappedBy = "secServicioEducativo")
    private List<SgSeccion> sduSeccion;
    
    @Transient
    private Boolean sduOrigenExterno;

    
    public SgServicioEducativo() {
    }

    public SgServicioEducativo(Long sduPk) {
        this.sduPk = sduPk;
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public SgSede getSduSede() {
        return sduSede;
    }

    public void setSduSede(SgSede sduSede) {
        this.sduSede = sduSede;
    }

    public Integer getSduVersion() {
        return sduVersion;
    }

    public void setSduVersion(Integer sduVersion) {
        this.sduVersion = sduVersion;
    }

    public SgOpcion getSduOpcion() {
        return sduOpcion;
    }

    public void setSduOpcion(SgOpcion sduOpcion) {
        this.sduOpcion = sduOpcion;
    }

    public List<SgSeccion> getSduSeccion() {
        return sduSeccion;
    }

    public void setSduSeccion(List<SgSeccion> sduSeccion) {
        this.sduSeccion = sduSeccion;
    }

    public Boolean getSduOrigenExterno() {
        return sduOrigenExterno;
    }

    public void setSduOrigenExterno(Boolean sduOrigenExterno) {
        this.sduOrigenExterno = sduOrigenExterno;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.sduPk);
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
        final SgServicioEducativo other = (SgServicioEducativo) obj;
        if (!Objects.equals(this.sduPk, other.sduPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo[ sduPk=" + sduPk + " ]";
    }
    
}
