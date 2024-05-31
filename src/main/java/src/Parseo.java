package src;

import org.w3c.dom.Document;// permite manipular el contenido de un documento XML
import org.w3c.dom.NodeList;// permite manejar una lista de nodos XML

import javax.xml.parsers.DocumentBuilder;// para construir documentos XML
import javax.xml.parsers.DocumentBuilderFactory;// para crear nuevas instancias de DocumentBuilder
import java.io.File;// para manejar archivos en el sistema de archivos

public class Parseo {
    private Document doc;// Documento XML que será parseado
    private NodeList listaDePlazas;// Lista de nodos correspondientes a las plazas en el XML
    private NodeList listaDeTransiciones;// Lista de nodos correspondientes a las transiciones en el XML
    private NodeList listaDeArcos;// Lista de nodos correspondientes a los arcos en el XML

    public Parseo(String path) {
        leerArchivo(path);
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
        System.out.println(listaDePlazas.getLength());
        System.out.println(listaDeTransiciones.getLength());
        System.out.println(listaDeArcos.getLength());
    }
}
