/*
 * Copyright (C) 2024 The LineageOS Project
 * Copyright (C) 2024 Paranoid Android
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

package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class SoCModelPreferenceController extends BasePreferenceController {

    public SoCModelPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean useDynamicSliceSummary() {
        return true;
    }

    @Override
    public CharSequence getSummary() {
        String socModel = SystemProperties.get("ro.soc.model");
        if (TextUtils.isEmpty(socModel)) {
            socModel = Build.SOC_MODEL;
        }
        if (TextUtils.isEmpty(socModel) || Build.UNKNOWN.equalsIgnoreCase(socModel)) {
            socModel = SystemProperties.get("ro.board.platform");
        }
        if (TextUtils.isEmpty(socModel) || Build.UNKNOWN.equalsIgnoreCase(socModel)) {
            return mContext.getString(R.string.summary_placeholder);
        }
        String manufacturer = SystemProperties.get("ro.soc.manufacturer");
        if (!TextUtils.isEmpty(manufacturer) && !socModel.toLowerCase().contains(manufacturer.toLowerCase())) {
            return manufacturer + " " + socModel;
        }
        return socModel;
    }
}
