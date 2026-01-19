import model.Producto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class Ejemplo4_HQL {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Poblar datos
            session.beginTransaction();
            session.persist(new Producto("Teclado", 79.99));
            session.persist(new Producto("Monitor", 299.99));
            session.persist(new Producto("Webcam", 49.99));
            session.persist(new Producto("Mouse", 25.50));
            session.getTransaction().commit();

            // 1. SELECT simple
            System.out.println("=== Todos los productos ===");
            Query<Producto> q1 = session.createQuery("FROM Producto", Producto.class);
            q1.list().forEach(p -> System.out.println("  " + p.getNombre()));

            // 2. WHERE con parámetro
            System.out.println("\n=== Productos > $50 ===");
            Query<Producto> q2 = session.createQuery(
                    "FROM Producto WHERE precio > :minPrice", Producto.class);
            q2.setParameter("minPrice", 50.0);
            q2.list().forEach(p -> System.out.println("  " + p.getNombre() + ": $" + p.getPrecio()));

            // 3. COUNT - contar registros
            System.out.println("\n=== Total de productos ===");
            Query<Long> q3 = session.createQuery(
                    "SELECT COUNT(*) FROM Producto", Long.class);
            System.out.println("  Cantidad: " + q3.uniqueResult());

            // 4. AVG - promedio
            System.out.println("\n=== Precio promedio ===");
            Query<Double> q4 = session.createQuery(
                    "SELECT AVG(precio) FROM Producto", Double.class);
            Double promedio = q4.uniqueResult();
            System.out.println("  Promedio: $" + String.format("%.2f", promedio));

            // 5. ORDER BY
            System.out.println("\n=== Ordenados por precio (DESC) ===");
            Query<Producto> q5 = session.createQuery(
                    "FROM Producto ORDER BY precio DESC", Producto.class);
            q5.list().forEach(p -> System.out.println("  $" + p.getPrecio() + " - " + p.getNombre()));

            // 6. LIKE - búsqueda textual
            System.out.println("\n=== Búsqueda: 'tado' ===");
            Query<Producto> q6 = session.createQuery(
                    "FROM Producto WHERE LOWER(nombre) LIKE LOWER('%' || :patron || '%')", Producto.class);
            q6.setParameter("patron", "tado");
            q6.list().forEach(p -> System.out.println("  " + p.getNombre()));

        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}