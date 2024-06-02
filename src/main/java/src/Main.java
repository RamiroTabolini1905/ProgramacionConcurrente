package src;

public class Main {
    public static void main(String[] args) {
        String pathRedDePetri = "./RedesDePetri/Petri net 1-sinTiempo.xml";

        Parseo parseo = new Parseo(pathRedDePetri);
        RedDePetri red = new RedDePetri(parseo.getPreIncidencia(), parseo.getPostIncidencia(), parseo.getIncidencia(), parseo.getPlazasIniciales());

        red.printPlazas();
        red.dispararTransicion(0);
        red.printPlazas();
        red.dispararTransicion(1);
        red.printPlazas();
        red.dispararTransicion(10);

        red.printPlazas();
    }
}