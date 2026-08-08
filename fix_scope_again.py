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
                listOf("📝 تعديل النصوص", "🎵 تغيير الموسيقى", "🎨 تعديل النمط البصري").forEach { action ->
                    Box(
                        val scope = rememberCoroutineScope()
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardSurface)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                            .clickable {"""

replacement = """            // Quick Tweak Bar
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
                            .clickable {"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
