package org.smartregister.chw.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfConversion;
import com.mapbox.turf.TurfMeasurement;
import com.mapbox.turf.TurfMisc;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.R;
import org.smartregister.chw.application.ChwApplication;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.model.CommunityResponderModel;
import org.smartregister.chw.core.utils.CoreConstants;

import java.util.ArrayList;
import java.util.List;

import io.ona.kujaku.listeners.OnFeatureClickListener;
import io.ona.kujaku.views.KujakuMapView;

public class AncMemberMapActivity extends AppCompatActivity {

    public static final String RECYCLER_VIEW_POSITION_PROPERTY = "recycler-view-position";
    private KujakuMapView kujakuMapView;
    private GeoJsonSource communityTransportersSource;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anc_member_map);

        kujakuMapView = findViewById(R.id.kujakuMapView);
        kujakuMapView.onCreate(savedInstanceState);
        kujakuMapView.showCurrentLocationBtn(true);
        kujakuMapView.setDisableMyLocationOnMapMove(true);

        userLocation = extractUserLocation(savedInstanceState);

        kujakuMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                Style.Builder builder = new Style.Builder().fromUri("asset://ba_anc_style.json");
                mapboxMap.setStyle(builder, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        communityTransportersSource = style.getSourceAs("community-transporters-data-set");

                        FeatureCollection featureCollection = loadCommunityTransporters();
                        showCommunityTransporters(mapboxMap, featureCollection);

                        zoomToPatientLocation(mapboxMap);
                        addCommunityTransporterClickListener(kujakuMapView);
                    }
                });
            }
        });
    }

    private void addCommunityTransporterClickListener(@NonNull KujakuMapView kujakuMapView) {
        kujakuMapView.setOnFeatureClickListener(new OnFeatureClickListener() {
            @Override
            public void onFeatureClick(List<Feature> features) {
                // We only pick the first one
                Feature feature = features.get(0);

                Number recyclerViewPosition = feature.getNumberProperty(RECYCLER_VIEW_POSITION_PROPERTY);
                scrollToCardAtPosition(recyclerViewPosition.intValue());
            }
        }, "community-transporters");
    }

    private void scrollToCardAtPosition(int itemPosition) {

    }

    @Nullable
    private LatLng extractUserLocation(Bundle savedInstanceState) {
        return null;
    }

    private void zoomToPatientLocation(@NonNull MapboxMap mapboxMap) {
        if (userLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(16)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void showCommunityTransporters(@NonNull MapboxMap mapboxMap, @Nullable FeatureCollection featureCollection) {
        if (featureCollection != null && featureCollection.features() != null && communityTransportersSource != null) {
            //CameraPosition cameraPosition = new CameraPosition.Builder(). featureCollection.bbox();
            BoundingBox boundingBox = featureCollection.bbox();

            if (boundingBox == null) {
                double[] bbox = TurfMeasurement.bbox(featureCollection);
                boundingBox = BoundingBox.fromLngLats(bbox[0], bbox[1], bbox[2], bbox[3]);
            }

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.from(boundingBox.north(), boundingBox.east(), boundingBox.south(), boundingBox.west()), 20));

            communityTransportersSource.setGeoJson(featureCollection);
        }
    }

    @Nullable
    private FeatureCollection loadCommunityTransporters() {

        return FeatureCollection.fromFeatures(getRespondersFeatureList());
    }

    private List<Feature> getRespondersFeatureList() {
        List<CommunityResponderModel> communityResponders = ChwApplication.getInstance().communityResponderRepository().readAllResponders();
        List<Feature> featureList = new ArrayList<>();
        for (CommunityResponderModel communityResponderModel : communityResponders) {
            try {
                featureList.add(getFeature(communityResponderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return featureList;
    }

    private com.mapbox.geojson.Feature getFeature(CommunityResponderModel communityResponderModel) throws JSONException {
        String[] latLong = communityResponderModel.getResponderLocation().split(" ");
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);
        com.cocoahero.android.geojson.Feature feature = new com.cocoahero.android.geojson.Feature();
        feature.setGeometry(new com.cocoahero.android.geojson.Point(latitude, longitude));
        JSONObject properties = new JSONObject();
        properties.put(CoreConstants.JsonAssets.RESPONDER_NAME, communityResponderModel.getResponderName());
        properties.put(CoreConstants.JsonAssets.RESPONDER_PHONE_NUMBER, communityResponderModel.getResponderPhoneNumber());
        feature.setProperties(properties);
        return Feature.fromJson(feature.toJSON().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (kujakuMapView != null) {
            kujakuMapView.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kujakuMapView != null)
            kujakuMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (kujakuMapView != null)
            kujakuMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (kujakuMapView != null)
            kujakuMapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (kujakuMapView != null)
            kujakuMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (kujakuMapView != null)
            kujakuMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kujakuMapView != null)
            kujakuMapView.onDestroy();
    }
}
