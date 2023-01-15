package com.skodevoff.wegojim

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skodevoff.wegojim.ui.theme.*
import org.json.JSONObject
import java.io.File

/* Exercise class */
data class Exercise(val name:String, val sets: Int, val reps:Int)

/* Workout class */
class WorkoutPlan(val name: String, private val exerciseList: List<Exercise>) {
    fun getExercise(index: Int): Exercise {
        return exerciseList[index]
    }

    fun exercisesCount(): Int {
        return exerciseList.size
    }
}

class MainActivity : ComponentActivity() {

    /* sample variables for MyWorkouts() */
    // private val exerciseList: List<Exercise> = listOf(Exercise("Exercise 1", 3, 10), Exercise("Exercise 2", 4, 8), Exercise("Exercise 3", 4, 6), Exercise("Exercise 4", 5, 5))
    // private val sampleWorkoutPlan = WorkoutPlan("Workout 1", exerciseList)
    // private val exerciseList2: List<Exercise> = listOf(Exercise("Exercise 2 1", 3, 10), Exercise("Exercise 2 2", 4, 8), Exercise("Exercise 2 3", 4, 6), Exercise("Exercise 2 4", 5, 5))
    // private val sampleWorkoutPlan2 = WorkoutPlan("Workout 2", exerciseList2)
    // private val exerciseList3: List<Exercise> = listOf(Exercise("Exercise 3 1", 3, 10), Exercise("Exercise 3 2", 4, 8), Exercise("Exercise 3 3", 4, 6), Exercise("Exercise 3 4", 5, 5))
    // private val sampleWorkoutPlan3 = WorkoutPlan("Workout 3", exerciseList3)
    // private val exerciseList4: List<Exercise> = listOf(Exercise("Exercise 4 1", 3, 10), Exercise("Exercise 4 2", 4, 8), Exercise("Exercise 4 3", 4, 6), Exercise("Exercise 4 4", 5, 5))
    // private val sampleWorkoutPlan4 = WorkoutPlan("Workout 4", exerciseList4)
    // private val sampleWorkoutList: List<WorkoutPlan> = listOf(sampleWorkoutPlan, sampleWorkoutPlan2, sampleWorkoutPlan3, sampleWorkoutPlan4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeGoJimTheme {
                // OneRepMaxCalculator()
                // MyWorkouts(this)
                // SaveAndLoad(this)
                // saveWorkout(sampleWorkoutPlan, this)
                // Test()
                NavigationController(this)
                // Log.d("SKDVF", loadWorkout("Workout 1", this).getExercise(3).name)
                // loadWorkout("Workout 1", this)
            }
        }
    }
}

@Composable
fun Test() {

    var workoutNameTextFieldValue by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
        ,
        color = Color.DarkGray
    ) {
        TextField(
            modifier = Modifier
            //    .padding(4.dp, 0.dp, 4.dp, 4.dp)
            ,
            value = workoutNameTextFieldValue,
            onValueChange = {
                workoutNameTextFieldValue = it
            },
            singleLine = true,
            placeholder = { Text(text = "Enter Workout Name")},
            label = { Text("Workout Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
            )
        )
    }
}

