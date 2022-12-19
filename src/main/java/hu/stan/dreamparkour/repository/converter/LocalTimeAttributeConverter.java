package hu.stan.dreamparkour.repository.converter;

import hu.stan.dreamparkour.common.constant.PluginConstants;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, String> {
     
    @Override
    public String convertToDatabaseColumn(final LocalTime localTime) {
        return localTime == null ? null : localTime.format(PluginConstants.TIME_FORMAT);
    }

    @Override
    public LocalTime convertToEntityAttribute(final String sqlTime) {
        return sqlTime == null ? null : LocalTime.parse(sqlTime);
    }
}