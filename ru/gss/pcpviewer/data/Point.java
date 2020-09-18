/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.data;

/**
 * Point of section.
 * @version 1.1.0 27.03.2020
 * @author Sergey Guskov
 */
public class Point {

    /**
     * Angular position.
     */
    private Double angle;
    /**
     * Radius of curvature.
     */
    private Double radius;
    /**
     * Coordinate x.
     */
    private Double coordX;
    /**
     * Coordinate y.
     */
    private Double coordY;
    /**
     * Distance to the conditional center.
     */
    private Double distance;
    
    /**
     * Constructor.
     */
    public Point() {
    }

    /**
     * Angular position.
     * @return angular position
     */
    public Double getAngle() {
        return angle;
    }

    /**
     * Angular position.
     * @param aAngle angular position
     */
    public void setAngle(final Double aAngle) {
        angle = aAngle;
    }

    /**
     * Radius of curvature.
     * @return radius of curvature
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * Radius of curvature.
     * @param aRadius radius of curvature
     */
    public void setRadius(final Double aRadius) {
        radius = aRadius;
    }

    /**
     * Coordinate x.
     * @return coordinate x
     */
    public Double getCoordX() {
        return coordX;
    }

    /**
     * Coordinate x.
     * @param aCoordX coordinate x
     */
    public void setCoordX(final Double aCoordX) {
        coordX = aCoordX;
    }

    /**
     * Coordinate y.
     * @return coordinate y
     */
    public Double getCoordY() {
        return coordY;
    }

    /**
     * Coordinate y.
     * @param aCoordY coordinate y
     */
    public void setCoordY(final Double aCoordY) {
        coordY = aCoordY;
    }

    /**
     * Distance to the conditional center.
     * @return distance to the conditional center
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Distance to the conditional center.
     * @param aDistance distance to the conditional center
     */
    public void setDistance(final Double aDistance) {
        distance = aDistance;
    }
}
