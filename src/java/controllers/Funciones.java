package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.channels.ReadableByteChannel;
import model.Instalaciones;
import java.util.ArrayList;
import model.Sorteo;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.rmi.server.RMIClassLoader;
import model.JugadaDiaria;

public class Funciones 
{
    private static ArrayList<Sorteo> arrSorteosFromWS;
    private static final String URLExtractosWebService = "http://salapp2:8080/saaServices/services/smarttv/extractos/";
    private static final String URLWebService = "http://salapp2:8080/saaServices/services/smarttv/sorteos";
    private final static String carpetaDeLosSorteos = "/var/sorteos/";
    private final static String carpetaZips = "/var/descomprime/" ;
    
    public static String leerArchivo(String ruta)
    {
        String salida = "";
        File f = new File(ruta);
    
        try
        {
            
            if(f != null)
            {
                //1.Abro un lector de archivos del Stream del WS:
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                String linea = "";
                while((linea = in.readLine()) != null)
                {
                    salida += linea;
                }
            }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
        if(!salida.trim().equalsIgnoreCase(""))
        {
            return salida;
        }
        else
        {
            return null;
        }
    }
    public static JugadaDiaria parseoDelWS(String json)
    {
        JugadaDiaria jugada;
        
       // String json = "{\"jugadaDiaria\":{\"juego\":\"tombola\"},\"c_juego\":0,\"t_sorte\":\"E\",\"f_sorte\":\"27/05/2016\",\"n_sorte\":2885,\"d_sorte\":\"Tucuman de la Tarde\",\"d_loter\":\"Tucuman\",\"extractos\":[{\"posic\":\"1\",\"premio\":\"6958\"},{\"posic\":\"2\",\"premio\":\"9628\"},{\"posic\":\"3\",\"premio\":\"4271\"},{\"posic\":\"4\",\"premio\":\"5025\"},{\"posic\":\"5\",\"premio\":\"3119\"},{\"posic\":\"6\",\"premio\":\"2265\"},{\"posic\":\"7\",\"premio\":\"2452\"},{\"posic\":\"8\",\"premio\":\"9960\"},{\"posic\":\"9\",\"premio\":\"9569\"},{\"posic\":\"10\",\"premio\":\"6240\"},{\"posic\":\"11\",\"premio\":\"5455\"},{\"posic\":\"12\",\"premio\":\"1221\"},{\"posic\":\"13\",\"premio\":\"1149\"},{\"posic\":\"14\",\"premio\":\"7592\"},{\"posic\":\"15\",\"premio\":\"9544\"},{\"posic\":\"16\",\"premio\":\"8918\"},{\"posic\":\"17\",\"premio\":\"4668\"},{\"posic\":\"18\",\"premio\":\"5934\"},{\"posic\":\"19\",\"premio\":\"8868\"},{\"posic\":\"20\",\"premio\":\"5564\"}],\"horaSorteo\":\"19:30\"}";
        
        jugada = new Gson().fromJson(json, JugadaDiaria.class);
        
        return jugada;
    }
    public static String hacer()
    {
        String salida = "";
        if(arrSorteosFromWS == null)
        {
            //TRAIGO LA LISTA DE SORTEOS DEL WS:
            String rawWS = dameWSComoString(URLWebService);
            
            
            // PARA CADA UNO ME TRAIGO SU ZIP:
            for(Sorteo sorteo : parsearArrSorteosAJSON(rawWS))
            {
                //salida += sorteo.toString() + "<br>";
                
                String URL = URLExtractosWebService + sorteo.getcSorte();
                String rutaLocal = carpetaZips + sorteo.getcSorte()  + ".zip";
                
                //SI NO EXISTE LA CARPETA DE DESCOMPRESION ENTONCES LA CREO:
                File carpeta = new File(carpetaZips);
                if(!carpeta.exists())
                {
                    carpeta.mkdir();
                }

                
                //ME BAJO EL ZIP Y LO DESCOMPRIMO:
                System.out.println("URL TRAIGO: " + URL);
                System.out.println("ruta GUARDO: " + rutaLocal);
                downloadZipFile(URL, rutaLocal);
                
                if(unzip(String.valueOf(sorteo.getcSorte())))
                {
                   //salida += "descomprimi bien" + "<br>";
                }
                else
                {
                    //salida+= "no pude descomprimir" +  "<br>";
                }
                
            }
        }
        return salida;
    }
    public static boolean unzip(String rutaArchivo) 
    {
        boolean ok = false;
        byte[] buffer = new byte[2048];
        String zipFile = "";

        rutaArchivo = carpetaZips + rutaArchivo + ".zip";
        try 
        {
            FileInputStream fInput = new FileInputStream(rutaArchivo);

            ZipInputStream zipInput = new ZipInputStream(fInput);

            ZipEntry entry = zipInput.getNextEntry();

            while (entry != null) 
            {
                File file = new File(carpetaDeLosSorteos + entry.getName() );
                
                if (!file.exists())
                {
                    file.createNewFile();
                }
                FileOutputStream fOutput = new FileOutputStream(file);
                int count = 0;

                while ((count = zipInput.read(buffer)) > 0) {
                    fOutput.write(buffer, 0, count);
                }

                fOutput.close();
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
            
            File zipOriginal = new File(rutaArchivo);
            zipOriginal.delete();
            ok = true;
            
            System.out.println("Descomprimí y eliminé el ZIP: "  + rutaArchivo + " correctamente!.");
        } 
        catch (Exception e) 
        {
            String postData = "Error al descomprimir ZIP:" + rutaArchivo + " -> " + e.toString() + "\n. Posiblemente no exista la carpeta de descompresion:" + carpetaZips ;
            System.out.println(postData);
            //enviarMailErrorWS(postData);
            
            e.printStackTrace();
        }
        return ok;
    }
    public static java.util.List<Sorteo> parsearArrSorteosAJSON(String rawWS)
    {
        java.util.List<Sorteo> lista = new java.util.ArrayList<Sorteo>();
        
        Type listTipoSorteo = new TypeToken<ArrayList<Sorteo>>(){}.getType();
        lista = new Gson().fromJson(rawWS, listTipoSorteo);

        
        return lista;
    }
    public static String dameWSComoString(String urlWS)
    {
        String salida = "";
        URL url;
    
        try
        {
            url = new URL(urlWS);
            
            if(url != null)
            {
                //1.Abro un lector de archivos del Stream del WS:
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String linea = "";
                while((linea = in.readLine()) != null)
                {
                    salida += linea;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return salida;
    }
    public static void downloadZipFile(String rutaTraer, String rutaGuardar)
    {
        try 
        {
            URL url = new URL(rutaTraer);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(rutaGuardar);
            byte[] b = new byte[1024];
            int count;
            while ((count = in.read(b)) >= 0) 
            {
                out.write(b, 0, count);
            }
            out.flush(); out.close(); in.close();                   
 
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
    /*private static boolean traerZIP(Sorteo sorteoWS) {
        boolean ok = false;
        
        URL urlJuego;
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        
        try
        {
            urlJuego = new URL(URLExtractosWebService + sorteoWS.getcSorte());
            System.out.println("URL = " + urlJuego);
            
            rbc = Channels.newChannel(urlJuego.openStream());
            fos = new FileOutputStream(carpetaDeLosSorteos + "/" + sorteoWS.getcSorte() + ".zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            ok = true;
        }
        catch(MalformedURLException e)
        {
            String postData = "ERROR: No se pudo leer la URL DEL WS: " + URLExtractosWebService + sorteoWS.getcSorte() + " -> " + e.toString();
            System.out.println(postData);
           // enviarMailErrorWS(postData);
        }
        catch(FileNotFoundException e)
        {
            String postData = "ERROR: No se encontró Archivo ZIP: " + URLExtractosWebService + sorteoWS.getcSorte() + " -> " + e.toString();
            System.out.println( postData );
            //enviarMailErrorWS(postData);
        }
        catch(IOException e)
        {
            String postData ="ERROR: Al leer zip: " + URLExtractosWebService + sorteoWS.getcSorte() + " | error canales IO -> " + e.toString();
            System.out.println(postData);
            //enviarMailErrorWS(postData);
        }
        return ok;
    }
    *//*
    public static ArrayList<Sorteo> sorteosFromWebService(String URLWebService)
    {
        ArrayList<Sorteo> arrDeSorteos = new ArrayList<Sorteo>();
        URL url = null;  
        try
        {
            url = new URL(URLWebService);
            if(url != null)
            {
                //1.Abro un lector de archivos del Stream del WS:
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                //2.Leo el archivo HTML:
                String str = in.readLine();
                
                //3.Parseo el contenido del Archivo en formato JSON a objetos Java Representativos(model.Sorteo):
                JSONArray array = (JSONArray) JSONValue.parse(str);
            }
        } 
        catch (MalformedURLException ex)
        {
            String postData = "ERROR: Leyendo la URL del webservice:" + URLWebService ;
            
            System.out.println(postData);
            
            ex.printStackTrace();
        } 
        catch (IOException ex)
        {
            String postData = "ERROR: Abriendo URL del web-service (Canales I/O): " + url.toString() ;
            System.out.println(postData);
            
            ex.printStackTrace();
        }

        return arrDeSorteos;
    }
}
*/
