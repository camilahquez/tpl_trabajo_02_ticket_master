package org.example;
import org.example.servicios.*;
import org.example.modelo.Eventos;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import org.example.excepciones.ErrorReporteException;
import org.example.servicios.ReporteService;
import org.example.servicios.GraficaService;
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
    private final GraficaService graficaService;
    private final ReporteService reporteService;

    public MenuPrincipal(EventosService eventosService, AsientoService asientoService, ComprasService comprasService, GeneradorPdfService generadorPdfService, AuditoriaService auditoriaService, GraficaService graficaService, ReporteService reporteService) {
        this.eventosService = eventosService;
        this.asientoService = asientoService;
        this.comprasService = comprasService;
        this.generadorPdfService = generadorPdfService;
        this.auditoriaService =auditoriaService;
        this.graficaService = graficaService;
        this.reporteService = reporteService;
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
        List<Eventos> eventos = eventosService.obtenerEventos();
        for (Eventos evento : eventos){
            System.out.println(evento.getNombreEvento());
        }
    }
    private void mostrasCodigoAsientos( String idEvento){
        List<Asiento> asientos = asientoService.obtenerAsientosPorEvento(idEvento);
        for(Asiento asiento : asientos){
            if(asiento.isOcupado()){
                System.out.println(asiento.getCodigoAsiento() + " ocupado");
            }
            else{
                System.out.println(asiento.getCodigoAsiento()+ " disponible");
            }

        }

    }

    private void comprarBoletas() throws Exception {
        System.out.println("Comprar boletas");
        consultarEventos();
        System.out.println("Ingresa nombre del evento: ");
        String nombreEvento = scanner.nextLine();
        Eventos evento = eventosService.buscarEventoPorNombre(nombreEvento);
        List<String> codigosAsistos = new ArrayList<>();
        System.out.println("Ingresa  nombre del comprador: ");
        String nombreComprador = scanner.nextLine();
        System.out.println("Ingresa  cedula del comprador: ");
        String documentoComprador = scanner.nextLine();
        System.out.println("Ingresa correo del comprador: ");
        String correoComprador = scanner.nextLine();
        mostrasCodigoAsientos(evento.getIdEvento());
        System.out.println("Ingresa la cantidad de asientos que se van a comprar:  ");
        int cantidadAsisntos = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < cantidadAsisntos; i++){
            int j = i +1;
            System.out.println("Ingresa el numero del "+  j + "º asiento: ");
            String codigoAsiento = scanner.nextLine();
            codigosAsistos.add(codigoAsiento);
        }
        List<Asiento> asientos = new ArrayList<>();
        for (int i = 0; i < cantidadAsisntos; i++){
           Asiento asiento = asientoService.buscarAsiento(evento.getIdEvento(), codigosAsistos.get(i));
            if (asiento != null){
                asientos.add(asiento);
            } }
            Compra compra =comprasService.realizarCompra(nombreComprador,documentoComprador, correoComprador, nombreEvento, asientos);
        if (compra == null) {
            System.out.println("No se pudo hacer la compra. Verifica los datos ingresados.");
            return;
        }

        boolean confirmada = comprasService.confirmarCompra(compra);

        if (confirmada) {
            System.out.println("Compra realizada correctamente");
        } else {
            System.out.println("No se pudo confirmar la compra");
        }

    }

    private void consultarCompras() {
        System.out.println("Consultar compras");
        List<Compra> compras = comprasService.obtenerCompras();
        for(Compra compra : compras){
            System.out.println(compra);
        }
    }

    private void generarTicket() throws Exception {
        System.out.println("Generar ticket PDF");
        System.out.println("ingresa la cedula del comprador: ");
        String cedulaComprador = scanner.nextLine();
        System.out.println("ingresa el nombre del evento");
        String nombreEvento = scanner.nextLine();
        List<Compra> compras =  comprasService.buscarCompra(cedulaComprador, nombreEvento);
        if (compras.isEmpty() ){
            System.out.println("no se encontraron compras");
        } else if (compras.size() == 1) {
            String rutapdf =generadorPdfService.generarPDF(compras.get(0));
            File archivo = new File(rutapdf);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }
        } else if (compras.size() > 1) {
            for (Compra compra : compras){
                System.out.println(compra);
            }
            System.out.println("digite el id la compra a la que se le quiere generar el pdf");
            String idEleccion = scanner.nextLine();
            boolean encontrada = false;
            for (Compra compra : compras){
                if (idEleccion.equals(compra.getId())){
                    String rutapdf =generadorPdfService.generarPDF(compra);
                    File archivo = new File(rutapdf);
                    if (Desktop.isDesktopSupported()) {
                          Desktop.getDesktop().open(archivo);}
                    encontrada = true;
                    break;
                }

            }
        }else{
            System.out.println("El id no coincide");
        }
    }


    private void consultarReportes() {
        System.out.println("Consultar reportes de ventas");
        System.out.println("\n===== CONSULTAR REPORTES =====");

        try {
            reporteService.generarReporte();

            System.out.println("Reporte generado correctamente.");
            System.out.println("Se guardó en: reportes/ventas_por_evento.pdf");

        } catch (ErrorReporteException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
    private void mostrarAuditoria(){
        System.out.println("\n--- AUDITORÍA ---");
        System.out.println(auditoriaService.mostrarRegistros());

    }
}