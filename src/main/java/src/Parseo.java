package src;

import org.w3c.dom.Document;// permite manipular el contenido de un documento XML
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    private int marcadoInicial[][];// Para almacenar el marcado acutal de la RdP
    private int postIncidencia[][];// Plazas que terminan en transiciones(Representa la plaza) I+
    private int preIncidencia[][];// Trancisiones que terminan en plazas(Representa la plaza) I-
    private int incidencia[][];// Matriz de incidencia. I = (I+) - (I-)


    public Parseo(String path) {
        leerArchivo(path);

        setMarcadoInicial();
        //imprimirDetallePlazas();
        //imprimirDetalleTransiciones();
        //imprimirDetalleArcos();
        //imprimirMarcadoInicial();

        setMatrices();
        calcularIncidencia();
        printMatrix(incidencia);
    }

    public void leerArchivo(String path) {//metodo para leer y parsear el archivo XML
        try {
            File inputFile = new File(path);// Crea un objeto File con la ruta del archivo XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();// Crea una instancia de DocumentBuilderFactory
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();// Crea un DocumentBuilder a partir de la fábrica
            doc = dBuilder.parse(inputFile);// Parsea el archivo XML
            doc.getDocumentElement().normalize();// Normaliza el documento XML
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void setMarcadoInicial() {
        marcadoInicial = new int[getTamañoPlaza()][1]; // Inicializa la matriz para el marcado inicial

        // Itera sobre todos los nodos de la lista de plazas
        for (int i = 0; i < listaDePlazas.getLength(); i++) {
            Node nodo = listaDePlazas.item(i); // Obtiene el nodo actual de la lista de plazas

            // Verifica si el nodo es de tipo ELEMENT_NODE
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo; // Convierte el nodo a un elemento XML

                // Obtiene el atributo 'id' del elemento y extrae los dígitos para convertirlo en un entero
                int plaza = Integer.parseInt(element.getAttribute("id").replaceAll("\\D+", ""));

                // Obtiene el valor del elemento 'initialMarking', extrae los dígitos y los convierte en un entero
                int value = Integer.parseInt(
                        element.getElementsByTagName("initialMarking")
                                .item(0)
                                .getTextContent()
                                .replaceAll("\\D+", "")
                );

                // Asigna el valor obtenido a la matriz 'marcadoDeRed' en la posición correspondiente a 'plaza'
                marcadoInicial[plaza][0] = value;
            }
        }
    }

    public void imprimirMarcadoInicial() {
        System.out.println("Marcado inicial:");
        for (int i = 0; i < marcadoInicial.length; i++) {
            System.out.println("Plaza ID: P" + i + ", Marcado: " + marcadoInicial[i][0]);
        }
    }
    private void setMatrices() {
        // SETEAR TAMAÑO DE LAS MATRICES DE INCIDENCIA
        postIncidencia = new int[getTamañoPlaza()][getTamañoTransiciones()];
        preIncidencia = new int[getTamañoPlaza()][getTamañoTransiciones()];
        incidencia = new int[getTamañoPlaza()][getTamañoTransiciones()];

        // SETEAR VALORES DE MATRICES DE INCIDENCIA
        for (int i = 0; i < getTamañoArcos(); i++) {
            Node nodo = listaDeArcos.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodo;
                Element nodoHijo = getDirectChild(element, "type");
                String tipoOrigen = element.getAttribute("target").substring(0, 1);
                int origen = Integer.parseInt(element.getAttribute("target").replaceAll("\\D+", ""));
                int destino = Integer.parseInt(element.getAttribute("source").replaceAll("\\D+", ""));

                // Solo considero arcos que no sean inhibidores
                if (!nodoHijo.getAttribute("value").equals("inhibitor")) {
                    if (tipoOrigen.equals("P")) {
                        postIncidencia[origen][destino] = 1;
                    }
                    if (tipoOrigen.equals("T")) {
                        preIncidencia[destino][origen] = 1;
                    }
                }
            }
        }
    }

    public static Element getDirectChild(Element parent, String name) {
        for(Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if(child instanceof Element && name.equals(child.getNodeName())) return (Element) child;
        }
        return null;
    }
    private void calcularIncidencia() {
        for (int i = 0; i < getTamañoPlaza(); i++)
        {
            for (int j = 0; j < getTamañoTransiciones(); j++)
            {incidencia[i][j] = postIncidencia[i][j] - preIncidencia[i][j];}
        }
    }



    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getTamañoPlaza(){return listaDePlazas.getLength();}
    public int getTamañoTransiciones(){return listaDeTransiciones.getLength();}
    public int getTamañoArcos(){return listaDeArcos.getLength();}

    public NodeList getListaDePlazas(){return listaDePlazas;}
    public NodeList getListaDeTransiciones(){return listaDeTransiciones;}
    public NodeList getListaDeArcos(){return listaDeArcos;}
    public int[][] getMarcadoInicial(){return marcadoInicial;}
    public int[][] getPreIncidencia(){return preIncidencia;}
    public int[][] getPostIncidencia(){return postIncidencia;}
    public int[][] getIncidencia(){return incidencia;}
    public int[] getPlazasIniciales() {
        int[] plazasIniciales = new int[marcadoInicial.length];
        for (int i = 0; i < marcadoInicial.length; i++) {
            plazasIniciales[i] = marcadoInicial[i][0];
        }
        return plazasIniciales;
    }

}
