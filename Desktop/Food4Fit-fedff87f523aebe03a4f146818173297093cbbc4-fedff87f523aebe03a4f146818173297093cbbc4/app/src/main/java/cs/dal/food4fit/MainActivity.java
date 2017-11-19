package cs.dal.food4fit;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Button btn_signup;

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private Context context;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mTextMessage.setText(R.string.title_today);
                    TodayFragment todayFragment = new TodayFragment();
                    android.support.v4.app.FragmentTransaction todayTransction = getSupportFragmentManager().beginTransaction();
                    todayTransction.replace(R.id.content, todayFragment).commit();
                    return true;
                case R.id.navigation_explore:
                    mTextMessage.setText(R.string.title_explore);
                    ExploreFragment exploreFragment = new ExploreFragment();
                    android.support.v4.app.FragmentTransaction exploreTransction = getSupportFragmentManager().beginTransaction();
                    exploreTransction.replace(R.id.content, exploreFragment).commit();




                    return true;
                case R.id.navigation_cook:
                    mTextMessage.setText(R.string.title_cook);
                    CookFragment cookFragment = new CookFragment();
                    android.support.v4.app.FragmentTransaction cookTransction = getSupportFragmentManager().beginTransaction();
                    cookTransction.replace(R.id.content, cookFragment).commit();

                    return true;
                case R.id.navigation_shop:
                    mTextMessage.setText(R.string.title_shop);
                    ShopFragment shopFragment = new ShopFragment();
                    android.support.v4.app.FragmentTransaction shopTransction = getSupportFragmentManager().beginTransaction();
                    shopTransction.replace(R.id.content, shopFragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Edits By Mihyar
        btn_signup = (Button)findViewById(R.id.user);


        context = this;
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(context, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

    }


    public void goSignup (View view){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }




}
