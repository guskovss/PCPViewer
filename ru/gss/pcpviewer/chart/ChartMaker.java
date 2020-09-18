/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.chart;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import ru.gss.pcpviewer.data.DataList;

/**
 * Chart.
 * @version 1.1.0 25.03.2020
 * @author Sergey Guskov
 */
public class ChartMaker {

    /**
     * Constructor.
     */
    public ChartMaker() {
    }

    /**
     * Create chart.
     * @param data data
     * @return chart
     */
    public JFreeChart createChart(final DataList data) {
        NumberAxis axis = new NumberAxis();
        axis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesPaint(1, Color.red);
        renderer.setShapesVisible(false);
        renderer.setConnectFirstAndLastPoint(false);
        //Tooltip
        for (int i = 0; i < 2; i++) {
            renderer.setSeriesToolTipGenerator(i, new StandardXYToolTipGenerator("{1}; {2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
        }
        XYDataset dataset = null;
        if (data.getParameter().isShowChart1()) {
            dataset = data.createDatasetRadius();
        } else {
            dataset = data.createDatasetDistance();
        }
        axis.setRange(0, 800);
        PolarPlot pplot = new PolarPlot(dataset, axis, renderer);
        pplot.setAngleLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        pplot.setRadiusMinorGridlinesVisible(false);
        pplot.setAngleTickUnit(new NumberTickUnit(30));
        axis.setAxisLineVisible(false);
        pplot.setAngleGridlinePaint(Color.darkGray);
        pplot.setRadiusGridlinePaint(Color.darkGray);
        JFreeChart chart = new JFreeChart(pplot);
        chart.getPlot().setOutlinePaint(Color.white);
        chart.setBackgroundPaint(Color.white);
        //Legend
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setBorder(0, 0, 0, 0); 
        JFreeChart lc = new JFreeChart(new PolarPlot(data.createDatasetTemp(), null, renderer));
        LegendItemSource[] lis = lc.getLegend().getSources();
        chart.getLegend().setSources(lis);
        if (data.getParameter().isShowLegend()) {
            chart.getLegend().setVisible(true);
            chart.setPadding(new RectangleInsets(0, 0, 0, 5));
        } else {
            chart.getLegend().setVisible(false);
            chart.setPadding(new RectangleInsets(0, 0, 0, 40));
        }
        return chart;
    }
}
