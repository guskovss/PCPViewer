/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.calculation;

import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.application.Action;
import ru.gss.pcpviewer.commons.DlgDirEdit;
import ru.gss.pcpviewer.commons.NoLocaleNumberFormatter;
import ru.gss.pcpviewer.data.DataList;

/**
 * Dialog for edit of parameters.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class DlgParameterEdit extends DlgDirEdit < DataList > {

    /**
     * Constructor.
     */
    public DlgParameterEdit() {
        super();
        initComponents();
        jftfDiameter.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfDefectAngle.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfDefectAngleInterval.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfBorderAngleInterval.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfWeigthFactor.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
    }

    /**
     * Setter editing object.
     * @param aTempObj editing object
     */
    @Override
    public void setTempObj(final DataList aTempObj) {
        putTempObj(aTempObj);
        jcbCoupling1.removeAllItems();
        jcbCoupling2.removeAllItems();
        for (int i = 0; i < getTempObj().getCouplingList().size(); i++) {
            jcbCoupling1.addItem(getTempObj().getCouplingList().get(i).getName());
            jcbCoupling2.addItem(getTempObj().getCouplingList().get(i).getName());
        }
        jcbCouplingAngle.removeAllItems();
        double a0 = getTempObj().getPipeline().getPoints().get(0).getAngle();
        for (int i = 0; i < getTempObj().getPipeline().getPoints().size(); i++) {
            double a = getTempObj().getPipeline().getPoints().get(i).getAngle();
            jcbCouplingAngle.addItem(a - a0);
        }
        jftfDiameter.setValue(getTempObj().getPipeline().getDiameter());
        jftfDefectAngle.setValue(getTempObj().getDefectAngle());
        jftfDefectAngleInterval.setValue(getTempObj().getDefectAngleInterval());
        jftfBorderAngleInterval.setValue(getTempObj().getBorderAngleInterval());
        jftfWeigthFactor.setValue(getTempObj().getWeigthFactor());
        jcbSpline.setSelected(getTempObj().getParameter().isShowSpline());
        jcbLegend.setSelected(getTempObj().getParameter().isShowLegend());
        jcbUseDefect.setSelected(getTempObj().isUseDefect());
        jcbCoupling1.setSelectedIndex(getTempObj().getCoupling1Index());
        jcbCoupling2.setSelectedIndex(getTempObj().getCoupling2Index());
        if (getTempObj().getCoupling1Revers() > 0) {
            jcbCoupling1Revers.setSelected(true);
        } else {
            jcbCoupling1Revers.setSelected(false);
        }
        if (getTempObj().getCoupling2Revers() > 0) {
            jcbCoupling2Revers.setSelected(true);
        } else {
            jcbCoupling2Revers.setSelected(false);
        }
        jcbCouplingAngle.setSelectedIndex(getTempObj().getCouplingAngle());
        getRootPane().setDefaultButton(jbtnOk);
    }

    /**
     * Init new object.
     * @return new object
     */
    @Override
    public DataList createTempObj() {
        return new DataList();
    }

    /**
     * Action for Cancel button.
     */
    @Action
    public void acCancel() {
        setChangeObj(false);
    }

    /**
     * Action for OK button.
     */
    @Action
    public void acOk() {   
        if (checkFormattedTextFieldNullNoSupposed(jftfDiameter, 10.0, 1500.0)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfDefectAngle, 0.0, 360.0)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfDefectAngleInterval, 20.0, 160.0)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfBorderAngleInterval, 20.0, 70.0)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfWeigthFactor, 0.0, 1.0)) {
            return;
        }
        getTempObj().getPipeline().setDiameter(getDoubleFromFormattedTextField(jftfDiameter));
        getTempObj().setDefectAngle(getDoubleFromFormattedTextField(jftfDefectAngle));
        getTempObj().setDefectAngleInterval(getDoubleFromFormattedTextField(jftfDefectAngleInterval));
        getTempObj().setBorderAngleInterval(getDoubleFromFormattedTextField(jftfBorderAngleInterval));
        getTempObj().setWeigthFactor(getDoubleFromFormattedTextField(jftfWeigthFactor));
        getTempObj().getParameter().setShowSpline(jcbSpline.isSelected());
        getTempObj().getParameter().setShowLegend(jcbLegend.isSelected());
        getTempObj().setUseDefect(jcbUseDefect.isSelected());
        if (jcbCoupling1.getSelectedIndex() == jcbCoupling2.getSelectedIndex()) {
            return;
        }
        getTempObj().setCoupling1Index(jcbCoupling1.getSelectedIndex());
        getTempObj().setCoupling2Index(jcbCoupling2.getSelectedIndex());
        if (jcbCoupling1Revers.isSelected()) {
            getTempObj().setCoupling1Revers(1);
        } else {
            getTempObj().setCoupling1Revers(0);
        }
        if (jcbCoupling2Revers.isSelected()) {
            getTempObj().setCoupling2Revers(1);
        } else {
            getTempObj().setCoupling2Revers(0);
        }
        getTempObj().setCouplingAngle(jcbCouplingAngle.getSelectedIndex());
        setChangeObj(true);
    }

    //CHECKSTYLE:OFF
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnOk = new javax.swing.JButton();
        jbtnCancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jlbDiameter = new javax.swing.JLabel();
        jftfDiameter = new javax.swing.JFormattedTextField();
        jcbUseDefect = new javax.swing.JCheckBox();
        jlbDefectAngle = new javax.swing.JLabel();
        jlbDefectAngleInterval = new javax.swing.JLabel();
        jlbBorderAngleInterval = new javax.swing.JLabel();
        jlbWeigthFactor = new javax.swing.JLabel();
        jftfDefectAngle = new javax.swing.JFormattedTextField();
        jftfDefectAngleInterval = new javax.swing.JFormattedTextField();
        jftfBorderAngleInterval = new javax.swing.JFormattedTextField();
        jftfWeigthFactor = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jlbCoupling1 = new javax.swing.JLabel();
        jlbCoupling2 = new javax.swing.JLabel();
        jcbCoupling2 = new javax.swing.JComboBox();
        jcbCoupling1 = new javax.swing.JComboBox();
        jcbCoupling1Revers = new javax.swing.JCheckBox();
        jlbCouplingAngle = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jcbCouplingAngle = new javax.swing.JComboBox();
        jcbCoupling2Revers = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jcbSpline = new javax.swing.JCheckBox();
        jcbLegend = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.pcpviewer.PCPViewerApp.class).getContext().getResourceMap(DlgParameterEdit.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.pcpviewer.PCPViewerApp.class).getContext().getActionMap(DlgParameterEdit.class, this);
        jbtnOk.setAction(actionMap.get("acOk")); // NOI18N
        jbtnOk.setName("jbtnOk"); // NOI18N

        jbtnCancel.setAction(actionMap.get("acCancel")); // NOI18N
        jbtnCancel.setName("jbtnCancel"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jlbDiameter.setText(resourceMap.getString("jlbDiameter.text")); // NOI18N
        jlbDiameter.setName("jlbDiameter"); // NOI18N

        jftfDiameter.setName("jftfDiameter"); // NOI18N

        jcbUseDefect.setText(resourceMap.getString("jcbUseDefect.text")); // NOI18N
        jcbUseDefect.setName("jcbUseDefect"); // NOI18N

        jlbDefectAngle.setText(resourceMap.getString("jlbDefectAngle.text")); // NOI18N
        jlbDefectAngle.setName("jlbDefectAngle"); // NOI18N

        jlbDefectAngleInterval.setText(resourceMap.getString("jlbDefectAngleInterval.text")); // NOI18N
        jlbDefectAngleInterval.setName("jlbDefectAngleInterval"); // NOI18N

        jlbBorderAngleInterval.setText(resourceMap.getString("jlbBorderAngleInterval.text")); // NOI18N
        jlbBorderAngleInterval.setName("jlbBorderAngleInterval"); // NOI18N

        jlbWeigthFactor.setText(resourceMap.getString("jlbWeigthFactor.text")); // NOI18N
        jlbWeigthFactor.setName("jlbWeigthFactor"); // NOI18N

        jftfDefectAngle.setName("jftfDefectAngle"); // NOI18N

        jftfDefectAngleInterval.setName("jftfDefectAngleInterval"); // NOI18N

        jftfBorderAngleInterval.setName("jftfBorderAngleInterval"); // NOI18N

        jftfWeigthFactor.setName("jftfWeigthFactor"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N
        jSeparator1.setPreferredSize(new java.awt.Dimension(350, 2));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jcbUseDefect)
                        .addContainerGap(202, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbDefectAngle)
                            .addComponent(jlbDefectAngleInterval)
                            .addComponent(jlbBorderAngleInterval)
                            .addComponent(jlbDiameter)
                            .addComponent(jlbWeigthFactor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jftfWeigthFactor)
                            .addComponent(jftfBorderAngleInterval, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jftfDefectAngleInterval, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jftfDefectAngle, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                            .addComponent(jftfDiameter))
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbUseDefect)
                .addGap(7, 7, 7)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbDiameter)
                    .addComponent(jftfDiameter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbDefectAngle)
                    .addComponent(jftfDefectAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbDefectAngleInterval)
                    .addComponent(jftfDefectAngleInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbBorderAngleInterval)
                    .addComponent(jftfBorderAngleInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jftfWeigthFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbWeigthFactor))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jlbCoupling1.setText(resourceMap.getString("jlbCoupling1.text")); // NOI18N
        jlbCoupling1.setName("jlbCoupling1"); // NOI18N

        jlbCoupling2.setText(resourceMap.getString("jlbCoupling2.text")); // NOI18N
        jlbCoupling2.setName("jlbCoupling2"); // NOI18N

        jcbCoupling2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCoupling2.setName("jcbCoupling2"); // NOI18N

        jcbCoupling1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCoupling1.setName("jcbCoupling1"); // NOI18N

        jcbCoupling1Revers.setText(resourceMap.getString("jcbCoupling1Revers.text")); // NOI18N
        jcbCoupling1Revers.setName("jcbCoupling1Revers"); // NOI18N

        jlbCouplingAngle.setText(resourceMap.getString("jlbCouplingAngle.text")); // NOI18N
        jlbCouplingAngle.setName("jlbCouplingAngle"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N
        jSeparator2.setPreferredSize(new java.awt.Dimension(350, 2));

        jcbCouplingAngle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCouplingAngle.setName("jcbCouplingAngle"); // NOI18N

        jcbCoupling2Revers.setText(resourceMap.getString("jcbCoupling2Revers.text")); // NOI18N
        jcbCoupling2Revers.setName("jcbCoupling2Revers"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbCoupling2)
                            .addComponent(jlbCoupling1)
                            .addComponent(jlbCouplingAngle))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jcbCoupling1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbCoupling1Revers))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jcbCoupling2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcbCoupling2Revers)))
                                .addContainerGap(14, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbCouplingAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbCoupling1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbCoupling1Revers))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcbCoupling2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbCoupling2Revers)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jlbCoupling1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbCoupling2)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlbCouplingAngle)
                            .addComponent(jcbCouplingAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jcbSpline.setText(resourceMap.getString("jcbSpline.text")); // NOI18N
        jcbSpline.setName("jcbSpline"); // NOI18N

        jcbLegend.setText(resourceMap.getString("jcbLegend.text")); // NOI18N
        jcbLegend.setName("jcbLegend"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbSpline)
                    .addComponent(jcbLegend))
                .addContainerGap(306, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbSpline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbLegend)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(295, Short.MAX_VALUE)
                .addComponent(jbtnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnOk)
                    .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnOk;
    private javax.swing.JComboBox jcbCoupling1;
    private javax.swing.JCheckBox jcbCoupling1Revers;
    private javax.swing.JComboBox jcbCoupling2;
    private javax.swing.JCheckBox jcbCoupling2Revers;
    private javax.swing.JComboBox jcbCouplingAngle;
    private javax.swing.JCheckBox jcbLegend;
    private javax.swing.JCheckBox jcbSpline;
    private javax.swing.JCheckBox jcbUseDefect;
    private javax.swing.JFormattedTextField jftfBorderAngleInterval;
    private javax.swing.JFormattedTextField jftfDefectAngle;
    private javax.swing.JFormattedTextField jftfDefectAngleInterval;
    private javax.swing.JFormattedTextField jftfDiameter;
    private javax.swing.JFormattedTextField jftfWeigthFactor;
    private javax.swing.JLabel jlbBorderAngleInterval;
    private javax.swing.JLabel jlbCoupling1;
    private javax.swing.JLabel jlbCoupling2;
    private javax.swing.JLabel jlbCouplingAngle;
    private javax.swing.JLabel jlbDefectAngle;
    private javax.swing.JLabel jlbDefectAngleInterval;
    private javax.swing.JLabel jlbDiameter;
    private javax.swing.JLabel jlbWeigthFactor;
    // End of variables declaration//GEN-END:variables
    //CHECKSTYLE:ON
}
