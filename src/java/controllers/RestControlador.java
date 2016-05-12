package controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import model.Juego;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RestControlador
{
    @RequestMapping("/saludar")
    public String saludar()
    {
        return "Hola, como estas??";
    }
    @RequestMapping(value = "/juegos", method = RequestMethod.GET)
    public java.util.List<model.Juego> findJuegos()
    {
        java.util.List<model.Juego> arr = new java.util.ArrayList<model.Juego>();
        
        for (int vueltas = 0 ; vueltas < 4 ; vueltas++)
        {
        
            System.out.println("vuelta: " + vueltas + " | " + (vueltas < 4));
            Juego aux = new Juego();
            if(vueltas == 0)
            {
                aux.setNombre("SALTA");
            }
            else if(vueltas == 1)
            {
                aux.setNombre("JUJUY");
            }
            else if(vueltas == 2)
            {
                aux.setNombre("TUCUMAN");
            }
            else if(vueltas == 3)
            {
                aux.setNombre("NACIONAL");
            }
            for (int j = 0 ; j < 20 ; j++)
            {
                int numero = (int) ( Math.random() * 9999) + 1;
                String strNumero = ""+numero;
                while(strNumero.length() <= 3)
                {
                    strNumero ="0" + strNumero;
                }
                aux.addNumero(strNumero);
            }
            arr.add(aux);
            System.out.println("aux:" + aux);
            
        }
        
      
        return arr;
    }
    @RequestMapping(value = "/changuita", method = RequestMethod.GET)
    public java.util.List<String> findChanguita()
    {
        java.util.List<String> arr = new java.util.ArrayList<String>();
        
        for (int j = 0 ; j < 20 ; j++)
        {
            int numero = (int) ( Math.random() * 9999) + 1;
            String strNumero = ""+numero;
            while(strNumero.length() <= 3)
            {
                strNumero ="0" + strNumero;
            }
            arr.add(strNumero);
        }

        return arr;
    }
    
    @RequestMapping(value = "/fecha", method = RequestMethod.GET)
    public String fechaActual()
    {
        String strFechaSalida = null;
        String strSorteo = null;
        String strNombreDia = null;
        String strNumeroDia = null;
        String strNombreMes = null;
        String strAnio = null;
        
        Date fechaActual  = new Date();
        //SORTEO:
        if(fechaActual.getHours() < 12)
        {
            strSorteo = "Matutina";
        }else if(fechaActual.getHours() > 12 && fechaActual.getHours() < 18)
        {
            strSorteo = "Vespertina";
        }
        else
        {
            strSorteo = "Nocturna";
        }
        
        //NOMBRE DIA:
        switch(fechaActual.getDay())
        {
            case 0:strNombreDia = "Domingo"; break;
            case 1:strNombreDia = "Lunes"; break;
            case 2:strNombreDia = "Martes"; break;
            case 3:strNombreDia = "Miercoles"; break;
            case 4:strNombreDia = "Jueves"; break;
            case 5:strNombreDia = "Viernes"; break;
            case 6:strNombreDia = "Sabado"; break;
        }
        strNumeroDia = "" + fechaActual.getDate() ;
        
        //NOMBRE MES:
        switch(fechaActual.getMonth())
        {
            case 0:strNombreMes = "Enero"; break;
            case 1:strNombreMes = "Febrero"; break;
            case 2:strNombreMes = "Marzo"; break;
            case 3:strNombreMes = "Abril"; break;
            case 4:strNombreMes = "Mayo"; break;
            case 5:strNombreMes = "Junio"; break;
            case 6:strNombreMes = "Julio"; break;
            case 7:strNombreMes = "Agosto"; break;
            case 8:strNombreMes = "Setiembre"; break;
            case 9:strNombreMes = "Octubre"; break;
            case 10:strNombreMes = "Noviembre"; break;
            case 11:strNombreMes = "Diciembre"; break;
        }
        strAnio = "" + (fechaActual.getYear() + 1900);
        
        strFechaSalida = "Sorteo " + strSorteo + " " +strNombreDia + " del " + strNumeroDia + " " + strNombreMes + " " + strAnio;
        
        return strFechaSalida;
    }
    /*
    //<editor-fold desc="NEGOCIOS:">
    @RequestMapping(value = "negocios" , method = RequestMethod.GET)
    public java.util.List<model.Negocio> findNegocios()
    {
        return daos.NegociosDAO.findAll();
    }
    //</editor-fold>
    
    
    @RequestMapping(method = RequestMethod.POST ,value="/web/upload")
    public boolean provideUploadInfo(@RequestParam("nombre") String name,@RequestParam("file") MultipartFile file) 
    {
        boolean copio = false;
        File rootFolder = new File("C:\\temp");
        if (!file.isEmpty()) 
        {
            System.out.println("RECIBI:" + name);
            try 
            {
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(rootFolder + File.separator + name)));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();
                copio = true;
            }
            catch (Exception e) 
            {
                copio = false;
                e.printStackTrace();
                 System.out.println("salio por exception: " +e.toString() );
            }
        }
        else 
        {
                System.out.println("salio por el else");
                copio = false;
        }

	return copio;
        //System.out.println("pwd: " + System.getProperty("user.dir"));
           /* File rootFolder = new File("C:\\temp");
            
            System.out.println("rootFolder existe: " + rootFolder.exists());
            
            List<String> fileNames = Arrays.stream(rootFolder.listFiles()).map(f -> f.getName()).collect(Collectors.toList());
            model.addAttribute("files",Arrays.stream(rootFolder.listFiles()).sorted(Comparator.comparingLong(f -> -1 * f.lastModified())).map(f -> f.getName()).collect(Collectors.toList()));
            return "uploadForm";
    }
    */
}