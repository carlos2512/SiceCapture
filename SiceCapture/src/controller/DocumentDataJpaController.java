/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Document;
import entities.DataType;
import entities.ClientData;
import entities.DocumentData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class DocumentDataJpaController implements Serializable {

    public DocumentDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<DocumentData> findByDocument(Document fkDocument) {
        EntityManager em = getEntityManager();
         Query query = em.createNamedQuery("DocumentData.findByDocument", DocumentData.class);
         query.setParameter("fkDocument", fkDocument);
         List<DocumentData> documentDataList =  (List<DocumentData>) query.getResultList();
         return documentDataList;
    }

    public void create(DocumentData documentData) throws PreexistingEntityException, Exception {
        if (documentData.getClientDataCollection() == null) {
            documentData.setClientDataCollection(new ArrayList<ClientData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Document fkDocument = documentData.getFkDocument();
            if (fkDocument != null) {
                fkDocument = em.getReference(fkDocument.getClass(), fkDocument.getIdDocument());
                documentData.setFkDocument(fkDocument);
            }
            DataType fkDataType = documentData.getFkDataType();
            if (fkDataType != null) {
                fkDataType = em.getReference(fkDataType.getClass(), fkDataType.getIdDatatype());
                documentData.setFkDataType(fkDataType);
            }
            Collection<ClientData> attachedClientDataCollection = new ArrayList<ClientData>();
            for (ClientData clientDataCollectionClientDataToAttach : documentData.getClientDataCollection()) {
                clientDataCollectionClientDataToAttach = em.getReference(clientDataCollectionClientDataToAttach.getClass(), clientDataCollectionClientDataToAttach.getClientDataPK());
                attachedClientDataCollection.add(clientDataCollectionClientDataToAttach);
            }
            documentData.setClientDataCollection(attachedClientDataCollection);
            em.persist(documentData);
            if (fkDocument != null) {
                fkDocument.getDocumentDataCollection().add(documentData);
                fkDocument = em.merge(fkDocument);
            }
            if (fkDataType != null) {
                fkDataType.getDocumentDataCollection().add(documentData);
                fkDataType = em.merge(fkDataType);
            }
            for (ClientData clientDataCollectionClientData : documentData.getClientDataCollection()) {
                DocumentData oldDocumentDataOfClientDataCollectionClientData = clientDataCollectionClientData.getDocumentData();
                clientDataCollectionClientData.setDocumentData(documentData);
                clientDataCollectionClientData = em.merge(clientDataCollectionClientData);
                if (oldDocumentDataOfClientDataCollectionClientData != null) {
                    oldDocumentDataOfClientDataCollectionClientData.getClientDataCollection().remove(clientDataCollectionClientData);
                    oldDocumentDataOfClientDataCollectionClientData = em.merge(oldDocumentDataOfClientDataCollectionClientData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocumentData(documentData.getIdDocumentdata()) != null) {
                throw new PreexistingEntityException("DocumentData " + documentData + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocumentData documentData) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentData persistentDocumentData = em.find(DocumentData.class, documentData.getIdDocumentdata());
            Document fkDocumentOld = persistentDocumentData.getFkDocument();
            Document fkDocumentNew = documentData.getFkDocument();
            DataType fkDataTypeOld = persistentDocumentData.getFkDataType();
            DataType fkDataTypeNew = documentData.getFkDataType();
            Collection<ClientData> clientDataCollectionOld = persistentDocumentData.getClientDataCollection();
            Collection<ClientData> clientDataCollectionNew = documentData.getClientDataCollection();
            List<String> illegalOrphanMessages = null;
            for (ClientData clientDataCollectionOldClientData : clientDataCollectionOld) {
                if (!clientDataCollectionNew.contains(clientDataCollectionOldClientData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientData " + clientDataCollectionOldClientData + " since its documentData field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (fkDocumentNew != null) {
                fkDocumentNew = em.getReference(fkDocumentNew.getClass(), fkDocumentNew.getIdDocument());
                documentData.setFkDocument(fkDocumentNew);
            }
            if (fkDataTypeNew != null) {
                fkDataTypeNew = em.getReference(fkDataTypeNew.getClass(), fkDataTypeNew.getIdDatatype());
                documentData.setFkDataType(fkDataTypeNew);
            }
            Collection<ClientData> attachedClientDataCollectionNew = new ArrayList<ClientData>();
            for (ClientData clientDataCollectionNewClientDataToAttach : clientDataCollectionNew) {
                clientDataCollectionNewClientDataToAttach = em.getReference(clientDataCollectionNewClientDataToAttach.getClass(), clientDataCollectionNewClientDataToAttach.getClientDataPK());
                attachedClientDataCollectionNew.add(clientDataCollectionNewClientDataToAttach);
            }
            clientDataCollectionNew = attachedClientDataCollectionNew;
            documentData.setClientDataCollection(clientDataCollectionNew);
            documentData = em.merge(documentData);
            if (fkDocumentOld != null && !fkDocumentOld.equals(fkDocumentNew)) {
                fkDocumentOld.getDocumentDataCollection().remove(documentData);
                fkDocumentOld = em.merge(fkDocumentOld);
            }
            if (fkDocumentNew != null && !fkDocumentNew.equals(fkDocumentOld)) {
                fkDocumentNew.getDocumentDataCollection().add(documentData);
                fkDocumentNew = em.merge(fkDocumentNew);
            }
            if (fkDataTypeOld != null && !fkDataTypeOld.equals(fkDataTypeNew)) {
                fkDataTypeOld.getDocumentDataCollection().remove(documentData);
                fkDataTypeOld = em.merge(fkDataTypeOld);
            }
            if (fkDataTypeNew != null && !fkDataTypeNew.equals(fkDataTypeOld)) {
                fkDataTypeNew.getDocumentDataCollection().add(documentData);
                fkDataTypeNew = em.merge(fkDataTypeNew);
            }
            for (ClientData clientDataCollectionNewClientData : clientDataCollectionNew) {
                if (!clientDataCollectionOld.contains(clientDataCollectionNewClientData)) {
                    DocumentData oldDocumentDataOfClientDataCollectionNewClientData = clientDataCollectionNewClientData.getDocumentData();
                    clientDataCollectionNewClientData.setDocumentData(documentData);
                    clientDataCollectionNewClientData = em.merge(clientDataCollectionNewClientData);
                    if (oldDocumentDataOfClientDataCollectionNewClientData != null && !oldDocumentDataOfClientDataCollectionNewClientData.equals(documentData)) {
                        oldDocumentDataOfClientDataCollectionNewClientData.getClientDataCollection().remove(clientDataCollectionNewClientData);
                        oldDocumentDataOfClientDataCollectionNewClientData = em.merge(oldDocumentDataOfClientDataCollectionNewClientData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentData.getIdDocumentdata();
                if (findDocumentData(id) == null) {
                    throw new NonexistentEntityException("The documentData with id " + id + " no longer exists.");
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
            DocumentData documentData;
            try {
                documentData = em.getReference(DocumentData.class, id);
                documentData.getIdDocumentdata();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentData with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ClientData> clientDataCollectionOrphanCheck = documentData.getClientDataCollection();
            for (ClientData clientDataCollectionOrphanCheckClientData : clientDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocumentData (" + documentData + ") cannot be destroyed since the ClientData " + clientDataCollectionOrphanCheckClientData + " in its clientDataCollection field has a non-nullable documentData field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Document fkDocument = documentData.getFkDocument();
            if (fkDocument != null) {
                fkDocument.getDocumentDataCollection().remove(documentData);
                fkDocument = em.merge(fkDocument);
            }
            DataType fkDataType = documentData.getFkDataType();
            if (fkDataType != null) {
                fkDataType.getDocumentDataCollection().remove(documentData);
                fkDataType = em.merge(fkDataType);
            }
            em.remove(documentData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentData> findDocumentDataEntities() {
        return findDocumentDataEntities(true, -1, -1);
    }

    public List<DocumentData> findDocumentDataEntities(int maxResults, int firstResult) {
        return findDocumentDataEntities(false, maxResults, firstResult);
    }

    private List<DocumentData> findDocumentDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentData.class));
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

    public DocumentData findDocumentData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentData.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentData> rt = cq.from(DocumentData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
