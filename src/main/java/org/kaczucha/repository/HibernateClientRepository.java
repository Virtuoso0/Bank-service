package org.kaczucha.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.kaczucha.repository.entity.Client;

public class HibernateClientRepository implements ClientRepository{
    @Override
    public void save(Client client) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        client.getAccountList().forEach(session::persist);
        session.persist(client);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Client findByEmail(String email) {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        // Hibernate query language
        Query<Client> query = session.createQuery("from Client where email=:em", Client.class);
        query.setParameter("em", email);
        Client client = query.uniqueResult();
        session.close();
        return client;
    }

    @Override
    public void deleteByEmail(String email) {

    }
}
