/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.data;

import java.util.ArrayList;

/**
 * Section of pipeline.
 * @version 1.1.0 27.03.2020
 * @author Sergey Guskov
 */
public class Section {

    /**
     * Name.
     */
    private String name;
    /**
     * Diameter.
     */
    private Double diameter;
    /**
     * List of points.
     */
    private ArrayList<Point> points;
    /**
     * Count of iterations.
     */
    private int calculateIndex;
    /**
     * Calculated radius.
     */
    private double calculateRadius;
    /**
     * Correct calculation.
     */
    private boolean calculateCorrect;
       
    /**
     * Constructor.
     */
    public Section() {
        points = new ArrayList<Point>();
    }

    /**
     * Calculate default coordinates.
     */
    public void calculateDefaultCoordinate() {
        int n = points.size();
        double r = diameter / 2;
        for (int i = 0; i < n; i++) {
            double b = points.get(i).getAngle() * Math.PI / 180;
            points.get(i).setCoordX(r * Math.sin(b));
            points.get(i).setCoordY(r * Math.cos(b));
            points.get(i).setDistance(r);
        }
    }

    /**
     * Calculate coordinates.
     */
    public void calculateCoordinate() {
        int n = points.size();
        if (n > 0) {
            //Parameters of variations
            int[] varCount = {10000};
            double[] varStep = {2e-3};
            //Main variables
            double[] r = new double[n];
            double[] ta = new double[n];
            double[] x = new double[n];
            double[] y = new double[n];
            double[] c = new double[n];
            double[] ra = new double[n];
            double[] da = new double[n];
            double s = 0;
            //Angles
            double ds = Math.PI * diameter / n / 1e3;
            for (int i = 0; i < n; i++) {
                ta[i] = Math.tan(-ds * 1e3 / points.get(i).getRadius());
            }
            //Variations
            for (int v = 0; v < varCount.length; v++) {
                //Main variation
                calculateIndex = 0;
                //Init variable
                for (int i = 0; i < n; i++) {
                    r[i] = diameter / 2 / 1e3;
                }
                double dr = varStep[v];
                for (int k = 0; k < varCount[v]; k++) {
                    calculateIndex = k;
                    //Point coordinates
                    for (int i = 0; i < n; i++) {
                        double b = points.get(i).getAngle() * Math.PI / 180;
                        x[i] = r[i] * Math.sin(b);
                        y[i] = r[i] * Math.cos(b);
                    }
                    //Line coefficients
                    for (int i = 0; i < n; i++) {
                        if (i < n - 1) {
                            c[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
                        } else {
                            c[i] = (y[0] - y[i]) / (x[0] - x[i]);
                        }
                    }
                    //Calculate angles
                    for (int i = 0; i < n; i++) {
                        if (i > 0) {
                            ra[i] = (c[i] - c[i - 1]) / (1 + c[i] * c[i - 1]);
                        } else {
                            ra[i] = (c[i] - c[n - 1]) / (1 + c[i] * c[n - 1]);
                        }
                    }
                    //Compare angles and calculate results
                    s = 0;
                    for (int i = 0; i < n; i++) {
                        da[i] = ra[i] - ta[i];
                        s = s + da[i] * da[i];
                    }
                    s = Math.sqrt(s / n);
                    if (s < 1e-3) {
                        break;
                    }
                    //Change radiuses
                    for (int i = 0; i < n; i++) {
                        r[i] = r[i] + dr * da[i];
                    }
                }
                //Calculate average radius
                double cr = 0;
                for (int i = 0; i < n; i++) {
                    cr = cr + r[i];
                }
                calculateRadius = cr * 1e3 / n;
                //Break conditions
                calculateCorrect = false;
                if ((calculateIndex < (varCount[v] - 1)) && ((calculateRadius > 0.95 * (diameter / 2)) && (calculateRadius < 1.05 * (diameter / 2)))) {
                    calculateCorrect = true;
                    break;
                }
            }
            //Save results
            if (calculateCorrect) {
                for (int i = 0; i < n; i++) {
                    points.get(i).setCoordX(x[i] * 1e3);
                    points.get(i).setCoordY(y[i] * 1e3);
                    points.get(i).setDistance(r[i] * 1e3);
                }
            } else {
                calculateDefaultCoordinate();
            }
        }
    }
    
    /**
     * Name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name.
     * @param aName name
     */
    public void setName(final String aName) {
        name = aName;
    }

    /**
     * Diameter.
     * @return diameter
     */
    public Double getDiameter() {
        return diameter;
    }

    /**
     * Diameter.
     * @param aDiameter diameter
     */
    public void setDiameter(final Double aDiameter) {
        diameter = aDiameter;
    }

    /**
     * List of points.
     * @return list of points
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Point.
     * @param index index of point
     * @return point
     */
    public Point getPoint(final int index) {
        int n = points.size();
        int i = index;
        if (i >= n) {
            i = i - n;
        }
        return points.get(i);
    }

    /**
     * Count of iterations.
     * @return count of iterations
     */
    public int getCalculateIndex() {
        return calculateIndex;
    }

    /**
     * Calculated radius.
     * @return calculated radius
     */
    public double getCalculateRadius() {
        return calculateRadius;
    }

    /**
     * Correct calculation.
     * @return correct calculation
     */
    public boolean isCalculateCorrect() {
        return calculateCorrect;
    }
}
