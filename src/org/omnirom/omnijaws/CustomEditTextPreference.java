/*
 * Copyright (C) 2012 The CyanogenMod Project (DvTonder)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnirom.omnijaws;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CustomEditTextPreference extends EditTextPreference {
    private AlertDialog mDialog;
    public CustomEditTextPreference(Context context) {
        super(context);
    }
    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        String APIKey = Config.getAPIKey(getContext());
        if (APIKey != null) {
            setSummary(APIKey);
        } else {
            setSummary(R.string.no_owapi_key_title);
        }
    }
    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        mDialog = (AlertDialog) getDialog();
        Button okButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomEditTextPreference.this.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
                if (getEditText().getText().toString().length() > 0) {
                    String text = getEditText().getText().toString();
                    Config.setAPIKey(getContext(), text);
                    setSummary(text);
                    mDialog.dismiss();
                } else {
                    Config.setAPIKey(getContext(), null);
                    setSummary("");
                    setText("");
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        String APIKey = Config.getAPIKey(getContext());
        if (APIKey != null) {
            getEditText().setText(APIKey);
            getEditText().setSelection(APIKey.length());
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // we handle persisting the selected location below, so pretend cancel
        super.onDialogClosed(false);
    }
}
