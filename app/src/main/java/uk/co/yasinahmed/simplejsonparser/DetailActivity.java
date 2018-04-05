package uk.co.yasinahmed.simplejsonparser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    ImageView surgeryImage;
    TextView surgeryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        surgeryImage = findViewById(R.id.surgeryImage);
        surgeryName = findViewById(R.id.surgeryName);

        Intent intent = getIntent();

        // Create the surgery image from byteArray passed from RecyclerView
        if (intent.hasExtra("surgeryImage")) {
            Bitmap image = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("surgeryImage"), 0,
                    intent.getByteArrayExtra("surgeryImage").length);

            surgeryImage.setImageBitmap(image);
        }

        Surgery surgery = intent.getParcelableExtra("surgery");
        surgeryName.setText(surgery.getSurgeryName());

    }
}
