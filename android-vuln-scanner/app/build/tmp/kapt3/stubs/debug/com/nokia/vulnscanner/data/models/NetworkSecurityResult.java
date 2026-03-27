package com.nokia.vulnscanner.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003JU\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00032\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u00072\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000f\u00a8\u0006#"}, d2 = {"Lcom/nokia/vulnscanner/data/models/NetworkSecurityResult;", "", "ssid", "", "bssid", "securityType", "isVpnActive", "", "isProxySet", "tlsVersion", "findings", "", "Lcom/nokia/vulnscanner/data/models/SecurityFinding;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZLjava/lang/String;Ljava/util/List;)V", "getBssid", "()Ljava/lang/String;", "getFindings", "()Ljava/util/List;", "()Z", "getSecurityType", "getSsid", "getTlsVersion", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class NetworkSecurityResult {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String ssid = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String bssid = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String securityType = null;
    private final boolean isVpnActive = false;
    private final boolean isProxySet = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tlsVersion = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> findings = null;
    
    public NetworkSecurityResult(@org.jetbrains.annotations.NotNull()
    java.lang.String ssid, @org.jetbrains.annotations.NotNull()
    java.lang.String bssid, @org.jetbrains.annotations.NotNull()
    java.lang.String securityType, boolean isVpnActive, boolean isProxySet, @org.jetbrains.annotations.NotNull()
    java.lang.String tlsVersion, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> findings) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSsid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBssid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSecurityType() {
        return null;
    }
    
    public final boolean isVpnActive() {
        return false;
    }
    
    public final boolean isProxySet() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTlsVersion() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> getFindings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.NetworkSecurityResult copy(@org.jetbrains.annotations.NotNull()
    java.lang.String ssid, @org.jetbrains.annotations.NotNull()
    java.lang.String bssid, @org.jetbrains.annotations.NotNull()
    java.lang.String securityType, boolean isVpnActive, boolean isProxySet, @org.jetbrains.annotations.NotNull()
    java.lang.String tlsVersion, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> findings) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}