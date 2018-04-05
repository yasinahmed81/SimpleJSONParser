package uk.co.yasinahmed.simplejsonparser;

// Created by yasinahmed on 21/03/2018.
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Emulator address: http://10.0.2.2:3000
// Computer address: http://Computer_IP:3000

public class FetchSurgeries extends AsyncTask<String, Void, List<Surgery>> {

    private WeakReference<AllSurgeriesActivity> allSurgeriesActivityWeakReference;

    // Weak reference to the activity using this AsyncTask, (Utilised in onPostExecute).
    FetchSurgeries(AllSurgeriesActivity context) {
        allSurgeriesActivityWeakReference = new WeakReference<>(context);
    }

    @Override
    protected List<Surgery> doInBackground(String... params) {

        HttpURLConnection httpURLConnection = null;

        try {

            // Some devs pass URL string as params[0], treated as variadic arguments
            URL procedures = new URL("http://10.0.2.2:3000/procedures");
            httpURLConnection = (HttpURLConnection) procedures.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // When this is returned it can be received in onPostExecute
            return createSurgeryList(fromBufferedReader(bufferedReader));

        } catch (MalformedURLException e) {
            Log.d("URL Error", e.toString());
        } catch (SocketException e) {
            Log.d("Socket Error", e.toString());
        } catch (IOException e) {
            Log.d("I/O Error", e.toString());
        } catch (JSONException e) {
            Log.d("JSON Error", e.toString());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    // Called on main thread
    @Override
    protected void onPostExecute(List<Surgery> result) {
        super.onPostExecute(result);

        // Get a reference to the activity and check if it exists
        AllSurgeriesActivity allSurgeriesActivity = allSurgeriesActivityWeakReference.get();

        if (allSurgeriesActivity != null) {

            // Get the recyclerView, set it vertically (LayoutManager), create an instance
            // of the adapter with result (List<Surgery>), then set the adapter
            RecyclerView surgeryRecyclerView = allSurgeriesActivity.findViewById(R.id.surgeryRecyclerView);
            surgeryRecyclerView.setLayoutManager(new LinearLayoutManager(allSurgeriesActivity));

            SurgeryAdapter surgeryAdapter = new SurgeryAdapter(allSurgeriesActivity, result);
            surgeryRecyclerView.setAdapter(surgeryAdapter);
        }
    }

    private String createStringForJSONTextViewFromResult(List<Surgery> result) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Surgery s : result) {

            String string = "Surgery ID: " + s.getSurgeryID() + ", Surgery name: " + s.getSurgeryName() + ", Surgery icon: " + s.getSurgeryImageURL() + "\n";
            stringBuilder.append(string);

        }

        return stringBuilder.toString();
    }

    private String fromBufferedReader(BufferedReader bufferedReader) throws IOException {

        StringBuilder JSONOutput = new StringBuilder();
        String line = "";

        while (line != null) {
            line = bufferedReader.readLine();
            JSONOutput.append(line);
        }

        return JSONOutput.toString();
    }

    private List<Surgery> createSurgeryList(String finalJSON) throws JSONException {

        JSONArray surgeries = new JSONArray(finalJSON);
        List<Surgery> surgeriesList = new ArrayList<>();

        for (int i = 0; i < surgeries.length(); i++) {

            JSONObject jsonObject = surgeries.getJSONObject(i);

            Surgery surgeryModel = new Surgery(jsonObject.getString("id"),
                    jsonObject.getString("name"), jsonObject.getString("icon"));

            surgeriesList.add(surgeryModel);
        }

        return surgeriesList;
    }

}
