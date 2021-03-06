package com.thunderboltsoft.finalgradecalculator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.thunderboltsoft.finalgradecalculator.R;
import com.thunderboltsoft.finalgradecalculator.activities.MainActivity;
import com.thunderboltsoft.finalgradecalculator.models.Assessment;

/**
 * Represents the dialog fragment that will hold the "Add new assessment" fragment.
 * User can use this to enter the weight and grade of an assessment.
 *
 * @author Thushan Perera
 */
public class NewAssessmentDialogFragment extends DialogFragment {

    /**
     * Unique ID for the MaterialShowcaseView.
     */
    final private String SHOWCASE_ID = "Test3";

    /**
     * The EditText box for user to enter the weighting of assessment.
     */
    private EditText mWeight;

    /**
     * The EditText box for user to enter the grade of the assessment.
     */
    private EditText mGrade;

    /**
     * The assessment to be added.
     */
    private Assessment mAssessment;

    /**
     * Sets the assessments from an existing assessment.
     *
     * @param assessment existing assessment
     */
    public void setFromListView(Assessment assessment) {
        mAssessment = assessment;
    }

    /**
     * Create a new NewAssessmentDialogFragment fragment.
     *
     * @param editMode params
     * @return new fragment
     */
    public static NewAssessmentDialogFragment newInstance(boolean editMode) {
        NewAssessmentDialogFragment newAssessmentDialogFragment = new NewAssessmentDialogFragment();

        // Just to test Bundles
        Bundle args = new Bundle();
        args.putBoolean("isEditMode", editMode);
        newAssessmentDialogFragment.setArguments(args);

        return newAssessmentDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.fragment_new_assessment, null);

        mWeight = (EditText) view.findViewById(R.id.txtEnterWeight);
        mGrade = (EditText) view.findViewById(R.id.txtEnterGrade);

        // If assessment is given, populate EditText values from that
        if (mAssessment != null) {
            mWeight.setText(String.valueOf(mAssessment.getWeight()));
            mGrade.setText(String.valueOf(mAssessment.getGrade()));
        }

        // Add OK and Cancel buttons to our dialog
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.dialog_new_assessment_title))
                .setPositiveButton(getResources().getString(R.string.dialog_new_assessment_positive_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do Nothing
                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.dialog_new_assessment_negative_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Boolean canEdit = getArguments().getBoolean("isEditMode");

                                if (canEdit) {

                                    // Send the assessment to the activity to be added to the list of assessments
                                    MainActivity activity = (MainActivity) getActivity();

                                    activity.sendAssessment(mAssessment);

                                    // Hide the soft keyboard - annoying
                                    InputMethodManager imm = (InputMethodManager) activity.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }

                                dialog.dismiss(); // Do not do anything with the user entered values
                            }
                        }
                );

        Boolean canEdit = getArguments().getBoolean("isEditMode");

        if (canEdit) { // Check if opening up after the user clicks on an assessment in the assessments fragment
            b.setNeutralButton(getResources().getString(R.string.dialog_new_assessment_neutral_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.removeFromDataSet();
                            dialog.dismiss();
                        }
                    });
        }

        b.setView(view);


        return b.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);

            // First validate the fields, show any error messages, then bundle into Assessment object and send to the main activity
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean shouldDismiss = true;

                    if (mGrade.getText().toString().isEmpty()) {
                        mGrade.setError(getResources().getString(R.string.dialog_new_assessment_error));
                        shouldDismiss = false;
                    }

                    if (mWeight.getText().toString().isEmpty()) {
                        mWeight.setError(getResources().getString(R.string.dialog_new_assessment_error));
                        shouldDismiss = false;
                    }

                    if (!shouldDismiss) {
                        return;
                    }

                    double weightDouble = Double.parseDouble(mWeight.getText().toString());
                    double gradeDouble = Double.parseDouble(mGrade.getText().toString());

                    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_new_assessment, null);

                    Assessment newAssessment = new Assessment(weightDouble, gradeDouble);

                    // Send the assessment to the activity to be added to the list of assessments
                    MainActivity activity = (MainActivity) getActivity();

                    if (newAssessment.isGradeValid()) {
                        mGrade.setError(null);
                    } else {
                        mGrade.setError(getResources().getString(R.string.dialog_new_assessment_error));
                        shouldDismiss = false;
                    }

                    if (newAssessment.isWeightValid()) {
                        mWeight.setError(null);
                    } else {
                        mWeight.setError(getResources().getString(R.string.dialog_new_assessment_error));
                        shouldDismiss = false;
                    }

                    if (newAssessment.isValid()) {
                        activity.sendAssessment(newAssessment);
                    }

                    // Hide the soft keyboard - annoying
                    InputMethodManager imm = (InputMethodManager) activity.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    if (shouldDismiss) {
                        dismiss(); // Close the dialog
                    }
                }
            });
        }
    }
}
