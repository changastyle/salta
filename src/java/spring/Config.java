package spring;

import javax.servlet.MultipartConfigElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "/controllers")
public class Config extends WebMvcConfigurerAdapter 
{
    
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver resolver() 
    {
        return new StandardServletMultipartResolver();
    }
    @Bean
    public ViewResolver viewResolver() 
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        //viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
 
        return viewResolver;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/web/res/**").addResourceLocations("/web/res/*");
    }
}