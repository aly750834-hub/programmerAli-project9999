package com.example
import kotlinx.coroutines.launch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    QabasStudioScreen()
                }
            }
        }
    }
}

enum class AppState {
    INPUT, PROCESSING, RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QabasStudioScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("qabas_prefs", Context.MODE_PRIVATE) }

    var selectedRatio by remember { mutableStateOf(prefs.getString("ratio", "9:16") ?: "9:16") }
    var inputText by remember { mutableStateOf(prefs.getString("input", "") ?: "") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedInputType by remember { mutableStateOf(prefs.getString("inputType", null)) }
    var attachedFileName by remember { mutableStateOf(prefs.getString("fileName", null)) }
    var attachedFileIcon by remember { mutableStateOf<ImageVector?>(null) }
    var appState by remember { mutableStateOf(AppState.INPUT) }
    var showChatSheet by remember { mutableStateOf(false) }
    val chatSheetState = rememberModalBottomSheetState()
    var videoDuration by remember { mutableStateOf(prefs.getString("duration", "30 ثانية") ?: "30 ثانية") }
    var editingStyle by remember { mutableStateOf(prefs.getString("style", "أسلوب 3nvus / نيون داكن") ?: "أسلوب 3nvus / نيون داكن") }
    var voiceOver by remember { mutableStateOf(prefs.getString("voice", "عميق") ?: "عميق") }
    var musicVibe by remember { mutableStateOf(prefs.getString("music", "ملحمية") ?: "ملحمية") }

    LaunchedEffect(selectedInputType) {
        attachedFileIcon = when (selectedInputType) {
            "فيديو" -> Icons.Default.Movie
            "صورة" -> Icons.Default.Image
            "المستندات والكتب" -> Icons.Default.Description
            "صوت" -> Icons.Default.Audiotrack
            else -> null
        }
    }

    LaunchedEffect(selectedRatio, inputText, selectedInputType, attachedFileName, videoDuration, editingStyle, voiceOver, musicVibe) {
        prefs.edit()
            .putString("ratio", selectedRatio)
            .putString("input", inputText)
            .putString("inputType", selectedInputType)
            .putString("fileName", attachedFileName)
            .putString("duration", videoDuration)
            .putString("style", editingStyle)
            .putString("voice", voiceOver)
            .putString("music", musicVibe)
            .apply()
    }

    val goldGradient = Brush.horizontalGradient(
        colors = listOf(GoldSecondary, GoldPrimary)
    )

    when (appState) {
        AppState.PROCESSING -> {
            ProcessingScreen(
                onProcessingComplete = { appState = AppState.RESULT }
            )
        }
        AppState.RESULT -> {
            ResultScreen(
                goldGradient = goldGradient,
                videoDuration = videoDuration,
                editingStyle = editingStyle,
                selectedRatio = selectedRatio,
                onBack = { appState = AppState.INPUT }
            )
        }
        AppState.INPUT -> {
            Scaffold(

        containerColor = DeepSlate,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "استوديو قبس",
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepSlate
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Input Section
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "الفكرة أو النص",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Box {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        placeholder = { 
                            Text("أدخل فكرتك الإبداعية أو النص هنا...", color = TextSecondary)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = CardSurface,
                            unfocusedContainerColor = CardSurface,
                            focusedIndicatorColor = GoldPrimary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = GoldPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    
                    // Inline AI Enhance Button
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B))
                            .clickable { inputText = "هل تساءلت يوماً عن قيمة الثانية؟ [مؤثر صوتي لدقات ساعة] في كل لحظة تمر، هناك فرصة تتشكل. لا تنتظر اللحظة المثالية، بل اصنعها." }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "✨ تحسين وإعادة صياغة الفكرة",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            // Media Attachment Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardSurface)
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    .clickable { showBottomSheet = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "إضافة وسائط",
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedInputType == null) "إضافة وسائط ومرفقات (اختياري)" else "تم إرفاق: $selectedInputType",
                    color = GoldPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Production Settings Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF161E2E)) // Slightly lighter than DeepSlate
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚙️",
                        fontSize = 18.sp
                    )
                    Text(
                        text = "إعدادات الإنتاج",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Aspect Ratio Selector
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "أبعاد الفيديو",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RatioButton(
                            text = "9:16 (عمودي / ريلز)",
                            isSelected = selectedRatio == "9:16",
                            gradient = goldGradient,
                            modifier = Modifier.weight(1f)
                        ) { selectedRatio = "9:16" }
                        
                        RatioButton(
                            text = "16:9 (أفقي)",
                            isSelected = selectedRatio == "16:9",
                            gradient = goldGradient,
                            modifier = Modifier.weight(1f)
                        ) { selectedRatio = "16:9" }
                    }
                }

                // Duration Selector
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "مدة الفيديو",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("15 ثانية", "30 ثانية", "60 ثانية").forEach { duration ->
                            ChoiceChip(
                                text = duration,
                                isSelected = videoDuration == duration,
                                onClick = { videoDuration = duration },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Style Selector
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "أسلوب المونتاج والإخراج",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    listOf(
                        "أسلوب 3nvus / نيون داكن" to "تباين عالي، نصوص مضيئة، مؤثرات صوتية محيطية",
                        "سينمائي فاخر" to "ألوان دافئة، حركة بطيئة، انتقالات ناعمة",
                        "وثائقي حديث" to "لقطات سريعة، نصوص ديناميكية، طابع عصري"
                    ).forEach { (style, desc) ->
                        StyleCard(
                            title = style,
                            description = desc,
                            isSelected = editingStyle == style,
                            onClick = { editingStyle = style }
                        )
                    }
                }

                // Voice Over Selector
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "التعليق الصوتي",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("عميق", "حماسي", "رسمي").forEach { tone ->
                            ChoiceChip(
                                text = tone,
                                isSelected = voiceOver == tone,
                                onClick = { voiceOver = tone }
                            )
                        }
                    }
                }
                
                // Music Selector
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "الموسيقى",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("ملحمية", "هادئة", "غامضة").forEach { vibe ->
                            ChoiceChip(
                                text = vibe,
                                isSelected = musicVibe == vibe,
                                onClick = { musicVibe = vibe }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Call to Action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(
                        elevation = 16.dp, 
                        shape = RoundedCornerShape(16.dp),
                        spotColor = GoldPrimary,
                        ambientColor = GoldPrimary
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(goldGradient)
                    .clickable { 
                        val masterPrompt = buildCinematicPrompt(inputText, videoDuration, editingStyle, selectedRatio, voiceOver, musicVibe)
                        android.util.Log.d("QabasStudio", masterPrompt)
                        appState = AppState.PROCESSING 
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "بدء المعالجة الذهبية",
                    color = DeepSlate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = DeepSlate,
            dragHandle = { BottomSheetDefaults.DragHandle(color = TextSecondary) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "اختر نوع الوسائط",
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                MediaOption(icon = Icons.Default.Movie, label = "رفع فيديو") {
                    selectedInputType = "فيديو"
                    attachedFileName = "selected_video.mp4"
                    attachedFileIcon = Icons.Default.Movie
                    showBottomSheet = false
                }
                MediaOption(icon = Icons.Default.Image, label = "رفع صورة") {
                    selectedInputType = "صورة"
                    attachedFileName = "selected_image.png"
                    attachedFileIcon = Icons.Default.Image
                    showBottomSheet = false
                }
                MediaOption(icon = Icons.Default.Description, label = "مستندات / كتب") {
                    selectedInputType = "المستندات والكتب"
                    attachedFileName = "script_document.pdf"
                    attachedFileIcon = Icons.Default.Description
                    showBottomSheet = false
                }
                MediaOption(icon = Icons.Default.Audiotrack, label = "استخراج / إضافة صوت") {
                    selectedInputType = "صوت"
                    attachedFileName = "extracted_audio.mp3"
                    attachedFileIcon = Icons.Default.Audiotrack
                    showBottomSheet = false
                }
            }
        }
    }
        }
    }
}

