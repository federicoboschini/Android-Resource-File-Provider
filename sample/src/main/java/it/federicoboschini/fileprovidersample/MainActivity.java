package it.federicoboschini.fileprovidersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.FileNotFoundException;

import it.federicoboschini.resourcefileprovider.ResourceFileProvider;
import it.federicoboschini.arfp.sample.R;

import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.FOLDER_DRAWABLE;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.FOLDER_MIPMAP;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.FOLDER_RAW;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.TYPE_AUDIO;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.TYPE_IMAGE;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.TYPE_PDF;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void shareRawSoundFile(View view) {
        try {
            ResourceFileProvider.Builder
                    .from(this)
                    .setDirectory(FOLDER_RAW)
                    .setFileName("yee")
                    .setFileExtension("mp3")
                    .setFileType(TYPE_AUDIO)
                    .build()
                    .shareFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shareRawPdfFile(View view) {
        try {
            ResourceFileProvider.Builder
                    .from(this)
                    .setDirectory(FOLDER_RAW)
                    .setFileName("loremipsum")
                    .setFileExtension("pdf")
                    .setFileType(TYPE_PDF)
                    .build()
                    .shareFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shareDrawableFile(View view) {
        try {
            ResourceFileProvider.Builder
                    .from(this)
                    .setDirectory(FOLDER_DRAWABLE)
                    .setFileName("pokeflute")
                    .setFileExtension("png")
                    .setFileType(TYPE_IMAGE)
                    .build()
                    .shareFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shareMipMapFile(View view) {
        try {
            ResourceFileProvider.Builder
                    .from(this)
                    .setDirectory(FOLDER_MIPMAP)
                    .setFileName("ic_launcher")
                    .setFileExtension("png")
                    .setFileType(TYPE_IMAGE)
                    .build()
                    .shareFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
