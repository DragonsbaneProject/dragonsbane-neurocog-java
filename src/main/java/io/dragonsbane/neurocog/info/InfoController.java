package io.dragonsbane.neurocog.info;

import io.dragonsbane.core.BaseController;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class InfoController extends BaseController {

    public void view() {
        TextArea welcome = (TextArea)((VBox)((AnchorPane)root).getChildren().get(0)).getChildren().get(0);
        String text = "Welcome to Dragonbane's Neurocog!\nv0.2.0.\n";
        welcome.setText(text);
    }

}
