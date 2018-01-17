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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.android.MP.Data.ks;
import static com.android.antiexplosionphone.R.id.chart;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;


public class Vertical extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mLineChart;
    private View view;
    private int[] x;
    private int[] y;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vertical, container, false);
        findview();
        setTopBottom();
        init();
        return view;
    }

    private void findview() {
        mLineChart = (LineChart) view.findViewById(chart);
    }

    private void init() {
//      mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        XAxis xAxis = mLineChart.getXAxis();
//      YAxis leftAxis = mLineChart.getAxisLeft();
//       xAxis.setSpaceBetweenLabels(4);  //x轴间距
        mLineChart.getAxisRight().setEnabled(false);//设置y轴的右侧是否显示坐标
//		mLineChart.getAxisLeft().setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
//        xAxis.setTypeface(mTf); // 设置字体
        xAxis.setEnabled(true);
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        xAxis.setDrawAxisLine(true);
        // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
        LineData mLineData = getLineData();
        showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));//这里调背景颜色
//        YAxis leftAxis = mLineChart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        // no description text
//        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
//        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
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
//        // get the legend (only possible after setting data)
//        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
//        // modify the legend ...
//        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
//        mLegend.setForm(Legend.LegendForm.LINE);// 样式circle
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.WHITE);// 颜色
////      mLegend.setTypeface(mTf);// 字体
//        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * 生成一个数据
     *
     * @param //count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData() {
        int length = ks.length;
        FormulaUtil formula = new FormulaUtil(ks, Data.qj, Data.fw,length);
        x = new int[length];
        y = new int[length];
        int x_min = 0;
        int y_min = 0;
        for (int i = 0; i <length; i++) {
            x[i] =(int) ((formula.y_()[i] * Math.cos((270 + $E$2 + 0) * $F$1) - formula.x_()[i] * Math.sin((270 + $E$2 + 0) * $F$1)));
            y[i]= (int) (formula.z_()[i]);
            if (x[i] < x_min) x_min = x[i];
            if (y[i] < y_min) y_min = y[i];
        }

        /*********画一个原点*****/
        ArrayList<Entry> y_QD = new ArrayList<Entry>();
        y_QD.add(new Entry(y[0], x[0]));
        LineDataSet lineDataSet_QD = new LineDataSet(y_QD, "起点" /*显示在比例图上*/);
        lineDataSet_QD.setLineWidth(2f); // 线宽
        lineDataSet_QD.setDrawCircles(true);//取消小圆圈
        lineDataSet_QD.setDrawCircleHole(false);
        int color1 = Color.argb(254,30,144,255);// 半透明的紫色
        lineDataSet_QD.setCircleColor(color1);
        lineDataSet_QD.setColor(color1);// 显示颜色
        lineDataSet_QD.setCircleSize(5);
        lineDataSet_QD.setDrawValues(false);//设置折线图中的坐标点
        lineDataSet_QD.setDrawHighlightIndicators(false);
        lineDataSet_QD.setDrawVerticalHighlightIndicator(false);
        /*********结束*****/
        ArrayList<Entry> Values = new ArrayList<Entry>();
//		假设：任意一个采集点的东西坐标为E，南北坐标为N，垂直标高为H，A为设计磁方位角
//		则对于垂直投影图而言：纵坐标值不变，仍为H
        for (int i = 0; i < x.length; i++) {
            Values.add(new Entry(x[i], y[i]));
        }
        // create a dataset and give it a type y轴的数据集合
//		lineDataSets.add(lineDataSet); // add the datasets
        LineDataSet lineDataSet1 = new LineDataSet(Values, "钻孔曲线    横轴（设计方位线）,纵轴（上下偏差）" /*显示在比例图上*/);
//         mLineDataSet.setFillAlpha(110);
//         mLineDataSet.setFillColor(Color.RED);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(1.75f); // 线宽
        lineDataSet1.setDrawCircles(false);//取消小圆圈
//		lineDataSet1.setCircleSize(1f);// 显示的圆形大小
        lineDataSet1.setColor(Color.RED);// 显示颜色
        lineDataSet1.setDrawValues(false);//设置折线图中的坐标点
//		lineDataSet1.setDrawCircleHole(false);
//		lineDataSet1.setCircleColor(Color.WHITE);// 圆形的颜色
//		lineDataSet1.setHighLightColor(Color.WHITE); // 高亮的线的颜色
//		lineDataSet1.setDrawCubic(true); //设置是否允许曲线平滑
//		lineDataSet1.setCubicIntensity(0.2f);//平滑度
        // 關閉 highlight 線
        lineDataSet1.setDrawHighlightIndicators(false);
        // 設置垂直線
        lineDataSet1.setDrawVerticalHighlightIndicator(false);


        ArrayList<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
//        lineDataSets.add(lineDataSet_sjqx);
        lineDataSets.add(lineDataSet1);
//        lineDataSets.add(lineDataSet_QD);
        // create a data object with the datasets
        LineData lineData = new LineData(lineDataSets);
        return lineData;
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

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

//        @Override
//        public String getFormattedValue(float value, YAxis yAxis) {
//            return mFormat.format(value/100); // e.g. append a dollar-sign
//        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return null;
        }
    }

    public void setTopBottom(){
//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        LimitLine ll1 = new LimitLine(3f, "Upper Limit");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(-3f, "Lower Limit");
        ll2.setLineWidth(1f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);//虚线绘制
        leftAxis.setDrawZeroLine(false);   //绘制0的线

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
    }
}
