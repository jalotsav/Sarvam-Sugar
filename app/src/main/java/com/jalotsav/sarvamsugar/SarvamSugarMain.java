/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jalotsav.sarvamsugar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jalotsav.sarvamsugar.common.UserSessionManager;
import com.jalotsav.sarvamsugar.navgtndrawer.NavgtnDrwrMain;

public class SarvamSugarMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_sarvam_sugar_main);

        final UserSessionManager session = new UserSessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                if(session.checkLogin())
                    startActivity(new Intent(SarvamSugarMain.this, NavgtnDrwrMain.class));
            }
        }, 3000);
    }
}