@Composable
fun RatioButton(
    text: String,
    isSelected: Boolean,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundModifier = if (isSelected) {
        Modifier.background(gradient)
    } else {
        Modifier
            .background(CardSurface)
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(backgroundModifier)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) DeepSlate else TextPrimary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MediaOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurface)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = GoldPrimary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessingScreen(
    onProcessingComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onProcessingComplete()
    }

    Scaffold(
        containerColor = DeepSlate
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CircularProgressIndicator(
                    color = GoldPrimary,
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp,
                    trackColor = CardSurface
                )
                Text(
                    text = "جاري توليد الفيديو...",
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    goldGradient: Brush,
    videoDuration: String,
    editingStyle: String,
    selectedRatio: String,
    onBack: () -> Unit
) {
    var selectedVariant by remember { mutableStateOf("نسخة 1") }
    var showFeedbackSheet by remember { mutableStateOf(false) }
    var selectedFeedback by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    
    val maxSeconds = remember(videoDuration) { videoDuration.filter { it.isDigit() }.toIntOrNull() ?: 30 }
    var currentSeconds by remember { mutableStateOf(maxSeconds) }
    var isTimerComplete by remember { mutableStateOf(false) }
    
    LaunchedEffect(maxSeconds) {
        currentSeconds = maxSeconds
        isTimerComplete = false
        while (currentSeconds > 0) {
            kotlinx.coroutines.delay(1000L)
            currentSeconds--
        }
        isTimerComplete = true
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(isTimerComplete) {
        if (isTimerComplete) {
            snackbarHostState.showSnackbar("اكتملت المعالجة الوهمية بنجاح")
        }
    }

    if (showFeedbackSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFeedbackSheet = false },
            sheetState = sheetState,
            containerColor = CardSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "تعديل الذكاء الاصطناعي",
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ما الذي ترغب في تحسينه؟",
                    color = TextPrimary,
                    fontSize = 14.sp
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("السرعة زائدة", "اللون غير مناسب", "الصوت غير متناسق").forEach { feedback ->
                        ChoiceChip(
                            text = feedback,
                            isSelected = selectedFeedback == feedback,
                            onClick = { selectedFeedback = feedback },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedFeedback != null) goldGradient else SolidColor(Color(0xFF334155)))
                        .clickable(enabled = selectedFeedback != null) {
                            showFeedbackSheet = false
                            // TODO: trigger regeneration
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "تطبيق وإعادة التوليد",
                        color = if (selectedFeedback != null) DeepSlate else TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DeepSlate,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "معاينة الفيديو",
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "محرك قبس السينمائي",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "العودة",
                            tint = GoldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepSlate
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mock Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black)
                    .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
            ) {
                // Variant Selector Tab Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(4.dp)
                    ) {
                        listOf("نسخة 1", "نسخة 2", "نسخة 3").forEach { variant ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (selectedVariant == variant) GoldPrimary else Color.Transparent)
                                    .clickable { selectedVariant = variant }
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = variant,
                                    color = if (selectedVariant == variant) DeepSlate else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedVariant == variant) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                // Simulated Kinetic Typography Layer
                val variantText = when (selectedVariant) {
                    "نسخة 1" -> "لا تنتظر اللحظة المثالية،\nبل اصنعها."
                    "نسخة 2" -> "الوقت يمر سريعاً...\nاصنع أثرك الآن."
                    "نسخة 3" -> "النجاح يبدأ بخطوة،\nوالخطوة تبدأ بفكرة."
                    else -> ""
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.Crossfade(
                        targetState = variantText,
                        animationSpec = androidx.compose.animation.core.tween(500)
                    ) { text ->
                        Text(
                            text = text,
                            color = Color(0xFFFFFF00),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = GoldPrimary,
                                    blurRadius = 16f
                                )
                            )
                        )
                    }
                }

                // Play Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(2.dp, GoldPrimary, RoundedCornerShape(32.dp))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "تشغيل",
                        tint = GoldPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                // Timeline Slider and controls
                val progress = if (maxSeconds > 0) 1f - (currentSeconds.toFloat() / maxSeconds.toFloat()) else 1f
                val pointIndex = when {
                    progress < 0.33f -> 1
                    progress < 0.66f -> 2
                    else -> 3
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Points Row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(1, 2, 3).forEach { p ->
                            val isActive = pointIndex == p
                            val color = if (isActive) GoldPrimary else Color.White.copy(alpha = 0.5f)
                            Text(
                                text = "نقطة $p",
                                color = color,
                                fontSize = 10.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val mins = currentSeconds / 60
                        val secs = currentSeconds % 60
                        val timeStr = String.format(java.util.Locale.US, "%02d:%02d", mins, secs)
                        Text(text = timeStr, color = Color.White, fontSize = 12.sp)
                        Text(text = videoDuration, color = Color.White, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(GoldPrimary)
                        )
                    }
                }
            }
            
            // Quick Tweak Bar
            val scope = rememberCoroutineScope()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("📝 تعديل النصوص", "🎵 تغيير الموسيقى", "🎨 تعديل النمط البصري").forEach { action ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardSurface)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                            .clickable { 
                                val message = when (action) {
                                    "📝 تعديل النصوص" -> "جاري فتح حوار تعديل النصوص..."
                                    "🎵 تغيير الموسيقى" -> "تم تغيير المزاج الموسيقي بنجاح"
                                    else -> "تم تغيير أسلوب المونتاج بنجاح"
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = action,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            // Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerBadge(text = videoDuration)
                PlayerBadge(text = editingStyle)
                PlayerBadge(text = selectedRatio)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Regeneration Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .clickable { showFeedbackSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "إعادة التوليد مع تعديل الذكاء الاصطناعي",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary Download Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = GoldPrimary,
                            ambientColor = GoldPrimary
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(goldGradient)
                        .clickable { /* TODO: Download */ },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = DeepSlate
                        )
                        Text(
                            text = "تحميل الفيديو",
                            color = DeepSlate,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Secondary Row (Back to Editor)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
                        .background(Color.Transparent)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "تعديل المدخلات الأصلية",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ChoiceChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) GoldPrimary else CardSurface)
            .border(
                1.dp,
                if (isSelected) GoldPrimary else Color(0xFF334155),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) DeepSlate else TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun StyleCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CardSurface.copy(alpha = 0.8f) else CardSurface)
            .border(
                1.dp,
                if (isSelected) GoldPrimary else Color(0xFF334155),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                color = if (isSelected) GoldPrimary else TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            text = description,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PlayerBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = GoldPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun buildCinematicPrompt(userIdea: String, duration: String, style: String, aspectRatio: String, voiceOver: String, musicVibe: String): String {
    val styleInstructions = when {
        style.contains("3nvus") || style.contains("نيون") -> 
            "Visual Style: Dark moody aesthetic, high contrast. Typography: Kinetic glowing neon text synced to audio. Pacing: Fast viral transitions with ambient sound design."
        style.contains("سينمائي") -> 
            "Visual Style: Deep warm tones, elegant cinematic lighting, slow-motion B-roll, premium and smooth transitions."
        style.contains("وثائقي") -> 
            "Visual Style: Fast-paced documentary style, modern grid overlays, dynamic zooms, informative tone."
        else -> ""
    }

    val voiceInstruction = when {
        voiceOver.contains("عميق") -> "Voice Over Tone: Deep, Epic, authoritative."
        voiceOver.contains("حماسي") -> "Voice Over Tone: Energetic, fast-paced, motivating."
        voiceOver.contains("رسمي") -> "Voice Over Tone: Neutral, official, clear and documentary-style."
        else -> "Voice Over Tone: ${'$'}voiceOver"
    }
    
    val musicInstruction = when {
        musicVibe.contains("ملحمية") -> "Music Vibe: Epic, cinematic orchestration, swelling strings."
        musicVibe.contains("هادئة") -> "Music Vibe: Ambient, soft, calm, minimal interference."
        musicVibe.contains("غامضة") -> "Music Vibe: Suspenseful, dark synth, tension-building."
        else -> "Music Vibe: ${'$'}musicVibe"
    }

    return """
        [MASTER PROMPT]
        Duration: ${'$'}duration
        Aspect Ratio: ${'$'}aspectRatio
        
        ${'$'}styleInstructions
        ${'$'}voiceInstruction
        ${'$'}musicInstruction
        
        User Concept/Script:
        ${'$'}userIdea
    """.trimIndent()
}
