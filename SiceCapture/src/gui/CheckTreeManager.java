package gui;

import controller.ClientDataJpaController;
import entities.ClientData;
import entities.ClientDataPK;
import entities.Document;
import entities.DocumentData;
import entities.Expedient;
import entities.ExpedientClient;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class CheckTreeManager implements TreeSelectionListener {

    private CheckTreeSelectionModel selectionModel;
    private JTree tree = new JTree();
    private ExpedientClient expedientClient;
    int hotspot = new JCheckBox().getPreferredSize().width;
    private EntityManagerFactory emf;
    private ClientDataJpaController clientDataController;

    public CheckTreeManager(JTree tree, ExpedientClient expedientClient, EntityManagerFactory emf) {
        this.emf = emf;
        this.expedientClient = expedientClient;
        this.tree = tree;
        clientDataController = new ClientDataJpaController(emf);
        selectionModel = new CheckTreeSelectionModel(tree.getModel());
        tree.setCellRenderer(new CheckTreeCellRenderer(tree.getCellRenderer(), selectionModel));
        selectionModel.addTreeSelectionListener(this);
        automaticSelectionByClient();
    }

    public TreePath getPath(TreeNode treeNode) {
        List<Object> nodes = new ArrayList<Object>();
        if (treeNode != null) {
            nodes.add(treeNode);
            treeNode = treeNode.getParent();
            while (treeNode != null) {
                nodes.add(0, treeNode);
                treeNode = treeNode.getParent();
            }
        }

        return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
    }

    public void automaticSelectionByClient() {
        selectionModel.clearSelection();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) (tree.getModel().getRoot());
        Enumeration<Object> enumeration = root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            Object objeto = enumeration.nextElement();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) objeto;
            if (node != null) {
                if (node.getUserObject() instanceof DocumentData) {
                    DocumentData documentDataObject = (DocumentData) node.getUserObject();
                    List<ClientData> clientDataList = new ArrayList<>();
                    ClientDataPK clientDataPK = new ClientDataPK();
                    clientDataPK.setFkClient(expedientClient.getClient().getIdUser());
                    clientDataPK.setFkDocumentData(documentDataObject.getIdDocumentdata());
                    ClientData findedData = clientDataController.findClientData(clientDataPK);
                    if (findedData != null) {
                        TreePath path = getPath(node);
                        selectionModel.addSelectionPath(path);

                    }
                }
            }
        }
    }

    public CheckTreeSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void valueChanged(TreeSelectionEvent e) {
        tree.treeDidChange();
    }
}
