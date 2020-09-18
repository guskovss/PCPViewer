/*
 * Pipeline Coupling Position Viewer
 */
package ru.gss.pcpviewer.calculation;

import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import ru.gss.pcpviewer.data.DataList;

/**
 * Model of sections table.
 * @version 1.1.0 31.03.2020
 * @author Sergey Guskov
 */
public class SectionTableModel extends AbstractTableModel {

    /**
     * Data.
     */
    private DataList data;
    /**
     * Headers of table columns.
     */
    private String[] colNames = {"β, град", "ρт, мм", "ρм, мм", "Δρ, мм", "Δr, мм"};

    /**
     * Constructor.
     * @param aData data
     */
    public SectionTableModel(final DataList aData) {
        data = aData;
    }

    /**
     * Header of table column.
     * @param column index of table column
     * @return header of table column
     */
    @Override
    public String getColumnName(final int column) {
        return colNames[column];
    }

    /**
     * Count of table column.
     * @return count of table column
     */
    public int getColumnCount() {
        return 5;
    }

    /**
     * Count of table row.
     * @return count of table row
     */
    public int getRowCount() {
        return data.getPipeline().getPoints().size();
    }

    /**
     * Class of table column.
     * @param columnIndex index of table column
     * @return class of table column
     */
    @Override
    public Class < ? > getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            default:
                return String.class;
        }
    }

    /**
     * Convertation number to string.
     * @param value number
     * @param format count of symbols after separator
     * @return string representation of number
     */
    private String convertToString(final Double value, final int format) {
        if (value == null) {
            return "";
        }
        return String.format(Locale.US, "%." + format + "f", value);
    }

    /**
     * Value of table cell.
     * @param rowIndex index of table row
     * @param columnIndex index of table column
     * @return value of table cell
     */
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return convertToString(data.getPipeline().getPoint(rowIndex).getAngle(), 2);
            case 1:
                return convertToString(data.getPipeline().getPoint(rowIndex).getRadius(), 2);
            case 2:
                return convertToString(data.getCoupling().getPoint(rowIndex).getRadius(), 2);
            case 3:
                return convertToString(data.getDelta1().get(rowIndex), 2);
            case 4:
                return convertToString(data.getDelta2().get(rowIndex), 2);
            default:
                return null;
        }
    }
}
