package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SgNominaGradoMaterias implements Serializable {
    
    private SgGrado nomGrado;
    private Set<SgComponentePlanEstudio> nomMaterias;

    public SgNominaGradoMaterias() {
        nomMaterias = new HashSet<>();
    }

    public SgGrado getNomGrado() {
        return nomGrado;
    }

    public void setNomGrado(SgGrado nomGrado) {
        this.nomGrado = nomGrado;
    }

    public Set<SgComponentePlanEstudio> getNomMaterias() {
        return nomMaterias;
    }

    public void setNomMaterias(Set<SgComponentePlanEstudio> nomMaterias) {
        this.nomMaterias = nomMaterias;
    }
     
  
}