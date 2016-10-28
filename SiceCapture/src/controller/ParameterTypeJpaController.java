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
import entities.DocumentParameter;
import entities.ParameterType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ParameterTypeJpaController implements Serializable {

    public ParameterTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ParameterType parameterType) throws PreexistingEntityException, Exception {
        if (parameterType.getDocumentParameterCollection() == null) {
            parameterType.setDocumentParameterCollection(new ArrayList<DocumentParameter>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DocumentParameter> attachedDocumentParameterCollection = new ArrayList<DocumentParameter>();
            for (DocumentParameter documentParameterCollectionDocumentParameterToAttach : parameterType.getDocumentParameterCollection()) {
                documentParameterCollectionDocumentParameterToAttach = em.getReference(documentParameterCollectionDocumentParameterToAttach.getClass(), documentParameterCollectionDocumentParameterToAttach.getIdParameter());
                attachedDocumentParameterCollection.add(documentParameterCollectionDocumentParameterToAttach);
            }
            parameterType.setDocumentParameterCollection(attachedDocumentParameterCollection);
            em.persist(parameterType);
            for (DocumentParameter documentParameterCollectionDocumentParameter : parameterType.getDocumentParameterCollection()) {
                ParameterType oldFkParameterTypeOfDocumentParameterCollectionDocumentParameter = documentParameterCollectionDocumentParameter.getFkParameterType();
                documentParameterCollectionDocumentParameter.setFkParameterType(parameterType);
                documentParameterCollectionDocumentParameter = em.merge(documentParameterCollectionDocumentParameter);
                if (oldFkParameterTypeOfDocumentParameterCollectionDocumentParameter != null) {
                    oldFkParameterTypeOfDocumentParameterCollectionDocumentParameter.getDocumentParameterCollection().remove(documentParameterCollectionDocumentParameter);
                    oldFkParameterTypeOfDocumentParameterCollectionDocumentParameter = em.merge(oldFkParameterTypeOfDocumentParameterCollectionDocumentParameter);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParameterType(parameterType.getIdParametertype()) != null) {
                throw new PreexistingEntityException("ParameterType " + parameterType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParameterType parameterType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ParameterType persistentParameterType = em.find(ParameterType.class, parameterType.getIdParametertype());
            Collection<DocumentParameter> documentParameterCollectionOld = persistentParameterType.getDocumentParameterCollection();
            Collection<DocumentParameter> documentParameterCollectionNew = parameterType.getDocumentParameterCollection();
            List<String> illegalOrphanMessages = null;
            for (DocumentParameter documentParameterCollectionOldDocumentParameter : documentParameterCollectionOld) {
                if (!documentParameterCollectionNew.contains(documentParameterCollectionOldDocumentParameter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentParameter " + documentParameterCollectionOldDocumentParameter + " since its fkParameterType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DocumentParameter> attachedDocumentParameterCollectionNew = new ArrayList<DocumentParameter>();
            for (DocumentParameter documentParameterCollectionNewDocumentParameterToAttach : documentParameterCollectionNew) {
                documentParameterCollectionNewDocumentParameterToAttach = em.getReference(documentParameterCollectionNewDocumentParameterToAttach.getClass(), documentParameterCollectionNewDocumentParameterToAttach.getIdParameter());
                attachedDocumentParameterCollectionNew.add(documentParameterCollectionNewDocumentParameterToAttach);
            }
            documentParameterCollectionNew = attachedDocumentParameterCollectionNew;
            parameterType.setDocumentParameterCollection(documentParameterCollectionNew);
            parameterType = em.merge(parameterType);
            for (DocumentParameter documentParameterCollectionNewDocumentParameter : documentParameterCollectionNew) {
                if (!documentParameterCollectionOld.contains(documentParameterCollectionNewDocumentParameter)) {
                    ParameterType oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter = documentParameterCollectionNewDocumentParameter.getFkParameterType();
                    documentParameterCollectionNewDocumentParameter.setFkParameterType(parameterType);
                    documentParameterCollectionNewDocumentParameter = em.merge(documentParameterCollectionNewDocumentParameter);
                    if (oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter != null && !oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter.equals(parameterType)) {
                        oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter.getDocumentParameterCollection().remove(documentParameterCollectionNewDocumentParameter);
                        oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter = em.merge(oldFkParameterTypeOfDocumentParameterCollectionNewDocumentParameter);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parameterType.getIdParametertype();
                if (findParameterType(id) == null) {
                    throw new NonexistentEntityException("The parameterType with id " + id + " no longer exists.");
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
            ParameterType parameterType;
            try {
                parameterType = em.getReference(ParameterType.class, id);
                parameterType.getIdParametertype();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parameterType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DocumentParameter> documentParameterCollectionOrphanCheck = parameterType.getDocumentParameterCollection();
            for (DocumentParameter documentParameterCollectionOrphanCheckDocumentParameter : documentParameterCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ParameterType (" + parameterType + ") cannot be destroyed since the DocumentParameter " + documentParameterCollectionOrphanCheckDocumentParameter + " in its documentParameterCollection field has a non-nullable fkParameterType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(parameterType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ParameterType> findParameterTypeEntities() {
        return findParameterTypeEntities(true, -1, -1);
    }

    public List<ParameterType> findParameterTypeEntities(int maxResults, int firstResult) {
        return findParameterTypeEntities(false, maxResults, firstResult);
    }

    private List<ParameterType> findParameterTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParameterType.class));
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

    public ParameterType findParameterType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParameterType.class, id);
        } finally {
            em.close();
        }
    }

    public int getParameterTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParameterType> rt = cq.from(ParameterType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
