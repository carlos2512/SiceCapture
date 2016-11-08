/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Client;
import entities.Document;
import entities.Expedient;
import entities.ExpedientClient;
import entities.ExpedientClientPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ExpedientClientJpaController implements Serializable {

    public ExpedientClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExpedientClient expedientClient) throws PreexistingEntityException, Exception {
        if (expedientClient.getExpedientClientPK() == null) {
            expedientClient.setExpedientClientPK(new ExpedientClientPK());
        }
        expedientClient.getExpedientClientPK().setFkClient(expedientClient.getClient().getIdUser());
        expedientClient.getExpedientClientPK().setFkExpedient(expedientClient.getExpedient().getIdExpedient());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client = expedientClient.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getIdUser());
                expedientClient.setClient(client);
            }
            Expedient expedient = expedientClient.getExpedient();
            if (expedient != null) {
                expedient = em.getReference(expedient.getClass(), expedient.getIdExpedient());
                expedientClient.setExpedient(expedient);
            }
            em.persist(expedientClient);
            if (client != null) {
                client.getExpedientClientCollection().add(expedientClient);
                client = em.merge(client);
            }
            if (expedient != null) {
                expedient.getExpedientClientCollection().add(expedientClient);
                expedient = em.merge(expedient);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findExpedientClient(expedientClient.getExpedientClientPK()) != null) {
                throw new PreexistingEntityException("ExpedientClient " + expedientClient + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExpedientClient expedientClient) throws NonexistentEntityException, Exception {
        expedientClient.getExpedientClientPK().setFkClient(expedientClient.getClient().getIdUser());
        expedientClient.getExpedientClientPK().setFkExpedient(expedientClient.getExpedient().getIdExpedient());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExpedientClient persistentExpedientClient = em.find(ExpedientClient.class, expedientClient.getExpedientClientPK());
            Client clientOld = persistentExpedientClient.getClient();
            Client clientNew = expedientClient.getClient();
            Expedient expedientOld = persistentExpedientClient.getExpedient();
            Expedient expedientNew = expedientClient.getExpedient();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getIdUser());
                expedientClient.setClient(clientNew);
            }
            if (expedientNew != null) {
                expedientNew = em.getReference(expedientNew.getClass(), expedientNew.getIdExpedient());
                expedientClient.setExpedient(expedientNew);
            }
            expedientClient = em.merge(expedientClient);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getExpedientClientCollection().remove(expedientClient);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getExpedientClientCollection().add(expedientClient);
                clientNew = em.merge(clientNew);
            }
            if (expedientOld != null && !expedientOld.equals(expedientNew)) {
                expedientOld.getExpedientClientCollection().remove(expedientClient);
                expedientOld = em.merge(expedientOld);
            }
            if (expedientNew != null && !expedientNew.equals(expedientOld)) {
                expedientNew.getExpedientClientCollection().add(expedientClient);
                expedientNew = em.merge(expedientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ExpedientClientPK id = expedientClient.getExpedientClientPK();
                if (findExpedientClient(id) == null) {
                    throw new NonexistentEntityException("The expedientClient with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

   public List<ExpedientClient> findExpedientClientByClient(Client fkClient){
       EntityManager em = getEntityManager();
       Query query = em.createNamedQuery("ExpedientClient.findByFkClient", ExpedientClient.class);
       query.setParameter("fkClient", fkClient);
       List<ExpedientClient> expedientClientList = (List<ExpedientClient>) query.getResultList();
       return expedientClientList;
   }

    public void destroy(ExpedientClientPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExpedientClient expedientClient;
            try {
                expedientClient = em.getReference(ExpedientClient.class, id);
                expedientClient.getExpedientClientPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The expedientClient with id " + id + " no longer exists.", enfe);
            }
            Client client = expedientClient.getClient();
            if (client != null) {
                client.getExpedientClientCollection().remove(expedientClient);
                client = em.merge(client);
            }
            Expedient expedient = expedientClient.getExpedient();
            if (expedient != null) {
                expedient.getExpedientClientCollection().remove(expedientClient);
                expedient = em.merge(expedient);
            }
            em.remove(expedientClient);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExpedientClient> findExpedientClientEntities() {
        return findExpedientClientEntities(true, -1, -1);
    }

    public List<ExpedientClient> findExpedientClientEntities(int maxResults, int firstResult) {
        return findExpedientClientEntities(false, maxResults, firstResult);
    }

    private List<ExpedientClient> findExpedientClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExpedientClient.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ExpedientClient findExpedientClient(ExpedientClientPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExpedientClient.class, id);
        } finally {
            em.close();
        }
    }

    public int getExpedientClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExpedientClient> rt = cq.from(ExpedientClient.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
