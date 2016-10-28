/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Document;
import entities.DocumentParameter;
import entities.ParameterType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class DocumentParameterJpaController implements Serializable {

    public DocumentParameterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocumentParameter documentParameter) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Document fkDocument = documentParameter.getFkDocument();
            if (fkDocument != null) {
                fkDocument = em.getReference(fkDocument.getClass(), fkDocument.getIdDocument());
                documentParameter.setFkDocument(fkDocument);
            }
            ParameterType fkParameterType = documentParameter.getFkParameterType();
            if (fkParameterType != null) {
                fkParameterType = em.getReference(fkParameterType.getClass(), fkParameterType.getIdParametertype());
                documentParameter.setFkParameterType(fkParameterType);
            }
            em.persist(documentParameter);
            if (fkDocument != null) {
                fkDocument.getDocumentParameterCollection().add(documentParameter);
                fkDocument = em.merge(fkDocument);
            }
            if (fkParameterType != null) {
                fkParameterType.getDocumentParameterCollection().add(documentParameter);
                fkParameterType = em.merge(fkParameterType);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocumentParameter documentParameter) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentParameter persistentDocumentParameter = em.find(DocumentParameter.class, documentParameter.getIdParameter());
            Document fkDocumentOld = persistentDocumentParameter.getFkDocument();
            Document fkDocumentNew = documentParameter.getFkDocument();
            ParameterType fkParameterTypeOld = persistentDocumentParameter.getFkParameterType();
            ParameterType fkParameterTypeNew = documentParameter.getFkParameterType();
            if (fkDocumentNew != null) {
                fkDocumentNew = em.getReference(fkDocumentNew.getClass(), fkDocumentNew.getIdDocument());
                documentParameter.setFkDocument(fkDocumentNew);
            }
            if (fkParameterTypeNew != null) {
                fkParameterTypeNew = em.getReference(fkParameterTypeNew.getClass(), fkParameterTypeNew.getIdParametertype());
                documentParameter.setFkParameterType(fkParameterTypeNew);
            }
            documentParameter = em.merge(documentParameter);
            if (fkDocumentOld != null && !fkDocumentOld.equals(fkDocumentNew)) {
                fkDocumentOld.getDocumentParameterCollection().remove(documentParameter);
                fkDocumentOld = em.merge(fkDocumentOld);
            }
            if (fkDocumentNew != null && !fkDocumentNew.equals(fkDocumentOld)) {
                fkDocumentNew.getDocumentParameterCollection().add(documentParameter);
                fkDocumentNew = em.merge(fkDocumentNew);
            }
            if (fkParameterTypeOld != null && !fkParameterTypeOld.equals(fkParameterTypeNew)) {
                fkParameterTypeOld.getDocumentParameterCollection().remove(documentParameter);
                fkParameterTypeOld = em.merge(fkParameterTypeOld);
            }
            if (fkParameterTypeNew != null && !fkParameterTypeNew.equals(fkParameterTypeOld)) {
                fkParameterTypeNew.getDocumentParameterCollection().add(documentParameter);
                fkParameterTypeNew = em.merge(fkParameterTypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentParameter.getIdParameter();
                if (findDocumentParameter(id) == null) {
                    throw new NonexistentEntityException("The documentParameter with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentParameter documentParameter;
            try {
                documentParameter = em.getReference(DocumentParameter.class, id);
                documentParameter.getIdParameter();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentParameter with id " + id + " no longer exists.", enfe);
            }
            Document fkDocument = documentParameter.getFkDocument();
            if (fkDocument != null) {
                fkDocument.getDocumentParameterCollection().remove(documentParameter);
                fkDocument = em.merge(fkDocument);
            }
            ParameterType fkParameterType = documentParameter.getFkParameterType();
            if (fkParameterType != null) {
                fkParameterType.getDocumentParameterCollection().remove(documentParameter);
                fkParameterType = em.merge(fkParameterType);
            }
            em.remove(documentParameter);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentParameter> findDocumentParameterEntities() {
        return findDocumentParameterEntities(true, -1, -1);
    }

    public List<DocumentParameter> findDocumentParameterEntities(int maxResults, int firstResult) {
        return findDocumentParameterEntities(false, maxResults, firstResult);
    }

    private List<DocumentParameter> findDocumentParameterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentParameter.class));
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

    public DocumentParameter findDocumentParameter(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentParameter.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentParameterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentParameter> rt = cq.from(DocumentParameter.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
