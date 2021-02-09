/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class StringArrayType implements UserType {
    private final int[] arrayTypes = new int[] { Types.ARRAY };

 @Override
 public int[] sqlTypes() {
    return arrayTypes;
 }

 @Override
 public Class<String[]> returnedClass() {
    return String[].class;
 }

 @Override
 public boolean equals(Object x, Object y) throws HibernateException {
    return x == null ? y == null : x.equals(y);
 }

 @Override
 public int hashCode(Object x) throws HibernateException {
    return x == null ? 0 : x.hashCode();
 }

 @Override
 public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    // get the first column names
    if (names != null && names.length > 0 && rs != null && rs.getArray(names[0]) != null) {
        String[] results = (String[]) rs.getArray(names[0]).getArray();
        return results;
    }
    return null;
 }

 @Override
 public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    // setting the column with string array
    if (value != null && st != null) {
        String[] castObject = (String[]) value;
        Array array = session.connection().createArrayOf("text", castObject);
        st.setArray(index, array);
    } else if (st != null) {
        st.setNull(index, arrayTypes[0]);
    }
 }

 @Override
 public Object deepCopy(Object value) throws HibernateException {
    return value == null ? null : ((String[]) value).clone();
 }

 @Override
 public boolean isMutable() {
    return false;
 }

 @Override
 public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) value;
 }

 @Override
 public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
 }

 @Override
 public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
 }
}
