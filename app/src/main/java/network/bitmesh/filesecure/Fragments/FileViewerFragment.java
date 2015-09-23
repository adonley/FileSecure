package network.bitmesh.filesecure.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import network.bitmesh.filesecure.R;

public class FileViewerFragment extends Fragment {

    private static final String LOCATION_PARAM = "location_param";
    private static final String TAG = "FileViewerFragment";

    /** Set the default location to sdcard unless otherwise specified in parameters */
    private String currentLocation = "/sdcard";
    private TableLayout tableLayout;
    private TextView currentDirectory;

    public FileViewerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentLocation = getArguments().getString(LOCATION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_file_viewer, container, false);
        tableLayout = (TableLayout) root.findViewById(R.id.file_table);
        currentDirectory = (TextView) root.findViewById(R.id.current_location_view);
        updateFileView(currentLocation);
        return root;
    }

    public static FileViewerFragment newInstance() {
        FileViewerFragment fragment = new FileViewerFragment();
        return fragment;
    }

    public static FileViewerFragment newInstance(@NonNull String currentLocation) {
        FileViewerFragment fragment = new FileViewerFragment();
        Bundle args = new Bundle();
        args.putString(LOCATION_PARAM, currentLocation);
        fragment.setArguments(args);
        return fragment;
    }

    protected File[] readFilesFromDirectory(@NonNull String path) {
        File directory = new File(path);

        if(!directory.exists())
            Log.e(this.TAG,"Path does not exist.");

        if(!directory.isDirectory())
            Log.e(this.TAG,"Path is not a directory.");

        return directory.listFiles();
    }

    protected void updateFileView(@NonNull String path)
    {
        currentDirectory.setText(path);
        tableLayout.removeAllViews();

        // Probably not the best way to calculate image size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int reqWidth = display.getWidth() / 3;
        Log.i(TAG,"reqWidth: " + reqWidth);

        File[] files = readFilesFromDirectory(path);

        for(int i = 0; i < files.length; i=i+3)
        {
            TableRow row = new TableRow(this.getContext());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflated = inflater.inflate(R.layout.file_table_row, row);

            LinearLayout col1 = (LinearLayout) inflated.findViewById(R.id.left_file);
            LinearLayout col2 = (LinearLayout) inflated.findViewById(R.id.center_file);
            LinearLayout col3 = (LinearLayout) inflated.findViewById(R.id.right_file);

            ImageView fileIcon0 = (ImageView) inflated.findViewById(R.id.fileIconView0);
            ImageView fileIcon1 = (ImageView) inflated.findViewById(R.id.fileIconView1);
            ImageView fileIcon2 = (ImageView) inflated.findViewById(R.id.fileIconView2);

            TextView fileName0 = (TextView) inflated.findViewById(R.id.fileNameView0);
            TextView fileName1 = (TextView) inflated.findViewById(R.id.fileNameView1);
            TextView fileName2 = (TextView) inflated.findViewById(R.id.fileNameView2);

            fileName0.setText(files[i].getName());
            fileIcon0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.apk, 100));
            col1.setOnClickListener(new OnFileClickListener(files[i]));

            if((i+1) < files.length)
            {
                fileName1.setText(files[i + 1].getName());
                fileIcon1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.apk, 100));
                col2.setOnClickListener(new OnFileClickListener(files[i + 1]));
            }

            if((i+2) < files.length)
            {
                fileName2.setText(files[i+2].getName());
                fileIcon2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.apk, 100));
                col3.setOnClickListener(new OnFileClickListener(files[i + 2]));
            }

            tableLayout.addView(row);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
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

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth) {

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

    protected class OnFileClickListener implements View.OnClickListener
    {
        private File file;

        public OnFileClickListener(File file)
        {
            this.file = file;
        }

        @Override
        public void onClick(View v)
        {
            if(file.isDirectory())
                updateFileView(file.getAbsolutePath());
        }
    }

}
