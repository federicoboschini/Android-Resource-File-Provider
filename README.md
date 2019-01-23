# Android Resource File Provider

Easily share audio, video, image and document files from `raw` and `drawable` folders **without any specific permission**.

## Configuration

Add this to your app `AndroidManifest.xml`:

```xml
<application>
<!--    ...     -->
    <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="@string/rfp_provider_authority"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_provider_paths" />
    </provider>
<!--    ...     -->
</application>
```

(Optional) Add this to your app `strings.xml`:

```xml
<resources>
    <!--    ...     -->
    <string name="rfp_provider_authority">it.my_company.my_app.my_authority</string>
    <!--    ...     -->
</resources>
```

## Share a mp3 file example

```java
try {
    ResourceFileProvider.Builder
        .from(this)
        .setDirectory(FOLDER_RAW)
        .setFileName("my_sound")
        .setFileExtension("mp3")
        .setFileType(TYPE_AUDIO)
        .build()
        .shareFile();
} catch (FileNotFoundException e) {
    e.printStackTrace();
}
```

***Note:*** `setFileName` parameter is **without** the extension.

### Current supported and tested extensions:

* `.mp3`
* `.jpg`
* `.png`
* `.pdf`

### Current supported and tested folders:

* `drawable`
* `raw`

### Known issues:

* Can't get images from `mipmap` folder.

### Coming soon:

* Get files from `assets` folder.
