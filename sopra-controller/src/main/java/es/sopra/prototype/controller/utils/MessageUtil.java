package es.sopra.prototype.controller.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
public class MessageUtil {

    @Autowired
    protected transient MessageSource messageSource;

    /**
     * Function to get message from a key
     *
     * @param key
     * @return
     */
    public String getMessage( String key ) {
        return messageSource.getMessage( key, null, LocaleContextHolder.getLocale() );
    }
}
