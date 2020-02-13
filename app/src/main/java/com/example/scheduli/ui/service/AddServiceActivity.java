package com.example.scheduli.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scheduli.R;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.ui.provider.ProviderActivity;

public class AddServiceActivity extends AppCompatActivity {

    private static final String TAG_ADD_SERVICE = "ADD_SERVICE";
    private TextView serviceHeadLine;
    private EditText serviceName, serviceCost, serviceDuration;
    private Button serviceContinueButton, serviceBackButton, deleteServiceButton;
    private Service service;
    private Intent intent;
    private boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        initView();

        serviceContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    deleteServiceButton.setAlpha((float) 0.5);
                    serviceHeadLine.setText("Edit Service");
                    service.setName(String.valueOf(serviceName.getText()));
                    service.setSingleSessionInMinutes(Integer.parseInt(serviceDuration.getText().toString()));
                    service.setCost(Float.parseFloat(serviceCost.getText().toString()));

                    intent = new Intent(AddServiceActivity.this, SetServiceScheduleActivity.class);
                    intent.putExtra("service", service);
                    startActivity(intent);
                } else

                    createNewService();
            }
        });

        serviceBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToServices();
            }
        });

        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteService();
               //TODO: finish the delete service

            }
        });
    }

    private void deleteService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you would like to delete the current service ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete service method
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goBackToServices() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("If you Quit now your data wont be saved")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ProviderActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

    }

    private void createNewService() {
        Log.i(TAG_ADD_SERVICE, "creating new service");
        displayErrorToUserIfThereIsOne();

        if (checkIfInputValid()) {
            String name = serviceName.getText().toString();
            float cost = Float.parseFloat(serviceCost.getText().toString());
            int minutes = Integer.parseInt(serviceDuration.getText().toString());
            Service service = new Service(name, cost, minutes);

            Intent intent = new Intent(getBaseContext(), SetServiceScheduleActivity.class);
            intent.putExtra("service", service);
            startActivity(intent);
        }

    }

    private boolean checkIfInputValid() {
        return !checkIfEmpty(serviceCost) && !checkIfEmpty(serviceDuration) && !checkIfEmpty(serviceName) && checkIfNumberLegal(serviceCost) && checkIfNumberLegal(serviceDuration);
    }

    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private void displayErrorToUserIfThereIsOne() {
        if (checkIfEmpty(serviceName))
            serviceName.setError("You must fill Service Name");
        if (checkIfEmpty(serviceDuration))
            serviceDuration.setError("You Must fill the Duration of Each Session in Minutes");
        else if (!checkIfNumberLegal(serviceDuration))
            serviceDuration.setError("You Must use numbers only");
        if (checkIfEmpty(serviceCost))
            serviceCost.setError("You must add Your service Cost");
        else if (!checkIfNumberLegal(serviceCost))
            serviceCost.setError("You Must enter a numbers only");
    }

    private boolean checkIfNumberLegal(EditText serviceDuration) {
        try {
            TextUtils.isDigitsOnly(serviceDuration.getText().toString());
            return true;
        } catch (Exception e) {
            serviceDuration.setError("Your Input is not a number, Please enter a number");
            return false;
        }
    }

    private void initView() {
        Log.i(TAG_ADD_SERVICE, "initView()");
        serviceName = findViewById(R.id.ed_service_name);
        serviceCost = findViewById(R.id.ed_service_cost);
        serviceDuration = findViewById(R.id.et_service_duration);
        serviceContinueButton = findViewById(R.id.btn_continue_to_schedule);
        serviceHeadLine = findViewById(R.id.tv_add_new_service_headline);
        serviceBackButton = findViewById(R.id.btn_back_service);
        deleteServiceButton = findViewById(R.id.btn_delete_service);
        deleteServiceButton.setAlpha((float )0.5);
        deleteServiceButton.setEnabled(false);

        intent = getIntent();
        service = intent.getParcelableExtra("service");
        try {
            if (service.getName() != null) { // not a strong condition
                editMode = true;
                deleteServiceButton.setAlpha((float )1.0);
                deleteServiceButton.setEnabled(true);
                editExistingService();
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        Log.i(TAG_ADD_SERVICE, "finished initView() ");
    }

    private void editExistingService() {
        TextView header = findViewById(R.id.tv_add_new_service_headline);
        TextView intruct = findViewById(R.id.tv_instruc_addservice);
        header.setText("Edit your service");
        intruct.setText("Please edit the requested rows");

        serviceName.setText(service.getName());
        serviceCost.setText(String.valueOf(service.getCost()));
        serviceDuration.setText(String.valueOf(service.getSingleSessionInMinutes()));

    }

    private boolean isFormClear() {
        return checkIfEmpty(serviceName) && checkIfEmpty(serviceCost) && checkIfEmpty(serviceDuration);
    }
}
