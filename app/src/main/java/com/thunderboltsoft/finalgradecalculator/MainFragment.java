package com.thunderboltsoft.finalgradecalculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NewAssessment.OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity main = (MainActivity) getActivity();

        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView txtView = (TextView) view.findViewById(R.id.textView);

//        String counter = Integer.toString(main.getNumAssessments());
        String currentGrade = String.format("%.2f", main.getCurrentGrade());
        txtView.setText(currentGrade);

        final EditText editTxtDesiredGrade = (EditText) view.findViewById(R.id.editTextDesiredGrade);
        editTxtDesiredGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTxtDesiredGrade.getText().toString().equals("")) {
                    double gradeNeeded = main.getGradeNeeded(Double.parseDouble(editTxtDesiredGrade.getText().toString()));

                    TextView desiredExamGrade = (TextView) view.findViewById(R.id.textViewDesiredResult);
                    String neededGrade = "You need at least " + String.format("%.2f", gradeNeeded).toString() + "% to achieve a course grade of " + String.format("%.2f", Double.parseDouble(editTxtDesiredGrade.getText().toString())).toString() + "%";
                    desiredExamGrade.setText(neededGrade);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        Button btnCalc = (Button) view.findViewById(R.id.btnCalcNeededGrade);
//        btnCalc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView desiredExamGrade = (TextView) view.findViewById(R.id.textViewDesiredResult);
//
//                EditText editTxtDesiredGrade = (EditText) view.findViewById(R.id.editTextDesiredGrade);
//
//                String desiredGradeString = editTxtDesiredGrade.getText().toString();
//                if (desiredGradeString.matches("")) {
//                    editTxtDesiredGrade.setText(String.format("%.2f", 50.0).toString());
//
//                    String neededGrade = "You need at least " + String.format("%.2f", main.getGradeNeeded(50.0)).toString() + "% to achieve a course grade of " + String.format("%.2f", Double.parseDouble(editTxtDesiredGrade.getText().toString())).toString() + "%";
//                    desiredExamGrade.setText(neededGrade);
//                } else {
//                    String neededGrade = "You need at least " + String.format("%.2f", main.getGradeNeeded(Double.parseDouble(desiredGradeString))).toString() + "% to achieve a course grade of " + String.format("%.2f", Double.parseDouble(desiredGradeString)).toString() + "%";
//                    desiredExamGrade.setText(neededGrade);
//                }
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewAssessment fragment = new NewAssessment();
                fragmentTransaction.replace(R.id.main_screen, fragment);
                fragmentTransaction.commit();
            }
        });

        ListView mainList = (ListView) view.findViewById(R.id.assessmentListView);
        ArrayList<String> assessmentList = new ArrayList<>();
        assessmentList.addAll(Arrays.asList(main.getAssessmentsList()));

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(view.getContext(), R.layout.list_row, assessmentList);

        mainList.setAdapter(listAdapter);

        return view;
    }
}
