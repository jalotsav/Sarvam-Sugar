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

package com.jalotsav.sarvamsugar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JALOTSAV Dev. on 27/8/16.
 */
public class MdlDalalwsSlsData {

    @SerializedName("DPCODE")
    String dpCode;

    @SerializedName("DALAL")
    String dalal;

    @SerializedName("MADHUR")
    String madhur;

    @SerializedName("OTHER")
    String other;

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getDalal() {
        return dalal;
    }

    public void setDalal(String dalal) {
        this.dalal = dalal;
    }

    public String getMadhur() {
        return madhur;
    }

    public void setMadhur(String madhur) {
        this.madhur = madhur;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
