package edu.pacificu.cs.group6Boomshine.edu.pacificu.cs.userauth;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
  private static Retrofit cRetrofitInstance;

  public static Retrofit getInstance() {
    if (cRetrofitInstance == null)
    {
      cRetrofitInstance = new Retrofit.Builder().baseUrl("http://ead5d06e.ngrok.io/")
              .addConverterFactory(ScalarsConverterFactory.create())
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }
    return cRetrofitInstance;
  }
}
