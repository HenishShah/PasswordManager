package com.example.passwordmanager.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.roomdb.PasswordManagerDao
import com.example.passwordmanager.viewmodel.PasswordDetailsViewModel
import kotlinx.coroutines.launch

object ViewPasswordListScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordListItem(
        passwordDetailsModel: PasswordDetailsModel,
        viewModel: PasswordDetailsViewModel,
        dao: PasswordManagerDao
    ) {
        var showSheet by remember { mutableStateOf(false) }
        val bottomSheetState = rememberModalBottomSheetState()
        val coroutineScope = rememberCoroutineScope()

        var showSheetUpdate by remember { mutableStateOf(false) }
        val bottomSheetStateUpdate = rememberModalBottomSheetState()
        val coroutineScopeUpdate = rememberCoroutineScope()

        Card(
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(true) {
                    showSheet = true
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = passwordDetailsModel.accountType,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    fontSize = 18.sp,
                    color = Color(0xFF333333),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = "********",
                    color = Color(0xFF333333),
                    style = MaterialTheme.typography.labelLarge
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray,
                )
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

                ViewPasswordDetailsDialog.PasswordDetailBottomSheet(
                    passwordDetailsModel,
                    onEditClick = {
                        coroutineScopeUpdate.launch {
                            showSheetUpdate = true
                            showSheet = false
                            bottomSheetState.hide()
                        }
                    },
                    onDeleteClick = {

                        coroutineScopeUpdate.launch {
                            viewModel.setDeleteDataStateEvent(dao, passwordDetailsModel)
                            showSheet = false
                            bottomSheetState.hide()
                        }

                    }
                )

            }
        }

        LaunchedEffect(showSheetUpdate) {
            if (showSheetUpdate) bottomSheetState.show()
        }

        if (showSheetUpdate) {
            ModalBottomSheet(
                onDismissRequest = {
                    showSheetUpdate = false
                },
                containerColor = Color(0xFFF9F9F9),
                sheetState = bottomSheetStateUpdate,
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                AddOrUpdateBottomSheetDialog.UpdatePasswordDetailBottomSheet(viewModel, dao, passwordDetailsModel) {
                    coroutineScope.launch {
                        bottomSheetStateUpdate.hide()
                        showSheetUpdate = false
                    }
                }
            }
        }
    }
}