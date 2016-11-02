/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entities.Document;
import entities.DocumentData;
import entities.Expedient;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author intec
 */
public class DefaultMutableTreeNodeCustom extends DefaultMutableTreeNode {

    public DefaultMutableTreeNodeCustom(Object userObject) {
        super(userObject, true);
    }

    @Override
    public String toString() {
        if (userObject == null) {
            return "";
        } else if (userObject instanceof Expedient) {
            Expedient expedient = (Expedient) userObject;
            return expedient.getName();
        } else if (userObject instanceof Document) {
            Document document = (Document) userObject;
            return document.getName();
        } else if (userObject instanceof DocumentData) {
            DocumentData documentData = (DocumentData) userObject;
            return documentData.getName();
        } else {
            return userObject.toString();
        }
    }
}
