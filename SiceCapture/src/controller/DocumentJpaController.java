/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import entities.Document;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Expedient;
import java.util.ArrayList;
import java.util.Collection;
import entities.DocumentParameter;
import entities.DocumentData;
import entities.Image;
import gui.MainCustomGui;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class DocumentJpaController implements Serializable {

    public DocumentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Document document) {
        if (document.getExpedientCollection() == null) {
            document.setExpedientCollection(new ArrayList<Expedient>());
        }
        if (document.getDocumentParameterCollection() == null) {
            document.setDocumentParameterCollection(new ArrayList<DocumentParameter>());
        }
        if (document.getDocumentDataCollection() == null) {
            document.setDocumentDataCollection(new ArrayList<DocumentData>());
        }
        if (document.getImageCollection() == null) {
            document.setImageCollection(new ArrayList<Image>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Expedient> attachedExpedientCollection = new ArrayList<Expedient>();
            for (Expedient expedientCollectionExpedientToAttach : document.getExpedientCollection()) {
                expedientCollectionExpedientToAttach = em.getReference(expedientCollectionExpedientToAttach.getClass(), expedientCollectionExpedientToAttach.getIdExpedient());
                attachedExpedientCollection.add(expedientCollectionExpedientToAttach);
            }
            document.setExpedientCollection(attachedExpedientCollection);
            Collection<DocumentParameter> attachedDocumentParameterCollection = new ArrayList<DocumentParameter>();
            for (DocumentParameter documentParameterCollectionDocumentParameterToAttach : document.getDocumentParameterCollection()) {
                documentParameterCollectionDocumentParameterToAttach = em.getReference(documentParameterCollectionDocumentParameterToAttach.getClass(), documentParameterCollectionDocumentParameterToAttach.getIdParameter());
                attachedDocumentParameterCollection.add(documentParameterCollectionDocumentParameterToAttach);
            }
            document.setDocumentParameterCollection(attachedDocumentParameterCollection);
            Collection<DocumentData> attachedDocumentDataCollection = new ArrayList<DocumentData>();
            for (DocumentData documentDataCollectionDocumentDataToAttach : document.getDocumentDataCollection()) {
                documentDataCollectionDocumentDataToAttach = em.getReference(documentDataCollectionDocumentDataToAttach.getClass(), documentDataCollectionDocumentDataToAttach.getIdDocumentdata());
                attachedDocumentDataCollection.add(documentDataCollectionDocumentDataToAttach);
            }
            document.setDocumentDataCollection(attachedDocumentDataCollection);
            Collection<Image> attachedImageCollection = new ArrayList<Image>();
            for (Image imageCollectionImageToAttach : document.getImageCollection()) {
                imageCollectionImageToAttach = em.getReference(imageCollectionImageToAttach.getClass(), imageCollectionImageToAttach.getImagePK());
                attachedImageCollection.add(imageCollectionImageToAttach);
            }
            document.setImageCollection(attachedImageCollection);
            em.persist(document);
            for (Expedient expedientCollectionExpedient : document.getExpedientCollection()) {
                expedientCollectionExpedient.getDocumentCollection().add(document);
                expedientCollectionExpedient = em.merge(expedientCollectionExpedient);
            }
            for (DocumentParameter documentParameterCollectionDocumentParameter : document.getDocumentParameterCollection()) {
                Document oldFkDocumentOfDocumentParameterCollectionDocumentParameter = documentParameterCollectionDocumentParameter.getFkDocument();
                documentParameterCollectionDocumentParameter.setFkDocument(document);
                documentParameterCollectionDocumentParameter = em.merge(documentParameterCollectionDocumentParameter);
                if (oldFkDocumentOfDocumentParameterCollectionDocumentParameter != null) {
                    oldFkDocumentOfDocumentParameterCollectionDocumentParameter.getDocumentParameterCollection().remove(documentParameterCollectionDocumentParameter);
                    oldFkDocumentOfDocumentParameterCollectionDocumentParameter = em.merge(oldFkDocumentOfDocumentParameterCollectionDocumentParameter);
                }
            }
            for (DocumentData documentDataCollectionDocumentData : document.getDocumentDataCollection()) {
                Document oldFkDocumentOfDocumentDataCollectionDocumentData = documentDataCollectionDocumentData.getFkDocument();
                documentDataCollectionDocumentData.setFkDocument(document);
                documentDataCollectionDocumentData = em.merge(documentDataCollectionDocumentData);
                if (oldFkDocumentOfDocumentDataCollectionDocumentData != null) {
                    oldFkDocumentOfDocumentDataCollectionDocumentData.getDocumentDataCollection().remove(documentDataCollectionDocumentData);
                    oldFkDocumentOfDocumentDataCollectionDocumentData = em.merge(oldFkDocumentOfDocumentDataCollectionDocumentData);
                }
            }
            for (Image imageCollectionImage : document.getImageCollection()) {
                Document oldDocumentOfImageCollectionImage = imageCollectionImage.getDocument();
                imageCollectionImage.setDocument(document);
                imageCollectionImage = em.merge(imageCollectionImage);
                if (oldDocumentOfImageCollectionImage != null) {
                    oldDocumentOfImageCollectionImage.getImageCollection().remove(imageCollectionImage);
                    oldDocumentOfImageCollectionImage = em.merge(oldDocumentOfImageCollectionImage);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Document document) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Document persistentDocument = em.find(Document.class, document.getIdDocument());
            Collection<Expedient> expedientCollectionOld = persistentDocument.getExpedientCollection();
            Collection<Expedient> expedientCollectionNew = document.getExpedientCollection();
            Collection<DocumentParameter> documentParameterCollectionOld = persistentDocument.getDocumentParameterCollection();
            Collection<DocumentParameter> documentParameterCollectionNew = document.getDocumentParameterCollection();
            Collection<DocumentData> documentDataCollectionOld = persistentDocument.getDocumentDataCollection();
            Collection<DocumentData> documentDataCollectionNew = document.getDocumentDataCollection();
            Collection<Image> imageCollectionOld = persistentDocument.getImageCollection();
            Collection<Image> imageCollectionNew = document.getImageCollection();
            List<String> illegalOrphanMessages = null;
            for (DocumentParameter documentParameterCollectionOldDocumentParameter : documentParameterCollectionOld) {
                if (!documentParameterCollectionNew.contains(documentParameterCollectionOldDocumentParameter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentParameter " + documentParameterCollectionOldDocumentParameter + " since its fkDocument field is not nullable.");
                }
            }
            for (DocumentData documentDataCollectionOldDocumentData : documentDataCollectionOld) {
                if (!documentDataCollectionNew.contains(documentDataCollectionOldDocumentData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentData " + documentDataCollectionOldDocumentData + " since its fkDocument field is not nullable.");
                }
            }
            for (Image imageCollectionOldImage : imageCollectionOld) {
                if (!imageCollectionNew.contains(imageCollectionOldImage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Image " + imageCollectionOldImage + " since its document field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Expedient> attachedExpedientCollectionNew = new ArrayList<Expedient>();
            for (Expedient expedientCollectionNewExpedientToAttach : expedientCollectionNew) {
                expedientCollectionNewExpedientToAttach = em.getReference(expedientCollectionNewExpedientToAttach.getClass(), expedientCollectionNewExpedientToAttach.getIdExpedient());
                attachedExpedientCollectionNew.add(expedientCollectionNewExpedientToAttach);
            }
            expedientCollectionNew = attachedExpedientCollectionNew;
            document.setExpedientCollection(expedientCollectionNew);
            Collection<DocumentParameter> attachedDocumentParameterCollectionNew = new ArrayList<DocumentParameter>();
            for (DocumentParameter documentParameterCollectionNewDocumentParameterToAttach : documentParameterCollectionNew) {
                documentParameterCollectionNewDocumentParameterToAttach = em.getReference(documentParameterCollectionNewDocumentParameterToAttach.getClass(), documentParameterCollectionNewDocumentParameterToAttach.getIdParameter());
                attachedDocumentParameterCollectionNew.add(documentParameterCollectionNewDocumentParameterToAttach);
            }
            documentParameterCollectionNew = attachedDocumentParameterCollectionNew;
            document.setDocumentParameterCollection(documentParameterCollectionNew);
            Collection<DocumentData> attachedDocumentDataCollectionNew = new ArrayList<DocumentData>();
            for (DocumentData documentDataCollectionNewDocumentDataToAttach : documentDataCollectionNew) {
                documentDataCollectionNewDocumentDataToAttach = em.getReference(documentDataCollectionNewDocumentDataToAttach.getClass(), documentDataCollectionNewDocumentDataToAttach.getIdDocumentdata());
                attachedDocumentDataCollectionNew.add(documentDataCollectionNewDocumentDataToAttach);
            }
            documentDataCollectionNew = attachedDocumentDataCollectionNew;
            document.setDocumentDataCollection(documentDataCollectionNew);
            Collection<Image> attachedImageCollectionNew = new ArrayList<Image>();
            for (Image imageCollectionNewImageToAttach : imageCollectionNew) {
                imageCollectionNewImageToAttach = em.getReference(imageCollectionNewImageToAttach.getClass(), imageCollectionNewImageToAttach.getImagePK());
                attachedImageCollectionNew.add(imageCollectionNewImageToAttach);
            }
            imageCollectionNew = attachedImageCollectionNew;
            document.setImageCollection(imageCollectionNew);
            document = em.merge(document);
            for (Expedient expedientCollectionOldExpedient : expedientCollectionOld) {
                if (!expedientCollectionNew.contains(expedientCollectionOldExpedient)) {
                    expedientCollectionOldExpedient.getDocumentCollection().remove(document);
                    expedientCollectionOldExpedient = em.merge(expedientCollectionOldExpedient);
                }
            }
            for (Expedient expedientCollectionNewExpedient : expedientCollectionNew) {
                if (!expedientCollectionOld.contains(expedientCollectionNewExpedient)) {
                    expedientCollectionNewExpedient.getDocumentCollection().add(document);
                    expedientCollectionNewExpedient = em.merge(expedientCollectionNewExpedient);
                }
            }
            for (DocumentParameter documentParameterCollectionNewDocumentParameter : documentParameterCollectionNew) {
                if (!documentParameterCollectionOld.contains(documentParameterCollectionNewDocumentParameter)) {
                    Document oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter = documentParameterCollectionNewDocumentParameter.getFkDocument();
                    documentParameterCollectionNewDocumentParameter.setFkDocument(document);
                    documentParameterCollectionNewDocumentParameter = em.merge(documentParameterCollectionNewDocumentParameter);
                    if (oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter != null && !oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter.equals(document)) {
                        oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter.getDocumentParameterCollection().remove(documentParameterCollectionNewDocumentParameter);
                        oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter = em.merge(oldFkDocumentOfDocumentParameterCollectionNewDocumentParameter);
                    }
                }
            }
            for (DocumentData documentDataCollectionNewDocumentData : documentDataCollectionNew) {
                if (!documentDataCollectionOld.contains(documentDataCollectionNewDocumentData)) {
                    Document oldFkDocumentOfDocumentDataCollectionNewDocumentData = documentDataCollectionNewDocumentData.getFkDocument();
                    documentDataCollectionNewDocumentData.setFkDocument(document);
                    documentDataCollectionNewDocumentData = em.merge(documentDataCollectionNewDocumentData);
                    if (oldFkDocumentOfDocumentDataCollectionNewDocumentData != null && !oldFkDocumentOfDocumentDataCollectionNewDocumentData.equals(document)) {
                        oldFkDocumentOfDocumentDataCollectionNewDocumentData.getDocumentDataCollection().remove(documentDataCollectionNewDocumentData);
                        oldFkDocumentOfDocumentDataCollectionNewDocumentData = em.merge(oldFkDocumentOfDocumentDataCollectionNewDocumentData);
                    }
                }
            }
            for (Image imageCollectionNewImage : imageCollectionNew) {
                if (!imageCollectionOld.contains(imageCollectionNewImage)) {
                    Document oldDocumentOfImageCollectionNewImage = imageCollectionNewImage.getDocument();
                    imageCollectionNewImage.setDocument(document);
                    imageCollectionNewImage = em.merge(imageCollectionNewImage);
                    if (oldDocumentOfImageCollectionNewImage != null && !oldDocumentOfImageCollectionNewImage.equals(document)) {
                        oldDocumentOfImageCollectionNewImage.getImageCollection().remove(imageCollectionNewImage);
                        oldDocumentOfImageCollectionNewImage = em.merge(oldDocumentOfImageCollectionNewImage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = document.getIdDocument();
                if (findDocument(id) == null) {
                    throw new NonexistentEntityException("The document with id " + id + " no longer exists.");
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
            Document document;
            try {
                document = em.getReference(Document.class, id);
                document.getIdDocument();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The document with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DocumentParameter> documentParameterCollectionOrphanCheck = document.getDocumentParameterCollection();
            for (DocumentParameter documentParameterCollectionOrphanCheckDocumentParameter : documentParameterCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Document (" + document + ") cannot be destroyed since the DocumentParameter " + documentParameterCollectionOrphanCheckDocumentParameter + " in its documentParameterCollection field has a non-nullable fkDocument field.");
            }
            Collection<DocumentData> documentDataCollectionOrphanCheck = document.getDocumentDataCollection();
            for (DocumentData documentDataCollectionOrphanCheckDocumentData : documentDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Document (" + document + ") cannot be destroyed since the DocumentData " + documentDataCollectionOrphanCheckDocumentData + " in its documentDataCollection field has a non-nullable fkDocument field.");
            }
            Collection<Image> imageCollectionOrphanCheck = document.getImageCollection();
            for (Image imageCollectionOrphanCheckImage : imageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Document (" + document + ") cannot be destroyed since the Image " + imageCollectionOrphanCheckImage + " in its imageCollection field has a non-nullable document field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Expedient> expedientCollection = document.getExpedientCollection();
            for (Expedient expedientCollectionExpedient : expedientCollection) {
                expedientCollectionExpedient.getDocumentCollection().remove(document);
                expedientCollectionExpedient = em.merge(expedientCollectionExpedient);
            }
            em.remove(document);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Document> findDocumentEntities() {
        return findDocumentEntities(true, -1, -1);
    }

    public List<Document> findDocumentEntities(int maxResults, int firstResult) {
        return findDocumentEntities(false, maxResults, firstResult);
    }

    private List<Document> findDocumentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Document.class));
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

    public Document findByName(String name) {
        Document document = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createNamedQuery("Document.findByName", Document.class);
            query.setParameter("name", name);
            document = (Document) query.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(DocumentJpaController.class.getName()).log(Level.SEVERE, null, e);
        }
        return document;
    }

    public Document findDocument(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Document.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Document> rt = cq.from(Document.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
