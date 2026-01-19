import org.hibernate.Session;
import util.HibernateUtil;

public class Ejemplo3_Relaciones {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            // Crear usuario
            Usuario u = new Usuario("juan@mail.com", "Juan");

            // Agregar pedidos (sincroniza ambos lados)
            u.agregarPedido(new Pedido(u, 250.00));
            u.agregarPedido(new Pedido(u, 150.00));
            u.agregarPedido(new Pedido(u, 320.50));

            // Guardar usuario (CASCADE guarda pedidos automáticamente)
            session.persist(u);

            session.getTransaction().commit();
            System.out.println("Usuario creado con " + u.getPedidos().size() + " pedidos");

            // Recuperar usuario
            Usuario recuperado = session.get(Usuario.class, u.getId());
            System.out.println("\nPedidos del usuario " + recuperado.getNombre() + ":");
            recuperado.getPedidos().forEach(p ->
                    System.out.println("  - Pedido #" + p.getId() + ": $" + p.getTotal())
            );

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }
}