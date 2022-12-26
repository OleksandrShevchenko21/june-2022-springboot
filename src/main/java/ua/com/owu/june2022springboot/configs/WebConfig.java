package ua.com.owu.june2022springboot.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@EnableWebMvc // чтоб активировались некоторые дфолтные параметры
@Configuration // позволяет создвать и описывать разные Bean
public class WebConfig implements WebMvcConfigurer {

    @Override
    /*если ты увидишь какую-то URL, которая, например начинается с токого -то сегмента
    * то иди в такую-то папку и найди такой-то файл и оттдай его*/
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String folder = System.getProperty("user.home")+ File.separator + "images" + File.separator;
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + folder);
    }
}
