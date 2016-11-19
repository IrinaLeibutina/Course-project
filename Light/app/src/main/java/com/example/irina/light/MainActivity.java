package com.example.irina.light;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements View.OnClickListener {

    //Экземпляры классов наших кнопок
    ToggleButton redButton;
    ToggleButton greenButton;
    ToggleButton yellowButton;
    int hour = 0;
    int minute = 0;


    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket;

    //Эта функция запускается автоматически при запуске приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("Menu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //"Соединям" вид кнопки в окне приложения с реализацией
        redButton = (ToggleButton) findViewById(R.id.toggleRedLed);
        greenButton = (ToggleButton) findViewById(R.id.toggleGreenLed);
        yellowButton = (ToggleButton) findViewById(R.id.toggleYellowLed);

        //Добавлем "слушатель нажатий" к кнопке
        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        yellowButton.setOnClickListener(this);
        //Включаем bluetooth. Если он уже включен, то ничего не произойдет
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);

        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        //Пытаемся проделать эти действия
        try {
            //Устройство с данным адресом - наш Bluetooth Bee
            //Адрес опредеяется следующим образом: установите соединение
            //между ПК и модулем (пин: 1234), а затем посмотрите в настройках
            //соединения адрес модуля. Скорее всего он будет аналогичным.
            BluetoothDevice device = bluetooth.getRemoteDevice("20:15:11:23:16:00");

            //Инициируем соединение с устройством
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[]{int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();

            //В случае появления любых ошибок, выводим в лог сообщение
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }

        //Выводим сообщение об успешном подключении
        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.print("Menu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void Click(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        //String hour = data.getStringExtra("hour");
        hour = data.getIntExtra("hour", -1);
        minute = data.getIntExtra("minute", -1);
        //tvName.setText("Your name is " + name);
        Toast.makeText(this, hour + " " + minute, Toast.LENGTH_LONG).show();


    }
    //Как раз эта функция и будет вызываться

    @Override
    public void onClick(View v) {
        System.out.print("Menu");

        //Пытаемся послать данные
        try {
            //Получаем выходной поток для передачи данных
            OutputStream outStream = clientSocket.getOutputStream();

            int value = 0;

            //В зависимости от того, какая кнопка была нажата,
            //изменяем данные для посылки
            if (v == redButton) {
                value = (redButton.isChecked() ? 1 : 0) + 60;
            } else if (v == greenButton) {
                value = (greenButton.isChecked() ? 1 : 0) + 70;
            } else if (v == yellowButton) {
                value = (yellowButton.isChecked() ? 1 : 0) + 50;
            }

            //Пишем данные в выходной поток
            outStream.write(value);
            outStream.write(hour);
            outStream.write(minute);

        } catch (IOException e) {
            //Если есть ошибки, выводим их в лог
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}