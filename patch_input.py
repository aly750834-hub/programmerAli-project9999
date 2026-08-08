import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """        AppState.INPUT -> {
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
    ) { paddingValues ->"""

replacement = """        AppState.INPUT -> {
            if (showChatSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showChatSheet = false },
                    sheetState = chatSheetState,
                    containerColor = CardSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "محادثة الذكاء الاصطناعي",
                            color = GoldPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepSlate)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = if (inputText.isBlank()) {
                                    "يرجى كتابة فكرتك أولاً ليتمكن المساعد من تحليلها."
                                } else {
                                    "اقتراح تحسين لفكرتك:\n\n\"${inputText}\"\n\nيمكننا جعلها أكثر جاذبية بإضافة مؤثرات بصرية حماسية وتسريع الإيقاع في البداية لجذب الانتباه."
                                },
                                color = TextPrimary,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

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
        },
        bottomBar = {
            BottomAppBar(
                containerColor = CardSurface,
                contentColor = GoldPrimary
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TextButton(onClick = { showChatSheet = true }) {
                        Icon(Icons.Default.Chat, contentDescription = "Chat", tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat", color = GoldPrimary, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = { /* Simulated Preview */ }) {
                        Icon(Icons.Default.Preview, contentDescription = "Preview", tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Preview", color = GoldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
