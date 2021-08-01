package com.example.movieclient.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.bean.PieModel;
import com.example.movieclient.bean.BarModel;
import com.example.movieclient.utils.SP;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class ChartActivity extends Tool_Bar_Activity implements OnChartValueSelectedListener {

    private List<PieModel> list = new ArrayList();
    private ProgressDialog dialog;
    private int personID;
    private NetworkConnection networkConnection;
    private Button bt_end;
    private Button bt_start;
    private PieChart mChart;
    private BarChart barChart;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private Spinner tv_year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setTitle("Chart Report");
        personID = SP.getInstance(this).getInt("ID");
        networkConnection = new NetworkConnection();
        dialog = new ProgressDialog(this);

        mChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bc_year);
        mChart.setOnChartValueSelectedListener(this);
        bt_start = findViewById(R.id.bt_start);
        bt_end = findViewById(R.id.bt_end);
        tv_year = findViewById(R.id.sp_year);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectTime(0);
            }
        });
        bt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(1);

            }
        });
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.sp_years, android.R.layout.simple_spinner_item);
        tv_year.setAdapter(adapter);
        tv_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = tv_year.getSelectedItem().toString();
                new BarTask().execute(year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = bt_start.getText().toString();
                String end = bt_end.getText().toString();
                String newStart = start.replace("-", "");
                String newend = end.replace("-", "");
                if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
                    Toast.makeText(ChartActivity.this, "Start Date and End Date can not be null", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(ChartActivity.this, "Same date or invalid  time period set may result in no match result", Toast.LENGTH_SHORT).show();
                    while (Integer.parseInt(newStart) != Integer.parseInt(newend) && Integer.parseInt(newend) > Integer.parseInt(newStart)) {
                        dialog.show();
                        new PieChartTask().execute(start, end);
                        break;
                    }
                }

            }
        });
    }

    private void selectTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(
                ChartActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String dayStr = "" + day;
                        if (day < 10) {
                            dayStr = "0" + day;
                        }
                        String monthStr = month + "";
                        if (month < 10) {
                            monthStr = "0" + month;
                        }

                        String date = year + "-" + monthStr + "-" + dayStr;
                        if (type == 0) {
                            bt_start.setText(date);
                        } else {
                            bt_end.setText(date);
                        }

                    }
                },
                year, month, day).show();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        PieEntry pe = (PieEntry) e;
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("CODE", pe.getLabel());
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {

    }

    private class PieChartTask extends AsyncTask<String, Void, List<PieModel>> {

        @Override
        protected List<PieModel> doInBackground(String... params) {
            String url = NetworkConnection.MEMOIR_PATH + "/Task04a/" + personID + "/" + params[0] + "/" + params[1];
            String result = networkConnection.getRequest(url);
            Log.e("result", result);
            Type type = new TypeToken<List<PieModel>>() {
            }.getType();
            List<PieModel> list = new Gson().fromJson(result, type);
            return list;

        }

        @Override
        protected void onPostExecute(List<PieModel> pieModels) {
            int total = 0;
            int count = 0;
            dialog.dismiss();
            Toast.makeText(ChartActivity.this, " Load successful", Toast.LENGTH_SHORT).show();
            if (pieModels != null) {
                list.clear();
                list.addAll(pieModels);
                entries.clear();
//                int a = list.size();
                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {
                        PieModel pieModel = list.get(i);
                        entries.add(new PieEntry(Float.parseFloat(String.valueOf(pieModel.getCount())), pieModel.getPostcode() + ""));
                    }
                }
                ArrayList<Integer> colors = new ArrayList<>();

                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());


                PieDataSet dataSet = new PieDataSet(entries, "");
                PieData data = new PieData(dataSet);
                dataSet.setColors(colors);
                mChart.setData(data);
                mChart.invalidate();
            }

        }
    }

    private class BarTask extends AsyncTask<String, Void, List<BarModel>> {

        @Override
        protected List<BarModel> doInBackground(String... params) {
            String url = NetworkConnection.MEMOIR_PATH + "/Task04b/" + personID + "/" + params[0];
            String result = networkConnection.getRequest(url);
            Type type = new TypeToken<List<BarModel>>() {
            }.getType();
            List<BarModel> list = new Gson().fromJson(result, type);
            return list;
        }

        @Override
        protected void onPostExecute(List<BarModel> dataList) {
            dialog.dismiss();
            Toast.makeText(ChartActivity.this, " Load successful", Toast.LENGTH_SHORT).show();

            ArrayList<BarEntry> values = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                switch (dataList.get(i).getMonth()) {

                    case "January":
                        dataList.get(i).setMonth("1");
                        break;
                    case "February":
                        dataList.get(i).setMonth("2");
                        break;
                    case "March":
                        dataList.get(i).setMonth("3");
                        break;
                    case "April":
                        dataList.get(i).setMonth("4");
                        break;
                    case "May":
                        dataList.get(i).setMonth("5");
                        break;
                    case "June":
                        dataList.get(i).setMonth("6");
                        break;
                    case "July":
                        dataList.get(i).setMonth("7");
                        break;
                    case "August":
                        dataList.get(i).setMonth("8");
                        break;
                    case "September":
                        dataList.get(i).setMonth("9");
                        break;
                    case "October":
                        dataList.get(i).setMonth("10");
                        break;
                    case "November":
                        dataList.get(i).setMonth("11");
                        break;
                    case "December":
                        dataList.get(i).setMonth("12");
                        break;

                }
                values.add(new BarEntry(Float.parseFloat(dataList.get(i).getMonth()), Float.parseFloat(dataList.get(i).getCount())));
            }
            BarDataSet set = new BarDataSet(values, "");
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            BarData data = new BarData(dataSets);
            barChart.setData(data);
            barChart.invalidate();
        }
    }


    public static String randomHexStr(int len) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < len; i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return "#" + result.toString().toUpperCase();
        } catch (Exception e) {
            return "#00CCCC";
        }
    }


}
