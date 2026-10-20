package org.example;
import  org.example.excepciones.EventoNoEncontradoException;
import org.example.servicios.*;
import org.example.modelo.Eventos;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);
    private final EventosService eventosService;
    private final AsientoService asientoService;
    private final ComprasService comprasService;
    private  final GeneradorPdfService generadorPdfService;
    private  final AuditoriaService auditoriaService;

    public MenuPrincipal(EventosService eventosService, AsientoService asientoService, ComprasService comprasService, GeneradorPdfService generadorPdfService, AuditoriaService auditoriaService) {
        this.eventosService = eventosService;
        this.asientoService = asientoService;
        this.comprasService = comprasService;
        this.generadorPdfService = generadorPdfService;
        this.auditoriaService =auditoriaService;
    }

    public void mostrarMenu() throws Exception {
        int opcion = 0;

        do {
            System.out.println("\n===== TICKETERA =====");
            System.out.println("1. Consultar eventos");
            System.out.println("2. Comprar boletas");
            System.out.println("3. Consultar compras");
            System.out.println("4. Generar ticket PDF");
            System.out.println("5. Consultar reportes de ventas");
            System.out.println("6.Auditoria");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            if (!scanner.hasNextInt()) {System.out.println("Debe ingresar un número válido.");scanner.nextLine();
                continue;
            }

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    consultarEventos();
                    break;
                case 2:
                    comprarBoletas();
                    break;
                case 3:
                    consultarCompras();
                    break;
                case 4:
                    generarTicket();
                    break;
                case 5:
                    consultarReportes();
                    break;
                case 6:
                    mostrarAuditoria();
                case 7:
                    System.out.println("Gracias por utilizar la tiquetera.");
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }

        } while (opcion != 7);
    }

    private void consultarEventos() {
        System.out.println("Consultar eventos");


    }

    private void comprarBoletas() throws Exception {
        System.out.println("Comprar boletas");
        consultarEventos();
        System.out.println("Ingresa nombre del evento: ");
        String nombreEvento = scanner.nextLine();
        Eventos evento = eventosService.buscarEventoPorNombre(nombreEvento);
        asientoService.obtenerAsientosPorEvento(evento.getIdEvento());
        List<String> codigosAsistos = new ArrayList<>();
        System.out.println("Ingresa  nombre del comprador: ");
        String nombreComprador = scanner.nextLine();
        System.out.println("Ingresa  cedula del comprador: ");
        String documentoComprador = scanner.nextLine();
        System.out.println("Ingresa correo del comprador: ");
        String correoComprador = scanner.nextLine();
        System.out.println("Ingresa la cantidad de asientos que se van a comprar:  ");
        int cantidadAsisntos = scanner.nextInt();
        for (int i = 0; i <= cantidadAsisntos; i++){
            System.out.println("Ingresa el numero del "+ i+1 + "º asiento: ");
            String codigoAsiento = scanner.nextLine();
            codigosAsistos.add(codigoAsiento);
        }
        List<Asiento> asientos = new ArrayList<>();
        for (int i = 0; i <= cantidadAsisntos; i++){
           Asiento asiento = asientoService.buscarAsiento(evento.getIdEvento(), codigosAsistos.get(i));
            if (asiento != null){
                asientos.add(asiento);
            }
            Compra compra =comprasService.realizarCompra(nombreComprador,documentoComprador, correoComprador, nombreEvento, asientos);
            comprasService.confirmarCompra(compra);

        }

    }

    private void consultarCompras() {
        System.out.println("Consultar compras");

    }

    private void generarTicket() throws Exception {
        System.out.println("Generar ticket PDF");
        System.out.println("ingresa el nombre del comprador: ");
        String nombreComprador = scanner.nextLine();
        System.out.println("ingresa el nombre del evento");
        String nombreEvento = scanner.nextLine();
        Compra compra =  comprasService.buscarCompra(nombreComprador, nombreEvento);
        String rutapdf =generadorPdfService.generarPDF(compra);
        File archivo = new File(rutapdf);
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivo);
        }

    }

    private void consultarReportes() {
        System.out.println("Consultar reportes de ventas");
        // Calcular estadísticas y generar el reporte
    }
    private void mostrarAuditoria(){
        System.out.println("\n--- AUDITORÍA ---");
        System.out.println(auditoriaService.mostrarRegistros());

    }
}