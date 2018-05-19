package com.edunext.webservice;

import com.edunext.model.SchoolModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface WebService {


    @GET("/getalldetails")
    Observable<List<SchoolModel>> getApiData();



}
