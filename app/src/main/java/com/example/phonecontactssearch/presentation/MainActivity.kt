package com.example.phonecontactssearch.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.phonecontactssearch.R
import com.example.phonecontactssearch.extension.isGranted
import com.example.phonecontactssearch.ui.theme.PhoneContactsSearchTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            PhoneContactsSearchTheme {
                val viewModel = koinViewModel<ContactViewModel>()
                var isGrantedContact by remember {
                    mutableStateOf(context.isGranted(Manifest.permission.READ_CONTACTS))
                }
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGrated ->
                        isGrantedContact = isGrated
                        if (isGrated)
                            viewModel.getContactList()
                    })

                Box(modifier = Modifier.fillMaxSize()) {
                    SearchView(modifier = Modifier.align(Alignment.TopCenter), viewModel)

                    Spacer(modifier = Modifier.height(16.dp))
                    if (!isGrantedContact) {
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .align(Alignment.BottomCenter)
                        ) {
                            Text(
                                text = "Request Contacts Permission",
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        viewModel.getContactList()
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(modifier: Modifier = Modifier, viewModel: ContactViewModel) {
    val searchText by viewModel.searchText.collectAsState()
    val contact by viewModel.contact.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(contact) { contact ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(45.dp),
                            model = contact.avatar,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.ic_launcher_background),
                            filterQuality = FilterQuality.High,
                        )

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = contact.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = contact.phoneNumber,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}