package com.example.administrator.azlist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv_main;
    private WordsNavigation wordsNavigation;
    private MainAdapter mainAdapter;
    private List<CityBean> cityBeans = new ArrayList<>();
    private List<String> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr = "https://dianying.taobao.com/cityAction.json?action=cityAction&event_submit_doGetAllRegion=true";
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(is);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String msg = "";
                        String msgLine = "";
                        while ((msgLine = bufferedReader.readLine()) != null) {
                            msg += msgLine + "\n";
                        }
                        JSONObject jsonObject = new JSONObject(msg);
                        JSONObject data = jsonObject.getJSONObject("returnValue");
                        Iterator<String> keys = data.keys();
                        TreeMap<String, JSONArray> treeMap = new TreeMap<>();
                        while (keys.hasNext()) {
                            String index = keys.next();
                            JSONArray jsonArray = data.getJSONArray(index);
                            treeMap.put(index, jsonArray);
                        }
                        cityBeans.clear();
                        for (Map.Entry<String, JSONArray> next : treeMap.entrySet()) {
                            String key = next.getKey();
                            JSONArray jsonArray = next.getValue();
                            words.add(key);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String city = jsonArray.getJSONObject(i).getString("regionName");
                                CityBean cityBean = new CityBean();
                                cityBean.setCity(city);
                                if (i == 0) {
                                    cityBean.setIndex(key);
                                } else {
                                    cityBean.setIndex("");
                                }
                                cityBean.setFirstWord(key);
                                cityBeans.add(cityBean);
                            }
                        }
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wordsNavigation.setWords(words);
                        mainAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private void initView() {
        rv_main = findViewById(R.id.rv_main);
        rv_main.setLayoutManager(new LinearLayoutManager(this));
        FloatingItemDecoration floatingItemDecoration = new FloatingItemDecoration(this, cityBeans);
        rv_main.addItemDecoration(floatingItemDecoration);
        //rv_main.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainAdapter = new MainAdapter(cityBeans);
        rv_main.setAdapter(mainAdapter);

        rv_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = ((LinearLayoutManager) rv_main.getLayoutManager()).findFirstVisibleItemPosition();
                String index = cityBeans.get(firstVisibleItemPosition).getFirstWord();
                wordsNavigation.setTouchIndex(index);
            }
        });


        wordsNavigation = findViewById(R.id.works_navigation);
        wordsNavigation.setOnWordsChangeListener(new WordsNavigation.OnWordsChangeListener() {
            @Override
            public void wordsChange(String word) {
                for (int i = 0; i < cityBeans.size(); i++) {
                    if (cityBeans.get(i).getIndex().equals(word)) {
                        rv_main.scrollToPosition(i);
                        ((LinearLayoutManager) rv_main.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    }
                }
            }
        });

    }
}
