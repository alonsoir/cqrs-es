package sopra.prototype.config;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    private static final Logger LOG = Logger.getLogger( ApplicationConfig.class );

    @Value( "${spring.locale.default}" )
    private String locale;

    @Value( "${spring.messages.basename}" )
    private String basename;

    /**
     * Message Resource declaration.
     *
     * @return MessageRessource
     */
    @Bean
    public MessageSource messageSource() {
        LOG.info( "Set Locale to: " + locale );
        Locale.setDefault( new Locale( locale ) );

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename( basename );
        source.setUseCodeAsDefaultMessage( true );
        return source;
    }

    /**
     * Define LocaleResolver
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale( new Locale( locale ) );
        return slr;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors( InterceptorRegistry registry ) {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName( "lang" );
        registry.addInterceptor( lci );
    }
}
