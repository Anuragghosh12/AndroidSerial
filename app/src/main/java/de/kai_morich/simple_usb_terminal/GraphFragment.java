package de.kai_morich.simple_usb_terminal;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    private static final String ARG_DATA = "data";

    public static GraphFragment newInstance(ArrayList<float[]> graphData) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATA, graphData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        LineChart chart = view.findViewById(R.id.line_chart);
        setupGraph(chart);

        // Retrieve data
        ArrayList<float[]> graphData = (ArrayList<float[]>) getArguments().getSerializable(ARG_DATA);
        if (graphData != null) {
            displayGraph(chart, graphData);
        }

        return view;
    }

    private void setupGraph(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(1f);

        chart.getAxisRight().setEnabled(false);
    }

    private void displayGraph(LineChart chart, ArrayList<float[]> graphData) {
        List<Entry> entries0 = new ArrayList<>();
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();

        for (int i = 0; i < graphData.size(); i++) {
            float[] row = graphData.get(i);
            entries0.add(new Entry(i, row[0]));
            entries1.add(new Entry(i, row[1]));
            entries2.add(new Entry(i, row[2]));
            entries3.add(new Entry(i, row[3]));
        }

        LineDataSet dataSet0 = new LineDataSet(entries0, "Channel 0");
        LineDataSet dataSet1 = new LineDataSet(entries1, "Channel 1");
        LineDataSet dataSet2 = new LineDataSet(entries2, "Channel 2");
        LineDataSet dataSet3 = new LineDataSet(entries3, "Channel 3");

        styleDataSet(dataSet0, Color.RED);
        styleDataSet(dataSet1, Color.BLUE);
        styleDataSet(dataSet2, Color.GREEN);
        styleDataSet(dataSet3, Color.YELLOW);

        LineData lineData = new LineData(dataSet0, dataSet1, dataSet2, dataSet3);
        chart.setData(lineData);
        chart.invalidate(); // Refresh the graph
    }

    private void styleDataSet(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(3f);
    }
}
