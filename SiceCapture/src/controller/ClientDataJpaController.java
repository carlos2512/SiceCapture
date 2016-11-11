/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.DocumentData;
import entities.Client;
import entities.ClientData;
import entities.ClientDataPK;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ClientDataJpaController implements Serializable {

    public ClientDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientData clientData) throws PreexistingEntityException, Exception {
        if (clientData.getClientDataPK() == null) {
            clientData.setClientDataPK(new ClientDataPK());
        }
        clientData.getClientDataPK().setFkClient(clientData.getClient().getIdUser());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentData documentData = clientData.getDocumentData();
            if (documentData != null) {
                documentData = em.getReference(documentData.getClass(), documentData.getIdDocumentdata());
                clientData.setDocumentData(documentData);
            }
            Client client = clientData.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getIdUser());
                clientData.setClient(client);
            }
            em.persist(clientData);
            if (documentData != null) {
                documentData.getClientDataCollection().add(clientData);
                documentData = em.merge(documentData);
            }
            if (client != null) {
                client.getClientDataCollection().add(clientData);
                client = em.merge(client);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientData(clientData.getClientDataPK()) != null) {
                throw new PreexistingEntityException("ClientData " + clientData + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientData clientData) throws NonexistentEntityException, Exception {
        clientData.getClientDataPK().setFkClient(clientData.getClient().getIdUser());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientData persistentClientData = em.find(ClientData.class, clientData.getClientDataPK());
            DocumentData documentDataOld = persistentClientData.getDocumentData();
            DocumentData documentDataNew = clientData.getDocumentData();
            Client clientOld = persistentClientData.getClient();
            Client clientNew = clientData.getClient();
            if (documentDataNew != null) {
                documentDataNew = em.getReference(documentDataNew.getClass(), documentDataNew.getIdDocumentdata());
                clientData.setDocumentData(documentDataNew);
            }
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getIdUser());
                clientData.setClient(clientNew);
            }
            clientData = em.merge(clientData);
            if (documentDataOld != null && !documentDataOld.equals(documentDataNew)) {
                documentDataOld.getClientDataCollection().remove(clientData);
                documentDataOld = em.merge(documentDataOld);
            }
            if (documentDataNew != null && !documentDataNew.equals(documentDataOld)) {
                documentDataNew.getClientDataCollection().add(clientData);
                documentDataNew = em.merge(documentDataNew);
            }
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getClientDataCollection().remove(clientData);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getClientDataCollection().add(clientData);
                clientNew = em.merge(clientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ClientDataPK id = clientData.getClientDataPK();
                if (findClientData(id) == null) {
                    throw new NonexistentEntityException("The clientData with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ClientDataPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientData clientData;
            try {
                clientData = em.getReference(ClientData.class, id);
                clientData.getClientDataPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientData with id " + id + " no longer exists.", enfe);
            }
            DocumentData documentData = clientData.getDocumentData();
            if (documentData != null) {
                documentData.getClientDataCollection().remove(clientData);
                documentData = em.merge(documentData);
            }
            Client client = clientData.getClient();
            if (client != null) {
                client.getClientDataCollection().remove(clientData);
                client = em.merge(client);
            }
            em.remove(clientData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClientData> findClientDataEntities() {
        return findClientDataEntities(true, -1, -1);
    }

    public List<ClientData> findClientDataEntities(int maxResults, int firstResult) {
        return findClientDataEntities(false, maxResults, firstResult);
    }

    private List<ClientData> findClientDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientData.class));
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

    public ClientData findClientData(ClientDataPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientData.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientData> rt = cq.from(ClientData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
