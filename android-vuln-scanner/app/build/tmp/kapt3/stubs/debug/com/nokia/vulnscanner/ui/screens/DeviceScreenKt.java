package com.nokia.vulnscanner.ui.screens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0003\u001a(\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007\u001a\u0010\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0010\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0014H\u0007\u001a\u0018\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0003H\u0003\u00a8\u0006\u0017"}, d2 = {"CheckItem", "", "label", "", "ok", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "CveCard", "cveId", "description", "cvss", "", "severity", "Lcom/nokia/vulnscanner/data/models/Severity;", "DeviceScreen", "result", "Lcom/nokia/vulnscanner/data/models/DeviceSecurityResult;", "FindingCard", "finding", "Lcom/nokia/vulnscanner/data/models/SecurityFinding;", "InfoRow", "value", "app_debug"})
public final class DeviceScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void DeviceScreen(@org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.models.DeviceSecurityResult result) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CheckItem(java.lang.String label, boolean ok, androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FindingCard(@org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.models.SecurityFinding finding) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CveCard(@org.jetbrains.annotations.NotNull()
    java.lang.String cveId, @org.jetbrains.annotations.NotNull()
    java.lang.String description, double cvss, @org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.data.models.Severity severity) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InfoRow(java.lang.String label, java.lang.String value) {
    }
}