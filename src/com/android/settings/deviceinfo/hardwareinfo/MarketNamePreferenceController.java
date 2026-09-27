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

public class MarketNamePreferenceController extends BasePreferenceController {

    public MarketNamePreferenceController(Context context, String preferenceKey) {
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
        String marketName = SystemProperties.get("ro.product.marketname");
        if (TextUtils.isEmpty(marketName)) {
            marketName = SystemProperties.get("ro.oppo.market.name");
        }
        if (TextUtils.isEmpty(marketName)) {
            marketName = Build.MODEL;
        }
        return marketName;
    }
}
