package com.app.autocommitwithcrawling.config;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfig {
    @Bean
    public FlexmarkHtmlConverter flexmarkHtmlConverter(){
        return FlexmarkHtmlConverter.builder().build();
    }


}
