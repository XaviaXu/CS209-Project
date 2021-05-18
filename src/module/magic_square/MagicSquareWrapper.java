package module.magic_square;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "games")
@XmlAccessorType(XmlAccessType.FIELD)

public class MagicSquareWrapper {
    private int n;
    private int[][] board;
    private int[][] cnst;
    private int mn;
//    private List<Mutate> mutates;
    private int Generation;
    private int error;
//
    @XmlElement(name = "n")
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }

    @XmlElement(name = "cnst")
    public int[][] getCnst() {
        return cnst;
    }

    public void setCnst(int[][] cnst) {
        this.cnst = cnst;
    }
//
//
    @XmlElement(name = "error")
    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
//
    @XmlElement(name = "board")
    public int[][] getBoard() {
        return board;
    }
    public void setBoard(int[][] board) {
        this.board = board;
    }

    @XmlElement(name = "mn")
    public int getMn() {
        return mn;
    }
    public void setMn(int mn) {
        this.mn = mn;
    }

    @XmlElement(name = "generation")
    public int getGeneration() {
        return Generation;
    }
    public void setGeneration(int generation) {
        Generation = generation;
    }


//    public List<Mutate> getMutates() {
//        return mutates;
//    }
//
//    @XmlElement(name = "mutates")
//    public void setMutates(List<Mutate> mutates) {
//        this.mutates = mutates;
//    }

}

