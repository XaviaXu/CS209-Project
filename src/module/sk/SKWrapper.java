package module.sk;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "SK")
@XmlAccessorType(XmlAccessType.FIELD)

public class SKWrapper {
    @XmlElement
    private int n;
    @XmlElement
    private int[][] square;
    @XmlElement
    private int[][] cnst;

    @XmlElement
    private int generation;
    @XmlElement
    private int error;


    @XmlTransient
    public int getN(){return n;}

    public void setN(int n) {
        this.n = n;
    }

    public void setSquare(int[][] square) {
        this.square = square;
    }

    public void setCnst(int[][] cnst) {
        this.cnst = cnst;
    }

    @XmlTransient
    public int[][]getSquare(){return square;}

    @XmlTransient
    public int[][]getCnst(){return cnst;}

    @XmlTransient
    public int getGeneration(){return generation;}
    public void setGeneration(int generation){this.generation = generation;}

    @XmlTransient
    public int getError(){return error;}
    public void setError(int error){this.error = error;}



}
