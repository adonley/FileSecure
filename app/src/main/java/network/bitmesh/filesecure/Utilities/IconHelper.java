package network.bitmesh.filesecure.Utilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

import network.bitmesh.filesecure.R;

public class IconHelper
{
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth)
    {
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (width > reqWidth) {

            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, File file,
                                                         int reqWidth)
    {
        return decodeSampledBitmapFromResource(res, getIconForFileType(file), reqWidth);
    }

    public static int getIconForFileType(File file)
    {
        String fileName = file.getName();
        int resourceId = R.mipmap.file;

        if(file.isDirectory())
            resourceId = R.mipmap.directory;
        else if(fileName.endsWith(".crypt"))
            resourceId = R.mipmap.encrypted;

        return resourceId;
    }
}
