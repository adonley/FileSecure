package network.bitmesh.filesecure.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import network.bitmesh.filesecure.R;

public class FileViewerFragment extends Fragment {

    private static final String LOCATION_PARAM = "location_param";

    /** Set the default location to sdcard unless otherwise specified in parameters */
    private String currentLocation = "/sdcard";
    private TableLayout tableLayout;

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
        return inflater.inflate(R.layout.fragment_file_viewer, container, false);
    }

    public static FileViewerFragment newInstance(String currentLocation) {
        FileViewerFragment fragment = new FileViewerFragment();
        Bundle args = new Bundle();
        args.putString(LOCATION_PARAM, currentLocation);
        fragment.setArguments(args);
        return fragment;
    }

}
