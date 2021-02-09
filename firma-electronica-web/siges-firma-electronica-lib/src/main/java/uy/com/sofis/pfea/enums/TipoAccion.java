package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum TipoAccion {

    ADDLOCAL("Crear local"),
    ADDSALA("Crear sala"),
    ADDOFERTA("Crear oferta"),
    ADDCATEGORIA("Crear categoría"),
    ADDETIQUETA("Crear etiqueta"),
    ADDESPECTACULO("Crear espectáculo"),
    ADDFUNCION("Crear función"),
    ADDUSUARIO("Crear usuario"),
    ADDROL("Crear rol"),
    ADDBOLETERO("Crear boletero"),
    ADDDOCENTE("Crear docente"),
    ADDESTUDIANTE("Crear estudiante"),
    ADDBENEFICIARIO("Crear beneficiario"),
    ADDRESERVA("Crear reserva"),
    ADDCENTRO("Crear centro"),
    ADDTEXTO("Crear texto"),
    ADDCONFIGURACION("Crear configuración"),
    ADDHTML("Crear HTML"),
    ADDCOMUNICACION("Crear comunicacion"),
    ADDNOTIFICACION("Crear notificacion"),
    ADDHIJO("Crear hijo"),
    ADDDOCUMENTO("Crear Documento"),
    
    EDITLOCAL("Editar local"),
    EDITSALA("Editar sala"),
    EDITOFERTA("Editar oferta"),
    EDITCATEGORIA("Editar categoría"),
    EDITETIQUETA("Editar etiqueta"),
    EDITESPECTACULO("Editar espectáculo"),
    EDITFUNCION("Editar función"),
    EDITUSUARIO("Editar usuario"),
    EDITROL("Editar rol"),
    EDITBOLETERO("Editar boletero"),
    EDITDOCENTE("Editar docente"),
    EDITESTUDIANTE("Editar estudiante"),
    EDITBENEFICIARIO("Editar beneficiario"),
    EDITRESERVA("Editar reserva"),
    EDITCENTRO("Editar centro"),
    EDITTEXTO("Editar texto"),
    EDITCONFIGURACION("Editar configuración"),
    EDITHTML("Editar HTML"),
    EDITCOMUNICACION("Editar comunicacion"),
    EDITNOTIFICACION("Editar notificacion"),
    EDITHIJO("Editar hijo"),
    
    DELLOCAL("Eliminar local"),
    DELSALA("Eliminar sala"),
    DELOFERTA("Eliminar oferta"),
    DELCATEGORIA("Eliminar categoría"),
    DELETIQUETA("Eliminar etiqueta"),
    DELUSUARIO("Eliminar usuario"),
    DELROL("Eliminar rol"),
    DELBOLETERO("Eliminar boletero"),
    DELDOCENTE("Eliminar docente"),
    DELESTUDIANTE("Eliminar estudiante"),
    DELBENEFICIARIO("Eliminar beneficiario"),
    DELCENTRO("Eliminar centro"),
    DELTEXTO("Eliminar texto"),
    DELCONFIGURACION("Eliminar configuración"),
    DELFUNCION("Eliminar función"),
    DELHTML("Eliminar HTML"),
    DELCOMUNICACION("Eliminar comunicacion"),
    DELNOTIFICACION("Eliminar notificacion"),
    DELHIJO("Eliminar hijo"),
    DELRESERVA("Eliminar reserva"),
    
    CANCELESPECTACULO("Cancelar espectáculo"),
    CANCELFUNCION("Cancelar función"),
    CANCELRESERVA("Cancelar reserva"),
    
    LIBERARCUPOS("Liberar cupos");
    
    private final String nombre;

    TipoAccion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}
