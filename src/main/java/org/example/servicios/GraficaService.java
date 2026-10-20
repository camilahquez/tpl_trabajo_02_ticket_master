package org.example.servicios;

import org.example.excepciones.ErrorReporteException;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

import java.io.IOException;
import java.util.List;

public class GraficaService {

    public byte[] generarGraficaBarras(String titulo, String tituloEjeX, String tituloEjeY, List<String> categorias, List<Double> valores) {
        if (categorias.isEmpty() || categorias.size() != valores.size()) {
            throw new ErrorReporteException("No hay datos válidos para generar la gráfica.");
        }
        CategoryChart grafica = new CategoryChartBuilder()
                .width(700)
                .height(400)
                .title(titulo)
                .xAxisTitle(tituloEjeX)
                .yAxisTitle(tituloEjeY)
                .build();

        grafica.getStyler().setLegendVisible(false);

        grafica.addSeries(tituloEjeY, categorias, valores);

        try {
            return BitmapEncoder.getBitmapBytes(grafica, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new ErrorReporteException("No se pudo generar la imagen de la gráfica.", e);
        }
    }
}
