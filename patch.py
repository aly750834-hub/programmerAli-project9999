import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """@Composable
fun MediaOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit        val maxSeconds = remember(videoDuration) { videoDuration.filter { it.isDigit() }.toIntOrNull() ?: 30 }"""

replacement = """@Composable
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
    videoDuration: String,
    editingStyle: String,
    onProcessingComplete: () -> Unit,
    onBack: () -> Unit
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
    
    val maxSeconds = remember(videoDuration) { videoDuration.filter { it.isDigit() }.toIntOrNull() ?: 30 }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
