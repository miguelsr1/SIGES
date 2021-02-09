package sv.gob.mined.siges.web.dto;

import java.time.LocalDate;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;

public class SgAlcanceTreeNode {

    private LocalDate fechaMatricula;
    private Object objeto;
    private String type;
    private SgGrado reportaEnGrado; //Utilizado solamente por type 'grado'

    public SgAlcanceTreeNode() {
    }
    
    

    public LocalDate getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(LocalDate fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SgGrado getReportaEnGrado() {
        return reportaEnGrado;
    }

    public void setReportaEnGrado(SgGrado reportaEnGrado) {
        this.reportaEnGrado = reportaEnGrado;
    }
    
    

    
    public Long getPk() {
        if (type.equals("org")) {
            SgOrganizacionCurricular org = (SgOrganizacionCurricular) objeto;
            return org.getOcuPk();
        } else if (type.equals("nivel")) {
            SgNivel niv = (SgNivel) objeto;
            return niv.getNivPk();
        } else if (type.equals("ciclo")) {
            SgCiclo ciclo = (SgCiclo) objeto;
            return ciclo.getCicPk();
        } else if (type.equals("modalidad")) {
            SgModalidad mod = (SgModalidad) objeto;
            return mod.getModPk();
        } else if (type.equals("modalidad_atencion")) {
            SgModalidadAtencion modaten = (SgModalidadAtencion) objeto;
            return modaten.getMatPk();
        } else if (type.equals("submodalidad_atencion")) {
            SgSubModalidadAtencion smo = (SgSubModalidadAtencion) objeto;
            return smo.getSmoPk();
        } else if (type.equals("grado")) {
            SgGrado gra = (SgGrado) objeto;
            return gra.getGraPk();
        } else {
            return null;
        }
    }

}
