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
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "sg_secciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Cache(expiry = 150000)
public class SgSeccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sec_pk", nullable = false)
    private Long secPk;

    @Size(max = 60)
    @Column(name = "sec_codigo")
    private String secCodigo;

    @Size(max = 255)
    @Column(name = "sec_nombre")
    private String secNombre;

    @JoinColumn(name = "sec_servicio_educativo_fk", referencedColumnName = "sdu_pk")
    @ManyToOne(optional = false)
    private SgServicioEducativo secServicioEducativo;

    @JoinColumn(name = "sec_plan_estudio_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPlanEstudio secPlanEstudio;

    @Column(name = "sec_version")
    @Version
    private Integer secVersion;

    public SgSeccion() {
    }

    public SgSeccion(Long secPk, Integer secVersion) {
        this.secPk = secPk;
        this.secVersion = secVersion;
    }

    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public SgPlanEstudio getSecPlanEstudio() {
        return secPlanEstudio;
    }

    public void setSecPlanEstudio(SgPlanEstudio secPlanEstudio) {
        this.secPlanEstudio = secPlanEstudio;
    }

    public SgServicioEducativo getSecServicioEducativo() {
        return secServicioEducativo;
    }

    public void setSecServicioEducativo(SgServicioEducativo secServicioEducativo) {
        this.secServicioEducativo = secServicioEducativo;
    }

    public Integer getSecVersion() {
        return secVersion;
    }

    public void setSecVersion(Integer secVersion) {
        this.secVersion = secVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.secPk);
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
        final SgSeccion other = (SgSeccion) obj;
        if (!Objects.equals(this.secPk, other.secPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSeccion{" + "secPk=" + secPk + ", secVersion=" + secVersion + '}';
    }

    

}
