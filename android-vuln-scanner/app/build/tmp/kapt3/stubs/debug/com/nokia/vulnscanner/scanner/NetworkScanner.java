package com.nokia.vulnscanner.scanner;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000bH\u0002J\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/nokia/vulnscanner/scanner/NetworkScanner;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "checkProxySet", "", "checkVpnActive", "cm", "Landroid/net/ConnectivityManager;", "getWifiSecurityType", "", "wifiMgr", "Landroid/net/wifi/WifiManager;", "ssid", "scan", "Lcom/nokia/vulnscanner/data/models/NetworkSecurityResult;", "app_debug"})
public final class NetworkScanner {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    public NetworkScanner(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.NetworkSecurityResult scan() {
        return null;
    }
    
    private final boolean checkVpnActive(android.net.ConnectivityManager cm) {
        return false;
    }
    
    private final boolean checkProxySet() {
        return false;
    }
    
    private final java.lang.String getWifiSecurityType(android.net.wifi.WifiManager wifiMgr, java.lang.String ssid) {
        return null;
    }
}