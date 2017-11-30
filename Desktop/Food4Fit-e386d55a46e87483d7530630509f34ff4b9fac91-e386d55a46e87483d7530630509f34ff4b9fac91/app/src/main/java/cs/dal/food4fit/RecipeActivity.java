package cs.dal.food4fit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static cs.dal.food4fit.TodayFragment.getDateString;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "RecipeActivity";

    private RecipePageAdapter mRecipePageAdapter;

    private ViewPager mViewPager;
    private Recipe imageitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int imageID = getIntent().getIntExtra("imageID",0);
//        Bitmap photo = (Bitmap) getIntent().getParcelableExtra("photo");




        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SpoonacularAPI spoon = new SpoonacularAPI();
                imageitem = spoon.getRecipe(imageID);

            }
        });
        thread.start();

        try {
            Thread.sleep(3000);                 //1000 毫秒，也就是1秒.

        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        setContentView(R.layout.activity_recipe1);

        ImageView imageView = (ImageView) findViewById(R.id.headerImage);
        imageView.setImageBitmap(imageitem.getPhoto());

        mRecipePageAdapter = new RecipePageAdapter(getSupportFragmentManager());

        // Set up the RecipePager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton addMeal = (FloatingActionButton)findViewById(R.id.addmeal);
//        final int SECOND_ACTIVITY_RESULT_CODE = 0;
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create intent to choose date
                Intent intent = new Intent(RecipeActivity.this, AddMealActivity.class);
                intent.putExtra("ImageID", imageID);
                startActivity(intent);

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        RecipePageAdapter adapter = new RecipePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new IngredientsFragment(), "Ingredients");
        adapter.addFragment(new DirectionsFragment(), "Directions");
        viewPager.setAdapter(adapter);
    }


}
