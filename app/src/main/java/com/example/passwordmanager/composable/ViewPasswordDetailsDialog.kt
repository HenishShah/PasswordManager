package com.example.passwordmanager.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.utils.EncryptDecrypt

object ViewPasswordDetailsDialog {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordDetailBottomSheet(
        passwordDetailsModel: PasswordDetailsModel,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit
    ) {
        var passwordVisible by remember { mutableStateOf(false) }
        val visualTransformation =
            if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF3F7DE3),
                fontSize = 20.sp,
            )
            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .background(color = Color(0xFFCBCBCB))
            )
            Text(
                text = "Account Type",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFCCCCCC),
                fontSize = 12.sp
            )
            Text(
                text = passwordDetailsModel.accountType,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF333333),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "UserName/ Email",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFCCCCCC),
                fontSize = 12.sp
            )
            Text(
                text = passwordDetailsModel.userName,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF333333),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Password",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFCCCCCC),
                fontSize = 12.sp
            )
            TextField(
                value = try {
                    EncryptDecrypt().getDecryptedBody(
                        passwordDetailsModel.accountType,
                        passwordDetailsModel.password
                    )
                } catch (e: Exception) {
                    passwordDetailsModel.password
                },
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                readOnly = true,
                textStyle = TextStyle(
                    color = Color(0xFF333333),
                    fontSize = 16.sp,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                visualTransformation = visualTransformation,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                Button(
                    onClick = { onEditClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text("Edit", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Button(
                    onClick = { onDeleteClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF04646)),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}