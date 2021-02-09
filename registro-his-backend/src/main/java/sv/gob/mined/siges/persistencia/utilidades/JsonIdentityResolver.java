/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sofis Solutions
 */
public class JsonIdentityResolver implements ObjectIdResolver {
    
    protected Map<IdKey,Object> _items;
    
    public JsonIdentityResolver() { }

    @Override
    public void bindItem(IdKey id, Object ob) {
        if (_items == null) {
            _items = new HashMap<ObjectIdGenerator.IdKey, Object>();
        } else if (_items.containsKey(id)) {
            return; //Dont throw exception
        }
        _items.put(id, ob);
    }
    
     @Override
    public Object resolveId(IdKey id) {
        return (_items == null) ? null : _items.get(id);
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == getClass();
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        // 19-Dec-2014, tatu: Important: must re-create without existing mapping; otherwise bindings leak
        //    (and worse, cause unnecessary memory retention)
        return new JsonIdentityResolver();
}

}
