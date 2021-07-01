package phone.vishnu.quotes.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import phone.vishnu.quotes.R;
import phone.vishnu.quotes.model.Quote;

public class ExportHelper {

    private final SharedPreferenceHelper sharedPreferenceHelper;
    private final Context context;

    public ExportHelper(Context context) {
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        this.context = context;
    }

    public void exportBackgroundImage(Bitmap image) {
        try {

            FileOutputStream fOutputStream = new FileOutputStream(getBGPath());
            BufferedOutputStream bos = new BufferedOutputStream(fOutputStream);

            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            fOutputStream.flush();
            fOutputStream.close();

            sharedPreferenceHelper.setBackgroundPath(getBGPath());

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public String getBGPath() {
        return new File(context.getFilesDir(), "background.jpg").toString();
    }

    public String getSSPath() {
        return new File(context.getFilesDir(), "screenshot.jpg").toString();
    }

    public void shareImage(Context context, Quote q) {

        String quote = q.getQuote();
        String author = q.getAuthor();

        View shareView = View.inflate(context, R.layout.share_layout, null);

        String cardColor = sharedPreferenceHelper.getCardColorPreference();
        String fontColor = sharedPreferenceHelper.getFontColorPreference();
        String fontPath = sharedPreferenceHelper.getFontPath();

        String backgroundPath = sharedPreferenceHelper.getBackgroundPath();
        if (!"-1".equals(backgroundPath))
            shareView.findViewById(R.id.shareRelativeLayout).setBackground(Drawable.createFromPath(backgroundPath));

        CardView cardView = shareView.findViewById(R.id.shareCardView);
        cardView.setCardBackgroundColor(Color.parseColor(cardColor));

        TextView shareQuoteTextView = shareView.findViewById(R.id.shareQuoteTextView);
        TextView shareAuthorTextView = shareView.findViewById(R.id.shareAuthorTextView);

        if (!(fontPath.equals("-1")) && (new File(fontPath).exists())) {
            try {
                Typeface face = Typeface.createFromFile(fontPath);
                shareQuoteTextView.setTypeface(face);
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }

        shareQuoteTextView.setTextColor(Color.parseColor(fontColor));
        shareAuthorTextView.setTextColor(Color.parseColor(fontColor));

        shareQuoteTextView.setText(quote);
        shareAuthorTextView.setText(author);

        int widthPixels = 1080;
        int heightPixels = 1920;

        shareView.measure(View.MeasureSpec.makeMeasureSpec(widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(heightPixels, View.MeasureSpec.EXACTLY));

        Bitmap bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        shareView.layout(0, 0, widthPixels, heightPixels);
        shareView.draw(c);

        File file = null;

        try {

            file = File.createTempFile("screenshot", ".jpg", context.getCacheDir());

            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException | SecurityException e) {

            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();

        } finally {

            if (file != null) {

                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(file.getAbsolutePath()));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "For more, visit: https://kutt.it/Quotes");

                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        }
    }

    public void saveImage(Context context, Quote q) {

        String quote = q.getQuote();
        String author = q.getAuthor();

        View shareView = View.inflate(context, R.layout.share_layout, null);

        String cardColor = sharedPreferenceHelper.getCardColorPreference();
        String fontColor = sharedPreferenceHelper.getFontColorPreference();
        String fontPath = sharedPreferenceHelper.getFontPath();

        String backgroundPath = sharedPreferenceHelper.getBackgroundPath();
        if (!"-1".equals(backgroundPath))
            shareView.findViewById(R.id.shareRelativeLayout).setBackground(Drawable.createFromPath(backgroundPath));

        CardView cardView = shareView.findViewById(R.id.shareCardView);
        cardView.setCardBackgroundColor(Color.parseColor(cardColor));

        TextView shareQuoteTextView = shareView.findViewById(R.id.shareQuoteTextView);
        TextView shareAuthorTextView = shareView.findViewById(R.id.shareAuthorTextView);

        if (!(fontPath.equals("-1")) && (new File(fontPath).exists())) {
            try {
                Typeface face = Typeface.createFromFile(fontPath);
                shareQuoteTextView.setTypeface(face);
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }

        shareQuoteTextView.setTextColor(Color.parseColor(fontColor));
        shareAuthorTextView.setTextColor(Color.parseColor(fontColor));

        shareQuoteTextView.setText(quote);
        shareAuthorTextView.setText(author);

        int widthPixels = 1080;
        int heightPixels = 1920;

        shareView.measure(View.MeasureSpec.makeMeasureSpec(widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(heightPixels, View.MeasureSpec.EXACTLY));

        Bitmap bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        shareView.layout(0, 0, widthPixels, heightPixels);
        shareView.draw(c);

        //FIXME:Check saving for <28

        try {
            saveBitmap(context, bitmap, Bitmap.CompressFormat.JPEG, "image/jpg", "Quotes - " + System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
            Toast.makeText(context, "Oops! Something went wrong!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                            @NonNull final Bitmap.CompressFormat format,
                            @NonNull final String mimeType,
                            @NonNull final String displayName) throws IOException {

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + context.getString(R.string.app_name));
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());

        final ContentResolver resolver = context.getContentResolver();
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);

            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");

            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null)
                    throw new IOException("Failed to open output stream.");

                if (!bitmap.compress(format, 100, stream))
                    throw new IOException("Failed to save bitmap.");
            }

        } catch (IOException e) {
            if (uri != null)
                resolver.delete(uri, null, null);
            throw e;
        }
    }
}
