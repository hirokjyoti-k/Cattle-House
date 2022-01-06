package in.adbu.cattlehouse.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.adbu.cattlehouse.CattleData;
import in.adbu.cattlehouse.R;

public class Statistics extends Fragment {

    int incalfCounter = 0;
    int cyclingCounter = 0;
    int inheatCounter = 0;
    int insemiantedCounter = 0;

    int incalfCowCounter = 0;
    int cyclingCowCounter = 0;
    int inheatCowCounter = 0;
    int insemiantedCowCounter = 0;

    int incalfGoatCounter = 0;
    int cyclingGoatCounter = 0;
    int inheatGoatCounter = 0;
    int insemiantedGoatCounter = 0;

    int incalfPigCounter = 0;
    int cyclingPigCounter = 0;
    int inheatPigCounter = 0;
    int insemiantedPigCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cattles");
        Query incalfQuery = reference.orderByChild("Beeding").equalTo("Incalf");
        Query cyclingQuery = reference.orderByChild("Beeding").equalTo("Cycling");
        Query inheatQuery = reference.orderByChild("Beeding").equalTo("Inheat");
        Query inseminatedQuery = reference.orderByChild("Beeding").equalTo("Inseminated");

        incalfQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CattleData snapshotData = snapshot.getValue(CattleData.class);
                    if(snapshotData.Type.equals("Cattle"))
                        incalfCowCounter++;
                    if(snapshotData.Type.equals("Goat"))
                        incalfGoatCounter++;
                    if(snapshotData.Type.equals("Pig"))
                        incalfPigCounter++;
                    incalfCounter += 1;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        cyclingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CattleData snapshotData = snapshot.getValue(CattleData.class);
                    if(snapshotData.Type.equals("Cattle"))
                        cyclingCowCounter++;
                    if(snapshotData.Type.equals("Goat"))
                        cyclingGoatCounter++;
                    if(snapshotData.Type.equals("Pig"))
                        cyclingPigCounter++;
                    cyclingCounter++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        inheatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CattleData snapshotData = snapshot.getValue(CattleData.class);
                    if(snapshotData.Type.equals("Cattle"))
                        inheatCowCounter++;
                    if(snapshotData.Type.equals("Goat"))
                        inheatGoatCounter++;
                    if(snapshotData.Type.equals("Pig"))
                        inheatPigCounter++;
                    inheatCounter += 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        inseminatedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CattleData snapshotData = snapshot.getValue(CattleData.class);
                    if(snapshotData.Type.equals("Cattle"))
                        insemiantedCowCounter++;
                    if(snapshotData.Type.equals("Goat"))
                        insemiantedGoatCounter++;
                    if(snapshotData.Type.equals("Pig"))
                        insemiantedPigCounter++;
                    insemiantedCounter += 1;
                }
                loadChart(view);
                setData(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(View v) {

        TextView incalf = (TextView) v.findViewById(R.id.incalf_total);
        TextView cycling = (TextView) v.findViewById(R.id.cycling_total);
        TextView inheat = (TextView) v.findViewById(R.id.inheat_total);
        TextView insemination = (TextView) v.findViewById(R.id.inseminated_total);

        TextView incalfcow = (TextView) v.findViewById(R.id.incalf_cow);
        TextView cyclingcow = (TextView) v.findViewById(R.id.cycling_cow);
        TextView inheatcow = (TextView) v.findViewById(R.id.inheat_cow);
        TextView inseminationcow = (TextView) v.findViewById(R.id.inseminated_cow);

        TextView incalfgoat = (TextView) v.findViewById(R.id.incalf_goat);
        TextView cyclinggoat = (TextView) v.findViewById(R.id.cycling_goat);
        TextView inheatgoat = (TextView) v.findViewById(R.id.inheat_goat);
        TextView inseminationgaot = (TextView) v.findViewById(R.id.inseminated_goat);

        TextView incalfpig = (TextView) v.findViewById(R.id.incalf_pig);
        TextView cyclingpig = (TextView) v.findViewById(R.id.cycling_pig);
        TextView inheatpig = (TextView) v.findViewById(R.id.inheat_pig);
        TextView inseminationpig = (TextView) v.findViewById(R.id.inseminated_pig);

        incalf.setText(""+incalfCounter);
        cycling.setText(""+cyclingCounter);
        inheat.setText(""+inheatCounter);
        insemination.setText(""+insemiantedCounter);

        incalfcow.setText("Cow: "+incalfCowCounter);
        cyclingcow.setText("Cow: "+cyclingCowCounter);
        inheatcow.setText("Cow: "+inheatCowCounter);
        inseminationcow.setText("Cow: "+insemiantedCowCounter);

        incalfgoat.setText("Goat: "+incalfGoatCounter);
        cyclinggoat.setText("Goat: "+cyclingGoatCounter);
        inheatgoat.setText("Goat: "+inheatGoatCounter);
        inseminationgaot.setText("Goat: "+insemiantedGoatCounter);

        incalfpig.setText("Pig: "+incalfPigCounter);
        cyclingpig.setText("Pig: "+cyclingPigCounter);
        inheatpig.setText("Pig: "+inheatPigCounter);
        inseminationpig.setText("Pig: "+insemiantedPigCounter);
    }

    private void loadChart(View v) {

        AnyChartView CowChart = v.findViewById(R.id.cowChart);
        //Required line for multiple chart.
        APIlib.getInstance().setActiveAnyChartView(CowChart);
        Pie pieCow = AnyChart.pie();

        List<DataEntry> cow_data = new ArrayList<>();
        cow_data.add(new ValueDataEntry("IN CALF", incalfCowCounter));
        cow_data.add(new ValueDataEntry("CYCLING", cyclingCowCounter));
        cow_data.add(new ValueDataEntry("IN HEAT", inheatCowCounter));
        cow_data.add(new ValueDataEntry("INSEMINATED", insemiantedCowCounter));

        pieCow.data(cow_data);
        pieCow.title("Cows Overview");
        pieCow.labels().position("outside");
        pieCow.legend().title().enabled(true);
        pieCow.legend().title()
                .text("Breeding Herd")
                .padding(0d, 0d, 10d, 0d);
        pieCow.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        CowChart.setChart(pieCow);

        AnyChartView GoatChart = v.findViewById(R.id.goatChart);
        //Required line for multiple chart.
        APIlib.getInstance().setActiveAnyChartView(GoatChart);
        Pie pieGoat = AnyChart.pie();

        List<DataEntry> goat_data = new ArrayList<>();
        goat_data.add(new ValueDataEntry("IN CALF", incalfGoatCounter));
        goat_data.add(new ValueDataEntry("CYCLING", cyclingGoatCounter));
        goat_data.add(new ValueDataEntry("IN HEAT", inheatGoatCounter));
        goat_data.add(new ValueDataEntry("INSEMINATED", insemiantedGoatCounter));

        pieGoat.data(goat_data);
        pieGoat.title("Goat Overview");
        pieGoat.labels().position("outside");
        pieGoat.legend().title().enabled(true);
        pieGoat.legend().title()
                .text("Breeding Herd")
                .padding(0d, 0d, 10d, 0d);
        pieGoat.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        GoatChart.setChart(pieGoat);

        AnyChartView PigChart = v.findViewById(R.id.pigChart);
        //Required line for multiple chart.
        APIlib.getInstance().setActiveAnyChartView(PigChart);
        Pie piePig = AnyChart.pie();

        List<DataEntry> pig_data = new ArrayList<>();
        pig_data.add(new ValueDataEntry("IN CALF", incalfPigCounter));
        pig_data.add(new ValueDataEntry("CYCLING", cyclingPigCounter));
        pig_data.add(new ValueDataEntry("IN HEAT", inheatPigCounter));
        pig_data.add(new ValueDataEntry("INSEMINATED", insemiantedPigCounter));

        piePig.data(pig_data);
        piePig.title("Pig Overview");
        piePig.labels().position("outside");
        piePig.legend().title().enabled(true);
        piePig.legend().title()
                .text("Breeding Herd")
                .padding(0d, 0d, 10d, 0d);
        piePig.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        PigChart.setChart(piePig);
    }
}