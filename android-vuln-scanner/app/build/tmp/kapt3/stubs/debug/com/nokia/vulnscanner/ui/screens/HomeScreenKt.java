package com.nokia.vulnscanner.ui.screens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a2\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a6\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a$\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a\u0010\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0015H\u0003\u001a\b\u0010\u0017\u001a\u00020\u0001H\u0003\u001a\u0018\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u001aH\u0003\u001a2\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020!H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\"\u0010#\u001a\u0010\u0010$\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0015H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006%"}, d2 = {"ErrorCard", "", "message", "", "HomeScreen", "uiState", "Lcom/nokia/vulnscanner/viewmodel/ScanUiState;", "onStartScan", "Lkotlin/Function0;", "onNavigate", "Lkotlin/Function1;", "NavCard", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "title", "subtitle", "severity", "Lcom/nokia/vulnscanner/data/models/Severity;", "onClick", "NavigationCards", "summary", "Lcom/nokia/vulnscanner/data/models/ScanSummary;", "OverallScoreCard", "PlaceholderCard", "ScanningCard", "progress", "", "StatChip", "modifier", "Landroidx/compose/ui/Modifier;", "value", "label", "color", "Landroidx/compose/ui/graphics/Color;", "StatChip-g2O1Hgs", "(Landroidx/compose/ui/Modifier;Ljava/lang/String;Ljava/lang/String;J)V", "SummaryStatsRow", "app_debug"})
public final class HomeScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    com.nokia.vulnscanner.viewmodel.ScanUiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStartScan, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigate) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScanningCard(java.lang.String message, float progress) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OverallScoreCard(com.nokia.vulnscanner.data.models.ScanSummary summary) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SummaryStatsRow(com.nokia.vulnscanner.data.models.ScanSummary summary) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NavigationCards(com.nokia.vulnscanner.data.models.ScanSummary summary, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigate) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NavCard(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String title, java.lang.String subtitle, com.nokia.vulnscanner.data.models.Severity severity, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PlaceholderCard() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ErrorCard(java.lang.String message) {
    }
}