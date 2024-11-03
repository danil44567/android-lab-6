package com.example.lab6;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;

    private ListView productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DBHelper helper = new DBHelper(getApplicationContext());
        try {
            database = helper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsList = findViewById(R.id.productsList);

        Button category1 = findViewById(R.id.category1);
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts(1);
            }
        });

        Button category2 = findViewById(R.id.category2);
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts(2);
            }
        });

        Button category3 = findViewById(R.id.category3);
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProducts(3);
            }
        });
    }

    private void loadProducts(Integer categoryId) {
        ArrayList<HashMap<String, String>> products = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT products.name, products.description, products.price FROM products WHERE products.category_id = " + categoryId, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            HashMap<String, String> product = new HashMap<>();
            product.put("name", "Ничего не найдено");
            product.put("description", "Попробуйте  поискать в другой катерогии");
            products.add(product);

            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), products, android.R.layout.simple_list_item_2,
                    new String[]{"name", "description"}, new int[]{android.R.id.text1, android.R.id.text2});

            productsList.setAdapter(adapter);

            return;
        }

        while (!cursor.isAfterLast()) {
            HashMap<String, String> product = new HashMap<>();
            product.put("name", cursor.getString(0));

            String description = cursor.getString(1);
            String price = "Цена: " + cursor.getString(2);

            product.put("description", description + "\n\n" + price);
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), products, android.R.layout.simple_list_item_2,
                new String[]{"name", "description"}, new int[]{android.R.id.text1, android.R.id.text2});

        productsList.setAdapter(adapter);
    }
}