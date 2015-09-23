package network.bitmesh.filesecure.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import network.bitmesh.filesecure.R;

public class FileViewerFragment extends Fragment {

    public FileViewerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_viewer, container, false);
    }
}
