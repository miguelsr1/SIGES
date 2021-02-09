/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

/**
 *
 * @author USUARIO
 */
public class SgUnirPersona {
    
    private Long personaApk; //Persona a es la que queda y se le pasan algunos datos de b
    private Long personaBpk; //Persona b es la persona que se elimina

    public Long getPersonaApk() {
        return personaApk;
    }

    public void setPersonaApk(Long personaApk) {
        this.personaApk = personaApk;
    }

    public Long getPersonaBpk() {
        return personaBpk;
    }

    public void setPersonaBpk(Long personaBpk) {
        this.personaBpk = personaBpk;
    }



    

  
    
}
