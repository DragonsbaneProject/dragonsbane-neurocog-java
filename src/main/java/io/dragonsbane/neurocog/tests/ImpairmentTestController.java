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

import java.util.Properties;

public abstract class ImpairmentTestController extends BaseController {

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

}
