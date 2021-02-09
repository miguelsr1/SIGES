
package sv.gob.mined.siges.dto;

import java.util.Objects;


public class SgIndicadorNota {
    
    private String sexo;
    private String componenteNombre;
    private String componenteTipo;
    private Long componenteId;
    private Double promedio;
    private String moda;

    public SgIndicadorNota() {
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getComponenteNombre() {
        return componenteNombre;
    }

    public void setComponenteNombre(String componenteNombre) {
        this.componenteNombre = componenteNombre;
    }

    public String getComponenteTipo() {
        return componenteTipo;
    }

    public void setComponenteTipo(String componenteTipo) {
        this.componenteTipo = componenteTipo;
    }

    public Long getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(Long componenteId) {
        this.componenteId = componenteId;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public String getModa() {
        return moda;
    }

    public void setModa(String moda) {
        this.moda = moda;
    }

    
    

  

    @Override
    public int hashCode() {
        int hash = 7;
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
        final SgIndicadorNota other = (SgIndicadorNota) obj;
        if (!Objects.equals(this.sexo, other.sexo)) {
            return false;
        }
        if (!Objects.equals(this.componenteId, other.componenteId)) {
            return false;
        }
        return true;
    }

    
    

    

    
    
    
    
    
}
