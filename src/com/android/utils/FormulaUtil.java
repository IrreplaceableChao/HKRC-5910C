package com.android.utils;


public class FormulaUtil {
    public static final double $F$1 = 0.017453293;//
    public static double $B$1 = 0;// 子午线收敛角
    public static double $D$1 = 0;// 磁偏角
    public static final double $H$1 = 3.141592654;//
    public static double $E$2= 0 ;//设计磁方位角
    public static double $C$2= 0 ;//设计倾角
    public static double kongshen = 0;
    public static double shang = 0;
    public static double zuo = 0;
    public static double zong= 0;

    // 孔深
    private double[] b;
    // 倾角
    private double[] c;
    // 磁方位
    private double[] d;
    private int num;
    // 井斜
    private double i[];
    private double j[];
    private double k[]; // K （坐标方位增量△Q）
    private double l[]; // （孔深增量△L） = 孔深-（孔深-1）
    private double m[];// 井眼曲率

    private double r[];
    private double o[];
    private double q[];
    private double p[];

//    double s[];
//    double t[];
//    double u[];
//    double v[];

    private double x[];//南北
    private double y[];//东西
    private double z[];//垂直

    private double e_w[];
    private double e_x[];
    private  double e_y[];
    private double e_z[];
    private  double aa[];



    public FormulaUtil(double[] b1, double[] c1, double[] d1, int num) {
        this.num = num;
            // 井斜
        i = new double[num];
        j = new double[num];
        k = new double[num]; // K （坐标方位增量△Q）
        l = new double[num]; // （孔深增量△L） = 孔深-（孔深-1）
        m = new double[num];// 井眼曲率

        r = new double[num];
        o = new double[num];
        q = new double[num];
        p = new double[num];

//        s = new double[num];
//        t = new double[num];
//        u = new double[num];
//        v = new double[num];

        /**@author AChao time：2017-2-10 11:02:36**/
        e_w = new double[num];
        e_x = new double[num];
        e_y = new double[num];
        e_z = new double[num];
        aa = new double[num];

        x = new double[num];//南北
        y = new double[num];//东西
        z = new double[num];//垂直

        b = new double[b1.length];
        c = new double[c1.length];
        d = new double[d1.length];


        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(c1, 0, c, 0, c1.length);
        System.arraycopy(d1, 0, d, 0, d1.length);
        I_();
        J_();
        K_();
        M_();
        L_();
        O_();
        R_();
        Q_();
        P_();

//        S_();
//        T_();
//        U_();
//        V_();
        E_W();
        E_X();
        E_Y();
        E_Z();
        AA();

        for (int i = 1; i < x.length; i++) {
            x[i] = BD(x[i - 1] + e_y[i]);
        }
        for (int i = 1; i < y.length; i++) {
            y[i] = BD(y[i - 1] + e_z[i]);
        }
        for (int i = 1; i < z.length; i++) {
            z[i] = BD(z[i - 1] + aa[i]);
        }
    }

    public double[] x_() {
        return x;
    }
    public double[] y_() {
        return y;
    }
    public double[] z_() { return z;
    }


    // 井斜
    private void I_() {
        for (int i1 = 0; i1 < i.length; i1++) {
            i[i1] = c[i1] + 90;
            i[i1] = BD(i[i1]);
        }
        i[0] = i[1];
        // System.arraycopy(c, 0, i, 0, c.length);
    }

    private void K_() {
        for (int i = 1; i < k.length; i++) {
            if (Math.abs(j[i] - j[i - 1]) <= 180) {
                k[i] = j[i] - j[i - 1];
            } else {
                if ((j[i] - j[i - 1]) >= 0) {
                    k[i] = j[i] - j[i - 1] - 360;
                } else {
                    k[i] = j[i] - j[i - 1] + 360;
                }
            }
            k[i] = BD(k[i]);
        }
    }

    // 坐标方位
    private void J_() {
        for (int i = 0; i < j.length; i++) {
            if ((d[i] + $D$1 - $B$1) >= 360) {
                j[i] = d[i] + $D$1 - $B$1 - 360;
            } else {
                if ((d[i] + $D$1 - $B$1) < 0) {
                    j[i] = d[i] + $D$1 - $B$1 + 360;
                } else {
                    j[i] = d[i] + $D$1 - $B$1;
                }
            }
        }
        j[0] = j[1];
    }

    // 井眼曲率
    private void M_() {
//        for (int i1 = 1; i1 < m.length; i1++) {
//            m[i1] = Math.acos(Math.cos( i[i1] * $F$1))) * Math.cos( i[i1 - 1] * $F$1))) + Math.sin( i[i1] * $F$1))) * Math.sin( i[i1 - 1] * $F$1))) * Math.cos( k[i1] * $F$1)))) / $F$1;
//        }

        for (int i1 = 1; i1 < m.length; i1++) {
            m[i1] = BD6(Math.acos(BD6(Math.cos(i[i1] * $F$1) * Math.cos(i[i1 - 1] * $F$1) + Math.sin(i[i1] * $F$1) * Math.sin(i[i1 - 1] * $F$1) * Math.cos(k[i1] * $F$1)))) / $F$1;
            m[i1] = BD6(m[i1]);
        }
    }

