package src;

import org.w3c.dom.Document;// permite manipular el contenido de un documento XML
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;// permite manejar una lista de nodos XML

import javax.xml.parsers.DocumentBuilder;// para construir documentos XML
import javax.xml.parsers.DocumentBuilderFactory;// para crear nuevas instancias de DocumentBuilder
import java.io.File;// para manejar archivos en el sistema de archivos
import java.util.ArrayList;
import java.util.Collections;

public class Parseo {
    private Document doc;// Documento XML que será parseado
    private NodeList listaDePlazas;// Lista de nodos correspondientes a las plazas en el XML
    private NodeList listaDeTransiciones;// Lista de nodos correspondientes a las transiciones en el XML
    private NodeList listaDeArcos;// Lista de nodos correspondientes a los arcos en el XML

    public Parseo(String path) {
        leerArchivo(path);
        imprimirDetallePlazas();
        imprimirDetalleTransiciones();
        imprimirDetalleArcos();
    }

    public void leerArchivo(String path) {//metodo para leer y parsear el archivo XML
        try {
            File inputFile = new File(path);// Crea un objeto File con la ruta del archivo XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();// Crea una instancia de DocumentBuilderFactory
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();// Crea un DocumentBuilder a partir de la fábrica
            doc = dBuilder.parse(inputFile);// Parsea el archivo XML
            doc.getDocumentElement().normalize();// Normaliza el documento XML
        } catch (Exception e) {e.printStackTrace();}

        listaDePlazas = doc.getElementsByTagName("place");
        listaDeTransiciones = doc.getElementsByTagName("transition");
        listaDeArcos = doc.getElementsByTagName("arc");
        System.out.println("Cantidad de plazas: " + listaDePlazas.getLength());
        System.out.println("Cantidad de Transiciones " + listaDeTransiciones.getLength());
        System.out.println("Cantidad de Arcos " + listaDeArcos.getLength());
    }
    public void imprimirDetallePlazas() {// Método para obtener la información detallada de las plazas
        ArrayList<String> plazas = new ArrayList<>();
        for (int i = 0; i < listaDePlazas.getLength(); i++) {
            Element plaza = (Element) listaDePlazas.item(i);
            String id = plaza.getAttribute("id").trim();
            String nombre = plaza.getElementsByTagName("name").item(0).getTextContent().trim();
            plazas.add("Plaza ID: " + id + ", Nombre: " + nombre);
        }
        Collections.sort(plazas);
        for (String plaza : plazas) {
            System.out.println(plaza);
        }
    }
    public void imprimirDetalleTransiciones() {// Método para obtener la información detallada de las transiciones
        ArrayList<String> transiciones = new ArrayList<>();
        for (int i = 0; i < listaDeTransiciones.getLength(); i++) {
            Element transicion = (Element) listaDeTransiciones.item(i);
            String id = transicion.getAttribute("id").trim();
            String nombre = transicion.getElementsByTagName("name").item(0).getTextContent().trim();
            transiciones.add("Transición ID: " + id + ", Nombre: " + nombre);
        }
        Collections.sort(transiciones);
        for (String transicion : transiciones) {
            System.out.println(transicion);
        }
    }
    public void imprimirDetalleArcos() {// Método para obtener la información detallada de los arcos
        for (int i = 0; i < listaDeArcos.getLength(); i++) {
            Element arco = (Element) listaDeArcos.item(i);
            String source = arco.getAttribute("source").trim();
            String target = arco.getAttribute("target").trim();
            System.out.println("Arco de " + source + " a " + target);
        }
    }
}
