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

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class TotalRAMPreferenceController extends BasePreferenceController {

    public TotalRAMPreferenceController(Context context, String preferenceKey) {
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
        String propRam = SystemProperties.get("ro.device.ram");
        if (TextUtils.isEmpty(propRam)) {
            propRam = SystemProperties.get("ro.total_ram");
        }
        if (!TextUtils.isEmpty(propRam)) {
            return propRam;
        }

        ActivityManager am = mContext.getSystemService(ActivityManager.class);
        if (am != null) {
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memInfo);
            if (memInfo.totalMem > 0) {
                return formatRam(memInfo.totalMem);
            }
        }

        return mContext.getString(R.string.summary_placeholder);
    }

    private String formatRam(long totalMemBytes) {
        double ramGB = totalMemBytes / (1000.0 * 1000.0 * 1000.0);
        int[] tiers = {2, 3, 4, 6, 8, 12, 16, 18, 24, 32, 64};
        for (int tier : tiers) {
            if (ramGB <= tier) {
                return tier + " GB";
            }
        }
        return (int) Math.ceil(ramGB) + " GB";
    }
}
