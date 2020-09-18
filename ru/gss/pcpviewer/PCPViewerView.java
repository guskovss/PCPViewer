/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import ru.gss.pcpviewer.chart.ChartMaker;
import ru.gss.pcpviewer.calculation.SectionTableModel;
import ru.gss.pcpviewer.calculation.DlgParameterEdit;
import ru.gss.pcpviewer.chart.DlgParameterPolarChartEdit;
import ru.gss.pcpviewer.commons.FileChooserFactory;
import ru.gss.pcpviewer.data.DataList;
import ru.gss.pcpviewer.data.Section;

/**
 * The main frame of the application.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class PCPViewerView extends FrameView {

    static {
        UIManager.put("JXTable.column.horizontalScroll", "Горизонтальная прокрутка");
        UIManager.put("JXTable.column.packAll", "Упаковка всех столбцов");
        UIManager.put("JXTable.column.packSelected", "Упаковка выбранного столбца");
    }

    /**
     * Constructor.
     * @param app application
     */
    public PCPViewerView(final SingleFrameApplication app) {
        super(app);
        initComponents();

        //Icon
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.pcpviewer.PCPViewerApp.class).getContext().getResourceMap(PCPViewerView.class);
        //getFrame().setIconImage(resourceMap.getImageIcon("mainFrame.icon").getImage());

        //Translate
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.lookInLabelText", "Папка:");
        UIManager.put("FileChooser.saveInLabelText", "Папка:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Фильтр");
        UIManager.put("FileChooser.upFolderToolTipText", "Наверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Новая папка");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Справка");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonToolTipText", "Справка");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить как");
        UIManager.put("ProgressMonitor.progressText", "Загрузка...");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.messageDialogTitle", "Внимание");

        //Main objects
        data = new DataList();
        chartMaker = new ChartMaker();

        //Settings of sections table
        tmSection = new SectionTableModel(data);
        jtSectionCoupling.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtSectionCoupling.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtSectionCoupling.setModel(tmSection);

        //Buttons for chart visualisation
        jtbtnChart1.setSelected(data.getParameter().isShowChart1());
        jtbtnChart2.setSelected(data.getParameter().isShowChart2());

        chartPanel = new ChartPanel(chartMaker.createChart(data));
        chartPanel.setPopupMenu(jpmChart);
        chartPanel.setMouseZoomable(false);
        jpChart.add(chartPanel);
    }

    /**
     * Save log to file.
     */
    @Action(enabledProperty = "existData")
    public void acSaveLogToFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.saveTextAreaToFile(f, jtaLog);
                addToLog("Запись сообщений в файл " + f.getAbsolutePath());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Text message.
     * @param s message
     */
    private void addToLog(final String s) {
        jtaLog.append(s + "\n");
    }

    /**
     * Existing data.
     */
    private boolean existData = false;

    /**
     * Existing data.
     * @return existing data
     */
    public boolean isExistData() {
        return existData;
    }

    /**
     * Existing data.
     * @param b existing data
     */
    public void setExistData(final boolean b) {
        boolean old = isExistData();
        existData = b;
        firePropertyChange("existData", old, isExistData());
    }

    /**
     * Message of exeption.
     * @param ex exeption
     */
    public void showErrorMessage(final Exception ex) {
        JOptionPane.showMessageDialog(
                PCPViewerApp.getApplication().getMainFrame(), ex,
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Message of no correct coordinates calculation.
     */
    private void showNoCalculateCoordinateMessage() {
        JOptionPane.showMessageDialog(this.getFrame(),
                "Не удалось выполнить расчет координат точек сечения.\n" +
                "Проверьте корректность данных о радиусах кривизны и их соответствие \n" +
                "номинальному диаметру трубопровода.",
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Chart save.
     */
    @Action
    public void acChartSaveAs() {
        JFileChooser ch = FileChooserFactory.getChooser(2);
        if (ch.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                ChartUtilities.saveChartAsPNG(f, chartPanel.getChart(), 600, 550);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Chart parameters.
     */
    @Action
    public void acChartParameter() {
        DlgParameterPolarChartEdit d = new DlgParameterPolarChartEdit();
        d.setTempObj(chartPanel.getChart());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
    }

    /**
     * Chart repaint.
     */
    @Action(enabledProperty = "existData")
    public void acPlot() {
        chartPanel.setChart(chartMaker.createChart(data));
        chartPanel.setMouseZoomable(false);
    }
    
    /**
     * Status of chart show control buttons.
     * @return true, if there are chart selected for show
     */
    private boolean isShowChartSelectedButton() {
        boolean b = false;
        if (jtbtnChart1.isSelected()) {
            b = true;
        }
        if (jtbtnChart2.isSelected()) {
            b = true;
        }
        return b;
    }

    /**
     * Control show of charts.
     */
    @Action(enabledProperty = "existData")
    public void acShowChartButton() {
        if (!isShowChartSelectedButton()) {
            jtbtnChart1.setSelected(data.getParameter().isShowChart1());
            jtbtnChart2.setSelected(data.getParameter().isShowChart2());
        } else {
            data.getParameter().setShowChart1(jtbtnChart1.isSelected());
            data.getParameter().setShowChart2(jtbtnChart2.isSelected());
            acPlot();
        }
    }

    /**
     * Load sections parameters from file.
     */
    @Action
    public void acOpenFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(1);
        if (chooser.showOpenDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                FileNameExtensionFilter ff = (FileNameExtensionFilter) chooser.getFileFilter();
                String ext = ff.getExtensions()[0];
                if (ext.equals("xls")) {
                    data.loadFromFileXLS(f, false);
                }
                if (ext.equals("xlsx")) {
                    data.loadFromFileXLS(f, true);
                }
                addToLog("Открыт файл " + f.getAbsolutePath());
                setExistData(true);
                checkCoordinate(data.getPipeline());
                calculateDelta();
                acPlot();        
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Check coordinate of section.
     * @param s section
     */
    private void checkCoordinate(final Section s) {
        if (!s.isCalculateCorrect()) {
            showNoCalculateCoordinateMessage();
        }
    }

    /**
     * Change of parameters.
     */
    @Action(enabledProperty = "existData")
    public void acParameterEdit() {
        DlgParameterEdit d = new DlgParameterEdit();
        d.setTempObj(data);
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {         
            calculateDelta();
            acPlot();
        }
    }

    /**
     * String representation of coupling configuration.
     * @return string representation of coupling configuration
     */
    private String couplingConfiguration() {
        String c1Name = data.getCouplingList().get(data.getCoupling1Index()).getName();
        String c2Name = data.getCouplingList().get(data.getCoupling2Index()).getName();
        String c1Revers = "Н";
        String c2Revers = "Н";
        if (data.getCoupling1Revers() > 0) {
            c1Revers = "Р";
        }
        if (data.getCoupling2Revers() > 0) {
            c2Revers = "Р";
        }
        return c1Name + " " + c1Revers + " " + c2Name + " " + c2Revers;
    }

    /**
     * Calculation gap between pileline and coupling.
     */
    private void calculateDelta() {
        String cConfiguration = couplingConfiguration();
        double a = data.getPipeline().getPoints().get(0).getAngle();
        double b = data.getPipeline().getPoints().get(data.getCouplingAngle()).getAngle();
        String mAngle = String.format(Locale.US, "%6.2f", (b - a));
        String cAngle = String.format(Locale.US, "%.2f", (b - a));
        data.calculateSectionCoupling();
        data.getCoupling().calculateCoordinate();
        checkCoordinate(data.getCoupling());
        double s1 = data.calculateDeviation();
        double s2 = data.calculateDelta();
        String mDelta1 = String.format(Locale.US, "%5.2f", s1);
        String cDelta1 = String.format(Locale.US, "%.2f", s1);
        String mDelta2 = String.format(Locale.US, "%5.2f", s2);
        String cDelta2 = String.format(Locale.US, "%.2f", s2);
        String mMessage = cConfiguration + " " + mAngle +
                " Δρс, мм: " + mDelta1 + " Δrс, мм: " + mDelta2;
        String cMessage = cConfiguration + " " + cAngle +
                " Δρс, мм: " + cDelta1 + " Δrс, мм: " + cDelta2;
        if (data.isUseDefect()) {
            double sd1 = data.calculateDeviationDefect();
            double sd2 = data.calculateDeltaDefect();
            String mDeltaD1 = String.format(Locale.US, "%5.2f", sd1);
            String cDeltaD1 = String.format(Locale.US, "%.2f", sd1);
            String mDeltaD2 = String.format(Locale.US, "%5.2f", sd2);
            String cDeltaD2 = String.format(Locale.US, "%.2f", sd2);
            mMessage = mMessage + " Δρд, мм: " + mDeltaD1 + " Δrд, мм: " + mDeltaD2;
            cMessage = cMessage + " Δρд, мм: " + cDeltaD1 + " Δrд, мм: " + cDeltaD2;
        }
        addToLog(mMessage);
        statusMessageLabel.setText(" " + cMessage);
        tmSection.fireTableDataChanged();
    }

    /**
     * Rotate coupling clockwise.
     */
    @Action(enabledProperty = "existData")
    public void acCouplingRotation1() {
        int k = data.getCouplingAngle();
        int n = data.getPipeline().getPoints().size();
        k--;
        if (k < 0) {
            k = k + n;
        }
        data.setCouplingAngle(k);
        calculateDelta();
        acPlot();    
    }

    /**
     * Rotate coupling counterclockwise.
     */
    @Action(enabledProperty = "existData")
    public void acCouplingRotation2() {
        int k = data.getCouplingAngle();
        int n = data.getPipeline().getPoints().size();
        k++;
        if (k > (n - 1)) {
            k = k - n;
        }
        data.setCouplingAngle(k);
        calculateDelta();
        acPlot(); 
    }

    /**
     * Action for About button.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            aboutBox = new PCPViewerAboutBox();
        }
        aboutBox.setLocationRelativeTo(this.getFrame());
        aboutBox.setVisible(true);
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

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jbtnOpen = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jbtnParameters = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jbtnRefresh = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jtbtnChart1 = new javax.swing.JToggleButton();
        jtbtnChart2 = new javax.swing.JToggleButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jbtnRotation1 = new javax.swing.JButton();
        jbtnRotation2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtSectionCoupling = new org.jdesktop.swingx.JXTable();
        jPanel5 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jbtnSaveLog = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jpChart = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jmFile = new javax.swing.JMenu();
        jmiOpen = new javax.swing.JMenuItem();
        jmiParameters = new javax.swing.JMenuItem();
        jmiSaveLog = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jmiExit = new javax.swing.JMenuItem();
        javax.swing.JMenu jmHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem jmiAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        jpmChart = new javax.swing.JPopupMenu();
        jmiChartParameters = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(350);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel2.setMinimumSize(new java.awt.Dimension(250, 290));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(626, 210));
        jPanel2.setRequestFocusEnabled(false);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar1.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 36));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.pcpviewer.PCPViewerApp.class).getContext().getActionMap(PCPViewerView.class, this);
        jbtnOpen.setAction(actionMap.get("acOpenFile")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.pcpviewer.PCPViewerApp.class).getContext().getResourceMap(PCPViewerView.class);
        jbtnOpen.setIcon(resourceMap.getIcon("jbtnOpen.icon")); // NOI18N
        jbtnOpen.setDisabledIcon(resourceMap.getIcon("jbtnOpen.disabledIcon")); // NOI18N
        jbtnOpen.setFocusable(false);
        jbtnOpen.setHideActionText(true);
        jbtnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnOpen.setName("jbtnOpen"); // NOI18N
        jbtnOpen.setRolloverIcon(resourceMap.getIcon("jbtnOpen.rolloverIcon")); // NOI18N
        jbtnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnOpen);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        jbtnParameters.setAction(actionMap.get("acParameterEdit")); // NOI18N
        jbtnParameters.setIcon(resourceMap.getIcon("jbtnParameters.icon")); // NOI18N
        jbtnParameters.setDisabledIcon(resourceMap.getIcon("jbtnParameters.disabledIcon")); // NOI18N
        jbtnParameters.setFocusable(false);
        jbtnParameters.setHideActionText(true);
        jbtnParameters.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnParameters.setName("jbtnParameters"); // NOI18N
        jbtnParameters.setRolloverIcon(resourceMap.getIcon("jbtnParameters.rolloverIcon")); // NOI18N
        jbtnParameters.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnParameters);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jbtnRefresh.setAction(actionMap.get("acPlot")); // NOI18N
        jbtnRefresh.setIcon(resourceMap.getIcon("jbtnRefresh.icon")); // NOI18N
        jbtnRefresh.setDisabledIcon(resourceMap.getIcon("jbtnRefresh.disabledIcon")); // NOI18N
        jbtnRefresh.setFocusable(false);
        jbtnRefresh.setHideActionText(true);
        jbtnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRefresh.setName("jbtnRefresh"); // NOI18N
        jbtnRefresh.setRolloverIcon(resourceMap.getIcon("jbtnRefresh.rolloverIcon")); // NOI18N
        jbtnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jbtnRefresh);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jtbtnChart1.setAction(actionMap.get("acShowChartButton")); // NOI18N
        buttonGroup1.add(jtbtnChart1);
        jtbtnChart1.setIcon(resourceMap.getIcon("jtbtnChart1.icon")); // NOI18N
        jtbtnChart1.setToolTipText(resourceMap.getString("jtbtnChart1.toolTipText")); // NOI18N
        jtbtnChart1.setDisabledIcon(resourceMap.getIcon("jtbtnChart1.disabledIcon")); // NOI18N
        jtbtnChart1.setFocusable(false);
        jtbtnChart1.setHideActionText(true);
        jtbtnChart1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbtnChart1.setName("jtbtnChart1"); // NOI18N
        jtbtnChart1.setRolloverIcon(resourceMap.getIcon("jtbtnChart1.rolloverIcon")); // NOI18N
        jtbtnChart1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbtnChart1);

        jtbtnChart2.setAction(actionMap.get("acShowChartButton")); // NOI18N
        buttonGroup1.add(jtbtnChart2);
        jtbtnChart2.setIcon(resourceMap.getIcon("jtbtnChart2.icon")); // NOI18N
        jtbtnChart2.setToolTipText(resourceMap.getString("jtbtnChart2.toolTipText")); // NOI18N
        jtbtnChart2.setDisabledIcon(resourceMap.getIcon("jtbtnChart2.disabledIcon")); // NOI18N
        jtbtnChart2.setFocusable(false);
        jtbtnChart2.setHideActionText(true);
        jtbtnChart2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbtnChart2.setName("jtbtnChart2"); // NOI18N
        jtbtnChart2.setRolloverIcon(resourceMap.getIcon("jtbtnChart2.rolloverIcon")); // NOI18N
        jtbtnChart2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jtbtnChart2);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar2.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar2.setName("jToolBar2"); // NOI18N
        jToolBar2.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnRotation1.setAction(actionMap.get("acCouplingRotation1")); // NOI18N
        jbtnRotation1.setIcon(resourceMap.getIcon("jbtnRotation1.icon")); // NOI18N
        jbtnRotation1.setDisabledIcon(resourceMap.getIcon("jbtnRotation1.disabledIcon")); // NOI18N
        jbtnRotation1.setFocusable(false);
        jbtnRotation1.setHideActionText(true);
        jbtnRotation1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRotation1.setName("jbtnRotation1"); // NOI18N
        jbtnRotation1.setRolloverIcon(resourceMap.getIcon("jbtnRotation1.rolloverIcon")); // NOI18N
        jbtnRotation1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jbtnRotation1);

        jbtnRotation2.setAction(actionMap.get("acCouplingRotation2")); // NOI18N
        jbtnRotation2.setIcon(resourceMap.getIcon("jbtnRotation2.icon")); // NOI18N
        jbtnRotation2.setDisabledIcon(resourceMap.getIcon("jbtnRotation2.disabledIcon")); // NOI18N
        jbtnRotation2.setFocusable(false);
        jbtnRotation2.setHideActionText(true);
        jbtnRotation2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRotation2.setName("jbtnRotation2"); // NOI18N
        jbtnRotation2.setRolloverIcon(resourceMap.getIcon("jbtnRotation2.rolloverIcon")); // NOI18N
        jbtnRotation2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jbtnRotation2);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtSectionCoupling.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtSectionCoupling.setColumnControlVisible(true);
        jtSectionCoupling.setName("jtSectionCoupling"); // NOI18N
        jtSectionCoupling.setSortable(false);
        jScrollPane1.setViewportView(jtSectionCoupling);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jToolBar3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar3.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar3.setName("jToolBar3"); // NOI18N
        jToolBar3.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnSaveLog.setAction(actionMap.get("acSaveLogToFile")); // NOI18N
        jbtnSaveLog.setIcon(resourceMap.getIcon("jbtnSaveLog.icon")); // NOI18N
        jbtnSaveLog.setDisabledIcon(resourceMap.getIcon("jbtnSaveLog.disabledIcon")); // NOI18N
        jbtnSaveLog.setFocusable(false);
        jbtnSaveLog.setHideActionText(true);
        jbtnSaveLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSaveLog.setName("jbtnSaveLog"); // NOI18N
        jbtnSaveLog.setRolloverIcon(resourceMap.getIcon("jbtnSaveLog.rolloverIcon")); // NOI18N
        jbtnSaveLog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jbtnSaveLog);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtaLog.setColumns(20);
        jtaLog.setEditable(false);
        jtaLog.setFont(resourceMap.getFont("jtaLog.font")); // NOI18N
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jtaLog.setName("jtaLog"); // NOI18N
        jScrollPane2.setViewportView(jtaLog);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel3.setMinimumSize(new java.awt.Dimension(600, 290));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(762, 290));

        jpChart.setName("jpChart"); // NOI18N
        jpChart.setLayout(new javax.swing.BoxLayout(jpChart, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1039, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1039, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        jmFile.setText(resourceMap.getString("jmFile.text")); // NOI18N
        jmFile.setName("jmFile"); // NOI18N

        jmiOpen.setAction(actionMap.get("acOpenFile")); // NOI18N
        jmiOpen.setName("jmiOpen"); // NOI18N
        jmFile.add(jmiOpen);

        jmiParameters.setAction(actionMap.get("acParameterEdit")); // NOI18N
        jmiParameters.setName("jmiParameters"); // NOI18N
        jmFile.add(jmiParameters);

        jmiSaveLog.setAction(actionMap.get("acSaveLogToFile")); // NOI18N
        jmiSaveLog.setName("jmiSaveLog"); // NOI18N
        jmFile.add(jmiSaveLog);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jmFile.add(jSeparator4);

        jmiExit.setAction(actionMap.get("quit")); // NOI18N
        jmiExit.setName("jmiExit"); // NOI18N
        jmFile.add(jmiExit);

        menuBar.add(jmFile);

        jmHelp.setText(resourceMap.getString("jmHelp.text")); // NOI18N
        jmHelp.setName("jmHelp"); // NOI18N

        jmiAbout.setAction(actionMap.get("showAboutBox")); // NOI18N
        jmiAbout.setName("jmiAbout"); // NOI18N
        jmHelp.add(jmiAbout);

        menuBar.add(jmHelp);

        statusPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setFont(resourceMap.getFont("statusMessageLabel.font")); // NOI18N
        statusMessageLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(228, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jpmChart.setName("jpmChart"); // NOI18N

        jmiChartParameters.setAction(actionMap.get("acChartParameter")); // NOI18N
        jmiChartParameters.setName("jmiChartParameters"); // NOI18N
        jpmChart.add(jmiChartParameters);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JButton jbtnOpen;
    private javax.swing.JButton jbtnParameters;
    private javax.swing.JButton jbtnRefresh;
    private javax.swing.JButton jbtnRotation1;
    private javax.swing.JButton jbtnRotation2;
    private javax.swing.JButton jbtnSaveLog;
    private javax.swing.JMenuItem jmiChartParameters;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOpen;
    private javax.swing.JMenuItem jmiParameters;
    private javax.swing.JMenuItem jmiSaveLog;
    private javax.swing.JPanel jpChart;
    private javax.swing.JPopupMenu jpmChart;
    private org.jdesktop.swingx.JXTable jtSectionCoupling;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JToggleButton jtbtnChart1;
    private javax.swing.JToggleButton jtbtnChart2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private SectionTableModel tmSection;
    private DataList data;
    private ChartMaker chartMaker;
    private ChartPanel chartPanel;
    //CHECKSTYLE:ON
}
