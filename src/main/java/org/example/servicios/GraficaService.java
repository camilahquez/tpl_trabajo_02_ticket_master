package org.example.servicios;

import org.example.excepciones.ErrorReporteException;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

import java.io.IOException;
import java.util.List;
//Se encarga de generar gráficas de barras a partir de categorías y valores proporcionados. Utiliza la biblioteca XChart para construir la gráfica y convertirla en una imagen PNG, que puede utilizarse en los reportes de la aplicación.
public class GraficaService {
//Genera una gráfica de barras a partir de las categorías y sus valores correspondientes. Recibe el título de la gráfica, los títulos de los ejes X e Y, una lista de categorías y una lista de valores numéricos. Verifica que existan categorías y que ambas listas tengan la misma cantidad de elementos. Devuelve la gráfica en formato PNG como un arreglo de bytes (byte[]). Lanza una excepción ErrorReporteException si los datos no son válidos o si ocurre un error al generar la imagen.
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
