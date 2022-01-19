package com.example.tickets;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flights.Flights;
import com.example.singiair.DBHelper;
import com.example.singiair.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchDepartureFlight extends Fragment {

    String departureCountryAndCity, flightForCountryAndCity, departureDateTime, fightReturnDate;
    int user_id;
    List<String> allFlightsList = new ArrayList<String>();
    EditText searchBar;
    ArrayAdapter<String> listViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_departure_flight, container, false);

        searchBar = (EditText) view.findViewById(R.id.search_bar_for_select_country);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            departureCountryAndCity = bundle.getString("departureCountryAndCity");
            departureDateTime = bundle.getString("departureDateTime");
            flightForCountryAndCity = bundle.getString("flightForCountryAndCity");
            fightReturnDate = bundle.getString("fightReturnDate");
            user_id = bundle.getInt("user_id");
        }

        ListView listView = (ListView) view.findViewById(R.id.list_view_departure_search_flight);

        updateAllFlightsList();

        ArrayList<String> uniqueAllFlightsList = new ArrayList<String>();
        // Traverse through the first list
        for (String element : allFlightsList) {
            // If this element is not present in newList
            // then add it
            if (!uniqueAllFlightsList.contains(element)) { uniqueAllFlightsList.add(element); }
        }

        listViewAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, uniqueAllFlightsList);
        listView.setAdapter(listViewAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchDepartureFlight.this).listViewAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] selectedCountryAndCity = String.valueOf(parent.getItemAtPosition(position)).split(",");

                if(selectedCountryAndCity[0].equals("Bosna i Hercegovina")){
                    selectedCountryAndCity[0] = "BiH";
                }
                else if(selectedCountryAndCity[0].equals("Ujedinjeno Kraljevstvo")){
                    selectedCountryAndCity[0] = "UK";
                }
                else if(selectedCountryAndCity[0].equals("Severna Makedonija")){
                    selectedCountryAndCity[0] = "Makedonija";
                }
                SearchFlightFragment searchFlightFragment = new SearchFlightFragment(getContext(), user_id);
                final Bundle bundle = new Bundle();
                bundle.putString("departureCountryAndCity", selectedCountryAndCity[0] + "," + selectedCountryAndCity[1]);
                bundle.putString("departureDateTime", "Date departure");
                bundle.putString("flightForCountryAndCity", "Flight for");
                bundle.putString("fightReturnDate", "Return date");
                searchFlightFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), searchFlightFragment, "findThisFragment")
                        .commit();
            }
        });

        return view;
    }

    private void updateAllFlightsList() {
        //get all flights from sqlite
        DBHelper MyDB = new DBHelper(getContext());
        Cursor cursor = MyDB.getData("SELECT * FROM flights");

        allFlightsList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String flightDepartureCountry = cursor.getString(1);
            String flightDepartureCity = cursor.getString(2);
            //add to list
            allFlightsList.add(flightDepartureCountry + ", " + flightDepartureCity);
        }
    }
}