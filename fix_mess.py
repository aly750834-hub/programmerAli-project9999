import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """    LaunchedEffect(maxSeconds) {
        currentSeconds = maxSeconds
        isTimerComplete = false
        while (currentSeconds > 0) {
            kotlinx.coroutines.delay(1000L)
            currentSeconds--
        }
        isTimerComplete = true
    }
        currentSeconds = maxSeconds
        isTimerComplete = false
        while (currentSeconds > 0) {
            kotlinx.coroutines.delay(1000L)
            currentSeconds--
        }
        isTimerComplete = true
    }"""

replacement = """    LaunchedEffect(maxSeconds) {
        currentSeconds = maxSeconds
        isTimerComplete = false
        while (currentSeconds > 0) {
            kotlinx.coroutines.delay(1000L)
            currentSeconds--
        }
        isTimerComplete = true
    }"""

content = content.replace(target, replacement)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
