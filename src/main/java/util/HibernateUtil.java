package util;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Singleton que gestiona la SessionFactory de Hibernate.
 * Se inicializa UNA SOLA VEZ al cargar la clase (bloque static).
 *
 * Uso en los ejemplos:
 * - Session session = HibernateUtil.getSessionFactory().openSession();
 * - HibernateUtil.shutdown(); (al finalizar la app)
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            // Carga la configuración de hibernate.cfg.xml
            // (debe estar en src/main/resources/)
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error inicializando SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Obtiene la SessionFactory singleton.
     * Todos los ejemplos llaman a esto: getSessionFactory().openSession()
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory (llamar al finalizar la app).
     * Todos los ejemplos lo llaman en el finally o al final.
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("✓ SessionFactory cerrada");
        }
    }
}
