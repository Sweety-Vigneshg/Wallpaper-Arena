package com.vigneshgbe.wallpaperarena;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class trending extends Fragment{

    private final String JSON_URL = Constants.home;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private List<ModelRv> modelRvList;
    String myResponse;
    private List<ModelRv> modelRvListhd;
    public trending() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        modelRvList = new ArrayList<>();
        modelRvListhd = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        boolean isFilePresent = isFilePresent(getContext(), "storage.json");
        if(isFilePresent) {
            String jsonString = read(getContext(), "storage.json");
            //do the json parsing here and do the rest of functionality of app
            try {
                jsonrequestOnPresent(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
    private void jsonrequestOnPresent(String myresponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(myresponse);
        JSONArray array = jsonObject.getJSONArray("photos");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject object1 = object.getJSONObject("src");
            ModelRv modelRv = new ModelRv(R.drawable.animals);
            modelRv.setPhoto(object1.getString("medium"));
            modelRvList.add(modelRv);
            ModelRv modelRv1 = new ModelRv(R.drawable.animals);
            modelRv1.setPhoto(object1.getString("portrait"));
            modelRvListhd.add(modelRv1);
        }
        setuprecyclerviewForSaved(modelRvList);

    }

    private void setuprecyclerviewForSaved(List<ModelRv> modelRvList) {
        final  Adapter adapter = new Adapter(getContext(), modelRvList,modelRvListhd);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

    }
    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}