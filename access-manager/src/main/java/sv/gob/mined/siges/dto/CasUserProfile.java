/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

/**
 *
 * @author USUARIO
 */
public class CasUserProfile {
    
    private String id;
    private Object attributes;
    private String service;
    private String client_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "CasUserProfile{" + "id=" + id + ", attributes=" + attributes + ", service=" + service + ", client_id=" + client_id + '}';
    }

    
    
    
}
