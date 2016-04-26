package spring;
/*
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
 
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
 
public class HelloWorldInitializer implements WebApplicationInitializer {
 
    public void onStartup(ServletContext container) throws ServletException {
 
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(Config.class);
        ctx.setServletContext(container);
 
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
 
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        
    }
 
}*/
import java.io.File;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
 
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer 
{
 
    @Override
    protected Class<?>[] getRootConfigClasses() 
    {
        return new Class[] { Config.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() 
    {
        return null;
    }
  
    @Override
    protected String[] getServletMappings() 
    {
        return new String[] { "/" };
    }
    protected void customizeRegistration(ServletRegistration.Dynamic registration)
    {
        registration.setMultipartConfig(getMultipartConfigElement());
    }
    private MultipartConfigElement getMultipartConfigElement() 
    {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }
 
    private static final String LOCATION = "C:/temp/"; // Temporary location where files will be stored
 
    private static final long MAX_FILE_SIZE = 5242880; // 5MB : Max file size.
                                                        // Beyond that size spring will throw exception.
    private static final long MAX_REQUEST_SIZE = 20971520; // 20MB : Total request size containing Multi part.
     
    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

}