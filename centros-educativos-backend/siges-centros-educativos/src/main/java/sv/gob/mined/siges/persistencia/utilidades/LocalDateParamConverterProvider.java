/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author fabricio
 */
@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public <T> ParamConverter<T> getConverter(
            Class<T> rawType, Type genericType, Annotation[] antns) {

        if (LocalDate.class == rawType) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String string) {
                    try {
                        LocalDate localDate = LocalDate.parse(string, formatter);
                        return rawType.cast(localDate);
                    } catch (Exception ex) {
                        throw new BadRequestException(ex);
                    }
                }

                @Override
                public String toString(T t) {
                    LocalDate localDate = (LocalDate) t;
                    return formatter.format(localDate);
                }
            };
        }

        return null;
    }
}