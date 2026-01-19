import org.hibernate.Session;
import org.hibernate.query.Query;
import model.Producto; // <--- ESTA ES LA LÍNEA QUE FALTA
import util.HibernateUtil;

import java.util.List;

public class ProductoService {

    /**
     * Obtiene un producto por ID.
     */
    public Producto obtenerPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Producto.class, id);
        } finally {
            session.close();
        }
    }

    /**
     * Obtiene todos los productos.
     */
    public List<Producto> obtenerTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Producto> q = session.createQuery("FROM Producto", Producto.class);
            return q.list();
        } finally {
            session.close();
        }
    }

    /**
     * Crea un nuevo producto.
     */
    public void crear(Producto producto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(producto);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Error al crear: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /**
     * Actualiza un producto existente.
     */
    public void actualizar(Producto producto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.merge(producto);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /**
     * Elimina un producto por ID.
     */
    public void eliminar(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Producto p = session.get(Producto.class, id);
            if (p != null) {
                session.remove(p);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}