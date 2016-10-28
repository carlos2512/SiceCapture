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
@Table(name = "client_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientData.findAll", query = "SELECT c FROM ClientData c"),
    @NamedQuery(name = "ClientData.findByFkClient", query = "SELECT c FROM ClientData c WHERE c.clientDataPK.fkClient = :fkClient"),
    @NamedQuery(name = "ClientData.findByFkDocumentData", query = "SELECT c FROM ClientData c WHERE c.clientDataPK.fkDocumentData = :fkDocumentData"),
    @NamedQuery(name = "ClientData.findByValue", query = "SELECT c FROM ClientData c WHERE c.value = :value"),
    @NamedQuery(name = "ClientData.findByLastModification", query = "SELECT c FROM ClientData c WHERE c.lastModification = :lastModification")})
public class ClientData implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientDataPK clientDataPK;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @Column(name = "last_modification")
    @Temporal(TemporalType.DATE)
    private Date lastModification;
    @JoinColumn(name = "fk_document_data", referencedColumnName = "fk_document", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentData documentData;
    @JoinColumn(name = "fk_client", referencedColumnName = "idUser", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;

    public ClientData() {
    }

    public ClientData(ClientDataPK clientDataPK) {
        this.clientDataPK = clientDataPK;
    }

    public ClientData(ClientDataPK clientDataPK, String value, Date lastModification) {
        this.clientDataPK = clientDataPK;
        this.value = value;
        this.lastModification = lastModification;
    }

    public ClientData(int fkClient, int fkDocumentData) {
        this.clientDataPK = new ClientDataPK(fkClient, fkDocumentData);
    }

    public ClientDataPK getClientDataPK() {
        return clientDataPK;
    }

    public void setClientDataPK(ClientDataPK clientDataPK) {
        this.clientDataPK = clientDataPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientDataPK != null ? clientDataPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientData)) {
            return false;
        }
        ClientData other = (ClientData) object;
        if ((this.clientDataPK == null && other.clientDataPK != null) || (this.clientDataPK != null && !this.clientDataPK.equals(other.clientDataPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClientData[ clientDataPK=" + clientDataPK + " ]";
    }
    
}
