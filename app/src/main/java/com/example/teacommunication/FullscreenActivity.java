package com.example.teacommunication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacommunication.databinding.ActivityFullscreenBinding;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int[] imageResources = {
            R.drawable.acostar,
            R.drawable.cama,
            R.drawable.comer,
            R.drawable.correr,
            R.drawable.en,
            R.drawable.la,
            R.drawable.manzana,
            R.drawable.nina,
            R.drawable.nino,
            R.drawable.parque,
            R.drawable.se,
            R.drawable.uno,
            R.drawable._con,
            R.drawable.saltarcuerda,
            R.drawable.jugar,
            R.drawable.pelota
    };
    // primer valor, frase
    // segundo valor, palabra
    // tercer valor, identificador de pictograma
    String[][][] frasesSep = {
            {
                    {"El", "11"}, {"niño", "9"}, {"corre", "4"}, {"en", "5"}, {"parque", "10"}
            },
            {
                    {"La", "6"}, {"niña", "8"}, {"salta la cuerda","14"}
            },
            {
                    {"El", "11"}, {"niño", "9"}, {"se", "11"}, {"acuesta", "1"}, {"en", "5"},  {"cama", "2"}
            },
            {
                    {"El", "11"}, {"niño", "9"}, {"come", "3"}, {"una", "12"}, {"manzana", "7"}
            },
            {
                    {"La", "6"}, {"niña", "8"}, {"juega", "15"}, {"con", "13"}, {"pelota", "16"}
            }
    };
    //primero frase
    //segundo cantidad de palabras
    String[][] frases = {
            {"El niño corre en parque", "6"},        // Número de palabras: 5
            {"La niña salta la cuerda", "3"},           // Número de palabras: 3
            {"El niño se acuesta en cama", "6"},     // Número de palabras: 6
            {"El niño come una manzana", "5"},          // Número de palabras: 5
            {"La niña juega con pelota", "6"}        // Número de palabras: 5
    };
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityFullscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] current = {};
        Random rn = new Random();
        int Select = 4;
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ImageButtonAdapter imageAdapter = new ImageButtonAdapter(new int[]{}, null);
        recyclerView.setAdapter(imageAdapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.dummyButton.setOnTouchListener(mDelayHideTouchListener);
        ImageButton imageButtonAcostar = findViewById(R.id.imageButton_acostar);
        ImageButton imageButtonCama = findViewById(R.id.imageButton_cama);
        ImageButton imageButtonComer = findViewById(R.id.imageButton_comer);
        ImageButton imageButtonCorrer = findViewById(R.id.imageButton_correr);
        ImageButton imageButtonEn = findViewById(R.id.imageButton_en);
        ImageButton imageButtonLa = findViewById(R.id.imageButton_la);
        ImageButton imageButtonManzana = findViewById(R.id.imageButton_manzana);
        ImageButton imageButtonNina = findViewById(R.id.imageButton_nina);
        ImageButton imageButtonNino = findViewById(R.id.imageButton_nino);
        ImageButton imageButtonParque = findViewById(R.id.imageButton_parque);
        ImageButton imageButtonSe = findViewById(R.id.imageButton_se);
        ImageButton imageButtonUno = findViewById(R.id.imageButton_uno);
        ImageButton imageButtonCon = findViewById(R.id.imageButton_con);
        ImageButton imageButtonSaltarCuerda = findViewById(R.id.imageButton_saltarcuerda);
        ImageButton imageButtonJugar = findViewById(R.id.imageButton_jugar);
        ImageButton imageButtonPelota = findViewById(R.id.imageButton_pelota);

        // Asignación de listeners a cada ImageButton
        imageButtonAcostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para acostar
                comp(0,frasesSep[Select]);
            }
        });

        imageButtonCama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para cama
                comp(1,frasesSep[Select]);
            }
        });

        imageButtonComer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para comer
                comp(2,frasesSep[Select]);
            }
        });

        imageButtonCorrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para correr
                comp(3,frasesSep[Select]);
            }
        });

        imageButtonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para en
                comp(4,frasesSep[Select]);
            }
        });

        imageButtonLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para la
                comp(5,frasesSep[Select]);

            }
        });

        imageButtonManzana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para manzana
                comp(6,frasesSep[Select]);
            }
        });

        imageButtonNina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para niña
                comp(7,frasesSep[Select]);
            }
        });

        imageButtonNino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para niño
                comp(8,frasesSep[Select]);
            }
        });

        imageButtonParque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para parqu
                comp(9,frasesSep[Select]);
            }
        });

        imageButtonSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para se
                comp(10,frasesSep[Select]);
            }
        });

        imageButtonUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para un
                comp(11,frasesSep[Select]);
            }
        });

        imageButtonCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para con
                comp(12,frasesSep[Select]);
            }
        });

        imageButtonSaltarCuerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para saltar cuerda
                comp(13,frasesSep[Select]);
            }
        });

        imageButtonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para jugar
                comp(14,frasesSep[Select]);
            }
        });

        imageButtonPelota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para jugar
                comp(15,frasesSep[Select]);
            }
        });
            }
// Inside FullscreenActivity.java




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    private void addImageToRecyclerView(int imageResource) {
        int position = recyclerView.getAdapter().getItemCount();
        ((ImageButtonAdapter) recyclerView.getAdapter()).addItem(imageResource, position);
    }
    private void comp(int pict,String[][] id){
        Toast.makeText(getApplicationContext(), frases[4][0], Toast.LENGTH_SHORT).show();
        if(pict == Integer.valueOf(id[recyclerView.getAdapter().getItemCount()][1])-1){
            addImageToRecyclerView(imageResources[pict]);
            return;
        }
    }

    }
