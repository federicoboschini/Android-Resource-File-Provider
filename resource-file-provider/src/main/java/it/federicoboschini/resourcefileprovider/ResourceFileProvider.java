package it.federicoboschini.resourcefileprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;

import it.federicoboschini.androidresourcefileprovider.R;

/**
 * @author Federico Boschini
 */
public class ResourceFileProvider {

    private static final String TAG = "ResourceFileProvider";

    private static final String ANDROID_RES_URI = "android.resource://%1$s/%2$s";

    public static final String FOLDER_RAW = "raw";
    public static final String FOLDER_DRAWABLE = "drawable";
    public static final String FOLDER_MIPMAP = "mipmap";
    public static final String FOLDER_ASSETS = "assets";

    public static final String TYPE_AUDIO = "audio/*";
    public static final String TYPE_IMAGE = "image/*";
    public static final String TYPE_VIDEO = "video/*";
    public static final String TYPE_PDF = "application/pdf";

    private final Activity activity;
    private final String directory;
    private final String fileName;
    private final String fileExtension;
    private final String fileType;

    private ResourceFileProvider(@NonNull Activity activity, @NonNull String directory, @NonNull String fileName, @NonNull String fileExtension, @NonNull String fileType) {
        this.activity = activity;
        this.directory = directory;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileType = fileType;
    }

    /**
     * Given the {@link Builder} parameters, retrieves the file. Automatically starts a "share" Intent.
     * @throws FileNotFoundException if can't find the file.
     */
    public void shareFile() throws FileNotFoundException {
        int resId = activity.getResources().getIdentifier(fileName, directory, activity.getPackageName());
        InputStream inputStream = null;
        if (isValidResId(resId)) {
            switch (directory) {
                case FOLDER_RAW:
                    inputStream = activity.getResources().openRawResource(resId);
                    break;
                case FOLDER_MIPMAP:
                case FOLDER_DRAWABLE:
                    Uri fileUri;
                    try {
                        fileUri = Uri.parse(String.format(ANDROID_RES_URI, activity.getPackageName(), resId));
                    } catch (IllegalFormatException e) {
                        throw new FileNotFoundException();
                    }
                    if (fileUri != null) {
                        inputStream = activity.getContentResolver().openInputStream(fileUri);
                    }
                    break;
                default:
                    inputStream = activity.getResources().openRawResource(resId);
                    break;
            }
            prepareAndShare(inputStream);
        } else {
            if (directory.equals(FOLDER_ASSETS)) {
                AssetManager assetManager = activity.getAssets();
                try {
                    inputStream = assetManager.open(fileName.concat(".").concat(fileExtension));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileNotFoundException();
                }
                prepareAndShare(inputStream);
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    private void prepareAndShare(InputStream inputStream) {
        try {
            byte[] buff = new byte[1024];
            int len;
            FileOutputStream outputStream = activity.openFileOutput(fileName.concat(".").concat(fileExtension), Context.MODE_PRIVATE);
            if (inputStream != null) {
                while ((len = inputStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, len);
                }
                inputStream.close();
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        Uri uri = FileProvider.getUriForFile(activity, activity.getResources().getString(R.string.rfp_provider_authority), new File(activity.getFilesDir(), fileName.concat(".").concat(fileExtension)));

        Intent intent = ShareCompat.IntentBuilder.from(activity).getIntent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(fileType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    private boolean isValidResId(int resId) {
        return resId != 0;
    }

    /**
     * Builder class: the entry point of this tool.
     */
    public static class Builder {
        private final Activity activity;
        private String directory;
        private String fileName;
        private String fileExtension;
        private String fileType;

        private Builder(Activity activity) {
            this.activity = activity;
        }

        /**
         * @param activity a valid Activity used as Context and to start the "share" Intent
         * @return the Builder itself.
         */
        public static Builder from(@NonNull Activity activity) {
            return new Builder(activity);
        }

        /**
         * Set the directory where the file is stored.
         * @param directory a String representing the directory. Consider using one of the following
         *          {@link ResourceFileProvider#FOLDER_ASSETS}
         *          {@link ResourceFileProvider#FOLDER_RAW}
         *          {@link ResourceFileProvider#FOLDER_DRAWABLE}
         * @return the Builder itself.
         */
        public Builder setDirectory(@NonNull String directory) {
            this.directory = directory;
            return this;
        }

        /**
         * Set the file name.
         * @param fileName the filename as a String, WITHOUT any extension, see {@link Builder#setFileExtension(String)}.
         * @return the Builder itself.
         */
        public Builder setFileName(@NonNull String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * Set the file extension.
         * @param fileExtension the extension as a String, WITHOUT the point.
         * @return the Builder itself.
         */
        public Builder setFileExtension(@NonNull String fileExtension) {
            this.fileExtension = fileExtension;
            return this;
        }

        /**
         * Set the file type. Used for the "share" intent.
         * @param fileType the file type as a String (MIME type). Consider using one of the following
         *          {@link ResourceFileProvider#TYPE_AUDIO}
         *          {@link ResourceFileProvider#TYPE_VIDEO}
         *          {@link ResourceFileProvider#TYPE_IMAGE}
         *          {@link ResourceFileProvider#TYPE_PDF}
         * @return the Builder itself.
         */
        public Builder setFileType(@NonNull String fileType) {
            this.fileType = fileType;
            return this;
        }

        /**
         * Builds the ResourceFileProvider.
         * @return the ResourceFileProvider.
         */
        public ResourceFileProvider build() {
            return new ResourceFileProvider(activity, directory, fileName, fileExtension, fileType);
        }
    }


}
