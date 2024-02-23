package edu.fvtc.galleryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public static final String TAG = "MainActivity";
    Player[] player = {
            new Player("Micheal Jordan", ""),
            new Player("Kobe Bryant", ""),
            new Player("Lebron James", "")
    };


    int[] imgs = {R.drawable.jordan,
            R.drawable.kobe,
            R.drawable.lebron
    };

    int[] textfiles = {
            R.raw.jordan,
            R.raw.kobe,
            R.raw.lebron
    };

    int cardNo = 0;
    boolean isFront = true;

    ImageView imgCard;

    TextView tvCard;
    
    GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCard = findViewById(R.id.imageView);
        tvCard = findViewById(R.id.tvInfo);

        updatetoNextCard();

        gestureDetector = new GestureDetector(this, this);
        Log.d(TAG, "onCreate: Complete");
        
    }

    private void updatetoNextCard() {
        player[cardNo].setDescription(FileIO.readFile(this, textfiles[cardNo]));

        isFront = true;
        imgCard.setVisibility(View.VISIBLE);
        imgCard.setImageResource(imgs[cardNo]);
        tvCard.setText(player[cardNo].getName());
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        Log.d(TAG, "onDown: ");
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        Log.d(TAG, "onSingleTapUp: ");

        String message;

        try {
            if (isFront) {
                // Go to the back
                message = "Go to back";
                imgCard.setVisibility(View.VISIBLE);
                tvCard.setText(player[cardNo].getDescription());
                Typeface desiredFont = Typeface.create("YourFontName", Typeface.NORMAL);
                Animation move = AnimationUtils.loadAnimation(this, R.anim.singletap);
                move.setAnimationListener(new AnimationListener());
                tvCard.startAnimation(move);


                Log.d(TAG, "onFling: SingleTap");

            } else {
                // got to front
                message = "Go to front";
                imgCard.setVisibility(View.VISIBLE);
                tvCard.setText(player[cardNo].getName());
            }

            isFront = !isFront;
            Log.d(TAG, "onSingleTapUp: " + message);
        }
        catch(Exception e1)
        {
            Log.e(TAG, "onSingleTapUp: " + e1.getMessage());
        }

        return true;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onShowPress: ");
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onShowPress: ");
    }

    @Override
    public boolean onFling(@Nullable MotionEvent motionEvent1,
                           @NonNull MotionEvent motionEvent2,
                           float velocityX,
                           float velocityY) {
        Log.d(TAG, "onFling: ");

        int numCards = player.length;

        try{
            // Decide which direction I am flinging.
            int x1 = (int) (motionEvent1 != null ? motionEvent1.getX() : 0);
            int x2 = (int)motionEvent2.getX();

            if(x1 < x2)
            {
                Animation move = AnimationUtils.loadAnimation(this, R.anim.moveright);
                move.setAnimationListener(new AnimationListener());
                imgCard.startAnimation(move);
                tvCard.startAnimation(move);
                // Swipe right
                Log.d(TAG, "onFling: Right");
                cardNo = (cardNo - 1 + numCards) % numCards;
            }
            else {
                Animation move = AnimationUtils.loadAnimation(this, R.anim.moveleft);
                move.setAnimationListener(new AnimationListener());
                imgCard.startAnimation(move);
                tvCard.startAnimation(move);
                // Swipe left
                Log.d(TAG, "onFling: Left");
                cardNo = (cardNo + 1) % numCards;
            }
            //updateToNextCard();

        }
        catch(Exception ex)
        {
            Log.e(TAG, "onFling: " + ex.getMessage());
            ex.printStackTrace();
        }
        return true;
    }



    private class AnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "onAnimationEnd: ");
            updatetoNextCard();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.Micheal_Jordan) {
            cardNo = 0;
        } else if (id == R.id.Kobe_Bryant) {
            cardNo = 1;
        } else if (id == R.id.Lebron_James) {
            cardNo = 2;
        }

        // Update UI to show the selected card
        updatetoNextCard();

        return true;
    }


}