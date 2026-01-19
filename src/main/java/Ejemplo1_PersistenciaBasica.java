import org.hibernate.Session;
import util.HibernateUtil; // Clase auxiliar para el SessionFactory
import model.Producto;     // Tu entidad Producto

public class Ejemplo1_PersistenciaBasica {
    public static void main(String[] args) {
        // Obtenemos la sesión desde nuestra utilidad
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            // 1. Estado TRANSIENT (Transitorio)
            // El objeto existe en memoria, pero Hibernate no sabe nada de él.
            Producto p = new Producto("Laptop", 999.99);
            System.out.println("Estado Transient - ID es nulo");

            // 2. Estado MANAGED (Gestionado/Persistente)
            // Al hacer persist(), el objeto entra en el contexto de persistencia.
            session.persist(p);
            System.out.println("Estado Managed - ID asignado: " + p.getId());

            // 3. Sincronización con la BD
            // El commit dispara el SQL INSERT.
            session.getTransaction().commit();
            System.out.println("Guardado en BD con éxito.");

            // 4. Recuperación
            Producto recuperado = session.get(Producto.class, p.getId());
            System.out.println("Recuperado: " + recuperado.getNombre());

        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close(); // 5. Estado DETACHED (Desconectado)
        }
    }
}