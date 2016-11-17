/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import entities.Client;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.ExpedientClient;
import java.util.ArrayList;
import java.util.Collection;
import entities.Image;
import entities.ClientData;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) {
        if (client.getExpedientClientCollection() == null) {
            client.setExpedientClientCollection(new ArrayList<ExpedientClient>());
        }
        if (client.getImageCollection() == null) {
            client.setImageCollection(new ArrayList<Image>());
        }
        if (client.getClientDataCollection() == null) {
            client.setClientDataCollection(new ArrayList<ClientData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ExpedientClient> attachedExpedientClientCollection = new ArrayList<ExpedientClient>();
            for (ExpedientClient expedientClientCollectionExpedientClientToAttach : client.getExpedientClientCollection()) {
                expedientClientCollectionExpedientClientToAttach = em.getReference(expedientClientCollectionExpedientClientToAttach.getClass(), expedientClientCollectionExpedientClientToAttach.getExpedientClientPK());
                attachedExpedientClientCollection.add(expedientClientCollectionExpedientClientToAttach);
            }
            client.setExpedientClientCollection(attachedExpedientClientCollection);
            Collection<Image> attachedImageCollection = new ArrayList<Image>();
            for (Image imageCollectionImageToAttach : client.getImageCollection()) {
                imageCollectionImageToAttach = em.getReference(imageCollectionImageToAttach.getClass(), imageCollectionImageToAttach.getIdImage());
                attachedImageCollection.add(imageCollectionImageToAttach);
            }
            client.setImageCollection(attachedImageCollection);
            Collection<ClientData> attachedClientDataCollection = new ArrayList<ClientData>();
            for (ClientData clientDataCollectionClientDataToAttach : client.getClientDataCollection()) {
                clientDataCollectionClientDataToAttach = em.getReference(clientDataCollectionClientDataToAttach.getClass(), clientDataCollectionClientDataToAttach.getClientDataPK());
                attachedClientDataCollection.add(clientDataCollectionClientDataToAttach);
            }
            client.setClientDataCollection(attachedClientDataCollection);
            em.persist(client);
            for (ExpedientClient expedientClientCollectionExpedientClient : client.getExpedientClientCollection()) {
                Client oldClientOfExpedientClientCollectionExpedientClient = expedientClientCollectionExpedientClient.getClient();
                expedientClientCollectionExpedientClient.setClient(client);
                expedientClientCollectionExpedientClient = em.merge(expedientClientCollectionExpedientClient);
                if (oldClientOfExpedientClientCollectionExpedientClient != null) {
                    oldClientOfExpedientClientCollectionExpedientClient.getExpedientClientCollection().remove(expedientClientCollectionExpedientClient);
                    oldClientOfExpedientClientCollectionExpedientClient = em.merge(oldClientOfExpedientClientCollectionExpedientClient);
                }
            }
            for (Image imageCollectionImage : client.getImageCollection()) {
                Client oldFkClientOfImageCollectionImage = imageCollectionImage.getFkClient();
                imageCollectionImage.setFkClient(client);
                imageCollectionImage = em.merge(imageCollectionImage);
                if (oldFkClientOfImageCollectionImage != null) {
                    oldFkClientOfImageCollectionImage.getImageCollection().remove(imageCollectionImage);
                    oldFkClientOfImageCollectionImage = em.merge(oldFkClientOfImageCollectionImage);
                }
            }
            for (ClientData clientDataCollectionClientData : client.getClientDataCollection()) {
                Client oldClientOfClientDataCollectionClientData = clientDataCollectionClientData.getClient();
                clientDataCollectionClientData.setClient(client);
                clientDataCollectionClientData = em.merge(clientDataCollectionClientData);
                if (oldClientOfClientDataCollectionClientData != null) {
                    oldClientOfClientDataCollectionClientData.getClientDataCollection().remove(clientDataCollectionClientData);
                    oldClientOfClientDataCollectionClientData = em.merge(oldClientOfClientDataCollectionClientData);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getIdUser());
            Collection<ExpedientClient> expedientClientCollectionOld = persistentClient.getExpedientClientCollection();
            Collection<ExpedientClient> expedientClientCollectionNew = client.getExpedientClientCollection();
            Collection<Image> imageCollectionOld = persistentClient.getImageCollection();
            Collection<Image> imageCollectionNew = client.getImageCollection();
            Collection<ClientData> clientDataCollectionOld = persistentClient.getClientDataCollection();
            Collection<ClientData> clientDataCollectionNew = client.getClientDataCollection();
            List<String> illegalOrphanMessages = null;
            for (ExpedientClient expedientClientCollectionOldExpedientClient : expedientClientCollectionOld) {
                if (!expedientClientCollectionNew.contains(expedientClientCollectionOldExpedientClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExpedientClient " + expedientClientCollectionOldExpedientClient + " since its client field is not nullable.");
                }
            }
            for (Image imageCollectionOldImage : imageCollectionOld) {
                if (!imageCollectionNew.contains(imageCollectionOldImage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Image " + imageCollectionOldImage + " since its fkClient field is not nullable.");
                }
            }
            for (ClientData clientDataCollectionOldClientData : clientDataCollectionOld) {
                if (!clientDataCollectionNew.contains(clientDataCollectionOldClientData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientData " + clientDataCollectionOldClientData + " since its client field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ExpedientClient> attachedExpedientClientCollectionNew = new ArrayList<ExpedientClient>();
            for (ExpedientClient expedientClientCollectionNewExpedientClientToAttach : expedientClientCollectionNew) {
                expedientClientCollectionNewExpedientClientToAttach = em.getReference(expedientClientCollectionNewExpedientClientToAttach.getClass(), expedientClientCollectionNewExpedientClientToAttach.getExpedientClientPK());
                attachedExpedientClientCollectionNew.add(expedientClientCollectionNewExpedientClientToAttach);
            }
            expedientClientCollectionNew = attachedExpedientClientCollectionNew;
            client.setExpedientClientCollection(expedientClientCollectionNew);
            Collection<Image> attachedImageCollectionNew = new ArrayList<Image>();
            for (Image imageCollectionNewImageToAttach : imageCollectionNew) {
                imageCollectionNewImageToAttach = em.getReference(imageCollectionNewImageToAttach.getClass(), imageCollectionNewImageToAttach.getIdImage());
                attachedImageCollectionNew.add(imageCollectionNewImageToAttach);
            }
            imageCollectionNew = attachedImageCollectionNew;
            client.setImageCollection(imageCollectionNew);
            Collection<ClientData> attachedClientDataCollectionNew = new ArrayList<ClientData>();
            for (ClientData clientDataCollectionNewClientDataToAttach : clientDataCollectionNew) {
                clientDataCollectionNewClientDataToAttach = em.getReference(clientDataCollectionNewClientDataToAttach.getClass(), clientDataCollectionNewClientDataToAttach.getClientDataPK());
                attachedClientDataCollectionNew.add(clientDataCollectionNewClientDataToAttach);
            }
            clientDataCollectionNew = attachedClientDataCollectionNew;
            client.setClientDataCollection(clientDataCollectionNew);
            client = em.merge(client);
            for (ExpedientClient expedientClientCollectionNewExpedientClient : expedientClientCollectionNew) {
                if (!expedientClientCollectionOld.contains(expedientClientCollectionNewExpedientClient)) {
                    Client oldClientOfExpedientClientCollectionNewExpedientClient = expedientClientCollectionNewExpedientClient.getClient();
                    expedientClientCollectionNewExpedientClient.setClient(client);
                    expedientClientCollectionNewExpedientClient = em.merge(expedientClientCollectionNewExpedientClient);
                    if (oldClientOfExpedientClientCollectionNewExpedientClient != null && !oldClientOfExpedientClientCollectionNewExpedientClient.equals(client)) {
                        oldClientOfExpedientClientCollectionNewExpedientClient.getExpedientClientCollection().remove(expedientClientCollectionNewExpedientClient);
                        oldClientOfExpedientClientCollectionNewExpedientClient = em.merge(oldClientOfExpedientClientCollectionNewExpedientClient);
                    }
                }
            }
            for (Image imageCollectionNewImage : imageCollectionNew) {
                if (!imageCollectionOld.contains(imageCollectionNewImage)) {
                    Client oldFkClientOfImageCollectionNewImage = imageCollectionNewImage.getFkClient();
                    imageCollectionNewImage.setFkClient(client);
                    imageCollectionNewImage = em.merge(imageCollectionNewImage);
                    if (oldFkClientOfImageCollectionNewImage != null && !oldFkClientOfImageCollectionNewImage.equals(client)) {
                        oldFkClientOfImageCollectionNewImage.getImageCollection().remove(imageCollectionNewImage);
                        oldFkClientOfImageCollectionNewImage = em.merge(oldFkClientOfImageCollectionNewImage);
                    }
                }
            }
            for (ClientData clientDataCollectionNewClientData : clientDataCollectionNew) {
                if (!clientDataCollectionOld.contains(clientDataCollectionNewClientData)) {
                    Client oldClientOfClientDataCollectionNewClientData = clientDataCollectionNewClientData.getClient();
                    clientDataCollectionNewClientData.setClient(client);
                    clientDataCollectionNewClientData = em.merge(clientDataCollectionNewClientData);
                    if (oldClientOfClientDataCollectionNewClientData != null && !oldClientOfClientDataCollectionNewClientData.equals(client)) {
                        oldClientOfClientDataCollectionNewClientData.getClientDataCollection().remove(clientDataCollectionNewClientData);
                        oldClientOfClientDataCollectionNewClientData = em.merge(oldClientOfClientDataCollectionNewClientData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getIdUser();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ExpedientClient> expedientClientCollectionOrphanCheck = client.getExpedientClientCollection();
            for (ExpedientClient expedientClientCollectionOrphanCheckExpedientClient : expedientClientCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the ExpedientClient " + expedientClientCollectionOrphanCheckExpedientClient + " in its expedientClientCollection field has a non-nullable client field.");
            }
            Collection<Image> imageCollectionOrphanCheck = client.getImageCollection();
            for (Image imageCollectionOrphanCheckImage : imageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Image " + imageCollectionOrphanCheckImage + " in its imageCollection field has a non-nullable fkClient field.");
            }
            Collection<ClientData> clientDataCollectionOrphanCheck = client.getClientDataCollection();
            for (ClientData clientDataCollectionOrphanCheckClientData : clientDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the ClientData " + clientDataCollectionOrphanCheckClientData + " in its clientDataCollection field has a non-nullable client field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
