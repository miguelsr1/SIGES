package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;

public class SgNominaDocente implements Serializable {
    
    private SgPersonalSedeEducativa nomPersonal;
    private Set<SgGrado> nomGradoUnicoDocente;
    private List<SgNominaGradoMaterias> nomMateriasPorGrado;

    public SgNominaDocente() {
        nomGradoUnicoDocente = new HashSet<>();
        nomMateriasPorGrado = new ArrayList<>();
    }

    public SgPersonalSedeEducativa getNomPersonal() {
        return nomPersonal;
    }

    public void setNomPersonal(SgPersonalSedeEducativa nomPersonal) {
        this.nomPersonal = nomPersonal;
    }

    public Set<SgGrado> getNomGradoUnicoDocente() {
        return nomGradoUnicoDocente;
    }

    public void setNomGradoUnicoDocente(Set<SgGrado> nomGradoUnicoDocente) {
        this.nomGradoUnicoDocente = nomGradoUnicoDocente;
    }

    public List<SgNominaGradoMaterias> getNomMateriasPorGrado() {
        return nomMateriasPorGrado;
    }

    public void setNomMateriasPorGrado(List<SgNominaGradoMaterias> nomMateriasPorGrado) {
        this.nomMateriasPorGrado = nomMateriasPorGrado;
    }

    
  
}
