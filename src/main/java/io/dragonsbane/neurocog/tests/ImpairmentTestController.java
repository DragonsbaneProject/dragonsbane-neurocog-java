package io.dragonsbane.neurocog.tests;

import io.dragonsbane.core.BaseController;
import io.dragonsbane.neurocog.NeurocogDApp;
import io.dragonsbane.neurocog.NeurocogService;
import io.onemfive.core.infovault.InfoVaultService;
import io.onemfive.data.DID;
import io.onemfive.data.Envelope;
import io.onemfive.data.util.DLC;
import javafx.animation.Animation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class ImpairmentTestController extends BaseController {

    protected static final double fromMiddleFromXScale = 0.0;
    protected static final double fromMiddleFromYScale = 1.0;
    protected static final double fromMiddleToXScale = 1.0;
    protected static final double fromMiddleToYScale = 1.0;
    protected static final double fromMiddlePivotX = 0.5;
    protected static final double fromMiddlePivotY = 0.5;
    protected static final long fromMiddleDuration = 250;

    protected static final double toMiddleFromXScale = 1.0;
    protected static final double toMiddleFromYScale = 1.0;
    protected static final double toMiddleToXScale = 0.0;
    protected static final double toMiddleToYScale = 1.0;
    protected static final double toMiddlePivotX = 0.5;
    protected static final double toMiddlePivotY = 0.5;
    protected static final long toMiddleDuration = 250;

    protected static String[] tileNames = {
            "card_c2","card_c3","card_c4","card_c5","card_c6","card_c7","card_c8","card_c9","card_c10","card_cj","card_cq","card_ck","card_ca",
            "card_d2","card_d3","card_d4","card_d5","card_d6","card_d7","card_d8","card_d9","card_d10","card_dj","card_dq","card_dk","card_da",
            "card_h2","card_h3","card_h4","card_h5","card_h6","card_h7","card_h8","card_h9","card_h10","card_hj","card_hq","card_hk","card_ha",
            "card_s2","card_s3","card_s4","card_s5","card_s6","card_s7","card_s8","card_s9","card_s10","card_sj","card_sq","card_sk","card_sa"
    };
    protected static Map<String,Image> tiles = new HashMap<>();

    protected ImpairmentTest impairmentTest;
    protected Animation animation1;
    protected Animation animation2;

    protected Long begin;
    protected Long end;

    protected NeurocogDApp dApp;
    protected DID did;
    protected Double bac = 0.0;
    protected Boolean baseline = false;
    protected long normalFlipDurationMs = 3 * 1000;
    protected long shortFlipDuractionMs = 1000;

    public Boolean init(NeurocogDApp neurocogDApp, Parent root, Scene scene, Properties p) {
        super.init(neurocogDApp, root, scene, p);
        this.dApp = neurocogDApp;
        Boolean success = super.init(neurocogDApp, root, scene, p);
        if(success) {
            impairmentTest = new ImpairmentTest();
            did = dApp.getUserDID();
            if(dApp.getBac() != null)
                bac = dApp.getBac();
            if(dApp.getBaseline() != null)
                baseline = dApp.getBaseline();
            loadImages();
//            animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
//            animation1.setAnimationListener(this);
//            animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
//            animation2.setAnimationListener(this);
        }
        return success;
    }

    protected void testFinished() {
        Envelope e = Envelope.documentFactory();
        SaveTestRequest req = new SaveTestRequest();
        req.test = impairmentTest;
        DLC.addData(SaveTestRequest.class, req, e);
        DLC.addRoute(NeurocogService.class, NeurocogService.OPERATION_SAVE_TEST, e);
        dApp.send(e);
        dApp.addTest(impairmentTest);
    }

    void loadImages() {
        if(tiles.size() == 0) {
            for(String n : tileNames) {
                tiles.put(n,new Image("io/dragonsbane/neurocog/tests/images/"+n+".png"));
            }
        }
    }

}
