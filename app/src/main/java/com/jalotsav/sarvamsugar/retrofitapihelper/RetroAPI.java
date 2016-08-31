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

package com.jalotsav.sarvamsugar.retrofitapihelper;

import com.jalotsav.sarvamsugar.model.MdlDalalWiseSales;
import com.jalotsav.sarvamsugar.model.MdlGodownStock;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by JALOTSAV Dev. on 27/7/16.
 */
public interface RetroAPI {

    @GET("index.aspx")
    Call<ResponseBody> getGodownStock(@Query("method") String method,
                                      @Query("fromdate") String fromDate,
                                      @Query("todate") String toDate);

    @GET("index.aspx")
    Call<ResponseBody> getMasterDtls(@Query("method") String method);

    @GET("index.aspx")
    Call<MdlDalalWiseSales> getDalalwisesales(@Query("method") String method,
                                              @Query("fromdate") String fromDate,
                                              @Query("todate") String toDate,
                                              @Query("dalal") String dalal);
}