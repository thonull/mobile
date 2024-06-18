package com.trinhnguyennhutqui.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.trinhnguyennhutqui.test.databinding.ActivityAddBinding;

import java.io.ByteArrayOutputStream;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    Databases db;

    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new Databases(this);


        launcher = registerForActivityResult(new ActivityResultContracts
                .StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == RESULT_OK && o.getData() != null){
                    Bitmap bitmap = (Bitmap) o.getData().getExtras().get("data");
                    binding.imgCar.setImageBitmap(bitmap);
                }
            }
        });

        addEvent();
    }

    private void addEvent() {

        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = binding.edtCarName.getText().toString().trim();
                    double price = Double.parseDouble(binding.edtCarPrice.getText().toString());
                    String des = binding.edtCarDes.getText().toString().trim();
                    String cate = binding.edtCarType.getText().toString().trim();

                    if (name.isEmpty() || price<=0 || des.isEmpty() || cate.isEmpty()){
                        Toast.makeText(AddActivity.this, "Please fill in all information.", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean inserted = db.insertData(name, price, des, convertPhoto(), cate);
                        if (inserted) {
                            Toast.makeText(AddActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddActivity.this, MainActivity.class));
                        }else{
                            Toast.makeText(AddActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (NumberFormatException e) {
                    Toast.makeText(AddActivity.this, "Please enter a valid number for the price.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private byte[] convertPhoto() {
        BitmapDrawable drawable = (BitmapDrawable) binding.imgCar.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
