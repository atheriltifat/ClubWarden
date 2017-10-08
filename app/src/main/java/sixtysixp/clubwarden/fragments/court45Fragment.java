package sixtysixp.clubwarden.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class court45Fragment extends Fragment {

    public court45Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_court45, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView tv;
        SharedPreferences sharedpreferences = MainActivity.getMainInstance().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        int spUserTypeID = sharedpreferences.getInt("userTypeID", -1);
        int[][] bookingSlotRes = ResRepository.slotRes;
        for (int i=0; i< 12; i++) {
            for (int j=3; j< 5; j++) {
                tv = view.findViewById(bookingSlotRes[i][j]);
                if(spUserTypeID == 1) {
                    tv.setOnClickListener(onClickListener);
                }
                tv.setOnLongClickListener(_OnLongClickListener);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.getMainInstance().removeSharedPrefInAPIClient();
        MainActivity.getMainInstance().getBookingTblData();
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
