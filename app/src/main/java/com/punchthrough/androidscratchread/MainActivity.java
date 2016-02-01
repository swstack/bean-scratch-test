package com.punchthrough.androidscratchread;

import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;



public class MainActivity extends AppCompatActivity {


    private class UpdateTask extends AsyncTask<TextView, String, Void> {

        private TextView tv;
        private String targetName;

        private void getBeanByName(final String name, final GetBeanListener gbl) {
            targetName = name;
            System.out.println("Attempting to get Bean: " + targetName);

            // hacky?
            runOnUiThread(new Runnable() {
                public void run() {
                    BeanManager.getInstance().startDiscovery(new BeanDiscoveryListener() {

                        private Boolean success;

                        @Override
                        public void onBeanDiscovered(Bean bean, int rssi) {
                            System.out.println("Discovered Bean: " + bean.getDevice().getName());
                            if (bean.getDevice().getName().equals(targetName)) {
                                System.out.println("Found the Bean!");
                                success = true;
                                gbl.success(bean);
                            }
                        }

                        @Override
                        public void onDiscoveryComplete() {
                            System.out.println("onDiscovery Complete!");
                            if (!success) {
                                gbl.fail();
                            }
                        }
                    });
                }
            });

        }

        @Override
        protected Void doInBackground(TextView... textViews) {
            tv = textViews[0];
            System.out.println(tv);
            System.out.println("Hereeee");

            GetBeanListener gbl = new GetBeanListener() {
                @Override
                public void success(Bean bean) {
                    String msg = "Found Bean: " + bean.getDevice().getName();
                    System.out.println(msg);
                    publishProgress(msg);
                }

                @Override
                public void fail() {
                    String msg = "Couldn't find Bean with name: " + targetName;
                    System.out.println(msg);
                    publishProgress(msg);
                }
            };

            getBeanByName("TESTBEAN", gbl);


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

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            System.out.println("onProgressUpdate");
            System.out.println(progress);
            tv.setText(progress[0]);
        }

        protected void onPostExecute() {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UpdateTask task = new UpdateTask();
        task.execute((TextView) findViewById(R.id.connectedState));

    }


}
