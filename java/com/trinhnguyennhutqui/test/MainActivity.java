package com.trinhnguyennhutqui.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trinhnguyennhutqui.adapter.CarAdapter;
import com.trinhnguyennhutqui.model.Car;
import com.trinhnguyennhutqui.test.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActivityResultLauncher<Intent> launcher;

    Databases db;

    CarAdapter adapter;
    ArrayList<Car> listCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preParedb();
        loadData();
        addEvent();
    }

    private void preParedb() {
        db = new Databases(this);
        db.CreateSampleData();
    }

    private void addEvent() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Car> filteredList = new ArrayList<>();
                for (Car f : listCar) {
                    if (f.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredList.add(f);
                    }
                    adapter.updateList(filteredList);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
    }


    public void openEditDialog(Car f){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit);
        EditText editName = dialog.findViewById(R.id.edtEditProductName);
        editName.setText(f.getName());
        EditText editPrice = dialog.findViewById(R.id.edtEditProductPrice);
        editPrice.setText(String.valueOf(f.getPrice()));
        EditText editDescription = dialog.findViewById(R.id.edtEditProductDes);
        editDescription.setText(f.getDescription());
        EditText editCategory = dialog.findViewById(R.id.edtEditProductType);
        editCategory.setText(f.getCategory());

        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                double price = Double.parseDouble(editPrice.getText().toString());
                String des = editDescription.getText().toString();
                String cate = editCategory.getText().toString();

                db.execSql("UPDATE " + Databases.TBL_NAME + " SET " + Databases.COL_NAME + " = '" + name + "', " + Databases.COL_PRICE + " = " + price + ", " + Databases.COL_DES + " = '" + des + "', " + Databases.COL_CATE + " = '" + cate + "' WHERE " + Databases.COL_CODE + " = " + f.getCode());
                loadData();
                dialog.dismiss();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public void OpenDeleteDialog(Car f){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa  ");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Bạn có chắc chắn muốn xóa " + f.getName() + " này không?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.execSql("DELETE FROM " +Databases.TBL_NAME + " WHERE " + Databases.COL_CODE + " = " + f.getCode());
                loadData();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void loadData() {
        adapter = new CarAdapter(MainActivity.this,R.layout.item_list, getDatafromDb());
        binding.lvFood.setAdapter(adapter);
    }

    private List<Car> getDatafromDb() {
        listCar= new ArrayList<>();
        Cursor cursor = db.queryData("SELECT * FROM " + Databases.TBL_NAME);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                listCar.add(new Car(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getBlob(3), cursor.getString(4), cursor.getString(5)));
            }
            cursor.close();
        }
        return listCar;
    }
}