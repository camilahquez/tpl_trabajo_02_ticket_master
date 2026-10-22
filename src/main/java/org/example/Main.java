package org.example;
import org.example.excepciones.ArchivoDatosException;
import org.example.repositorios.CompraJSONRepository;
import org.example.repositorios.EventosCSVRepository;
import org.example.modelo.Eventos;
import  org.example.servicios.AsientoService;
import  org.example.servicios.EventosService;
import  org.example.servicios.ComprasService;
import  org.example.servicios.AuditoriaService;
import  org.example.servicios.GeneradorQRService;
import  org.example.servicios.GeneradorPdfService;
import  org.example.servicios.ValidadorCompraService;
import  org.example.servicios.GraficaService;
import  org.example.servicios.ReporteService;

import java.util.List;


/**
 * Punto de entrada de la aplicación de venta de boletas.
 * Crea los repositorios y servicios y lanza el menú principal.
 */
public class Main {
    public static void main(String[] args) {
        AuditoriaService auditoriaService = new AuditoriaService("logs/auditoria.txt");
        try {
      // inicilizar repositorios
      EventosCSVRepository eventosRepository = new EventosCSVRepository("Data/eventos.csv");
      List<Eventos> eventos = eventosRepository.cargarEventos();
      if (eventos.isEmpty()) {
          System.out.println("No hay eventos válidos en data/eventos.csv. Revisa el archivo.");
          auditoriaService.registrar("Error: no se cargó ningún evento válido");
          return;
      }
      CompraJSONRepository compraRepository = new CompraJSONRepository("Data/compras.json");

      //inicilizamos servicios
      AsientoService asientoService = new AsientoService("Data/asientos.csv");
      EventosService eventosService = new EventosService(eventos);
      //Servicios auxiliares
      ValidadorCompraService validadorCompraService = new ValidadorCompraService(asientoService, eventosService);
      GeneradorQRService generadorQRService = new GeneradorQRService();
      GeneradorPdfService generadorPdfService = new GeneradorPdfService(eventosService, generadorQRService);
      //servicio principal
      ComprasService comprasService = new ComprasService(compraRepository, validadorCompraService, asientoService, auditoriaService, generadorPdfService, eventosService);
      GraficaService graficaService = new GraficaService();
      ReporteService reporteService = new ReporteService(eventosService,asientoService, compraRepository, graficaService);
      //menu
      MenuPrincipal menu = new MenuPrincipal(eventosService, asientoService, comprasService, generadorPdfService,auditoriaService, graficaService, reporteService);
      menu.mostrarMenu();

        } catch (ArchivoDatosException e) {
            System.out.println("No se pudieron cargar los datos: " + e.getMessage());
            registrarSeguro(auditoriaService, "Error de archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al iniciar: " + e.getMessage());
            registrarSeguro(auditoriaService, "Error inesperado: " + e.getMessage());
        }
    }

    private static void registrarSeguro(AuditoriaService auditoriaService, String mensaje) {
        try {
            auditoriaService.registrar(mensaje);
        } catch (RuntimeException ignorado) {
            // si la auditoría también falla no se puede hacer más
        }
    }
}
