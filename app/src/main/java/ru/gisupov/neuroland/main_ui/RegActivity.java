package ru.gisupov.neuroland.main_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import ru.gisupov.neuroland.ClientServer;
import ru.gisupov.neuroland.MyResponse;
import ru.gisupov.neuroland.R;
import ru.gisupov.neuroland.MyRequestReg;


/**
 * Активность с возможностью регистрации пользователя
 */
public class RegActivity extends AppCompatActivity {

    public static String userLoginFromFile = "";
    public static String userPasswordFromFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        changeStatusBarColor();
    }

    /**
     * Меняет цвет строки состояния (строка уведомлений)
     */
    private void changeStatusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_app_color));
    }

    /**
     * Переход на Login активность при успешной регистрации
     * @throws InterruptedException исключение ошибки, связанной с ипользование другого потока
      * при взаимодействии с сервером
     */
    public void goToLoginAfterReg(View view) throws InterruptedException {
        EditText userName = (EditText) findViewById(R.id.BeginLogin);
        EditText userPassword1 = (EditText) findViewById(R.id.BeginPassword1);
        EditText userPassword2 = (EditText) findViewById(R.id.BeginPassword2);

        String login = userName.getText().toString();
        String pass1 = userPassword1.getText().toString();
        String pass2 = userPassword2.getText().toString();

        MyRequestReg myRequestReg = new MyRequestReg("register", new String[] {login, pass1, pass2});

        if (myRequestReg.checkNamePass() != 1) {
            Toast.makeText(getApplicationContext(), myRequestReg.checkNamePass(), Toast.LENGTH_SHORT).show();
            return;
        }

        ClientServer clientServer = new ClientServer();
        clientServer.makeRequest(myRequestReg);

        MyResponse myResponse = clientServer.getResponse();
        if (myResponse.data.equals("True")) {
            userLoginFromFile = login;
            userPasswordFromFile = pass1;

            Intent answer = new Intent(this, LoginActivity.class);
            startActivity(answer);
        }
    }
}