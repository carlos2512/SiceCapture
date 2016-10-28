/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author intec
 */
@Entity
@Table(name = "expedient_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpedientClient.findAll", query = "SELECT e FROM ExpedientClient e"),
    @NamedQuery(name = "ExpedientClient.findByFkExpedient", query = "SELECT e FROM ExpedientClient e WHERE e.expedientClientPK.fkExpedient = :fkExpedient"),
    @NamedQuery(name = "ExpedientClient.findByFkClient", query = "SELECT e FROM ExpedientClient e WHERE e.expedientClientPK.fkClient = :fkClient"),
    @NamedQuery(name = "ExpedientClient.findByLastModification", query = "SELECT e FROM ExpedientClient e WHERE e.lastModification = :lastModification")})
public class ExpedientClient implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpedientClientPK expedientClientPK;
    @Basic(optional = false)
    @Column(name = "last_modification")
    @Temporal(TemporalType.DATE)
    private Date lastModification;
    @JoinColumn(name = "fk_client", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;
    @JoinColumn(name = "fk_expedient", referencedColumnName = "idExpedient", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Expedient expedient;

    public ExpedientClient() {
    }

    public ExpedientClient(ExpedientClientPK expedientClientPK) {
        this.expedientClientPK = expedientClientPK;
    }

    public ExpedientClient(ExpedientClientPK expedientClientPK, Date lastModification) {
        this.expedientClientPK = expedientClientPK;
        this.lastModification = lastModification;
    }

    public ExpedientClient(int fkExpedient, int fkClient) {
        this.expedientClientPK = new ExpedientClientPK(fkExpedient, fkClient);
    }

    public ExpedientClientPK getExpedientClientPK() {
        return expedientClientPK;
    }

    public void setExpedientClientPK(ExpedientClientPK expedientClientPK) {
        this.expedientClientPK = expedientClientPK;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Expedient getExpedient() {
        return expedient;
    }

    public void setExpedient(Expedient expedient) {
        this.expedient = expedient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expedientClientPK != null ? expedientClientPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedientClient)) {
            return false;
        }
        ExpedientClient other = (ExpedientClient) object;
        if ((this.expedientClientPK == null && other.expedientClientPK != null) || (this.expedientClientPK != null && !this.expedientClientPK.equals(other.expedientClientPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ExpedientClient[ expedientClientPK=" + expedientClientPK + " ]";
    }
    
}
