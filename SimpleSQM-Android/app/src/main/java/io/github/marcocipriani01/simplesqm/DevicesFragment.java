package io.github.marcocipriani01.simplesqm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.Locale;

public class DevicesFragment extends ListFragment {

    private final ArrayList<ListItem> listItems = new ArrayList<>();
    private ArrayAdapter<ListItem> listAdapter;
    private Context context;
    private LayoutInflater inflater;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listAdapter = new ArrayAdapter<>(getActivity(), 0, listItems) {
            @SuppressLint("SetTextI18n")
            @NonNull
            @Override
            public View getView(int position, View view, @NonNull ViewGroup parent) {
                ListItem item = listItems.get(position);
                if (view == null)
                    view = inflater.inflate(R.layout.device_list_item, parent, false);
                TextView text1 = view.findViewById(R.id.text1);
                if (item.driver == null) {
                    text1.setText(R.string.no_driver);
                } else if (item.driver.getPorts().size() == 1) {
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", ""));
                } else {
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", "") + ", " + item.port);
                }
                TextView text2 = view.findViewById(R.id.text2);
                text2.setText(String.format(Locale.US, getString(R.string.vendor_product), item.device.getVendorId(), item.device.getProductId()));
                return view;
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getString(R.string.no_devices_found));
        ((TextView) getListView().getEmptyView()).setTextSize(18);
        setListAdapter(listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_devices, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        listItems.clear();
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver == null) driver = usbCustomProber.probeDevice(device);
            if (driver != null) {
                for (int port = 0; port < driver.getPorts().size(); port++)
                    listItems.add(new ListItem(device, port, driver));
            } else {
                listItems.add(new ListItem(device, 0, null));
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
            UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
            listItems.clear();
            for (UsbDevice device : usbManager.getDeviceList().values()) {
                UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
                if (driver == null) {
                    driver = usbCustomProber.probeDevice(device);
                }
                if (driver != null) {
                    for (int port = 0; port < driver.getPorts().size(); port++)
                        listItems.add(new ListItem(device, port, driver));
                } else {
                    listItems.add(new ListItem(device, 0, null));
                }
            }
            listAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ListItem item = listItems.get(position);
        if (item.driver == null) {
            Toast.makeText(getActivity(), R.string.no_driver, Toast.LENGTH_SHORT).show();
        } else {
            Bundle args = new Bundle();
            args.putInt("device", item.device.getDeviceId());
            args.putInt("port", item.port);
            Fragment fragment = new SQMFragment();
            fragment.setArguments(args);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "sqm").addToBackStack(null).commit();
        }
    }

    static class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }
}