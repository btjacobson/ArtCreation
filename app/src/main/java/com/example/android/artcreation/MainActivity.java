package com.example.android.artcreation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.android.artcreation.view.ArtCreationView;

public class MainActivity extends AppCompatActivity {
    private ArtCreationView artCreationView;
    private AlertDialog.Builder currentAlertDialog;
    private ImageView widthImageView;
    private AlertDialog dialogLineWidth;
    private AlertDialog colorDialog;

    private SeekBar alphaSeekbar;
    private SeekBar redSeekbar;
    private SeekBar blueSeekbar;
    private SeekBar greenSeekbar;

    private View colorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        artCreationView = findViewById(R.id.view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearId:
                artCreationView.clear();
                break;

            case R.id.saveId:
                artCreationView.saveImage();
                break;

            case R.id.colorId:
                showColorDialog();
                break;

            case R.id.lineWidth:
                showLineWidthDialog();
                break;

            case R.id.erase:
                artCreationView.setDrawingColor(Color.WHITE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void showColorDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.color_dialog, null);
        alphaSeekbar = view.findViewById(R.id.alphaSeekBar);
        redSeekbar = view.findViewById(R.id.redSeekBar);
        blueSeekbar = view.findViewById(R.id.blueSeekBar);
        greenSeekbar = view.findViewById(R.id.greenSeekBar);
        colorView = view.findViewById(R.id.colorView);

        // Register SeekBar event listeners
        alphaSeekbar.setOnSeekBarChangeListener(colorSeekBarChanged);
        redSeekbar.setOnSeekBarChangeListener(colorSeekBarChanged);
        blueSeekbar.setOnSeekBarChangeListener(colorSeekBarChanged);
        greenSeekbar.setOnSeekBarChangeListener(colorSeekBarChanged);

        int color = artCreationView.getDrawingColor();
        alphaSeekbar.setProgress(Color.alpha(color));
        redSeekbar.setProgress(Color.red(color));
        greenSeekbar.setProgress(Color.green(color));
        blueSeekbar.setProgress(Color.blue(color));

        Button setColorButton = view.findViewById(R.id.setColorButton);
        setColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artCreationView.setDrawingColor(Color.argb(
                        alphaSeekbar.getProgress(),
                        redSeekbar.getProgress(),
                        greenSeekbar.getProgress(),
                        blueSeekbar.getProgress()
                ));

                colorDialog.dismiss();
            }
        });
        currentAlertDialog.setView(view);
        currentAlertDialog.setTitle("Choose Color");
        colorDialog = currentAlertDialog.create();
        colorDialog.show();
    }

    void showLineWidthDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.width_dialog, null);
        final SeekBar seekBar = view.findViewById(R.id.widthSeekBar);
        Button setLineWidthButton = view.findViewById(R.id.widthDialogButton);
        widthImageView = view.findViewById(R.id.imageViewId);
        setLineWidthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artCreationView.setLineWidth(seekBar.getProgress());
                dialogLineWidth.dismiss();
                currentAlertDialog = null;
            }
        });

        seekBar.setOnSeekBarChangeListener(widthSeekBarChanged);
        seekBar.setProgress(artCreationView.getLineWidth());

        currentAlertDialog.setView(view);
        dialogLineWidth = currentAlertDialog.create();
        dialogLineWidth.setTitle("Set Line Width");
        dialogLineWidth.show();
    }

    private SeekBar.OnSeekBarChangeListener colorSeekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            artCreationView.setBackgroundColor(Color.argb(
                    alphaSeekbar.getProgress(),
                    redSeekbar.getProgress(),
                    greenSeekbar.getProgress(),
                    blueSeekbar.getProgress()
            ));

            colorView.setBackgroundColor(Color.argb(
                    alphaSeekbar.getProgress(),
                    redSeekbar.getProgress(),
                    greenSeekbar.getProgress(),
                    blueSeekbar.getProgress()
            ));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener widthSeekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Paint p = new Paint();
            p.setColor(artCreationView.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
