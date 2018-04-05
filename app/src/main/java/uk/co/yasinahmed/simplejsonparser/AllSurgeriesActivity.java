package uk.co.yasinahmed.simplejsonparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AllSurgeriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_surgeries);
    }

    public void fetchJSON(View view) {

        FetchSurgeries fetchSurgeries = new FetchSurgeries(this);
        fetchSurgeries.execute();
    }

}