    private void L_() {
        for (int i1 = 1; i1 < l.length; i1++) {
            l[i1] = b[i1] - b[i1 - 1];
        }
    }

    private void R_() {
        for (int i1 = 1; i1 < r.length; i1++) {
            if (m[i1] == 0) {
                r[i1] = 0;
            } else {
                r[i1] = o[i1] * Math.sin(m[i1] * $F$1);
            }
            r[i1] = BD(r[i1]);
        }
    }

    private void O_() {
        for (int i1 = 1; i1 < o.length; i1++) {
            if (m[i1] == 0) {
                o[i1] = 0;
            } else {
                o[i1] = (180 * l[i1]) / ($H$1 * m[i1]);
            }
            o[i1] = BD(o[i1]);
        }
    }

    private void Q_() {
        for (int i1 = 1; i1 < q.length; i1++) {
            if (m[i1] == 0) {
                q[i1] = 0;
            } else {
                q[i1] = o[i1] * (1 - Math.cos(m[i1] * $F$1));
            }
            q[i1] = BD(q[i1]);
        }
    }

    private void P_() {
        for (int i1 = 1; i1 < p.length; i1++) {
            if (m[i1] == 0) {
                p[i1] = 0;
            } else {
                double dd =Math.sin(k[i1] * $F$1);
                double cc = Math.sin(i[i1] * $F$1);
                double bb =  Math.sin(m[i1] * $F$1);
//                double aa = ;
                p[i1] = Math.asin(cc*dd) /bb / $F$1;
            }
            p[i1] = BD(p[i1]);
        }
    }
    /**
    // S 南北增量△N ：
    private void S_() {
        for (int i1 = 1; i1 < s.length; i1++) {
            if (m[i1] == 0) {
                s[i1] = l[i1] * Math.sin( i[i1] * $F$1) * Math.cos( j[i1] * $F$1);
            } else {
                s[i1] = r[i1] * Math.sin( i[i1 - 1] * $F$1) * Math.cos( j[i1 - 1] * $F$1)
                        + q[1] * (Math.cos( i[i1 - 1] * $F$1) * Math.cos( j[i1 - 1] * $F$1)
                        * Math.cos( p[i1] * $F$1) - Math.sin( j[i1 - 1] * $F$1)
                        * Math.sin( p[i1] * $F$1));
            }
            s[i1] = BD(s[i1]);
        }
    }

    // T 东西增量△E
    private void T_() {
        for (int i1 = 1; i1 < t.length; i1++) {
            if (m[i1] == 0) {
                t[i1] = l[i1] * Math.sin( i[i1] * $F$1) * Math.sin( j[i1] * $F$1);
            } else {
                t[i1] = r[i1] * Math.sin( i[i1 - 1] * $F$1) * Math.sin( j[i1 - 1] * $F$1) + q[1] * (Math.cos( i[i1 - 1] * $F$1) * Math.sin( j[i1 - 1] * $F$1) * Math.cos( p[i1] * $F$1) + Math.cos( j[i1 - 1] * $F$1) * Math.sin( p[i1] * $F$1));
            }
            t[i1] = BD(t[i1]);
        }
    }

    // U 垂直增量△H
    private void U_() {
        for (int i1 = 1; i1 < u.length; i1++) {
            if (m[i1] == 0) {
                u[i1] = -l[i1] * Math.cos( i[i1] * $F$1);
            } else {
                u[i1] = -(r[i1] * Math.cos( i[i1 - 1] * $F$1) - q[i1] * Math.sin( i[i1 - 1] * $F$1) * Math.cos( p[i1] * $F$1));
            }
            u[i1] = BD(u[i1]);
        }
    }

    // V 水平投影增量△S
    private void V_() {
        for (int i1 = 1; i1 < v.length; i1++) {
            if (m[i1] == 0) {
                v[i1] = l[i1] * Math.sin( i[i1] * $F$1);
            } else {
                if (k[i1] == 0) {
                    v[i1] = Math.sqrt(s[i1] * s[i1] + t[i1] * t[i1]);
                } else {
                    v[i1] = ($H$1 / 180) * o[i1] * Math.tan(( m[i1] * $F$1) / 2) * ((k[i1] / 2) / Math.tan(( k[i1] * $F$1) / 2)) * (Math.sin( i[i1 - 1] * $F$1) + Math.sin( i[i1] * $F$1));
                }
            }
            v[i1] = BD(v[i1]);
        }
    }

    */

