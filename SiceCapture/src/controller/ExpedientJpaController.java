/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Document;
import entities.Expedient;
import java.util.ArrayList;
import java.util.Collection;
import entities.ExpedientClient;
import entities.Image;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ExpedientJpaController implements Serializable {

    public ExpedientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Expedient expedient) {
        if (expedient.getDocumentCollection() == null) {
            expedient.setDocumentCollection(new ArrayList<Document>());
        }
        if (expedient.getExpedientClientCollection() == null) {
            expedient.setExpedientClientCollection(new ArrayList<ExpedientClient>());
        }
        if (expedient.getImageCollection() == null) {
            expedient.setImageCollection(new ArrayList<Image>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Document> attachedDocumentCollection = new ArrayList<Document>();
            for (Document documentCollectionDocumentToAttach : expedient.getDocumentCollection()) {
                documentCollectionDocumentToAttach = em.getReference(documentCollectionDocumentToAttach.getClass(), documentCollectionDocumentToAttach.getIdDocument());
                attachedDocumentCollection.add(documentCollectionDocumentToAttach);
            }
            expedient.setDocumentCollection(attachedDocumentCollection);
            Collection<ExpedientClient> attachedExpedientClientCollection = new ArrayList<ExpedientClient>();
            for (ExpedientClient expedientClientCollectionExpedientClientToAttach : expedient.getExpedientClientCollection()) {
                expedientClientCollectionExpedientClientToAttach = em.getReference(expedientClientCollectionExpedientClientToAttach.getClass(), expedientClientCollectionExpedientClientToAttach.getExpedientClientPK());
                attachedExpedientClientCollection.add(expedientClientCollectionExpedientClientToAttach);
            }
            expedient.setExpedientClientCollection(attachedExpedientClientCollection);
            Collection<Image> attachedImageCollection = new ArrayList<Image>();
            for (Image imageCollectionImageToAttach : expedient.getImageCollection()) {
                imageCollectionImageToAttach = em.getReference(imageCollectionImageToAttach.getClass(), imageCollectionImageToAttach.getIdImage());
                attachedImageCollection.add(imageCollectionImageToAttach);
            }
            expedient.setImageCollection(attachedImageCollection);
            em.persist(expedient);
            for (Document documentCollectionDocument : expedient.getDocumentCollection()) {
                documentCollectionDocument.getExpedientCollection().add(expedient);
                documentCollectionDocument = em.merge(documentCollectionDocument);
            }
            for (ExpedientClient expedientClientCollectionExpedientClient : expedient.getExpedientClientCollection()) {
                Expedient oldExpedientOfExpedientClientCollectionExpedientClient = expedientClientCollectionExpedientClient.getExpedient();
                expedientClientCollectionExpedientClient.setExpedient(expedient);
                expedientClientCollectionExpedientClient = em.merge(expedientClientCollectionExpedientClient);
                if (oldExpedientOfExpedientClientCollectionExpedientClient != null) {
                    oldExpedientOfExpedientClientCollectionExpedientClient.getExpedientClientCollection().remove(expedientClientCollectionExpedientClient);
                    oldExpedientOfExpedientClientCollectionExpedientClient = em.merge(oldExpedientOfExpedientClientCollectionExpedientClient);
                }
            }
            for (Image imageCollectionImage : expedient.getImageCollection()) {
                Expedient oldFkExpedientOfImageCollectionImage = imageCollectionImage.getFkExpedient();
                imageCollectionImage.setFkExpedient(expedient);
                imageCollectionImage = em.merge(imageCollectionImage);
                if (oldFkExpedientOfImageCollectionImage != null) {
                    oldFkExpedientOfImageCollectionImage.getImageCollection().remove(imageCollectionImage);
                    oldFkExpedientOfImageCollectionImage = em.merge(oldFkExpedientOfImageCollectionImage);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Expedient expedient) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expedient persistentExpedient = em.find(Expedient.class, expedient.getIdExpedient());
            Collection<Document> documentCollectionOld = persistentExpedient.getDocumentCollection();
            Collection<Document> documentCollectionNew = expedient.getDocumentCollection();
            Collection<ExpedientClient> expedientClientCollectionOld = persistentExpedient.getExpedientClientCollection();
            Collection<ExpedientClient> expedientClientCollectionNew = expedient.getExpedientClientCollection();
            Collection<Image> imageCollectionOld = persistentExpedient.getImageCollection();
            Collection<Image> imageCollectionNew = expedient.getImageCollection();
            List<String> illegalOrphanMessages = null;
            for (ExpedientClient expedientClientCollectionOldExpedientClient : expedientClientCollectionOld) {
                if (!expedientClientCollectionNew.contains(expedientClientCollectionOldExpedientClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExpedientClient " + expedientClientCollectionOldExpedientClient + " since its expedient field is not nullable.");
                }
            }
            for (Image imageCollectionOldImage : imageCollectionOld) {
                if (!imageCollectionNew.contains(imageCollectionOldImage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Image " + imageCollectionOldImage + " since its fkExpedient field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Document> attachedDocumentCollectionNew = new ArrayList<Document>();
            for (Document documentCollectionNewDocumentToAttach : documentCollectionNew) {
                documentCollectionNewDocumentToAttach = em.getReference(documentCollectionNewDocumentToAttach.getClass(), documentCollectionNewDocumentToAttach.getIdDocument());
                attachedDocumentCollectionNew.add(documentCollectionNewDocumentToAttach);
            }
            documentCollectionNew = attachedDocumentCollectionNew;
            expedient.setDocumentCollection(documentCollectionNew);
            Collection<ExpedientClient> attachedExpedientClientCollectionNew = new ArrayList<ExpedientClient>();
            for (ExpedientClient expedientClientCollectionNewExpedientClientToAttach : expedientClientCollectionNew) {
                expedientClientCollectionNewExpedientClientToAttach = em.getReference(expedientClientCollectionNewExpedientClientToAttach.getClass(), expedientClientCollectionNewExpedientClientToAttach.getExpedientClientPK());
                attachedExpedientClientCollectionNew.add(expedientClientCollectionNewExpedientClientToAttach);
            }
            expedientClientCollectionNew = attachedExpedientClientCollectionNew;
            expedient.setExpedientClientCollection(expedientClientCollectionNew);
            Collection<Image> attachedImageCollectionNew = new ArrayList<Image>();
            for (Image imageCollectionNewImageToAttach : imageCollectionNew) {
                imageCollectionNewImageToAttach = em.getReference(imageCollectionNewImageToAttach.getClass(), imageCollectionNewImageToAttach.getIdImage());
                attachedImageCollectionNew.add(imageCollectionNewImageToAttach);
            }
            imageCollectionNew = attachedImageCollectionNew;
            expedient.setImageCollection(imageCollectionNew);
            expedient = em.merge(expedient);
            for (Document documentCollectionOldDocument : documentCollectionOld) {
                if (!documentCollectionNew.contains(documentCollectionOldDocument)) {
                    documentCollectionOldDocument.getExpedientCollection().remove(expedient);
                    documentCollectionOldDocument = em.merge(documentCollectionOldDocument);
                }
            }
            for (Document documentCollectionNewDocument : documentCollectionNew) {
                if (!documentCollectionOld.contains(documentCollectionNewDocument)) {
                    documentCollectionNewDocument.getExpedientCollection().add(expedient);
                    documentCollectionNewDocument = em.merge(documentCollectionNewDocument);
                }
            }
            for (ExpedientClient expedientClientCollectionNewExpedientClient : expedientClientCollectionNew) {
                if (!expedientClientCollectionOld.contains(expedientClientCollectionNewExpedientClient)) {
                    Expedient oldExpedientOfExpedientClientCollectionNewExpedientClient = expedientClientCollectionNewExpedientClient.getExpedient();
                    expedientClientCollectionNewExpedientClient.setExpedient(expedient);
                    expedientClientCollectionNewExpedientClient = em.merge(expedientClientCollectionNewExpedientClient);
                    if (oldExpedientOfExpedientClientCollectionNewExpedientClient != null && !oldExpedientOfExpedientClientCollectionNewExpedientClient.equals(expedient)) {
                        oldExpedientOfExpedientClientCollectionNewExpedientClient.getExpedientClientCollection().remove(expedientClientCollectionNewExpedientClient);
                        oldExpedientOfExpedientClientCollectionNewExpedientClient = em.merge(oldExpedientOfExpedientClientCollectionNewExpedientClient);
                    }
                }
            }
            for (Image imageCollectionNewImage : imageCollectionNew) {
                if (!imageCollectionOld.contains(imageCollectionNewImage)) {
                    Expedient oldFkExpedientOfImageCollectionNewImage = imageCollectionNewImage.getFkExpedient();
                    imageCollectionNewImage.setFkExpedient(expedient);
                    imageCollectionNewImage = em.merge(imageCollectionNewImage);
                    if (oldFkExpedientOfImageCollectionNewImage != null && !oldFkExpedientOfImageCollectionNewImage.equals(expedient)) {
                        oldFkExpedientOfImageCollectionNewImage.getImageCollection().remove(imageCollectionNewImage);
                        oldFkExpedientOfImageCollectionNewImage = em.merge(oldFkExpedientOfImageCollectionNewImage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = expedient.getIdExpedient();
                if (findExpedient(id) == null) {
                    throw new NonexistentEntityException("The expedient with id " + id + " no longer exists.");
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
            Expedient expedient;
            try {
                expedient = em.getReference(Expedient.class, id);
                expedient.getIdExpedient();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The expedient with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ExpedientClient> expedientClientCollectionOrphanCheck = expedient.getExpedientClientCollection();
            for (ExpedientClient expedientClientCollectionOrphanCheckExpedientClient : expedientClientCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expedient (" + expedient + ") cannot be destroyed since the ExpedientClient " + expedientClientCollectionOrphanCheckExpedientClient + " in its expedientClientCollection field has a non-nullable expedient field.");
            }
            Collection<Image> imageCollectionOrphanCheck = expedient.getImageCollection();
            for (Image imageCollectionOrphanCheckImage : imageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Expedient (" + expedient + ") cannot be destroyed since the Image " + imageCollectionOrphanCheckImage + " in its imageCollection field has a non-nullable fkExpedient field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Document> documentCollection = expedient.getDocumentCollection();
            for (Document documentCollectionDocument : documentCollection) {
                documentCollectionDocument.getExpedientCollection().remove(expedient);
                documentCollectionDocument = em.merge(documentCollectionDocument);
            }
            em.remove(expedient);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Expedient> findExpedientEntities() {
        return findExpedientEntities(true, -1, -1);
    }

    public List<Expedient> findExpedientEntities(int maxResults, int firstResult) {
        return findExpedientEntities(false, maxResults, firstResult);
    }

    private List<Expedient> findExpedientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Expedient.class));
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

    public Expedient findExpedient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Expedient.class, id);
        } finally {
            em.close();
        }
    }

    public int getExpedientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Expedient> rt = cq.from(Expedient.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Expedient findByName(String name) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Expedient.findByName", Expedient.class);
        query.setParameter("name", name);
        Expedient expedient = (Expedient) query.getSingleResult();
        return expedient;
    }

    public List<Expedient> finAllExpedients() {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Expedient.findAll", Expedient.class);
        List<Expedient> expedient = (List<Expedient>) query.getResultList();
        return expedient;
    }

}
