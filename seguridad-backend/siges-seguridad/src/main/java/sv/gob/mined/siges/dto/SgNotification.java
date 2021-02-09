/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.util.List;
import sv.gob.mined.siges.kafka.EnumTipoDestinoNotificacion;
import sv.gob.mined.siges.kafka.EnumTipoNotificacion;

public class SgNotification {

    private List<EnumTipoNotificacion> tipo;
    private EnumTipoDestinoNotificacion tipoDestino;
    private List<String> destinos;
    
    private String asunto;
    private String cuerpo;
    private List<SgNotificationFile> files;

    public EnumTipoDestinoNotificacion getTipoDestino() {
        return tipoDestino;
    }

    public void setTipoDestino(EnumTipoDestinoNotificacion tipoDestino) {
        this.tipoDestino = tipoDestino;
    }


    public List<String> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<String> destinos) {
        this.destinos = destinos;
    }  

    public List<EnumTipoNotificacion> getTipo() {
        return tipo;
    }

    public void setTipo(List<EnumTipoNotificacion> tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public List<SgNotificationFile> getFiles() {
        return files;
    }

    public void setFiles(List<SgNotificationFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "SgNotification{" + "tipo=" + tipo + ", tipoDestino=" + tipoDestino + ", destinos=" + destinos + ", asunto=" + asunto + ", cuerpo=" + cuerpo + '}';
    }
    


}
