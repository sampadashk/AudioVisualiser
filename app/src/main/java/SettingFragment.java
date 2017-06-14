import android.content.SharedPreferences;
import android.example.com.visualizerpreferences.R;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by KV on 12/6/17.
 */

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener,Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_visualiser);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Preference preference=findPreference(getString(R.string.pref_size_key));
        preference.setOnPreferenceChangeListener(this);
        PreferenceScreen prefscreen = getPreferenceScreen();

        int count = prefscreen.getPreferenceCount();
        Log.d("countval",""+count);
        for (int i = 0; i < count; i++)
        {
            Preference p = prefscreen.getPreference(i);


        if(!(p instanceof CheckBoxPreference))
            {
                String value=sharedPreferences.getString(p.getKey(),"");
                Log.d("checkval",value);
                setPreferenceSummary(p,value);
            }
    }
    }
    private void setPreferenceSummary(Preference preference,String value)
    {
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference=(ListPreference)preference;
            int preindex=listPreference.findIndexOfValue(value);
            if(preindex>=0)
            {
               listPreference.setSummary(listPreference.getEntries()[preindex]);
            }


        }
        else if(preference instanceof EditTextPreference)
        {
            preference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


Preference pref=findPreference(key);
        if(pref!=null)
        {
            if(!(pref instanceof CheckBoxPreference))
            {
                String value=sharedPreferences.getString(pref.getKey(),"");
                setPreferenceSummary(pref,value);
            }
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3", Toast.LENGTH_SHORT);

        String sizeKey = getString(R.string.pref_size_key);
        if (preference.getKey().equals(sizeKey)) {
            String stringSize = ((String) (newValue)).trim();
            if (stringSize.equals("")) stringSize = "1";
            try {
                float size = Float.parseFloat(stringSize);
                if (size > 3 || size <= 0) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                error.show();
                return false;
            }
        }
        return true;
    }
}
