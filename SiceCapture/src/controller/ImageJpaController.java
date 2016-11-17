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
import entities.Expedient;
import entities.Document;
import entities.Client;
import entities.Image;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author intec
 */
public class ImageJpaController implements Serializable {

    public ImageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Image image) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expedient fkExpedient = image.getFkExpedient();
            if (fkExpedient != null) {
                fkExpedient = em.getReference(fkExpedient.getClass(), fkExpedient.getIdExpedient());
                image.setFkExpedient(fkExpedient);
            }
            Document fkDocument = image.getFkDocument();
            if (fkDocument != null) {
                fkDocument = em.getReference(fkDocument.getClass(), fkDocument.getIdDocument());
                image.setFkDocument(fkDocument);
            }
            Client fkClient = image.getFkClient();
            if (fkClient != null) {
                fkClient = em.getReference(fkClient.getClass(), fkClient.getIdUser());
                image.setFkClient(fkClient);
            }
            em.persist(image);
            if (fkExpedient != null) {
                fkExpedient.getImageCollection().add(image);
                fkExpedient = em.merge(fkExpedient);
            }
            if (fkDocument != null) {
                fkDocument.getImageCollection().add(image);
                fkDocument = em.merge(fkDocument);
            }
            if (fkClient != null) {
                fkClient.getImageCollection().add(image);
                fkClient = em.merge(fkClient);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Image> findByExpedientDocumentClient(Expedient fkExpedient,Document fkDocument, Client fkClient) {
        List<Image> imageList = null;
        try {
            EntityManager em = getEntityManager();
            Query query = em.createNamedQuery("Image.findByExpedientDocumentClient", Image.class);
            query.setParameter("fkExpedient", fkExpedient);
            query.setParameter("fkDocument", fkDocument);
            query.setParameter("fkClient", fkClient);
            imageList =  (List<Image>) query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(DocumentJpaController.class.getName()).log(Level.SEVERE, null, e);
        }
        return imageList;
    }

    public void edit(Image image) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Image persistentImage = em.find(Image.class, image.getIdImage());
            Expedient fkExpedientOld = persistentImage.getFkExpedient();
            Expedient fkExpedientNew = image.getFkExpedient();
            Document fkDocumentOld = persistentImage.getFkDocument();
            Document fkDocumentNew = image.getFkDocument();
            Client fkClientOld = persistentImage.getFkClient();
            Client fkClientNew = image.getFkClient();
            if (fkExpedientNew != null) {
                fkExpedientNew = em.getReference(fkExpedientNew.getClass(), fkExpedientNew.getIdExpedient());
                image.setFkExpedient(fkExpedientNew);
            }
            if (fkDocumentNew != null) {
                fkDocumentNew = em.getReference(fkDocumentNew.getClass(), fkDocumentNew.getIdDocument());
                image.setFkDocument(fkDocumentNew);
            }
            if (fkClientNew != null) {
                fkClientNew = em.getReference(fkClientNew.getClass(), fkClientNew.getIdUser());
                image.setFkClient(fkClientNew);
            }
            image = em.merge(image);
            if (fkExpedientOld != null && !fkExpedientOld.equals(fkExpedientNew)) {
                fkExpedientOld.getImageCollection().remove(image);
                fkExpedientOld = em.merge(fkExpedientOld);
            }
            if (fkExpedientNew != null && !fkExpedientNew.equals(fkExpedientOld)) {
                fkExpedientNew.getImageCollection().add(image);
                fkExpedientNew = em.merge(fkExpedientNew);
            }
            if (fkDocumentOld != null && !fkDocumentOld.equals(fkDocumentNew)) {
                fkDocumentOld.getImageCollection().remove(image);
                fkDocumentOld = em.merge(fkDocumentOld);
            }
            if (fkDocumentNew != null && !fkDocumentNew.equals(fkDocumentOld)) {
                fkDocumentNew.getImageCollection().add(image);
                fkDocumentNew = em.merge(fkDocumentNew);
            }
            if (fkClientOld != null && !fkClientOld.equals(fkClientNew)) {
                fkClientOld.getImageCollection().remove(image);
                fkClientOld = em.merge(fkClientOld);
            }
            if (fkClientNew != null && !fkClientNew.equals(fkClientOld)) {
                fkClientNew.getImageCollection().add(image);
                fkClientNew = em.merge(fkClientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = image.getIdImage();
                if (findImage(id) == null) {
                    throw new NonexistentEntityException("The image with id " + id + " no longer exists.");
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
            Image image;
            try {
                image = em.getReference(Image.class, id);
                image.getIdImage();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The image with id " + id + " no longer exists.", enfe);
            }
            Expedient fkExpedient = image.getFkExpedient();
            if (fkExpedient != null) {
                fkExpedient.getImageCollection().remove(image);
                fkExpedient = em.merge(fkExpedient);
            }
            Document fkDocument = image.getFkDocument();
            if (fkDocument != null) {
                fkDocument.getImageCollection().remove(image);
                fkDocument = em.merge(fkDocument);
            }
            Client fkClient = image.getFkClient();
            if (fkClient != null) {
                fkClient.getImageCollection().remove(image);
                fkClient = em.merge(fkClient);
            }
            em.remove(image);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Image> findImageEntities() {
        return findImageEntities(true, -1, -1);
    }

    public List<Image> findImageEntities(int maxResults, int firstResult) {
        return findImageEntities(false, maxResults, firstResult);
    }

    private List<Image> findImageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Image.class));
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

    public Image findImage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Image.class, id);
        } finally {
            em.close();
        }
    }

    public int getImageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Image> rt = cq.from(Image.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
