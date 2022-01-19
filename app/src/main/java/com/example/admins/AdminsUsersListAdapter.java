package com.example.admins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.singiair.DBHelper;
import com.example.singiair.R;
import com.example.singiair.User;

import java.util.ArrayList;

public class AdminsUsersListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<User> allUsers;

    public AdminsUsersListAdapter(Context context, int layout, ArrayList<User> allUsers){
        this.context = context;
        this.layout = layout;
        this.allUsers = allUsers;
    }

    @Override
    public int getCount() {
        return allUsers.size();
    }

    public void filteredList(ArrayList<User> filterList) {
        allUsers = filterList;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return allUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        protected ImageView imageShow;
        protected TextView nameShow, surnameShow, usernameShow;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DBHelper MyDB = new DBHelper(context);
        View row = convertView;
        ViewHolder holder = null;

         if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.nameShow = row.findViewById(R.id.allUsersNameShow);
            holder.surnameShow = row.findViewById(R.id.allUserSurnameShow);
            holder.usernameShow = row.findViewById(R.id.allUserUsernameShow);
            holder.imageShow = row.findViewById(R.id.allUsersImageShow);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        User user = allUsers.get(position);
        holder.nameShow.setText(user.getUserName());
        holder.surnameShow.setText(user.getUserSurname());
        holder.usernameShow.setText(user.getUserUsername());

        //Set user image, or if has not image do not change
        try {
            Bitmap image = MyDB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                holder.imageShow.setImageBitmap(MyDB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e) {
            System.out.println("ERROR! User have not image!");
        }

        return row;
    }
}
