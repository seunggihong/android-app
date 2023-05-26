package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    // spinner item
    String[] grade_item = {"학년 선택","1학년","2학년","3학년","4학년","5학년"};
    String[] credit_item = {"학점 선택","3","2","1"};
    String[] score_item = {"받은 학점 선택","A+","A0","B+","B0","C+","C0","D+","D0","F"};

    // spinner and edit text correct input checker flag
    Boolean[] flag = {false,false,false,false};

    // final input value string
    String grade, credit, score, subject;
    Double avg;

    // db
    SQLiteHelper db;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        // find id
        Spinner grade_spinner = (Spinner) findViewById(R.id.grade_spinner);
        Spinner credit_spinner = (Spinner) findViewById(R.id.credit_spinner);
        Spinner score_spinner = (Spinner) findViewById(R.id.score_spinner);
        EditText subject_edt = (EditText) findViewById(R.id.subject_edt);
        Button done_btn = (Button) findViewById(R.id.done_btn);
        Button backup_btn = (Button) findViewById(R.id.backup_btn);

        // spinner load
        ArrayAdapter<String> grade_adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,grade_item);
        grade_adapter.setDropDownViewResource(R.layout.spinner_layout);
        grade_spinner.setAdapter(grade_adapter);

        ArrayAdapter<String> credit_adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,credit_item);
        credit_adapter.setDropDownViewResource(R.layout.spinner_layout);
        credit_spinner.setAdapter(credit_adapter);

        ArrayAdapter<String> score_adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout,score_item);
        score_adapter.setDropDownViewResource(R.layout.spinner_layout);
        score_spinner.setAdapter(score_adapter);

        // spinner selected event
        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(grade_spinner.getItemAtPosition(i).toString().compareTo(grade_item[0]) != 0) {
                    flag[0] = true;
                    grade = grade_spinner.getItemAtPosition(i).toString();
                } else {
                    flag[0] = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        credit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(credit_spinner.getItemAtPosition(i).toString().compareTo(credit_item[0]) != 0) {
                    flag[2] = true;
                    credit = credit_spinner.getItemAtPosition(i).toString();
                } else {
                    flag[2] = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        score_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(score_spinner.getItemAtPosition(i).toString().compareTo(score_item[0]) != 0) {
                    flag[3] = true;
                    score = score_spinner.getItemAtPosition(i).toString();
                } else {
                    flag[3] = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // btn click event
        db = new SQLiteHelper(getApplicationContext());
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edit text check
                if(subject_edt.getText().toString().compareTo("") != 0) {
                    flag[1] = true;
                    subject = subject_edt.getText().toString();
                } else {
                    flag[1] = false;
                }

                for(int i = 0; i < flag.length; i++) {
                    if (!flag[i]) {
                        Toast.makeText(getApplicationContext(),"올바른 입력을 해주세요!",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                // score trans
                double trans_score = scoreTrans(score);
                avg = trans_score * Integer.parseInt(credit);

                String INSERT_DATA =
                        "INSERT INTO "+ TableInfo.TABLE_NAME + " VALUES ( ' " +
                                credit + "' , '" +
                                grade + "' , '" +
                                subject + "' , '" +
                                score + "' , '" +
                                avg + "');";

                // insert db
                sqlDB = db.getWritableDatabase();
                sqlDB.execSQL(INSERT_DATA);
                sqlDB.close();

                // reset spinner and edit text place
                subject_edt.setText("");
                grade_spinner.setSelection(0);
                credit_spinner.setSelection(0);
                score_spinner.setSelection(0);

                Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_LONG).show();
            }
        });

        // back to activity
        backup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main_intent);
                finish();
            }
        });
    }

    public double scoreTrans(String s_score) {
        double t_score = 0;
        switch (s_score) {
            case "A+" :
                t_score = 4.5;
                break;
            case "A0" :
                t_score = 4.0;
                break;
            case "B+" :
                t_score = 3.5;
                break;
            case "B0" :
                t_score = 3.0;
                break;
            case "C+":
                t_score = 2.5;
                break;
            case "C0":
                t_score = 2.0;
                break;
            case "D+":
                t_score = 1.5;
                break;
            case "D0":
                t_score = 1.0;
                break;
            case "F":
                t_score = 0;
                break;
            default:
                break;
        }
        return t_score;
    }
}
