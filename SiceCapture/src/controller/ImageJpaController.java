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
import entities.Image;
import entities.ImagePK;
import java.util.List;
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

    public void create(Image image) throws PreexistingEntityException, Exception {
        if (image.getImagePK() == null) {
            image.setImagePK(new ImagePK());
        }
        image.getImagePK().setFkDocument(image.getDocument().getIdDocument());
        image.getImagePK().setFkExpedient(image.getExpedient().getIdExpedient());
        image.getImagePK().setFkClient(image.getClient().getIdUser());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client = image.getClient();
            if (client != null) {
                client = em.getReference(client.getClass(), client.getIdUser());
                image.setClient(client);
            }
            Document document = image.getDocument();
            if (document != null) {
                document = em.getReference(document.getClass(), document.getIdDocument());
                image.setDocument(document);
            }
            Expedient expedient = image.getExpedient();
            if (expedient != null) {
                expedient = em.getReference(expedient.getClass(), expedient.getIdExpedient());
                image.setExpedient(expedient);
            }
            em.persist(image);
            if (client != null) {
                client.getImageCollection().add(image);
                client = em.merge(client);
            }
            if (document != null) {
                document.getImageCollection().add(image);
                document = em.merge(document);
            }
            if (expedient != null) {
                expedient.getImageCollection().add(image);
                expedient = em.merge(expedient);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findImage(image.getImagePK()) != null) {
                throw new PreexistingEntityException("Image " + image + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Image image) throws NonexistentEntityException, Exception {
        image.getImagePK().setFkDocument(image.getDocument().getIdDocument());
        image.getImagePK().setFkExpedient(image.getExpedient().getIdExpedient());
        image.getImagePK().setFkClient(image.getClient().getIdUser());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Image persistentImage = em.find(Image.class, image.getImagePK());
            Client clientOld = persistentImage.getClient();
            Client clientNew = image.getClient();
            Document documentOld = persistentImage.getDocument();
            Document documentNew = image.getDocument();
            Expedient expedientOld = persistentImage.getExpedient();
            Expedient expedientNew = image.getExpedient();
            if (clientNew != null) {
                clientNew = em.getReference(clientNew.getClass(), clientNew.getIdUser());
                image.setClient(clientNew);
            }
            if (documentNew != null) {
                documentNew = em.getReference(documentNew.getClass(), documentNew.getIdDocument());
                image.setDocument(documentNew);
            }
            if (expedientNew != null) {
                expedientNew = em.getReference(expedientNew.getClass(), expedientNew.getIdExpedient());
                image.setExpedient(expedientNew);
            }
            image = em.merge(image);
            if (clientOld != null && !clientOld.equals(clientNew)) {
                clientOld.getImageCollection().remove(image);
                clientOld = em.merge(clientOld);
            }
            if (clientNew != null && !clientNew.equals(clientOld)) {
                clientNew.getImageCollection().add(image);
                clientNew = em.merge(clientNew);
            }
            if (documentOld != null && !documentOld.equals(documentNew)) {
                documentOld.getImageCollection().remove(image);
                documentOld = em.merge(documentOld);
            }
            if (documentNew != null && !documentNew.equals(documentOld)) {
                documentNew.getImageCollection().add(image);
                documentNew = em.merge(documentNew);
            }
            if (expedientOld != null && !expedientOld.equals(expedientNew)) {
                expedientOld.getImageCollection().remove(image);
                expedientOld = em.merge(expedientOld);
            }
            if (expedientNew != null && !expedientNew.equals(expedientOld)) {
                expedientNew.getImageCollection().add(image);
                expedientNew = em.merge(expedientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ImagePK id = image.getImagePK();
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

    public void destroy(ImagePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Image image;
            try {
                image = em.getReference(Image.class, id);
                image.getImagePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The image with id " + id + " no longer exists.", enfe);
            }
            Client client = image.getClient();
            if (client != null) {
                client.getImageCollection().remove(image);
                client = em.merge(client);
            }
            Document document = image.getDocument();
            if (document != null) {
                document.getImageCollection().remove(image);
                document = em.merge(document);
            }
            Expedient expedient = image.getExpedient();
            if (expedient != null) {
                expedient.getImageCollection().remove(image);
                expedient = em.merge(expedient);
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

    public Image findImage(ImagePK id) {
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
