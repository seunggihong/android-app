package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // spinner item
    String[] main_grade_item = {"전체","1학년","2학년","3학년","4학년","5학년"};
    SQLiteHelper db;
    SQLiteDatabase sqlDB;

    // cursor
    Cursor check_cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create db. if already db exist load data
        db = new SQLiteHelper(getApplicationContext());

        // text view load
        TextView grade_view = (TextView) findViewById(R.id.grade_view);
        TextView score_view = (TextView) findViewById(R.id.score_avg_view);

        // Button id load
        Button add_btn = (Button) findViewById(R.id.add_btn);
        Button delete_btn = (Button) findViewById(R.id.delete_btn);

        Spinner main_grade_spinner = (Spinner) findViewById(R.id.main_grade_spinner);

        // spinner load
        ArrayAdapter<String> main_grade_adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,main_grade_item);
        main_grade_adapter.setDropDownViewResource(R.layout.spinner_layout);
        main_grade_spinner.setAdapter(main_grade_adapter);

        // spinner choose item
        main_grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade_view.setText("학 년 : "+ main_grade_spinner.getItemAtPosition(i).toString());

                sqlDB = db.getWritableDatabase();
                int count;

                if(i == 0) {
                    check_cursor = sqlDB.rawQuery("SELECT " + TableInfo.COLUMN_NAME_CREDIT + ", " + TableInfo.COLUMN_NAME_MULTIPLE + "  FROM " + TableInfo.TABLE_NAME + ";",null);
                    count = check_cursor.getCount();
                    if(count == 0) {
                        score_view.setText("평 균 : -");
                    } else {
                        double sum = 0;
                        double credit = 0;
                        while(check_cursor.moveToNext()) {
                            sum += Double.parseDouble(check_cursor.getString(1));
                            credit += Double.parseDouble(check_cursor.getString(0));
                        }
                        score_view.setText("평 균 : " + Math.round(sum/credit*100)/100.0);
                    }
                } else {
                    check_cursor = sqlDB.rawQuery("SELECT " + TableInfo.COLUMN_NAME_CREDIT + ", " + TableInfo.COLUMN_NAME_MULTIPLE + " FROM " + TableInfo.TABLE_NAME + " WHERE " + "\"" + TableInfo.COLUMN_NAME_GRADE + "\"" + "=" + "\"" + main_grade_spinner.getItemAtPosition(i).toString() + "\"" + ";",null);
                    count = check_cursor.getCount();
                    if(count == 0) {
                        score_view.setText("평 균 : -");
                    } else {
                        double sum = 0;
                        double credit = 0;
                        while(check_cursor.moveToNext()) {
                            sum += Double.parseDouble(check_cursor.getString(1));
                            credit += Double.parseDouble(check_cursor.getString(0));
                        }
                        score_view.setText("평 균 : " + Math.round(sum/credit*100)/100.0);
                    }
                    Log.d("count : " , String.valueOf(count) );
                }
                db.close();
                //check_cursor = sqlDB.rawQuery("SELECT count(*) FROM "+TableInfo.TABLE_NAME+" ;",null);
                //grade_cursor = sqlDB.rawQuery("SELECT count(*) FROM " + TableInfo.TABLE_NAME + " WHERE " + TableInfo.COLUMN_NAME_GRADE + " = " + main_grade_item[i] + " ;",null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Add btn
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change scene
                Intent add_intent = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(add_intent);
                finish();
            }
        });

        // Delete btn
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent delete_intent = new Intent(getApplicationContext(),DeleteActivity.class);
                startActivity(delete_intent);
                finish();
            }
        });
    }

    public void setScore(int spinner_num) {

    }
}