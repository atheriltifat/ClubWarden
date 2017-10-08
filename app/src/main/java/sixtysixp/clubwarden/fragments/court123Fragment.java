package sixtysixp.clubwarden.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sixtysixp.clubwarden.MainActivity;
import sixtysixp.clubwarden.R;
import sixtysixp.clubwarden.staticClass.ResRepository;

/**
 * Author: Ather Iltifat
 */

public class court123Fragment extends Fragment {
    private final static String MSG = "TestFragment";
    public court123Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MSG, "Fragment On Create .....");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(MSG, "Fragment onCreateView .....");
        return inflater.inflate(R.layout.fragment_court123, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(MSG, "Fragment onViewCreated .....");
        SharedPreferences sharedpreferences = MainActivity.getMainInstance().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        int spUserTypeID = sharedpreferences.getInt("userTypeID", -1);
        int[][] bookingSlotRes = ResRepository.slotRes;
        for (int i=0; i< 12; i++) {
            for (int j=0; j< 3; j++) {
                TextView tv = view.findViewById(bookingSlotRes[i][j]);
                if(spUserTypeID == 1){
                    tv.setOnClickListener(onClickListener);
                }
                tv.setOnLongClickListener(_OnLongClickListener);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(MSG, "Fragment onActivityCreated .....");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(MSG, "Fragment onStart .....");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(MSG, "Fragment onResume .....");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(MSG, "Fragment onPause .....");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(MSG, "Fragment onStop .....");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(MSG, "Fragment onDestroyView .....");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(MSG, "Fragment onDestroy .....");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(MSG, "Fragment onDetach .....");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView txtView = (TextView)v;
            MainActivity.getMainInstance().onClickBookingSlot(txtView);
        }
    };


    private View.OnLongClickListener _OnLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            MainActivity.getMainInstance().onLongClickBookingSlot(v);
            return true;
        }
    };

}
