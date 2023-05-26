package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {
    TableLayout tableLayout;
    SQLiteHelper db;
    SQLiteDatabase sqlDB;
    Cursor cursor;

    int id = 0;
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    ArrayList<String> subjectName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        // load database <>
        db = new SQLiteHelper(getApplicationContext());
        sqlDB = db.getWritableDatabase();
        cursor = sqlDB.rawQuery(
                "SELECT " + TableInfo.COLUMN_NAME_GRADE + "," + TableInfo.COLUMN_NAME_CREDIT + "," +
                        TableInfo.COLUMN_NAME_SUBJECT + "," +
                        TableInfo.COLUMN_NAME_SCORE + " FROM " +
                        TableInfo.TABLE_NAME + " ORDER BY " + "\"" + TableInfo.COLUMN_NAME_GRADE + "\";", null
        );
        // table is null check
        if(cursor.getCount() == 0) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setTextColor(Color.parseColor("#FFFFFFFF"));
            textView.setText("- 입력값 없음 -");
            tableLayout.addView(textView);
        } else {
            // show layout
            while (cursor.moveToNext()) {
                Log.d("Query : ", cursor.getString(0) + "\t" + cursor.getString(1) + "학점" + "\t" + cursor.getString(2) + "\t" + cursor.getString(3));
                subjectName.add(cursor.getString(2));
                TableRow tableRow = new TableRow(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                tableRow.setLayoutParams(params);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setId(id);
                id += 1;
                String txt_string = String.format("%5s%5s학점 %20s %10s", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFFFF")));
                checkBox.setText(txt_string);
                checkBox.setTextSize(17);
                checkBox.setTextColor(Color.parseColor("#FFFFFFFF"));

                checkBoxes.add(checkBox);

                tableRow.addView(checkBox);

                tableLayout.addView(tableRow);
                Log.d("id : ", String.valueOf(id));
            }
        }
        db.close();

        Button d_delete_btn = (Button) findViewById(R.id.d_delete_btn);
        Button d_backup_btn = (Button) findViewById(R.id.d_backup_btn);

        // delete btn click event
        d_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = db.getWritableDatabase();
                // delete components and delete database
                for(int i = 0; i < checkBoxes.size(); i++) {
                    CheckBox chooseCheckBox = new CheckBox(getApplicationContext());
                    chooseCheckBox = checkBoxes.get(i);
                    if(chooseCheckBox.isChecked()) {
                        sqlDB.execSQL("DELETE FROM " + TableInfo.TABLE_NAME + " WHERE "+  TableInfo.COLUMN_NAME_SUBJECT  +" LIKE " + "\"" + subjectName.get(i) +"\";");
                        finish();
                        startActivity(getIntent());
                    }

                }
                db.close();
            }
        });

        // back to intent
        d_backup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main_intent);
                finish();
            }
        });
    }
}
