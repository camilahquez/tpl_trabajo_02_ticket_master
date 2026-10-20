package org.example;
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


public class Main  {
  public static  void main(String[] arg) throws Exception{
      // inicilizar repositorios
      EventosCSVRepository eventosRepository = new EventosCSVRepository("Data/eventos.csv");
      List<Eventos> eventos = eventosRepository.cargarEventos();
      CompraJSONRepository compraRepository = new CompraJSONRepository("Data/compras.json");
      //inicilizamos servicios

      AsientoService asientoService = new AsientoService("Data/asientos.csv");
      EventosService eventosService = new EventosService(eventos);
      //Servicios auxiliares
      ValidadorCompraService validadorCompraService = new ValidadorCompraService(asientoService, eventosService);
      AuditoriaService auditoriaService = new AuditoriaService("Data/auditoria.txt");
      GeneradorQRService generadorQRService = new GeneradorQRService();
      GeneradorPdfService generadorPdfService = new GeneradorPdfService(eventosService, generadorQRService);
      //servicio principal
      ComprasService comprasService = new ComprasService(compraRepository, validadorCompraService, asientoService, auditoriaService, generadorPdfService, eventosService);
      GraficaService graficaService = new GraficaService();
      ReporteService reporteService = new ReporteService(eventosService,asientoService, compraRepository, graficaService);
      //menu
      MenuPrincipal menu = new MenuPrincipal(eventosService, asientoService, comprasService, generadorPdfService,auditoriaService, graficaService, reporteService);
      menu.mostrarMenu();


      ;

  }




}