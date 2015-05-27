package com.jskierbi.reactivecombineexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Observable<String> strObservable = Observable.create(new Observable.OnSubscribe<String>() {
      @Override
      public void call(Subscriber<? super String> subscriber) {
        String TAG = "strObservable";
        subscriber.onNext("jeden");
        Log.d(TAG, "jeden");
        subscriber.onNext("dwa");
        Log.d(TAG, "dwa");
        subscriber.onNext("trzy");
        Log.d(TAG, "trzy");
        subscriber.onNext("cztery");
        Log.d(TAG, "cztery");
        subscriber.onNext("piec");
        Log.d(TAG, "piec");
        subscriber.onNext("szesc");
        Log.d(TAG, "szesc");
        subscriber.onCompleted();
        Log.d(TAG, "onCompleted()");
      }
    }).subscribeOn(Schedulers.newThread());

    final Observable<Integer> intObservable = Observable
        .create(new Observable.OnSubscribe<Integer>() {
          @Override
          public void call(Subscriber<? super Integer> subscriber) {
            String TAG = "intObservable";
            subscriber.onNext(1);
            Log.d(TAG, "1");
            subscriber.onCompleted();
            Log.d(TAG, "onCompleted()");
          }
        })
        .subscribeOn(Schedulers.newThread());

    // zip completes after one of observables completes
    Observable
        .zip(strObservable, intObservable, new Func2<String, Integer, String>() {
          @Override
          public String call(String s, Integer integer) {
            Log.d(TAG, "ZIP: " + s + ", " + integer);
            return s + " " + integer;
          }
        })
        .subscribe(new Action1<String>() {
          @Override
          public void call(String o) {
            Log.d(TAG, "ZIP result" + o);
          }
        });

    // combineLatest completes after ALL of observables completes
    Observable
        .combineLatest(strObservable, intObservable, new Func2<String, Integer, String>() {
          @Override
          public String call(String s, Integer integer) {
            Log.d(TAG, "COMBINE LATEST: " + s + ", " + integer);
            return s + " " + integer;
          }
        })
        .subscribe(new Action1<String>() {
          @Override
          public void call(String s) {
            Log.d(TAG, "Combine result: " + s);
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
