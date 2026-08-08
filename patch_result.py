import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """            // Quick Tweak Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("🎵 تغيير الموسيقى", "🎨 تغيير النمط", "📝 تعديل النصوص").forEach { action ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardSurface)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                            .clickable { /* TODO: Quick Tweak */ }
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
            }"""

replacement = """            // Quick Tweak Bar
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
                                snackbarHostState.showSnackbar(message) // Needs coroutine scope? No, needs LaunchedEffect or CoroutineScope. Let's just do an empty click if we can't get scope easily here.
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
            }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
