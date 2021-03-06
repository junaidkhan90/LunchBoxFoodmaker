package com.jayzonsolutions.lunchboxfoodmaker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jayzonsolutions.lunchboxfoodmaker.Service.DishService;
import com.jayzonsolutions.lunchboxfoodmaker.Service.FoodmakerDishesService;
import com.jayzonsolutions.lunchboxfoodmaker.model.ApiResponse;
import com.jayzonsolutions.lunchboxfoodmaker.model.Dish;
import com.jayzonsolutions.lunchboxfoodmaker.model.FoodmakerDishes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customfonts.MyEditText;
import customfonts.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddDish extends Fragment {

    private static FoodmakerDishes foodmakerDish ;

    Context context = getActivity();
    private DishService dishService;
    private FoodmakerDishesService foodmakerDishesService;
    Spinner spinner;
    MyEditText dishName;
    MyEditText dishPrice;
    MyEditText dishDescription;
    MyTextView btnRegisterDish;


    /*
    *image upload
     */
    private static final int SELECT_PICTURE = 0;
    private ImageView imageView;


    public static int selectedItemId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(Constant.foodmaker == null){
                Intent in = new Intent(getActivity(),signin.class);
                startActivity(in);
        }
        Integer foodmakerId = Constant.foodmaker.getFoodmakerId();

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_dish, container, false);



        imageView =  v.findViewById(android.R.id.icon);

        // resources initialization
        dishName = v.findViewById(R.id.dishName);
        dishDescription = v.findViewById(R.id.dishDescription);
        btnRegisterDish = v.findViewById(R.id.btnRegisterDish);
        dishPrice = v.findViewById(R.id.dishPrice);

        // Spinner element
        spinner =  v.findViewById(R.id.dishCatSpinner);
        // Spinner click listener
/*

        setImage =  v.findViewById(R.id.setImage);

        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
*/



        dishService = ApiUtils.getDishService();
        foodmakerDishesService = ApiUtils.getFoodmakerDishes();


        final List<String> categories = new ArrayList<>();
        final Map<String,Integer> categoriesKeyVlaue = new HashMap<>();

        try {
            dishService.getDishList().enqueue(new Callback<List<Dish>>() {
                @Override
                public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {

                    for(Dish dish: response.body()) {
                        categories.add(dish.getDishName());
                        categoriesKeyVlaue.put(dish.getDishName(),dish.getDishId());
                    }
                    Log.e("cat",""+categories);
                    // Spinner Drop down elements
                    context = getActivity();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(@NonNull Call<List<Dish>> call, @NonNull Throwable t) {
                    Toast.makeText(context,"Connection Problem ",Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "error: "+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



        // spinner.setOnItemSelectedListener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("***********", "THIS ");
                String selectValue = (String) parent.getSelectedItem();
                 selectedItemId = categoriesKeyVlaue.get(selectValue); //getting id of selected dish

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("***********", "THIS Nothing ");
            }
        });



        /**
         * if update in call then check foodmakerDish is aviable
         * */

        if(getFoodmakerDish() != null){
            FoodmakerDishes foodmakerDishes = getFoodmakerDish();
            dishName.setText(""+foodmakerDishes.getName());
            dishDescription.setText(""+foodmakerDishes.getDescription());
            dishPrice.setText(""+foodmakerDishes.getPrice());
        }











        btnRegisterDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Integer foodmakerId = Constant.foodmaker.getFoodmakerId();

                String dishNameData =  dishName.getText().toString();
                String dishDescriptionData =  dishDescription.getText().toString();
                String dishPriceData = dishPrice.getText().toString();
                FoodmakerDishes foodmakerDishes1,foodmakerDishes;
                foodmakerDishes1 =new FoodmakerDishes();
                if(getFoodmakerDish() != null){
                     foodmakerDishes = getFoodmakerDish();
                    foodmakerDishes1.setFoodmakerDishesId(foodmakerDishes.getFoodmakerDishesId());
                }

                    foodmakerDishes1.setName(dishNameData);
                    foodmakerDishes1.setDescription(dishDescriptionData);
                    foodmakerDishes1.setDishId(selectedItemId);
                    //  String imagePath = Constant.IMAGEPATH+"no_image_avaible.jpeg";

                    String imagePath = Constant.IMAGEPATH+"catering-chef-clipart-1.jpg";
                    foodmakerDishes1.setImagepath(imagePath);
                    foodmakerDishes1.setFoodmakerid(foodmakerId); //set default foodmaker id
                    foodmakerDishes1.setPrice(Double.parseDouble(dishPriceData));




                foodmakerDishesService.addFoodmakerDishes(foodmakerDishes1).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                    //    Toast.makeText(context,""+response.body().getStatus(),Toast.LENGTH_LONG).show();
                        Intent it = new Intent(context,MainActivity.class);
                        startActivity(it);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                       // Toast.makeText(context,"Connection Problem ",Toast.LENGTH_LONG).show();
                        Intent it = new Intent(context,MainActivity.class);
                        startActivity(it);
                    }
                });
            }
        });
return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bitmap bitmap = getPath(data.getData());
            imageView.setImageBitmap(bitmap);
        }
    }
    private Bitmap getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        // Convert file path into bitmap image using below line.
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }
    public void pickPhoto(View view) {
        //TODO: launch the photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }
    public void uploadPhoto(View view) {
        try {
            executeMultipartPost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void executeMultipartPost(){
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

            Bitmap bitmap = drawable.getBitmap();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

            byte[] data = bos.toByteArray();
            Toast.makeText(context,"btye array = "+data,Toast.LENGTH_LONG).show();

        }catch(Exception e) {

            // handle exception here
            e.printStackTrace();
        }

        }
    public void setFoodmakerDish(FoodmakerDishes foodmakerDish) {
        AddDish.foodmakerDish = foodmakerDish;
    }
    public FoodmakerDishes getFoodmakerDish() {
       return AddDish.foodmakerDish;
    }
}

