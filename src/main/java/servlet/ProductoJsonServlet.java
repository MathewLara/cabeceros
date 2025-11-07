package servlet;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import service.ProductoService;
import service.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/*
 * autor: Mathew Lara
 * fecha y version: 06/11/2025 version: 1.0
 * descripcion: Servlet que maneja la ruta "/productojson".
 * Obtiene la lista de productos, la convierte a formato JSON
 * usando la librería Gson y la ofrece al cliente como un
 * archivo descargable llamado "productos.json".
 */
@WebServlet("/productojson")
public class ProductoJsonServlet extends HttpServlet {

    /*
     * Metodo que maneja las solicitudes HTTP GET.
     * El parametro req define la solicitud del cliente (HttpServletRequest).
     * El parametro resp define la respuesta al cliente (HttpServletResponse).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtenemos la lista de productos desde el servicio
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listar();

        // 2. Convertimos la lista de Java a un string JSON
        // Se crea una instancia de la librería Gson
        Gson gson = new Gson();
        // Se usa el metodo toJson para convertir la lista
        String json = gson.toJson(productos);

        // 3. Establecemos el tipo de contenido como JSON genérico
        resp.setContentType("application/json");

        // 4.Esta cabecera fuerza la descarga en el navegador
        // y le da el nombre "productos.json" al archivo.
        resp.setHeader("Content-Disposition", "attachment; filename=\"productos.json\"");

        // 5. Escribimos el string JSON en la respuesta
        try (PrintWriter out = resp.getWriter()) {
            out.print(json);
        }
    }
}