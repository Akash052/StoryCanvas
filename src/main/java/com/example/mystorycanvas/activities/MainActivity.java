package com.example.mystorycanvas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.mystorycanvas.MotionView;
import com.example.mystorycanvas.R;
import com.example.mystorycanvas.entities.ImageEntity;
import com.example.mystorycanvas.entities.MotionEntity;
import com.example.mystorycanvas.layers.Layer;
import com.example.mystorycanvas.utils.DrawableToBitmap;

public class MainActivity extends AppCompatActivity {

    private final String img = "https://s3-alpha-sig.figma.com/img/6d57/1a3d/32320752422317ec58c1f26b44f5cf1b?Expires=1660521600&Signature=ZArW4XmRUyFe1yDw3Ki7GyNS~sPTNZy1Qc0N2BzfZfAUzco~IsZiI~BbXgAiTm6EX8bn3HjFM5VtAef3kRwSUtVPurWz4tegp9Owy7BbrF-JTGZoRVleoY3ILKDZupADwb8Sg2nUcGThSHjzHaKKjR7l725K5~oAIoHdryoWE03JdARTGrUAcd-R6lM1j1ZJaq2H~Q-qSS1mAqsddy1bhQaYWPJ3glDjheIbTjRWDJclkspPWL9Oy3Lh2EnDFx7O3avv~JFy71DPW8wv1wuSd5KbGo8AHxgTHCb8XwEq2xU~6agLn4S7esDqCfi6rzYn83~y7T2rfuzQRmjtE6N8ug__&Key-Pair-Id=APKAINTVSUGEWH5XD5UA";
    private MotionView motionView;
    private ImageView stickerButton;
    public static final int SELECT_STICKER_REQUEST_CODE = 123;
    private MotionEntity currentEntity;
    private MotionView.MotionViewCallback motionViewCallback;
    private ImageView previewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        motionView = findViewById(R.id.back_image);
        previewImage=findViewById(R.id.preview_image);

        stickerButton = findViewById(R.id.add_stickers_button);
        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StickersActivity.class);
                startActivity(intent);
            }
        });
        addSticker(R.drawable.ic_stickers_v1);
        addSticker(R.drawable.ic_stickers_v2);
        addSticker(R.drawable.ic_stickers_v3);
        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap finalImage=motionView.getThumbnailImage();
                previewImage.setImageBitmap(finalImage);
            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addSticker(final int stickerResId) {
        motionView.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();

                Drawable drawable = getDrawable(stickerResId);
                Bitmap pica = DrawableToBitmap.drawableToBitmap(drawable);

                ImageEntity entity = new ImageEntity(layer, pica, motionView.getWidth(), motionView.getHeight());

                motionView.addEntityAndPosition(entity);
                motionView.flipSelectedEntity();
            }
        });
    }
}