package com.drawoverapps;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.bridge.Promise;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.facebook.react.ReactRootView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;

public class DrawOverAppsViewManager extends ReactContextBaseJavaModule {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1;

    private LinearLayout overlayView;
    private WindowManager windowManager;
    private ReactApplicationContext reactContext;

    public DrawOverAppsViewManager(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "DrawOverAppsView";
    }

    @ReactMethod
    public void askForPermission(Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            promise.reject("Activity doesn't exist", "Activity doesn't exist. Cannot ask for permission.");
            return;
        }

        // Check if the application already has the permission to draw overlays
        if (!Settings.canDrawOverlays(activity)) {
            // If not, form up an intent to launch the permission request
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            // Launch the intent with a request code
            activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            promise.resolve(false);
        } else {
            // Permission is already granted
            promise.resolve(true);
        }
    }



    @ReactMethod
    public void createOverlay(final String componentName) {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            try {
                overlayView = new LinearLayout(activity);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.TOP | Gravity.START;
                windowManager = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
                windowManager.addView(overlayView, params);

                // Create a ReactRootView and start the React application
                ReactRootView reactRootView = new ReactRootView(activity);
                ReactInstanceManager reactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(activity.getApplication())
                    .setBundleAssetName("index.android.bundle")
                    .setJSMainModulePath("index")
                    .addPackage(new MainReactPackage())
                    // Add any other packages here
                    .setUseDeveloperSupport(BuildConfig.DEBUG)
                    .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                    .build();

                // Start the React application with the specified component
                reactRootView.startReactApplication(reactInstanceManager, componentName, null);
                overlayView.addView(reactRootView);
               } catch (IllegalViewOperationException e) {
                    // Handle exception
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @ReactMethod
    public void removeOverlay() {
        final Activity activity = getCurrentActivity();
        if (activity == null || overlayView == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(overlayView);
                overlayView = null;
            }
        });
    }
}