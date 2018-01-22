package com.android.MPFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.MP.Data;
import com.android.antiexplosionphone.R;
import com.android.utils.FormulaUtil;
import com.android.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import static com.android.MP.Data.ks;
import static com.android.MP.Data.zy;
import static com.android.utils.FormulaUtil.$C$2;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;
import static com.android.utils.FormulaUtil.kongshen;
import static com.android.utils.FormulaUtil.zuo;


public class Horizontal extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mLineChart;
    private View view;
    int sj_x; //设计曲线X轴坐标

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_horizontal, container, false);
        findview();
        setTopBottom((float) zuo, getZY_Max() + 30);
        init();
        return view;
    }

    private void findview() {
        mLineChart = (LineChart) view.findViewById(R.id.chart);
    }

    private void init() {

        sj_x = (int) (kongshen * Math.abs(Math.cos($C$2 * $F$1)));
        XAxis xAxis = mLineChart.getXAxis();
        mLineChart.getAxisRight().setEnabled(false);//设置y轴的右侧是否显示坐标
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        LineData mLineData = getLineData();
        showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));//这里调背景颜色
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    ;

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);//  xy轴是否同时缩放
        lineChart.setBackgroundColor(color);// 设置背景
        // add data
        lineChart.setData(lineData);  // 设置数据
    }

    /**
     * 生成一个数据
     */
    private LineData getLineData() {
        int length = ks.length;
        FormulaUtil formula = new FormulaUtil(ks, Data.qj, Data.fw, length);
        float[] x = new float[length];
        float[] y = new float[length];
        double min = 0;
        for (int i = 0; i < length; i++) {
            x[i] = Float.parseFloat(String.valueOf(Utils.BD((formula.y_()[i] * Math.cos((270 + $E$2 + 0) * $F$1) - formula.x_()[i] * Math.sin((270 + $E$2 + 0) * $F$1)))));//2017-7-12 16:00:50 为单旭升单独更改  在X轴*100  所以在这个位置需要除以100
            y[i] = Float.parseFloat(String.valueOf( zy[i]));
            if (x[i] < min) min = x[i];
        }

        /*********画一个原点*****/
        ArrayList<Entry> y_QD = new ArrayList<Entry>();
        y_QD.add(new Entry(y[0], x[0]));
        LineDataSet lineDataSet_QD = new LineDataSet(y_QD, "  起点" /*显示在比例图上*/);
        lineDataSet_QD.setLineWidth(1.75f); // 线宽
        lineDataSet_QD.setDrawCircles(true);//取消小圆圈
        lineDataSet_QD.setDrawCircleHole(false);
        int color1 = Color.argb(254, 30, 144, 255);// 半透明的紫色
        lineDataSet_QD.setCircleColor(color1);
        lineDataSet_QD.setColor(color1);// 显示颜色
        lineDataSet_QD.setDrawValues(false);//设置折线图中的坐标点
        lineDataSet_QD.setDrawHighlightIndicators(false);
        lineDataSet_QD.setDrawVerticalHighlightIndicator(false);
        /*********结束*****/

        /*********开始*****/
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(0, 0));
        yValues.add(new Entry(sj_x,0));
        LineDataSet lineDataSet = new LineDataSet(yValues, "设计曲线" /*显示在比例图上*/);
        lineDataSet.setLineWidth(1.25f); // 线宽
        lineDataSet.setDrawCircles(false);//取消小圆圈
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setDrawValues(false);//设置折线图中的坐标点
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        /*********结束*****/

        /*********开始画实钻设计曲线*****/
        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        for (int i = 0; i < x.length; i++) {
            yValues1.add(new Entry(x[i], y[i]));
        }

        LineDataSet lineDataSet1 = new LineDataSet(yValues1, "钻孔曲线    横轴（设计方位线）,纵轴（左右偏差）" /*显示在比例图上*/);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(1.75f); // 线宽
        lineDataSet1.setDrawCircles(false);//取消小圆圈
        lineDataSet1.setColor(Color.RED);// 显示颜色
        lineDataSet1.setDrawValues(false);//设置折线图中的坐标点
        /*********结束*****/

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet_QD);
        // create a data object with the datasets
        LineData lineData = new LineData(lineDataSets);
        return lineData;
    }
    public void setTopBottom(float line, float maximum) {
        if (line == 0) line = 3;
        if (maximum == 0) maximum = 50;
        LimitLine ll1 = new LimitLine(line, "左偏差参考线");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-line, "右偏差参考线");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
//      ll2.setTypeface(tf);
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(maximum);
        leftAxis.setAxisMinimum(-maximum);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);//虚线绘制
        leftAxis.setDrawZeroLine(false);   //绘制0的线
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
    }

    public float getZY_Max() {
        double max;
        max = Data.zy[0];
        for (int i = 0; i < Data.zy.length; i++) {
            if (Math.abs(Data.zy[i]) > max) max = Math.abs(Data.zy[i]);
        }
        return (float)Utils.BD(max);
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
