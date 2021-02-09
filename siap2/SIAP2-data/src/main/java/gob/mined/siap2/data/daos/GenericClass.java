/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;
public class GenericClass<T> {

     private final Class<T> type;

     public GenericClass(Class<T> type) {
          this.type = type;
     }

     public Class<T> getMyType() {
         return this.type;
     }
}
