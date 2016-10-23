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
import com.jalotsav.sarvamsugar.model.MdlDalalWiseSauda;
import com.jalotsav.sarvamsugar.model.MdlGodownStock;
import com.jalotsav.sarvamsugar.model.MdlOutstanding;
import com.jalotsav.sarvamsugar.model.MdlPendngSauda;
import com.jalotsav.sarvamsugar.model.MdlUserLogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by JALOTSAV Dev. on 27/7/16.
 */
public interface RetroAPI {

    @GET("index.aspx")
    Call<MdlUserLogin> userLogin(@Query("method") String method,
                                 @Query("username") String username,
                                 @Query("password") String password);

    @GET("index.aspx")
    Call<MdlOutstanding> getOutstanding(@Query("method") String method,
                                        @Query("todate") String toDate,
                                        @Query("type") String type,
                                        @Query("party") String party,
                                        @Query("dalal") String dalal,
                                        @Query("area") String area,
                                        @Query("zone") String zone,
                                        @Query("sortby") String sortby);

    @GET("index.aspx")
    Call<ResponseBody> getPendingSauda(@Query("method") String method,
                                         @Query("fromdate") String fromDate,
                                         @Query("todate") String toDate,
                                         @Query("party") String party,
                                         @Query("dalal") String dalal,
                                         @Query("item") String item,
                                         @Query("area") String area);

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
                                              @Query("dalal") String dalal,
                                              @Query("mobile") String mobile);

    @GET("index.aspx")
    Call<MdlDalalWiseSauda> getDalalwisesauda(@Query("method") String method,
                                              @Query("fromdate") String fromDate,
                                              @Query("todate") String toDate,
                                              @Query("dalal") String dalal);
}
