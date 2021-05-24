package module.ms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "games")
@XmlAccessorType(XmlAccessType.FIELD)

public class MSWrapper {
    public int mn;

    public int evl1;
    public int evl2;
    public int[] rowErr;
    public int[] colErr;
    public int[] diaErr; // 0: \, 1: /
    public boolean[] rowCnst;
    public boolean[] colCnst;
    //

    @XmlElement(name = "Cnst")
    public boolean[] getColCnst() {
        return colCnst;
    }

    public void setColCnst(boolean[] colCnst) {
        this.colCnst = colCnst;
    }
    @XmlElement(name = "mn")
    public int getMn() {
        return mn;
    }
    public void setMn(int mn) {
        this.mn = mn;
    }
    @XmlElement(name = "Evl1")
    public int getEvl1() {
        return evl1;
    }
    public void setEvl1(int evl1) {
        this.evl1 = evl1;
    }
    @XmlElement(name = "Evl2")
    public int getEvl2() {
        return evl2;
    }
    public void setEvl2(int evl2) {
        this.evl2 = evl2;
    }

    @XmlElement(name = "RowErr")
    public int[] getRowErr() {
        return rowErr;
    }
    public void setRowErr(int[] rowErr) {
        this.rowErr = rowErr;
    }

    @XmlElement(name = "ColErr")
    public int[] getColErr() {
        return colErr;
    }

    public void setColErr(int[] colErr) {
        this.colErr = colErr;
    }

    @XmlElement(name = "DiaErr")
    public int[] getDiaErr() {
        return diaErr;
    }

    public void setDiaErr(int[] diaErr) {
        this.diaErr = diaErr;
    }

    @XmlElement(name = "RowCnst")
    public boolean[] getRowCnst() {
        return rowCnst;
    }
    public void setRowCnst(boolean[] rowCnst) {
        this.rowCnst = rowCnst;
    }
}

