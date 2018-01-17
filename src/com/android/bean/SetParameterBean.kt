package com.android.bean

import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table

/**
 * Created by Administrator on 2018/1/3.
 */
@Table(name = "parameter")
 class SetParameterBean{
 @Column(name = "id", isId = true)
 var id: Int = 0
 @Column(name = "declination")
 var declination: Double = 0.0
 @Column(name = "convergenceOfMeridians")
 var convergenceOfMeridians: Double = 0.0
}
//@Table(name = "info")
//public class SetValueBean {
// @Column(name = "id", isId = true)
// private int id;
// @Column(name = "qinjiao")
// private double qinjiao;
// @Column(name = "fangwei")