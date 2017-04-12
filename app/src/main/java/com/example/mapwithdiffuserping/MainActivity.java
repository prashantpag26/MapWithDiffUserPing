package com.example.mapwithdiffuserping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<UserModel> userModelArrayList;
    private GoogleMap mGoogleMap;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userModelArrayList = new ArrayList<>();

        addDataArrayList(23.026451, 72.508135, "Prashant Gosai", "http://blogs-images.forbes.com/jacobmorgan/files/2015/05/Jacob-Morgan_avatar_1430962685-400x400.jpg");
        addDataArrayList(23.012238, 72.557487, "Jatin Patel", "http://www.danielgroupus.com/hs-fs/hub/63072/file-14997515-jpg/images/jeremywilcomb_thedanielgroup.jpg?t=1430422772000&width=229&height=229&name=jeremywilcomb_thedanielgroup.jpg");
        addDataArrayList(23.013818, 72.546329, "Paras Ghasadiya", "http://www.irishlifecorporatebusiness.ie/sites/default/files/slider/employee_2.jpg");
        addDataArrayList(23.018400, 72.515945, "Vimal Chauhan", "http://www.groupmgmt.com/_CE/pagecontent/Images/employee-benefits/employee-benefits.jpg");
        addDataArrayList(23.055681, 72.550793, "Bindra Vitra", "https://pbs.twimg.com/profile_images/433026535738470400/KcGjEiKX.jpeg");
        addDataArrayList(23.044308, 72.591476, "Mohak Sahu", "http://www.oneindia.com/img/2014/12/16-1418731854-vivekmurthy.jpg");
        addDataArrayList(23.013028, 72.602291, "Bhadress Pani", "https://thumbs.dreamstime.com/t/can-t-remember-closeup-portrait-senior-mature-man-thinking-daydreaming-trying-hard-to-something-looking-confused-isolated-white-39479985.jpg");

       initViews();
    }

    private void initViews() {

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        addCustomMarker();
    }

    private void addCustomMarker() {
        if (mGoogleMap == null) {
            return;
        }

        // adding a marker on map with image from  drawable
       /* mGoogleMap.addMarker(new MarkerOptions()
                .position(mDummyLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.avatar))));*/
        for (int i = 0; i < userModelArrayList.size(); i++) {
            final LatLng latlong = new LatLng(userModelArrayList.get(i).getLatitude(), userModelArrayList.get(i).getLongitude());
            // adding a marker with image from URL using glide image loading library
            final int finalI = i;
            Glide.with(getApplicationContext()).
                    load(userModelArrayList.get(i).getUrl())
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(latlong)
                                    .title(userModelArrayList.get(finalI).getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10f));


                        }
                    });
        }


    }


    private void addDataArrayList(double latitude, double longitude, String name, String userPicUrl) {
        final UserModel userModel = new UserModel();
        userModel.setLatitude(latitude);
        userModel.setLongitude(longitude);
        userModel.setName(name);
        userModel.setUrl(userPicUrl);
        userModelArrayList.add(userModel);
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }
}

