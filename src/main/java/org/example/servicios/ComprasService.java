package org.example.servicios;
import org.example.excepciones.EventoNoEncontradoException;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import org.example.repositorios.CompraJSONRepository;
import org.example.excepciones.AsientoNoDisponibleException;
import org.example.modelo.Eventos;
//Se encarga de la gestion de las compras antes de genrera una compra valida los datos del usurio y la diponibilidad de los asientos. Confirma la compra, que hace un  llamdo para guardar la compra cambiar el estado de los asientos y genera el pdf con el boleto.
public class ComprasService {
    private final CompraJSONRepository repositorio;
    private final ValidadorCompraService validadorCompraService;
    private final AsientoService asientoService;
    private final AuditoriaService auditoriaService;
    private final GeneradorPdfService generadorPdfService;
    private final  EventosService eventosService;

//Inicializa el servicio de reportes mediante la inyección de las dependencias necesarias
    public ComprasService(CompraJSONRepository repositorio, ValidadorCompraService validadorCompraService, AsientoService asientoService, AuditoriaService auditoriaService, GeneradorPdfService generadorPdfService, EventosService eventosService) {
        this.repositorio = repositorio;
        this.validadorCompraService = validadorCompraService;
        this.asientoService= asientoService;
        this.auditoriaService = auditoriaService;
        this.generadorPdfService = generadorPdfService;
        this.eventosService = eventosService;
    }
    //genera el id de la compra de forma ramdon
    public String generarId() {
        return UUID.randomUUID().toString();
    }
    //por medio del validador verifica que que los datos del comprador sen correctos y que la lista de asientos no este vacia ni contenga asientos ya ocupados
    public boolean validarCompra(String nombreComprador, String documentoComprador, String correoComprador, String nombreEvento, List<Asiento> asientos) throws EventoNoEncontradoException {

        boolean correoValido = validadorCompraService.verificarCorreo(correoComprador);
        boolean eventoValido = validadorCompraService.validarEvento(nombreEvento);
        boolean asientosValidos = validadorCompraService.verificarSeleccionAsientos(asientos);
        boolean cedulaValida = validadorCompraService.verificarCedula(documentoComprador);
        boolean nombreValido = validadorCompraService.verificarNombre(nombreComprador);

        System.out.println("Correo válido: " + correoValido);
        System.out.println("Evento válido: " + eventoValido);
        System.out.println("Asientos válidos: " + asientosValidos);
        System.out.println("Cédula válida: " + cedulaValida);
        System.out.println("Nombre válido: " + nombreValido);

        return correoValido && eventoValido && asientosValidos
                && cedulaValida && nombreValido;
    }
    //resive los datos del usuria y los valida con validarCompra y gener un id con generaId si lod datos son correctos genera una compra si no duveulve null
    public Compra realizarCompra(String nombreComprador, String documentoComprador, String correoComprador, String nombreEvento, List<Asiento> asientos) throws EventoNoEncontradoException {
        if (validarCompra(nombreComprador, documentoComprador, correoComprador, nombreEvento, asientos)){
         double subtotal = CalculadoraCompraService.carcularSubtotal(asientos);
         double servicio = CalculadoraCompraService.calcularServicio(subtotal);
         double total = CalculadoraCompraService.calcularTotal(subtotal, servicio);
         String id =  generarId();
         Compra compra = new Compra(id, nombreComprador, documentoComprador, correoComprador,  nombreEvento, asientos, subtotal, servicio, total);

         return compra;
    } return null;
    }
    //resive la compra que se genero  si es nula registra un error en el historial de auditoria si la compra es valida se reservan los asientos de la compra, se guarda la compra en el repositorio y se registra la compra en auditoria. si los asientos no estan dispibles no se guarda la compra y se registra en la auditoria que no se puedo hacer la compra porque los asientos no estan dispobles.
    public boolean confirmarCompra(Compra compra) throws  Exception {
        if (compra == null) {
            auditoriaService.registrar("Error la compra es nula");
            return false;
        }
        try {

            repositorio.guardar(compra);
            auditoriaService.registrar("Compra guardada correctamente, id: " + compra.getId());
            for (Asiento asiento : compra.getAsientos()) {
                asientoService.reservarAsiento(asiento.getIdEvento(), asiento.getCodigoAsiento());
            }
            String pdfRuta = generadorPdfService.generarPDF(compra);
            return true;
            }catch (AsientoNoDisponibleException e) {
            System.err.println(e.getMessage());
            auditoriaService.registrar("No se realizo la compra, asintos no disponibles");
            return false;
        }
    }
    //devulve todas las compras en el repositorio
    public List<Compra> obtenerCompras(){
        return repositorio.obtenerTodas();
    }
//busca una compra en el repositorio segun el nombre del copmprador y el nombre del evento
    public List<Compra> buscarCompraNombreComprador(String nombreComprador, String nombreEvento) {
        List<Compra> compras = obtenerCompras();
        List<Compra> comprasBuscadas = new ArrayList<>();
        for (Compra compra : compras) {
            if (compra.getNombreComprador().equalsIgnoreCase(nombreComprador) && compra.getNombreEvento().equalsIgnoreCase(nombreEvento)) {
                comprasBuscadas.add(compra);
                return comprasBuscadas;
            }
        }
        System.out.println("No se encontraron compras");
        return comprasBuscadas;
    }
    //busca una compra en el repositorio segun el documento del copmprador y el nombre del evento
    public List<Compra> buscarCompra(String cedulaComprador, String nombreEvento) {
        List<Compra> compras = obtenerCompras();
        List<Compra> comprasBuscadas = new ArrayList<>();
        for (Compra compra : compras) {
            if (compra.getDocumentoComprador().equalsIgnoreCase(cedulaComprador) && compra.getNombreEvento().equalsIgnoreCase(nombreEvento)) {
                comprasBuscadas.add(compra);
            }
        }
        return comprasBuscadas;
    }

}