    /**国标给的公式经过推倒在井眼曲率之后有问题，在此用网及书籍内给出的公式
     * @author AChao time：2017-2-10 11:02:36
     * */
    // 中间值n
    private void E_W() {
        for (int i1 = 1; i1 < e_w.length; i1++) {
            if (m[i1] == 0) {
                e_w[i1] = 0.5;
            } else {
                if (m[i1]* $F$1 >= 0.00001) {
                    e_w[i1] = Math.tan((m[i1] * $F$1) / 2) / (m[i1] * $F$1);
                } else {
                    e_w[i1] = (1 / 2 + Math.pow(m[i1] * $F$1,2) / 24 + Math.pow(m[i1] * $F$1,4)  / 240 +  Math.pow(m[i1] * $F$1,6)* 17 / 40320 + Math.pow(m[i1] * $F$1,8) * 31 / 725760);
                }
            }
            e_w[i1] = BD(e_w[i1]);
//            =IF(M5=0,0.5,IF(M5*$F$1>=0.00001,TAN((M5*$F$1)/2)/(M5*$F$1),(1/2+(M5*$F$1)^2/24+(M5*$F$1)^4/240+(M5*$F$1)^6*17/40320+(M5*$F$1)^8*31/725760)))  与excel表一致
        }
    }
    // 水平投影增量△S
    private void E_X() {
        for (int i1 = 1; i1 < e_x.length; i1++) {
            if (m[i1] == 0) {
                e_x[i1] = l[i1]*Math.sin(i[i1]*$F$1);
            } else {
                e_x[i1] = e_w[i1]*l[i1]*(Math.sin(i[i1-1]*$F$1)+Math.sin(i[i1]*$F$1));
            }
            e_x[i1] = BD(e_x[i1]);
//            =IF(M5=0,L5*SIN(I5*$F$1),W5*L5*(SIN(I4*$F$1)+SIN(I5*$F$1)))   与excel表一致
        }
    }
    // 南北增量△N
    private void E_Y() {
        for (int i1 = 1; i1 < e_y.length; i1++) {
            if (m[i1] == 0) {
                e_y[i1] = l[i1]*Math.sin(i[i1]*$F$1)*Math.cos(j[i1]*$F$1);
            } else {
                e_y[i1] =e_w[i1]*l[i1]*(Math.sin(i[i1-1]*$F$1)*Math.cos(j[i1-1]*$F$1)+ Math.sin(i[i1]*$F$1)*Math.cos(j[i1]*$F$1));
            }
            e_y[i1] = BD(e_y[i1]);
//            =IF(M5=0,L5*SIN(I5*$F$1)*COS(J5*$F$1),W5*L5*(SIN(I4*$F$1)*COS(J4*$F$1)+SIN(I5*$F$1)*COS(J5*$F$1)))  与excel表一致
        }
    }
    // 东西增量△E
    private void E_Z() {
        for (int i1 = 1; i1 < e_x.length; i1++) {
            if (m[i1] == 0) {
                e_z[i1] = l[i1]*Math.sin(i[i1]*$F$1)*Math.sin(j[i1]*$F$1);
            } else {
                e_z[i1] =e_w[i1]*l[i1]*(Math.sin(i[i1-1]*$F$1)*Math.sin(j[i1-1]*$F$1)+ Math.sin(i[i1]*$F$1)*Math.sin(j[i1]*$F$1));
            }
            e_z[i1] = BD(e_z[i1]);
//            =IF(M5=0,L5*SIN(I5*$F$1)*SIN(J5*$F$1),W5*L5*(SIN(I4*$F$1)*SIN(J4*$F$1)+SIN(I5*$F$1)*SIN(J5*$F$1)))   与excel表一致
        }
    }
    // 垂直增量△H
    private void AA() {
        for (int i1 = 1; i1 < aa.length; i1++) {
            if (m[i1] == 0) {
                aa[i1] =-l[i1]*Math.cos(i[i1]*$F$1);
            } else {
                aa[i1] =-e_w[i1]*l[i1]*(Math.cos(i[i1-1]*$F$1)+Math.cos(i[i1]*$F$1));
            }
            aa[i1] = BD(aa[i1]);
//            =IF(M5=0,-L5*COS(I5*$F$1),-W5*L5*(COS(I4*$F$1)+COS(I5*$F$1)))    与excel表一致
        }
    }
    private double BD(double dou){
//        if (Double.isInfinite(dou) || Double.isNaN(dou)) {
////            throw new NumberFormatException("Infinity or NaN: " + dou);
//            Log.e("NumberFormatException","Infinity or NaN: " + dou);
//            return 0;
//        }else {
            java.math.BigDecimal b = new java.math.BigDecimal(dou);
            double myNum3 = b.setScale(6, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
            return myNum3;
//        }
    }

    private double BD2(double dou){
//        if (Double.isInfinite(dou) || Double.isNaN(dou)) {
////            throw new NumberFormatException("Infinity or NaN: " + dou);
//            Log.e("NumberFormatException","Infinity or NaN: " + dou);
//            return 0;
//        }else {
        java.math.BigDecimal b = new java.math.BigDecimal(dou);
        double myNum3 = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        return myNum3;
//        }
    }

    private double BD6(double dou){
        java.math.BigDecimal b = new java.math.BigDecimal(dou);
        double myNum3 = b.setScale(6, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        return myNum3;
    }
}
