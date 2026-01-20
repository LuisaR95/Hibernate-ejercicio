package org.example.dao;

import org.example.models.Autor;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * DAO para la getión de Autores.
 * Implementa operaciones CRUD y consultas HQL.
 */
public class AutorDAO {

    /**
     * Crea un nuevo autor en la base de datos.
     */
    public void crear(Autor autor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(autor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al crear autor: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los autores ordenados por nombre (HQL 1).
     */
    public List<Autor> obtenerTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Requisito: Consulta HQL con ordenamiento
            return session.createQuery("FROM Autor a ORDER BY a.nombre ASC", Autor.class).list();
        }
    }

    public Autor obtenerPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Autor.class, id);
        }
    }

    public void actualizar(Autor autor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(autor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar autor: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Autor autor = session.get(Autor.class, id);
            if (autor != null) session.remove(autor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar autor: " + e.getMessage());
        }
    }
}