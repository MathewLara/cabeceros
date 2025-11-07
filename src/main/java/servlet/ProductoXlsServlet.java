package servlet;

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
 * descripcion: Servlet con doble propósito. Responde en dos rutas:
 * 1. "/productos.html": Muestra una tabla HTML con los productos.
 * 2. "/productos.xls": Genera un archivo Excel (XLS) para descargar
 * con esos mismos productos.
 */
@WebServlet ({"/productos.xls","/productos.html"})
public class ProductoXlsServlet extends HttpServlet {
    /*
     * Metodo que maneja las solicitudes HTTP GET.
     * El parametro req define la solicitud del cliente (HttpServletRequest).
     * El parametro resp define la respuesta al cliente (HttpServletResponse).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Obtener la lista de productos desde el servicio
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listar();

        // 2. Determinar qué tipo de respuesta quiere el cliente (HTML o Excel)
        // Si no, se configuran las cabeceras para HTML
        resp.setContentType("text/html;charset=UTF-8");
        // Se obtiene la ruta del servlet (ej: /productos.xls)
        String servletPath=req.getServletPath();
        // Se comprueba si la ruta termina en .xls
        boolean esXls=servletPath.endsWith(".xls");

        // 3. Configurar las cabeceras (headers) de la respuesta
        if (esXls){
            // Si es Excel, se configuran las cabeceras para Excel
            resp.setContentType("application/vnd.ms-excel");
            // Se fuerza la descarga con un nombre de archivo
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        }
        // 4. Escribir el cuerpo de la respuesta (HTML o la tabla para Excel)
        try (PrintWriter out = resp.getWriter()) {
            // Si NO es Excel, se escribe la cabecera HTML y los enlaces
            if (!esXls) {
                //Creo la plantilla html
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listado dre Productos</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Listado de productos</h1>");
                // Enlace para descargar el Excel (apunta a este mismo servlet pero con .xls)
                out.println("<p><a href=\"" + req.getContextPath() + "/productos.xls" + "\">exportar a excel</a></p>");
                // Enlace para descargar el JSON (apunta al otro servlet)
                out.println("<p><a href=\"" + req.getContextPath() + "/productojson" + "\">mostrar json</a></p>");
            }

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>tipo</th>");
            out.println("<th>precio</th>");
            out.println("</tr>");
            // Si NO es Excel, se cierran las etiquetas HTML
            productos.forEach(p->{
                out.println("<tr>");
                out.println("<td>"+p.getId()+"</td>");
                out.println("<td>"+p.getNombre()+"</td>");
                out.println("<td>"+p.getTipo()+"</td>");
                out.println("<td>"+p.getPrecio()+"</td>");
                out.println("</tr>");
            });
            out.println("</table>");
            // Si NO es Excel, se cierran las etiquetas HTML
            if (!esXls) {
                out.println("</body>");
                out.println("</html>");
            }

        }
    }
}
