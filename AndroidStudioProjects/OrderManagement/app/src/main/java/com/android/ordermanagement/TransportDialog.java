package com.android.ordermanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by challa on 26/02/17.
 */

public class TransportDialog extends DialogFragment implements TransportListAdapter.ViewClickListener {

    private List<String> transports;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Use this instance of the interface to deliver action events
    private TransportDialogListener mListener;

    public static TransportDialog newInstance(ArrayList<String> estimates) {
        Bundle args = new Bundle();

        TransportDialog fragment = new TransportDialog();
        args.putStringArrayList("estimates", estimates);
        fragment.setArguments(args);
        return fragment;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TransportDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.transport_dialog_layout, null);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.transport_list);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        transports = getArguments().getStringArrayList("estimates");
        // specify an adapter (see also next example)
        mAdapter = new TransportListAdapter(transports, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        // Use the Builder class for convenient dialog construction
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select Transport")
                .setCancelable(false)
                .setView(v)
                .create();
    }

    @Override
    public void onViewClick(View view, String currentEstimate) {
        mListener.onDialogItemClick(this, currentEstimate);
    }

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TransportDialogListener {
        void onDialogItemClick(DialogFragment dialog, String estimate);
    }
}

