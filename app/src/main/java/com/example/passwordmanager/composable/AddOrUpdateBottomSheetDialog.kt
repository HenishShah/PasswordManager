package com.example.passwordmanager.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.roomdb.PasswordManagerDao
import com.example.passwordmanager.utils.EncryptDecrypt
import com.example.passwordmanager.viewmodel.PasswordDetailsViewModel

object AddOrUpdateBottomSheetDialog {
    @Composable
    fun AddPasswordDetailBottomSheet(
        passwordDetailsModel: PasswordDetailsViewModel,
        dao: PasswordManagerDao,
        onAddClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var accountName: String = ""
            var userName: String = ""
            var password: String = ""

            BorderedTextFields(
                label1 = "Account Name",
                label2 = "Username/ Email",
                label3 = "Password",
                onText1Change = {
                    accountName = it
                },
                onText2Change = {
                    userName = it
                },
                onText3Change = {
                    password = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    passwordDetailsModel.setInsertDataStateEvent(
                        dao,
                        PasswordDetailsModel(
                            accountType = accountName,
                            userName = userName,
                            password = password
                        )
                    )
                    onAddClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Add New Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    @Composable
    fun UpdatePasswordDetailBottomSheet(
        passwordDetailsViewModel: PasswordDetailsViewModel,
        dao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel,
        onAddClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var accountName: String = passwordDetailsModel.accountType
            var userName: String = passwordDetailsModel.userName
            var password: String = try {
                EncryptDecrypt().getDecryptedBody(
                    passwordDetailsModel.accountType,
                    passwordDetailsModel.password
                )
            } catch (e: Exception) {
                passwordDetailsModel.password
            }

            BorderedTextFields(
                label1 = "Account Name",
                label2 = "Username/ Email",
                label3 = "Password",
                onText1Change = {
                    accountName = it
                },
                onText2Change = {
                    userName = it
                },
                onText3Change = {
                    password = it
                },
                text1 = accountName,
                text2 = userName,
                text3 = password
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    passwordDetailsViewModel.setUpdateDataStateEvent(
                        dao,
                        PasswordDetailsModel(
                            accountType = accountName,
                            userName = userName,
                            password = password
                        )
                    )
                    onAddClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Update Account Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    @Composable
    fun BorderedTextFields(
        label1: String,
        label2: String,
        label3: String,
        onText1Change: (String) -> Unit,
        onText2Change: (String) -> Unit,
        onText3Change: (String) -> Unit,
        text1: String = "",
        text2: String = "",
        text3: String = "",
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BorderedTextField(label1, onText1Change, text1)
            Spacer(modifier = Modifier.height(16.dp))
            BorderedTextField(label2, onText2Change, text2)
            Spacer(modifier = Modifier.height(16.dp))
            BorderedTextField(label3, onText3Change, text3)
        }
    }

    @Composable
    fun BorderedTextField(
        label: String,
        onTextChange: (String) -> Unit,
        text: String = ""

    ) {
        var textFieldValue by remember { mutableStateOf(text) }

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onTextChange(it)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp
            ),
            placeholder = {
                Text(
                    text = label,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
            }
        )
    }
}