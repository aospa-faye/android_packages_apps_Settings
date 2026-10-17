/*
 * Copyright (C) 2026 Paranoid Android
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

package com.android.settings.display;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;

public class ShowRefreshRatePreferenceController extends TogglePreferenceController {

    private static final String SURFACE_FLINGER_SERVICE_KEY = "SurfaceFlinger";
    private static final int SURFACE_FLINGER_CODE = 1034;
    private static final String SURFACE_COMPOSER_INTERFACE_KEY = "android.ui.ISurfaceComposer";

    private static final int SETTING_VALUE_QUERY = 2;
    private static final int SETTING_VALUE_ON = 1;
    private static final int SETTING_VALUE_OFF = 0;

    public ShowRefreshRatePreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public boolean isChecked() {
        try {
            final IBinder flinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
            if (flinger != null) {
                final Parcel data = Parcel.obtain();
                final Parcel reply = Parcel.obtain();
                data.writeInterfaceToken(SURFACE_COMPOSER_INTERFACE_KEY);
                data.writeInt(SETTING_VALUE_QUERY);
                flinger.transact(SURFACE_FLINGER_CODE, data, reply, 0);
                final boolean enabled = reply.readBoolean();
                reply.recycle();
                data.recycle();
                return enabled;
            }
        } catch (RemoteException ex) {
            // intentional no-op
        }
        return false;
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        try {
            final IBinder flinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
            if (flinger != null) {
                final Parcel data = Parcel.obtain();
                data.writeInterfaceToken(SURFACE_COMPOSER_INTERFACE_KEY);
                data.writeInt(isChecked ? SETTING_VALUE_ON : SETTING_VALUE_OFF);
                flinger.transact(SURFACE_FLINGER_CODE, data, null, 0);
                data.recycle();
                return true;
            }
        } catch (RemoteException ex) {
            // intentional no-op
        }
        return false;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_display;
    }
}
