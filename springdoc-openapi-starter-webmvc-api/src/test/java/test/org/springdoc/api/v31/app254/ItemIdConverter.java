package test.org.springdoc.api.v31.app254;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemIdConverter implements Converter<String, ItemId> {

    @Override
    public ItemId convert(String source) {
        return ItemId.fromString(source);
    }
}
