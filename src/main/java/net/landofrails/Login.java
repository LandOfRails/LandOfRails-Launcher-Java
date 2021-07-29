package net.landofrails;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.landofrails.minecraft.Minecraft;

public class Login {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private CheckBox remember;

    @FXML
    private Label inputwasincorrect;

    @FXML
    protected void handleLoginButton(ActionEvent event) {
        boolean correct = Minecraft.getInstance().login(email.getText(), password.getText());
        if (!correct)
            inputwasincorrect.setVisible(true);
    }

}
