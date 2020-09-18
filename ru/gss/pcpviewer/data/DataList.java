/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextArea;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Initial data and calculation results.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class DataList {

    /**
     * Chart parameters.
     */
    private ParameterChart parameter;
    /**
     * Cross section of pipeline.
     */
    private Section pipeline;
    /**
     * Cross section of coupling.
     */
    private Section coupling;
    /**
     * List of couplings.
     */
    private ArrayList<Section> couplingList;
    /**
     * Difference between radius of curvature of coupling and pipeline.
     */
    private ArrayList<Double> delta1;
    /**
     * Gap between coupling and pipeline.
     */
    private ArrayList<Double> delta2;
    /**
     * Index of coupling 1.
     */
    private int coupling1Index;
    /**
     * Index of coupling 2.
     */
    private int coupling2Index;
    /**
     * Revers of coupling 1.
     */
    private int coupling1Revers;
    /**
     * Revers of coupling 2.
     */
    private int coupling2Revers;
    /**
     * Angle position of coupling.
     */
    private int couplingAngle;
    /**
     * Using defect position.
     */
    private boolean useDefect;
    /**
     * Angle position of defect.
     */
    private double defectAngle;
    /**
     * Angle interval for calculation.
     */
    private double defectAngleInterval;
    /**
     * Angle between defect and coupling border.
     */
    private double borderAngleInterval;
    /**
     * Weigth factor for calculation.
     */
    private double weigthFactor;

    /**
     * Constructor.
     */
    public DataList() {
        parameter = new ParameterChart();
        pipeline = new Section();
        coupling = new Section();
        couplingList = new ArrayList<Section>();
        delta1 = new ArrayList<Double>();
        delta2 = new ArrayList<Double>();
        coupling1Index = 0;
        coupling2Index = 1;
        coupling1Revers = 0;
        coupling2Revers = 0;
        couplingAngle = 0;
        useDefect = true;
        defectAngle = 120;
        defectAngleInterval = 60;
        borderAngleInterval = 30;
        weigthFactor = 0.5;
    }

    /**
     * Save text area to file.
     * @param file file
     * @param jta text area
     * @throws java.io.IOException exception
     */
    public void saveTextAreaToFile(final File file, final JTextArea jta) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file), true);
            out.print(jta.getText());
        } finally {
            out.close();
        }
    }

    /**
     * Parse double value from table cell.
     * @param row row of table
     * @param column column of table
     * @return double value or null
     */
    private Double parseCellDouble(final Row row, final int column) {
        Cell cell = row.getCell(column);
        if (cell == null) {
            return null;
        }
        int cellType = cell.getCellType();
        if (cellType != Cell.CELL_TYPE_NUMERIC) {
            return null;
        }
        return cell.getNumericCellValue();
    }

    /**
     * Parse string value from table cell.
     * @param row row of table
     * @param column column of table
     * @return string value or null
     */
    private String parseCellString(final Row row, final int column) {
        Cell cell = row.getCell(column);
        if (cell == null) {
            return null;
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue();
    }

    /**
     * Load data from file.
     * @param file file
     * @param isX type of file
     * @throws java.io.IOException exception
     */
    public void loadFromFileXLS(final File file, final boolean isX) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            Workbook wb;
            if (isX) {
                wb = new XSSFWorkbook(in);
            } else {
                wb = new HSSFWorkbook(in);
            }
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            pipeline.setDiameter(null);
            pipeline.getPoints().clear();
            couplingList.clear();
            coupling1Index = 0;
            coupling2Index = 1;
            coupling1Revers = 0;
            coupling2Revers = 0;
            couplingAngle = 0;
            int n = 0;
            if (it.hasNext()) {
                Row row = it.next();
                pipeline.setDiameter(parseCellDouble(row, 0));
                coupling.setDiameter(pipeline.getDiameter());
                for (int i = 2; i < row.getLastCellNum(); i++) {
                    if (row.getCell(i) == null) {
                        break;
                    }
                    n++;
                    Section sc = new Section();
                    sc.setName(parseCellString(row, i));
                    couplingList.add(sc);
                }
            }
            while (it.hasNext()) {
                Row row = it.next();
                Point pp = new Point();
                double a = parseCellDouble(row, 0);
                pp.setAngle(a);
                pp.setRadius(parseCellDouble(row, 1));
                pipeline.getPoints().add(pp);
                for (int i = 0; i < n; i++) {
                    Point pc = new Point();
                    pc.setAngle(a);
                    if (row.getCell(2 + i) == null) {
                        continue;
                    }
                    pc.setRadius(parseCellDouble(row, 2 + i));
                    couplingList.get(i).getPoints().add(pc);
                }
            }
            pipeline.calculateCoordinate();
        } finally {
            in.close();
        }
    }

    /**
     * Calculation coordinates of coupling points.
     */
    public void calculateSectionCoupling() {
        coupling.getPoints().clear();
        if (couplingList.size() > 1) {
            ArrayList<Point> point = new ArrayList<Point>();
            //Coupling 1
            if (coupling1Revers == 0) {
                for (int j = 0; j < couplingList.get(coupling1Index).getPoints().size(); j++) {
                    Point p = new Point();
                    p.setAngle(couplingList.get(coupling1Index).getPoints().get(j).getAngle());
                    p.setRadius(couplingList.get(coupling1Index).getPoints().get(j).getRadius());
                    point.add(p);
                }
            } else {
                for (int j = couplingList.get(coupling1Index).getPoints().size() - 1; j > -1; j--) {
                    Point p = new Point();
                    p.setAngle(180 - couplingList.get(coupling1Index).getPoints().get(j).getAngle());
                    p.setRadius(couplingList.get(coupling1Index).getPoints().get(j).getRadius());
                    point.add(p);
                }
            }
            //Coupling 2
            if (coupling2Revers == 0) {
                for (int j = 0; j < couplingList.get(coupling2Index).getPoints().size(); j++) {
                    Point p = new Point();
                    p.setAngle(couplingList.get(coupling2Index).getPoints().get(j).getAngle() + 180);
                    p.setRadius(couplingList.get(coupling2Index).getPoints().get(j).getRadius());
                    point.add(p);
                }
            } else {
                for (int j = couplingList.get(coupling2Index).getPoints().size() - 1; j > -1; j--) {
                    Point p = new Point();
                    p.setAngle(360 - couplingList.get(coupling2Index).getPoints().get(j).getAngle());
                    p.setRadius(couplingList.get(coupling2Index).getPoints().get(j).getRadius());
                    point.add(p);
                }
            }
            //Rotation
            for (int i = 0; i < couplingAngle; i++) {
                Point p = new Point();
                p.setAngle(point.get(i).getAngle());
                p.setRadius(point.get(i + point.size() - couplingAngle).getRadius());
                coupling.getPoints().add(p);
            }
            for (int i = couplingAngle; i < point.size(); i++) {
                Point p = new Point();
                p.setAngle(point.get(i).getAngle());
                p.setRadius(point.get(i - couplingAngle).getRadius());
                coupling.getPoints().add(p);
            }
        }
    }

    /**
     * Calculation deviation for all points.
     * @return deviation for all points
     */
    public double calculateDeviation() {
        double s = 0;
        delta1.clear();
        int n = pipeline.getPoints().size();
        for (int i = 0; i < n; i++) {
            double d = coupling.getPoint(i).getRadius() - pipeline.getPoint(i).getRadius();
            s = s + d * d;
            delta1.add(d);
        }
        if (n > 0) {
            return Math.sqrt(s / n);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Calculation deviation for points in area of defect.
     * @return deviation for points in area of defect
     */
    public double calculateDeviationDefect() {
        double s = 0;
        int n = pipeline.getPoints().size();
        int m = 0;
        double da = 360 / n;
        for (int i = 0; i < n; i++) {
            double a = da / 2 + i * da;
            if (checkAngleInterval(a, defectAngle, defectAngleInterval)) {
                m++;
                double d = coupling.getPoint(i).getRadius() - pipeline.getPoint(i).getRadius();
                s = s + d * d;
            }
        }
        if (m > 0) {
            return Math.sqrt(s / m);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Check angle in specified interval.
     * @param a angle
     * @param a0 center of interval
     * @param da half width of interval
     * @return true if angle is in interval, false if angle is not in interval
     */
    public boolean checkAngleInterval(final double a, final double a0, final double da) {
        boolean b = false;
        double na = a;
        double na0 = a0;
        while (na > 360) {
            na = na - 360;
        }
        while (na < 0) {
            na = na + 360;
        }
        while (na0 > 360) {
            na0 = na0 - 360;
        }
        while (na0 < 0) {
            na0 = na0 + 360;
        }
        na = na + 360;
        na0 = na0 + 360;
        if ((na >= (na0 - da)) && (na <= (na0 + da))) {
            b = true;
        }
        return b;
    }

    /**
     * Calculation gap for all points.
     * @return gap for all points
     */
    public double calculateDelta() {
        //List of possible configurations
        ArrayList<CouplingConfiguration> cl1 = new ArrayList<CouplingConfiguration>();
        ArrayList<CouplingConfiguration> cl2 = new ArrayList<CouplingConfiguration>();
        ArrayList<Double> delta = new ArrayList<Double>();
        //Count of points of half coupling
        int n = Math.round(coupling.getPoints().size() / 2);
        //Points of half coupling 1
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = coupling.getPoint(i + couplingAngle).getCoordX();
            y[i] = coupling.getPoint(i + couplingAngle).getCoordY();
        }
        double[] nx = new double[n];
        double[] ny = new double[n];
        //Cycle for first contact point
        for (int p = 0; p < n; p++) {
            //Shift of half coupling to contact point with index p
            for (int i = 0; i < n; i++) {
                nx[i] = x[i] + (pipeline.getPoint(couplingAngle + p).getCoordX() - x[p]);
                ny[i] = y[i] + (pipeline.getPoint(couplingAngle + p).getCoordY() - y[p]);
            }
            //Calculating angles for every point
            for (int i = 0; i < n; i++) {
                if (i == p) {
                    continue;
                }
                //Intermediate calculations
                double rc = Math.sqrt(nx[i] * nx[i] + ny[i] * ny[i]);
                double rp = Math.sqrt(pipeline.getPoint(couplingAngle + i).getCoordX() * pipeline.getPoint(couplingAngle + i).getCoordX() +
                        pipeline.getPoint(couplingAngle + i).getCoordY() * pipeline.getPoint(couplingAngle + i).getCoordY());
                double dr = rc - rp;
                double ro = Math.sqrt((nx[i] - nx[p]) * (nx[i] - nx[p]) + (ny[i] - ny[p]) * (ny[i] - ny[p]));
                double c1 = pipeline.getPoint(couplingAngle + i).getCoordY() / pipeline.getPoint(couplingAngle + i).getCoordX();
                double c2 = (pipeline.getPoint(couplingAngle + i).getCoordY() - ny[p]) / (pipeline.getPoint(couplingAngle + i).getCoordX() - nx[p]);
                double sing = (c2 - c1) / Math.sqrt(1 + c1 * c1) / Math.sqrt(1 + c2 * c2);
                double fi = Math.abs(dr / ro / sing);
                if ((i > p) && (rc < rp)) {
                    fi = -fi;
                }
                if ((i < p) && (rc > rp)) {
                    fi = -fi;
                }
                //At this angle calculate all gaps and average gap
                double cdavr = 0;
                double cdmin = Double.POSITIVE_INFINITY;
                delta.clear();
                for (int j = 0; j < n; j++) {
                    double cd = 0;
                    if ((p != j) && (i != j)) {
                        double nro = Math.sqrt((nx[j] - nx[p]) * (nx[j] - nx[p]) + (ny[j] - ny[p]) * (ny[j] - ny[p]));
                        double nc1 = pipeline.getPoint(couplingAngle + j).getCoordY() / pipeline.getPoint(couplingAngle + j).getCoordX();
                        double nc2 = (pipeline.getPoint(couplingAngle + j).getCoordY() - ny[p]) / (pipeline.getPoint(couplingAngle + j).getCoordX() - nx[p]);
                        double ndr = Math.abs(fi * nro * (nc2 - nc1) / Math.sqrt(1 + nc1 * nc1) / Math.sqrt(1 + nc2 * nc2));
                        if ((fi > 0) && (j < p)) {
                            ndr = -ndr;
                        }
                        if ((fi < 0) && (j > p)) {
                            ndr = -ndr;
                        }
                        cd = Math.sqrt(nx[j] * nx[j] + ny[j] * ny[j]) -
                                Math.sqrt(pipeline.getPoint(couplingAngle + j).getCoordX() * pipeline.getPoint(couplingAngle + j).getCoordX() +
                                pipeline.getPoint(couplingAngle + j).getCoordY() * pipeline.getPoint(couplingAngle + j).getCoordY()) - ndr;
                    }      
                    cdavr = cdavr + cd;
                    if (cd < cdmin) {
                        cdmin = cd;
                    }
                    delta.add(cd);
                }
                //If all gaps are greater or equal to zero, then include this configuration (p, i) in the list
                if (cdmin >= 0) {
                    CouplingConfiguration cc = new CouplingConfiguration();
                    cc.setIndex1(p);
                    cc.setIndex2(i);
                    cc.setDelta(cdavr / n);
                    for (int j = 0; j < delta.size(); j++) {
                        cc.getAllDelta().add(delta.get(j));
                    }
                    cl1.add(cc);
                }
            }
        }
        //Points of half coupling 2
        x = new double[n];
        y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = coupling.getPoint(i + couplingAngle + n).getCoordX();
            y[i] = coupling.getPoint(i + couplingAngle + n).getCoordY();
        }
        nx = new double[n];
        ny = new double[n];
        //Cycle for first contact point
        for (int p = 0; p < n; p++) {
            //Shift of half coupling to contact point with index p
            for (int i = 0; i < n; i++) {
                nx[i] = x[i] + (pipeline.getPoint(couplingAngle + n + p).getCoordX() - x[p]);
                ny[i] = y[i] + (pipeline.getPoint(couplingAngle + n + p).getCoordY() - y[p]);
            }
            //Calculating angles for every point
            for (int i = 0; i < n; i++) {
                if (i == p) {
                    continue;
                }
                //Intermediate calculations
                double rc = Math.sqrt(nx[i] * nx[i] + ny[i] * ny[i]);
                double rp = Math.sqrt(pipeline.getPoint(couplingAngle + n + i).getCoordX() * pipeline.getPoint(couplingAngle + n + i).getCoordX() +
                        pipeline.getPoint(couplingAngle + n + i).getCoordY() * pipeline.getPoint(couplingAngle + n + i).getCoordY());
                double dr = rc - rp;
                double ro = Math.sqrt((nx[i] - nx[p]) * (nx[i] - nx[p]) + (ny[i] - ny[p]) * (ny[i] - ny[p]));
                double c1 = pipeline.getPoint(couplingAngle + n + i).getCoordY() / pipeline.getPoint(couplingAngle + n + i).getCoordX();
                double c2 = (pipeline.getPoint(couplingAngle + n + i).getCoordY() - ny[p]) / (pipeline.getPoint(couplingAngle + n + i).getCoordX() - nx[p]);
                double sing = (c2 - c1) / Math.sqrt(1 + c1 * c1) / Math.sqrt(1 + c2 * c2);
                double fi = Math.abs(dr / ro / sing);
                if ((i > p) && (rc < rp)) {
                    fi = -fi;
                }
                if ((i < p) && (rc > rp)) {
                    fi = -fi;
                }
                //At this angle calculate all gaps and average gap
                double cdavr = 0;
                double cdmin = Double.POSITIVE_INFINITY;
                delta.clear();
                for (int j = 0; j < n; j++) {
                    double cd = 0;
                    if ((p != j) && (i != j)) {
                        double nro = Math.sqrt((nx[j] - nx[p]) * (nx[j] - nx[p]) + (ny[j] - ny[p]) * (ny[j] - ny[p]));
                        double nc1 = pipeline.getPoint(couplingAngle + n + j).getCoordY() / pipeline.getPoint(couplingAngle + n + j).getCoordX();
                        double nc2 = (pipeline.getPoint(couplingAngle + n + j).getCoordY() - ny[p]) / (pipeline.getPoint(couplingAngle + n + j).getCoordX() - nx[p]);
                        double ndr = Math.abs(fi * nro * (nc2 - nc1) / Math.sqrt(1 + nc1 * nc1) / Math.sqrt(1 + nc2 * nc2));
                        if ((fi > 0) && (j < p)) {
                            ndr = -ndr;
                        }
                        if ((fi < 0) && (j > p)) {
                            ndr = -ndr;
                        }
                        cd = Math.sqrt(nx[j] * nx[j] + ny[j] * ny[j]) -
                                Math.sqrt(pipeline.getPoint(couplingAngle + n + j).getCoordX() * pipeline.getPoint(couplingAngle + n + j).getCoordX() +
                                pipeline.getPoint(couplingAngle + n + j).getCoordY() * pipeline.getPoint(couplingAngle + n + j).getCoordY()) - ndr;
                    }
                    delta.add(cd);
                    cdavr = cdavr + cd;
                    if (cd < cdmin) {
                        cdmin = cd;
                    }
                }
                //If all gaps are greater or equal to zero, then include this configuration (p, i) in the list
                if (cdmin >= 0) {
                    CouplingConfiguration cc = new CouplingConfiguration();
                    cc.setIndex1(p);
                    cc.setIndex2(i);
                    cc.setDelta(cdavr / n);
                    for (int j = 0; j < delta.size(); j++) {
                        cc.getAllDelta().add(delta.get(j));
                    }
                    cl2.add(cc);
                }
            }
        }
        //Analysis of configurations list
        ArrayList<Double> deltaC0 = new ArrayList<Double>();
        ArrayList<Double> deltaC1 = new ArrayList<Double>();
        ArrayList<Double> deltaC2 = new ArrayList<Double>();
        double s1 = Double.POSITIVE_INFINITY;
        for (int i = 0; i < cl1.size(); i++) {
           if (cl1.get(i).getDelta() < s1) {
                s1 = cl1.get(i).getDelta();
                deltaC1.clear();
                for (int j = 0; j < cl1.get(i).getAllDelta().size(); j++) {
                    deltaC1.add(cl1.get(i).getAllDelta().get(j));
                }
            }
        }
        double s2 = Double.POSITIVE_INFINITY;
        for (int i = 0; i < cl2.size(); i++) {
            if (cl2.get(i).getDelta() < s2) {
                s2 = cl2.get(i).getDelta();
                deltaC2.clear();
                for (int j = 0; j < cl2.get(i).getAllDelta().size(); j++) {
                    deltaC2.add(cl2.get(i).getAllDelta().get(j));
                }
            }
        }
        //Create list of gaps
        double s = 0;
        deltaC0.clear();
        for (int i = 0; i < deltaC1.size(); i++) {
            s = s + deltaC1.get(i);
            deltaC0.add(deltaC1.get(i));
        }
        for (int i = 0; i < deltaC2.size(); i++) {
            s = s + deltaC2.get(i);
            deltaC0.add(deltaC2.get(i));
        }
        delta2.clear();
        for (int i = 0; i < deltaC0.size(); i++) {
            if (i < getCouplingAngle()) {
                delta2.add(deltaC0.get(deltaC0.size() - couplingAngle + i));
            } else {
                delta2.add(deltaC0.get(i - couplingAngle));
            }
        }
        return s / deltaC0.size();
    }

    /**
     * Calculation gap for points in area of defect.
     * @return gap for points in area of defect
     */
    public double calculateDeltaDefect() {
        double s = 0;
        int n = pipeline.getPoints().size();
        int m = 0;
        double da = 360 / n;
        for (int i = 0; i < n; i++) {
            double a = da / 2 + i * da;
            if (checkAngleInterval(a, defectAngle, defectAngleInterval)) {
                m++;
                double d = delta2.get(i);
                s = s + d;
            }
        }
        if (m > 0) {
            return s / m;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Create empty dataset for chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetTemp() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Т");
        XYSeries series2 = new XYSeries("М");
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    /**
     * Create dataset for radius chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetRadius() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1;
        XYSeries series2;
        if (parameter.isShowSpline()) {
            series1 = createSeriesSplineRadius(pipeline.getPoints(), "Т");
            series2 = createSeriesSplineRadius(coupling.getPoints(), "М");
        } else {
            //Pipeline
            series1 = new XYSeries("Т");
            for (int j = 0; j < pipeline.getPoints().size(); j++) {
                series1.add(pipeline.getPoints().get(j).getAngle(), pipeline.getPoints().get(j).getRadius());
            }
            if (pipeline.getPoints().size() > 0) {
                series1.add(pipeline.getPoints().get(0).getAngle() + 360, pipeline.getPoints().get(0).getRadius());
            }
            //Coupling
            series2 = new XYSeries("М");
            for (int j = 0; j < coupling.getPoints().size(); j++) {
                series2.add(coupling.getPoints().get(j).getAngle(), coupling.getPoints().get(j).getRadius());
            }
            if (coupling.getPoints().size() > 0) {
                series2.add(coupling.getPoints().get(0).getAngle() + 360, coupling.getPoints().get(0).getRadius());
            }
        }
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    /**
     * Create dataset for distance chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetDistance() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1;
        XYSeries series2;
        if (parameter.isShowSpline()) {
            series1 = createSeriesSplineDistance(pipeline.getPoints(), "Т");
            ArrayList<Point> point = new ArrayList<Point>();
            for (int i = 0; i < pipeline.getPoints().size(); i++) {
                Point p = new Point();
                p.setAngle(pipeline.getPoint(i).getAngle());
                p.setDistance(pipeline.getPoint(i).getDistance() + delta2.get(i));
                point.add(p);
            }
            series2 = createSeriesSplineDistance(point, "М");
        } else {
            //Pipeline
            series1 = new XYSeries("Т");
            for (int j = 0; j < pipeline.getPoints().size(); j++) {
                series1.add(pipeline.getPoint(j).getAngle(), pipeline.getPoint(j).getDistance());
            }
            if (pipeline.getPoints().size() > 0) {
                series1.add(pipeline.getPoint(0).getAngle() + 360, pipeline.getPoint(0).getDistance());
            }
            //Coupling
            series2 = new XYSeries("М");
            for (int j = 0; j < delta2.size(); j++) {
                series2.add(pipeline.getPoint(j).getAngle(), new Double(pipeline.getPoint(j).getDistance() + delta2.get(j)));
            }
            if (delta2.size() > 0) {
                series2.add(new Double(pipeline.getPoint(0).getAngle() + 360), new Double(pipeline.getPoint(0).getDistance() + delta2.get(0)));
            }
        }
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    /**
     * Create spline series for radius chart.
     * @param p list of points
     * @param s name of series
     * @return spline series
     */
    private XYSeries createSeriesSplineRadius(final ArrayList<Point> p, final String s) {
        XYSeries series = new XYSeries(s);
        //Construct spline
        int np = p.size() + 5;
        double[] d = new double[np];
        double[] x = new double[np]; 
        double[] a = new double[np];
        double[] h = new double[np];
        double y, t, t1, t2;
        for (int i = 0; i < np - 5; i++) {
            x[i] = p.get(i).getAngle();
            d[i] = p.get(i).getRadius();
        }
        if (np > 5) {
            x[np - 5] = p.get(0).getAngle() + 360;
            d[np - 5] = p.get(0).getRadius();
            x[np - 4] = p.get(1).getAngle() + 360;
            d[np - 4] = p.get(1).getRadius();
            x[np - 3] = p.get(2).getAngle() + 360;
            d[np - 3] = p.get(2).getRadius();
            x[np - 2] = p.get(3).getAngle() + 360;
            d[np - 2] = p.get(3).getRadius();
            x[np - 1] = p.get(4).getAngle() + 360;
            d[np - 1] = p.get(4).getRadius();
        }
        for (int i = 1; i <= np - 1; i++) {
            h[i] = x[i] - x[i - 1];
        }
        double[] sub = new double[np - 1];
        double[] diag = new double[np - 1];
        double[] sup = new double[np - 1];
        for (int i = 1; i <= np - 2; i++) {
            diag[i] = (h[i] + h[i + 1]) / 3;
            sup[i] = h[i + 1] / 6;
            sub[i] = h[i] / 6;
            a[i] = (d[i + 1] - d[i]) / h[i + 1] - (d[i] - d[i - 1]) / h[i];
        }
        solveTridiag(sub, diag, sup, a, np - 2);
        int precision = 10;
        for (int i = 3; i <= np - 3; i++) {
            int nn;
            if (i == (np - 3)) {
                nn = precision + 1;
            } else {
                nn = precision;
            }
            for (int j = 0; j < nn; j++) {
                t1 = (h[i] * j) / precision;
                t2 = h[i] - t1;
                y = ((-a[i - 1] / 6 * (t2 + h[i]) * t1 + d[i - 1]) * t2 + (-a[i] / 6 * (t1 + h[i]) * t2 + d[i]) * t1) / h[i];
                t = x[i - 1] + t1;
                series.add(t, y);
            }
        }
        return series;
    }

    /**
     * Create spline series for distance chart.
     * @param p list of points
     * @param s name of series
     * @return spline series
     */
    private XYSeries createSeriesSplineDistance(final ArrayList<Point> p, final String s) {
        XYSeries series = new XYSeries(s);
        //Construct spline
        int np = p.size() + 5;
        double[] d = new double[np];
        double[] x = new double[np]; 
        double[] a = new double[np];
        double[] h = new double[np];
        double y, t, t1, t2;
        for (int i = 0; i < np - 5; i++) {
            x[i] = p.get(i).getAngle();
            d[i] = p.get(i).getDistance();
        }
        if (np > 5) {
            x[np - 5] = p.get(0).getAngle() + 360;
            d[np - 5] = p.get(0).getDistance();
            x[np - 4] = p.get(1).getAngle() + 360;
            d[np - 4] = p.get(1).getDistance();
            x[np - 3] = p.get(2).getAngle() + 360;
            d[np - 3] = p.get(2).getDistance();
            x[np - 2] = p.get(3).getAngle() + 360;
            d[np - 2] = p.get(3).getDistance();
            x[np - 1] = p.get(4).getAngle() + 360;
            d[np - 1] = p.get(4).getDistance();
        }
        for (int i = 1; i <= np - 1; i++) {
            h[i] = x[i] - x[i - 1];
        }
        double[] sub = new double[np - 1];
        double[] diag = new double[np - 1];
        double[] sup = new double[np - 1];
        for (int i = 1; i <= np - 2; i++) {
            diag[i] = (h[i] + h[i + 1]) / 3;
            sup[i] = h[i + 1] / 6;
            sub[i] = h[i] / 6;
            a[i] = (d[i + 1] - d[i]) / h[i + 1] - (d[i] - d[i - 1]) / h[i];
        }
        solveTridiag(sub, diag, sup, a, np - 2);
        int precision = 10;
        for (int i = 3; i <= np - 3; i++) {
            int nn;
            if (i == (np - 3)) {
                nn = precision + 1;
            } else {
                nn = precision;
            }
            for (int j = 0; j < nn; j++) {
                t1 = (h[i] * j) / precision;
                t2 = h[i] - t1;
                y = ((-a[i - 1] / 6 * (t2 + h[i]) * t1 + d[i - 1]) * t2 + (-a[i] / 6 * (t1 + h[i]) * t2 + d[i]) * t1) / h[i];
                t = x[i - 1] + t1;
                series.add(t, y);
            }
        }
        return series;
    }

    /**
     * Solve linear system with tridiagonal n by n matrix a
     * @param sub a(i,i-1) = sub[i]
     * @param diag a(i,i) = diag[i]
     * @param sup a(i,i+1) = sup[i]
     * @param b vector b[1:n] is overwritten with solution
     * @param n range of matrix
     */
    private void solveTridiag(final double[] sub, final double[] diag, final double[] sup, final double[] b, final int n) {
        int i;
        for (i = 2; i <= n; i++) {
            sub[i] /= diag[i - 1];
            diag[i] -= sub[i] * sup[i - 1];
            b[i] -= sub[i] * b[i - 1];
        }
        b[n] /= diag[n];
        for (i = n - 1; i >= 1; i--) {
            b[i] = (b[i] - sup[i] * b[i + 1]) / diag[i];
        }
    }

    /**
     * Chart parameters.
     * @return chart parameters
     */
    public ParameterChart getParameter() {
        return parameter;
    }

    /**
     * Cross section of pipeline.
     * @return cross section of pipeline
     */
    public Section getPipeline() {
        return pipeline;
    }

    /**
     * Cross section of coupling.
     * @return cross section of coupling
     */
    public Section getCoupling() {
        return coupling;
    }

    /**
     * List of couplings.
     * @return list of couplings
     */
    public ArrayList<Section> getCouplingList() {
        return couplingList;
    }

    /**
     * Difference between radius of curvature of coupling and pipeline.
     * @return difference between radius of curvature of coupling and pipeline
     */
    public ArrayList<Double> getDelta1() {
        return delta1;
    }

    /**
     * Gap between coupling and pipeline.
     * @return gap between coupling and pipeline
     */
    public ArrayList<Double> getDelta2() {
        return delta2;
    }

    /**
     * Index of coupling 1.
     * @return index of coupling 1
     */
    public int getCoupling1Index() {
        return coupling1Index;
    }

    /**
     * Index of coupling 1.
     * @param aCoupling1Index index of coupling 1
     */
    public void setCoupling1Index(final int aCoupling1Index) {
        coupling1Index = aCoupling1Index;
    }

    /**
     * Index of coupling 2.
     * @return index of coupling 2
     */
    public int getCoupling2Index() {
        return coupling2Index;
    }

    /**
     * Index of coupling 2.
     * @param aCoupling2Index index of coupling 2
     */
    public void setCoupling2Index(final int aCoupling2Index) {
        coupling2Index = aCoupling2Index;
    }

    /**
     * Revers of coupling 1.
     * @return revers of coupling 1
     */
    public int getCoupling1Revers() {
        return coupling1Revers;
    }

    /**
     * Revers of coupling 1.
     * @param aCoupling1Revers revers of coupling 1
     */
    public void setCoupling1Revers(final int aCoupling1Revers) {
        coupling1Revers = aCoupling1Revers;
    }

    /**
     * Revers of coupling 2.
     * @return revers of coupling 2
     */
    public int getCoupling2Revers() {
        return coupling2Revers;
    }

    /**
     * Revers of coupling 2.
     * @param aCoupling2Revers revers of coupling 2
     */
    public void setCoupling2Revers(final int aCoupling2Revers) {
        coupling2Revers = aCoupling2Revers;
    }

    /**
     * Angle position of coupling.
     * @return angle position of coupling
     */
    public int getCouplingAngle() {
        return couplingAngle;
    }

    /**
     * Angle position of coupling.
     * @param aCouplingAngle angle position of coupling
     */
    public void setCouplingAngle(final int aCouplingAngle) {
        couplingAngle = aCouplingAngle;
    }

    /**
     * Using defect position.
     * @return using defect position
     */
    public boolean isUseDefect() {
        return useDefect;
    }

    /**
     * Using defect position.
     * @param aUseDefect using defect position
     */
    public void setUseDefect(final boolean aUseDefect) {
        useDefect = aUseDefect;
    }

    /**
     * Angle position of defect.
     * @return angle position of defect
     */
    public double getDefectAngle() {
        return defectAngle;
    }

    /**
     * Angle position of defect.
     * @param aDefectAngle angle position of defect
     */
    public void setDefectAngle(final double aDefectAngle) {
        defectAngle = aDefectAngle;
    }

    /**
     * Angle interval for calculation.
     * @return angle interval for calculation
     */
    public double getDefectAngleInterval() {
        return defectAngleInterval;
    }

    /**
     * Angle interval for calculation.
     * @param aDefectAngleInterval angle interval for calculation
     */
    public void setDefectAngleInterval(final double aDefectAngleInterval) {
        defectAngleInterval = aDefectAngleInterval;
    }

    /**
     * Angle between defect and coupling border.
     * @return angle between defect and coupling border
     */
    public double getBorderAngleInterval() {
        return borderAngleInterval;
    }

    /**
     * Angle between defect and coupling border.
     * @param aBorderAngleInterval angle between defect and coupling border
     */
    public void setBorderAngleInterval(final double aBorderAngleInterval) {
        borderAngleInterval = aBorderAngleInterval;
    }

    /**
     * Weigth factor for calculation.
     * @return weigth factor for calculation
     */
    public double getWeigthFactor() {
        return weigthFactor;
    }

    /**
     * Weigth factor for calculation.
     * @param aWeigthFactor weigth factor for calculation
     */
    public void setWeigthFactor(final double aWeigthFactor) {
        weigthFactor = aWeigthFactor;
    }
}
