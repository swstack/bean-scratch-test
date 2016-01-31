package com.punchthrough.androidscratchread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Bean myBean = getBeanByName("LED2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BeanListener beanListener = new BeanListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onConnectionFailed() {
            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onSerialMessageReceived(byte[] data) {
            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {
            }

            @Override
            public void onError(BeanError error) {
            }
        };


    }

    private Bean getBeanByName(String name) throws Exception {
        final CountDownLatch beanLatch = new CountDownLatch(1);
        final List<Bean> beans = new ArrayList<>();

        final String targetName = name;

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                if (bean.getDevice().getName().equals(targetName)) {
                    System.out.println("Found Bean!!!!!!!");
                    beans.add(bean);
                    beanLatch.countDown();
                }
            }

            @Override
            public void onDiscoveryComplete() {
                System.out.println("Nothing");
                beanLatch.countDown();
            }
        };

        BeanManager.getInstance().startDiscovery(listener);
        beanLatch.await(60, TimeUnit.SECONDS);
        if (beans.isEmpty()) {
            throw new Exception("No bean");
        }
        return beans.get(0);
    }
}
