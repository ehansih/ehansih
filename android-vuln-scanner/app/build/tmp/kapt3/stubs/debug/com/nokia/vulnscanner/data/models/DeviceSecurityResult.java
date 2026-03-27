package com.nokia.vulnscanner.data.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001d\b\u0087\b\u0018\u00002\u00020\u0001By\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\u0006\u0012\u0006\u0010\r\u001a\u00020\u0006\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u00a2\u0006\u0002\u0010\u0015J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00c6\u0003J\t\u0010\"\u001a\u00020\u0012H\u00c6\u0003J\u000f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00140\u000fH\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0006H\u00c6\u0003J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\t\u0010(\u001a\u00020\u0006H\u00c6\u0003J\t\u0010)\u001a\u00020\u0006H\u00c6\u0003J\t\u0010*\u001a\u00020\u0006H\u00c6\u0003J\t\u0010+\u001a\u00020\u0006H\u00c6\u0003J\u0097\u0001\u0010,\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u00062\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000fH\u00c6\u0001J\u0013\u0010-\u001a\u00020\u00062\b\u0010.\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010/\u001a\u00020\u0012H\u00d6\u0001J\t\u00100\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u001aR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001aR\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u001aR\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u001aR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u001aR\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u001aR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0017R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00061"}, d2 = {"Lcom/nokia/vulnscanner/data/models/DeviceSecurityResult;", "", "androidVersion", "", "securityPatchLevel", "isRooted", "", "isDeveloperModeEnabled", "isUsbDebuggingEnabled", "isUnknownSourcesAllowed", "isScreenLockEnabled", "isDeviceEncrypted", "isBiometricEnabled", "isVerifyAppsEnabled", "osVulnerabilities", "", "Lcom/nokia/vulnscanner/data/models/CveRecord;", "securityScore", "", "findings", "Lcom/nokia/vulnscanner/data/models/SecurityFinding;", "(Ljava/lang/String;Ljava/lang/String;ZZZZZZZZLjava/util/List;ILjava/util/List;)V", "getAndroidVersion", "()Ljava/lang/String;", "getFindings", "()Ljava/util/List;", "()Z", "getOsVulnerabilities", "getSecurityPatchLevel", "getSecurityScore", "()I", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class DeviceSecurityResult {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String androidVersion = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String securityPatchLevel = null;
    private final boolean isRooted = false;
    private final boolean isDeveloperModeEnabled = false;
    private final boolean isUsbDebuggingEnabled = false;
    private final boolean isUnknownSourcesAllowed = false;
    private final boolean isScreenLockEnabled = false;
    private final boolean isDeviceEncrypted = false;
    private final boolean isBiometricEnabled = false;
    private final boolean isVerifyAppsEnabled = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nokia.vulnscanner.data.models.CveRecord> osVulnerabilities = null;
    private final int securityScore = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> findings = null;
    
    public DeviceSecurityResult(@org.jetbrains.annotations.NotNull()
    java.lang.String androidVersion, @org.jetbrains.annotations.NotNull()
    java.lang.String securityPatchLevel, boolean isRooted, boolean isDeveloperModeEnabled, boolean isUsbDebuggingEnabled, boolean isUnknownSourcesAllowed, boolean isScreenLockEnabled, boolean isDeviceEncrypted, boolean isBiometricEnabled, boolean isVerifyAppsEnabled, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.models.CveRecord> osVulnerabilities, int securityScore, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> findings) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAndroidVersion() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSecurityPatchLevel() {
        return null;
    }
    
    public final boolean isRooted() {
        return false;
    }
    
    public final boolean isDeveloperModeEnabled() {
        return false;
    }
    
    public final boolean isUsbDebuggingEnabled() {
        return false;
    }
    
    public final boolean isUnknownSourcesAllowed() {
        return false;
    }
    
    public final boolean isScreenLockEnabled() {
        return false;
    }
    
    public final boolean isDeviceEncrypted() {
        return false;
    }
    
    public final boolean isBiometricEnabled() {
        return false;
    }
    
    public final boolean isVerifyAppsEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.CveRecord> getOsVulnerabilities() {
        return null;
    }
    
    public final int getSecurityScore() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> getFindings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.CveRecord> component11() {
        return null;
    }
    
    public final int component12() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nokia.vulnscanner.data.models.SecurityFinding> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nokia.vulnscanner.data.models.DeviceSecurityResult copy(@org.jetbrains.annotations.NotNull()
    java.lang.String androidVersion, @org.jetbrains.annotations.NotNull()
    java.lang.String securityPatchLevel, boolean isRooted, boolean isDeveloperModeEnabled, boolean isUsbDebuggingEnabled, boolean isUnknownSourcesAllowed, boolean isScreenLockEnabled, boolean isDeviceEncrypted, boolean isBiometricEnabled, boolean isVerifyAppsEnabled, @org.jetbrains.annotations.NotNull()
    java.util.List<com.nokia.vulnscanner.data.models.CveRecord> osVulnerabilities, int securityScore, @org.jetbrains.annotations.NotNull()
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