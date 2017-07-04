package silence.com.cn.a310application.appanalyst;

public class Gps {
    public float mLatitude;
    public float mLongitude;

    public Gps(float latitude, float longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Gps(Gps gps) {
        mLatitude = gps.mLatitude;
        mLongitude = gps.mLongitude;
    }
}
