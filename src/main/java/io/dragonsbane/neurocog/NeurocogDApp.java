package io.dragonsbane.neurocog;

import io.dragonsbane.neurocog.home.HomeController;
import io.dragonsbane.neurocog.info.InfoController;
import io.dragonsbane.neurocog.settings.SettingsController;
import io.dragonsbane.neurocog.tests.ImpairmentTest;
import io.onemfive.core.OneMFiveAppContext;
import io.onemfive.core.admin.AdminService;
import io.onemfive.core.client.ClientStatusListener;
import io.onemfive.data.*;
import io.onemfive.data.health.HealthRecord;
import io.onemfive.data.util.DLC;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class NeurocogDApp extends io.dragonsbane.core.DApp implements ClientStatusListener {

    private static Logger LOG = Logger.getLogger(NeurocogDApp.class.getName());

    private HealthRecord healthRecord;
    private Double bac = 0.0D;
    private Boolean baseline = false;
    private List<ImpairmentTest> tests = new ArrayList<>();

    // UI - Menu
    private Stage primaryStage;

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public Double getBac() {
        return bac;
    }

    public void setBac(Double bac) {
        this.bac = bac;
    }

    public Boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(Boolean baseline) {
        this.baseline = baseline;
    }

    public void addTest(ImpairmentTest test) {
        tests.add(test);
    }

    public List<ImpairmentTest> getTests() {
        return tests;
    }

    @Override
    public void init() throws Exception {
        super.init();

        // Directories
        String appsDir = config.getProperty("apps.dir.base");
        if(appsDir == null) throw new Exception("apps.dir.base property not present.");

        String neurocogDir =  appsDir + "/neurocog";
        File neurocogFolder = new File(neurocogDir);
        if(!neurocogFolder.exists())
            if(!neurocogFolder.mkdir())
                throw new Exception("Unable to create Dragonsbane Neurocog directory: "+neurocogDir);
        config.setProperty("root.dir.base",neurocogDir);
        LOG.info("Dragonsbane Neurocog Root Directory: "+neurocogDir);

        // UI
        LOG.info("Loading UI components...");
        FXMLLoader loader;
        Parent root;
        Scene scene;

        // UI - Home
        loader = new FXMLLoader(getClass().getResource("home/home.fxml"));
        root = loader.load();
        scene = new Scene(root, 700, 500);
        HomeController homeController = loader.getController();
        homeController.init(this, root, scene, config);
        controllers.put(HomeController.class.getName(),homeController);
        if(soloApp)
            super.homeController = homeController;

        SplitPane splitPane = (SplitPane)((AnchorPane)root).getChildren().get(0);
        ObservableList<Node> nodes = splitPane.getItems();
        for(Node n : nodes) {
            if("body".equals(n.getId())) {
                ObservableList<Node> bodyNodes = ((AnchorPane) n).getChildren();
                for (Node bn : bodyNodes) {
                    if (bn instanceof TabPane) {
                        ObservableList<Tab> tabs = ((TabPane) bn).getTabs();
                        for (Tab tab : tabs) {
                            switch (tab.getText()) {
                                case "Info": {
                                    loader = new FXMLLoader(getClass().getResource("info/info.fxml"));
                                    root = loader.load();
                                    tab.setContent(root);
                                    InfoController infoController = loader.getController();
                                    infoController.init(this, root, scene, config);
                                    controllers.put(InfoController.class.getName(),infoController);
                                    break;
                                }
                                case "Settings": {
                                    loader = new FXMLLoader(getClass().getResource("settings/settings.fxml"));
                                    root = loader.load();
                                    tab.setContent(root);
                                    SettingsController settingsController = loader.getController();
                                    settingsController.init(this, root, scene, config);
                                    controllers.put(SettingsController.class.getName(),settingsController);
                                    break;
                                }
                                default: {
                                    LOG.warning("Tab not supported: " + tab.getText());
                                }
                            }
                        }
                    }
                }
            }
        }

        // Getting ClientAppManager starts 1M5 Bus
        LOG.info("Starting 1M5 Bus...");
        oneMFiveAppContext = OneMFiveAppContext.getInstance(config);
        clientAppManager = oneMFiveAppContext.getClientAppManager();
        client = clientAppManager.getClient(true);

        serviceRegistrationExceptionsCallback = new ServiceCallback() {
            @Override
            public void reply(Envelope envelope) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        List<Exception> exceptions = DLC.getExceptions(envelope);
                        if(exceptions != null && exceptions.size() > 0) {
                            // signal to shutdown as exceptions occurred during service registration
                            for(Exception ex : exceptions) {
                                LOG.severe(ex.getLocalizedMessage());
                            }
                            shutdownCode = -1;
                        }
                    }
                });
            }
        };

        // register Neurocog service
        Envelope e1 = Envelope.documentFactory();
        List<Class> services = new ArrayList<>();
        services.add(NeurocogService.class);
        DLC.addEntity(services,e1);
        DLC.addRoute(AdminService.class, AdminService.OPERATION_REGISTER_SERVICES,e1);
        LOG.info("Registering Neurocog service with bus...");
        send(e1, serviceRegistrationExceptionsCallback);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        super.start(primaryStage);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Neurocog");
        homeController.view();
        LOG.info("Running");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
