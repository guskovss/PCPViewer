/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.data;

import java.util.ArrayList;

/**
 * Configuration of coupling.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class CouplingConfiguration {

    /**
     * Index of the first point of contact.
     */
    private int index1;
    /**
     * Index of the second point of contact.
     */
    private int index2;
    /**
     * Average gap.
     */
    private double delta;
    /**
     * List of gaps.
     */
    private ArrayList<Double> allDelta;
    
    /**
     * Constructor.
     */
    public CouplingConfiguration() {
        allDelta = new ArrayList<Double>();
    }

    /**
     * Index of the first point of contact.
     * @return index of the first point of contact
     */
    public int getIndex1() {
        return index1;
    }

    /**
     * Index of the first point of contact.
     * @param aIndex1 index of the first point of contact
     */
    public void setIndex1(final int aIndex1) {
        index1 = aIndex1;
    }

    /**
     * Index of the second point of contact.
     * @return index of the second point of contact
     */
    public int getIndex2() {
        return index2;
    }

    /**
     * Index of the second point of contact.
     * @param aIndex2 index of the second point of contact
     */
    public void setIndex2(final int aIndex2) {
        index2 = aIndex2;
    }

    /**
     * Average gap.
     * @return average gap
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Average gap.
     * @param aDelta average gap
     */
    public void setDelta(final double aDelta) {
        delta = aDelta;
    }

    /**
     * List of gaps.
     * @return list of gaps
     */
    public ArrayList<Double> getAllDelta() {
        return allDelta;
    }
}
