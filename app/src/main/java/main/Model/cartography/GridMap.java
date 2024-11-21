package main.Model.cartography;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import main.Model.BDD.ObjectBox;

import java.util.List;

@Entity
public class GridMap {


    @Id
    private long id;

    private int nbRow;
    private int nbCol;

    @Transient
    private Cell[][] gridCells;

    public GridMap(long id, int nbRow, int nbCol) {
        this.id = id;
        this.nbRow = nbRow;
        this.nbCol = nbCol;
        this.gridCells = new Cell[nbRow][nbCol];
    }

    public GridMap(int nbRow, int nbCol) {
        this.nbRow = nbRow;
        this.nbCol = nbCol;
        this.gridCells = new Cell[nbRow][nbCol];

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNbRow() {
        return nbRow;
    }

    public void setNbRow(int nbRow) {
        this.nbRow = nbRow;
    }

    public int getNbCol() {
        return nbCol;
    }

    public void setNbCol(int nbCol) {
        this.nbCol = nbCol;
    }

    public Cell[][] getGridCells() {
        Box<Cell> cellBox = ObjectBox.get().boxFor(Cell.class);
        List<Cell> cells = cellBox.query().equal(Cell_.gridpMapId, this.id).build().find();
        for (Cell cell : cells) {
            gridCells[cell.getRow()][cell.getCol()] = cell;
        }
        return gridCells;
    }

    public void save(){
        Box<GridMap> gridMapBox = ObjectBox.get().boxFor(GridMap.class);
        Box<Cell> cellBox = ObjectBox.get().boxFor(Cell.class);
        gridMapBox.put(this);
        for (Cell[] cells : gridCells) {
            for (Cell cell : cells) {
                if (cell != null){
                    Cell newCell = cellBox.query().equal(Cell_.row, cell.getRow()).equal(Cell_.col, cell.getCol()).build().findFirst();
                    if (newCell == null) {
                        cellBox.put(cell);
                    }
                }
            }
        }
    }


}
