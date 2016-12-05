package com.sohanchy.matrix.encryption;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainWindowController {

    @FXML
    private TextArea msgTextArea;
    @FXML
    private TextArea encMsgTextArea;
    @FXML
    private TextArea MatrixTextArea;
    @FXML
    private TextField dimTextField;

    public MainWindowController() {
        System.out.println("Created.");


    }

    @FXML
    private void onEncryptButton() {
        if (dimTextField.getText().isEmpty()) {
            MatrixEncrypt.showWarning("Enter Dimension.", "Enter a single number for matrix dimension please.");
        } else {
            double dim = Double.parseDouble(dimTextField.getText());
            String res = MatrixEncrypt.encrypt(msgTextArea.getText(), MatrixTextArea.getText(), dim);

            encMsgTextArea.setText(res);
        }
    }

    @FXML
    private void onDecryptButton() {
        if (dimTextField.getText().isEmpty()) {
            MatrixEncrypt.showWarning("Enter Dimension.", "Enter a single number for matrix dimension please.");
        } else {
            double dim = Double.parseDouble(dimTextField.getText());
            String res = MatrixEncrypt.decrypt(encMsgTextArea.getText(), MatrixTextArea.getText(), dim);

            msgTextArea.setText(res);
        }
    }


}
