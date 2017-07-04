package silence.com.cn.a310application.appanalyst;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;

public class GpsMocker {
    private Gps[] mMockGps = new Gps[]{null};

    public void init(@NonNull Context context) {
        if (null == context) {
            throw new NullPointerException("context");
        }
        if (Macro.RealGps) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                false, true, true, false, true, true, true,
                android.location.Criteria.POWER_HIGH,
                android.location.Criteria.ACCURACY_FINE);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }
        );
    }

    public void start(@NonNull Context context, @NonNull Gps gps) {
        synchronized (mMockGps) {
            mMockGps[0] = gps;
        }
        scheduleMockGps(context);
    }

    public boolean isWorking() {
        synchronized (mMockGps) {
            return null != mMockGps[0];
        }
    }

    private void scheduleMockGps(final Context context) {
        Gps gps;
        synchronized (mMockGps) {
            gps = mMockGps[0];
        }
        if (null == gps) {
            return;
        }
        if (!Macro.RealGps) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(gps.mLatitude);
            location.setLongitude(gps.mLongitude);
            location.setAltitude(0);
            location.setBearing(0);
            location.setSpeed(0);
            location.setAccuracy(2);
            location.setTime(System.currentTimeMillis());
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
        }
        new Handler(context.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                scheduleMockGps(context);
            }
        }, 1000);
    }

    public void stop() {
        synchronized (mMockGps) {
            mMockGps[0] = null;
        }
    }
}
