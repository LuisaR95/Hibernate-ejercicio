import model.Producto;

public class Ejemplo2_CRUD {
    public static void main(String[] args) {
        ProductoService service = new ProductoService();

        // CREATE
        System.out.println("=== CREATE ===");
        service.crear(new Producto("Mouse", 25.50));
        service.crear(new Producto("Teclado", 79.99));

        // READ
        System.out.println("\n=== READ ===");
        Producto p = service.obtenerPorId(1);
        System.out.println("Encontrado: " + p.getNombre() + " - $" + p.getPrecio());

        // UPDATE
        System.out.println("\n=== UPDATE ===");
        p.setPrecio(29.99);
        service.actualizar(p);
        System.out.println("Actualizado: " + p.getNombre() + " - $" + p.getPrecio());

        // LIST ALL
        System.out.println("\n=== LIST ALL ===");
        service.obtenerTodos().forEach(x ->
                System.out.println("  " + x.getNombre() + ": $" + x.getPrecio())
        );

        // DELETE
        System.out.println("\n=== DELETE ===");
        service.eliminar(1);
        System.out.println("Producto eliminado");

        System.out.println("\n=== VERIFICAR ===");
        service.obtenerTodos().forEach(x ->
                System.out.println("  " + x.getNombre() + ": $" + x.getPrecio())
        );
    }
}