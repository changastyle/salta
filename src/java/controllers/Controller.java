/*package controller;

import Threads.ComandoActualizar;
import daos.UltimoSorteoDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.*;

public class Controller 
{

    private static ArrayList<ComandoActualizar> arrDeThreadQueNoPudieronSincronizar;
    private static ArrayList<Instalaciones> arrDeInstalaciones;
    private static ArrayList<Listaip> arrDeListaips;
    private static ArrayList<Ultimosorteo> arrDeUltimosSorteos;
    private static ArrayList<Sorteo> arrSorteosFromWS;
    private static boolean flagActualizarClientes;
    
    //private final static String nombreCarpetaConfig = Controller.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private final static String nombreCarpetaConfig = "/root/updateSorteos/";
    private final static String nombreArchivoConfig = "parametrosDeConfiguracion.xml";
    
    //<editor-fold desc="Parametros">
    private static String senderEmail;
    private static String[] arrDestinatarios;
    private static String URLWebService ;
    private static String URLExtractosWebService;
    private static int tiempoDeLoop ;
    private static int tiempoRecargarWScasoError;
    private static int tiempoEsperaSincro ;        //AL CAMBIAR ESTE VALOR, TENER EN CUENTA QUE EN LA CLASE ComandoActualizar EL ATRIBUTO tiempoEsperaParaSaberSiSincronizo = 20 * 1000; DEBE SER UN VALOR MENOR QUE tiempoEsperaSincro!.
    private static String carpetaDeLosSorteos ;
    private static String carpetaDescompresion ;
    
    public static void recargarParametros()
    {
        System.out.println( "|--------------------- Recargo los parametros de :" + nombreArchivoConfig  + "---------------------|");
        URLWebService = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"URLWebService");System.out.println("URLWebService:" + URLWebService);
        URLExtractosWebService = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"URLExtractosWebService");System.out.println("URLExtractosWebService:" + URLExtractosWebService);
        tiempoDeLoop = XML.XMLAPI.getValorFromIntegerKey(nombreCarpetaConfig, nombreArchivoConfig, "tiempoDeLoop");System.out.println("tiempoDeLoop:" + tiempoDeLoop);
        tiempoRecargarWScasoError = XML.XMLAPI.getValorFromIntegerKey(nombreCarpetaConfig, nombreArchivoConfig, "tiempoRecargarWScasoError");System.out.println("tiempoRecargarWScasoError:" + tiempoRecargarWScasoError);
        tiempoEsperaSincro = XML.XMLAPI.getValorFromIntegerKey(nombreCarpetaConfig, nombreArchivoConfig, "tiempoEsperaSincro");System.out.println("tiempoEsperaSincro:" + tiempoEsperaSincro);
        carpetaDeLosSorteos = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"carpetaDeLosSorteos");System.out.println("carpetaDeLosSorteos:" + carpetaDeLosSorteos);
        carpetaDescompresion = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"carpetaDescompresion");System.out.println("carpetaDescompresion:" + carpetaDescompresion);
        arrDestinatarios = new String[2];
        arrDestinatarios[0] = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"receiberEmail1");System.out.println("arrDestinatarios[0]:" + arrDestinatarios[0]);
        arrDestinatarios[1] = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"receiberEmail2");System.out.println("arrDestinatarios[1]:" + arrDestinatarios[1]);
        senderEmail = XML.XMLAPI.getValorFromKey(nombreCarpetaConfig, nombreArchivoConfig,"senderEmail");System.out.println("senderEmail:" + senderEmail);
    }
    //</editor-fold>
    
    
    //<editor-fold desc="Metodos Importantes (Logica)">
   
    public static void hacer() 
    {
        flagActualizarClientes = false;
        int vueltas = 0;
        
        recargarParametros();
        while(true)
        {
            recargarParametros();
            boolean WSstatusOK = true;
            boolean deboEnviarEmail = false;
            arrDeThreadQueNoPudieronSincronizar = new ArrayList<ComandoActualizar>();
            try
            {
                vueltas++;

                //Traigo datos del WS:
                Controller.arrDeInstalaciones = imprimirInstalaciones();
                Controller.arrDeUltimosSorteos = imprimirSorteosFromDB();
                Controller.arrSorteosFromWS = imprimirSorteos();
                
                for(Sorteo sorteoWS : arrSorteosFromWS)
                {
                    if( !existeSorteoEnDB(sorteoWS) )
                    {
                        if(traerZIP(sorteoWS))
                        {
                            unzip(String.valueOf(sorteoWS.getcSorte()));
                            creoSorteo(sorteoWS);
                            flagActualizarClientes = true;
                        }
                    } 
                    else if (existeSorteoEnDB(sorteoWS) && sorteoEstaDesactualizado(sorteoWS)) {
                        if (traerZIP(sorteoWS)) 
                        {
                            unzip(String.valueOf(sorteoWS.getcSorte()));
                            flagActualizarClientes = true;
                            actualizoSorteo(sorteoWS);
                        }
                    }
                }
                
                //EN EL CASO DE QUE HALLA QUE ACTUALIZAR, ENTONCES PONGO TODA LA LISTA DE IP COMO DESACTUALIZADA:
                if(flagActualizarClientes)
                {
                    System.out.println("HAY CAMBIOS EN EL WEB-SERVICE?? -> SI");
                    System.out.println("LOS SIGUIENTES EQUIPOS SERAN ACTUALIZADOS AHORA:");
                    for (Listaip listaip: findAllListaIPS())
                    {
                        listaip.setEstado('E');
                        //daos.ListaipsDAO.update(listaip);
                    }
                } else {
                    System.out.println("HAY CAMBIOS EN EL WEB-SERVICE?? -> NO");
                    System.out.println("LOS SIGUIENTES EQUIPOS NO HABIAN SINCRONIZADO CORRECTAMENTE EN LA VUELTA ANTERIOR:");
                }

                
                // RICKY
                ArrayList<ComandoActualizar> arrDeThreadActulizadores = envioComandoActualizar();
                
                deboEnviarEmail = false;
                for(ComandoActualizar comandoActualizar : arrDeThreadActulizadores)
                {
                    comandoActualizar.join(tiempoEsperaSincro);
                    System.out.println(comandoActualizar.getListaip().getId().getIp() + " -> pudoActualizar " + comandoActualizar.pudoActualizar() );
                    if(!comandoActualizar.pudoActualizar())
                    {
                        arrDeThreadQueNoPudieronSincronizar.add(comandoActualizar);
                        deboEnviarEmail = true;
                    }
                }

                System.out.println("|---------------------------------- FIN Vuelta " + vueltas +". ----------------------------------|");
                Thread.sleep(tiempoDeLoop);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("ERROR: Se interrumpio el metodo Controller.hacer()");
                WSstatusOK = false;
                deboEnviarEmail = true;
            } 
            if(deboEnviarEmail)
            {
                System.out.println("ENTRE A DEBO ENVIAR EMAIL: !WSstatusOK ->" + !WSstatusOK);
                String mensaje = "";
                if(!WSstatusOK)
                {
                    String postData =  "ERROR: En logica de la Aplicacion , se interrumpió el hilo de ejecucion (Runtime Exception)!";
                    System.out.println(postData);
                    enviarMailErrorWS(postData);
                    
                }
                else
                {
                    System.out.println("ERROR: ACTUALIZANDO FLEXs");
                    mensaje += "Las siguientes direcciones IP no se sincronizaron:\n";

                    for(ComandoActualizar comandoActualizar : arrDeThreadQueNoPudieronSincronizar)
                    {
                        mensaje += comandoActualizar.getListaip().getId().getIp() + " : " + comandoActualizar.getListaip().getDescripcion() + "\n";
                    }
                }

//                sendEmail(senderEmail, "" , arrDestinatarios, "Status Smartview" , mensaje );
            }
        }
    }
    private static ArrayList<ComandoActualizar> envioComandoActualizar()
    {   
        ArrayList<ComandoActualizar> arrDeThreadActualizadores = new ArrayList<ComandoActualizar>();
        
/*        for (Listaip ip : controller.Controller.findAllListaIPS()) {
            // RICKY
            if (ip.isHabil() && ip.getEstado() != 'O') {
                ComandoActualizar comandoActualizar = new ComandoActualizar(ip);
                arrDeThreadActualizadores.add(comandoActualizar);
                comandoActualizar.start();
            }
        }
        return arrDeThreadActualizadores;
    }*/
    //</editor-fold>
    
    
    //<editor-fold desc="Metodos Utilitarios:">
   /*public static void enviarMailErrorWS(String postData)
    {
        String mensaje = "";
            mensaje += "|**********************************************************************|\n";
            mensaje += "|************************ ERROR DEL WEB-SERVICE ***********************|\n";
            mensaje += "|**********************************************************************|\n";
            mensaje += "    URL DEL WEB-SERVICE: " + URLWebService +"\n";
            mensaje += "    Pista del " + postData + "   \n\n";
            

//        sendEmail(senderEmail, "" , arrDestinatarios, "Status Smartview" , mensaje );
    }    
    public static ArrayList<Sorteo> sorteosFromWebService()
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
                
                //Muestro el contenido del archivo:
                /*int contador = 0;
                System.out.println(contador +" | " + str );
                
                //3.Parseo el contenido del Archivo en formato JSON a objetos Java Representativos(model.Sorteo):
                JSONArray array = (JSONArray) JSONValue.parse(str);
                arrDeSorteos = mapeoSorteosDB(array);
            }
        } 
        catch (MalformedURLException ex)
        {
            String postData = "ERROR: Leyendo la URL del webservice:" + URLWebService ;
            
            enviarMailErrorWS(postData);
            System.out.println(postData);
            
            ex.printStackTrace();
        } 
        catch (IOException ex)
        {
            String postData = "ERROR: Abriendo URL del web-service (Canales I/O): " + url.toString() ;
            enviarMailErrorWS(postData);
            System.out.println(postData);
            
            ex.printStackTrace();
        }

        return arrDeSorteos;
    }
    private static ArrayList<model.Sorteo> mapeoSorteosDB(JSONArray arrayRecibido)
    {
        ArrayList<model.Sorteo> arrDeSorteos = new ArrayList<model.Sorteo>();
        
        for(int i = 0 ; i < (arrayRecibido.size() ); i++)
        {
            JSONObject objetoJSONProvisorio = (JSONObject) arrayRecibido.get(i);
            model.Sorteo sorteoAux =  new Sorteo();
            sorteoAux.setFileName( (char) objetoJSONProvisorio.get("fileName").toString().charAt(0) );
            sorteoAux.setcSorte(Integer.parseInt(String.valueOf(objetoJSONProvisorio.get("c_sorte"))));
            sorteoAux.setcJuego(String.valueOf(objetoJSONProvisorio.get("c_juego")));
            sorteoAux.setfSorte(String.valueOf(objetoJSONProvisorio.get("f_sorte")));
            sorteoAux.setServj(String.valueOf(objetoJSONProvisorio.get("t_servj")));
            arrDeSorteos.add(sorteoAux);
            //System.out.println("sorteoAUX= "+ sorteoAux.toString());
            //System.out.println("JSON OBJECT#"+i + ": " + objetoJSONProvisorio.toString());
        }
        
        return arrDeSorteos;
    }
    public static String imprimirArrayDeSorteos(ArrayList<Sorteo>  arrAImprimir)
    {
        String salida = "";
        
        for(Sorteo sorteo : arrAImprimir)
        {
            salida += "    " + sorteo.toString() + "\n";
        }
        
        return salida;
    }/*
    public static ArrayList<model.Ultimosorteo> findAllUltimosSorteos()
    {
        return daos.UltimoSorteoDAO.findAllUltimosSorteos();
    }
    public static ArrayList<model.Instalaciones> findAllInstalaciones()
    {
        return daos.InstalacionesDAO.findAllInstalaciones();
    }
    private static ArrayList<Listaip> findAllListaIPS()
    {
        return daos.ListaipsDAO.findAllListaIps();
    }

    private static ArrayList<Instalaciones> imprimirInstalaciones() {
        ArrayList<Instalaciones> arr = controller.Controller.findAllInstalaciones();

        System.out.println("INSTALACIONES:");

        for (model.Instalaciones instalacion : arr) {
            System.out.println("    " + instalacion.toString());
        }

        return arr;
    }

    private static ArrayList<Sorteo> imprimirSorteos() {
        ArrayList<Sorteo> arr = controller.Controller.sorteosFromWebService();

        System.out.println("Sorteos FROM WS:");
        System.out.println(controller.Controller.imprimirArrayDeSorteos(arr));

        return arr;

    }

    public static ArrayList<Ultimosorteo> imprimirSorteosFromDB() {
        ArrayList<Ultimosorteo> arr = controller.Controller.findAllUltimosSorteos();
        System.out.println("ULTIMOS SORTEOS DB:");
        for (model.Ultimosorteo ultimoSorteo : arr) {
            System.out.println("    " + ultimoSorteo.toString());
        }
        return arr;
    }

    public static ArrayList<Listaip> imprimirListaIps() {
        ArrayList<Listaip> arr = controller.Controller.findAllListaIPS();

        //System.out.println("INSTALACIONES:");
        for (model.Listaip listaip : arr) {
            System.out.println("    " + listaip.toString());
        }
        return arr;
    }

    private static boolean existeSorteoEnDB(Sorteo sorteoWS) {
        boolean respuesta = false;

        for (Ultimosorteo sorteoDB : arrDeUltimosSorteos) {
            if (sorteoDB.getId().getCJuego() == Integer.parseInt(sorteoWS.getcJuego())) {
                if (sorteoDB.getId().getDName().charAt(0) == sorteoWS.getFileName()) {
                    respuesta = true;
                    break;
                }
            }
        }

        return respuesta;
    }
    private static void creoSorteo(Sorteo sorteoWS) {
        Ultimosorteo ultimosorteo = new Ultimosorteo();
        ultimosorteo.setId(new UltimosorteoId(1, Integer.parseInt(sorteoWS.getcJuego()), String.valueOf(sorteoWS.getFileName())));
        ultimosorteo.setCSorte(sorteoWS.getcSorte());

        Timestamp stamp = new Timestamp(Long.parseLong(sorteoWS.getfSorte()));
        Date date = new Date(stamp.getTime());
        ultimosorteo.setFSorte(date);

        System.out.println("CREO SORTEO: " + ultimosorteo.toString());
        UltimoSorteoDAO.saveUltimoSorteo(ultimosorteo);
    }
    private static void actualizoSorteo(Sorteo sorteoWS) {

        Ultimosorteo ultimosorteo = new Ultimosorteo();
        ultimosorteo.setId(new UltimosorteoId(1, Integer.parseInt(sorteoWS.getcJuego()), String.valueOf(sorteoWS.getFileName())));
        ultimosorteo.setCSorte(sorteoWS.getcSorte());

        Timestamp stamp = new Timestamp(Long.parseLong(sorteoWS.getfSorte()));
        Date date = new Date(stamp.getTime());
        ultimosorteo.setFSorte(date);

        System.out.println("ACTUALIZO SORTEO: " + ultimosorteo.toString());

        UltimoSorteoDAO.updateUltimoSorteo(ultimosorteo);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private static boolean sorteoEstaDesactualizado(Sorteo sorteoWS) {
        boolean respuesta = false;

        if (existeSorteoEnDB(sorteoWS)) {
            for (Ultimosorteo sorteoDB : arrDeUltimosSorteos) {
                if (sorteoDB.getId().getDName().charAt(0) == sorteoWS.getFileName()) {
                    if (sorteoDB.getId().getCJuego() == Integer.parseInt(sorteoWS.getcJuego())) {
                        if (sorteoDB.getCSorte() != sorteoWS.getcSorte()) {
                            respuesta = true;
                        }
                        // RICKY
                        break;
                    }
                }
            }
        }

        return respuesta;
    }
    private static boolean traerZIP(Sorteo sorteoWS) {
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
            enviarMailErrorWS(postData);
        }
        catch(FileNotFoundException e)
        {
            String postData = "ERROR: No se encontró Archivo ZIP: " + URLExtractosWebService + sorteoWS.getcSorte() + " -> " + e.toString();
            System.out.println( postData );
            enviarMailErrorWS(postData);
        }
        catch(IOException e)
        {
            String postData ="ERROR: Al leer zip: " + URLExtractosWebService + sorteoWS.getcSorte() + " | error canales IO -> " + e.toString();
            System.out.println(postData);
            enviarMailErrorWS(postData);
        }
        return ok;
    }
    public static boolean unzip(String rutaArchivo) 
    {
        boolean ok = false;
        byte[] buffer = new byte[2048];
        String zipFile = "";

        rutaArchivo = carpetaDeLosSorteos + "/" + rutaArchivo + ".zip";
        try 
        {
            FileInputStream fInput = new FileInputStream(rutaArchivo);

            ZipInputStream zipInput = new ZipInputStream(fInput);

            ZipEntry entry = zipInput.getNextEntry();

            while (entry != null) {
                File file = new File(carpetaDescompresion + "/" + entry.getName() );

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
            
            System.out.println("Descomprimí y eliminé el ZIP: "  + rutaArchivo + "correctamente!.");
        } 
        catch (Exception e) 
        {
            String postData = "Error al descomprimir ZIP:" + rutaArchivo + " -> " + e.toString() + "\n. Posiblemente no exista la carpeta de descompresion:" + carpetaDescompresion ;
            System.out.println(postData);
            enviarMailErrorWS(postData);
            
            e.printStackTrace();
        }
        return ok;
    }
    public static void sendEmail(String usuario , String password, String[] destinatarios, String asunto , String mensaje)
    {
        Properties props = new Properties();
        /*props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");*/
        
        /*props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "10.10.20.10");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "10.10.20.10");//
        
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", "10.10.20.10");
        props.put("mail.smtp.port", "25");
        //props.put("mail.smtp.ssl.trust", "10.10.20.10");
 
       /* Session session = Session.getInstance(props,new javax.mail.Authenticator() 
        {
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(usuario, password);
            }
        });//
        Session session = Session.getInstance(props);
 
        try 
        {
 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            for(int i = 0 ; i < destinatarios.length ; i++)
            {
                message.addRecipient(Message.RecipientType.TO,  new InternetAddress(destinatarios[i]) );
            }
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);
 
            Transport.send(message);
            System.out.println("Email enviado con exito!");
 
        } 
        catch (MessagingException e) 
        {
            String postData = "Error: al enviar email: " + destinatarios.toString() + " -> " + e.toString();
            System.out.println(postData);
        }
    }
    //</editor-fold>
}
*/