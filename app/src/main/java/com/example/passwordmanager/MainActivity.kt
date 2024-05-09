package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanager.composable.AddOrUpdateBottomSheetDialog.AddPasswordDetailBottomSheet
import com.example.passwordmanager.composable.AddOrUpdateBottomSheetDialog.UpdatePasswordDetailBottomSheet
import com.example.passwordmanager.composable.ViewPasswordDetailsDialog.PasswordDetailBottomSheet
import com.example.passwordmanager.composable.ViewPasswordListScreen.PasswordListItem
import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.roomdb.PasswordManagerDao
import com.example.passwordmanager.roomdb.PasswordManagerDatabase
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import com.example.passwordmanager.utils.DataState
import com.example.passwordmanager.utils.EncryptDecrypt
import com.example.passwordmanager.utils.notifyUser
import com.example.passwordmanager.viewmodel.PasswordDetailsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PasswordDetailsViewModel
    private lateinit var dao: PasswordManagerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[PasswordDetailsViewModel::class.java]
        dao = PasswordManagerDatabase.invoke(this).getPasswordManagerDao()

        setContent {
            PasswordManagerTheme {
                PasswordList(viewModel = viewModel, dao = dao)
            }
        }
        setObservers()
    }

    private fun setObservers() {
        viewModel.insertDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    //binding.loader.hide()
                    notifyUser(dataState.data)
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    //binding.loader.show()
                }
            }
        }

        viewModel.updateDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    viewModel.setPasswordDetailsListDataStateEvent(dao)
                    //binding.loader.hide()
                    notifyUser(dataState.data)
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    //binding.loader.show()
                }
            }
        }

        viewModel.deleteDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    //binding.loader.hide()
                    notifyUser(dataState.data)
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    //binding.loader.show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordList(
    modifier: Modifier = Modifier,
    viewModel: PasswordDetailsViewModel,
    dao: PasswordManagerDao
) {
    val getPasswordDetailsListDataState by viewModel.getPasswordDetailsListDataState.observeAsState()

    var showSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showSheet = true
                },
                containerColor = Color(0xFF3F7DE3),
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(innerPadding)
        ) {
            Text(
                text = "Password Manager",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = modifier.padding(horizontal = 10.dp, vertical = 20.dp)
            )

            HorizontalDivider(modifier = modifier.padding(bottom = 20.dp))

            getPasswordDetailsListDataState?.let { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        //binding.loader.hide()
                        if (dataState.data.size > 0) {
                            LazyColumn {
                                items(dataState.data.size) { password ->
                                    PasswordListItem(
                                        passwordDetailsModel = dataState.data[password],
                                        viewModel,
                                        dao
                                    )
                                }
                            }
                        } else {
                            Box (
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ){
                                Text(
                                    text = "No Data Found\nClick + to add data",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333),
                                    textAlign = TextAlign.Center,
                                    modifier = modifier.padding(horizontal = 10.dp, vertical = 20.dp)
                                )
                            }
                        }
                    }

                    is DataState.Error -> {
                        //notifyUser(dataState.message)
                    }

                    is DataState.Loading -> {
                        //binding.loader.show()
                    }
                }
            }

        }
    }

    LaunchedEffect(showSheet) {
        if (showSheet) bottomSheetState.show()
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            containerColor = Color(0xFFF9F9F9),
            sheetState = bottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            AddPasswordDetailBottomSheet(viewModel, dao) {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    showSheet = false
                }
            }
        }
    }
}