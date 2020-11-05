package com.maiwavo.simpleobjectplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    //private String MODEL_URL = "https://modelviewer.dev/shared-assets/models/Astronaut.glb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!CameraARPermissionHelper.hasCameraPermission(this)) {
            CameraARPermissionHelper.requestCameraPermission(this);
            return;
        }
        if (!CameraARPermissionHelper.hasARCoreServicesPermission(this)) {
            return;
        }

        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        setUpModel();
        setUpPlane();
    }

    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        });
    }

    private void createModel(AnchorNode anchorNode) {
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }

    private void setUpModel() {
        ModelRenderable.builder().setSource(this, R.raw.model)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(MainActivity.this, "Error in loding", Toast.LENGTH_LONG).show();
                    return null;
                });

/*
        RenderableSource.Builder sourceBuilder = RenderableSource.builder();
        sourceBuilder.setSource(this, Uri.parse(MODEL_URL), RenderableSource.SourceType.GLB);
        //builder.setScale(0.8f);
        RenderableSource renderableSource = sourceBuilder.setRecenterMode(RenderableSource.RecenterMode.ROOT).build();

        ModelRenderable.Builder modelBuilder = ModelRenderable.builder().setSource(this, renderableSource);
        modelBuilder.setRegistryId(MODEL_URL).build().thenAccept(renderable -> modelRenderable = renderable).exceptionally(throwable -> {
            Toast.makeText(MainActivity.this, "Error in loding", Toast.LENGTH_LONG).show();
            return null;
        });

 */
    }
}