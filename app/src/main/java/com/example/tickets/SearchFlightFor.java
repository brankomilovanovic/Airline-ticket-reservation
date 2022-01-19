package com.example.tickets;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.singiair.DBHelper;
import com.example.singiair.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightFor extends Fragment {

    String departureCountryAndCity, flightForCountryAndCity, departureDateTime, fightReturnDate;
    String[] departureCountryAndCitySplited;
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

        departureCountryAndCitySplited = departureCountryAndCity.split(",");

        if(departureCountryAndCitySplited[0].equals("BiH")){
            departureCountryAndCitySplited[0] = "Bosna i Hercegovina";
        }
        else if(departureCountryAndCitySplited[0].equals("UK")){
            departureCountryAndCitySplited[0] = "Ujedinjeno Kraljevstvo";
        }
        else if(departureCountryAndCitySplited[0].equals("Makedonija")){
            departureCountryAndCitySplited[0] = "Severna Makedonija";
        }

        ListView listView = (ListView) view.findViewById(R.id.list_view_departure_search_flight);

        updateAllFlightsList();

        ArrayList<String> uniqueAllFlightsList = new ArrayList<String>();
        String departureCountryAndCityForSearch = departureCountryAndCitySplited[0] + "," + departureCountryAndCitySplited[1];
        // Traverse through the first list
        for (String element : allFlightsList) {
            // If this element is not present in newList
            // then add it
            String[] separated = element.split(",");
            if(departureCountryAndCityForSearch.equals(separated[0] + ", " + separated[1])) {
                if (!uniqueAllFlightsList.contains(element)) { uniqueAllFlightsList.add(separated[2] + ", " + separated[3]); }
            }
        }

        String[] separated = allFlightsList.get(0).split(",");

        listViewAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, uniqueAllFlightsList);
        listView.setAdapter(listViewAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchFlightFor.this).listViewAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountryAndCity = (String)parent.getItemAtPosition(position);
                SearchFlightFragment searchFlightFragment = new SearchFlightFragment(getContext(), user_id);
                final Bundle bundle = new Bundle();
                bundle.putString("departureCountryAndCity", departureCountryAndCity);
                bundle.putString("departureDateTime", "Date departure");
                bundle.putString("flightForCountryAndCity", selectedCountryAndCity);
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
            String flightForCountry = cursor.getString(4);
            String flightForCity = cursor.getString(5);
            //add to list
            allFlightsList.add(flightDepartureCountry + "," + flightDepartureCity + "," + flightForCountry + "," + flightForCity);
        }
    }
}