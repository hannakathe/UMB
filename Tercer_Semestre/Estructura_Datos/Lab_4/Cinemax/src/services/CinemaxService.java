package services;

import models.CinemaxModel;

import java.util.*;

// Clase que proporciona servicios para gestionar estadísticas de visitas a salas de cine
public class CinemaxService {
    // Lista para almacenar los registros de visitas
    private final List<CinemaxModel> registros = new ArrayList<>();

    // Método para registrar visitas a una sala de cine
    public void registrarVisitas(int semana, int sala, String pelicula, int espectadores) {
        registros.add(new CinemaxModel(semana, sala, pelicula, espectadores));
    }

    // Método para calcular estadísticas de visitas para una semana específica
    public String calcularEstadisticas(int semana) {
        // Mapas para almacenar estadísticas por película
        Map<String, Integer> totalPorPelicula = new HashMap<>(); // Total de espectadores por película
        Map<String, Integer> salaMasVista = new HashMap<>();     // Máximo de espectadores por película
        Map<String, Integer> salaMenosVista = new HashMap<>();  // Mínimo de espectadores por película
        Map<String, Integer> salaPeliMax = new HashMap<>();     // Sala con más visitas por película
        Map<String, Integer> salaPeliMin = new HashMap<>();     // Sala con menos visitas por película

        // Recorrer todos los registros para calcular estadísticas
        for (CinemaxModel registro : registros) {
            if (registro.getSemana() == semana) { // Filtrar por semana
                String pelicula = registro.getPelicula();
                int espectadores = registro.getEspectadores();
                int sala = registro.getSala();

                // Actualizar el total de espectadores por película
                totalPorPelicula.put(pelicula, totalPorPelicula.getOrDefault(pelicula, 0) + espectadores);

                // Determinar la sala con más visitas para cada película
                if (!salaMasVista.containsKey(pelicula) || espectadores > salaMasVista.get(pelicula)) {
                    salaMasVista.put(pelicula, espectadores);
                    salaPeliMax.put(pelicula, sala);
                }

                // Determinar la sala con menos visitas para cada película
                if (!salaMenosVista.containsKey(pelicula) || espectadores < salaMenosVista.get(pelicula)) {
                    salaMenosVista.put(pelicula, espectadores);
                    salaPeliMin.put(pelicula, sala);
                }
            }
        }

        // Determinar la película más y menos vista de la semana
        String peliculaMasVista = Collections.max(totalPorPelicula.entrySet(), Map.Entry.comparingByValue()).getKey();
        String peliculaMenosVista = Collections.min(totalPorPelicula.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Construir el informe de estadísticas
        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("📊 Estadísticas Semana ").append(semana).append("\n\n");
        for (String pelicula : totalPorPelicula.keySet()) {
            estadisticas.append("🎬 ").append(pelicula).append("\n");
            estadisticas.append("👥 Total Espectadores: ").append(totalPorPelicula.get(pelicula)).append("\n");
            estadisticas.append("🏆 Sala con más visitas: Sala ").append(salaPeliMax.get(pelicula)).append(" con ").append(salaMasVista.get(pelicula)).append(" espectadores\n");
            estadisticas.append("📉 Sala con menos visitas: Sala ").append(salaPeliMin.get(pelicula)).append(" con ").append(salaMenosVista.get(pelicula)).append(" espectadores\n");
            estadisticas.append("────────────────────────────────────────────────────────────────────────────\n");
        }

        // Añadir las películas más y menos vistas al informe
        estadisticas.append("🔥 Película más vista de la semana: ").append(peliculaMasVista).append("\n");
        estadisticas.append("❄️ Película menos vista de la semana: ").append(peliculaMenosVista).append("\n");

        return estadisticas.toString();
    }

    // Método para borrar todas las estadísticas registradas
    public void borrarEstadisticas() {
        registros.clear();
    }
}