import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime
import android.util.Range
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulingScreen(navController: NavController, packageName: String, appName: String) {

    AppTheme {
        val calendarState = rememberUseCaseState()
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,

                ),
            selection = CalendarSelection.Dates {
                    dates ->  val selectedDates = dates.map { it.toString() } // Convert each date to string
                Log.d("Selected Dates", selectedDates.joinToString()) // Log the selected dates
            } )

        val clockStartState = rememberUseCaseState()
        ClockDialog(state = clockStartState, selection = ClockSelection.HoursMinutes { hours, minutes ->
            Log.d("Selected Start Time", "Hours: $hours, Minutes: $minutes") // Log the selected hours and minutes
        })

        val clockEndState = rememberUseCaseState()
        ClockDialog(state = clockEndState, selection = ClockSelection.HoursMinutes { hours, minutes ->
            Log.d("Selected End Time", "Hours: $hours, Minutes: $minutes") // Log the selected hours and minutes
        })

        val appBarHeight = 64.dp

        Surface(modifier = Modifier.padding(top = appBarHeight)) {
            Column {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
                    shape = RoundedCornerShape(22.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end = 22.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", modifier = Modifier.padding(6.dp))
                        Text(
                            text = "You could set the preferred time or dates to lock your child's apps here.",
                            modifier = Modifier
                                .padding(6.dp),
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp
                        )
                    }

                }

                Card(

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    shape = RoundedCornerShape(22.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end = 22.dp, top = 22.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.padding(6.dp))
                        Text(
                            text = "You are currently editing: $appName",
                            modifier = Modifier
                                .padding(6.dp),
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp
                        )
                    }

                }

                Card(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            text = "Lock App by Dates",
                            modifier = Modifier.padding(start = 22.dp, top = 22.dp, bottom = 4.dp),
                            textAlign = TextAlign.Justify,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "You can use this feature to lock $appName by dates. Start by clicking the Set Start Dates below. ",
                            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 12.dp),
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp
                        )
                        Button( modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp, start = 22.dp, end = 22.dp,), onClick = { calendarState.show() }) {
                            Text("Set Start Dates")
                        }
                    }
                }

                Card(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            text = "Lock App by Time",
                            modifier = Modifier.padding(start = 22.dp, top = 22.dp, bottom = 4.dp),
                            textAlign = TextAlign.Justify,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "You can use this feature to lock $appName by time. Start by clicking the Set Start Time below. ",
                            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 12.dp),
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 22.dp, end = 22.dp, bottom = 12.dp)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Button(onClick = { clockStartState.show() }, modifier = Modifier.weight(1f)) {
                                Text("Set Start Time")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = { clockEndState.show() }, modifier = Modifier.weight(1f)) {
                                Text("Set End Time")
                            }
                        }
                    }

                }






            }
        }
    }


}
