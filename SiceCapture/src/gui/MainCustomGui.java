package gui;

import com.toedter.calendar.JDateChooser;
import controller.ClientDataJpaController;
import controller.ClientJpaController;
import controller.DataTypeJpaController;
import controller.DocumentDataJpaController;
import controller.DocumentJpaController;
import controller.ExpedientClientJpaController;
import controller.ExpedientJpaController;
import controller.exceptions.NonexistentEntityException;
import core.ScannerBackground;
import entities.Client;
import entities.ClientData;
import entities.ClientDataPK;
import entities.DataType;
import entities.Document;
import entities.DocumentData;
import entities.Expedient;
import entities.ExpedientClient;
import entities.ExpedientClientPK;
import static gui.CaptureDataDialogCustom.listOfTextFields;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author intec
 */
public class MainCustomGui extends javax.swing.JFrame {
    
    private final ScannerBackground scannerBackground;
    private Document selectedDocument;
    private DocumentData selectedDocumentData;
    private ExpedientClient expedientClientInProcess;
    private DocumentJpaController documentController;
    private ExpedientJpaController expedientController;
    private JDialog includeDocumentDialog;
    private ExpedientClientJpaController expedientClientController;
    private CheckTreeManager checkTreeManager;
    private ClientJpaController clientController;
    private DocumentDataJpaController documentDataController;
    private DataTypeJpaController dataTypeController;
    private Expedient selectedExpedientInCreationPanel;
    private JDialog registPersonDialog;
    private List componentList;
    private ClientDataJpaController clientDataController;
    private JDialog includeExpedientDialog;
    private boolean indexProcessIsActive;
    private Expedient selectedExpedientFromTree;
    private List<DocumentData> documentDataListCurrentCaptureProcess;
    private Document selectedDocumentFromTree;
    private final String expedientIconPath = "../resources/img/expedientIcon.png";
    private final String expedientIconPath2 = "../resources/img/expedientIcon2.png";
    private final String documentIconPath = "../resources/img/doc.png";
    private final String documentIconPath2 = "../resources/img/doc2.png";
    private final String dataDateIconPath = "../resources/img/datefield.png";
    private final String dataValueIconPath = "../resources/img/datafield.png";
    private final String metaIconPath = "../resources/img/meta.png";
    private final String DATE_DATA_TYPE_CODE = "type.date";
    private final String VALUE_DATA_TYPE_CODE = "type.data";
    private javax.swing.JPanel captureDataShowPane;
    private javax.swing.JLabel documentValidationLabel;
    private JDialog captureDataDialog;
    private boolean creationExpedientState;
    private boolean documentsReceptionState;
    private final EntityManagerFactory emf;
    private TableRowSorter<TableModel> sorter;

    /**
     * Creates new form MainGui
     *
     * @param emf
     */
    public MainCustomGui(EntityManagerFactory emf) {
        this.emf = emf;
        initControllers();
        initComponents();
        desactiveDocumentParameterPane();
        desactiveDocumentDataParameterPane();
        desactiveCaptureDataShowPane();
        setLocationRelativeTo(null);
        scannerBackground = new ScannerBackground();
        desactiveAllScannerPanes();
    }
    
    private void desactiveDocumentParameterPane() {
        documentParameterPane.setVisible(false);
    }
    
    private void desactiveAllScannerPanes() {
        scannerPaneDownLeft.setVisible(false);
        scannerPaneUpLeft.setVisible(false);
        scannerPaneUpRight.setVisible(false);
        scannerPaneDownRight.setVisible(false);
    }
    
    private void desactiveDocumentDataParameterPane() {
        dataParameterPane.setVisible(false);
    }
    
    private void activeDocumentDataParameterPane(DocumentData data) {
        this.nameDataParameterPaneTxt.setText(data.getName());
        this.dataTypeDataParameterTxt.setText(data.getFkDataType().getName());
        if (data.getRequired() == 1) {
            this.requiredDataParameterPaneCheckBox.setSelected(true);
        } else {
            this.requiredDataParameterPaneCheckBox.setSelected(false);
        }
        nameDataParameterPaneTxt.setEditable(false);
        dataTypeDataParameterTxt.setEditable(false);
        requiredDataParameterPaneCheckBox.setEnabled(false);
        dataParameterPane.setVisible(true);
    }
    
    private void activeDocumentParameterPane(Document selectedDocument) {
        this.nameDocumentPaneTxt.setText(selectedDocument.getName());
        this.maxSizeDocumentPaneTxt.setText(String.valueOf(selectedDocument.getMaxImageSize()));
        if (selectedDocument.getIsRequired() == 1) {
            this.requeridDocumentPaneCheckBox.setSelected(true);
        } else {
            this.requeridDocumentPaneCheckBox.setSelected(false);
        }
        if (selectedDocument.getCanExpire() == 1) {
            this.expireDocumentPaneCheckBox.setSelected(true);
        } else {
            this.expireDocumentPaneCheckBox.setSelected(false);
        }
        if (selectedDocument.getCanRepeat() == 1) {
            this.repeatDocumentPaneCheckBox.setSelected(true);
        } else {
            this.repeatDocumentPaneCheckBox.setSelected(false);
        }
        requeridDocumentPaneCheckBox.setEnabled(false);
        repeatDocumentPaneCheckBox.setEnabled(false);
        expireDocumentPaneCheckBox.setEnabled(false);
        maxSizeDocumentPaneTxt.setEnabled(false);
        nameDocumentPaneTxt.setEnabled(false);
        documentParameterPane.setVisible(true);
    }
    
