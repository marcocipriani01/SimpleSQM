package io.github.marcocipriani01.simplesqm;

import static io.github.marcocipriani01.simplesqm.Constants.SERVICE_PENDING_INTENT_FLAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hoho.android.usbserial.driver.SerialTimeoutException;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class SQMFragment extends Fragment implements ServiceConnection, SerialListener {

    private final static String NEWLINE_CRLF = "\r\n";
    private final static String NEWLINE_LF = "\n";
    private final BroadcastReceiver broadcastReceiver;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int deviceId, portNum;
    private UsbSerialPort usbSerialPort;
    private SerialService service;
    private TextView sqm;
    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private Context context;
    private boolean running = false;

    public SQMFragment() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constants.INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    Boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                    connect(granted);
                }
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //setRetainInstance(true);
        Bundle arguments = requireArguments();
        deviceId = arguments.getInt("device");
        portNum = arguments.getInt("port");
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        this.context.stopService(new Intent(this.context, SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (service != null)
            service.attach(this);
        else
            this.context.startService(new Intent(this.context, SerialService.class));
        running = true;
        handler.post(this::handlerLoop);
    }

    private void handlerLoop() {
        if (running) {
            if (connected == Connected.True) {
                try {
                    service.write((">" + NEWLINE_CRLF).getBytes());
                } catch (SerialTimeoutException e) {
                    Toast.makeText(context, R.string.write_timeout, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    onSerialIoError(e);
                }
            }
            handler.postDelayed(this::handlerLoop, 2000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (service != null) service.detach();
        running = false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.context.bindService(new Intent(this.context, SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try {
            this.context.unbindService(this);
        } catch (Exception ignored) {
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.context.registerReceiver(broadcastReceiver, new IntentFilter(Constants.INTENT_ACTION_GRANT_USB));
        if (initialStart && service != null) {
            initialStart = false;
            handler.post(this::connect);
        }
    }

    @Override
    public void onPause() {
        this.context.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart && isResumed()) {
            initialStart = false;
            handler.post(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    /*
     * UI
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sqm, container, false);
        sqm = view.findViewById(R.id.sqm_text);
        return view;
    }

    /*
     * Serial + UI
     */
    private void connect() {
        connect(null);
    }

    @SuppressLint("WrongConstant")
    private void connect(Boolean permissionGranted) {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values())
            if (v.getDeviceId() == deviceId)
                device = v;
        if (device == null) {
            sqm.setText(getString(R.string.device_not_found_err));
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            sqm.setText(getString(R.string.no_driver_err));
            return;
        }
        if (driver.getPorts().size() < portNum) {
            sqm.setText(getString(R.string.not_enough_port_err));
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if (usbConnection == null && permissionGranted == null && !usbManager.hasPermission(driver.getDevice())) {
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(Constants.INTENT_ACTION_GRANT_USB), SERVICE_PENDING_INTENT_FLAG);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if (usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice())) {
                sqm.setText(getString(R.string.permission_denied_err));
            } else {
                sqm.setText(getString(R.string.connectinon_failed_err));
            }
            return;
        }

        connected = Connected.Pending;
        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(115200, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            SerialSocket socket = new SerialSocket(this.context.getApplicationContext(), usbConnection, usbSerialPort);
            service.connect(socket);
            onSerialConnect();
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
        usbSerialPort = null;
    }

    @SuppressLint("SetTextI18n")
    private void receive(byte[] data) {
        String msg = new String(data);
        if (msg.length() > 0) {
            msg = msg.replace(NEWLINE_CRLF, NEWLINE_LF);
            sqm.setText("SQM " + msg.replace("<", ""));
        }
    }

    @Override
    public void onSerialConnect() {
        connected = Connected.True;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSerialConnectError(Exception e) {
        sqm.setText(getString(R.string.connection_failed_err) + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSerialIoError(Exception e) {
        sqm.setText(getString(R.string.connection_lost) + e.getMessage());
        disconnect();
    }

    private enum Connected {False, Pending, True}
}