package src;

import org.w3c.dom.NodeList;

public class RedDePetri {
    private int[][] preIncidencia;// Define cuantos tokens se necesitan en cada plaza para disparar una transicion
    private int[][] postIncidencia;//Define cuantos tokens se aniaden a cada plaza despues de disparar una transicion
    private int[][] incidencia;//Es la dif entre las anteriores post - pre
    private int[] plazas; // Estado actual de las plazas

    public RedDePetri(int[][] preIncidencia, int[][] postIncidencia, int[][] incidencia, int[] plazasIniciales) {
        this.preIncidencia = preIncidencia;
        this.postIncidencia = postIncidencia;
        this.incidencia = incidencia;
        this.plazas = plazasIniciales;
    }

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printPlazas() {
        System.out.print("Estado de las plazas: ");
        for (int i = 0; i < plazas.length; i++) {
            System.out.print(plazas[i] + " ");
        }
        System.out.println();
    }

    // Verificar si una transición está habilitada
    public boolean puedeDispararse(int transicion) {
        for (int i = 0; i < plazas.length; i++) {
            if (plazas[i] < preIncidencia[i][transicion]) {
                return false; // La transición no puede dispararse
            }
        }
        return true;
    }

    // Disparar una transición
    public void dispararTransicion(int transicion) {
        if (puedeDispararse(transicion)) {
            for (int i = 0; i < plazas.length; i++) {
                plazas[i] = plazas[i] - preIncidencia[i][transicion] + postIncidencia[i][transicion];
            }
            System.out.println("Transición " + transicion + " disparada.");
        } else {
            System.out.println("Transición " + transicion + " no puede dispararse.");
        }
    }

    public void simular(int pasos) {
        for (int paso = 0; paso < pasos; paso++) {
            System.out.println("Paso " + (paso + 1));
            boolean transitionFired = false;

            for (int t = 0; t < incidencia[0].length; t++) {
                if (puedeDispararse(t)) {
                    dispararTransicion(t);
                    transitionFired = true;
                    System.out.println("Transición T" + t + " disparada.");
                    imprimirMarcadoActual();
                    break;
                }
            }

            if (!transitionFired) {
                System.out.println("No hay transiciones disparables.");
                break;
            }
        }
    }

    private void imprimirMarcadoActual() {
        System.out.println("Marcado actual:");
        for (int i = 0; i < plazas.length; i++) {
            System.out.println("Plaza ID: P" + i + ", Marcado: " + plazas[i]);
        }
    }

}
