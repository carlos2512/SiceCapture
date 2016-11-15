/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entities.DataType;
import entities.Document;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class DataTypeJpaController implements Serializable {

    public DataTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DataType dataType) throws PreexistingEntityException, Exception {
        if (dataType.getDocumentDataCollection() == null) {
            dataType.setDocumentDataCollection(new ArrayList<DocumentData>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DocumentData> attachedDocumentDataCollection = new ArrayList<DocumentData>();
            for (DocumentData documentDataCollectionDocumentDataToAttach : dataType.getDocumentDataCollection()) {
                documentDataCollectionDocumentDataToAttach = em.getReference(documentDataCollectionDocumentDataToAttach.getClass(), documentDataCollectionDocumentDataToAttach.getIdDocumentdata());
                attachedDocumentDataCollection.add(documentDataCollectionDocumentDataToAttach);
            }
            dataType.setDocumentDataCollection(attachedDocumentDataCollection);
            em.persist(dataType);
            for (DocumentData documentDataCollectionDocumentData : dataType.getDocumentDataCollection()) {
                DataType oldFkDataTypeOfDocumentDataCollectionDocumentData = documentDataCollectionDocumentData.getFkDataType();
                documentDataCollectionDocumentData.setFkDataType(dataType);
                documentDataCollectionDocumentData = em.merge(documentDataCollectionDocumentData);
                if (oldFkDataTypeOfDocumentDataCollectionDocumentData != null) {
                    oldFkDataTypeOfDocumentDataCollectionDocumentData.getDocumentDataCollection().remove(documentDataCollectionDocumentData);
                    oldFkDataTypeOfDocumentDataCollectionDocumentData = em.merge(oldFkDataTypeOfDocumentDataCollectionDocumentData);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDataType(dataType.getIdDatatype()) != null) {
                throw new PreexistingEntityException("DataType " + dataType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DataType dataType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DataType persistentDataType = em.find(DataType.class, dataType.getIdDatatype());
            Collection<DocumentData> documentDataCollectionOld = persistentDataType.getDocumentDataCollection();
            Collection<DocumentData> documentDataCollectionNew = dataType.getDocumentDataCollection();
            List<String> illegalOrphanMessages = null;
            for (DocumentData documentDataCollectionOldDocumentData : documentDataCollectionOld) {
                if (!documentDataCollectionNew.contains(documentDataCollectionOldDocumentData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentData " + documentDataCollectionOldDocumentData + " since its fkDataType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DocumentData> attachedDocumentDataCollectionNew = new ArrayList<DocumentData>();
            for (DocumentData documentDataCollectionNewDocumentDataToAttach : documentDataCollectionNew) {
                documentDataCollectionNewDocumentDataToAttach = em.getReference(documentDataCollectionNewDocumentDataToAttach.getClass(), documentDataCollectionNewDocumentDataToAttach.getIdDocumentdata());
                attachedDocumentDataCollectionNew.add(documentDataCollectionNewDocumentDataToAttach);
            }
            documentDataCollectionNew = attachedDocumentDataCollectionNew;
            dataType.setDocumentDataCollection(documentDataCollectionNew);
            dataType = em.merge(dataType);
            for (DocumentData documentDataCollectionNewDocumentData : documentDataCollectionNew) {
                if (!documentDataCollectionOld.contains(documentDataCollectionNewDocumentData)) {
                    DataType oldFkDataTypeOfDocumentDataCollectionNewDocumentData = documentDataCollectionNewDocumentData.getFkDataType();
                    documentDataCollectionNewDocumentData.setFkDataType(dataType);
                    documentDataCollectionNewDocumentData = em.merge(documentDataCollectionNewDocumentData);
                    if (oldFkDataTypeOfDocumentDataCollectionNewDocumentData != null && !oldFkDataTypeOfDocumentDataCollectionNewDocumentData.equals(dataType)) {
                        oldFkDataTypeOfDocumentDataCollectionNewDocumentData.getDocumentDataCollection().remove(documentDataCollectionNewDocumentData);
                        oldFkDataTypeOfDocumentDataCollectionNewDocumentData = em.merge(oldFkDataTypeOfDocumentDataCollectionNewDocumentData);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dataType.getIdDatatype();
                if (findDataType(id) == null) {
                    throw new NonexistentEntityException("The dataType with id " + id + " no longer exists.");
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
            DataType dataType;
            try {
                dataType = em.getReference(DataType.class, id);
                dataType.getIdDatatype();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dataType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DocumentData> documentDataCollectionOrphanCheck = dataType.getDocumentDataCollection();
            for (DocumentData documentDataCollectionOrphanCheckDocumentData : documentDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DataType (" + dataType + ") cannot be destroyed since the DocumentData " + documentDataCollectionOrphanCheckDocumentData + " in its documentDataCollection field has a non-nullable fkDataType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dataType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DataType> findDataTypeEntities() {
        return findDataTypeEntities(true, -1, -1);
    }

    public List<DataType> findDataTypeEntities(int maxResults, int firstResult) {
        return findDataTypeEntities(false, maxResults, firstResult);
    }

    private List<DataType> findDataTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DataType.class));
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

    public DataType findDataType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DataType.class, id);
        } finally {
            em.close();
        }
    }

    public DataType findByCode(String code) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("DataType.findByCode", DataType.class);
        query.setParameter("code", code);
        DataType dataType = (DataType) query.getSingleResult();
        return dataType;
    }

    public int getDataTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DataType> rt = cq.from(DataType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
