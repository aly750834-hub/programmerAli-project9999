import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """                        modifier = Modifier
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
                            }"""

replacement = """                        val scope = rememberCoroutineScope()
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
                            }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
