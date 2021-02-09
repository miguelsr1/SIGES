package sv.gob.mined.siges.web.converters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.beans.ApplicationBean;

@Named
@ApplicationScoped
public class LongToDateTimeConverter implements javax.faces.convert.Converter {

    @Inject
    private ApplicationBean aplicacionBean;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        if (modelValue == null) {
            return "";
        }

        if (modelValue instanceof Long) {
            LocalDateTime value = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) modelValue), ZoneId.ofOffset("GMT", ZoneOffset.ofHours(-6)));
            return getFormatter().format(ZonedDateTime.of(value, ZoneOffset.UTC));
        } else {
            throw new ConverterException(new FacesMessage(modelValue + " is not a valid Long"));
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }
        return new ConverterException("NOT IMPLEMENTED");
    }

    private DateTimeFormatter getFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getPattern(), getLocale());
        ZoneId zone = getZoneId();
        return (zone != null) ? formatter.withZone(zone) : formatter;
    }

    private String getPattern() {
        return aplicacionBean.getPatternFechaHora();
    }

    private Locale getLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    private ZoneId getZoneId() {
        return ZoneId.ofOffset("GMT", ZoneOffset.ofHours(0));
    }

}
