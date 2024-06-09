package component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitReminderTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
) {
    Surface(
        color = Color.DarkGray,
        modifier = modifier
                .fillMaxWidth()
                .height(64.dp),
    ) {
        Row(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            onBackClick?.let {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back",
                    modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { it() }
                            .size(24.dp)
                )
            }
            Text(
                text = title,
                color = Color.White,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            action?.let {
                it()
            }
        }
    }
}
