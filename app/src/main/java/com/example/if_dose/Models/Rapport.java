package com.example.if_dose.Models;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;


public class Rapport extends AppCompatActivity {

    protected  float r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16;
    protected  float rd, rp, rc, rdi, is, obj;
    protected  float gluco0, gluco1, gluco2, gluco3;
    protected  String c1,c2,c3,c4;
    protected  String alimentsPDej, alimentsDej,alimentsCol , alimentsDin;
    protected  String date;
    protected  long longdate;
    private String[] a1, a2, a3, a4;


    public Rapport(long longdate,float r1, float r2, float r3, float r4, float r5, float r6, float r7, float r8,
                   float r9, float r10, float r11, float r12, float r13, float r14, float r15,
                   float r16, float rd, float rp, float rc, float rdi, float is, float obj,
                   float gluco0, float gluco1, float gluco2, float gluco3, String c1, String c2,
                   String c3, String c4, String alimentsPDej, String alimentsDej,
                   String alimentsCol, String alimentsDin, String date) {
        this.longdate = longdate;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
        this.r7 = r7;
        this.r8 = r8;
        this.r9 = r9;
        this.r10 = r10;
        this.r11 = r11;
        this.r12 = r12;
        this.r13 = r13;
        this.r14 = r14;
        this.r15 = r15;
        this.r16 = r16;
        this.rd = rd;
        this.rp = rp;
        this.rc = rc;
        this.rdi = rdi;
        this.is = is;
        this.obj = obj;
        this.gluco0 = gluco0;
        this.gluco1 = gluco1;
        this.gluco2 = gluco2;
        this.gluco3 = gluco3;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.alimentsPDej = alimentsPDej;
        this.alimentsDej = alimentsDej;
        this.alimentsCol = alimentsCol;
        this.alimentsDin = alimentsDin;
        this.date = date;
    }

    public Rapport() {

    }


    public long getL() {
        return longdate;
    }

    public void setL(long l) {
        this.longdate = l;
    }

    public float getR1() {
        return r1;
    }

    public void setR1(float r1) {
        this.r1 = r1;
    }

    public float getR2() {
        return r2;
    }

    public void setR2(float r2) {
        this.r2 = r2;
    }

    public float getR3() {
        return r3;
    }

    public void setR3(float r3) {
        this.r3 = r3;
    }

    public float getR4() {
        return r4;
    }

    public void setR4(float r4) {
        this.r4 = r4;
    }

    public float getR5() {
        return r5;
    }

    public void setR5(float r5) {
        this.r5 = r5;
    }

    public float getR6() {
        return r6;
    }

    public void setR6(float r6) {
        this.r6 = r6;
    }

    public float getR7() {
        return r7;
    }

    public void setR7(float r7) {
        this.r7 = r7;
    }

    public float getR8() {
        return r8;
    }

    public void setR8(float r8) {
        this.r8 = r8;
    }

    public float getR9() {
        return r9;
    }

    public void setR9(float r9) {
        this.r9 = r9;
    }

    public float getR10() {
        return r10;
    }

    public void setR10(float r10) {
        this.r10 = r10;
    }

    public float getR11() {
        return r11;
    }

    public void setR11(float r11) {
        this.r11 = r11;
    }

    public float getR12() {
        return r12;
    }

    public void setR12(float r12) {
        this.r12 = r12;
    }

    public float getR13() {
        return r13;
    }

    public void setR13(float r13) {
        this.r13 = r13;
    }

    public float getR14() {
        return r14;
    }

    public void setR14(float r14) {
        this.r14 = r14;
    }

    public float getR15() {
        return r15;
    }

    public void setR15(float r15) {
        this.r15 = r15;
    }

    public float getR16() {
        return r16;
    }

    public void setR16(float r16) {
        this.r16 = r16;
    }

    public float getRd() {
        return rd;
    }

    public void setRd(float rd) {
        this.rd = rd;
    }

    public float getRp() {
        return rp;
    }

    public void setRp(float rp) {
        this.rp = rp;
    }

    public float getRc() {
        return rc;
    }

    public void setRc(float rc) {
        this.rc = rc;
    }

    public float getRdi() {
        return rdi;
    }

    public void setRdi(float rdi) {
        this.rdi = rdi;
    }

    public float getIs() {
        return is;
    }

    public void setIs(float is) {
        this.is = is;
    }

    public float getObj() {
        return obj;
    }

    public void setObj(float obj) {
        this.obj = obj;
    }

    public float getGluco0() {
        return gluco0;
    }

    public void setGluco0(float gluco0) {
        this.gluco0 = gluco0;
    }

    public float getGluco1() {
        return gluco1;
    }

    public void setGluco1(float gluco1) {
        this.gluco1 = gluco1;
    }

    public float getGluco2() {
        return gluco2;
    }

    public void setGluco2(float gluco2) {
        this.gluco2 = gluco2;
    }

    public float getGluco3() {
        return gluco3;
    }

    public void setGluco3(float gluco3) {
        this.gluco3 = gluco3;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getAlimentsPDej() {
        return alimentsPDej;
    }

    public void setAlimentsPDej(String alimentsPDej) {
        this.alimentsPDej = alimentsPDej;
    }

    public String getAlimentsDej() {
        return alimentsDej;
    }

    public void setAlimentsDej(String alimentsDej) {
        this.alimentsDej = alimentsDej;
    }

    public String getAlimentsCol() {
        return alimentsCol;
    }

    public void setAlimentsCol(String alimentsCol) {
        this.alimentsCol = alimentsCol;
    }

    public String getAlimentsDin() {
        return alimentsDin;
    }

    public void setAlimentsDin(String alimentsDin) {
        this.alimentsDin = alimentsDin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String[] getA1() {return getAlimentsPDej().split(";");}
    public String[] getA2() {return getAlimentsDej().split(";");}
    public String[] getA3() {return getAlimentsCol().split(";");}
    public String[] getA4() {return getAlimentsDin().split(";");}


}
