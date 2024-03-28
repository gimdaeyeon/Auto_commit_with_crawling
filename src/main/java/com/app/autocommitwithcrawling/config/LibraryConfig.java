package com.app.autocommitwithcrawling.config;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfig {
    @Bean
    public FlexmarkHtmlConverter flexmarkHtmlConverter(){
        return FlexmarkHtmlConverter.builder().build();
    }

    @Bean
    public UsernamePasswordCredentialsProvider
    usernamePasswordCredentialsProvider(
            @Value("${git.userName}")String userName,
            @Value("${git.accessToken}")String accessToken){
        return new UsernamePasswordCredentialsProvider(userName,accessToken);
    }


}