@Composable
fun NewWorkoutPage(navigationController: NavController, context: Context) {

    val workoutToJSON = JSONObject()

    var exerciseCount by rememberSaveable { mutableStateOf(1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        item {

            var workoutNameTextFieldValue by rememberSaveable { mutableStateOf("") }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color.DarkGray
            ) {
                TextField(
                    modifier = Modifier,
                        //.padding(4.dp, 0.dp, 4.dp, 4.dp),
                    value = workoutNameTextFieldValue,
                    onValueChange = {
                        workoutNameTextFieldValue = it
                        workoutToJSON.put("workoutName", it)
                    },
                    singleLine = true,
                    placeholder = { Text(text = "Enter Workout Name")},
                    label = { Text("Workout Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                    )
                )
            }
        }

        items(exerciseCount) { index ->

            var exerciseNameTextFieldValue by rememberSaveable { mutableStateOf("") }
            var exerciseSetsTextFieldValue by rememberSaveable { mutableStateOf("") }
            var exerciseRepsTextFieldValue by rememberSaveable { mutableStateOf("") }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color.DarkGray
            ) {

                Column {
                    /* exercise name text field */
                    TextField(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        value = exerciseNameTextFieldValue,
                        onValueChange = {
                            exerciseNameTextFieldValue = it
                            workoutToJSON.put("exercise_${index}_name", it)
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Enter Exercise Name")},
                        label = { Text("Name") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White
                        )
                    )

                    /* exercise sets text field */
                    TextField(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        value = exerciseSetsTextFieldValue,
                        onValueChange = {
                            exerciseSetsTextFieldValue = it
                            workoutToJSON.put("exercise_${index}_sets", it.toInt())
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Enter Exercise Sets")},
                        label = { Text("Sets") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White
                        )
                    )

                    /* exercise reps text field */
                    TextField(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        value = exerciseRepsTextFieldValue,
                        onValueChange = {
                            exerciseRepsTextFieldValue = it
                            workoutToJSON.put("exercise_${index}_reps", it.toInt())
                        },
                        singleLine = true,
                        placeholder = { Text(text = "Enter Exercise Reps")},
                        label = { Text("Reps") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White
                        )
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    OutlinedButton(
                        onClick = { exerciseCount += 1 }
                    ) {
                        Text(text = "ADD")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = {
                            if ((exerciseCount - 1) < 1) {
                                exerciseCount = 1
                            } else {
                                exerciseCount -= 1
                                workoutToJSON.put("exercise_${exerciseCount}_name", null)
                                workoutToJSON.put("exercise_${exerciseCount}_sets", null)
                                workoutToJSON.put("exercise_${exerciseCount}_reps", null)
                            }
                        }
                    ) {
                        Text(text = "REMOVE")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = {
                            workoutToJSON.put("exercisesCount", exerciseCount)
                            saveJSONWorkout(workoutToJSON, context)
                        }
                    ) {
                        Text(text = "SAVE")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = {
                            Log.d("SKDVF", workoutToJSON.toString())
                        }
                    ) {
                        Text(text = "LOG")
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationController(context: Context) {
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = "OneRepMaxCalculator") {
        composable("MyWorkouts") { MyWorkouts(navigationController, context) }
        composable("OneRepMaxCalculator") { OneRepMaxCalculator(navigationController) }
        composable("NewWorkoutPage") { NewWorkoutPage(navigationController, context) }
    }
}

fun loadWorkout(workoutName: String, context: Context): WorkoutPlan {
    val workoutFolder = File(context.filesDir, "workouts")
    val workoutFile = File(workoutFolder, "${workoutName}.txt")
    val workoutToJSON = JSONObject(workoutFile.readText())

    val exerciseList: ArrayList<Exercise> = ArrayList()

    for (i in 0 until workoutToJSON.get("exercisesCount") as Int) {
        val exercise = Exercise(
            workoutToJSON.get("exercise_${i}_name") as String,
            workoutToJSON.get("exercise_${i}_sets") as Int,
            workoutToJSON.get("exercise_${i}_reps") as Int
        )
        exerciseList.add(exercise)
    }
    return WorkoutPlan(workoutToJSON.get("workoutName").toString(), exerciseList)
}

fun saveJSONWorkout(workout: JSONObject, context: Context) {
    val workoutFolder = File(context.filesDir, "workouts")

    if (!workoutFolder.exists()) {
        /* mkdirs() will create all the folders of the provided path */
        /* mkdir() won't create the folder if one parent folder is missing */
        workoutFolder.mkdirs()
        Log.d("SKDVF", "Created ${workoutFolder.name} folder")
    } else {
        Log.d("SKDVF", "${workoutFolder.name} folder already exists")
    }

    val workoutFile = File(workoutFolder, "${workout.get("workoutName")}.txt")
    workoutFile.delete()
    workoutFile.appendText(workout.toString())
}

fun saveWorkout(workout: WorkoutPlan, context: Context) {
    val workoutToJSON = JSONObject()
    workoutToJSON.put("workoutName", workout.name)
    workoutToJSON.put("exercisesCount", workout.exercisesCount())

    for (i in 0 until workout.exercisesCount()) {
        workoutToJSON.put("exercise_${i}_name", workout.getExercise(i).name)
        workoutToJSON.put("exercise_${i}_sets", workout.getExercise(i).sets)
        workoutToJSON.put("exercise_${i}_reps", workout.getExercise(i).reps)
    }

    val workoutFolder = File(context.filesDir, "workouts")

    if (!workoutFolder.exists()) {
        /* mkdirs() will create all the folders of the provided path */
        /* mkdir() won't create the folder if one parent folder is missing */
        workoutFolder.mkdirs()
        Log.d("SKDVF", "Created ${workoutFolder.name} folder")
    } else {
        Log.d("SKDVF", "${workoutFolder.name} folder already exists")
    }

    val workoutFile = File(workoutFolder, "${workout.name}.txt")
    workoutFile.delete()
    workoutFile.appendText(workoutToJSON.toString())
}

@Composable
fun SaveAndLoad(context: Context) {
    val jsonObject = JSONObject()
    val test: List<String> = listOf("test1", "test2")
    jsonObject.put("workoutName", "sampleName")
    jsonObject.put("test", test)
    Log.d("SKDVF", jsonObject.toString())

    val data = jsonObject.toString()
    val path = context.filesDir

    val folder = File(path, "test")
    Log.d("SKDVF", folder.exists().toString())
    folder.mkdirs()
    Log.d("SKDVF", folder.exists().toString())

    val file = File(folder, "test.txt")
    file.delete()
    file.appendText(data)

    Log.d("SKDVF", file.readText())

    val sampleJsonObject = JSONObject(file.readText())
    Log.d("SKDVF", sampleJsonObject.get("workoutName").toString())
}

@Composable
fun OneRepMaxCalculator(navigationController: NavController) {

    /* weight variables */
    val weightRegex = Regex("^[0-9]{1,4}[.][0,5]\$")                                                /* Returns true if value begins within 1-4 digits (from 0 to 9), continues with a dot and ends with one digit (0 or 5) */
    var weightTextFieldValue by rememberSaveable { mutableStateOf("0.0") }
    var weight by rememberSaveable { mutableStateOf(0.0F) }
    var isWeightWrong by rememberSaveable { mutableStateOf(false) }

    /* reps variables */
    val repsRegex = Regex("^[0-9]{1,3}\$")                                                          /* Returns true if value is within 1 and 3 digits (from 0 to 9) */
    var repsTextFieldValue by rememberSaveable { mutableStateOf("0") }
    var reps by rememberSaveable { mutableStateOf(0) }
    var isRepsWrong by rememberSaveable { mutableStateOf(false) }

    /* main column */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {

        /* weight text field */
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            value = weightTextFieldValue,
            onValueChange = {
                weightTextFieldValue = it
                if (weightRegex.containsMatchIn(it)) {
                    weight = it.toFloat()
                    isWeightWrong = false
                } else {
                    weight = 0.0F
                    isWeightWrong = true
                }
            },
            isError = isWeightWrong,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Enter Weight")},
            label = { Text("Weight") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White
            )
        )

        /* reps text field */
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            value = repsTextFieldValue,
            onValueChange = {
                repsTextFieldValue = it
                if (repsRegex.containsMatchIn(it)) {
                    reps = it.toInt()
                    isRepsWrong = false
                } else {
                    reps = 0
                    isRepsWrong = true
                }
            },
            isError = isRepsWrong,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "Enter Reps") },
            label = { Text("Reps") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White
            )
        )

        /* 1RM text */
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            color = Color.White,
            text = "1RM: ${(weight * (1 + (reps.toFloat() / 30))).toInt()} kg")

        OutlinedButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
                navigationController.navigate("MyWorkouts") {
                    popUpTo("OneRepMaxCalculator")
                }
            }
        ) {
            Text(text = "My Workouts")
        }
    }
}

@Composable
fun MyWorkouts(navigationController: NavController, context: Context) {

    val workoutFolder = File(context.filesDir, "workouts")
    val list = remember { mutableStateListOf<WorkoutPlan>() }

    list.clear()
    workoutFolder.walkTopDown().forEach {
        if (it.nameWithoutExtension != "workouts") {
            val workout = loadWorkout(it.nameWithoutExtension, context)
            list.add(workout)
            // Log.d("SKDVF", "${it.nameWithoutExtension} | FIXMEASAP: MyWorkouts() - Doing this everytime users expands element")
        } else {
            // Log.d("SKDVF", "skipped")
        }
    }

    /* main column */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        itemsIndexed(list) { workout, _ ->
            var expanded by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkPurple)
                    .padding(4.dp)
                    .animateContentSize()
                    .clip(RoundedCornerShape(5.dp))
            ) {
                /* workout name and delete button */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(.90F)
                            .padding(4.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                expanded = !expanded
                            }
                            .clip(RoundedCornerShape(5.dp)),
                        color = LightPurple
                    ) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = list[workout].name,
                            color = Color.White
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                val workoutFile = File(workoutFolder, "${list[workout].name}.txt")
                                list.removeAt(workout)
                                workoutFile.delete()
                            },
                        imageVector = Icons.Filled.Delete,
                        tint = Color.Red,
                        contentDescription = "Favorite",
                    )
                }

                if (expanded) {
                    /* for every exercise in workout */
                    for (exercise in 0 until list[workout].exercisesCount()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = Purple
                        ) {
                            /* exercise name, reps and sets */
                            Column {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = list[workout].getExercise(exercise).name,
                                    color = Color.White
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = list[workout].getExercise(exercise).sets.toString(),
                                    color = Color.White
                                )
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = list[workout].getExercise(exercise).reps.toString(),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            /* spacer between every workout plan */
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        navigationController.navigate("NewWorkoutPage") {
                            popUpTo("MyWorkouts")
                        }
                    }
                ) {
                    Text(text = "ADD NEW")
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

/* VERSION STRING - Text(text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})") */

/* TODO: 1RM CALCULATOR */
    /* DONE: ENTER WEIGHT */
    /* DONE: ENTER REPS */
    /* DONE: CALCULATE 1RM */
    /* TODO: FIND BETTER 1RM FORMULA */
    /* FIXME: REPS CAN'T START WITH 0 */

/* TODO: WORKOUT PLAN PAGE */
    /* DONE: MY WORKOUTS */
        /* DONE: IMPROVE WORKOUT LIST ELEMENT */
            /* DONE: CREATE "WORKOUT" CLASS (WORKOUT NAME AND EXERCISES) */
            /* DONE: SHOW ONLY TITLE IN NON-EXPANDED ELEMENTS */
            /* DONE: EXPAND ELEMENT ON TOUCH AND SHOW ALL DETAILS */
            /* TODO: BETTER UI FOR WORKOUT ELEMENT */

    /* TODO: ADD PERSONAL WORKOUT PLAN */
        /* DONE: FIND A WAY TO SAVE AND LOAD USER'S WORKOUT PLAN ON PHONE STORAGE */
            /* FIXME: IMPROVE loadWorkout() TO BE MORE EFFICIENT */
        /* TODO: ADD AND DELETE WORKOUTS */
            /* DONE: ADD WORKOUT PAGE */
                /* FIXME: ADD REGEX FOR TEXT FIELDS */
            /* DONE: DELETE WORKOUT FUNCTION */

    /* TODO: NOTIFICATION ON WORKOUT DAYS */

/* TODO: NAVIGATION BETWEEN DIFFERENT PAGES */



/* PROJECT STARTED ON 10/11/2022
* 19/11/2022 (83) - weight and reps text fields, 1RM calculation (to optimize)
* 22/11/2022 (109) - started My Workout page: shows list element based on the list of strings given as argument
* 27/11/2022 (134) - finished basic my workout page: 4 workouts, when user clicks on the name it expands and shows all the exercises in it
* 01/12/2022 (225) - working basic load and save workouts system (functions), loads the workouts saved in workouts folder on phone storage
* 04/12/2022 (292) - working basic save workout page (NewWorkoutPage), saves the user's workout in phone storage and it appears in my workout page
* 09/12/2022 (342) - working basic delete workout, deletes the corresponding workout from the phone storage and removes it from the UI list
*/