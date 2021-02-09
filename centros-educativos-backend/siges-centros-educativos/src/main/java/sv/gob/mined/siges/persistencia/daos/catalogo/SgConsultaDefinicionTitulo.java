/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos.catalogo;


import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

public class SgConsultaDefinicionTitulo implements Serializable {
    private Long dtiPk;
    private String dtiCodigo;
    private String dtiNombre;
    private String dtiNombreCertificado;
    private Integer dtiVersion;

    public SgConsultaDefinicionTitulo() {
    }

 
    public Long getDtiPk() {
        return dtiPk;
    }

    public void setDtiPk(Long dtiPk) {
        this.dtiPk = dtiPk;
    }

    public String getDtiCodigo() {
        return dtiCodigo;
    }

    public void setDtiCodigo(String dtiCodigo) {
        this.dtiCodigo = dtiCodigo;
    }

    public String getDtiNombre() {
        return dtiNombre;
    }

    public void setDtiNombre(String dtiNombre) {
        this.dtiNombre = dtiNombre;
    }

   

    public Integer getDtiVersion() {
        return dtiVersion;
    }

    public void setDtiVersion(Integer dtiVersion) {
        this.dtiVersion = dtiVersion;
    }

    public String getDtiNombreCertificado() {
        return dtiNombreCertificado;
    }

    public void setDtiNombreCertificado(String dtiNombreCertificado) {
        this.dtiNombreCertificado = dtiNombreCertificado;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.dtiPk);
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
        final SgConsultaDefinicionTitulo other = (SgConsultaDefinicionTitulo) obj;
        if (!Objects.equals(this.dtiPk, other.dtiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDefinicionTitulo{" + "dtiPk=" + dtiPk + ", dtiCodigo=" + dtiCodigo + ", dtiNombre=" + dtiNombre + ", dtiNombreBusqueda=" +  ", dtiVersion=" + dtiVersion + '}';
    }
    
    

}
