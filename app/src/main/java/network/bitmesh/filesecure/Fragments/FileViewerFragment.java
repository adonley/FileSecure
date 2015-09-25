package network.bitmesh.filesecure.Fragments;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Arrays;

import network.bitmesh.filesecure.R;
import network.bitmesh.filesecure.Utilities.IconHelper;

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
        Log.i(TAG, "Current path - " + path);
        tableLayout.removeAllViews();
        currentLocation = path;

        File[] filesArray = readFilesFromDirectory(path);
        ArrayList<File> files = new ArrayList<>();
        File currentDir = new File(currentLocation);
        currentDirectory.setText(currentDir.getAbsolutePath());

        Log.i(TAG, "AbsolutePath - " + currentDir.getAbsolutePath());

        // TODO: Generalize the external write dir name
        if(currentDir.isDirectory() && currentDir.getAbsolutePath() != "/sdcard")
            files.add(new File(currentDir.getParent()));

        files.addAll(Arrays.asList(filesArray));

        for(int i = 0; i < files.size(); i=i+3)
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

            // Special case for the parent directory
            if(i == 0 && currentLocation != "/sdcard")
                fileName0.setText("..");
            else
                fileName0.setText(files.get(i).getName());

            fileIcon0.setImageBitmap(IconHelper.decodeSampledBitmapFromResource(getResources(), files.get(i), 100));
            col1.setOnClickListener(new OnFileClickListener(files.get(i)));

            if((i+1) < files.size())
            {
                fileName1.setText(files.get(i + 1).getName());
                fileIcon1.setImageBitmap(IconHelper.decodeSampledBitmapFromResource(getResources(), files.get(i+1), 100));
                col2.setOnClickListener(new OnFileClickListener(files.get(i + 1)));
            }

            if((i+2) < files.size())
            {
                fileName2.setText(files.get(i+2).getName());
                fileIcon2.setImageBitmap(IconHelper.decodeSampledBitmapFromResource(getResources(), files.get(i+2), 100));
                col3.setOnClickListener(new OnFileClickListener(files.get(i + 2)));
            }

            tableLayout.addView(row);
        }
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
