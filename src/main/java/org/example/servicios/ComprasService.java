package org.example.servicios;

import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import org.example.servicios.CalculadoraCompraService;
import org.example.servicios.ValidadorCompraService;
import java.util.UUID;
import java.util.List;
import org.example.repositorios.CompraJSONRepository;
import org.example.excepciones.AsientoNoDisponibleException;

public class ComprasService {
    private final CompraJSONRepository repositorio;
    private final ValidadorCompraService validadorCompraService;
    private final AsientoService asientoService;
    private final AuditoriaService auditoriaService;
    private final GeneradorPdfService generadorPdfService;

    public ComprasService(CompraJSONRepository repositorio, ValidadorCompraService validadorCompraService, AsientoService asientoService, AuditoriaService auditoriaService, GeneradorPdfService generadorPdfService) {
        this.repositorio = repositorio;
        this.validadorCompraService = validadorCompraService;
        this.asientoService= asientoService;
        this.auditoriaService = auditoriaService;
        this.generadorPdfService = generadorPdfService;
    }
    public String generarId() {
        return UUID.randomUUID().toString();
    }
    public boolean validarCompra(String nombreComprador, String documentoComprador, String correoComprador, String nombreEvento, List<Asiento> asientos){
        return validadorCompraService.verificarCorreo(correoComprador) && validadorCompraService.validarEvento(nombreEvento) && validadorCompraService.verificarSeleccionAsientos(asientos) && validadorCompraService.verificarCedula(documentoComprador) && validadorCompraService.verificarNombre(nombreComprador);

    }
    public Compra realizarCompra(String nombreComprador, String documentoComprador, String correoComprador, String nombreEvento, List<Asiento> asientos){
        if (validarCompra(nombreComprador, documentoComprador, correoComprador, nombreEvento, asientos)){
         double subtotal = CalculadoraCompraService.carcularSubtotal(asientos);
         double servicio = CalculadoraCompraService.calcularServicio(subtotal);
         double total = CalculadoraCompraService.calcularTotal(subtotal, servicio);
         String id =  generarId();
         Compra compra = new Compra(id, nombreComprador, documentoComprador, correoComprador,  nombreEvento, asientos, subtotal, servicio, total);
         return compra;
    } return null;
    }
    public boolean confirmarCompra(Compra compra) throws  Exception {
        if (compra == null) {
            auditoriaService.registrar("Error la compra es nula");
            return false;
        }
        try {
            for (Asiento asiento : compra.getAsientos()) {
               asientoService.reservarAsiento(asiento.getIdEvento(), asiento.getCodigoAsiento());
            }
            repositorio.guardar(compra);
            auditoriaService.registrar("Compra guardada correctamente, id: " + compra.getId());
            generadorPdfService.generarPDF(compra);
            return true;
            }catch (AsientoNoDisponibleException e) {
            System.err.println(e.getMessage());
            auditoriaService.registrar("No se realizo la compra, asintos no disponibles");
            return false;
        }
    }
    
}
