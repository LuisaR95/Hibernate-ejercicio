import org.hibernate.Hibernate;
import org.hibernate.Session;
import util.HibernateUtil;

public class Ejemplo5_LazyVsEager {
    public static void main(String[] args) {
        // Preparar datos
        System.out.println("=== PREPARANDO DATOS ===");
        Session s0 = HibernateUtil.getSessionFactory().openSession();
        try {
            s0.beginTransaction();
            Usuario u = new Usuario("maria@mail.com", "María");
            u.agregarPedido(new Pedido(u, 100.0));
            u.agregarPedido(new Pedido(u, 200.0));
            u.agregarPedido(new Pedido(u, 150.0));
            s0.persist(u);
            s0.getTransaction().commit();
        } finally {
            s0.close();
        }

        // LAZY LOADING (por defecto)
        System.out.println("\n=== LAZY LOADING (default) ===");
        Session s1 = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("Query 1: Cargar usuario");
            Usuario u = s1.get(Usuario.class, 1);
            System.out.println("Usuario cargado: " + u.getNombre());
            System.out.println("Los pedidos aún NO están cargados (lazy)");

            System.out.println("\nAccediendo a pedidos...");
            System.out.println("Query 2: Se lanza automáticamente al acceder");
            int cantidad = u.getPedidos().size();
            System.out.println("Pedidos cargados: " + cantidad);

        } finally {
            s1.close();
        }

        // EAGER LOADING con JOIN FETCH
        System.out.println("\n=== EAGER LOADING (JOIN FETCH) ===");
        Session s2 = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("Query 1: Cargar usuario Y pedidos juntos");
            Usuario u = s2.createQuery(
                    "FROM Usuario u JOIN FETCH u.pedidos WHERE u.id = 1",
                    Usuario.class).uniqueResult();

            System.out.println("Usuario: " + u.getNombre());
            System.out.println("Pedidos ya están cargados: " + u.getPedidos().size());
            System.out.println("Solo 1 query en total");

        } finally {
            s2.close();
        }

        // INICIALIZAR EXPLÍCITAMENTE
        System.out.println("\n=== INICIALIZAR EXPLÍCITAMENTE ===");
        Session s3 = HibernateUtil.getSessionFactory().openSession();
        try {
            Usuario u = s3.get(Usuario.class, 1);
            System.out.println("Usuario cargado: " + u.getNombre());

            System.out.println("Inicializando pedidos...");
            Hibernate.initialize(u.getPedidos());  // Carga ahora dentro de sesión
            s3.close();  // Cierra sesión (pedidos ya están en memoria)

            System.out.println("Sesión cerrada, pero pedidos disponibles:");
            u.getPedidos().forEach(p ->
                    System.out.println("  - Pedido #" + p.getId() + ": $" + p.getTotal())
            );

        } finally {
            if (s3.isOpen()) s3.close();
        }

        // ERROR COMÚN: Lazy sin initialize
        System.out.println("\n=== ERROR COMÚN (LazyInitializationException) ===");
        Session s4 = HibernateUtil.getSessionFactory().openSession();
        try {
            Usuario u = s4.get(Usuario.class, 1);
            s4.close();
            System.out.println("Sesión cerrada");

            // ¡ERROR! Acceder a pedidos lazy fuera de sesión
            try {
                u.getPedidos().forEach(p -> System.out.println(p.getId()));
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getClass().getSimpleName());
                System.out.println("Mensaje: " + e.getMessage());
                System.out.println("LECCIÓN: Initialize datos antes de cerrar sesión");
            }

        } finally {
            if (s4.isOpen()) s4.close();
        }

        HibernateUtil.shutdown();
    }
}