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

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 31/8/16.
 */
public class MdlDalalWiseSauda {

    @SerializedName("method")
    String method;

    @SerializedName("result")
    String result;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    ArrayList<MdlDalalwsSaudaData> data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MdlDalalwsSaudaData> getData() {
        return data;
    }

    public void setData(ArrayList<MdlDalalwsSaudaData> data) {
        this.data = data;
    }
}
