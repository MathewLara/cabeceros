package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
// Import necesario para la clase Cookie
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import service.ProductoService;
import service.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
// Import necesario para usar Arrays.stream()
import java.util.Arrays;
import java.util.List;
// Import necesario para la clase Optional
import java.util.Optional;

/*
 * autor: Mathew Lara
 * fecha y version: 10/11/2025 version: 2.0
 * descripcion: Servlet con doble propósito. Responde en dos rutas:
 * 1. "/productos.html": Muestra una tabla HTML con los productos.
 * (Con lógica de cookies para ocultar/mostrar precios).
 * 2. "/productos.xls": Genera un archivo Excel (XLS) para descargar
 * (También con lógica de cookies).
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
        // 2. Obtener y verificar la cookie de la sesion
        // Obtener todas las cookies que el navegador envió en la solicitud.
        // req.getCookies() puede devolver 'null' si el navegador no envía ninguna.
        // Si es 'null', creamos un array de Cookie vacío (new Cookie[0]).
        // Si NO es 'null', usamos el array de cookies devuelto.
        Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];

        // Buscar la cookie "username" usando un Stream de Java.
        // Convertimos el array 'cookies' en un Stream para usar métodos funcionales.
        Optional<String> cookieOptional = Arrays.stream(cookies)
                // Filtrar: Nos quedamos solo con la cookie cuyo nombre ("getName()")
                // sea exactamente "username".
                .filter(c -> "username".equals(c.getName()))
                // Mapear: De la cookie filtrada, extraemos solo su valor ("getValue()").
                .map(Cookie::getValue)
                // Buscar: Tomamos el primer (y único) valor que haya coincidido.
                .findAny();

        // 'cookieOptional' es ahora un contenedor.
        // - Si se encontró la cookie "username", cookieOptional.isPresent() será 'true'.
        // - Si no se encontró, cookieOptional.isPresent() será 'false'.


        // 3. Determinar qué tipo de respuesta quiere el cliente (HTML o Excel)
        resp.setContentType("text/html;charset=UTF-8");
        String servletPath=req.getServletPath();
        boolean esXls=servletPath.endsWith(".xls");

        // 4. Configurar las cabeceras (headers) de la respuesta
        if (esXls){
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        }

        // 5. Escribir el cuerpo de la respuesta (HTML o la tabla para Excel)
        try (PrintWriter out = resp.getWriter()) {
            // Si NO es Excel, se escribe la cabecera HTML y los enlaces
            if (!esXls) {
                //(código de la plantilla HTML: <html>, <head>, <body>, <h1>, etc.)
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Listado dre Productos</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Listado de productos</h1>");
                out.println("<p><a href=\"" + req.getContextPath() + "/productos.xls" + "\">exportar a excel</a></p>");
                out.println("<p><a href=\"" + req.getContextPath() + "/productojson" + "\">mostrar json</a></p>");
            }

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>tipo</th>");
            out.println("<th>precio</th>");
            out.println("</tr>");

            // 6. Recorre los productos y los muestra en la lista
            // Iteramos sobre la lista de productos. 'p' es cada objeto Producto.
            productos.forEach(p->{
                out.println("<tr>");
                out.println("<td>"+p.getId()+"</td>");
                out.println("<td>"+p.getNombre()+"</td>");
                out.println("<td>"+p.getTipo()+"</td>");

                // 7. Logica del cookie
                // Aquí usamos la variable 'cookieOptional' que definimos en el 2.
                // se pregunta si está presente el valor de la cookie
                // (Es decir, ¿el usuario SÍ inició sesión?).
                if (cookieOptional.isPresent()) {
                    // Si hay sesión: El usuario está logueado.
                    // Imprimimos la celda (<td>) con el precio real del producto.
                    out.println("<td>"+p.getPrecio()+"</td>");
                } else {
                    // Si no hay sesión: El usuario no está logueado.
                    // Imprimimos la celda (<td>) con un mensaje indicando
                    // que debe iniciar sesión.
                    out.println("<td>--- Inicie sesión para ver ---</td>");
                }
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