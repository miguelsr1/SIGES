package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SgNominaDocente implements Serializable {

    private SgPersonalSedeEducativa nomPersonal;
    private Set<SgGrado> nomGradoUnicoDocente;
    private List<SgNominaGradoMaterias> nomMateriasPorGrado;


    public SgNominaDocente() {
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

    @JsonIgnore
    public String getMateriasPorGrado(){
        StringBuilder sb = new StringBuilder();
        
        
        if (nomGradoUnicoDocente != null){
            if ((nomMateriasPorGrado == null || nomMateriasPorGrado.isEmpty()) && nomGradoUnicoDocente.size() == 1){
                sb.append("Todos los componentes");
            } else {
                for (SgGrado g : nomGradoUnicoDocente){
                    sb.append("<b>"+g.getGraNombre()+"</b>").append(": Todos los componentes").append("<br/>");
                }
            }
        }
        if (nomMateriasPorGrado != null){
            if ((nomGradoUnicoDocente == null || nomGradoUnicoDocente.isEmpty()) && nomMateriasPorGrado.size() == 1){
                 sb.append(nomMateriasPorGrado.get(0).getNomMaterias().stream().map(c -> c.getCpeNombre()).collect(Collectors.joining(", ")));
            } else {
                Collections.sort(nomMateriasPorGrado, (s1, s2) -> s1.getNomGrado().getGraOrden().compareTo(s2.getNomGrado().getGraOrden()));
                for (SgNominaGradoMaterias g : nomMateriasPorGrado){
                    sb.append("<b>"+g.getNomGrado().getGraNombre()+"</b>").append(": ")
                            .append(g.getNomMaterias().stream().map(c -> c.getCpeNombre()).collect(Collectors.joining(", ")))
                            .append("<br/>");
                }
            }
        }   
        return sb.toString();  
    }

    @JsonIgnore
    public String getGradosNombres() {
        Set<SgGrado> gra = new HashSet<>();
        if (nomGradoUnicoDocente != null){
            gra.addAll(nomGradoUnicoDocente);
        }
        if (nomMateriasPorGrado != null){
            gra.addAll(nomMateriasPorGrado.stream().map(n -> n.getNomGrado()).collect(Collectors.toSet()));
        }
        if (gra != null) {
            return gra.stream()
                    .sorted((s1, s2) -> s1.getGraOrden().compareTo(s2.getGraOrden()))
                    .map(g -> g.getGraNombre())    
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

}
