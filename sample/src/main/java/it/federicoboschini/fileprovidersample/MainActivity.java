package it.federicoboschini.fileprovidersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import it.federicoboschini.resourcefileprovider.ResourceFileProvider;
import it.federicoboschini.arfp.sample.R;

import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.FOLDER_RAW;
import static it.federicoboschini.resourcefileprovider.ResourceFileProvider.TYPE_AUDIO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void shareRawFile(View view) {
        ResourceFileProvider.Builder
                .from(this)
                .setDirectory(FOLDER_RAW)
                .setFileName("a_bene")
                .setFileExtension("mp3")
                .setFileType(TYPE_AUDIO)
                .build()
                .shareFile();
    }
}
