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
    ToggleButton rgbButton;
    int hour = 0;
    int minute = 0;


    public int value_1 = 0;
    public int value_2 = 0;
    public int value_3 = 0;
    public int value_4 = 0;

    public int value_5 = 0;
    public int value_6 = 0;
    public int value_7 = 0;
    public int value_8 = 0;

    public int value_9 = 0;
    public int value_10 = 0;
    public int value_11 = 0;
    public int value_12 = 0;

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
        rgbButton = (ToggleButton) findViewById(R.id.toggleButton) ;

        //Добавлем "слушатель нажатий" к кнопке
        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        yellowButton.setOnClickListener(this);
        rgbButton.setOnClickListener(this);
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
                value_1 = (redButton.isChecked() ? 1 : 0) + 20;
                value_2 = (redButton.isChecked() ? 1 : 0) + 50;
                value_3 = (redButton.isChecked() ? 1 : 0) + 80;
                value_4 = (redButton.isChecked() ? 1 : 0) + 110;

            } else if (v == greenButton) {
                value_1 = (greenButton.isChecked() ? 1 : 0) + 30;
                value_2 = (greenButton.isChecked() ? 1 : 0) + 60;
                value_3 = (greenButton.isChecked() ? 1 : 0) + 90;
                value_4 = (greenButton.isChecked() ? 1 : 0) + 120;

            } else if (v == yellowButton) {
                value_1 = (yellowButton.isChecked() ? 1 : 0) + 40;
                value_2 = (yellowButton.isChecked() ? 1 : 0) + 70;
                value_3 = (yellowButton.isChecked() ? 1 : 0) + 100;
                value_4 = (yellowButton.isChecked() ? 1 : 0) + 130;

            } else if (v == rgbButton) {
               value_1 = (rgbButton.isChecked() ? 1 : 0) + 40;
               value_2 = (rgbButton.isChecked() ? 1 : 0) + 70;
               value_3 = (rgbButton.isChecked() ? 1 : 0) + 100;
               value_4 = (rgbButton.isChecked() ? 1 : 0) + 130;

                value_5 = (rgbButton.isChecked() ? 1 : 0) + 20;
                value_6 = (rgbButton.isChecked() ? 1 : 0) + 50;
                value_7 = (rgbButton.isChecked() ? 1 : 0) + 800;
                value_8 = (rgbButton.isChecked() ? 1 : 0) + 110;

               value_9 = (rgbButton.isChecked() ? 1 : 0) + 30;
               value_10 = (rgbButton.isChecked() ? 1 : 0) + 60;
               value_11 = (rgbButton.isChecked() ? 1 : 0) + 90;
               value_12 = (rgbButton.isChecked() ? 1 : 0) + 120;
            }

            //outStream.write(hour);
            //Пишем данные в выходной поток
            outStream.write(value_1);
            outStream.write(value_2);
            outStream.write(value_3);
            outStream.write(value_4);

            outStream.write(value_5);
            outStream.write(value_6);
            outStream.write(value_7);
            outStream.write(value_8);

            outStream.write(value_9);
            outStream.write(value_10);
            outStream.write(value_11);
            outStream.write(value_12);
            // outStream.write(minute);

        } catch (IOException e) {
            //Если есть ошибки, выводим их в лог
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}