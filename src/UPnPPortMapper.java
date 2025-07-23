import org.jupnp.DefaultUpnpServiceConfiguration;
import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.controlpoint.ActionCallback;
import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.LocalDevice;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.UDAServiceType;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.model.types.UnsignedIntegerTwoBytes;
import org.jupnp.registry.Registry;
import org.jupnp.registry.RegistryListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class UPnPPortMapper {

    private UpnpService upnpService;
    private ControlPoint controlPoint;
    private final Timer retryTimer = new Timer(true);
    private final int maxRetries;
    private final int retryDelayMs;
    private final String intIP;
    private RemoteService wanService;
    private final int port;

    public UPnPPortMapper(int maxRetries, int retryDelayMs,String intIP,int port) {
        this.maxRetries = maxRetries;
        this.retryDelayMs = retryDelayMs;
        this.intIP = intIP;
        this.port = port;
        start();
    }
    RegistryListener listener = new RegistryListener() {

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            System.out.println("Discovery started: " + device.getDisplayString());
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception e) {
            System.out.println("Discovery failed: " + device.getDisplayString() + " => " + e);
        }

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            System.out.println("Remote device available: " + device.getDisplayString());
        }

        @Override
        public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
            System.out.println("Remote device updated: " + device.getDisplayString());
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            System.out.println("Remote device removed: " + device.getDisplayString());
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            System.out.println("Local device added: " + device.getDisplayString());
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            System.out.println("Local device removed: " + device.getDisplayString());
        }

        @Override
        public void beforeShutdown(Registry registry) {
            System.out.println("Before shutdown, the registry has devices: "
                + registry.getDevices().size());
        }

        @Override
        public void afterShutdown() {
            System.out.println("Shutdown of registry complete!");
        }
    };
    public void start() {
        upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());
        upnpService.startup();
        upnpService.getRegistry().addListener(listener);
        controlPoint = upnpService.getControlPoint();
    }

    public void shutdown() {
        if (upnpService != null&& wanService != null) {
        	ActionInvocation<?> get = new ActionInvocation<>(wanService.getAction("GetSpecificPortMappingEntry"));
        	get.setInput("NewRemoteHost", "");
        	get.setInput("NewExternalPort", new UnsignedIntegerTwoBytes(port));
        	get.setInput("NewProtocol", "TCP");

        	upnpService.getControlPoint().execute(new ActionCallback(get) {
        	    @Override
        	    public void success(ActionInvocation invocation) {
        	        System.out.println("Mapping exists. Proceeding to delete.");
        	        deleteMapping(); // Your delete logic here
        	    }

        	    @Override
        	    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
        	        System.out.println("No existing mapping to delete. Skipping. Def msg: "+defaultMsg);
        	    }
        	});
            upnpService.shutdown();
            retryTimer.cancel();
        }
    }
    private void deleteMapping()
    {
    	ActionInvocation<?> invocation = new ActionInvocation<>(wanService.getAction("DeletePortMapping"));
        invocation.setInput("NewRemoteHost", ""); // Leave blank unless using remote IP filtering
        invocation.setInput("NewExternalPort", new UnsignedIntegerTwoBytes(port));
        invocation.setInput("NewProtocol", "TCP");

        upnpService.getControlPoint().execute(new ActionCallback(invocation) {

			@Override
			public void success(ActionInvocation invocation) {
				System.out.println("Port mapping successfully deleted.");
			}

			@Override
			public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
				System.err.println("Failed to delete port mapping: " + defaultMsg);
			}

        });
    }
    public void addPortMapping(PortMappingListener listener) {

        AtomicInteger attempt = new AtomicInteger(0);

        final Runnable[] tryAddMapping = new Runnable[1];
        tryAddMapping[0] = new Runnable() {
            @Override
            public void run() {
                attempt.incrementAndGet();

                wanService = findWANIPConnectionService();
                if (wanService == null) {
                    if (attempt.get() >= maxRetries) {
                        listener.onFailure("WANIPConnection service not found after " + attempt.get() + " attempts.");
                        return;
                    }
                    // retry later
                    retryTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                        	tryAddMapping[0].run();
                        }
                    }, retryDelayMs);
                    return;
                }

                ActionInvocation<?> invocation = new ActionInvocation<>(wanService.getAction("AddPortMapping"));
                invocation.setInput("NewRemoteHost", "");
                invocation.setInput("NewExternalPort", new UnsignedIntegerTwoBytes(port));
                invocation.setInput("NewProtocol", "TCP");
                invocation.setInput("NewInternalPort", new UnsignedIntegerTwoBytes(port));
                invocation.setInput("NewInternalClient", intIP);
                invocation.setInput("NewEnabled", true);
                invocation.setInput("NewPortMappingDescription", "Redlines");
                invocation.setInput("NewLeaseDuration",  new UnsignedIntegerFourBytes(0));

                controlPoint.execute(new ActionCallback(invocation) {

					@Override
					public void success(ActionInvocation invocation) {
						// TODO Auto-generated method stub
						listener.onSuccess();
					}

					@Override
					public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
						// TODO Auto-generated method stub
						if (attempt.get() >= maxRetries) {
                            listener.onFailure("Failed to add port mapping after " + attempt.get() + " attempts. Reason: " + defaultMsg);
                        } else {
                            // retry after delay
                            retryTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                	tryAddMapping[0].run();
                                }
                            }, retryDelayMs);
                        }
					}
                });
            }
        };

        tryAddMapping[0].run();
    }

    private RemoteService findWANIPConnectionService() {
        for (RemoteDevice device : upnpService.getRegistry().getRemoteDevices()) {
            RemoteService service = device.findService(new UDAServiceType("WANIPConnection"));
            if (service != null) {
                return service;
            }
        }
        return null;
    }
}
