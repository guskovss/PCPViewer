/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.data;

/**
 * Chart show parameters.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class ParameterChart {
    
    /**
     * Show chart 1.
     */
    private boolean showChart1;
    /**
     * Show chart 2.
     */
    private boolean showChart2;
    /**
     * Show legend.
     */
    private boolean showLegend;
    /**
     * Show spline.
     */
    private boolean showSpline;
    
    /**
     * Constructor.
     */
    public ParameterChart() {
        showChart1 = true;
        showChart2 = false;
        showLegend = true;
        showSpline = true;
    }

    /**
     * Show chart 1.
     * @return show chart 1
     */
    public boolean isShowChart1() {
        return showChart1;
    }

    /**
     * Show chart 1.
     * @param aShowChart1 show chart 1
     */
    public void setShowChart1(final boolean aShowChart1) {
        showChart1 = aShowChart1;
    }

    /**
     * Show chart 2.
     * @return show chart 2
     */
    public boolean isShowChart2() {
        return showChart2;
    }

    /**
     * Show chart 2.
     * @param aShowChart2 show chart 2
     */
    public void setShowChart2(final boolean aShowChart2) {
        showChart2 = aShowChart2;
    }

    /**
     * Show legend.
     * @return show legend
     */
    public boolean isShowLegend() {
        return showLegend;
    }

    /**
     * Show legend.
     * @param aShowLegend show legend
     */
    public void setShowLegend(final boolean aShowLegend) {
        showLegend = aShowLegend;
    }

    /**
     * Show spline.
     * @return show spline
     */
    public boolean isShowSpline() {
        return showSpline;
    }

    /**
     * Show spline.
     * @param aShowSpline show spline
     */
    public void setShowSpline(final boolean aShowSpline) {
        showSpline = aShowSpline;
    }
}
