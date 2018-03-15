package com.example.louis.customerinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText customerId, first, last, street, city;
    Button pull;
    int input;

    public final static String URL = "http://www.thomas-bayer.com/sqlrest/CUSTOMER/";
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create variables for components used in the app
        customerId = findViewById(R.id.customerId);
        first = findViewById(R.id.firstOutput);
        last = findViewById(R.id.lastOutput);
        street = findViewById(R.id.streetOutput);
        city = findViewById(R.id.cityOutput);
        pull = findViewById(R.id.pull);

        //make the pull button unusable until a proper entry is made
        pull.setEnabled(false);

        //what happens when the customerId box is filled in
        customerId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_GO) {
                    try {
                        //check for incorrect input
                        input = Integer.parseInt(customerId.getText().toString());
                        //append the url with the customerId entered by the user
                        if(input <= 49 && input >=0) {
                            url = URL + input + "/";
                            //re-enable the pull button if the input is valid
                            pull.setEnabled(true);

                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "There are only listings for customers 0-49", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        first.setText(null);
                        last.setText(null);
                        street.setText(null);
                        city.setText(null);
                    }
                    catch(NumberFormatException e){
                        //clear screen and send alert
                        customerId.setText(null);
                        first.setText(null);
                        last.setText(null);
                        street.setText(null);
                        city.setText(null);
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid ID Entry", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                return handled;
            }
        });


        //when the pull button is pressed, the callParser method is called.
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when the pull button is pressed it is disabled to prevent multiple presses
                pull.setEnabled(false);
                callParser();
            }
        });
    }

    //this method calls the GetCustomerInfo class to execute.
    public void callParser()
    {
        new GetCustomerInfo(this, url).execute();
    }

    //this is the method that the GetCustomerInfo class calls back to
    public void displayData(String[] result){
        //checks that the correct data was pulled
        if(input == Integer.parseInt(result[0].toString()))
        {
            first.setText(result[1]);
            last.setText(result[2]);
            street.setText(result[3]);
            city.setText(result[4]);
        }else {
            Toast toast = Toast.makeText(getApplicationContext(), "No information available for customer", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}


