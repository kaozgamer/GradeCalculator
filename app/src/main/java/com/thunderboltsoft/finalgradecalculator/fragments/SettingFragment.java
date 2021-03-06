package com.thunderboltsoft.finalgradecalculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.thunderboltsoft.finalgradecalculator.R;

/**
 * Fragment that lets the user edit the settings of the app.
 * <p/>
 * NOT TO BE SENT TO PRODUCTION VERSION AT THIS STAGE.
 *
 * @author Thushan Perera
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_setting);
        Preference contactDeveloper = findPreference("pref_contact_dev");

        // Email the developer
        contactDeveloper.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent mailto = new Intent(Intent.ACTION_SEND);
                mailto.setType("message/rfc822");
                mailto.putExtra(Intent.EXTRA_EMAIL, new String[]{"kaozgamerdev+grade_calculator@gmail.com"});
                mailto.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_email_subject));
                mailto.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.feedback_email_text));
                startActivity(Intent.createChooser(mailto, getResources().getString(R.string.email_intent_chooser)));

                return true;
            }
        });
    }
}