    private void openPopupDocument(Component component, int x, int y) {
        JPopupMenu popupDocument = new JPopupMenu();
        JMenuItem addDataLabel = new JMenuItem("Añadir Dato");
        addDataLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JDialog dataCreationDialog = new JDialog();
                initComponentsCreationDataDialog(dataCreationDialog);
                dataCreationDialog.setModal(true);
                dataCreationDialog.setVisible(true);
            }
        });
        JMenuItem modifyDocumentLabel = new JMenuItem("Modificar Documento");
        JMenuItem deleteDocumentLabel = new JMenuItem("Eliminar Documento");
        popupDocument.add(addDataLabel);
        popupDocument.add(modifyDocumentLabel);
        popupDocument.add(deleteDocumentLabel);
        popupDocument.show(component, x, y);
    }
    
    private void openPopupExpedient(Component component, int x, int y) {
        JPopupMenu popupExpedient = new JPopupMenu();
        JMenuItem modifyExpedientLabel = new JMenuItem("Modificar Expediente");
        JMenuItem documentInclude = new JMenuItem("Incluir Documento");
        JMenuItem deleteExpedientLabel = new JMenuItem("Eliminar Expediente");
        documentInclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeDocumentDialog = new JDialog();
                initDocumentIncludeDialog();
                includeDocumentDialog.setModal(true);
                includeDocumentDialog.setVisible(true);
            }
        });
        popupExpedient.add(documentInclude);
        popupExpedient.add(modifyExpedientLabel);
        popupExpedient.add(deleteExpedientLabel);
        popupExpedient.show(component, x, y);
    }
    
    private void openPopupMetaExpedientDocumentReception(Component component, int x, int y) {
        JPopupMenu popupMetaExpedient = new JPopupMenu();
        JMenuItem expedientInclude = new JMenuItem("Incluir Expediente");
        expedientInclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeExpedientDialog = new JDialog();
                initIncludeExpedientDialog();
                includeExpedientDialog.setModal(true);
                includeExpedientDialog.setVisible(true);
            }
        });
        popupMetaExpedient.add(expedientInclude);
        popupMetaExpedient.show(component, x, y);
    }
    
    public void displayDocumentByExpedient(ExpedientClient expedientClient) {
        DefaultMutableTreeNode metaExpedient = new DefaultMutableTreeNode("Meta Expedientes");
        DefaultMutableTreeNodeCustom expedientNode = new DefaultMutableTreeNodeCustom(expedientClient.getExpedient());
        if (expedientClient.getExpedient().getDocumentCollection() != null && !expedientClient.getExpedient().getDocumentCollection().isEmpty()) {
            for (Document document : expedientClient.getExpedient().getDocumentCollection()) {
                DefaultMutableTreeNodeCustom documentNode = new DefaultMutableTreeNodeCustom(document);
                List<DocumentData> documentDataList = documentDataController.findByDocument(document);
                if (documentDataList != null && !documentDataList.isEmpty()) {
                    for (DocumentData documentData : documentDataList) {
                        DefaultMutableTreeNodeCustom documentDataNode = new DefaultMutableTreeNodeCustom(documentData);
                        documentNode.add(documentDataNode);
                    }
                }
                expedientNode.add(documentNode);
            }
        }
        metaExpedient.add(expedientNode);
        expedientTree.setModel(new DefaultTreeModel(metaExpedient));
    }
    
    public void displayExpedientClientTree(List<Expedient> expedientList) {
        DefaultMutableTreeNode metaExpedient = new DefaultMutableTreeNode("Meta Expedientes");
        for (Expedient expedient : expedientList) {
            DefaultMutableTreeNodeCustom expedientNode = new DefaultMutableTreeNodeCustom(expedient);
            if (expedient.getDocumentCollection() != null && !expedient.getDocumentCollection().isEmpty()) {
                for (Document document : expedient.getDocumentCollection()) {
                    DefaultMutableTreeNodeCustom documentNode = new DefaultMutableTreeNodeCustom(document);
                    List<DocumentData> documentDataList = documentDataController.findByDocument(document);
                    if (documentDataList != null && !documentDataList.isEmpty()) {
                        for (DocumentData documentData : documentDataList) {
                            DefaultMutableTreeNodeCustom documentDataNode = new DefaultMutableTreeNodeCustom(documentData);
                            documentNode.add(documentDataNode);
                        }
                    }
                    expedientNode.add(documentNode);
                }
            }
            metaExpedient.add(expedientNode);
            expedientTree.setModel(new DefaultTreeModel(metaExpedient));
        }
        
    }
    
    private void refreshExpedientsOfClient(ExpedientClient expedientClient) {
        List<ExpedientClient> allExpedientsListOfClient = expedientClientController.findExpedientClientByClient(expedientClient.getClient());
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Object arrayExpedient1 : allExpedientsListOfClient) {
            model.addElement(arrayExpedient1);
        }
        expedientSelectorReception.setModel(model);
    }
    
    private void initDocumentVerification(ExpedientClient expedientClient) {
        expedientTree.setCellRenderer(null);
        assingIconsToBussinesTree(expedientTree);
        creationExpedientState = false;
        documentsReceptionState = true;
        List<ExpedientClient> allExpedientsListOfClient = expedientClientController.findExpedientClientByClient(expedientClient.getClient());
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Object arrayExpedient1 : allExpedientsListOfClient) {
            model.addElement(arrayExpedient1);
            ExpedientClient expedientClientTemp = (ExpedientClient) arrayExpedient1;
            if (expedientClientTemp.getExpedient().equals(expedientClient.getExpedient())) {
                model.setSelectedItem(arrayExpedient1);
            }
        }
        documentReceptionNameClient.setText(expedientClient.getClient().getName());
        expedientSelectorReception.setModel(model);
        receptionLayoutCardPane.setVisible(true);
        ExpedientClient expedientClientTemp = (ExpedientClient) expedientSelectorReception.getSelectedItem();
        displayDocumentByExpedient(expedientClientTemp);
        checkTreeManager = new CheckTreeManager(expedientTree, expedientClient, emf);
        checkTreeManager.automaticSelectionByClient();
        indexButton.setEnabled(true);
    }
    
    private void initComponentSearchPersonDialog(final JDialog searchPersonDialog) {
        infoSearchLabel = new javax.swing.JLabel();
        idenTypeSearchLabel = new javax.swing.JLabel();
        idenSearchLabel = new javax.swing.JLabel();
        typeIdenSearchSelector = new javax.swing.JComboBox();
        idenSearchTxt = new javax.swing.JTextField();
        findSearchButton = new javax.swing.JButton();
        cleanSearchButton = new javax.swing.JButton();
        searchTableScrollPane = new javax.swing.JScrollPane();
        searchTable = new javax.swing.JTable();
        searchPersonDialog.setTitle("SICE CAPTURE/ BUSCAR EXPEDIENTES");
        infoSearchLabel.setText("Seleccione el filtro que desee, para realizar la búsqueda ");
        idenTypeSearchLabel.setText("Tipo de Identificación:");
        idenSearchLabel.setText("Número de Identificación:");
        typeIdenSearchSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Pasaporte", "Documento de Identidad"}));
        findSearchButton.setText("Buscar");
        cleanSearchButton.setText("Limpiar");
        List<ExpedientClient> expedientClientList = new ArrayList<>();
        expedientClientList = expedientClientController.findExpedientClientEntities();
        TableModelCustom tblModel = new TableModelCustom(expedientClientList);
        sorter = new TableRowSorter<TableModel>(tblModel);
        searchTable.setModel(new TableModelCustom(expedientClientList));
        searchTable.setRowSorter(sorter);
        searchTableScrollPane.setViewportView(searchTable);
        if (searchTable.getColumnModel().getColumnCount() > 0) {
            searchTable.getColumnModel().getColumn(0).setResizable(false);
            searchTable.getColumnModel().getColumn(1).setResizable(false);
            searchTable.getColumnModel().getColumn(2).setResizable(false);
            searchTable.getColumnModel().getColumn(3).setResizable(false);
            searchTable.getColumnModel().getColumn(4).setResizable(false);
            searchTable.getColumnModel().getColumn(5).setResizable(false);
        }
        
        searchTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        searchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    TableModelCustom tableModel = (TableModelCustom) searchTable.getModel();
                    int selectedRow = searchTable.getSelectionModel().getMinSelectionIndex();
                    ExpedientClient expedientClient = tableModel.getExpedientClient(selectedRow);
                    searchPersonDialog.setVisible(false);
                    expedientClientInProcess = expedientClient;
                    expedientSelectorReception.setSelectedItem(expedientClientInProcess.getExpedient());
                    initDocumentVerification(expedientClient);
                    
                }
            }
        });
        
        findSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String idenSearchTxtString = idenSearchTxt.getText();
                String typeIdenSearchTxtString = (String) typeIdenSearchSelector.getSelectedItem();
                if (idenSearchTxtString.length() == 0) {
                    try {
                        sorter.setRowFilter(
                                RowFilter.regexFilter(typeIdenSearchTxtString));
                    } catch (PatternSyntaxException pse) {
                    }
                } else {
                    try {
                        ArrayList<RowFilter<Object, Object>> multipleFilters = new ArrayList<RowFilter<Object, Object>>();
                        RowFilter<Object, Object> idenFilter = RowFilter.regexFilter(idenSearchTxtString);
                        RowFilter<Object, Object> idenTypeFilter = RowFilter.regexFilter(typeIdenSearchTxtString);
                        multipleFilters.add(idenFilter);
                        multipleFilters.add(idenTypeFilter);
                        sorter.setRowFilter(
                                RowFilter.andFilter(multipleFilters));
                    } catch (PatternSyntaxException pse) {
                        System.err.println("Bad regex pattern");
                    }
                }
            }
        });
        
        cleanSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idenSearchTxt.setText("");
                sorter.setRowFilter(null);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(searchPersonDialog.getContentPane());
        searchPersonDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(searchTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(infoSearchLabel)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(idenTypeSearchLabel)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(typeIdenSearchSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(idenSearchLabel)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(cleanSearchButton)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(findSearchButton))
                                                                        .addComponent(idenSearchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(infoSearchLabel)
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(idenTypeSearchLabel)
                                .addComponent(typeIdenSearchSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(idenSearchLabel)
                                .addComponent(idenSearchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(findSearchButton)
                                .addComponent(cleanSearchButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(searchTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        searchPersonDialog.setResizable(false);
        searchPersonDialog.pack();
        searchPersonDialog.setLocationRelativeTo(this);
    }
    
    private void desactiveCaptureDataShowPane() {
        captureDataShowPane.setVisible(false);
    }
    
    private void validateClientIdentificacionDocuments(String name, String value, Client client) {
        Document document = null;
        String upperName = name.toUpperCase();
        document = documentController.findByName(upperName);
        if (document != null) {
            List<DocumentData> documentDataList = documentDataController.findByDocument(document);
            if (documentDataList != null && !documentDataList.isEmpty()) {
                for (DocumentData data : documentDataList) {
                    if (data.getName().equalsIgnoreCase("numero")) {
                        EntityManager em = emf.createEntityManager();
                        em.getTransaction().begin();
                        ClientDataPK clientDataPK = new ClientDataPK(client.getIdUser(), data.getIdDocumentdata());
                        ClientData clientData = new ClientData();
                        clientData.setClientDataPK(clientDataPK);
                        clientData.setDocumentData(data);
                        clientData.setClient(client);
                        clientData.setValue(value);
                        clientData.setLastModification(Calendar.getInstance().getTime());
                        try {
                            clientDataController.create(clientData);
                        } catch (Exception ex) {
                            Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                }
            } else {
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                DocumentData identificationNumberData = new DocumentData();
                identificationNumberData.setName("numero");
                DataType dataType = dataTypeController.findByCode(VALUE_DATA_TYPE_CODE);
                identificationNumberData.setFkDataType(dataType);
                identificationNumberData.setRequired(1);
                identificationNumberData.setFkDocument(document);
                try {
                    documentDataController.create(identificationNumberData);
                } catch (Exception ex) {
                    Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                ClientDataPK clientDataPK = new ClientDataPK(client.getIdUser(), identificationNumberData.getIdDocumentdata());
                ClientData clientData = new ClientData();
                clientData.setClientDataPK(clientDataPK);
                clientData.setDocumentData(identificationNumberData);
                clientData.setClient(client);
                clientData.setValue(value);
                clientData.setLastModification(Calendar.getInstance().getTime());
                
                try {
                    clientDataController.create(clientData);
                } catch (Exception ex) {
                    Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                em.getTransaction().commit();
            }
        } else {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Document newDocument = new Document();
            newDocument.setName(name);
            newDocument.setDescription("Documento de identificación");
            newDocument.setMaxImageSize(300);
            newDocument.setCanRepeat(1);
            newDocument.setCanExpire(1);
            newDocument.setIsRequired(1);
            documentController.create(newDocument);
            DocumentData identificationNumberData = new DocumentData();
            identificationNumberData.setName("numero");
            DataType dataType = dataTypeController.findByCode(VALUE_DATA_TYPE_CODE);
            identificationNumberData.setFkDataType(dataType);
            identificationNumberData.setRequired(1);
            identificationNumberData.setFkDocument(document);
            
            try {
                documentDataController.create(identificationNumberData);
            } catch (Exception ex) {
                Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClientDataPK clientDataPK = new ClientDataPK(client.getIdUser(), identificationNumberData.getIdDocumentdata());
            ClientData clientData = new ClientData();
            clientData.setClientDataPK(clientDataPK);
            clientData.setDocumentData(identificationNumberData);
            clientData.setClient(client);
            clientData.setValue(value);
            clientData.setLastModification(Calendar.getInstance().getTime());
            
            try {
                clientDataController.create(clientData);
            } catch (Exception ex) {
                Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            em.getTransaction().commit();
            
        }
    }
    
    private void activeCaptureDataShowPane() {
        captureDataShowPane.setVisible(true);
    }
    
    private void initComponentsRegistPersonDialog(JDialog registPersonDialog) {
        expedientIncludeList = new javax.swing.JList();
        expedientIncludeRegistLabel = new javax.swing.JLabel();
        idenTypeSelectorLabel = new javax.swing.JLabel();
        identificationNumberLabel = new javax.swing.JLabel();
        nameRegistPersonLabel = new javax.swing.JLabel();
        nationSelectorRegistPersonLabel = new javax.swing.JLabel();
        registPersonButton = new javax.swing.JButton();
        cleanRegistPersonButton = new javax.swing.JButton();
        identificacionNumberTxt = new javax.swing.JTextField();
        idenTypeSelector = new javax.swing.JComboBox();
        nameRegistPersonTxt = new javax.swing.JTextField();
        verifyDocumentButton = new javax.swing.JButton();
        nationalityTxt = new javax.swing.JComboBox();
        registPersonTitle = new javax.swing.JLabel();
        
        registPersonDialog.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        registPersonDialog.setResizable(false);
        registPersonDialog.setTitle("Registrar Persona");
        
        idenTypeSelectorLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        idenTypeSelectorLabel.setForeground(new java.awt.Color(153, 153, 153));
        idenTypeSelectorLabel.setText("Tipo Identificación:");
        
        identificationNumberLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        identificationNumberLabel.setForeground(new java.awt.Color(153, 153, 153));
        identificationNumberLabel.setText("Número de Identificación:");
        
        nameRegistPersonLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameRegistPersonLabel.setForeground(new java.awt.Color(153, 153, 153));
        nameRegistPersonLabel.setText("Nombre:");
        
        nationSelectorRegistPersonLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nationSelectorRegistPersonLabel.setForeground(new java.awt.Color(153, 153, 153));
        nationSelectorRegistPersonLabel.setText("Nacionalidad:");
        
        registPersonButton.setText("Guardar");
        
        registPersonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registPersonAction(evt);
            }
        });
        
        cleanRegistPersonButton.setText("Limpiar");
        
        idenTypeSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Pasaporte", "Documento de Identidad"}));
        
        verifyDocumentButton.setText("Verificación de Documentos");
        
        nationalityTxt.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Venezuela", "Colombia", "Costa Rica"}));
        
        registPersonTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        registPersonTitle.setForeground(new java.awt.Color(153, 153, 153));
        registPersonTitle.setText("Registro de Persona");
        
        expedientIncludeRegistLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expedientIncludeRegistLabel.setForeground(new java.awt.Color(153, 153, 153));
        expedientIncludeRegistLabel.setText("Inlcuir Expediente:");
        
        List<Expedient> expedientList = this.expedientController.findExpedientEntities();
        expedientIncludeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        expedientIncludeList = new JList(new Vector<Expedient>(expedientList));
        expedientIncludeList.setVisibleRowCount(10);
        expedientIncludeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Expedient) {
                    ((JLabel) renderer).setText(((Expedient) value).getName());
                }
                return renderer;
            }
        });
        jScrollPane1.setViewportView(expedientIncludeList);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(registPersonDialog.getContentPane());
        registPersonDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(114, 114, 114)
                                        .addComponent(registPersonTitle))
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(idenTypeSelectorLabel)
                                                        .addGap(113, 113, 113)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(identificacionNumberTxt)
                                                                .addComponent(idenTypeSelector, 0, 147, Short.MAX_VALUE)
                                                                .addComponent(nameRegistPersonTxt)
                                                                .addComponent(nationalityTxt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jScrollPane1)))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(identificationNumberLabel)
                                                                                .addComponent(nameRegistPersonLabel)
                                                                                .addComponent(nationSelectorRegistPersonLabel)
                                                                                .addComponent(expedientIncludeRegistLabel))
                                                                        .addGap(18, 18, 18))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addComponent(cleanRegistPersonButton)
                                                                        .addGap(30, 30, 30)))
                                                        .addComponent(registPersonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(registPersonTitle)
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(idenTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(idenTypeSelectorLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(identificationNumberLabel)
                                .addComponent(identificacionNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nameRegistPersonLabel)
                                                .addComponent(nameRegistPersonTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nationSelectorRegistPersonLabel)
                                                .addComponent(nationalityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(30, 30, 30)
                                                        .addComponent(expedientIncludeRegistLabel)))
                                        .addGap(25, 77, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(cleanRegistPersonButton)
                                                .addComponent(registPersonButton))
                                        .addContainerGap())))
        );
        registPersonDialog.pack();
        registPersonDialog.setLocationRelativeTo(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponentsCreationDocumentDialog(JDialog creationDocumentDialog) {
        creationDocumentPane = new javax.swing.JPanel();
        createDocumentTitleLabel = new javax.swing.JLabel();
        nameDocumentLabel = new javax.swing.JLabel();
        descriptionNameLabel = new javax.swing.JLabel();
        expedientIncludeLabel = new javax.swing.JLabel();
        cleanDocumentButton = new javax.swing.JButton();
        nameDocumentTxt = new javax.swing.JTextField();
        descriptionDocumentTxt = new javax.swing.JTextField();
        creationDocumentScroll = new javax.swing.JScrollPane();
        ExpedientList = new javax.swing.JList();
        createDocumentButton = new javax.swing.JButton();
        maxImageSizeLabel = new javax.swing.JLabel();
        maxImageSizeTxt = new javax.swing.JTextField();
        documentRepeatLabel = new javax.swing.JLabel();
        documentRepeatCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        documentExpireCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        documentRequeridedCheckBox = new javax.swing.JCheckBox();
        
        creationDocumentDialog.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        creationDocumentDialog.setTitle("Crear Nuevo Documento");
        
        createDocumentTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        createDocumentTitleLabel.setForeground(new java.awt.Color(153, 153, 153));
        createDocumentTitleLabel.setText("Crear Nuevo Documento");
        createDocumentTitleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        createDocumentTitleLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        nameDocumentLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameDocumentLabel.setForeground(new java.awt.Color(153, 153, 153));
        nameDocumentLabel.setText("Nombre:");
        
        descriptionNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        descriptionNameLabel.setForeground(new java.awt.Color(153, 153, 153));
        descriptionNameLabel.setText("Descripción:");
        
        expedientIncludeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expedientIncludeLabel.setForeground(new java.awt.Color(153, 153, 153));
        expedientIncludeLabel.setText("Incluir a Expediente:");
        
        cleanDocumentButton.setText("Limpiar");
        
        List<Expedient> expedientList = this.expedientController.findExpedientEntities();
        ExpedientList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ExpedientList = new JList(new Vector<Expedient>(expedientList));
        ExpedientList.setVisibleRowCount(10);
        ExpedientList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Expedient) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((Expedient) value).getName());
                }
                return renderer;
            }
        });
        
        creationDocumentScroll.setViewportView(ExpedientList);
        
        createDocumentButton.setText("Crear");
        createDocumentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDocumentButtonActionPerformed(evt);
            }
        });
        
        maxImageSizeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        maxImageSizeLabel.setForeground(new java.awt.Color(153, 153, 153));
        maxImageSizeLabel.setText("Tamaño Máximo de Imágen:");
        
        documentRepeatLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        documentRepeatLabel.setForeground(new java.awt.Color(153, 153, 153));
        documentRepeatLabel.setText("Documento repetitivo:");
        
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Documento Expira:");
        
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Documento Requerido:");
        
        javax.swing.GroupLayout creationDocumentPaneLayout = new javax.swing.GroupLayout(creationDocumentPane);
        creationDocumentPane.setLayout(creationDocumentPaneLayout);
        creationDocumentPaneLayout.setHorizontalGroup(
                creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creationDocumentPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cleanDocumentButton)
                        .addGap(27, 27, 27)
                        .addComponent(createDocumentButton))
                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(createDocumentTitleLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(nameDocumentLabel)
                                                                .addComponent(descriptionNameLabel))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(nameDocumentTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(descriptionDocumentTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(expedientIncludeLabel)
                                                                .addComponent(maxImageSizeLabel)
                                                                .addComponent(documentRepeatLabel))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(documentRepeatCheckBox)
                                                                .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(creationDocumentScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                                                        .addComponent(maxImageSizeTxt))
                                                                .addComponent(documentExpireCheckBox)
                                                                .addComponent(documentRequeridedCheckBox))))
                                        .addGap(60, 60, 60))
                                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel2))
                                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        creationDocumentPaneLayout.setVerticalGroup(
                creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(creationDocumentPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(createDocumentTitleLabel)
                        .addGap(45, 45, 45)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nameDocumentLabel)
                                .addComponent(nameDocumentTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(descriptionDocumentTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(descriptionNameLabel))
                        .addGap(32, 32, 32)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(expedientIncludeLabel)
                                .addComponent(creationDocumentScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(maxImageSizeLabel)
                                .addComponent(maxImageSizeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(documentRepeatLabel)
                                .addComponent(documentRepeatCheckBox))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(documentExpireCheckBox))
                        .addGap(28, 28, 28)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(documentRequeridedCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addGroup(creationDocumentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cleanDocumentButton)
                                .addComponent(createDocumentButton)))
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(creationDocumentDialog.getContentPane());
        creationDocumentDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(creationDocumentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(creationDocumentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        
        creationDocumentDialog.pack();
        creationDocumentDialog.setLocationRelativeTo(null);
        
    }// </editor-fold>                        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponentsCreationExpedientDialog(JDialog dialogCreationExpedient) {
        creationExpedientPane = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameExpedientLabel = new javax.swing.JLabel();
        descriptionExpedientLabel = new javax.swing.JLabel();
        documentIncludeLabel = new javax.swing.JLabel();
        cleanExpedientButton = new javax.swing.JButton();
        nameExpedientTxt = new javax.swing.JTextField();
        descriptionExpedientTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        ExpedientDocumentList = new javax.swing.JList();
        createExpedientButton = new javax.swing.JButton();
        
        dialogCreationExpedient.setTitle("Crear Expediente");
        dialogCreationExpedient.setResizable(false);
        
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Crear Nuevo Expediente");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        nameExpedientLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameExpedientLabel.setForeground(new java.awt.Color(153, 153, 153));
        nameExpedientLabel.setText("Nombre:");
        
        descriptionExpedientLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        descriptionExpedientLabel.setForeground(new java.awt.Color(153, 153, 153));
        descriptionExpedientLabel.setText("Descripción:");
        
        documentIncludeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        documentIncludeLabel.setForeground(new java.awt.Color(153, 153, 153));
        documentIncludeLabel.setText("Incluir Documentos:");
        
        cleanExpedientButton.setText("Limpiar");
        List<Document> documentList = documentController.findDocumentEntities();
        ExpedientDocumentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ExpedientDocumentList = new JList(new Vector<Document>(documentList));
        ExpedientDocumentList.setVisibleRowCount(10);
        ExpedientDocumentList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Document) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((Document) value).getName());
                }
                return renderer;
            }
        });
        jScrollPane2.setViewportView(ExpedientDocumentList);
        
        createExpedientButton.setText("Crear");
        createExpedientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createExpedientButtonActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout creationExpedientPaneLayout = new javax.swing.GroupLayout(creationExpedientPane);
        creationExpedientPane.setLayout(creationExpedientPaneLayout);
        creationExpedientPaneLayout.setHorizontalGroup(
                creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                                        .addGap(105, 105, 105)
                                        .addComponent(jLabel2))
                                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                                                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(nameExpedientLabel)
                                                                .addComponent(descriptionExpedientLabel))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(nameExpedientTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(descriptionExpedientTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                                                        .addComponent(documentIncludeLabel)
                                                        .addGap(44, 44, 44)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creationExpedientPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cleanExpedientButton)
                        .addGap(27, 27, 27)
                        .addComponent(createExpedientButton))
        );
        creationExpedientPaneLayout.setVerticalGroup(
                creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(creationExpedientPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(45, 45, 45)
                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nameExpedientLabel)
                                .addComponent(nameExpedientTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(descriptionExpedientTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(descriptionExpedientLabel))
                        .addGap(32, 32, 32)
                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(documentIncludeLabel)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(creationExpedientPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cleanExpedientButton)
                                .addComponent(createExpedientButton)))
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialogCreationExpedient.getContentPane());
        dialogCreationExpedient.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(creationExpedientPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(creationExpedientPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        
        dialogCreationExpedient.pack();
        dialogCreationExpedient.setLocationRelativeTo(null);
    }// </editor-fold>                      

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        creationExpedientState = true;
        documentsReceptionState = false;
        
        templateCreationLayoutCardPane = new javax.swing.JPanel();
        templateContainerCardPane = new javax.swing.JPanel();
        templateCreationIcon = new javax.swing.JLabel();
        templateCreationLabel = new javax.swing.JLabel();
        templateCreationInfo = new javax.swing.JLabel();
        indexDocumentIconProcess = new javax.swing.JLabel();
        indexDocumentLabel = new javax.swing.JLabel();
        dataCaptureTable = new javax.swing.JTable();
        captureDataScrollPane = new javax.swing.JScrollPane();
        indextionContainerPane = new javax.swing.JPanel();
        dataCaptureScrollPane = new javax.swing.JScrollPane();
        infoProcessLayeredPane = new javax.swing.JLayeredPane();
        captureDataShowPane = new javax.swing.JPanel();
        mainPanelNavigation = new javax.swing.JPanel();
        scannerLabel = new javax.swing.JLabel();
        receptionContainerPane = new javax.swing.JPanel();
        removeUpLeftButton = new javax.swing.JButton();
        indexDocumentHelpLebel = new javax.swing.JLabel();
        indexDocumentButtonProcess = new javax.swing.JButton();
        nameExpedientIndexLabel = new javax.swing.JLabel();
        captureDataShowDocumentNameLabel = new javax.swing.JLabel();
        idenClientIndexLabel = new javax.swing.JLabel();
        receptionDocumentLabel = new javax.swing.JLabel();
        uploadImageLabel = new javax.swing.JLabel();
        indextionLayoutCardPane = new javax.swing.JPanel();
        documentReceptionNameClient = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        receptionIcon = new javax.swing.JLabel();
        selectExpedientLabel = new javax.swing.JLabel();
        documentReceptionLabel = new javax.swing.JLabel();
        receptionLayoutCardPane = new javax.swing.JPanel();
        expedientSelectorReception = new javax.swing.JComboBox();
        removeUpRightButton = new javax.swing.JButton();
        expedientTree = new javax.swing.JTree();
        documentValidationLabel = new javax.swing.JLabel();
        uploadImageButton = new javax.swing.JButton();
        removeDownRightButton = new javax.swing.JButton();
        removeDownLeftButton = new javax.swing.JButton();
        expedientFormTitleLabel = new javax.swing.JLabel();
        scannerButton = new javax.swing.JButton();
        layeredOperationalPane = new javax.swing.JLayeredPane();
        documentParameterPane = new javax.swing.JPanel();
        indexButton = new javax.swing.JButton();
        indexLabel = new javax.swing.JLabel();
        titleDocumentPaneLabel = new javax.swing.JLabel();
        nameDocumentPaneLabel = new javax.swing.JLabel();
        nameDocumentPaneTxt = new javax.swing.JTextField();
        genericDocumentPaneLabel = new javax.swing.JLabel();
        upSeparatorDocumentPane = new javax.swing.JSeparator();
        maxSizeDocumentPaneLabel = new javax.swing.JLabel();
        maxSizeDocumentPaneTxt = new javax.swing.JTextField();
        repeatDocumentPaneLabel = new javax.swing.JLabel();
        repeatDocumentPaneCheckBox = new javax.swing.JCheckBox();
        alertDocumentPaneLabel = new javax.swing.JLabel();
        middleSeparatorDocumentPane = new javax.swing.JSeparator();
        expireDocumentPaneLabel = new javax.swing.JLabel();
        expireDocumentPaneCheckBox = new javax.swing.JCheckBox();
        rulesDocumentPaneLabel = new javax.swing.JLabel();
        downSeparatorDocumentPane = new javax.swing.JSeparator();
        requeridDocumentPaneLabel = new javax.swing.JLabel();
        requeridDocumentPaneCheckBox = new javax.swing.JCheckBox();
        dataParameterPane = new javax.swing.JPanel();
        dataTypeDataParameterPaneLabel = new javax.swing.JLabel();
        nameDataParameterPaneLabel = new javax.swing.JLabel();
        dataTypeFieldParameterPaneLabel = new javax.swing.JLabel();
        requeridDataParameterPaneLabel = new javax.swing.JLabel();
        nameDataParameterPaneTxt = new javax.swing.JTextField();
        dataTypeDataParameterTxt = new javax.swing.JTextField();
        scannerPaneUpLeft = new PaintedPane();
        scannerPaneUpRight = new PaintedPane();
        scannerPaneDownRight = new PaintedPane();
        scannerPaneDownLeft = new PaintedPane();
        requiredDataParameterPaneCheckBox = new javax.swing.JCheckBox();
        mainBarMenu = new javax.swing.JMenuBar();
        mainTemplateOption = new javax.swing.JMenu();
        menuItemScannerSelection = new javax.swing.JMenuItem();
        mainMenuConfiguration = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mainDocumentOption = new javax.swing.JMenu();
        createNewDocumentItemMenuButton = new javax.swing.JMenuItem();
        modifyDocumentItemMenuButton = new javax.swing.JMenuItem();
        deleteDocumentItemMenuButton = new javax.swing.JMenuItem();
        mainConsultOption = new javax.swing.JMenu();
        receptionNameLabel = new javax.swing.JLabel();
        registPersonMenuItem = new javax.swing.JMenuItem();
        consultPersonMenuItem = new javax.swing.JMenuItem();
        indexProcessIsActive = false;
        setTitle("Sice Capture Demo");
        setResizable(false);
        
        selectExpedientLabel.setText("Expediente Seleccionado:");
        mainPanelNavigation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documentReceptionLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        documentReceptionLabel.setForeground(new java.awt.Color(153, 153, 153));
        documentReceptionLabel.setText("Recepción de Documentos");
        documentReceptionLabel.setVisible(false);
        indextionLayoutCardPane.setVisible(false);
        templateCreationLayoutCardPane.setVisible(true);
        List<Expedient> allExpedientsList = expedientController.findExpedientEntities();
        DefaultMutableTreeNode metaExpedient = new DefaultMutableTreeNode("Meta Expedientes");
        for (Expedient expedient : allExpedientsList) {
            DefaultMutableTreeNodeCustom expedientNode = new DefaultMutableTreeNodeCustom(expedient);
            if (expedient.getDocumentCollection() != null && !expedient.getDocumentCollection().isEmpty()) {
                for (Document document : expedient.getDocumentCollection()) {
                    DefaultMutableTreeNodeCustom documentNode = new DefaultMutableTreeNodeCustom(document);
                    List<DocumentData> documentDataList = documentDataController.findByDocument(document);
                    if (documentDataList != null && !documentDataList.isEmpty()) {
                        for (DocumentData documentData : documentDataList) {
                            DefaultMutableTreeNodeCustom documentDataNode = new DefaultMutableTreeNodeCustom(documentData);
                            documentNode.add(documentDataNode);
                        }
                    }
                    expedientNode.add(documentNode);
                }
            }
            metaExpedient.add(expedientNode);
        }
        
        expedientTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                selectedExpedientFromTree = null;
                selectedDocumentFromTree = null;
                
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) expedientTree.getLastSelectedPathComponent();
                if (node != null) {
                    if (node.getUserObject() instanceof Expedient) {
                        selectedExpedientFromTree = (Expedient) node.getUserObject();
                    } else if (node.getUserObject() instanceof Document) {
                        selectedDocumentFromTree = (Document) node.getUserObject();
                    }
                }
            }
        });
        
        expedientTree.setModel(
                new DefaultTreeModel(metaExpedient));
        expedientTree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        expedientTree.addMouseListener(new java.awt.event.MouseAdapter() {
            
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expedientTreeMouseClicked(evt);
            }
        }
        );
        assingIconsToBussinesTree(expedientTree);
        jScrollPane1.setViewportView(expedientTree);
        
        indexDocumentButtonProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                indexImagesAction(e);
            }
        });
        
        expedientSelectorReception.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                expedientClientSelectorAction(e);
            }
        });
        
        javax.swing.GroupLayout mainPanelNavigationLayout = new javax.swing.GroupLayout(mainPanelNavigation);
        mainPanelNavigation.setLayout(mainPanelNavigationLayout);
        mainPanelNavigationLayout.setHorizontalGroup(
                mainPanelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelNavigationLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        mainPanelNavigationLayout.setVerticalGroup(
                mainPanelNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelNavigationLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        
        expedientFormTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        expedientFormTitleLabel.setForeground(new java.awt.Color(153, 153, 153));
        expedientFormTitleLabel.setText("SiceCapture");
        
        scannerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/scnIco.png"))); // NOI18N
        scannerButton.setEnabled(false);
        scannerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerButtonActionPerformed(evt);
            }
        });
        
        layeredOperationalPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        
        titleDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        titleDocumentPaneLabel.setText("Documento");
        
        nameDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        nameDocumentPaneLabel.setText("Nombre:");
        
        genericDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        genericDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        genericDocumentPaneLabel.setText("Generales");
        
        maxSizeDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        maxSizeDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        maxSizeDocumentPaneLabel.setText("Tamaño Máximo Imágen:");
        
        repeatDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        repeatDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        repeatDocumentPaneLabel.setText("Documento Repetitivo ");
        
        alertDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        alertDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        alertDocumentPaneLabel.setText("Generación de Alarmas");
        
        expireDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expireDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        expireDocumentPaneLabel.setText("Documento Expira:");
        
        rulesDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rulesDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        rulesDocumentPaneLabel.setText("Reglas documentos requeridos");
        
        requeridDocumentPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        requeridDocumentPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        requeridDocumentPaneLabel.setText("Es requerido:");
        
        javax.swing.GroupLayout documentParameterPaneLayout = new javax.swing.GroupLayout(documentParameterPane);
        documentParameterPane.setLayout(documentParameterPaneLayout);
        documentParameterPaneLayout.setHorizontalGroup(
                documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addComponent(titleDocumentPaneLabel)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(nameDocumentPaneLabel)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(nameDocumentPaneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap())
                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                        .addComponent(genericDocumentPaneLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(upSeparatorDocumentPane))
                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(maxSizeDocumentPaneLabel)
                                                                .addComponent(repeatDocumentPaneLabel))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(repeatDocumentPaneCheckBox)
                                                                .addComponent(maxSizeDocumentPaneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addComponent(alertDocumentPaneLabel)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(middleSeparatorDocumentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addComponent(expireDocumentPaneLabel)
                                                        .addGap(63, 63, 63)
                                                        .addComponent(expireDocumentPaneCheckBox))
                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(rulesDocumentPaneLabel)
                                                                .addComponent(requeridDocumentPaneLabel))
                                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                        .addComponent(downSeparatorDocumentPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                                                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                                                        .addGap(6, 6, 6)
                                                                        .addComponent(requeridDocumentPaneCheckBox)
                                                                        .addGap(0, 250, Short.MAX_VALUE)))))
                                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        documentParameterPaneLayout.setVerticalGroup(
                documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(middleSeparatorDocumentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(documentParameterPaneLayout.createSequentialGroup()
                                        .addComponent(titleDocumentPaneLabel)
                                        .addGap(62, 62, 62)
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nameDocumentPaneLabel)
                                                .addComponent(nameDocumentPaneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(37, 37, 37)
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(genericDocumentPaneLabel)
                                                .addComponent(upSeparatorDocumentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(maxSizeDocumentPaneLabel)
                                                .addComponent(maxSizeDocumentPaneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(29, 29, 29)
                                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(repeatDocumentPaneCheckBox)
                                                .addComponent(repeatDocumentPaneLabel))
                                        .addGap(18, 18, 18)
                                        .addComponent(alertDocumentPaneLabel)))
                        .addGap(30, 30, 30)
                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(expireDocumentPaneLabel)
                                .addComponent(expireDocumentPaneCheckBox))
                        .addGap(18, 18, 18)
                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(rulesDocumentPaneLabel)
                                .addComponent(downSeparatorDocumentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(documentParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(requeridDocumentPaneCheckBox)
                                .addComponent(requeridDocumentPaneLabel))
                        .addContainerGap(94, Short.MAX_VALUE))
        );
        
        dataTypeDataParameterPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        dataTypeDataParameterPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        dataTypeDataParameterPaneLabel.setText("Tipo de Dato");
        
        nameDataParameterPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameDataParameterPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        nameDataParameterPaneLabel.setText("Nombre:");
        
        dataTypeFieldParameterPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dataTypeFieldParameterPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        dataTypeFieldParameterPaneLabel.setText("Tipo de Dato:");
        
        requeridDataParameterPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        requeridDataParameterPaneLabel.setForeground(new java.awt.Color(153, 153, 153));
        requeridDataParameterPaneLabel.setText("Es requerido:");
        
        javax.swing.GroupLayout dataParameterPaneLayout = new javax.swing.GroupLayout(dataParameterPane);
        dataParameterPane.setLayout(dataParameterPaneLayout);
        dataParameterPaneLayout.setHorizontalGroup(
                dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(dataParameterPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dataTypeDataParameterPaneLabel)
                                .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dataTypeFieldParameterPaneLabel)
                                        .addComponent(nameDataParameterPaneLabel)
                                        .addComponent(requeridDataParameterPaneLabel)))
                        .addGap(23, 23, 23)
                        .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nameDataParameterPaneTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                        .addComponent(dataTypeDataParameterTxt))
                                .addComponent(requiredDataParameterPaneCheckBox))
                        .addContainerGap(107, Short.MAX_VALUE))
        );
        dataParameterPaneLayout.setVerticalGroup(
                dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(dataParameterPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(dataTypeDataParameterPaneLabel)
                        .addGap(72, 72, 72)
                        .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nameDataParameterPaneLabel)
                                .addComponent(nameDataParameterPaneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dataTypeFieldParameterPaneLabel)
                                .addComponent(dataTypeDataParameterTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(dataParameterPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(requeridDataParameterPaneLabel)
                                .addComponent(requiredDataParameterPaneCheckBox))
                        .addContainerGap(274, Short.MAX_VALUE))
        );
        
        scannerPaneUpLeft.setMaximumSize(new java.awt.Dimension(240, 259));
        scannerPaneUpLeft.setMinimumSize(new java.awt.Dimension(240, 259));
        scannerPaneUpLeft.setPreferredSize(new java.awt.Dimension(240, 259));
        
        removeUpLeftButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/remove3.png"))); // NOI18N
        removeUpLeftButton.setBorder(null);
        removeUpLeftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeUpLeftButtonActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout scannerPaneUpLeftLayout = new javax.swing.GroupLayout(scannerPaneUpLeft);
        scannerPaneUpLeft.setLayout(scannerPaneUpLeftLayout);
        scannerPaneUpLeftLayout.setHorizontalGroup(
                scannerPaneUpLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scannerPaneUpLeftLayout.createSequentialGroup()
                        .addGap(0, 183, Short.MAX_VALUE)
                        .addComponent(removeUpLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        scannerPaneUpLeftLayout.setVerticalGroup(
                scannerPaneUpLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(scannerPaneUpLeftLayout.createSequentialGroup()
                        .addComponent(removeUpLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 220, Short.MAX_VALUE))
        );
        scannerPaneUpRight.setMaximumSize(new java.awt.Dimension(240, 259));
        scannerPaneUpRight.setMinimumSize(new java.awt.Dimension(240, 259));
        scannerPaneUpRight.setPreferredSize(new java.awt.Dimension(240, 259));
        
        removeUpRightButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/remove3.png"))); // NOI18N
        removeUpRightButton.setPreferredSize(new java.awt.Dimension(39, 39));
        removeUpRightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeUpRightButtonActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout scannerPaneUpRightLayout = new javax.swing.GroupLayout(scannerPaneUpRight);
        scannerPaneUpRight.setLayout(scannerPaneUpRightLayout);
        scannerPaneUpRightLayout.setHorizontalGroup(
                scannerPaneUpRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 240, Short.MAX_VALUE)
                .addGroup(scannerPaneUpRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scannerPaneUpRightLayout.createSequentialGroup()
                                .addGap(0, 197, Short.MAX_VALUE)
                                .addComponent(removeUpRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        scannerPaneUpRightLayout.setVerticalGroup(
                scannerPaneUpRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 259, Short.MAX_VALUE)
                .addGroup(scannerPaneUpRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scannerPaneUpRightLayout.createSequentialGroup()
                                .addComponent(removeUpRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 227, Short.MAX_VALUE)))
        );
        
        scannerPaneDownRight.setMaximumSize(new java.awt.Dimension(240, 259));
        scannerPaneDownRight.setMinimumSize(new java.awt.Dimension(240, 259));
        scannerPaneDownRight.setPreferredSize(new java.awt.Dimension(240, 259));
        
        removeDownRightButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/remove3.png"))); // NOI18N
        removeDownRightButton.setPreferredSize(new java.awt.Dimension(39, 39));
        removeDownRightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDownRightButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout scannerPaneDownRightLayout = new javax.swing.GroupLayout(scannerPaneDownRight);
        scannerPaneDownRight.setLayout(scannerPaneDownRightLayout);
        scannerPaneDownRightLayout.setHorizontalGroup(
                scannerPaneDownRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 240, Short.MAX_VALUE)
                .addGroup(scannerPaneDownRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scannerPaneDownRightLayout.createSequentialGroup()
                                .addGap(0, 176, Short.MAX_VALUE)
                                .addComponent(removeDownRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        scannerPaneDownRightLayout.setVerticalGroup(
                scannerPaneDownRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 259, Short.MAX_VALUE)
                .addGroup(scannerPaneDownRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scannerPaneDownRightLayout.createSequentialGroup()
                                .addComponent(removeDownRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 220, Short.MAX_VALUE)))
        );
        
        scannerPaneDownLeft.setMaximumSize(new java.awt.Dimension(240, 259));
        scannerPaneDownLeft.setMinimumSize(new java.awt.Dimension(240, 259));
        scannerPaneDownLeft.setPreferredSize(new java.awt.Dimension(240, 259));
        removeDownLeftButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/remove3.png")));
        removeDownLeftButton.setPreferredSize(new java.awt.Dimension(39, 39));
        removeDownLeftButton.setBorder(null);
        removeDownLeftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDownLeftButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout scannerPaneDownLeftLayout = new javax.swing.GroupLayout(scannerPaneDownLeft);
        scannerPaneDownLeft.setLayout(scannerPaneDownLeftLayout);
        scannerPaneDownLeftLayout.setHorizontalGroup(
                scannerPaneDownLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 240, Short.MAX_VALUE)
                .addGroup(scannerPaneDownLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scannerPaneDownLeftLayout.createSequentialGroup()
                                .addGap(0, 185, Short.MAX_VALUE)
                                .addComponent(removeDownLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        scannerPaneDownLeftLayout.setVerticalGroup(
                scannerPaneDownLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 259, Short.MAX_VALUE)
                .addGroup(scannerPaneDownLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scannerPaneDownLeftLayout.createSequentialGroup()
                                .addComponent(removeDownLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 220, Short.MAX_VALUE)))
        );
        
        captureDataShowDocumentNameLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        captureDataShowDocumentNameLabel.setForeground(new java.awt.Color(153, 153, 153));
        captureDataShowDocumentNameLabel.setText("Nombre Documento");
        
        dataCaptureScrollPane.setViewportView(dataCaptureTable);
        
        javax.swing.GroupLayout captureDataShowPaneLayout = new javax.swing.GroupLayout(captureDataShowPane);
        captureDataShowPane.setLayout(captureDataShowPaneLayout);
        captureDataShowPaneLayout.setHorizontalGroup(
                captureDataShowPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(captureDataShowPaneLayout.createSequentialGroup()
                        .addContainerGap(17, Short.MAX_VALUE)
                        .addGroup(captureDataShowPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, captureDataShowPaneLayout.createSequentialGroup()
                                        .addComponent(dataCaptureScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, captureDataShowPaneLayout.createSequentialGroup()
                                        .addComponent(captureDataShowDocumentNameLabel)
                                        .addGap(148, 148, 148))))
        );
        captureDataShowPaneLayout.setVerticalGroup(
                captureDataShowPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, captureDataShowPaneLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(captureDataShowDocumentNameLabel)
                        .addGap(18, 18, 18)
                        .addComponent(dataCaptureScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout layeredOperationalPaneLayout = new javax.swing.GroupLayout(layeredOperationalPane);
        layeredOperationalPane.setLayout(layeredOperationalPaneLayout);
        layeredOperationalPaneLayout.setHorizontalGroup(
                layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 481, Short.MAX_VALUE)
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(documentParameterPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dataParameterPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredOperationalPaneLayout.createSequentialGroup()
                                .addComponent(scannerPaneUpLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 241, Short.MAX_VALUE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layeredOperationalPaneLayout.createSequentialGroup()
                                .addGap(0, 235, Short.MAX_VALUE)
                                .addComponent(scannerPaneUpRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layeredOperationalPaneLayout.createSequentialGroup()
                                .addGap(0, 188, Short.MAX_VALUE)
                                .addComponent(scannerPaneDownRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredOperationalPaneLayout.createSequentialGroup()
                                .addComponent(scannerPaneDownLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 188, Short.MAX_VALUE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(captureDataShowPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layeredOperationalPaneLayout.setVerticalGroup(
                layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 545, Short.MAX_VALUE)
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(documentParameterPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dataParameterPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredOperationalPaneLayout.createSequentialGroup()
                                .addComponent(scannerPaneUpLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 279, Short.MAX_VALUE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layeredOperationalPaneLayout.createSequentialGroup()
                                .addComponent(scannerPaneUpRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 286, Short.MAX_VALUE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layeredOperationalPaneLayout.createSequentialGroup()
                                .addGap(0, 206, Short.MAX_VALUE)
                                .addComponent(scannerPaneDownRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layeredOperationalPaneLayout.createSequentialGroup()
                                .addGap(0, 207, Short.MAX_VALUE)
                                .addComponent(scannerPaneDownLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layeredOperationalPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(captureDataShowPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layeredOperationalPane.setLayer(documentParameterPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(dataParameterPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(scannerPaneUpLeft, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(scannerPaneUpRight, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(scannerPaneDownRight, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(scannerPaneDownLeft, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredOperationalPane.setLayer(captureDataShowPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        indexButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/index1.png"))); // NOI18N
        indexButton.setEnabled(false);
        indexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexProcessAction(evt);
            }
        });
        
        scannerLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        scannerLabel.setForeground(new java.awt.Color(153, 153, 153));
        scannerLabel.setText("Digitalizar");
        
        indexLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        indexLabel.setForeground(new java.awt.Color(153, 153, 153));
        indexLabel.setText("Indexar");
        
        uploadImageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/upload.png"))); // NOI18N
        uploadImageButton.setEnabled(false);
        uploadImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadImageAction(evt);
            }
        });
        uploadImageLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        uploadImageLabel.setForeground(new java.awt.Color(153, 153, 153));
        uploadImageLabel.setText("Subir Imagen");
        
        receptionLayoutCardPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        receptionLayoutCardPane.setMaximumSize(new java.awt.Dimension(407, 114));
        receptionLayoutCardPane.setMinimumSize(new java.awt.Dimension(407, 114));
        receptionLayoutCardPane.setPreferredSize(new java.awt.Dimension(407, 114));
        receptionLayoutCardPane.setLayout(new java.awt.CardLayout());
        
        receptionDocumentLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        receptionDocumentLabel.setForeground(new java.awt.Color(153, 153, 153));
        receptionDocumentLabel.setText("Recepción de Documentos");
        
        receptionNameLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        receptionNameLabel.setForeground(new java.awt.Color(153, 153, 153));
        receptionNameLabel.setText("Nombre:");
        
        documentReceptionNameClient.setText("Nombre Cliente");
        
        receptionIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/docReception.png"))); // NOI18N

        selectExpedientLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        selectExpedientLabel.setForeground(new java.awt.Color(153, 153, 153));
        selectExpedientLabel.setText("Seleccionar Expediente:");
        
        expedientSelectorReception.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        
        javax.swing.GroupLayout receptionContainerPaneLayout = new javax.swing.GroupLayout(receptionContainerPane);
        receptionContainerPane.setLayout(receptionContainerPaneLayout);
        receptionContainerPaneLayout.setHorizontalGroup(
                receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                        .addGroup(receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                                        .addComponent(selectExpedientLabel)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(expedientSelectorReception, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                                        .addComponent(receptionNameLabel)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(documentReceptionNameClient))))
                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                        .addComponent(receptionIcon)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(receptionDocumentLabel)
                                        .addGap(0, 153, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        receptionContainerPaneLayout.setVerticalGroup(
                receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                        .addGroup(receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(receptionIcon)
                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(receptionDocumentLabel)))
                        .addGap(7, 7, 7)
                        .addGroup(receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(receptionContainerPaneLayout.createSequentialGroup()
                                        .addGroup(receptionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(receptionNameLabel)
                                                .addComponent(documentReceptionNameClient))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(expedientSelectorReception, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(selectExpedientLabel))
                        .addGap(0, 12, Short.MAX_VALUE))
        );
        
        receptionLayoutCardPane.add(receptionContainerPane, "card2");
        
        receptionLayoutCardPane.add(receptionContainerPane, "card2");
        
        indextionLayoutCardPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        indextionLayoutCardPane.setLayout(new java.awt.CardLayout());
        
        indexDocumentIconProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/indextionProcess.png"))); // NOI18N

        indexDocumentLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        indexDocumentLabel.setForeground(new java.awt.Color(153, 153, 153));
        indexDocumentLabel.setText("Indexación de Documentos");
        
        indexDocumentHelpLebel.setText("Seleccione las imágenes a indexar");
        
        indexDocumentButtonProcess.setText("Indexar Imágenes");
        
        nameExpedientIndexLabel.setText("Nombre Expediente");
        
        idenClientIndexLabel.setText("Identificación del cliente");
        
        javax.swing.GroupLayout indextionContainerPaneLayout = new javax.swing.GroupLayout(indextionContainerPane);
        indextionContainerPane.setLayout(indextionContainerPaneLayout);
        indextionContainerPaneLayout.setHorizontalGroup(
                indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                        .addComponent(indexDocumentIconProcess)
                        .addGap(45, 45, 45)
                        .addComponent(indexDocumentLabel)
                        .addGap(0, 61, Short.MAX_VALUE))
                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                                        .addComponent(nameExpedientIndexLabel)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                                        .addGroup(indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(indexDocumentHelpLebel)
                                                .addComponent(idenClientIndexLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(indexDocumentButtonProcess)))
                        .addContainerGap())
        );
        indextionContainerPaneLayout.setVerticalGroup(
                indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                        .addGroup(indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(indexDocumentIconProcess)
                                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(indexDocumentLabel)))
                        .addGap(8, 8, 8)
                        .addComponent(nameExpedientIndexLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(indextionContainerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                                        .addComponent(indexDocumentHelpLebel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(idenClientIndexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(indextionContainerPaneLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(indexDocumentButtonProcess)))
                        .addContainerGap())
        );
        
        indextionLayoutCardPane.add(indextionContainerPane, "card2");
        
        templateCreationLayoutCardPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        templateCreationLayoutCardPane.setLayout(new java.awt.CardLayout());
        
        templateContainerCardPane.setMaximumSize(new java.awt.Dimension(403, 110));
        templateContainerCardPane.setMinimumSize(new java.awt.Dimension(403, 110));
        templateContainerCardPane.setPreferredSize(new java.awt.Dimension(403, 110));
        
        templateCreationIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/template.png"))); // NOI18N

        templateCreationLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        templateCreationLabel.setForeground(new java.awt.Color(153, 153, 153));
        templateCreationLabel.setText("Creación de plantillas");
        
        templateCreationInfo.setText("Utilice esta modalidad para crear expedientes");
        
        javax.swing.GroupLayout templateContainerCardPaneLayout = new javax.swing.GroupLayout(templateContainerCardPane);
        templateContainerCardPane.setLayout(templateContainerCardPaneLayout);
        templateContainerCardPaneLayout.setHorizontalGroup(
                templateContainerCardPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(templateContainerCardPaneLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(templateCreationIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(templateContainerCardPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(templateCreationInfo, 403, 403, 403)
                                .addComponent(templateCreationLabel))
                        .addGap(0, 48, Short.MAX_VALUE))
        );
        templateContainerCardPaneLayout.setVerticalGroup(
                templateContainerCardPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(templateContainerCardPaneLayout.createSequentialGroup()
                        .addGroup(templateContainerCardPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(templateContainerCardPaneLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(templateCreationIcon))
                                .addGroup(templateContainerCardPaneLayout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(templateCreationLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(templateCreationInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
        );
        
        templateCreationLayoutCardPane.add(templateContainerCardPane, "card2");
        
        javax.swing.GroupLayout infoProcessLayeredPaneLayout = new javax.swing.GroupLayout(infoProcessLayeredPane);
        infoProcessLayeredPane.setLayout(infoProcessLayeredPaneLayout);
        infoProcessLayeredPaneLayout.setHorizontalGroup(
                infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(receptionLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoProcessLayeredPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(indextionLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(templateCreationLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        infoProcessLayeredPaneLayout.setVerticalGroup(
                infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(infoProcessLayeredPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(receptionLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                .addGroup(infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoProcessLayeredPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(indextionLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(infoProcessLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoProcessLayeredPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(templateCreationLayoutCardPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(18, Short.MAX_VALUE)))
        );
        infoProcessLayeredPane.setLayer(receptionLayoutCardPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        infoProcessLayeredPane.setLayer(indextionLayoutCardPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        infoProcessLayeredPane.setLayer(templateCreationLayoutCardPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainTemplateOption.setBorder(null);
        mainTemplateOption.setText("Expedientes");
        mainTemplateOption.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mainTemplateOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainTemplateOptionActionPerformed(evt);
            }
        });
        
        this.mainMenuConfiguration.setBorder(null);
        mainMenuConfiguration.setText("Configuración");
        mainMenuConfiguration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        menuItemScannerSelection.setText("Seleccionar Escanner");
        menuItemScannerSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scannerSelectionAction(evt);
            }
        });
        mainMenuConfiguration.add(menuItemScannerSelection);
        
        mainBarMenu.add(mainMenuConfiguration);
        
        jMenuItem1.setText("Crear Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mainTemplateOption.add(jMenuItem1);
        
        jMenuItem2.setText("Modificar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mainTemplateOption.add(jMenuItem2);
        
        jMenuItem3.setText("Eliminar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mainTemplateOption.add(jMenuItem3);
        
        mainBarMenu.add(mainTemplateOption);
        
        mainDocumentOption.setBorder(null);
        mainDocumentOption.setText("Documentos");
        mainDocumentOption.setFocusable(false);
        mainDocumentOption.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        createNewDocumentItemMenuButton.setText("Crear Nuevo");
        createNewDocumentItemMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewDocumentItemMenuButtonActionPerformed(evt);
            }
        });
        mainDocumentOption.add(createNewDocumentItemMenuButton);
        
        modifyDocumentItemMenuButton.setText("Modificar");
        mainDocumentOption.add(modifyDocumentItemMenuButton);
        
        deleteDocumentItemMenuButton.setText("Eliminar");
        mainDocumentOption.add(deleteDocumentItemMenuButton);
        
        mainBarMenu.add(mainDocumentOption);
        
        mainConsultOption.setBorder(null);
        mainConsultOption.setText("Consultas & Reportes");
        mainConsultOption.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        registPersonMenuItem.setText("Registrar Persona");
        registPersonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registPersonMenuItemActionPerformed(evt);
            }
        });
        mainConsultOption.add(registPersonMenuItem);
        
        consultPersonMenuItem.setText("Consultar Personas");
        consultPersonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultPersonMenuItemActionPerformed(evt);
            }
        });
        mainConsultOption.add(consultPersonMenuItem);
        mainBarMenu.add(mainConsultOption);
        setJMenuBar(mainBarMenu);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(expedientFormTitleLabel)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(infoProcessLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addComponent(uploadImageLabel)
                                                        .addGap(44, 44, 44)
                                                        .addComponent(scannerLabel)
                                                        .addGap(55, 55, 55)
                                                        .addComponent(indexLabel))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(uploadImageButton)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(scannerButton)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(indexButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(mainPanelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(layeredOperationalPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(uploadImageButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(indexButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addComponent(scannerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(scannerLabel)
                                                .addComponent(indexLabel)
                                                .addComponent(uploadImageLabel)))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(expedientFormTitleLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoProcessLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(layeredOperationalPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mainPanelNavigation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );
        receptionLayoutCardPane.setVisible(false);
        Dimension mainPanesDimension = new Dimension(480, 518);
        Dimension mainWindow = new Dimension(915, 780);
        layeredOperationalPane.setMaximumSize(mainPanesDimension);
        layeredOperationalPane.setMinimumSize(mainPanesDimension);
        layeredOperationalPane.setPreferredSize(mainPanesDimension);
        this.setMaximumSize(mainWindow);
        this.setPreferredSize(mainWindow);
        this.setMinimumSize(mainWindow);
        setLocationByPlatform(true);
        pack();
    }// </editor-fold>                        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponentsCreationDataDialog(JDialog dialogCreationData) {
        
        creationDataPane = new javax.swing.JPanel();
        createDataTitleLabel = new javax.swing.JLabel();
        dataDocumentLabel = new javax.swing.JLabel();
        nameDataField = new javax.swing.JLabel();
        dataTypeLabel = new javax.swing.JLabel();
        cleanDataButton = new javax.swing.JButton();
        nameDataTxt = new javax.swing.JTextField();
        createDataButton = new javax.swing.JButton();
        selectedDocumentLabel = new javax.swing.JLabel();
        dataTypeSelector = new javax.swing.JComboBox();
        isRequiredDataLabel = new javax.swing.JLabel();
        isRequiredDataCheckBox = new javax.swing.JCheckBox();
        if (selectedDocument != null) {
            selectedDocumentLabel.setText(selectedDocument.getName());
        }
        dialogCreationData.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        dialogCreationData.setTitle("Agregar Nuevo Dato");
        dialogCreationData.setResizable(false);
        
        createDataTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        createDataTitleLabel.setForeground(new java.awt.Color(153, 153, 153));
        createDataTitleLabel.setText("Agregar Nuevo Dato");
        createDataTitleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        createDataTitleLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        
        dataDocumentLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dataDocumentLabel.setForeground(new java.awt.Color(153, 153, 153));
        dataDocumentLabel.setText("Documento:");
        
        nameDataField.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nameDataField.setForeground(new java.awt.Color(153, 153, 153));
        nameDataField.setText("Nombre:");
        
        dataTypeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dataTypeLabel.setForeground(new java.awt.Color(153, 153, 153));
        dataTypeLabel.setText("Tipo de dato:");
        
        cleanDataButton.setText("Limpiar");
        cleanDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanDataButtonActionPerformed(evt);
            }
        });
        
        createDataButton.setText("Crear");
        createDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataButtonActionPerformed(evt);
            }
        });
        
        List<DataType> dataTypeList = dataTypeController.findDataTypeEntities();
        for (DataType dataType : dataTypeList) {
            dataTypeSelector.addItem(dataType);
        }
        
        dataTypeSelector.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof DataType) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((DataType) value).getName());
                }
                return renderer;
            }
        });
        
        isRequiredDataLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        isRequiredDataLabel.setForeground(new java.awt.Color(153, 153, 153));
        isRequiredDataLabel.setText("Es requerido:");
        
        javax.swing.GroupLayout creationDataPaneLayout = new javax.swing.GroupLayout(creationDataPane);
        creationDataPane.setLayout(creationDataPaneLayout);
        creationDataPaneLayout.setHorizontalGroup(
                creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creationDataPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cleanDataButton)
                        .addGap(27, 27, 27)
                        .addComponent(createDataButton))
                .addGroup(creationDataPaneLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(createDataTitleLabel)
                        .addContainerGap(138, Short.MAX_VALUE))
                .addGroup(creationDataPaneLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dataDocumentLabel)
                                .addComponent(nameDataField)
                                .addComponent(dataTypeLabel)
                                .addComponent(isRequiredDataLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(isRequiredDataCheckBox)
                                .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nameDataTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                        .addComponent(selectedDocumentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dataTypeSelector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        creationDataPaneLayout.setVerticalGroup(
                creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(creationDataPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(createDataTitleLabel)
                        .addGap(45, 45, 45)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dataDocumentLabel)
                                .addComponent(selectedDocumentLabel))
                        .addGap(35, 35, 35)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nameDataTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nameDataField))
                        .addGap(31, 31, 31)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(dataTypeLabel)
                                .addComponent(dataTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(isRequiredDataLabel)
                                .addComponent(isRequiredDataCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addGroup(creationDataPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cleanDataButton)
                                .addComponent(createDataButton)))
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialogCreationData.getContentPane());
        dialogCreationData.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(creationDataPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(creationDataPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        
        dialogCreationData.pack();
        dialogCreationData.setLocationRelativeTo(null);
    }// </editor-fold>  

    private void scannerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
    }
    
    private void captureProcessAction(java.awt.event.ActionEvent evt) {
        boolean validProcess = true;
        
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        for (Object object : componentList) {
            String value = null;
            Integer id = 0;
            if (object instanceof JTextField) {
                JTextField textField = (JTextField) object;
                id = (Integer) textField.getClientProperty("id");
                value = textField.getText();
            } else if (object instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) object;
                id = (Integer) dateChooser.getClientProperty("id");
                value = dt.format(dateChooser.getDate());
            }
            if (value == null || "".equals(value.trim())) {
                validProcess = false;
            }
            
            EntityManager em = emf.createEntityManager();
            DocumentData data = documentDataController.findDocumentData(id);
            ClientDataPK clientDataPK = new ClientDataPK();
            clientDataPK.setFkClient(expedientClientInProcess.getClient().getIdUser());
            clientDataPK.setFkDocumentData(data.getIdDocumentdata());
            ClientData duplicateControl = clientDataController.findClientData(clientDataPK);
            em.getTransaction().begin();
            if (duplicateControl != null) {
                duplicateControl.setValue(value);
                duplicateControl.setLastModification(currentDate);
                try {
                    clientDataController.edit(duplicateControl);
                } catch (Exception ex) {
                    Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ClientData clientData = new ClientData();
                clientData.setClientDataPK(clientDataPK);
                clientData.setClient(expedientClientInProcess.getClient());
                clientData.setLastModification(currentDate);
                clientData.setValue(value);
                clientData.setDocumentData(data);
                try {
                    clientDataController.create(clientData);
                } catch (Exception ex) {
                    Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            em.getTransaction().commit();
            captureDataDialog.setVisible(false);
            
        }
    }
    
    private void initDocumentIncludeDialog() {
        inclusionExpedienteScrollpane = new javax.swing.JScrollPane();
        listExpedientInclusion = new javax.swing.JList();
        inclusionExpedientButton = new javax.swing.JButton();
        inclusionExpedientLabel = new javax.swing.JLabel();
        includeDocumentDialog.setTitle("Inclusión de Documentos");
        includeDocumentDialog.setResizable(false);
        List<Document> documentList = this.documentController.findDocumentEntities();
        List<Document> documentTemp = new ArrayList<>();
        documentTemp = (List<Document>) selectedExpedientInCreationPanel.getDocumentCollection();
        if (!documentTemp.isEmpty()) {
            documentList.removeAll(documentTemp);
        }
        listExpedientInclusion.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listExpedientInclusion = new JList(new Vector<Document>(documentList));
        listExpedientInclusion.setVisibleRowCount(10);
        listExpedientInclusion.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Expedient) {
                    ((JLabel) renderer).setText(((Document) value).getName());
                }
                return renderer;
            }
        });
        inclusionExpedienteScrollpane.setViewportView(listExpedientInclusion);
        inclusionExpedientButton.setText("Incluir");
        inclusionExpedientLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        inclusionExpedientLabel.setForeground(new java.awt.Color(153, 153, 153));
        inclusionExpedientLabel.setText("Inclusión de Documentos");
        inclusionExpedientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                List<Document> selectedDocumentsForInclude = new ArrayList<>();
                List selectDocumentList = listExpedientInclusion.getSelectedValuesList();
                if (selectDocumentList != null && !selectDocumentList.isEmpty()) {
                    for (Object documentObject : selectDocumentList) {
                        Document document = (Document) documentObject;
                        selectedDocumentsForInclude.add(document);
                    }
                }
                if (!selectedDocumentsForInclude.isEmpty()) {
                    EntityManager em = emf.createEntityManager();
                    em.getTransaction().begin();
                    selectedExpedientInCreationPanel.getDocumentCollection().addAll(selectedDocumentsForInclude); //                List selectedExpedientList = listExpedientInclusion.getSelectedValuesList();
                    try {
                        expedientController.edit(selectedExpedientFromTree);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    em.getTransaction().commit();
                }
                refreshExpedientTree();
                includeDocumentDialog.setVisible(false);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(includeDocumentDialog.getContentPane());
        includeDocumentDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inclusionExpedienteScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(inclusionExpedientLabel)
                                        .addGap(16, 16, 16))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(inclusionExpedientButton)
                                        .addGap(93, 93, 93)))
                        .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(inclusionExpedientLabel)
                        .addGap(18, 18, 18)
                        .addComponent(inclusionExpedienteScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(inclusionExpedientButton)
                        .addContainerGap())
        );
        
        includeDocumentDialog.pack();
        includeDocumentDialog.setLocationRelativeTo(this);
        
    }
    
    private void initIncludeExpedientDialog() {
        inclusionExpedienteScrollpane = new javax.swing.JScrollPane();
        listExpedientInclusion = new javax.swing.JList();
        inclusionExpedientButton = new javax.swing.JButton();
        inclusionExpedientLabel = new javax.swing.JLabel();
        includeExpedientDialog.setTitle("Inclusión de Expedientes");
        includeExpedientDialog.setResizable(false);
        List<Expedient> expedientList = this.expedientController.findExpedientEntities();
        List<Expedient> expedientTemp = new ArrayList<>();
        List<ExpedientClient> allExpedientsListOfClient = expedientClientController.findExpedientClientByClient(expedientClientInProcess.getClient());
        for (ExpedientClient expedientClient : allExpedientsListOfClient) {
            expedientTemp.add(expedientClient.getExpedient());
        }
        if (!expedientTemp.isEmpty()) {
            expedientList.removeAll(expedientTemp);
        }
        listExpedientInclusion.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listExpedientInclusion = new JList(new Vector<Expedient>(expedientList));
        listExpedientInclusion.setVisibleRowCount(10);
        listExpedientInclusion.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Expedient) {
                    ((JLabel) renderer).setText(((Expedient) value).getName());
                }
                return renderer;
            }
        });
        inclusionExpedienteScrollpane.setViewportView(listExpedientInclusion);
        inclusionExpedientButton.setText("Incluir");
        inclusionExpedientLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        inclusionExpedientLabel.setForeground(new java.awt.Color(153, 153, 153));
        inclusionExpedientLabel.setText("Inclusión de Expedientes");
        inclusionExpedientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                List selectedExpedientList = listExpedientInclusion.getSelectedValuesList();
                if (selectedExpedientList != null && !selectedExpedientList.isEmpty()) {
                    EntityManager em = emf.createEntityManager();
                    for (Object expedientObject : selectedExpedientList) {
                        em.getTransaction().begin();
                        Expedient expedient = (Expedient) expedientObject;
                        ExpedientClientPK expedientPK = new ExpedientClientPK();
                        expedientPK.setFkClient(expedientClientInProcess.getClient().getIdUser());
                        expedientPK.setFkExpedient(expedient.getIdExpedient());
                        ExpedientClient expedientClient = new ExpedientClient();
                        expedientClient.setClient(expedientClientInProcess.getClient());
                        expedientClient.setExpedient(expedient);
                        expedientClient.setExpedientClientPK(expedientPK);
                        expedientClient.setLastModification(Calendar.getInstance().getTime());
                        try {
                            expedientClientController.create(expedientClient);
                        } catch (Exception ex) {
                            Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        em.getTransaction().commit();
                    }
                }
                refreshExpedientsOfClient(expedientClientInProcess);
                includeExpedientDialog.setVisible(false);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(includeExpedientDialog.getContentPane());
        includeExpedientDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(inclusionExpedienteScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(inclusionExpedientLabel)
                                        .addGap(16, 16, 16))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(inclusionExpedientButton)
                                        .addGap(93, 93, 93)))
                        .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(inclusionExpedientLabel)
                        .addGap(18, 18, 18)
                        .addComponent(inclusionExpedienteScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(inclusionExpedientButton)
                        .addContainerGap())
        );
        
        includeExpedientDialog.pack();
        includeExpedientDialog.setLocationRelativeTo(null);
    }
    
    private void initShowCaptureData() {
        documentDataListCurrentCaptureProcess = new ArrayList<>();
        documentDataListCurrentCaptureProcess = documentDataController.findByDocument(selectedDocumentFromTree);
        List<ClientData> clientDataList = new ArrayList<>();
        for (DocumentData documentData : documentDataListCurrentCaptureProcess) {
            ClientDataPK clientDataPK = new ClientDataPK();
            clientDataPK.setFkClient(expedientClientInProcess.getClient().getIdUser());
            clientDataPK.setFkDocumentData(documentData.getIdDocumentdata());
            ClientData duplicateControl = clientDataController.findClientData(clientDataPK);
            if (duplicateControl != null) {
                clientDataList.add(duplicateControl);
            }
        }
        if (!clientDataList.isEmpty()) {
            TableModelCustomClientData tblModelClientData = new TableModelCustomClientData(clientDataList);
            sorter = new TableRowSorter<TableModel>(tblModelClientData);
            dataCaptureTable.setModel(tblModelClientData);
            dataCaptureTable.setRowSorter(sorter);
            dataCaptureScrollPane.setViewportView(dataCaptureTable);
            captureDataShowPane.setVisible(true);
        } else {
            captureDataShowPane.setVisible(false);
        }
    }
    
    private void initCreationCaptureDataDialog(JDialog dialog, List componentList, Map<Integer, JLabel> jLabelMap) {
        JPanel form = new JPanel();
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.setContentPane(form);
        form.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();
        for (Object object : componentList) {
            if (object instanceof JTextField) {
                JTextField textField = (JTextField) object;
                Integer idData = Integer.valueOf(textField.getClientProperty("id").toString());
                formUtility.addLabel(jLabelMap.get(idData), form);
                formUtility.addLastField(textField, form);
            } else if (object instanceof JDateChooser) {
                JDateChooser dataChooser = (JDateChooser) object;
                Integer idData = Integer.valueOf(dataChooser.getClientProperty("id").toString());
                formUtility.addLabel(jLabelMap.get(idData), form);
                formUtility.addLastField(dataChooser, form);
            }
            
        }
        JButton captureButton = new JButton("Guardar");
        formUtility.addLastField(captureButton, form);
        dialog.setPreferredSize(new Dimension(380, 200));
        dialog.pack();
        dialog.setResizable(false);
        dialog.setTitle("Capturar Datos");
        dialog.setLocationRelativeTo(null);
        captureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captureProcessAction(evt);
            }
        });
    }
    
    private void expedientClientSelectorAction(java.awt.event.ActionEvent evt) {
        ExpedientClient expedientClient = (ExpedientClient) this.expedientSelectorReception.getSelectedItem();
        this.expedientClientInProcess = expedientClient;
        displayDocumentByExpedient(expedientClient);
        checkTreeManager.automaticSelectionByClient();
    }
    
    private void indexImagesAction(java.awt.event.ActionEvent evt) {
        //You have to choose one image at least
        boolean firstCondition = true;
        //You have to choose one document
        boolean secondCondition = true;
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        if (!this.scannerPaneDownLeft.isImageSelected() && !scannerPaneDownRight.isImageSelected() && !scannerPaneUpRight.isImageSelected() && !scannerPaneUpLeft.isImageSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar almenos una Imagen para indexar");
            firstCondition = false;
        } else if (this.selectedDocumentFromTree == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el documento donde se quiere indexar las imagenes");
            secondCondition = false;
        }
        if (firstCondition && secondCondition) {
            captureDataDialog = new JDialog();
            documentDataListCurrentCaptureProcess = documentDataController.findByDocument(selectedDocumentFromTree);
            componentList = new ArrayList<>();
            Map<Integer, JLabel> labelMap = new HashMap<>();
            if (documentDataListCurrentCaptureProcess != null && !documentDataListCurrentCaptureProcess.isEmpty()) {
                for (DocumentData documentData : documentDataListCurrentCaptureProcess) {
                    ClientDataPK clientDataPK = new ClientDataPK();
                    clientDataPK.setFkClient(expedientClientInProcess.getClient().getIdUser());
                    clientDataPK.setFkDocumentData(documentData.getIdDocumentdata());
                    ClientData duplicateControl = clientDataController.findClientData(clientDataPK);
                    if (duplicateControl != null) {
                        if (documentData.getFkDataType().getCode().equalsIgnoreCase(VALUE_DATA_TYPE_CODE)) {
                            JTextField textField = new JTextField();
                            textField.setText(duplicateControl.getValue());
                            textField.putClientProperty("id", documentData.getIdDocumentdata());
                            componentList.add(textField);
                        } else if (documentData.getFkDataType().getCode().equalsIgnoreCase(DATE_DATA_TYPE_CODE)) {
                            JDateChooser dataChooser = new JDateChooser();
                            Date dateValue = null;
                            try {
                                dateValue = dt.parse(duplicateControl.getValue());
                            } catch (ParseException ex) {
                                Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            dataChooser.setDateFormatString("dd/MM/yyyy");
                            dataChooser.setDate(dateValue);
                            dataChooser.putClientProperty("id", documentData.getIdDocumentdata());
                            componentList.add(dataChooser);
                        }
                    } else {
                        if (documentData.getFkDataType().getCode().equalsIgnoreCase(VALUE_DATA_TYPE_CODE)) {
                            JTextField textField = new JTextField();
                            textField.putClientProperty("id", documentData.getIdDocumentdata());
                            componentList.add(textField);
                        } else if (documentData.getFkDataType().getCode().equalsIgnoreCase(DATE_DATA_TYPE_CODE)) {
                            JDateChooser dataChooser = new JDateChooser();
                            dataChooser.setDateFormatString("dd/MM/yyyy");
                            dataChooser.putClientProperty("id", documentData.getIdDocumentdata());
                            componentList.add(dataChooser);
                        }
                    }
                    labelMap.put(documentData.getIdDocumentdata(), new JLabel(documentData.getName()));
                }
                captureDataDialog.setModal(true);
                initCreationCaptureDataDialog(captureDataDialog, componentList, labelMap);
                captureDataDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Debe crear datos de captura para el documento seleccionado");
            }
        }
    }
    
    public void assingIconsToBussinesTree(JTree bussinesTree) {
        bussinesTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
                if (nodo.getUserObject() instanceof Expedient) {
                    java.net.URL imgURL = getClass().getResource(expedientIconPath2);
                    ImageIcon icon = new ImageIcon(imgURL, "Expedient Icon");
                    if (imgURL != null) {
                        this.setIcon(icon);
                    } else {
                        System.err.println("Couldn't find file: " + expedientIconPath2);
                        return null;
                    }
                } else if (nodo.getUserObject() instanceof String) {
                    java.net.URL imgURL = getClass().getResource(metaIconPath);
                    ImageIcon icon = new ImageIcon(imgURL, "Meta Icon");
                    if (imgURL != null) {
                        this.setIcon(icon);
                    } else {
                        System.err.println("Couldn't find file: " + metaIconPath);
                        return null;
                    }
                } else if (nodo.getUserObject() instanceof DocumentData) {
                    java.net.URL imgDateURL = getClass().getResource(dataDateIconPath);
                    java.net.URL imgValueURL = getClass().getResource(dataValueIconPath);
                    ImageIcon iconDate = new ImageIcon(imgDateURL, "Data Date Icon");
                    ImageIcon iconValue = new ImageIcon(imgValueURL, "Data Value Icon");
                    DocumentData data = (DocumentData) nodo.getUserObject();
                    if (data.getFkDataType().getCode().equalsIgnoreCase(DATE_DATA_TYPE_CODE)) {
                        if (imgDateURL != null) {
                            this.setIcon(iconDate);
                        } else {
                            System.err.println("Couldn't find file: " + imgDateURL);
                            return null;
                        }
                    } else if (data.getFkDataType().getCode().equalsIgnoreCase(VALUE_DATA_TYPE_CODE)) {
                        if (imgValueURL != null) {
                            this.setIcon(iconValue);
                        } else {
                            System.err.println("Couldn't find file: " + imgValueURL);
                            return null;
                        }
                    }
                    
                } else if (nodo.getUserObject() instanceof Document) {
                    java.net.URL imgURL = getClass().getResource(documentIconPath);
                    ImageIcon icon = new ImageIcon(imgURL, "Document Icon");
                    if (imgURL != null) {
                        this.setIcon(icon);
                    } else {
                        System.err.println("Couldn't find file: " + documentIconPath);
                        return null;
                    }
                }
                return this;
            }
        });
    }
    
    private void initControllers() {
        expedientController = new ExpedientJpaController(emf);
        expedientClientController = new ExpedientClientJpaController(emf);
        documentController = new DocumentJpaController(emf);
        dataTypeController = new DataTypeJpaController(emf);
        clientController = new ClientJpaController(emf);
        clientDataController = new ClientDataJpaController(emf);
        documentDataController = new DocumentDataJpaController(emf);
    }
    
    private void createExpedientButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean validProcess = true;
        if ("".equals(nameExpedientTxt.getText().trim())) {
            validProcess = false;
        } else if ("".equals(descriptionExpedientTxt.getText().trim())) {
            validProcess = false;
        }
        if (validProcess) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Expedient expedient = new Expedient();
            expedient.setName(nameExpedientTxt.getText().trim());
            expedient.setDescription(descriptionExpedientTxt.getText().trim());
            List<Document> selectedDocuments = ExpedientDocumentList.getSelectedValuesList();
            expedient.setDocumentCollection(selectedDocuments);
            expedientController.create(expedient);
            em.getTransaction().commit();
            refreshExpedientTree();
        }
    }
    
    public void refreshExpedientTree() {
        List<Expedient> allExpedientsList = expedientController.findExpedientEntities();
        DefaultMutableTreeNode metaExpedient = new DefaultMutableTreeNode("Meta Expedientes");
        for (Expedient expedient : allExpedientsList) {
            DefaultMutableTreeNodeCustom expedientNode = new DefaultMutableTreeNodeCustom(expedient);
            if (expedient.getDocumentCollection() != null && !expedient.getDocumentCollection().isEmpty()) {
                for (Document document : expedient.getDocumentCollection()) {
                    DefaultMutableTreeNodeCustom documentNode = new DefaultMutableTreeNodeCustom(document);
                    List<DocumentData> documentDataList = documentDataController.findByDocument(document);
                    if (documentDataList != null && !documentDataList.isEmpty()) {
                        for (DocumentData documentData : documentDataList) {
                            DefaultMutableTreeNodeCustom documentDataNode = new DefaultMutableTreeNodeCustom(documentData);
                            documentNode.add(documentDataNode);
                        }
                    }
                    expedientNode.add(documentNode);
                }
            }
            metaExpedient.add(expedientNode);
        }
        expedientTree.setModel(new DefaultTreeModel(metaExpedient));
    }
    
    private void initDocumentList() {
        List<Document> documentList = documentController.findDocumentEntities();
        ExpedientDocumentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ExpedientDocumentList = new JList(new Vector<Document>(documentList));
        ExpedientDocumentList.setVisibleRowCount(3);
        ExpedientDocumentList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Document) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((Document) value).getName());
                }
                return renderer;
            }
        });
    }
    
    private void scannerSelectionAction(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        scannerBackground.selectScanner();
    }
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        JDialog creationExpedientDialog = new JDialog();
        initComponentsCreationExpedientDialog(creationExpedientDialog);
        creationExpedientDialog.setModal(true);
        creationExpedientDialog.setVisible(true);
    }
    
    private void createDocumentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean validProcess = true;
        boolean validSize = true;
        
        if ("".equals(nameDocumentTxt.getText().trim())) {
            validProcess = false;
        } else if ("".equals(descriptionDocumentTxt.getText().trim())) {
            validProcess = false;
        } else if ("".equals(this.maxImageSizeTxt.getText().trim())) {
            validProcess = false;
        }
        
        if (validProcess) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Document document = new Document();
            document.setName(nameDocumentTxt.getText().trim());
            document.setDescription(descriptionDocumentTxt.getText().trim());
            document.setMaxImageSize(Integer.parseInt(maxImageSizeTxt.getText()));
            
            if (documentRepeatCheckBox.isSelected()) {
                document.setCanRepeat(1);
            } else {
                document.setCanRepeat(0);
            }
            if (documentExpireCheckBox.isSelected()) {
                document.setCanExpire(1);
            } else {
                document.setCanExpire(0);
            }
            if (documentRequeridedCheckBox.isSelected()) {
                document.setIsRequired(1);
            } else {
                document.setIsRequired(0);
            }
            
            List<Expedient> selectedExpedients = new ArrayList<>();
            List selectedExpedientList = ExpedientList.getSelectedValuesList();
            if (selectedExpedientList != null && !selectedExpedientList.isEmpty()) {
                for (Object expedientObject : selectedExpedientList) {
                    Expedient expedient = (Expedient) expedientObject;
                    selectedExpedients.add(expedient);
                }
            }
            
            document.setExpedientCollection(selectedExpedients);
            documentController.create(document);
            em.getTransaction().commit();
            refreshExpedientTree();
        }
    }
    
    private void removeUpRightButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.scannerPaneUpRight.removeImage();
    }
    
    private void removeUpLeftButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.scannerPaneUpLeft.removeImage();
    }
    
    private void removeDownRightButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.scannerPaneDownRight.removeImage();
    }
    
    private void removeDownLeftButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.scannerPaneDownLeft.removeImage();
    }
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void mainTemplateOptionActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void indexProcessAction(java.awt.event.ActionEvent evt) {
        indexProcessIsActive = !indexProcessIsActive;
        if (indexProcessIsActive) {
            this.desactiveCaptureDataShowPane();
            this.desactiveDocumentDataParameterPane();
            this.desactiveDocumentParameterPane();
            indexButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/indexout.png"))); // NOI18N
            scannerButton.setEnabled(true);
            uploadImageButton.setEnabled(true);
            creationExpedientState = false;
            documentsReceptionState = false;
            this.nameExpedientIndexLabel.setText(expedientClientInProcess.getExpedient().getName());
            indexDocumentHelpLebel.setText(expedientClientInProcess.getClient().getName());
            idenClientIndexLabel.setText(String.valueOf(expedientClientInProcess.getClient().getIdentification()));
            this.receptionLayoutCardPane.setVisible(false);
            this.templateCreationLayoutCardPane.setVisible(false);
            this.indextionLayoutCardPane.setVisible(true);
            indexButton.repaint();
            indexLabel.setText("Finalizar");
        } else {
            documentsReceptionState = true;
            indexButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/img/index1.png"))); // NOI18N
            indexButton.repaint();
            scannerPaneDownLeft.setVisible(false);
            scannerPaneDownLeft.removeImage();
            this.receptionLayoutCardPane.setVisible(true);
            this.indextionLayoutCardPane.setVisible(false);
            this.templateCreationLayoutCardPane.setVisible(false);
            scannerPaneUpRight.setVisible(false);
            scannerPaneUpRight.removeImage();
            scannerPaneUpLeft.removeImage();
            scannerPaneUpLeft.setVisible(false);
            scannerPaneDownRight.setVisible(false);
            scannerPaneDownRight.removeImage();
            scannerButton.setEnabled(false);
            uploadImageButton.setEnabled(false);
            indexLabel.setText("Indexar");
        }
    }
    
    private void uploadImageAction(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Subir");
        fileChooser.showDialog(this, null);
        File file = fileChooser.getSelectedFile();
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType.equalsIgnoreCase("image/jpeg")) {
            if (!scannerPaneUpLeft.isImageLoaded()) {
                scannerPaneUpLeft.loadImage(file);
                scannerPaneUpLeft.setVisible(true);
                scannerPaneUpLeft.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            } else if (!scannerPaneUpRight.isImageLoaded()) {
                scannerPaneUpRight.loadImage(file);
                scannerPaneUpRight.setVisible(true);
                scannerPaneUpRight.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            } else if (!scannerPaneDownLeft.isImageLoaded()) {
                scannerPaneDownLeft.loadImage(file);
                scannerPaneDownLeft.setVisible(true);
                scannerPaneDownLeft.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            } else if (!scannerPaneDownRight.isImageLoaded()) {
                scannerPaneDownRight.loadImage(file);
                scannerPaneDownRight.setVisible(true);
                scannerPaneDownRight.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                
            }
        }
    }
    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void createNewDocumentItemMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        JDialog creationDocumentDialog = new JDialog();
        initComponentsCreationDocumentDialog(creationDocumentDialog);
        creationDocumentDialog.setModal(true);
        creationDocumentDialog.setVisible(true);
    }
    
    private void registPersonAction(java.awt.event.ActionEvent evt) {
        boolean validProcess = true;
        if ("".equalsIgnoreCase(identificacionNumberTxt.getText())) {
            validProcess = false;
        } else if ("".equalsIgnoreCase(nameRegistPersonTxt.getText())) {
            validProcess = false;
        } else if (expedientIncludeList.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, "Debe incluir almenos un expediente");
            validProcess = false;
        }
        if (validProcess) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Client client = new Client();
            client.setName(nameRegistPersonTxt.getText());
            client.setIdentificationType(idenTypeSelector.getSelectedItem().toString());
            client.setIdentification(Integer.valueOf(identificacionNumberTxt.getText()));
            client.setCountry(nationalityTxt.getSelectedItem().toString());
            try {
                clientController.create(client);
            } catch (Exception ex) {
                Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            List selectedExpedientList = expedientIncludeList.getSelectedValuesList();
            if (selectedExpedientList != null && !selectedExpedientList.isEmpty()) {
                for (Object expedientObject : selectedExpedientList) {
                    Expedient expedient = (Expedient) expedientObject;
                    ExpedientClientPK expedientPK = new ExpedientClientPK();
                    expedientPK.setFkClient(client.getIdUser());
                    expedientPK.setFkExpedient(expedient.getIdExpedient());
                    ExpedientClient expedientClient = new ExpedientClient();
                    expedientClient.setClient(client);
                    expedientClient.setExpedient(expedient);
                    expedientClient.setExpedientClientPK(expedientPK);
                    expedientClient.setLastModification(Calendar.getInstance().getTime());
                    try {
                        expedientClientController.create(expedientClient);
                    } catch (Exception ex) {
                        Logger.getLogger(MainCustomGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            em.getTransaction().commit();
            validateClientIdentificacionDocuments(idenTypeSelector.getSelectedItem().toString(), identificacionNumberTxt.getText(), client);
            registPersonDialog.setVisible(false);
        }
    }
    
    private void createDataButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean validProcess = true;
        if ("".equals(nameDataTxt.getText().trim())) {
            validProcess = false;
        }
        if (validProcess) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            DocumentData dataDocument = new DocumentData();
            dataDocument.setName(nameDataTxt.getText());
            dataDocument.setFkDataType((DataType) this.dataTypeSelector.getSelectedItem());
            dataDocument.setFkDocument(selectedDocument);
            if (isRequiredDataCheckBox.isSelected()) {
                dataDocument.setRequired(1);
            } else {
                dataDocument.setRequired(0);
            }
            try {
                documentDataController.create(dataDocument);
                
            } catch (Exception ex) {
                Logger.getLogger(MainCustomGui.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            em.getTransaction().commit();
            refreshExpedientTree();
        }
    }
    
    private void cleanDataButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    private void registPersonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        registPersonDialog = new JDialog();
        this.initComponentsRegistPersonDialog(registPersonDialog);
        registPersonDialog.setModal(true);
        registPersonDialog.setVisible(true);
    }
    
    private void consultPersonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JDialog searchPersonDialog = new JDialog();
        initComponentSearchPersonDialog(searchPersonDialog);
        searchPersonDialog.setModal(true);
        searchPersonDialog.setVisible(true);
    }
    
    private void expedientTreeMouseClicked(java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            int row = expedientTree.getClosestRowForLocation(evt.getX(), evt.getY());
            expedientTree.setSelectionRow(row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) expedientTree.getLastSelectedPathComponent();
            if (node != null && creationExpedientState) {
                if (node.getUserObject() instanceof Expedient) {
                    selectedExpedientInCreationPanel = (Expedient) node.getUserObject();
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                    openPopupExpedient(evt.getComponent(), evt.getX(), evt.getY());
                } else if (node.getUserObject() instanceof Document) {
                    selectedDocument = (Document) node.getUserObject();
                    this.desactiveDocumentDataParameterPane();
                    activeDocumentParameterPane(selectedDocument);
                    openPopupDocument(evt.getComponent(), evt.getX(), evt.getY());
                } else if (node.getUserObject() instanceof DocumentData) {
                    selectedDocumentData = (DocumentData) node.getUserObject();
                    this.desactiveDocumentParameterPane();
                    this.activeDocumentDataParameterPane(selectedDocumentData);
                } else {
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                }
            } else if (node != null && documentsReceptionState) {
                if (node.getUserObject() instanceof String) {
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                    this.openPopupMetaExpedientDocumentReception(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        } else if (SwingUtilities.isLeftMouseButton(evt)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) expedientTree.getLastSelectedPathComponent();
            if (node != null && creationExpedientState) {
                if (node.getUserObject() instanceof Expedient) {
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                } else if (node.getUserObject() instanceof Document) {
                    selectedDocument = (Document) node.getUserObject();
                    this.desactiveDocumentDataParameterPane();
                    activeDocumentParameterPane(selectedDocument);
                } else if (node.getUserObject() instanceof DocumentData) {
                    selectedDocumentData = (DocumentData) node.getUserObject();
                    this.desactiveDocumentParameterPane();
                    this.activeDocumentDataParameterPane(selectedDocumentData);
                } else {
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                }
            } else if (node != null && documentsReceptionState) {
                if (node.getUserObject() instanceof Document) {
                    selectedDocument = (Document) node.getUserObject();
                    this.desactiveDocumentDataParameterPane();
                    this.desactiveDocumentParameterPane();
                    captureDataShowDocumentNameLabel.setText(selectedDocument.getName());
                    initShowCaptureData();
                }
            }
        }
    }

    // Variables declaration - do not modify  
    private javax.swing.JList ExpedientDocumentList;
    private javax.swing.JButton cleanExpedientButton;
    private javax.swing.JButton createExpedientButton;
    private javax.swing.JButton removeUpLeftButton;
    private javax.swing.JScrollPane dataCaptureScrollPane;
    private javax.swing.JPanel creationExpedientPane;
    private javax.swing.JLabel descriptionExpedientLabel;
    private javax.swing.JTextField descriptionExpedientTxt;
    private javax.swing.JLabel documentIncludeLabel;
    private javax.swing.JLabel indexLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel uploadImageLabel;
    private javax.swing.JLabel nameExpedientLabel;
    private javax.swing.JTextField nameExpedientTxt;
    private javax.swing.JList ExpedientList;
    private javax.swing.JButton uploadImageButton;
    private javax.swing.JButton cleanDocumentButton;
    private javax.swing.JLabel documentReceptionLabel;
    private javax.swing.JLabel selectExpedientLabel;
    private javax.swing.JButton createDocumentButton;
    private javax.swing.JLabel createDocumentTitleLabel;
    private javax.swing.JPanel templateContainerCardPane;
    private javax.swing.JLabel templateCreationIcon;
    private javax.swing.JLabel templateCreationInfo;
    private javax.swing.JLabel templateCreationLabel;
    private javax.swing.JPanel templateCreationLayoutCardPane;
    private javax.swing.JPanel creationDocumentPane;
    private javax.swing.JScrollPane creationDocumentScroll;
    private javax.swing.JButton removeDownLeftButton;
    private javax.swing.JTextField descriptionDocumentTxt;
    private javax.swing.JLabel descriptionNameLabel;
    private javax.swing.JLabel expedientIncludeLabel;
    private javax.swing.JLabel nameDocumentLabel;
    private javax.swing.JTextField nameDocumentTxt;
    // Variables declaration - do not modify                     
    private javax.swing.JLabel alertDocumentPaneLabel;
    private javax.swing.JMenuItem consultPersonMenuItem;
    private javax.swing.JMenuItem createNewDocumentItemMenuButton;
    private javax.swing.JPanel dataParameterPane;
    private javax.swing.JButton removeUpRightButton;
    private javax.swing.JLabel dataTypeDataParameterPaneLabel;
    private javax.swing.JTextField dataTypeDataParameterTxt;
    private javax.swing.JLabel dataTypeFieldParameterPaneLabel;
    private javax.swing.JMenuItem deleteDocumentItemMenuButton;
    private javax.swing.JPanel documentParameterPane;
    private javax.swing.JSeparator downSeparatorDocumentPane;
    private javax.swing.JLabel expedientFormTitleLabel;
    private javax.swing.JLabel indexDocumentIconProcess;
    private javax.swing.JButton indexButton;
    private PaintedPane scannerPaneDownLeft;
    private PaintedPane scannerPaneDownRight;
    private PaintedPane scannerPaneUpLeft;
    private PaintedPane scannerPaneUpRight;
    private javax.swing.JTree expedientTree;
    private javax.swing.JCheckBox expireDocumentPaneCheckBox;
    private javax.swing.JLabel scannerLabel;
    private javax.swing.JLabel expireDocumentPaneLabel;
    private javax.swing.JLabel documentReceptionNameClient;
    private javax.swing.JLabel genericDocumentPaneLabel;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem menuItemScannerSelection;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane layeredOperationalPane;
    private javax.swing.JLayeredPane infoProcessLayeredPane;
    private javax.swing.JMenuBar mainBarMenu;
    private javax.swing.JMenu mainConsultOption;
    private javax.swing.JMenu mainDocumentOption;
    private javax.swing.JPanel mainPanelNavigation;
    private javax.swing.JMenu mainTemplateOption;
    private javax.swing.JMenu mainMenuConfiguration;
    private javax.swing.JLabel maxSizeDocumentPaneLabel;
    private javax.swing.JTextField maxSizeDocumentPaneTxt;
    private javax.swing.JTable dataCaptureTable;
    private javax.swing.JSeparator middleSeparatorDocumentPane;
    private javax.swing.JMenuItem modifyDocumentItemMenuButton;
    private javax.swing.JComboBox expedientSelectorReception;
    private javax.swing.JLabel nameDataParameterPaneLabel;
    private javax.swing.JTextField nameDataParameterPaneTxt;
    private javax.swing.JLabel nameDocumentPaneLabel;
    private javax.swing.JTextField nameDocumentPaneTxt;
    private javax.swing.JPanel receptionLayoutCardPane;
    private javax.swing.JMenuItem registPersonMenuItem;
    private javax.swing.JCheckBox repeatDocumentPaneCheckBox;
    private javax.swing.JLabel repeatDocumentPaneLabel;
    private javax.swing.JLabel requeridDataParameterPaneLabel;
    private javax.swing.JCheckBox requeridDocumentPaneCheckBox;
    private javax.swing.JLabel requeridDocumentPaneLabel;
    private javax.swing.JCheckBox requiredDataParameterPaneCheckBox;
    private javax.swing.JLabel rulesDocumentPaneLabel;
    private javax.swing.JButton scannerButton;
    private javax.swing.JLabel titleDocumentPaneLabel;
    private javax.swing.JSeparator upSeparatorDocumentPane;
    private javax.swing.JButton cleanRegistPersonButton;
    private javax.swing.JComboBox idenTypeSelector;
    private javax.swing.JLabel idenTypeSelectorLabel;
    private javax.swing.JTextField identificacionNumberTxt;
    private javax.swing.JLabel identificationNumberLabel;
    private javax.swing.JLabel nameRegistPersonLabel;
    private javax.swing.JTextField nameRegistPersonTxt;
    private javax.swing.JLabel nationSelectorRegistPersonLabel;
    private javax.swing.JComboBox nationalityTxt;
    private javax.swing.JLabel receptionDocumentLabel;
    private javax.swing.JButton registPersonButton;
    private javax.swing.JLabel registPersonTitle;
    private javax.swing.JButton verifyDocumentButton;
    private javax.swing.JCheckBox documentExpireCheckBox;
    private javax.swing.JButton cleanSearchButton;
    private javax.swing.JButton findSearchButton;
    private javax.swing.JLabel idenSearchLabel;
    private javax.swing.JTextField idenSearchTxt;
    private javax.swing.JLabel captureDataShowDocumentNameLabel;
    private javax.swing.JLabel idenTypeSearchLabel;
    private javax.swing.JLabel infoSearchLabel;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel indextionLayoutCardPane;
    private javax.swing.JTable searchTable;
    private javax.swing.JScrollPane searchTableScrollPane;
    private javax.swing.JComboBox typeIdenSearchSelector;
    private javax.swing.JCheckBox documentRepeatCheckBox;
    private javax.swing.JLabel documentRepeatLabel;
    private javax.swing.JCheckBox documentRequeridedCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel maxImageSizeLabel;
    private javax.swing.JTextField maxImageSizeTxt;
    private javax.swing.JButton cleanDataButton;
    private javax.swing.JButton createDataButton;
    private javax.swing.JLabel createDataTitleLabel;
    private javax.swing.JLabel receptionIcon;
    private javax.swing.JPanel creationDataPane;
    private javax.swing.JLabel dataDocumentLabel;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JList expedientIncludeList;
    private javax.swing.JLabel expedientIncludeRegistLabel;
    private javax.swing.JScrollPane captureDataScrollPane;
    private javax.swing.JComboBox dataTypeSelector;
    private javax.swing.JCheckBox isRequiredDataCheckBox;
    private javax.swing.JLabel isRequiredDataLabel;
    private javax.swing.JLabel indexDocumentHelpLebel;
    private javax.swing.JButton indexDocumentButtonProcess;
    private javax.swing.JLabel nameExpedientIndexLabel;
    private javax.swing.JLabel idenClientIndexLabel;
    private javax.swing.JLabel nameDataField;
    private javax.swing.JLabel indexDocumentLabel;
    private javax.swing.JButton removeDownRightButton;
    private javax.swing.JButton inclusionExpedientButton;
    private javax.swing.JLabel inclusionExpedientLabel;
    private javax.swing.JScrollPane inclusionExpedienteScrollpane;
    private javax.swing.JList listExpedientInclusion;
    private javax.swing.JPanel indextionContainerPane;
    private javax.swing.JPanel receptionContainerPane;
    private javax.swing.JTextField nameDataTxt;
    private javax.swing.JLabel receptionNameLabel;
    private javax.swing.JLabel selectedDocumentLabel;
}
