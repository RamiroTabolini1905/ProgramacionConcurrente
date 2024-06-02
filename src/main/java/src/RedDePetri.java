package src;

public class RedDePetri {
    private	int marcadoDeRed[][];
    private int[][] preIncidencia;
    private int[][] postIncidencia;
    private int[][] incidencia;
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

}
