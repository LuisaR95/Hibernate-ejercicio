package org.example.seeders;

import org.example.models.Autor;
import org.example.models.Categoria;
import org.example.models.Libro;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSeeder {

    public static void ejecutar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            // 1. Crear Categorías
            Categoria ficcion = new Categoria("Ficción");
            Categoria clasico = new Categoria("Clásico");
            Categoria aventura = new Categoria("Aventura");
            Categoria distopia = new Categoria("Distopía");

            session.persist(ficcion);
            session.persist(clasico);
            session.persist(aventura);
            session.persist(distopia);

            // 2. Crear Autores y sus Libros

            // Autor 1
            Autor cervantes = new Autor("Miguel de Cervantes", "Española");
            session.persist(cervantes);

            Libro quijote = new Libro("Don Quijote de la Mancha", 25.50, cervantes);
            quijote.getCategorias().add(ficcion);
            quijote.getCategorias().add(clasico);
            session.persist(quijote);

            // Autor 2
            Autor orwell = new Autor("George Orwell", "Británica");
            session.persist(orwell);

            Libro milNovecientos = new Libro("1984", 18.90, orwell);
            milNovecientos.getCategorias().add(distopia);
            milNovecientos.getCategorias().add(ficcion);
            session.persist(milNovecientos);

            Libro rebelion = new Libro("Rebelión en la Granja", 15.00, orwell);
            rebelion.getCategorias().add(ficcion);
            session.persist(rebelion);

            // Autor 3
            Autor gabriel = new Autor("Gabriel García Márquez", "Colombiana");
            session.persist(gabriel);

            Libro soledad = new Libro("Cien Años de Soledad", 22.00, gabriel);
            soledad.getCategorias().add(clasico);
            session.persist(soledad);

            // Autor
            Autor verne = new Autor("Julio Verne", "Francesa");
            session.persist(verne);

            Libro leguas = new Libro("20.000 Leguas de Viaje Submarino", 19.50, verne);
            leguas.getCategorias().add(aventura);
            session.persist(leguas);

            transaction.commit();
            System.out.println("[SEEDER] Datos reales cargados correctamente.");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}