package com.trinhnguyennhutqui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.trinhnguyennhutqui.model.Car;
import com.trinhnguyennhutqui.test.MainActivity;
import com.trinhnguyennhutqui.test.R;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends BaseAdapter {
    MainActivity context;
    int layout_item;

    List<Car> cars;
    public CarAdapter(MainActivity context, int layout_item, List<Car> cars) {
        this.context = context;
        this.layout_item = layout_item;
        this.cars= cars;
    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int position) {
        return cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout_item, null);
            holder.txtInfor =convertView.findViewById(R.id.txtInfor);
            holder.imgFood = convertView.findViewById(R.id.imgFood);
            holder.imgEdit = convertView.findViewById(R.id.imBtnEdit);
            holder.imgDelete = convertView.findViewById(R.id.imBtnDelete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }

        //binding data
        Car p = cars.get(position);
        byte[] photo = p.getImage();
        if (photo != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            holder.imgFood.setImageBitmap(bitmap);
        }
        // holder.imgFood
        holder.txtInfor.setText("Tên sản phẩm:" + p.getName() + "\n"
                +"Giá sản phẩm:" + p.getPrice()+"\n"
                +"Mô tả sản phẩm:" + p.getDescription()+"\n"
                +"Loại sản phẩm:" + p.getCategory() + "\n");

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.openEditDialog(p);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.OpenDeleteDialog(p);
            }
        });
        return convertView;
    }
    public void updateList(ArrayList<Car> filteredList) {
        cars = new ArrayList<>();
        cars.addAll(filteredList);
        notifyDataSetChanged();
    }


    public static class ViewHolder{

        ImageView imgFood;
        TextView txtInfor;

        ImageButton imgEdit;
        ImageButton imgDelete;

    }
}
